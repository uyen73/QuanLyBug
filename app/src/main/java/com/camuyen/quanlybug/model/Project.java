package com.camuyen.quanlybug.model;

public class Project {
    String maDuAn;
    String maNhanVien;
    String tenQuanLy;
    String tenDuAn;
    String moTa;
    String ngayBatDau;

    public Project(String maDuAn, String maNhanVien, String tenQuanLy, String tenDuAn, String moTa, String ngayBatDau) {
        this.maDuAn = maDuAn;
        this.maNhanVien = maNhanVien;
        this.tenQuanLy = tenQuanLy;
        this.tenDuAn = tenDuAn;
        this.moTa = moTa;
        this.ngayBatDau = ngayBatDau;
    }

    public Project() {
    }

    public String getTenQuanLy() {
        return tenQuanLy;
    }

    public void setTenQuanLy(String tenQuanLy) {
        this.tenQuanLy = tenQuanLy;
    }

    public String getMaDuAn() {
        return maDuAn;
    }

    public void setMaDuAn(String maDuAn) {
        this.maDuAn = maDuAn;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenDuAn() {
        return tenDuAn;
    }

    public void setTenDuAn(String tenDuAn) {
        this.tenDuAn = tenDuAn;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }
}
