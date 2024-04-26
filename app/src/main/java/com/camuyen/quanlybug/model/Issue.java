package com.camuyen.quanlybug.model;

public class Issue {
    String maVanDe;
    String tenVanDe;
    String ngayBatDau;
    String moTa;
    String dev;
    String dueDateDev;
    String test;
    String dueDateTest;
    String tienDoDev;
    String tienDoTest;
    String maNhanVien;
    String maDuAn;

    public Issue(String maVanDe, String tenVanDe, String ngayBatDau, String moTa, String dev, String dueDateDev, String test, String dueDateTest, String tienDoDev, String tienDoTest, String maNhanVien, String maDuAn) {
        this.maVanDe = maVanDe;
        this.tenVanDe = tenVanDe;
        this.ngayBatDau = ngayBatDau;
        this.moTa = moTa;
        this.dev = dev;
        this.dueDateDev = dueDateDev;
        this.test = test;
        this.dueDateTest = dueDateTest;
        this.tienDoDev = tienDoDev;
        this.tienDoTest = tienDoTest;
        this.maNhanVien = maNhanVien;
        this.maDuAn = maDuAn;
    }

    public Issue() {
    }

    public String getMaVanDe() {
        return maVanDe;
    }

    public void setMaVanDe(String maVanDe) {
        this.maVanDe = maVanDe;
    }

    public String getTenVanDe() {
        return tenVanDe;
    }

    public void setTenVanDe(String tenVanDe) {
        this.tenVanDe = tenVanDe;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getDueDateDev() {
        return dueDateDev;
    }

    public void setDueDateDev(String dueDateDev) {
        this.dueDateDev = dueDateDev;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getDueDateTest() {
        return dueDateTest;
    }

    public void setDueDateTest(String dueDateTest) {
        this.dueDateTest = dueDateTest;
    }

    public String getTienDoDev() {
        return tienDoDev;
    }

    public void setTienDoDev(String tienDoDev) {
        this.tienDoDev = tienDoDev;
    }

    public String getTienDoTest() {
        return tienDoTest;
    }

    public void setTienDoTest(String tienDoTest) {
        this.tienDoTest = tienDoTest;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getMaDuAn() {
        return maDuAn;
    }

    public void setMaDuAn(String maDuAn) {
        this.maDuAn = maDuAn;
    }
}
