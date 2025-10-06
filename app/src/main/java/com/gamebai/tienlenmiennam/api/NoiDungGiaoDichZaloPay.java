package com.gamebai.tienlenmiennam.api;

/**
 * body gửi lên server để tạo đơn giao dịch ZaloPay
 */
public class NoiDungGiaoDichZaloPay {
    private int amount;
    private String description;

    public NoiDungGiaoDichZaloPay(int soTien, String moTa) {
        this.amount = soTien;
        this.description = moTa;
    }
}
