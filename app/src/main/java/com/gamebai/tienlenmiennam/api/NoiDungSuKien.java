package com.gamebai.tienlenmiennam.api;

/**
 * body gửi lên api KiemTraVaKhoiTaoTienTrinhSuKien và NhanThuongSuKien
 */
public class NoiDungSuKien {
    private String SuKienId;
    private String NhiemVuId;
    public NoiDungSuKien(String suKienId, String nhiemVuId) {
        SuKienId = suKienId;
        NhiemVuId = nhiemVuId;
    }
    public NoiDungSuKien(){}
    public String getSuKienId() {
        return SuKienId;
    }
    public void setSuKienId(String suKienId) {
        SuKienId = suKienId;
    }
    public String getNhiemVuId() {
        return NhiemVuId;
    }
    public void setNhiemVuId(String nhiemVuId) {
        NhiemVuId = nhiemVuId;
    }
}
