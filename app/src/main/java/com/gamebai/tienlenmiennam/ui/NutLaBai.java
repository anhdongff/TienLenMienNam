package com.gamebai.tienlenmiennam.ui;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.gamebai.tienlenmiennam.hotro.ThongSo;
import com.gamebai.tienlenmiennam.thucthe.BoBai;
import com.gamebai.tienlenmiennam.thucthe.LaBai;

public class NutLaBai extends Nut{
    private LaBai laBai;
    private boolean kickHoat;
    private BoBai.ChiSoSoHuu chiSoSoHuu;
    /**
     * các thuộc tính phục vụ vẽ hoạt ảnh chia bài
     */
    private PointF diemDich;
    private float heSoGoc;
    private float tocDoQuay;
    private float gocQuay;
    private float tocDo;
    private boolean daDenDich;
    public NutLaBai(LaBai laBai, float viTriX, float viTriY, BoBai.ChiSoSoHuu chiSoSoHuu) {
        super(laBai.getHinhAnh(), viTriX, viTriY, laBai.getHinhAnh().getWidth(), laBai.getHinhAnh().getHeight());
        this.laBai = laBai;
        this.chiSoSoHuu=chiSoSoHuu;
        //
        diemDich=new PointF(chiSoSoHuu.getDichX(),chiSoSoHuu.getDichY());
        if(hitbox.left!=diemDich.x) heSoGoc=Math.abs((diemDich.y-hitbox.top)/(diemDich.x-hitbox.left));
        else heSoGoc=0;
        tocDo= (float) Math.sqrt((hitbox.left-diemDich.x)*(hitbox.left-diemDich.x)+(hitbox.top-diemDich.y)*(hitbox.top-diemDich.y))/ ThongSo.HoatAnhLaBaiDiChuyen.SO_FRAME;
//        System.out.println(laBai.getSo()+" "+laBai.getChat()+" "+tocDo+" "+ chiSoSoHuu.getChiSo()
//                +" "+hitbox.left+" "+hitbox.top+" "+diemDich.x+" "+diemDich.y);
        tocDoQuay= (float) 180 / ThongSo.HoatAnhLaBaiDiChuyen.SO_FRAME;
        gocQuay=0;
        daDenDich=false;
        //
        kickHoat =false;
//        System.out.println(diemDich.x+" "+diemDich.y+" "+chiSoSoHuu.getChiSo()+" "+heSoGoc);
    }
    public NutLaBai(LaBai laBai, float viTriX, float viTriY,PointF diemDich){
        super(laBai.getHinhAnh(), viTriX, viTriY, laBai.getHinhAnh().getWidth(), laBai.getHinhAnh().getHeight());
        this.laBai = laBai;
        //
        this.diemDich=new PointF(diemDich.x,diemDich.y);
        if(!diemDich.equals(hitbox.left,hitbox.top)){
            if(hitbox.left!=this.diemDich.x) heSoGoc=Math.abs((this.diemDich.y-hitbox.top)/(this.diemDich.x-hitbox.left));
            else heSoGoc=0;
            tocDo= (float) Math.sqrt((hitbox.left-this.diemDich.x)*(hitbox.left-this.diemDich.x)+(hitbox.top-this.diemDich.y)*(hitbox.top-this.diemDich.y))/ ThongSo.HoatAnhLaBaiDiChuyen.SO_FRAME;
            tocDoQuay= (float) 180 / ThongSo.HoatAnhLaBaiDiChuyen.SO_FRAME;
            gocQuay=0;
            daDenDich=false;
        }else{
            daDenDich=true;
        }
        //
        kickHoat =false;
    }
    public NutLaBai(LaBai laBai, float viTriX, float viTriY){
        super(laBai.getHinhAnh(), viTriX, viTriY, laBai.getHinhAnh().getWidth(), laBai.getHinhAnh().getHeight());
        this.laBai = laBai;
        daDenDich=true;
    }
    public NutLaBai(LaBai laBai, float viTriX, float viTriY,boolean kickHoat){
        super(laBai.getHinhAnh(), viTriX, viTriY, laBai.getHinhAnh().getWidth(), laBai.getHinhAnh().getHeight());
        this.laBai = laBai;
        this.kickHoat=kickHoat;
        daDenDich=true;
    }
    public boolean isKickHoat() {
        return kickHoat;
    }
    public void setKickHoat(boolean kickHoat) {
        this.kickHoat = kickHoat;
    }
    public BoBai.ChiSoSoHuu getChiSoSoHuu() {
        return chiSoSoHuu;
    }

