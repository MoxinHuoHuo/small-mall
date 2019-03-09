package com.mmall.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.sun.javafx.collections.MappingChange;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther lyd
 * @createDate 2019/2/27 23:35
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        // 所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        // 忽略在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return  obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("object转换成String错误！",e);
            return null;
        }
    }

    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return  obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("object转换成String错误！",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("String转换成Object错误！",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }

        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.warn("String转换成Object错误！",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);

        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("String转换成Object错误！",e);
            return null;
        }
    }

    // -------------------test-----------------
    public static void main(String[] args) {
        User u1 = new User();
        u1.setId(1);
        u1.setUsername("小明");
        u1.setEmail("916682404@qq.com");

        User u2 = new User();
        u2.setId(2);
        u2.setUsername("小wang");
        u2.setEmail("425420437@qq.com");

        /*List userList = new ArrayList();
        userList.add(u1);
        userList.add(u2);

        String userListString = JsonUtil.obj2StringPretty(userList);

        log.info("userList {}", userListString);
*/

        Map<String,User> userMap= Maps.newHashMap();
        userMap.put("学生1",u1);
        userMap.put("学生2",u2);
        String userMapString=JsonUtil.obj2String(userMap);
        log.info("userMapString {}", userMapString);


        //List<User> userListTemp=JsonUtil.string2Obj(userListString, List.class, User.class);

        Map<String,User> userMapTemp= JsonUtil.string2Obj(userMapString,Map.class,String.class,User.class);

        System.out.println("--end----");


    }

}
