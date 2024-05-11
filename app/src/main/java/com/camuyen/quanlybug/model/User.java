package com.camuyen.quanlybug.model;

import androidx.annotation.NonNull;

public class User {
    String uid;
    String maNhanVien;
    String ten;
    String hoTen;
    String chucVu;
    String soDienThoai;
    String gmail;
    String matKhau;
    public User(){};

    public User(String uid, String maNhanVien, String ten, String hoTen, String chucVu, String soDienThoai, String gmail, String matKhau) {
        this.uid = uid;
        this.maNhanVien = maNhanVien;
        this.ten = ten;
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.soDienThoai = soDienThoai;
        this.gmail = gmail;
        this.matKhau = matKhau;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    @NonNull
    @Override
    public String toString() {
        return ten + " - " + chucVu;
    }
}
