package com.gamebai.tienlenmiennam.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuKien {
    private String Id;
    private int Loai;
    private String MoTa;
    private String TieuDe;
    private int TrangThai;
    private long ThoiGianBatDau;
    private long ThoiGianKetThuc;
    private Map<String, TienTrinhNhiemVu> TienTrinhNhiemVu=new HashMap<>(); // Sử dụng Map để linh hoạt hơn
    private List<String> NhiemVuIds=new ArrayList<>(); // Danh sách ID nhiệm vụ liên quan
    // 🔹 Bắt buộc cần constructor rỗng để Firestore có thể tự map dữ liệu
    public SuKien() {
    }
    public List<String> taoDanhSachNhiemVuIds() {
        NhiemVuIds.clear();
        if (TienTrinhNhiemVu != null) {
            NhiemVuIds.addAll(TienTrinhNhiemVu.keySet());
        }
        return NhiemVuIds;
    }
    // 🔹 Constructor đầy đủ để bạn khởi tạo thủ công khi cần
    public SuKien(String id, int loai, String moTa, String tieuDe, int trangThai) {
        this.Id = id;
        this.Loai = loai;
        this.MoTa = moTa;
        this.TieuDe = tieuDe;
        this.TrangThai = trangThai;
    }
    public void copyInfoFrom(SuKien other) {
        this.Id = other.Id;
        this.Loai = other.Loai;
        this.MoTa = other.MoTa;
        this.TieuDe = other.TieuDe;
        this.TrangThai = other.TrangThai;
    }
    // 🔹 Getter & Setter

    public long getThoiGianBatDau() {
        return ThoiGianBatDau;
    }

    public void setThoiGianBatDau(long thoiGianBatDau) {
        ThoiGianBatDau = thoiGianBatDau;
    }

    public long getThoiGianKetThuc() {
        return ThoiGianKetThuc;
    }

    public void setThoiGianKetThuc(long thoiGianKetThuc) {
        ThoiGianKetThuc = thoiGianKetThuc;
    }

    public List<String> getNhiemVuIds() {
        return NhiemVuIds;
    }

    public void setNhiemVuIds(List<String> nhiemVuIds) {
        NhiemVuIds = nhiemVuIds;
    }

    public Map<String, TienTrinhNhiemVu> getTienTrinhNhiemVu() {
        return TienTrinhNhiemVu;
    }

    public void setTienTrinhNhiemVu(Map<String, TienTrinhNhiemVu> tienTrinhNhiemVu) {
        TienTrinhNhiemVu = tienTrinhNhiemVu;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getLoai() {
        return Loai;
    }

    public void setLoai(int loai) {
        Loai = loai;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        TieuDe = tieuDe;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    @Override
    public String toString() {
        String result = "SuKien{" +
                "Id='" + Id + '\'' +
                ", Loai=" + Loai +
                ", MoTa='" + MoTa + '\'' +
                ", TieuDe='" + TieuDe + '\'' +
                ", TrangThai=" + TrangThai +
                ", TienTrinhNhiemVu=" + TienTrinhNhiemVu +
                ", NhiemVuIds=" + NhiemVuIds +
                '}';
        for (Map.Entry<String, TienTrinhNhiemVu> entry : TienTrinhNhiemVu.entrySet()) {
            result += "\n  NhiemVuId: " + entry.getKey() + ", TienTrinhNhiemVu: " + entry.getValue();
        }
        return result;
    }
}
