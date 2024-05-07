package com.camuyen.quanlybug.model;

import java.time.LocalDate;
import java.util.Date;

public class Bugs {
    private String maBug;
    private String tenBug;
    private String moTaLoi;
    private String anh;
    private String cacBuoc;
    private String ketQuaMongMuon;
    private Date ngayXuatHien;
    private String trangThai;
    private String devFix;
    private String mucDoNghiemTrong;
    private String maVanDe;
    private String maDuAn;
    private String maNhanVien;
    private Date deadline;

    public Bugs(String maBug, String tenBug, String moTaLoi, String anh, String cacBuoc, String ketQuaMongMuon, Date ngayXuatHien, String trangThai, String devFix, String mucDoNghiemTrong, String maVanDe, String maDuAn, String maNhanVien, Date deadline) {
        this.maBug = maBug;
        this.tenBug = tenBug;
        this.moTaLoi = moTaLoi;
        this.anh = anh;
        this.cacBuoc = cacBuoc;
        this.ketQuaMongMuon = ketQuaMongMuon;
        this.ngayXuatHien = ngayXuatHien;
        this.trangThai = trangThai;
        this.devFix = devFix;
        this.mucDoNghiemTrong = mucDoNghiemTrong;
        this.maVanDe = maVanDe;
        this.maDuAn = maDuAn;
        this.maNhanVien = maNhanVien;
        this.deadline = deadline;
    }

    public Bugs() {
    }

    public String getMaBug() {
        return maBug;
    }

    public void setMaBug(String maBug) {
        this.maBug = maBug;
    }

    public String getTenBug() {
        return tenBug;
    }

    public void setTenBug(String tenBug) {
        this.tenBug = tenBug;
    }

    public String getMoTaLoi() {
        return moTaLoi;
    }

    public void setMoTaLoi(String moTaLoi) {
        this.moTaLoi = moTaLoi;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getCacBuoc() {
        return cacBuoc;
    }

    public void setCacBuoc(String cacBuoc) {
        this.cacBuoc = cacBuoc;
    }

    public String getKetQuaMongMuon() {
        return ketQuaMongMuon;
    }

    public void setKetQuaMongMuon(String ketQuaMongMuon) {
        this.ketQuaMongMuon = ketQuaMongMuon;
    }

    public Date getNgayXuatHien() {
        return ngayXuatHien;
    }

    public void setNgayXuatHien(Date ngayXuatHien) {
        this.ngayXuatHien = ngayXuatHien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getDevFix() {
        return devFix;
    }

    public void setDevFix(String devFix) {
        this.devFix = devFix;
    }

    public String getMucDoNghiemTrong() {
        return mucDoNghiemTrong;
    }

    public void setMucDoNghiemTrong(String mucDoNghiemTrong) {
        this.mucDoNghiemTrong = mucDoNghiemTrong;
    }

    public String getMaVanDe() {
        return maVanDe;
    }

    public void setMaVanDe(String maVanDe) {
        this.maVanDe = maVanDe;
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
