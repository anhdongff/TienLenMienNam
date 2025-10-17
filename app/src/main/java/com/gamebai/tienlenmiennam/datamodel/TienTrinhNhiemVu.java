package com.gamebai.tienlenmiennam.datamodel;

public class TienTrinhNhiemVu {
    private boolean DaNhan;
    private int GiaTriHienTai;
    private int GiaTriYeuCau;
    private String Id;
    private String SuKienId; // Thêm trường SuKienId
    private String UserId; // Thêm trường UserId
    private String Loai;
    private String NoiDung; // Thêm trường Nội Dung
    private int PhanThuong;
    private String NgayTaoStr;

    // Bắt buộc có constructor rỗng cho Firestore
    public TienTrinhNhiemVu() {}

    // Constructor đầy đủ
    public TienTrinhNhiemVu(boolean daNhan, int giaTriHienTai, int giaTriYeuCau,
                            String id, String loai, int phanThuong, String ngayTaoStr,
                            String suKienId, String userId, String noiDung) {
        this.DaNhan = daNhan;
        this.GiaTriHienTai = giaTriHienTai;
        this.GiaTriYeuCau = giaTriYeuCau;
        this.Id = id;
        this.Loai = loai;
        this.PhanThuong = phanThuong;
        this.NgayTaoStr = ngayTaoStr;
        this.SuKienId = suKienId;
        this.UserId = userId;
        this.NoiDung = noiDung;
    }

    // Getter và Setter

    public String getSuKienId() {
        return SuKienId;
    }

    public void setSuKienId(String suKienId) {
        SuKienId = suKienId;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public boolean isDaNhan() {
        return DaNhan;
    }

    public void setDaNhan(boolean daNhan) {
        DaNhan = daNhan;
    }

    public int getGiaTriHienTai() {
        return GiaTriHienTai;
    }

    public void setGiaTriHienTai(int giaTriHienTai) {
        GiaTriHienTai = giaTriHienTai;
    }

    public int getGiaTriYeuCau() {
        return GiaTriYeuCau;
    }

    public void setGiaTriYeuCau(int giaTriYeuCau) {
        GiaTriYeuCau = giaTriYeuCau;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLoai() {
        return Loai;
    }

    public void setLoai(String loai) {
        Loai = loai;
    }

    public int getPhanThuong() {
        return PhanThuong;
    }

    public void setPhanThuong(int phanThuong) {
        PhanThuong = phanThuong;
    }

    public String getNgayTaoStr() {
        return NgayTaoStr;
    }

    public void setNgayTaoStr(String ngayTaoStr) {
        NgayTaoStr = ngayTaoStr;
    }
}
