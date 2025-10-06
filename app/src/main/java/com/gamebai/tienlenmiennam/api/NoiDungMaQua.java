package com.gamebai.tienlenmiennam.api;

/**
 * body gửi lên api MaQua khi người chơi sử dụng mã quà
 */
public class NoiDungMaQua {
    private String MaQua;
    public NoiDungMaQua(String maQua) {
        MaQua = maQua;
    }
    public String getMaQua() {
        return MaQua;
    }
    public void setMaQua(String maQua) {
        MaQua = maQua;
    }
}
