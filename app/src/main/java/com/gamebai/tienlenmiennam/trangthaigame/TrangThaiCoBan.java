package com.gamebai.tienlenmiennam.trangthaigame;



import com.gamebai.tienlenmiennam.main.Game;

/**
 * các trạng thái game cần kế thừa lớp này
 */
public abstract class TrangThaiCoBan {
    protected Game game;
    public TrangThaiCoBan(Game game){
        this.game=game;
    }
    public Game getGame() {
        return game;
    }
}
