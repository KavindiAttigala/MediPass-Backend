package com.sdgp.MediPass.DTO;

public class LoginRequest {
    private long mediId;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(long mediId, String password) {
        this.mediId = mediId;
        this.password = password;
    }

    public long getMediId() {
        return mediId;
    }

    public void setMediId(long mediId) {
        this.mediId = mediId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
