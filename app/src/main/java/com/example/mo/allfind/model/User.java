package com.example.mo.allfind.model;

import java.io.Serializable;

/**
 * Created by ZYF on 2017/3/5.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    private String userName;

    private String nickName;

    private String password;

    private Boolean remeberMe;

    private String email;

    private String confirmPassword;

    private String vcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRemeberMe() {
        return remeberMe;
    }

    public void setRemeberMe(Boolean remeberMe) {
        this.remeberMe = remeberMe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }
}
