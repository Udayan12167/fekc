package com.example.shiv.fekc.rest.response;

/**
 * Created by shiv on 30/3/16.
 */
public class RegisterUserResponse {

    private String oid;
    private int userStatusCode;

    public int getUserStatusCode() {
        return userStatusCode;
    }

    public void setUserStatusCode(int userStatusCode) {
        this.userStatusCode = userStatusCode;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
