package com.gamebai.tienlenmiennam.datamodel;
import com.google.firebase.Timestamp; // nếu bạn dùng Firestore SDK Android
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.HashMap;

public class YeuCauHoTro implements Serializable {
    private String Id; // ID tài liệu trong Firestore
    private String NoiDung;
    private String PhanHoi;
    private Timestamp ThoiGianPhanHoi;
    private Timestamp ThoiGianTao;
    private String TieuDe;
    private int TrangThai;
    private String UserId;
    private String TenDangNhap; // Tên đăng nhập của người dùng

    @Override
    public String toString() {
        return "YeuCauHoTro{" +
                "Id='" + Id + '\'' +
                ", NoiDung='" + NoiDung + '\'' +
                ", PhanHoi='" + PhanHoi + '\'' +
                ", ThoiGianPhanHoi=" + ThoiGianPhanHoi +
                ", ThoiGianTao=" + ThoiGianTao +
                ", TieuDe='" + TieuDe + '\'' +
                ", TrangThai=" + TrangThai +
                ", UserId='" + UserId + '\'' +
                '}';
    }

    public String getTenDangNhap() {
        return TenDangNhap;
    }
    public void setTenDangNhap(String tenDangNhap) {
        TenDangNhap = tenDangNhap;
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }


    // 🔹 Bắt buộc cần constructor rỗng cho Firestore
    public YeuCauHoTro() {
    }

    // 🔹 Constructor đầy đủ
    public YeuCauHoTro(String noiDung, String phanHoi, Timestamp thoiGianPhanHoi,
                       Timestamp thoiGianTao, String tieuDe, int trangThai, String userId) {
        this.NoiDung = noiDung;
        this.PhanHoi = phanHoi;
        this.ThoiGianPhanHoi = thoiGianPhanHoi;
        this.ThoiGianTao = thoiGianTao;
        this.TieuDe = tieuDe;
        this.TrangThai = trangThai;
        this.UserId = userId;
    }

    // 🔹 Getter & Setter
    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        this.NoiDung = noiDung;
    }

    public String getPhanHoi() {
        return PhanHoi;
    }

    public void setPhanHoi(String phanHoi) {
        this.PhanHoi = phanHoi;
    }

    public Timestamp getThoiGianPhanHoi() {
        return ThoiGianPhanHoi;
    }

    public void setThoiGianPhanHoi(Timestamp thoiGianPhanHoi) {
        this.ThoiGianPhanHoi = thoiGianPhanHoi;
    }

    public Timestamp getThoiGianTao() {
        return ThoiGianTao;
    }

    public void setThoiGianTao(Timestamp thoiGianTao) {
        this.ThoiGianTao = thoiGianTao;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.TieuDe = tieuDe;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        this.TrangThai = trangThai;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Id", Id);
        map.put("NoiDung", NoiDung);
        map.put("PhanHoi", PhanHoi);
        map.put("ThoiGianPhanHoi", ThoiGianPhanHoi);
        map.put("ThoiGianTao", ThoiGianTao);
        map.put("TieuDe", TieuDe);
        map.put("TrangThai", TrangThai);
        map.put("UserId", UserId);
        map.put("TenDangNhap", TenDangNhap);
        return map;
    }
}