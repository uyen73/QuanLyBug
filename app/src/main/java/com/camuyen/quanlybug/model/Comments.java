package com.camuyen.quanlybug.model;

public class Comments {
    String maNhanVien;
    String noiDung;
    String anhDaiDien;

    public Comments(String maNhanVien, String noiDung, String anhDaiDien) {
        this.maNhanVien = maNhanVien;
        this.noiDung = noiDung;
        this.anhDaiDien = anhDaiDien;
    }

    public Comments() {
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }
}
