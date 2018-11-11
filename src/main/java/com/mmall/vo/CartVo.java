package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @auther lyd
 * @createDate 2018/11/10 20:52
 */
public class CartVo {

    //一对多的关系，一个购物车有很多商品
    private List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice;
    //是否已经都勾选
    private Boolean allChecked;
    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
