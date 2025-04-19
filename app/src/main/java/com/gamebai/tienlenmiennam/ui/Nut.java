package com.gamebai.tienlenmiennam.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Nut {
    protected RectF hitbox;
    protected boolean duocBam,kichHoat;
    protected Bitmap anhNut;
    protected int idConTro;
    public Nut(Bitmap anhNut,float viTriX, float viTriY, float rong, float cao) {
        this.anhNut = anhNut;
        hitbox = new RectF(viTriX, viTriY, viTriX + rong, viTriY + cao);
        duocBam = false;
        idConTro=-1;
        kichHoat=false;
    }
    public Bitmap getAnhNut() {
        return anhNut;
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public void setHitbox(RectF hitbox) {
        this.hitbox = hitbox;
    }

    public void setAnhNut(Bitmap anhNut) {
        this.anhNut = anhNut;
    }

    public int getIdConTro() {
        return idConTro;
    }
    public void setIdConTro(int idConTro) {
        this.idConTro = idConTro;
    }
    public void resetIdConTro(){
        this.idConTro=-1;
    }
    public void setViTri(float viTriX, float viTriY) {
        hitbox.set(viTriX, viTriY, viTriX + anhNut.getWidth(), viTriY + anhNut.getHeight());
    }
    public PointF getViTri() {
        return new PointF(hitbox.left, hitbox.top);
    }
    /**
     * kiểm tra xem nút có được nhấn hay không
     * @return
     */
    public boolean isDuocBam() {
        return duocBam;
    }

    /**
     * kiểm tra xem vị trí con trỏ có nằm trong nút hay không
     * @param event vị trí con trỏ
     * @return
     */
    public boolean isEventHere(PointF event){
        return hitbox.contains(event.x, event.y);
    }
    /**
     * đặt trạng thái của nút
     * @param duocBam: true: đươc nhấn, false: chưa được nhấn
     */
    public void setDuocBam(boolean duocBam) {
        this.duocBam = duocBam;
    }
    public void ve(Canvas canvas){
        canvas.drawBitmap(anhNut,hitbox.left,hitbox.top,null);
    }
    public void ve(Canvas canvas, Paint paint){
        canvas.drawBitmap(anhNut,hitbox.left,hitbox.top,paint);
    }
    public void setKichHoat(boolean kichHoat) {
        this.kichHoat = kichHoat;
    }
    public boolean isKichHoat() {
        return kichHoat;
    }
}
