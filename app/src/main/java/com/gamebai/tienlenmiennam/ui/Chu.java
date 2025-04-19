package com.gamebai.tienlenmiennam.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * cung cấp các phương thức để phục vụ việc vẽ chữ lên canvas (vẽ trong 1 dòng)
 */
public class Chu {
    private String noiDung;
    private Paint paint;
    private RectF hitBox;

    /**
     * tạo đối tượng mới
     * @param noiDung: nội dung muốn vẽ
     * @param paint: paint để vẽ chữ. PHẢI ĐẶT CỠ CHỮ VÀ CĂN LỀ VÌ CỠ CHỮ MẶC ĐỊNH CỦA PAINT LÀ 0 CĂN LỀ MẶC ĐỊNH VỀ BÊN TRÁI
     */
    public Chu(String noiDung, Paint paint) {
        this.noiDung = noiDung;
        this.paint = paint;
        Rect temp=new Rect();
        paint.getTextBounds(noiDung, 0, noiDung.length(), temp);
        hitBox=new RectF(temp);
    }
    public String getNoiDung() {
        return noiDung;
    }
    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
        Rect temp=new Rect();
        paint.getTextBounds(noiDung, 0, noiDung.length(), temp);
        hitBox.set(hitBox.left,hitBox.bottom-temp.height(),hitBox.left+temp.width(),hitBox.bottom);
    }
    public void setPaint(Paint paint) {
        this.paint = paint;
    }
    public Paint getPaint() {
        return paint;
    }

    /**
     *
     * @return trả về hitbox của string sẽ được vẽ
     */
    public RectF getHitBox() {
        return hitBox;
    }
    public void setViTri(float x, float y){
        hitBox.set(x,y-hitBox.height(),x+hitBox.width(),y);
    }

    /**
     * vị trí bắt đầu vẽ chữ là từ trái sang và từ dưới chân chữ (đường cơ bản) lên
     * @return
     */
    public PointF getViTri(){
        return new PointF(hitBox.left,hitBox.bottom);
    }

    /**
     * vẽ chữ lên canvas với vị trí được lưu trong hitbox
     * @param canvas
     */
    public void ve(Canvas canvas){
        canvas.drawText(noiDung,hitBox.left,hitBox.bottom,paint);
//        Paint temp=new Paint();
//        temp.setColor(paint.getColor());
//        temp.setStyle(Paint.Style.STROKE);
//        temp.setStrokeWidth(2);
//        canvas.drawRect(hitBox,temp);
    }
}
