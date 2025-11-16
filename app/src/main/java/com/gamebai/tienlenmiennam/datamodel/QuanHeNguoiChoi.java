package com.gamebai.tienlenmiennam.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuanHeNguoiChoi {
    private List<String> UserIds;
    private long ThoiGianTao;
    private int TrangThai;
    private String tenBanBe;
    private long Tien;
    private boolean online;
    DatabaseReference banBeReference;
    ValueEventListener banBeOnlineListener;
    public QuanHeNguoiChoi() {}

    public long getTien() {
        return Tien;
    }

    public void setTien(long tien) {
        Tien = tien;
    }

    public List<String> getUserIds() {
        return UserIds;
    }

    public void setUserIds(List<String> userIds) {
        this.UserIds = userIds;
    }

    public long getThoiGianTao() {
        return ThoiGianTao;
    }

    public void setThoiGianTao(long thoiGianTao) {
        this.ThoiGianTao = thoiGianTao;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        this.TrangThai = trangThai;
    }
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("UserIds", UserIds);
        result.put("ThoiGianTao", ThoiGianTao);
        result.put("TrangThai", TrangThai);
        return result;
    }

    public String getTenBanBe() {
        return tenBanBe;
    }

    public void setTenBanBe(String tenBanBe) {
        this.tenBanBe = tenBanBe;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    public void copyFrom(QuanHeNguoiChoi other) {
        this.UserIds = other.UserIds;
        this.ThoiGianTao = other.ThoiGianTao;
        this.TrangThai = other.TrangThai;
    }

    public DatabaseReference getBanBeReference() {
        return banBeReference;
    }

    public void setBanBeReference(DatabaseReference banBeReference) {
        this.banBeReference = banBeReference;
    }

    public ValueEventListener getBanBeOnlineListener() {
        return banBeOnlineListener;
    }

    public void setBanBeOnlineListener(ValueEventListener banBeOnlineListener) {
        this.banBeOnlineListener = banBeOnlineListener;
    }
    public void addLisener(){
        banBeReference.addValueEventListener(banBeOnlineListener);
    }
    public void removeListener() {
        if(banBeReference!=null&&banBeOnlineListener!=null){
            banBeReference.removeEventListener(banBeOnlineListener);
        }
    }

    @Override
    public String toString() {
        return "QuanHeNguoiChoi{" +
                "UserIds=" + Arrays.toString(UserIds.toArray()) +
                ", ThoiGianTao=" + ThoiGianTao +
                ", TrangThai=" + TrangThai +
                ", tenBanBe='" + tenBanBe + '\'' +
                ", Tien=" + Tien +
                ", online=" + online +
                '}';
    }
}
