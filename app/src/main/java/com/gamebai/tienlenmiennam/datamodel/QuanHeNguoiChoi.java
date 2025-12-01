package com.gamebai.tienlenmiennam.datamodel;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.main.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuanHeNguoiChoi {
    private String id;
    private List<String> UserIds;
    private long ThoiGianTao;
    private int TrangThai;
    private String tenBanBe;
    private long Tien;
    private boolean online;
    private long lastInviteTime;
    private boolean inGame;
    DatabaseReference banBeReference;
    ValueEventListener banBeOnlineListener;
    public QuanHeNguoiChoi() {
        lastInviteTime = 0;
    }

    public long getLastInviteTime() {
        return lastInviteTime;
    }

    public void setLastInviteTime(long lastInviteTime) {
        this.lastInviteTime = lastInviteTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

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

    /**
     * lấy trạng thái giữa 2 người chơi
     * 1 - đã hấp nhận
     * 0 - đang chờ
     * @return
     */
    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        this.TrangThai = trangThai;
    }

    /**
     * chuyển sang hashmap để đẩy lên firestore
     * @return
     */
    public HashMap<String, Object> toHashMapForFirestore() {
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
    private void addListener(){
        banBeReference.addValueEventListener(banBeOnlineListener);
    }
    private void removeListener() {
        if(banBeReference!=null&&banBeOnlineListener!=null){
            banBeReference.removeEventListener(banBeOnlineListener);
        }
    }

    /**
     * bắt sự kiện thay đổi trạng thái online của ban be
     */
    public void startListener(){
        setBanBeOnlineListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.child("TrangThai").exists()) {
                        String trangThai = snapshot.child("TrangThai").getValue(String.class);
                        setOnline("online".equals(trangThai));
                    }
                    if(snapshot.child("Ten").exists()){
                        String ten=snapshot.child("Ten").getValue(String.class);
                        setTenBanBe(ten);
                    }
                    if(snapshot.child("Tien").exists()){
                        Long tien=snapshot.child("Tien").getValue(Long.class);
                        setTien(tien!=null?tien:0);
                    }
                    if(snapshot.child("inGame").exists()) {
                        Boolean inGame = snapshot.child("InGame").getValue(Boolean.class);
                        setInGame(inGame!=null?inGame:false);
                    }
                } catch (Exception e) {
                    Log.e("LOI", "Error: " + e.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LOI", "Error: " + error.getMessage());
//                Toast.makeText(MainActivity.getContext(),MainActivity.getContext().getText(R.string.loi_tai_du_lieu),Toast.LENGTH_SHORT).show();
            }
        });
        addListener();
    }

    /**
     * dừng lắng nghe sự kiện thay đổi trạng thái online của ban be
     */
    public void stopListener(){
        removeListener();
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
