package com.camuyen.quanlybug.model;

import androidx.annotation.NonNull;

import java.util.Date;

public class Project {
    String maDuAn;
    String maQuanLy;
    String tenQuanLy;
    String tenDuAn;
    String moTa;
    Date ngayBatDau;
    String trangThai;

    public Project(String maDuAn, String maQuanLy, String tenQuanLy, String tenDuAn, String moTa, Date ngayBatDau, String trangThai) {
        this.maDuAn = maDuAn;
        this.maQuanLy = maQuanLy;
        this.tenQuanLy = tenQuanLy;
        this.tenDuAn = tenDuAn;
        this.moTa = moTa;
        this.ngayBatDau = ngayBatDau;
        this.trangThai = trangThai;
    }

    public Project() {
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaQuanLy() {
        return maQuanLy;
    }

    public void setMaQuanLy(String maQuanLy) {
        this.maQuanLy = maQuanLy;
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

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    @NonNull
    @Override
    public String toString() {
        return tenDuAn;
    }
}
