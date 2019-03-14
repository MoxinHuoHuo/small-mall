package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUservice")
public class UserServiceImpl implements IUserService{

    @Autowired
    UserMapper userMapper;

    //用户登录
    @Override
    public ServerResponse<User> login(String userName, String password) {
        int resultCount=userMapper.checkUserName(userName);

        if (resultCount ==0){
            return ServerResponse.createByERROR("用户不存在！");
        }

        //将passWord转为MD5加密
        String passwordMD5=MD5Util.MD5EncodeUtf8(password);

        User user=userMapper.selectLogin(userName,passwordMD5);

        if (user==null){
            return ServerResponse.createByERROR("账号/密码错误！");
        }

        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登陆成功！",user);
    }

    //处理注册的逻辑
    @Override
    public ServerResponse<String> register(User user){
        //调用该账号是否已经存在的接口
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;

        }
        //调用该email是否被注册的接口
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //将密码进行MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByERROR("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    //检查用户名是被注册，Email是否被注册
    @Override
    public ServerResponse<String> checkValid(String str,String type){
        if(StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUserName(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByERROR("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByERROR("email已存在");
                }
            }
        }else{
            return ServerResponse.createByERROR(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    //获取用户对应的密保问题
    @Override
    public ServerResponse<String> selectQuestion(String username){

        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByERROR("用户不存在");
        }
        String question = userMapper.selectQuestionByUserName(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByERROR("找回密码的问题是空的");
    }

    //检测用户输入的密保问题的答案是否正确
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明问题及问题答案是这个用户的,并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            // 将产生的token缓存到本地中
            // TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);

            // 将产生的token缓存在redis中
            RedisPoolUtil.setEx(Const.TOKEN_PREFIX+username, Const.TokenExpireTime.TOKEN_EXPIRE_TIME, forgetToken);

            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByERROR("问题的答案错误");
    }

    //将前端传过来的token与本地缓存的token比较，不一样则更改密码失败
    @Override
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByERROR("参数错误,token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            // 用户不存在
            return ServerResponse.createByERROR("用户不存在");
        }
        // String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        String token = RedisPoolUtil.get(Const.TOKEN_PREFIX+username);

        if(StringUtils.isBlank(token)){
            return ServerResponse.createByERROR("token无效或者过期");
        }

        if(StringUtils.equals(forgetToken,token)){
            String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUserName(username,md5Password);

            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByERROR("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByERROR("修改密码失败");
    }

    //在线修改密码
    @Override
    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越。所以要校验一下这个用户的旧密码,一定要指定是这个用户.如果不指定id,可以通过彩虹表进行不断的尝试，获取结果;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByERROR("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByERROR("密码更新失败");
    }

    //更新用户数据并把更新后的数据保存到session当中
    @Override
    public ServerResponse<User> updateInformation(User user){
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return ServerResponse.createByERROR("email已经被其他账号注册！");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);

        if(updateCount <= 0){
            return ServerResponse.createByERROR("更新个人信息失败");
        }

        //用户数据更新完毕后，需要重新获取用户的数据
        User currentUser=userMapper.selectByPrimaryKey(user.getId());

        if (currentUser==null){
            return ServerResponse.createByERROR("更新个人信息，重新获取个人信息失败！");
        }
        currentUser.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("更新个人信息成功！",currentUser);
    }

    //获取个人详细信息
    @Override
    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByERROR("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);

    }

    //backend

    //检测是否管理员
    @Override
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByERROR();
    }
}