    public PointF getDiemDich() {
        return diemDich;
    }
    public void setDiemDich(PointF diemDich) {
        this.diemDich = new PointF(diemDich.x,diemDich.y);
        if(diemDich.equals(hitbox.left,hitbox.top)) return;
        if(hitbox.left!=diemDich.x) heSoGoc=Math.abs((diemDich.y-hitbox.top)/(diemDich.x-hitbox.left));
        else heSoGoc=0;
        tocDo= (float) Math.sqrt((hitbox.left-diemDich.x)*(hitbox.left-diemDich.x)+(hitbox.top-diemDich.y)*(hitbox.top-diemDich.y))/
                ThongSo.HoatAnhLaBaiDiChuyen.SO_FRAME;
        tocDoQuay= (float) 180 / ThongSo.HoatAnhLaBaiDiChuyen.SO_FRAME;
        gocQuay=0;
        daDenDich=false;
//        if(heSoGoc==0&&(hitbox.left!=diemDich.x)&&(hitbox.top!=diemDich.y)){
//            System.out.println("nuuh" + hitbox.left + " " + hitbox.top + " " + diemDich.x + " " + diemDich.y);
//        }
    }
    public void setViTri(PointF viTri){
        hitbox.left=viTri.x;
        hitbox.top=viTri.y;
        hitbox.right=hitbox.left+laBai.getHinhAnh().getWidth();
        hitbox.bottom=hitbox.top+laBai.getHinhAnh().getHeight();
    }

    /**
     * thay đổi vị trí đồng thời kiểm tra liệu lá bài đã đến đích
     * @param viTri
     * @return
     */
    public boolean setViTriKiemTraDich(PointF viTri){
//        System.out.println(PointF.length(viTri.x-hitbox.left,viTri.y-hitbox.top)+" "+
//                PointF.length(diemDich.x-hitbox.left,diemDich.y-hitbox.top));
        float a=PointF.length(viTri.x-hitbox.left,viTri.y-hitbox.top);
        float b=PointF.length(diemDich.x-hitbox.left,diemDich.y-hitbox.top);
        if(a>=b){
            hitbox.left=diemDich.x;
            hitbox.top=diemDich.y;
            hitbox.right=hitbox.left+laBai.getHinhAnh().getWidth();
            hitbox.bottom=hitbox.top+laBai.getHinhAnh().getHeight();
            gocQuay=0;
            daDenDich=true;
//            System.out.println(laBai.getSo()+" "+laBai.getChat()+" "+hitbox.left+" "+hitbox.top+" "+hitbox.right+" "+hitbox.bottom);
//            if(laBai==LaBai.BA_BICH) System.out.println("OK");
//            if(chiSoSoHuu==BoBai.ChiSoSoHuu.NGUOI_CHOI_1) KickHoat =true;
            return true;
        }else{
//            System.out.println(viTri.x+" "+ viTri.y+" "+hitbox.left+" "+hitbox.top+" "+hitbox.right+" "+hitbox.bottom);
            hitbox.left=viTri.x;
            hitbox.top=viTri.y;
            hitbox.right=hitbox.left+laBai.getHinhAnh().getWidth();
            hitbox.bottom=hitbox.top+laBai.getHinhAnh().getHeight();
//            if(laBai==LaBai.BA_BICH) System.out.println("notOK");
            return false;
        }
    }

