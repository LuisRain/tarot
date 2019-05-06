package com.hy.entity.base;

/**
 * @author sren
 * @create 2018-12-20 上午9:54
 **/

public class AppUser {

    private String userName;

    private String oldPassword;

    private String newPasswprd;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPasswprd() {
        return newPasswprd;
    }

    public void setNewPasswprd(String newPasswprd) {
        this.newPasswprd = newPasswprd;
    }
}
