package com.camuyen.quanlybug.model;

public class Devices {
    String gmail;
    String token;
    String maNhanVien;

    public Devices(String gmail, String token, String maNhanVien) {
        this.gmail = gmail;
        this.token = token;
        this.maNhanVien = maNhanVien;
    }

    public Devices() {
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
}
