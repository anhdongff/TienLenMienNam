package com.gamebai.tienlenmiennam.main;

import com.gamebai.tienlenmiennam.hotro.ThongSo;

public class VongLapGame implements Runnable{
    private Thread gamethread;
    private Game game;
    public VongLapGame(Game game){
        gamethread=new Thread(this);
        this.game = game;
    }
    /**
     *
     */
    @Override
    public void run() {
        /**
         * fps checker
         */
//        long lastCheckTime=System.currentTimeMillis();
//        int fps=0;
        long lastDelta;
        long nanoSec=1_000_000_000;
        double delta= ThongSo.HoatAnhLaBaiDiChuyen.KHOANG_CACH_THOI_GIAN_GIUA_2_FRAME;
        while(true){
            if(!game.isTamDungVongLapGame()) {
                /**
                 * handle the lag: bằng cách tính chênh lệch thời gian giữa 2
                 * frame để game chạy đúng tốc độ, nếu chênh lệch lớn hơn so
                 * với thời gian cho phép giữa 1 frame thì game sẽ tăng khoảng
                 * cách di chuyển của vật thể, nếu chênh lệch nhỏ hơn thì game
                 * sẽ giảm khoảng cách di chuyển của vật thể
                 */
//                if(game.getTrangThaiGame()==ThongSo.TrangThaiGame.TRANG_THAI_CHOI_ONLINE
//                    &&!game.daTaiDuLieuChoiOnline()) continue;
                lastDelta=System.nanoTime();
                game.capNhatGame(delta);
                game.render();
                double timeSinceLastDelta=System.nanoTime()-lastDelta;
                delta=timeSinceLastDelta/nanoSec;//->đơn vị là giây
//            fps++;
//            if(System.currentTimeMillis()-lastCheckTime>=1000) {
//                lastCheckTime = System.currentTimeMillis();
//                System.out.println("FPS: " + fps);
//                fps = 0;
//            }
            }
        }
    }
    public void start(){
        if(!gamethread.isAlive()&&gamethread!=null) {
            gamethread.start();
        }
    }
}
