package com.gamebai.tienlenmiennam.api;

public class NoiDungGiaoDichZaloPay {
    private int amount;
    private String description;

    public NoiDungGiaoDichZaloPay(int soTien, String moTa) {
        this.amount = soTien;
        this.description = moTa;
    }
}
