package com.gamebai.tienlenmiennam.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.gamebai.tienlenmiennam.hotro.ThongSo;

/**
 * vẽ một viền đếm ngược hình vuông với điểm cho đầu ở trung điểm cạnh trên cùng
 */
public class VienDemNguoc {
    private Path path;
    private PointF diemDau;
    private float doDaiCanh;
    private float tocDo;
    private Paint paint;
    public VienDemNguoc(PointF diemDau, float doDaiCanh, Paint paint){
        this.diemDau=diemDau;
        this.doDaiCanh=doDaiCanh;
        path=new Path();
        tocDo=doDaiCanh*4/ ThongSo.TranDau.THOI_GIAN_CHO_CHON;
        this.paint=paint;
    }
    public VienDemNguoc(Paint paint){
        path=new Path();
        this.paint=paint;
    }
    public VienDemNguoc(float doDaiCanh,Paint paint){
        path=new Path();
        this.doDaiCanh=doDaiCanh;
        tocDo=doDaiCanh*4/ ThongSo.TranDau.THOI_GIAN_CHO_CHON;
        this.paint=paint;
    }
    /**
     * dẫn tạo đường vẽ lên path
     * @param thoiGianDemNguoc thời gian đếm ngược còn lại
     */
    public void tinhDuongVe(float thoiGianDemNguoc) {
        float doanDuongDiDuoc=tocDo*thoiGianDemNguoc;
        path.rewind();
        if(doanDuongDiDuoc>0){
            path.moveTo(diemDau.x,diemDau.y);
            PointF ngoiBut=new PointF(diemDau.x,diemDau.y);
            if(doanDuongDiDuoc>doDaiCanh/2) {
                path.lineTo(ngoiBut.x+doDaiCanh/2,ngoiBut.y);
                ngoiBut.x+=doDaiCanh/2;
                doanDuongDiDuoc-=doDaiCanh/2;
            }else{
                path.lineTo(ngoiBut.x+doanDuongDiDuoc,ngoiBut.y);
                ngoiBut.x+=doanDuongDiDuoc;
                doanDuongDiDuoc=0;
            }
            if(doanDuongDiDuoc>doDaiCanh) {
                path.lineTo(ngoiBut.x,ngoiBut.y+doDaiCanh);
                ngoiBut.y+=doDaiCanh;
                doanDuongDiDuoc-=doDaiCanh;
            }else{
                path.lineTo(ngoiBut.x,ngoiBut.y+doanDuongDiDuoc);
                ngoiBut.y+=doanDuongDiDuoc;
                doanDuongDiDuoc=0;
            }
            if(doanDuongDiDuoc>doDaiCanh) {
                path.lineTo(ngoiBut.x-doDaiCanh,ngoiBut.y);
                ngoiBut.x-=doDaiCanh;
                doanDuongDiDuoc-=doDaiCanh;
            }else{
                path.lineTo(ngoiBut.x-doanDuongDiDuoc,ngoiBut.y);
                ngoiBut.x-=doanDuongDiDuoc;
                doanDuongDiDuoc=0;
            }
            if(doanDuongDiDuoc>doDaiCanh){
                path.lineTo(ngoiBut.x,ngoiBut.y-doDaiCanh);
                ngoiBut.y-=doDaiCanh;
                doanDuongDiDuoc-=doDaiCanh;
            }else{
                path.lineTo(ngoiBut.x,ngoiBut.y-doanDuongDiDuoc);
                ngoiBut.y-=doanDuongDiDuoc;
                doanDuongDiDuoc=0;
            }
            if(doanDuongDiDuoc>0){
                path.lineTo(ngoiBut.x+doanDuongDiDuoc,ngoiBut.y);
                ngoiBut.x+=doanDuongDiDuoc;
                doanDuongDiDuoc=0;
            }
        }
    }
    public void ve(Canvas canvas){
        canvas.drawPath(path,paint);
    }

    public void setDiemDau(PointF diemDau) {
        this.diemDau = diemDau;
    }

    public void setDoDaiCanh(float doDaiCanh) {
        this.doDaiCanh = doDaiCanh;
        tocDo=doDaiCanh*4/ ThongSo.TranDau.THOI_GIAN_CHO_CHON;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }

    public PointF getDiemDau() {
        return diemDau;
    }

    public Path getPath() {
        return path;
    }

    public float getDoDaiCanh() {
        return doDaiCanh;
    }

    public float getTocDo() {
        return tocDo;
    }
}
