package com.camuyen.quanlybug.model;

import androidx.annotation.NonNull;

public class Jobs {
    private String maCongViec;
    private String maNhanVien;
    private String maVanDe;

    public Jobs(String maCongViec, String maNhanVien, String maVanDe) {
        this.maCongViec = maCongViec;
        this.maNhanVien = maNhanVien;
        this.maVanDe = maVanDe;
    }

    public Jobs() {
    }

    public String getMaCongViec() {
        return maCongViec;
    }

    public void setMaCongViec(String maCongViec) {
        this.maCongViec = maCongViec;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getMaVanDe() {
        return maVanDe;
    }

    public void setMaVanDe(String maVanDe) {
        this.maVanDe = maVanDe;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
