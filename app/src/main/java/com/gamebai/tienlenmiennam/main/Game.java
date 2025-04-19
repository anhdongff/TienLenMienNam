package com.gamebai.tienlenmiennam.main;

import static com.gamebai.tienlenmiennam.hotro.ThongSo.TrangThaiGame.TRANG_THAI_CHOI_ONLINE;
import static com.gamebai.tienlenmiennam.hotro.ThongSo.TrangThaiGame.TRANG_THAI_CHOI_VOI_MAY;
import static com.gamebai.tienlenmiennam.hotro.ThongSo.TrangThaiGame.TRANG_THAI_FARM_DATA;
import static com.gamebai.tienlenmiennam.hotro.ThongSo.TrangThaiGame.TRANG_THAI_SANH_CHO;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.gamebai.tienlenmiennam.trangthaigame.ChoiOnline;
import com.gamebai.tienlenmiennam.trangthaigame.ChoiVoiMay;
import com.gamebai.tienlenmiennam.trangthaigame.FarmData;

/**
 * "bộ não" của trò chơi
 */
public class Game {
    private SurfaceHolder holder;
    private int trangThaiGame;
    private VongLapGame vongLapGame;
    private boolean tamDungVongLapGame;
    private MainActivity mainActivity;
    /**
     * Trạng thái game (chế độ chơi)
     */
    private ChoiVoiMay choiVoiMay;
    private FarmData farmData;
    public ChoiOnline choiOnline;
    public Game(SurfaceHolder holder,MainActivity mainActivity) {
        this.holder = holder;
        vongLapGame = new VongLapGame(this);
        trangThaiGame = TRANG_THAI_SANH_CHO;
        this.mainActivity=mainActivity;
        /**
         * cho vòng lặp game chạy
         */
        tamDungVongLapGame=true;
        batDauVongLapGame();
    }
    /**
     * cập nhật mọi thông số của game, dựa vào trạng thái game để cập nhật riêng
     * @param delta: độ trễ giữa 2 lần cập nhật game trước để bù vào lần cập nhật này
     */
    public void capNhatGame(double delta){
        boolean isCanvasNull= !holder.getSurface().isValid();
        switch (trangThaiGame){
            case TRANG_THAI_CHOI_VOI_MAY:
                choiVoiMay.capNhat(delta,isCanvasNull);
                break;
            case TRANG_THAI_FARM_DATA:
                farmData.capNhat(delta,isCanvasNull);
                break;
            case TRANG_THAI_CHOI_ONLINE:
                choiOnline.capNhat(delta,isCanvasNull);
                break;
        }
    }
    /**
     * vẽ lên màn hình
     */
    public void render(){
        /**
         * kiểm tra tra xem liệu bề mặt có hợp lệ hay đã bị huỷ do chạy nền
         */
        if(!holder.getSurface().isValid()) {
            return;
        }
        Canvas c = holder.lockCanvas();
        if (c == null){
            return;
        }
//        c.drawColor(Color.BLACK);
        switch (trangThaiGame){
            case TRANG_THAI_CHOI_VOI_MAY:
                choiVoiMay.render(c);
                break;
            case TRANG_THAI_FARM_DATA:
                farmData.render(c);
                break;
            case TRANG_THAI_CHOI_ONLINE:
                choiOnline.render(c);
                break;
        }
        holder.unlockCanvasAndPost(c);
    }

    /**
     * xử lý sự kiện chạm màn hình
     * @param event
     * @return
     */
    public boolean touchEvent(MotionEvent event){
        switch (trangThaiGame){
            case TRANG_THAI_SANH_CHO:
                break;
            case TRANG_THAI_CHOI_VOI_MAY:
                choiVoiMay.touchEvent(event);
                break;
            case TRANG_THAI_FARM_DATA:
                farmData.touchEvent(event);
                break;
            case TRANG_THAI_CHOI_ONLINE:
                choiOnline.touchEvent(event);
                break;
        }
        return true;
    }
    public int getTrangThaiGame() {
        return trangThaiGame;
    }

    /**
     * đặt lại trạng thái game
     * @param trangThaiGame
     * @param idPhong chỉ dành cho chế độ chơi online, bỏ trống nếu không cần thiết
     */
    public void setTrangThaiGame(int trangThaiGame,String idPhong) {
        this.trangThaiGame = trangThaiGame;
        switch (trangThaiGame){
            case TRANG_THAI_CHOI_VOI_MAY:
                if(choiVoiMay==null) choiVoiMay=new ChoiVoiMay(this,mainActivity);
                else choiVoiMay.taiLai();
                tamDungVongLapGame=false;
                break;
            case TRANG_THAI_FARM_DATA:
                if(farmData==null) farmData=new FarmData(this,mainActivity);
                else farmData.taiLai();
                tamDungVongLapGame=false;
                break;
            case TRANG_THAI_SANH_CHO:
                tamDungVongLapGame=true;
                mainActivity.vaoSanhCho();
                break;
            case TRANG_THAI_CHOI_ONLINE:
                choiOnline=new ChoiOnline(this,mainActivity);
                choiOnline.maPhong=idPhong;
                choiOnline.taiLai();
                tamDungVongLapGame=false;
                break;
        }
    }
    public void batDauVongLapGame(){
        if(vongLapGame==null) {
            vongLapGame=new VongLapGame(this);
        }
        vongLapGame.start();
    }
    public boolean isTamDungVongLapGame() {
        return tamDungVongLapGame;
    }

    /**
     * huỷ theo dõi người chơi online khi người chơi bị đăng xuất hoặc thoát trò chơi chủ động
     */
    public void huyTheoDoiNguoiChoiOnline() {
        if(choiOnline!=null)
            choiOnline.huyDangKyListener(false);
    }
    public void setTamDungVongLapGame(boolean tamDungVongLapGame){
        this.tamDungVongLapGame=tamDungVongLapGame;
    }
}