    /**
     * di chuyển nút từ từ đến đích
     * @param thoiGianGiua2CapNhat khoảng thời gian giữa 2 lần cập nhật
     * @param quay có hay không quay
     */
    public void diChuyen(double thoiGianGiua2CapNhat,boolean quay){
//        double tiLeTocDo=thoiGianGiua2CapNhat/
//                ThongSo.HoatAnhLaBaiDiChuyen.KHOANG_CACH_THOI_GIAN_GIUA_2_FRAME;
//        System.out.println(tiLeTocDo);
        if(quay) this.setGocQuay((float) (this.getGocQuay()+(this.getTocDoQuay())));
        float bienThien=(float) (this.getTocDo()>=0.001?this.getTocDo():0.01);
        PointF viTriMoi=new PointF();
        if(this.getHeSoGoc()!=0){
            float bienThienX= (float) (bienThien* Math.cos(Math.atan(this.getHeSoGoc())));
            float bienThienY= (float) (bienThien* Math.sin(Math.atan(this.getHeSoGoc())));
            if(this.getHitbox().left<this.getDiemDich().x){
                viTriMoi.x=this.getHitbox().left+bienThienX;
            }else {
                viTriMoi.x = this.getHitbox().left - bienThienX;
            }
            if(this.getHitbox().top<this.getDiemDich().y){
                viTriMoi.y=this.getHitbox().top+bienThienY;
            }else {
                viTriMoi.y = this.getHitbox().top - bienThienY;
            }
        }else{
            if(this.getHitbox().left==this.getDiemDich().x){
//                System.out.println("ok");
                viTriMoi.x=this.getHitbox().left;
                if(this.getHitbox().top<this.getDiemDich().y) viTriMoi.y=this.getHitbox().top+bienThien;
                else viTriMoi.y=this.getHitbox().top-bienThien;
            }else{
//                System.out.println("notok");
//                if(this.getHitbox().left==this.getDiemDich().x) System.out.println("vcl");
//                else System.out.println("WHAT: "+this.getHitbox().left+" "+this.getDiemDich().x);
                viTriMoi.y=this.getHitbox().top;
                if(this.getHitbox().left<this.getDiemDich().x) viTriMoi.x=this.getHitbox().left+bienThien;
                else viTriMoi.x=this.getHitbox().left-bienThien;
            }
        }
//        System.out.println("move"+laBai.getSo()+" "+laBai.getChat()+" "+hitbox.left+" "+hitbox.top+" "+diemDich.x+" "+diemDich.y+" "+heSoGoc);
        setViTriKiemTraDich(viTriMoi);
    }
    /**
     * kiểm tra xem điểm nhấn có nằm trong hitbox hay không và thay đổi cờ được bấm
     * @param viTriChuot điểm nhấn
     * @return
     */
    @Override
    public boolean isEventHere(PointF viTriChuot){
        if(hitbox.contains(viTriChuot.x,viTriChuot.y)){
            if(duocBam) duocBam=false;
            else duocBam=true;
            return true;
        }
        return false;
    }
    public void setChiSoSoHuu(BoBai.ChiSoSoHuu chiSoSoHuu) {
        this.chiSoSoHuu = chiSoSoHuu;
    }
    public float getHeSoGoc() {
        return heSoGoc;
    }

    public float getTocDoQuay() {
        return tocDoQuay;
    }

    public float getGocQuay() {
        return gocQuay;
    }

    public float getTocDo() {
        return tocDo;
    }

    public void setTocDoQuay(float tocDoQuay) {
        this.tocDoQuay = tocDoQuay;
    }

    public void setHeSoGoc(float heSoGoc) {
        this.heSoGoc = heSoGoc;
    }

    public void setGocQuay(float gocQuay) {
        this.gocQuay = gocQuay;
    }

    public void setTocDo(float tocDo) {
        this.tocDo = tocDo;
    }

    public LaBai getLaBai() {
        return laBai;
    }
    public void setLaBai(LaBai laBai) {
        this.laBai = laBai;
    }
    public boolean isDaDenDich() {
        return daDenDich;
    }
    public void setDaDenDich(boolean daDenDich) {
        this.daDenDich = daDenDich;
    }
    public void veLungBai(Canvas canvas){
        canvas.drawBitmap(LaBai.LUNG_BAI.getHinhAnh(),hitbox.left,hitbox.top,null);
    }
}
