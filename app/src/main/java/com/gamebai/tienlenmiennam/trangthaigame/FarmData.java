package com.gamebai.tienlenmiennam.trangthaigame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.widget.Toast;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.hotro.PhuongThucBitmap;
import com.gamebai.tienlenmiennam.hotro.ThongSo;
import com.gamebai.tienlenmiennam.hotro.ThuThapDuLieu;
import com.gamebai.tienlenmiennam.hotro.TrangThaiGame;
import com.gamebai.tienlenmiennam.main.Game;
import com.gamebai.tienlenmiennam.main.MainActivity;
import com.gamebai.tienlenmiennam.thucthe.BoBai;
import com.gamebai.tienlenmiennam.thucthe.KiemTraTayBai;
import com.gamebai.tienlenmiennam.thucthe.LaBai;
import com.gamebai.tienlenmiennam.thucthe.NguoiChoi;
import com.gamebai.tienlenmiennam.thucthe.TayBai;
import com.gamebai.tienlenmiennam.ui.Chu;
import com.gamebai.tienlenmiennam.ui.Nut;
import com.gamebai.tienlenmiennam.ui.NutBatTat;
import com.gamebai.tienlenmiennam.ui.NutLaBai;
import com.gamebai.tienlenmiennam.ui.VienDemNguoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FarmData extends TrangThaiCoBan implements TrangThaiGame, PhuongThucBitmap {
    public enum GiaiDoan{BAT_DAU, CHIA_BAI, LAT_BAI, CHO_CHON, DANH_BAI, KET_THUC}
    private ChoiVoiMay.GiaiDoan giaiDoan;
    private MainActivity mainActivity;
    /**
     * UI
     */
    private Random random;
    private Bitmap playingBackground,avatar;
    private int demLaBaiDiChuyen,demLaDenDich,demSoFrame;
    private List<NutLaBai> baiTrenTay,baiDanhCu,baiDanhMoi,boBai;
    private Chu chuDemNguoc, chuCanhBao, chuDemLaBai, chuThongBaoKetQua;
    private Paint paintKhongBamDuoc,paintDaDanh;
    private Nut nutDanh,nutBoLuot,nutChoiLai,nutThoat;
    private VienDemNguoc vienDemNguoc;
    /**
     * trò chơi
     */
    private NguoiChoi[] nguoiChois;
    private BoBai thongTinBoBai;
    private KiemTraTayBai kiemTraBaiDanh;
    private LaBai laCuoi;
    private TayBai tayBaiCuoi;
    private boolean chuoiChat2,hopLe;
    private int nguoiThang,nguoiDanhCuoi,nguoiDangDanh,doDaiTayBaiCuoi,thoiGianMayDanh;
    private float thoiGianDemNguoc,thoiGianHienCanhBao;
    private long thoiDiemThoatGame;
    public FarmData(Game game, MainActivity mainActivity){
        super(game);
        this.mainActivity=mainActivity;
        /**
         * khởi tạo random
         */
        random=new Random();
        /**
         * khởi tạo ảnh cố định
         */
        options.inScaled=false;
        playingBackground= BitmapFactory.decodeResource(mainActivity.getResources(),
                R.drawable.playing_background,
                options);
        float tiLeRong=(float)MainActivity.chieuRongManHinh/playingBackground.getWidth();
        float tiLeCao=(float)MainActivity.chieuCaoManHinh/playingBackground.getHeight();
        playingBackground=getBitmapTheoTiLe(playingBackground,tiLeRong,tiLeCao,false);
        avatar= BitmapFactory.decodeResource(mainActivity.getResources(),
                R.drawable.player,
                options);
        float tiLe= ThongSo.Avatar.getTiLeKichThuocAvartar();
        avatar=getBitmapTheoTiLe(avatar,tiLe,tiLe,false);
        /**
         * khởi tạo thông tin về bộ bài
         */
        thongTinBoBai =new BoBai();
        /**
         * chia bài
         */
        demSoFrame=0;
        demLaBaiDiChuyen =0;
        demLaDenDich =0;
        /**
         * người chơi
         */
        nguoiChois=new NguoiChoi[4];
        baiTrenTay=new ArrayList<>();
        boBai=new ArrayList<>();
        /**
         * bài đã đánh
         */
        baiDanhCu=new ArrayList<>();
        baiDanhMoi=new ArrayList<>();
        kiemTraBaiDanh =new KiemTraTayBai();
        /**
         * some text in game
         */
        Paint paintCanhBao=new Paint();
        paintCanhBao.setTextAlign(Paint.Align.LEFT);
        paintCanhBao.setColor(Color.YELLOW);
        paintCanhBao.setTextSize(ThongSo.CanhBaoChonBai.getKichCo());
        paintCanhBao.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC));
        chuCanhBao =new Chu(ThongSo.CanhBaoChonBai.NOI_DUNG,paintCanhBao);
        chuCanhBao.setViTri(ThongSo.CanhBaoChonBai.getViTriX(),ThongSo.CanhBaoChonBai.getViTriY());
        Paint paintDemNguoc=new Paint();
        paintDemNguoc.setTextAlign(Paint.Align.CENTER);
        paintDemNguoc.setColor(Color.YELLOW);
        paintDemNguoc.setTextSize(ThongSo.DemNguoc.getKichCo());
        paintDemNguoc.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        chuDemNguoc =new Chu(String.valueOf(ThongSo.TranDau.THOI_GIAN_CHO_CHON),paintDemNguoc);
        thoiGianDemNguoc= ThongSo.TranDau.THOI_GIAN_CHO_CHON;
        Paint panitDemLaBai=new Paint();
        panitDemLaBai.setTextAlign(Paint.Align.CENTER);
        panitDemLaBai.setColor(Color.YELLOW);
        panitDemLaBai.setTextSize(ThongSo.CanhBaoChonBai.getKichCo());
        panitDemLaBai.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        chuDemLaBai =new Chu(String.valueOf(0),panitDemLaBai);
        Paint paintThongBaoKetQua=new Paint();
        paintThongBaoKetQua.setTextAlign(Paint.Align.CENTER);
        paintThongBaoKetQua.setColor(Color.YELLOW);
        paintThongBaoKetQua.setTextSize(ThongSo.ThongBaoKetQua.getKichCo());
        paintThongBaoKetQua.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        chuThongBaoKetQua =new Chu(ThongSo.ThongBaoKetQua.NOI_DUNG_THUA,paintThongBaoKetQua);
        /**
         * paint
         */
        paintKhongBamDuoc =new Paint();
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.5f, 0.5f, 0.5f, 0, -255,  // Giảm sáng và chuyển grayscale
                0.5f, 0.5f, 0.5f, 0, -255,
                0.5f, 0.5f, 0.5f, 0, -255,
                0,    0,    0,    1,    0   // Giữ nguyên kênh Alpha
        });
        ColorMatrixColorFilter colorMatrixColorFilter=new ColorMatrixColorFilter(colorMatrix);
        paintKhongBamDuoc.setColorFilter(colorMatrixColorFilter);
        paintDaDanh =new Paint();
        colorMatrix = new ColorMatrix(new float[]{
                0.7f, 0, 0, 0, 0,  // Giảm sáng
                0, 0.7f, 0, 0, 0,
                0, 0, 0.7f, 0, 0,
                0,    0,    0,    1,    0   // Giữ nguyên kênh Alpha
        });
        colorMatrixColorFilter=new ColorMatrixColorFilter(colorMatrix);
        paintDaDanh.setColorFilter(colorMatrixColorFilter);
        Paint paintVienDemNguoc = new Paint();
        paintVienDemNguoc.setColor(Color.GREEN);
        paintVienDemNguoc.setStyle(Paint.Style.STROKE);
        paintVienDemNguoc.setStrokeWidth(15*ThongSo.Avatar.getTiLeKichThuocAvartar());
        vienDemNguoc=new VienDemNguoc(ThongSo.Avatar.getKichThuocCaoAvatar(), paintVienDemNguoc);
        /**
         * nút
         */
        Bitmap temp=getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.danh,
                        options),
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH,
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,
                true);
        nutDanh =new Nut(temp, (float) MainActivity.chieuRongManHinh*2/3- (float) temp.getWidth() /2,
                BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichY()-ThongSo.UI.KHOANG_CACH_GIUA_PHAN_TU_CAO-
                        temp.getHeight(),
                temp.getWidth(),
                temp.getHeight());
        temp=getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.boluot,
                        options),
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH,
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,
                true);
        nutBoLuot =new Nut(temp,(float) MainActivity.chieuRongManHinh/3- (float) temp.getWidth() /2,
                BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichY()-ThongSo.UI.KHOANG_CACH_GIUA_PHAN_TU_CAO-
                        temp.getHeight(),
                temp.getWidth(),
                temp.getHeight());
        temp=getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.choilai,
                        options),
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH,
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,true);
        float tempX=(float) (MainActivity.chieuRongManHinh- temp.getWidth()) /2;
        float tempY=(MainActivity.chieuCaoManHinh- temp.getHeight()-
                ThongSo.UI.KHOANG_CACH_GIUA_PHAN_TU_CAO*ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH-
                chuThongBaoKetQua.getHitBox().height()) /2+
                ThongSo.UI.KHOANG_CACH_GIUA_PHAN_TU_CAO*ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH+
                chuThongBaoKetQua.getHitBox().height();
        nutChoiLai =new Nut(temp,tempX,tempY,temp.getWidth(),temp.getHeight());
        chuThongBaoKetQua.setViTri((float) MainActivity.chieuRongManHinh /2,
                tempY-ThongSo.UI.KHOANG_CACH_GIUA_PHAN_TU_CAO*ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH-
                        temp.getHeight());
        temp=getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.tro_lai_da_bam,options),ThongSo.ThongSoManHinh.getTiLe1Chieu(),
                ThongSo.ThongSoManHinh.getTiLe1Chieu(),true);
        nutThoat=new NutBatTat(temp,
                getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),
                                R.drawable.tro_lai_chua_bam,options),ThongSo.ThongSoManHinh.getTiLe1Chieu(),
                        ThongSo.ThongSoManHinh.getTiLe1Chieu(),true),
                0,0,temp.getWidth(),temp.getHeight());
        /**
         * start game
         */
        hopLe=true;
        giaiDoan= ChoiVoiMay.GiaiDoan.BAT_DAU;
    }

    /**
     * xử lý sự kiện chạm màn hình
     *
     * @param event
     */
    @Override
    public void touchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (giaiDoan == ChoiVoiMay.GiaiDoan.CHO_CHON&&nguoiDangDanh==0) {
//                for(NutLaBai nutLaBai: baiTrenTay){
//                    if(nguoiChois[0].getLaBai(baiTrenTay.indexOf(nutLaBai)).isCoTheChon()
//                            &&nutLaBai.isEventHere(new PointF(event.getX(),event.getY()))) {
//                        if (nutLaBai.isDuocBam()) {
//                            chonLaBai(baiTrenTay.indexOf(nutLaBai));
////                            System.out.println(nguoiChois[0].getLaBai(baiTrenTay.indexOf(nutLaBai)).getSo()+" "+
////                                    nguoiChois[0].getLaBai(baiTrenTay.indexOf(nutLaBai)).getChat());
////                            System.out.println(nguoiChois[0].getLaBai(baiTrenTay.indexOf(nutLaBai)).isDuocChon());
//                        } else {
//                            boChonLaBai(baiTrenTay.indexOf(nutLaBai));
////                            System.out.println(nguoiChois[0].getLaBai(baiTrenTay.indexOf(nutLaBai)).getSo()+" "+
////                                    nguoiChois[0].getLaBai(baiTrenTay.indexOf(nutLaBai)).getChat());
////                            System.out.println(nguoiChois[0].getLaBai(baiTrenTay.indexOf(nutLaBai)).isDuocChon());
//                        }
//                        break;
//                    }
//                }
//                if(nutDanh.isEventHere(new PointF(event.getX(),event.getY()))) {
//                    danh();
//                }
//                if(nutBoLuot.isEventHere(new PointF(event.getX(),event.getY()))) {
//                    boLuot();
//                }
//            }
//            if(giaiDoan== ChoiVoiMay.GiaiDoan.KET_THUC){
//                if(nutChoiLai.isEventHere(new PointF(event.getX(),event.getY()))) {
//                    giaiDoan= ChoiVoiMay.GiaiDoan.BAT_DAU;
//                }
//            }
            if(nutThoat.isEventHere(new PointF(event.getX(),event.getY()))) {
                if(nutThoat.isDuocBam()) {
                    nutThoat.setDuocBam(false);
                }
                else {
                    nutThoat.setDuocBam(true);
                    Toast.makeText(mainActivity,mainActivity
                                    .getString(R.string.game_se_thoat),
                            Toast.LENGTH_SHORT).show();
                }
                /**
                 *
                 */
                thoat();
            }
        }
    }

    private void thoat() {
        game.setTrangThaiGame(ThongSo.TrangThaiGame.TRANG_THAI_SANH_CHO,"");
    }

    private void chonLaBai(int i) {
        NutLaBai temp=baiTrenTay.get(i);
        temp.setViTri(new PointF(temp.getHitbox().left,
                temp.getHitbox().top - ThongSo.LaBai.getKichThuocLaBaiCao() / 4));
        nguoiChois[0].getLaBai(i).setDuocChon(true);
        baiTrenTay.get(i).setDuocBam(true);
    }

    /**
     * vẽ lên màn hình
     * @param c "vải căng khung tranh"
     */
    @Override
    public void render(Canvas c) {
        c.drawBitmap(playingBackground,0,0,null);
        nutThoat.ve(c);
//        Bitmap choiLai=BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.choilai,options);
//        c.drawBitmap(choiLai,100,100,null);
//        c.drawBitmap(BitmapFactory.decodeResource(mainActivity.getResources(),
//                        R.drawable.player,
//                        options),
//                100,
//                100,
//                null);
        switch (giaiDoan){
            case BAT_DAU:
                veAvartar(c);
                break;
            case CHIA_BAI:
                for(int i=boBai.size()-1;i>-1;i--){
                    NutLaBai nutLaBai=boBai.get(i);
                    if (!nutLaBai.isDaDenDich()||
                            nutLaBai.getChiSoSoHuu()==BoBai.ChiSoSoHuu.NGUOI_CHOI_1) {
//                        c.rotate(nutLaBai.getGocQuay(),nutLaBai.getHitbox().centerX(),nutLaBai.getHitbox().centerY());
//                        nutLaBai.veLungBai(c);
//                        c.rotate(-nutLaBai.getGocQuay(),nutLaBai.getHitbox().centerX(),nutLaBai.getHitbox().centerY());
                    }
                }
                veAvartar(c);
                demSoFrame++;
                break;
            case LAT_BAI:
//                for(NutLaBai nutLaBai: baiTrenTay){
//                    if(nutLaBai.isKickHoat()) nutLaBai.ve(c);
//                    else nutLaBai.veLungBai(c);
//                }
                veAvartar(c);
                veDemLaBai(c);
//                baiTrenTay.get(2).setDiemDich(new PointF((MainActivity.chieuRongManHinh-ThongSo.KichThuocLaBai.getKichThuocLaBaiRong())/2,
//                        (MainActivity.chieuCaoManHinh-ThongSo.KichThuocLaBai.getKichThuocLaBaiCao())/2));
//                NutLaBai temp2=new NutLaBai(LaBai.LUNG_BAI,
//                        BoBai.ChiSoSoHuu.DA_DANH.getDichX(),
//                        BoBai.ChiSoSoHuu.DA_DANH.getDichY(),
//                        new PointF(BoBai.ChiSoSoHuu.DA_DANH.getDichX(),
//                                BoBai.ChiSoSoHuu.DA_DANH.getDichY()-ThongSo.KichThuocLaBai.getKichThuocLaBaiCao()/2));
//                baiDanhCu.add(temp2);
//                baiDanhMoi.add(baiTrenTay.remove(2));
//                xepLaiBaiTrenTay(baiTrenTay.size());
                break;
            case CHO_CHON:
                synchronized (baiTrenTay){
                    for(NutLaBai nutLaBai: baiTrenTay) {
                        if (nguoiChois[0].getLaBai(baiTrenTay.indexOf(nutLaBai)).isCoTheChon())
                            nutLaBai.ve(c);
                        else nutLaBai.ve(c, paintKhongBamDuoc);
                    }
                }
                synchronized (baiDanhCu){
                    for(NutLaBai nutLaBai: baiDanhCu) {
                        nutLaBai.ve(c, paintDaDanh);
                    }
                }
                synchronized (baiDanhMoi){
                    for(NutLaBai nutLaBai: baiDanhMoi){
                        nutLaBai.ve(c);
                    }
                }
                if(nguoiDangDanh==0){
                    nutDanh.ve(c);
                    nutBoLuot.ve(c);
                }
                veAvartar(c);
                veDemLaBai(c);
                veDemNguoc(c);
                if(!hopLe&&thoiGianHienCanhBao>0) {
                    chuCanhBao.ve(c);
                }
                break;
            case DANH_BAI:
                for(NutLaBai nutLaBai: baiTrenTay){
                    nutLaBai.ve(c);
                }
//                for(NutLaBai nutLaBai: baiDanhCu) {
//                    nutLaBai.ve(c, paintDaDanh);
//                }
//                for(NutLaBai nutLaBai: baiDanhMoi){
//                    nutLaBai.ve(c);
//                }
                veAvartar(c);
                veDemLaBai(c);
                break;
            case KET_THUC:
                for(NutLaBai nutLaBai: baiTrenTay)
                    nutLaBai.ve(c);
                veAvartar(c);
                veDemLaBai(c);
                veDemNguoc(c);
                veThongBaoKetThuc(c);
                break;
        }
    }

    private void veThongBaoKetThuc(Canvas c) {
        chuThongBaoKetQua.ve(c);
        nutChoiLai.ve(c);
    }

    /**
     * vẽ các ô đếm lá bài cho đối thủ, khi kết thúc trận sẽ hiện các lá bài còn lại của đối thủ
     * @param c
     */
    private void veDemLaBai(Canvas c) {
        if (giaiDoan != ChoiVoiMay.GiaiDoan.KET_THUC) {
            Bitmap lungBai=getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.back_card,options),
                    ThongSo.LaBai.TI_LE_LA_BAI_RONG*ThongSo.LaBai.TI_LE_LA_BAI_NHO,
                    ThongSo.LaBai.TI_LE_LA_BAI_CAO*ThongSo.LaBai.TI_LE_LA_BAI_NHO,
                    true);
            c.drawBitmap(lungBai,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriDemLaBaiX(),
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriDemLaBaiY(),
                    null);
            chuDemLaBai.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriDemLaBaiX()+
                            ThongSo.LaBai.getKichThuocLaBaiNhoRong()/2,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriDemLaBaiY()+
                            (ThongSo.LaBai.getKichThuocLaBaiNhoCao()+ chuDemLaBai.getHitBox().height())/2);
            chuDemLaBai.setNoiDung(String.valueOf(nguoiChois[1].soLa()));
            chuDemLaBai.ve(c);
            if (nguoiChois[2].isActive()) {
                c.drawBitmap(lungBai,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriDemLaBaiX(),
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriDemLaBaiY(),
                        null);
                chuDemLaBai.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriDemLaBaiX()+
                                ThongSo.LaBai.getKichThuocLaBaiNhoRong()/2,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriDemLaBaiY()+
                                (ThongSo.LaBai.getKichThuocLaBaiNhoCao()+ chuDemLaBai.getHitBox().height())/2);
                chuDemLaBai.setNoiDung(String.valueOf(nguoiChois[2].soLa()));
                chuDemLaBai.ve(c);
            }
            if (nguoiChois[3].isActive()) {
                c.drawBitmap(lungBai,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriDemLaBaiX(),
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriDemLaBaiY(),
                        null);
                chuDemLaBai.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriDemLaBaiX()+
                                ThongSo.LaBai.getKichThuocLaBaiNhoRong()/2,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriDemLaBaiY()+
                                (ThongSo.LaBai.getKichThuocLaBaiNhoCao()+ chuDemLaBai.getHitBox().height())/2);
                chuDemLaBai.setNoiDung(String.valueOf(nguoiChois[3].soLa()));
                chuDemLaBai.ve(c);
            }
        }else{
            for(int i=1;i<nguoiChois.length;i++){
                if(nguoiChois[i].isActive()){
                    float viTriX=nguoiChois[i].getChiSoSoHuu().getViTriDemLaBaiX();
                    float viTriY=nguoiChois[i].getChiSoSoHuu().getViTriDemLaBaiY()+
                            ThongSo.LaBai.getKichThuocLaBaiNhoCao()/2-
                            ThongSo.LaBai.getKichThuocLaBaiNhoCao()*
                                    ((float) (nguoiChois[i].soLa() - 1) /4+1)/2;
                    if(viTriY<0){
                        /**
                         * trường hợp người chơi phía trên
                         */
                        viTriX=(MainActivity.chieuRongManHinh-
                                ThongSo.LaBai.getKichThuocLaBaiNhoRong()*
                                        ((float) (nguoiChois[i].soLa() - 1) /4+1))/2;
                        viTriY=0+ThongSo.Avatar.getKichThuocCaoAvatar();
                        for(LaBai laBai:nguoiChois[i].getBaiTrenTay()){
                            c.drawBitmap(laBai.getHinhAnhTheoTiLe(ThongSo.LaBai.TI_LE_LA_BAI_NHO,
                                            ThongSo.LaBai.TI_LE_LA_BAI_NHO),
                                    viTriX+nguoiChois[i].getBaiTrenTay().indexOf(laBai)*
                                            ThongSo.LaBai.getKichThuocLaBaiNhoRong()/4,
                                    viTriY,
                                    null);
                        }
                    }else{
                        /**
                         * trường hợp người chơi phía 2 bên
                         */
                        for(LaBai laBai:nguoiChois[i].getBaiTrenTay()){
                            c.drawBitmap(laBai.getHinhAnhTheoTiLe(ThongSo.LaBai.TI_LE_LA_BAI_NHO,
                                            ThongSo.LaBai.TI_LE_LA_BAI_NHO),
                                    viTriX,
                                    viTriY+nguoiChois[i].getBaiTrenTay().indexOf(laBai)*
                                            ThongSo.LaBai.getKichThuocLaBaiNhoCao()/4,
                                    null);
                        }
                    }
                }
            }
        }
    }

    /**
     * vẽ các avatar người chơi lên màn hình
     * @param c
     */
    private void veAvartar(Canvas c) {
        Paint temp;
        if(nguoiChois[0].boLuot) temp= paintKhongBamDuoc;
        else temp=null;
        c.drawBitmap(avatar,
                BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarX(),
                BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarY(),
                temp);
        if(nguoiChois[1].boLuot) temp= paintKhongBamDuoc;
        else temp=null;
        c.drawBitmap(avatar,
                BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarX(),
                BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarY(),
                temp);
        if(nguoiChois[2].isActive()) {
            if(nguoiChois[2].boLuot) temp= paintKhongBamDuoc;
            else temp=null;
            c.drawBitmap(avatar,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarX(),
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarY(),
                    temp);
        }
        if(nguoiChois[3].isActive()) {
            if(nguoiChois[3].boLuot) temp= paintKhongBamDuoc;
            else temp=null;
            c.drawBitmap(avatar,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarX(),
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarY(),
                    temp);
        }
        if(giaiDoan== ChoiVoiMay.GiaiDoan.CHO_CHON) vienDemNguoc.ve(c);
    }
    /**
     * vẽ đếm ngược lên người chơi tương ứng
     * @param c
     */
    private void veDemNguoc(Canvas c) {
//        Paint temp=new Paint();
//        temp.setStyle(Paint.Style.STROKE);
//        temp.setColor(Color.BLACK);
//        temp.setStrokeWidth(5);
//        c.drawLine(0, (float) MainActivity.chieuCaoManHinh /2,MainActivity.chieuRongManHinh, (float) MainActivity.chieuCaoManHinh /2,temp);
//        c.drawLine((float) MainActivity.chieuRongManHinh /2,0, (float) MainActivity.chieuRongManHinh /2,MainActivity.chieuCaoManHinh,temp);
        switch (nguoiDangDanh){
            case 0:
                chuDemNguoc.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarX()+
                                ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarY()+
                                (ThongSo.Avatar.getKichThuocCaoAvatar()+
                                        chuDemNguoc.getHitBox().height())/2);
//                c.drawRect(BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichX()+
//                        (ThongSo.Avatar.getKichThuocRongAvatar()-
//                                demNguoc.getHitbox().width())/2,
//                        BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichY()+
//                                (ThongSo.LaBai.getKichThuocLaBaiCao()+
//                                        demNguoc.getHitbox().height())/2-
//                        demNguoc.getHitbox().height(),
//                        BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichX()+
//                                (ThongSo.Avatar.getKichThuocRongAvatar()-
//                                        demNguoc.getHitbox().width())/2+
//                        demNguoc.getHitbox().width(),
//                        BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichY()+
//                                (ThongSo.LaBai.getKichThuocLaBaiCao()+
//                                        demNguoc.getHitbox().height())/2,
//                        temp);
                break;
            case 1:
                chuDemNguoc.setViTri(MainActivity.chieuRongManHinh-ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        (MainActivity.chieuCaoManHinh + chuDemNguoc.getHitBox().height()) /2);
//                c.drawRect(MainActivity.chieuRongManHinh-
//                                (ThongSo.Avatar.getKichThuocRongAvatar()+demNguoc.getHitbox().width())/2,
//                        (float) (MainActivity.chieuCaoManHinh- demNguoc.getHitbox().height()) /2,
//                        MainActivity.chieuRongManHinh-
//                                (ThongSo.Avatar.getKichThuocRongAvatar()+demNguoc.getHitbox().width())/2+demNguoc.getHitbox().width(),
//                        (float) (MainActivity.chieuCaoManHinh + demNguoc.getHitbox().height()) /2,
//                        temp);
                break;
            case 2:
                chuDemNguoc.setViTri((float) MainActivity.chieuRongManHinh /2,
                        (ThongSo.Avatar.getKichThuocCaoAvatar()+ chuDemNguoc.getHitBox().height())/2);
//                c.drawRect(200,200-demNguoc.getHitbox().height(),200+demNguoc.getHitbox().width(),200,temp);
                break;
            case 3:
                chuDemNguoc.setViTri(ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        (MainActivity.chieuCaoManHinh + chuDemNguoc.getHitBox().height()) /2);
//                c.drawRect(200,200-demNguoc.getHitbox().height(),200+demNguoc.getHitbox().width(),200,temp);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + nguoiDangDanh);
        }
        chuDemNguoc.ve(c);
    }

    /**
     * cập nhật mọi thông số trong trạng thái game
     * @param delta
     * @param isCanvasNull
     */
    @Override
    public void capNhat(double delta,boolean isCanvasNull) {
        switch (giaiDoan){
            case BAT_DAU:
                /**
                 * ui
                 */
                baiTrenTay.clear();
                baiDanhMoi.clear();
                baiDanhCu.clear();
                boBai.clear();
                nutThoat.setDuocBam(false);
                /**
                 * chỉ số
                 */
                demLaBaiDiChuyen=0;
                demLaDenDich=0;
                demSoFrame=0;
                nguoiDanhCuoi=0;
                thoiDiemThoatGame=0;
                thongTinBoBai.chiaBai();
                boBai=thongTinBoBai.danhSachNutLaBai();
                for(int i=0;i<4;i++){
                    this.nguoiChois[i]=new NguoiChoi(BoBai.ChiSoSoHuu.timTheoSoHuu(i+1));
                    this.nguoiChois[i].sauChiaBai(thongTinBoBai);
//                    if(i<3) this.solabai[i].setText(String.valueOf(this.nguoichoi[i].soLa()));
//                    this.demnguoc[i].setFont(new Font("Arial", Font.BOLD, 55));
//                    this.demnguoc[i].setVisible(false);
                    if(this.nguoiChois[i].timTuQuy(LaBai.getTempInstance(14,-1),true))
                        this.nguoiChois[i].anhLaKeMayMan=true;
                }
                giaiDoan= ChoiVoiMay.GiaiDoan.CHIA_BAI;
                break;
            case CHIA_BAI:
                /**
                 * do để đếm số frame nên ta cần đếm trong render, do đó ta cần đồng bộ nếu render chạy thì capNhap mới chạy
                 */
                if (!isCanvasNull) {
                    for(int i=0;i<boBai.size();i++){
                        NutLaBai temp=boBai.get(i);
                        if(i<= demLaBaiDiChuyen &&!temp.isDaDenDich()){
                            temp.diChuyen(delta,true);
                            if(temp.isDaDenDich()){
                                demLaDenDich++;
                            }
                            if(demSoFrame==2){
                                demLaBaiDiChuyen++;
                                demSoFrame=0;
                            }
                        }
                    }
                    if(demLaDenDich==52){
                        for(NutLaBai nutLaBai: boBai){
                            if(nutLaBai.getChiSoSoHuu()== BoBai.ChiSoSoHuu.NGUOI_CHOI_1)
                                baiTrenTay.add(nutLaBai);
                        }
                        Collections.reverse(baiTrenTay);
                        demLaDenDich=1;
                        demSoFrame=0;
                        demLaBaiDiChuyen=0;
                        baiTrenTay.get(0).setKickHoat(true);
                        giaiDoan= ChoiVoiMay.GiaiDoan.LAT_BAI;
                    }
                }
                break;
            case LAT_BAI:
                if(demLaDenDich<baiTrenTay.size()){
                    baiTrenTay.get(demLaDenDich).setKickHoat(true);
                    demLaDenDich++;
//                    demSoFrame=0;
                }else luotMoi();
                break;
            case CHO_CHON:
                thoiGianDemNguoc-= (float) (delta);
                vienDemNguoc.tinhDuongVe(thoiGianDemNguoc);
                if(thoiGianDemNguoc>0) thoiGianHienCanhBao-=(float) (delta);
                if(thoiGianDemNguoc<=thoiGianMayDanh){
                    if(nguoiChois[nguoiDangDanh].timCoTheChon(laCuoi,
                            tayBaiCuoi.ordinal(),
                            doDaiTayBaiCuoi,
                            thongTinBoBai)){
                        danh();
                    }else{
                        boLuot();
                    }
                }else{
                    if(thoiGianDemNguoc>0){
                        chuDemNguoc.setNoiDung(String.valueOf((int)thoiGianDemNguoc));
                    }else{
                        chuDemNguoc.setNoiDung("0");
                        boLuot();
                    }
                }
                break;
            case DANH_BAI:
                boolean done=true;
                for(NutLaBai nutLaBai: baiTrenTay){
                    if(!nutLaBai.isDaDenDich()) {
                        nutLaBai.diChuyen(delta,false);
                        done=false;
                    }
                }
                for(NutLaBai nutLaBai: baiDanhCu){
                    if(!nutLaBai.isDaDenDich()) {
                        nutLaBai.diChuyen(delta,false);
                        done=false;
                    }
                }
                for(NutLaBai nutLaBai: baiDanhMoi){
                    if(!nutLaBai.isDaDenDich()) {
                        nutLaBai.diChuyen(delta,false);
                        done=false;
                    }
                }
                if(done) raBai();
                break;
            case KET_THUC:
                if(thoiDiemThoatGame==0) thoiDiemThoatGame=System.currentTimeMillis()
                        +1000;
                if(System.currentTimeMillis()-thoiDiemThoatGame>=0){taiLai();}
                break;
        }
    }
    /**
     * thực hiện thao tác đánh đối với các người chơi khác nhau đặc biệt với bot
     */
    private void danh(){
        kiemTraBaiDanh =new KiemTraTayBai();
        if(this.nguoiDangDanh==0){
            resetMayChon();
            /**
             * AI ngu
             */
            if(tayBaiCuoi==TayBai.KhongCo){
                boolean a=nguoiChois[nguoiDangDanh].timDoiThong(LaBai.getTempInstance(2,-1),
                        3,thongTinBoBai,true);
                boolean b=nguoiChois[nguoiDangDanh].timTuQuy(LaBai.getTempInstance(2,-1),true);
                if(!a&&!b){
                    if(nguoiChois[nguoiDangDanh].chonBoDaiNhat(thongTinBoBai))
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            }
                        }
                    else if(nguoiChois[nguoiDangDanh].timSapKhac2(LaBai.getTempInstance(2,-1),
                            true)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                                if(dem>=3) break;
                            }
                        }
                    }else if(nguoiChois[nguoiDangDanh].timDoiKhac2(LaBai.getTempInstance(2,-1),
                            true)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                                if(dem>=2) break;
                            }
                        }
                    }else{
                        kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(0));
                        nguoiChois[nguoiDangDanh].setDuocChon(0, true);
                    }
                }else{
                    kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(0));
                    nguoiChois[nguoiDangDanh].setDuocChon(0, true);
                }
            }
            if(tayBaiCuoi==TayBai.Le){
                if(laCuoi.getSo()==14){
                    if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(LaBai.getTempInstance(2,-1),
                            3,
                            thongTinBoBai)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                            }
                            if(dem>=6) break;
                        }
                    }else if(nguoiChois[nguoiDangDanh].timTuQuy(LaBai.getTempInstance(2,-1),true)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                            }
                            if(dem>=4) break;
                        }
                    }
                    else{
                        nguoiChois[nguoiDangDanh].timLa(laCuoi,true);
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                break;
                            }
                        }
                    }
                }else{
                    if(!nguoiChois[nguoiDangDanh].timLaLe(laCuoi,thongTinBoBai))
                        nguoiChois[nguoiDangDanh].timLa(laCuoi,true);
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            break;
                        }
                    }
                }
            }
            if(tayBaiCuoi==TayBai.Doi){
                if(laCuoi.getSo()==14){
                    if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(LaBai.getTempInstance(2,-1),
                            4,
                            thongTinBoBai)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                            }
                            if(dem>=8) break;
                        }
                    }else{
                        nguoiChois[nguoiDangDanh].timDoi(laCuoi,true);
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                                if(dem>=2) break;
                            }
                        }
                    }
                }else{
                    nguoiChois[nguoiDangDanh].timDoi(laCuoi,true);
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                            if(dem>=2) break;
                        }
                    }
                }
            }
            if(tayBaiCuoi==TayBai.Sap){
                nguoiChois[nguoiDangDanh].timSap(laCuoi,true);
                int dem=0;
                for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                    if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                        kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                        nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                        dem++;
                        if(dem>=3) break;
                    }
                }
            }
            if(tayBaiCuoi==TayBai.TuQuy){
                if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(LaBai.getTempInstance(2,-1),
                        4,
                        thongTinBoBai)){
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=8) break;
                    }
                }else{
                    nguoiChois[nguoiDangDanh].timTuQuy(laCuoi,true);
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=4) break;
                    }
                }
            }
            if(tayBaiCuoi==TayBai.Bo){
                nguoiChois[nguoiDangDanh].timBoNhoNhat(laCuoi, doDaiTayBaiCuoi, thongTinBoBai);
                int dem=0;
                for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                    if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                        kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                        nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                        dem++;
                    }
                    if(dem>=doDaiTayBaiCuoi) break;
                }
            }
            if(tayBaiCuoi==TayBai.Thong){
                if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(LaBai.getTempInstance(2,-1),
                        doDaiTayBaiCuoi+1,
                        thongTinBoBai)){
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=2*(doDaiTayBaiCuoi+1)) break;
                    }
                }else if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(laCuoi,
                        doDaiTayBaiCuoi,
                        thongTinBoBai)){
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=doDaiTayBaiCuoi*2) break;
                    }
                }else{
                    nguoiChois[nguoiDangDanh].timTuQuy(LaBai.getTempInstance(2,-1),true);
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=4) break;
                    }
                }
            }
            kiemTraBaiDanh.sort();
            TayBai tayBaiMoi= kiemTraBaiDanh.baiDanhHopLe(tayBaiCuoi.ordinal(),
                    doDaiTayBaiCuoi,
                    this.laCuoi,
                    this.nguoiChois[0].chiChat2);
            if(tayBaiMoi==TayBai.KhongHopLe){
                hopLe=false;
                thoiGianHienCanhBao= ThongSo.TranDau.THOI_GIAN_HIEN_CANH_BAO;
                kiemTraBaiDanh.laBai.clear();
                ThuThapDuLieu.luuBoBaiLoi("loi.csv",thongTinBoBai);
                return;
            }
            if(tayBaiMoi==TayBai.Bo){
                tayBaiCuoi=tayBaiMoi;
                doDaiTayBaiCuoi= kiemTraBaiDanh.laBai.size();
                laCuoi= kiemTraBaiDanh.laBai.get(kiemTraBaiDanh.laBai.size()-1);
                hopLe=true;
            }
            if(tayBaiMoi==TayBai.Thong){
                tayBaiCuoi=tayBaiMoi;
                doDaiTayBaiCuoi= kiemTraBaiDanh.laBai.size()/2;
                laCuoi= kiemTraBaiDanh.laBai.get(kiemTraBaiDanh.laBai.size()-1);
                hopLe=true;
            }
            if(tayBaiMoi==TayBai.Le||tayBaiMoi==TayBai.Doi||tayBaiMoi==TayBai.Sap||tayBaiMoi==TayBai.TuQuy){
                try {
                    tayBaiCuoi=tayBaiMoi;
                    doDaiTayBaiCuoi=0;
                    laCuoi= kiemTraBaiDanh.laBai.get(kiemTraBaiDanh.laBai.size()-1);
                    hopLe=true;
                } catch (Exception e) {
                    System.out.println(Arrays.deepToString(thongTinBoBai.laBaiBanDau));
                }
            }
            /**
             * Lưu trạng thái và quyết định
             */
            nguoiChois[nguoiDangDanh].luuLaiQuyetDinh();
            ThuThapDuLieu.luuVaoFile("may.csv",nguoiChois[nguoiDangDanh].trangThaiGame,nguoiChois[nguoiDangDanh].quyetDinh);
            nguoiChois[nguoiDangDanh].thayDoiTrangThai(kiemTraBaiDanh.laBai,0);
            /**
             * thay đổi bài đánh ở bàn, cập nhật thông tin sở hữu lá bài
             */
            if(!baiDanhCu.isEmpty()) synchronized (baiDanhCu){
                baiDanhCu.clear();
            }
            for(NutLaBai nutLaBai: baiDanhMoi){
                nutLaBai.setDiemDich(new PointF(nutLaBai.getHitbox().left,
                        nutLaBai.getHitbox().top- ThongSo.LaBai.getKichThuocLaBaiCao()/2));
                synchronized (baiDanhCu){
                    baiDanhCu.add(nutLaBai);
                }
            }
            if(!baiDanhMoi.isEmpty()) synchronized (baiDanhMoi){
                baiDanhMoi.clear();
            }
            PointF temp=new PointF(BoBai.ChiSoSoHuu.DA_DANH.getDichX()-
                    (ThongSo.LaBai.getKichThuocLaBaiRong()/2)*(kiemTraBaiDanh.laBai.size()-1),
                    BoBai.ChiSoSoHuu.DA_DANH.getDichY());
            synchronized (baiTrenTay){
                synchronized (baiDanhMoi){
                    for(int i=0;i<nguoiChois[0].soLa();i++){
                        if(nguoiChois[0].isDuocChon(i)) {
                            thongTinBoBai.setSoHuu(nguoiChois[0].getSo(i),
                                    nguoiChois[0].getChat(i),
                                    BoBai.ChiSoSoHuu.DA_DANH.getChiSo());
                            nguoiChois[0].boLaBai(i);
                            baiTrenTay.get(i).setDiemDich(temp);
                            NutLaBai tempNut;
                            tempNut=baiTrenTay.remove(i);
                            baiDanhMoi.add(tempNut);
                            temp.x+= ThongSo.LaBai.getKichThuocLaBaiRong();
                            i--;
                        }
                    }
                }
            }
//            xepLaiBaiTrenTay(baiTrenTay.size());
        }
        else{
            resetMayChon();
            /**
             * AI ngu
             */
            if(tayBaiCuoi==TayBai.KhongCo){
                boolean a=nguoiChois[nguoiDangDanh].timDoiThong(LaBai.getTempInstance(2,-1),
                        3,thongTinBoBai,true);
                boolean b=nguoiChois[nguoiDangDanh].timTuQuy(LaBai.getTempInstance(2,-1),true);
                if(!a&&!b){
                    if(nguoiChois[nguoiDangDanh].chonBoDaiNhat(thongTinBoBai))
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            }
                        }else if(nguoiChois[nguoiDangDanh].timSapKhac2(LaBai.getTempInstance(2,-1),true)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                                if(dem>=3) break;
                            }
                        }
                    }else if(nguoiChois[nguoiDangDanh].timDoiKhac2(LaBai.getTempInstance(2,-1),true)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                                if(dem>=2) break;
                            }
                        }
                    }else{
                        kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(0));
                        nguoiChois[nguoiDangDanh].setDuocChon(0, true);
                    }
                }else{
                    kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(0));
                    nguoiChois[nguoiDangDanh].setDuocChon(0, true);
                }
            }
            if(tayBaiCuoi==TayBai.Le){
                if(laCuoi.getSo()==14){
                    if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(LaBai.getTempInstance(2,-1),
                            3,
                            thongTinBoBai)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                            }
                            if(dem>=6) break;
                        }
                    }else if(nguoiChois[nguoiDangDanh].timTuQuy(LaBai.getTempInstance(2,-1),true)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                            }
                            if(dem>=4) break;
                        }
                    }
                    else{
                        nguoiChois[nguoiDangDanh].timLa(laCuoi,true);
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                break;
                            }
                        }
                    }
                }else{
                    if(!nguoiChois[nguoiDangDanh].timLaLe(laCuoi,thongTinBoBai))
                        nguoiChois[nguoiDangDanh].timLa(laCuoi,true);
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            break;
                        }
                    }
                }
            }
            if(tayBaiCuoi==TayBai.Doi){
                if(laCuoi.getSo()==14){
                    if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(LaBai.getTempInstance(2,-1),
                            4,
                            thongTinBoBai)){
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                            }
                            if(dem>=8) break;
                        }
                    }else{
                        nguoiChois[nguoiDangDanh].timDoi(laCuoi,true);
                        int dem=0;
                        for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                            if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                                kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                                nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                                dem++;
                                if(dem>=2) break;
                            }
                        }
                    }
                }else{
                    nguoiChois[nguoiDangDanh].timDoi(laCuoi,true);
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                            if(dem>=2) break;
                        }
                    }
                }
            }
            if(tayBaiCuoi==TayBai.Sap){
                nguoiChois[nguoiDangDanh].timSap(laCuoi,true);
                int dem=0;
                for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                    if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                        kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                        nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                        dem++;
                        if(dem>=3) break;
                    }
                }
            }
            if(tayBaiCuoi==TayBai.TuQuy){
                if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(LaBai.getTempInstance(2,-1),
                        4,
                        thongTinBoBai)){
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=8) break;
                    }
                }else{
                    nguoiChois[nguoiDangDanh].timTuQuy(laCuoi,true);
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=4) break;
                    }
                }
            }
            if(tayBaiCuoi==TayBai.Bo){
                nguoiChois[nguoiDangDanh].timBoNhoNhat(laCuoi, doDaiTayBaiCuoi, thongTinBoBai);
                int dem=0;
                for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                    if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                        kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                        nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                        dem++;
                    }
                    if(dem>=doDaiTayBaiCuoi) break;
                }
            }
            if(tayBaiCuoi==TayBai.Thong){
                if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(LaBai.getTempInstance(2,-1),
                        doDaiTayBaiCuoi+1,
                        thongTinBoBai)){
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=2*(doDaiTayBaiCuoi+1)) break;
                    }
                }else if(nguoiChois[nguoiDangDanh].timDoiThongNhoNhat(laCuoi,
                        doDaiTayBaiCuoi,
                        thongTinBoBai)){
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=doDaiTayBaiCuoi*2) break;
                    }
                }else{
                    nguoiChois[nguoiDangDanh].timTuQuy(LaBai.getTempInstance(2,-1),true);
                    int dem=0;
                    for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                        if(nguoiChois[nguoiDangDanh].isCoTheChon(i)){
                            kiemTraBaiDanh.laBai.add(nguoiChois[nguoiDangDanh].getLaBai(i));
                            nguoiChois[nguoiDangDanh].setDuocChon(i, true);
                            dem++;
                        }
                        if(dem>=4) break;
                    }
                }
            }
            kiemTraBaiDanh.sort();
            TayBai tayBaiMoi= kiemTraBaiDanh.baiDanhHopLe(tayBaiCuoi.ordinal(),
                    doDaiTayBaiCuoi,
                    laCuoi,
                    nguoiChois[nguoiDangDanh].chiChat2);
            /**
             * dành cho mục đích sửa lỗi
             */
            if(tayBaiMoi==TayBai.KhongHopLe){
                hopLe=false;
                thoiGianHienCanhBao= ThongSo.TranDau.THOI_GIAN_HIEN_CANH_BAO;
                kiemTraBaiDanh.laBai.clear();
                ThuThapDuLieu.luuBoBaiLoi("loi.csv",thongTinBoBai);
                return;
            }
//            if(tayBaiMoi==TayBai.KhongHopLe){
//                System.out.println("\nMáy "+nguoiDangDanh+"Đánh lỗi");
//                System.out.print("\t"+tayBaiCuoi+"\t"+doDaiTayBaiCuoi+"\t");laCuoi.xuat();System.out.print("\t");
//                if(!checkBaiDanh.labai.isEmpty())
//                for(int i=0;i<checkBaiDanh.labai.size();i++){
//                    checkBaiDanh.labai.get(i).xuat();System.out.print("\t");
//                }
//                System.out.println();
//                for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
//                    nguoiChois[nguoiDangDanh].getLaBai(i).xuat();
//                    System.out.print("\t");
//                }
//                System.out.println();
//                for(int i=0;i<15;i++){
//                    for(int j=0;j<4;j++){
//                        System.out.print(bobai.getSoHuu(i, j)+",");
//                    }System.out.println();
//                }
//                checkBaiDanh.labai.clear();
//                return;
//            }
            if(tayBaiMoi==TayBai.Bo){
                tayBaiCuoi=tayBaiMoi;
                doDaiTayBaiCuoi= kiemTraBaiDanh.laBai.size();
                laCuoi= kiemTraBaiDanh.laBai.get(kiemTraBaiDanh.laBai.size()-1);
            }
            if(tayBaiMoi==TayBai.Thong){
                tayBaiCuoi=tayBaiMoi;
                doDaiTayBaiCuoi= kiemTraBaiDanh.laBai.size()/2;
                laCuoi= kiemTraBaiDanh.laBai.get(kiemTraBaiDanh.laBai.size()-1);
            }
            if(tayBaiMoi==TayBai.Le||tayBaiMoi==TayBai.Doi||tayBaiMoi==TayBai.Sap||tayBaiMoi==TayBai.TuQuy){
                tayBaiCuoi=tayBaiMoi;
                doDaiTayBaiCuoi=0;
                laCuoi= kiemTraBaiDanh.laBai.get(kiemTraBaiDanh.laBai.size()-1);
                hopLe=true;
            }
            /**
             * lưu trạng thái và quyết định
             */
            nguoiChois[nguoiDangDanh].luuLaiQuyetDinh();
            ThuThapDuLieu.luuVaoFile("may.csv",nguoiChois[nguoiDangDanh].trangThaiGame,nguoiChois[nguoiDangDanh].quyetDinh);
            nguoiChois[nguoiDangDanh].thayDoiTrangThai(kiemTraBaiDanh.laBai,0);
            /**
             * đánh bài
             */
            if(!baiDanhCu.isEmpty()) synchronized (baiDanhCu){
                baiDanhCu.clear();
            }
            for(NutLaBai nutLaBai: baiDanhMoi){
                nutLaBai.setDiemDich(new PointF(nutLaBai.getHitbox().left,
                        nutLaBai.getHitbox().top- ThongSo.LaBai.getKichThuocLaBaiCao()/2));
                synchronized (baiDanhCu){
                    baiDanhCu.add(nutLaBai);
                }
            }
            if(!baiDanhMoi.isEmpty()) synchronized (baiDanhMoi){
                baiDanhMoi.clear();
            }
            PointF temp=new PointF(BoBai.ChiSoSoHuu.DA_DANH.getDichX()-
                    (ThongSo.LaBai.getKichThuocLaBaiRong()/2)*(kiemTraBaiDanh.laBai.size()-1),
                    BoBai.ChiSoSoHuu.DA_DANH.getDichY());
            synchronized (baiDanhMoi){
                for(int i=0;i<nguoiChois[nguoiDangDanh].soLa();i++){
                    if(nguoiChois[nguoiDangDanh].isDuocChon(i)) {
                        int so=nguoiChois[nguoiDangDanh].getSo(i);
                        int chat=nguoiChois[nguoiDangDanh].getChat(i);
                        thongTinBoBai.setSoHuu(so, chat, BoBai.ChiSoSoHuu.DA_DANH.getChiSo());
                        BoBai.ChiSoSoHuu temp1=BoBai.ChiSoSoHuu.timTheoSoHuu(nguoiChois[nguoiDangDanh].getChiSoSoHuu().getChiSo());
                        baiDanhMoi.add(new NutLaBai(LaBai.timLaBaiTheoSoVaChat(so,chat),
                                temp1.getDichX(),
                                temp1.getDichY(),
                                temp));
//                        System.out.println(baiDanhMoi.get(baiDanhMoi.size()-1).getLaBai().getSo()+" "+baiDanhMoi.get(baiDanhMoi.size()-1).getLaBai().getChat()+": "+baiDanhMoi.get(baiDanhMoi.size()-1).getHitbox().left+" "+baiDanhMoi.get(baiDanhMoi.size()-1).getHitbox().top+" "+temp.x+" "+temp.y+" "+baiDanhMoi.get(baiDanhMoi.size()-1).getHeSoGoc());
                        nguoiChois[nguoiDangDanh].boLaBai(i);
                        temp.x+= ThongSo.LaBai.getKichThuocLaBaiRong();
                        i--;
                    }
                }
            }
//            this.solabai[nguoiDangDanh-1].setText(String.valueOf(this.nguoichoi[nguoiDangDanh].soLa()));
        }
        if((laCuoi.getSo()==14&&tayBaiCuoi.ordinal()<=2)||
                tayBaiCuoi==TayBai.TuQuy||
                tayBaiCuoi==TayBai.Thong) chuoiChat2=true;
        /**
         * cập nhật trạng thái cho người chơi khác
         */
        int dem=0;
        int i=nguoiDangDanh+1;
        if(i==4) i=0;
        while(i!=nguoiDangDanh){
            nguoiChois[i].thayDoiTrangThai(kiemTraBaiDanh.laBai,3-dem);
            dem++;
            if(i==3) i=0;
            else i++;
        }
        //TODO: âm thanh
//        try {
//            this.amThanh("src/AmThanh/danh.wav");
//        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
//            Logger.getLogger(TienLen.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if(tayBaiCuoi==TayBai.Thong||tayBaiCuoi==TayBai.TuQuy){
//            try {
//                this.amThanh("src/AmThanh/doithong.wav");
//            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
//                Logger.getLogger(TienLen.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        /**
         * chuyển người đánh
         */
        nguoiDanhCuoi=nguoiDangDanh;
        if(nguoiDangDanh==3) nguoiDangDanh=0;
        else nguoiDangDanh++;
        giaiDoan= ChoiVoiMay.GiaiDoan.DANH_BAI;
    }
    /**
     * đặt lại cờ có thể chọn và được chọn thành false cho tất cả các lá bài trên tay máy
     */
    private void resetMayChon() {
        for(int i=0;i<this.nguoiChois[nguoiDangDanh].soLa();i++){
            nguoiChois[nguoiDangDanh].setCoTheChon(i, false);
            nguoiChois[nguoiDangDanh].setDuocChon(i, false);
        }
    }
    private void boLuot() {
        /**
         * lưu lại trạng thái và quyết định
         */
        nguoiChois[nguoiDangDanh].quyetDinh=new float[52];
        ThuThapDuLieu.luuVaoFile("may.csv",
                nguoiChois[nguoiDangDanh].trangThaiGame,nguoiChois[nguoiDangDanh].quyetDinh);
        nguoiChois[nguoiDangDanh].boLuot=true;
        chuoiChat2=false;
        if(nguoiDangDanh==3) nguoiDangDanh=0;
        else nguoiDangDanh++;
        this.raBai();
    }
    /**
     * tạo vị trí mới cho các lá bài trên tay sau khi đánh bài
     * @param soLaConLai số lá bài còn lại trên tay
     */
    private void xepLaiBaiTrenTay(int soLaConLai){
        /**
         * căn giữa bài trên tay
         */
//        float dichX=(MainActivity.chieuRongManHinh -(ThongSo.LaBai.getKichThuocLaBaiRong()*((float) soLaConLai)))/2;
        /**
         * bắt đầu lại từ đầu
         */
        float dichX=BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichX();
        float dichY=BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichY();
        for(NutLaBai nutLaBai: baiTrenTay) {
            nutLaBai.setDiemDich(new PointF(dichX, dichY));
            dichX+= ThongSo.LaBai.getKichThuocLaBaiRong();
        }
    }
    /**
     * khởi tạo lượt mới
     */
    private void luotMoi(){
        this.laCuoi= LaBai.getTempInstance(2,-1);
        this.tayBaiCuoi=TayBai.KhongCo;
        this.doDaiTayBaiCuoi =0;
        chuoiChat2=false;
        if(!this.baiDanhMoi.isEmpty()){
            this.baiDanhMoi.clear();
        }
        if(!this.baiDanhCu.isEmpty()){
            this.baiDanhCu.clear();
        }
        for(int i=0;i<4;i++){
            if(this.nguoiChois[i].boLuot){
                this.nguoiChois[i].boLuot=false;
                nguoiChois[i].chiChat2=false;
//                this.avtnguoichoi[i].repaint();
                //TODO: do sth here
            }
        }
        /**
         * cập nhật trạng thái khi vào lượt mới
         */
        for(NguoiChoi x:nguoiChois){
            x.thayDoiTrangThai(new ArrayList<LaBai>(),0);
        }
        nguoiDangDanh=nguoiDanhCuoi;
        raBai();
    }

    /**
     * chuyển người chơi đánh bài
     */
    private void raBai() {
        /**
         * chặt hai kể cả khi đã bỏ lượt
         */
        if(chuoiChat2){
            for(int i=0;i<4;i++){
                if(nguoiChois[i].boLuot) nguoiChois[i].chiChat2=true;
                this.nguoiChois[i].boLuot=false;
            }
        }
        /**
         * bỏ qua người chơi bỏ lượt
         */
        if (!this.ketThuc()) {
            if(!this.ketThucLuot()){
                while(nguoiChois[nguoiDangDanh].boLuot||!this.nguoiChois[nguoiDangDanh].isActive()){
                    if(nguoiDangDanh<3) nguoiDangDanh++;
                    else nguoiDangDanh=0;
                }
                //TODO: âm thanh
                //            try {
                //                this.amThanh("src/AmThanh/chuyennguoidanh.wav");
                //            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                //                Logger.getLogger(TienLen.class.getName()).log(Level.SEVERE, null, ex);
                //            }
                if(nguoiDangDanh==0){
                    this.boChonAll();
                    thoiGianMayDanh=15;
                }else{
                    resetMayChon();
                    thoiGianMayDanh=15;
                }
                batDauChoChon();
            }
        }
    }

    /**
     * khởi tạo thời gian và hoạt ảnh đếm ngược khi bắt đầu giai đoạn chờ chọn
     */
    private void batDauChoChon() {
        switch (nguoiDangDanh){
            case 0:
                vienDemNguoc.setDiemDau(new PointF(BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarX()+
                        ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarY()));
                break;
            case 1:
                vienDemNguoc.setDiemDau(new PointF(BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarX()+
                        ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarY()));
                break;
            case 2:
                vienDemNguoc.setDiemDau(new PointF(BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarX()+
                        ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarY()));
                break;
            case 3:
                vienDemNguoc.setDiemDau(new PointF(BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarX()+
                        ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarY()));
                break;
        }
        this.thoiGianDemNguoc= ThongSo.TranDau.THOI_GIAN_CHO_CHON;
        vienDemNguoc.tinhDuongVe(thoiGianDemNguoc);
        chuDemNguoc.setNoiDung(String.valueOf((int)thoiGianDemNguoc));
        giaiDoan= ChoiVoiMay.GiaiDoan.CHO_CHON;
    }
    /**
     * bỏ chọn và không thể chọn tất cả các lá bài trên tay
     */
    private void boChonAll() {
        for(int i=0;i<this.nguoiChois[0].soLa();i++){
            this.nguoiChois[0].setCoTheChon(i, false);
            if(this.nguoiChois[0].isDuocChon(i)) this.boChonLaBai(i);
        }
    }

    /**
     * bỏ chọn lá bài trên tay
     * @param i lá thứ i trên tay
     */
    private void boChonLaBai(int i) {
        NutLaBai temp=baiTrenTay.get(i);
        temp.setViTri(new PointF(temp.getHitbox().left,
                temp.getHitbox().top + ThongSo.LaBai.getKichThuocLaBaiCao() / 4));
        nguoiChois[0].getLaBai(i).setDuocChon(false);
        baiTrenTay.get(i).setDuocBam(false);
    }

    private boolean ketThuc() {
        for(int i=0;i<4;i++){
            if(this.nguoiChois[i].soLa()==0||this.nguoiChois[i].anhLaKeMayMan) {
                this.nguoiChois[i].boLuot=false;
                giaiDoan= ChoiVoiMay.GiaiDoan.KET_THUC;
                this.nguoiThang=i;
//                this.tinhThuNhap();
                if(this.nguoiChois[i].anhLaKeMayMan) chuDemNguoc.setNoiDung(ThongSo.DemNguoc.NOI_DUNG_TU_QUY);
                else chuDemNguoc.setNoiDung(ThongSo.DemNguoc.NOI_DUNG_NHAT);
                nguoiDangDanh=i;
                if(i==0) chuThongBaoKetQua.setNoiDung(ThongSo.ThongBaoKetQua.NOI_DUNG_THANG);
                else chuThongBaoKetQua.setNoiDung(ThongSo.ThongBaoKetQua.NOI_DUNG_THUA);
                //TODO: âm thanh
//                if(i==0) try {
//                    this.amThanh("src/AmThanh/thang.wav");
//                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
//                    Logger.getLogger(TienLen.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                else try {
//                    this.amThanh("src/AmThanh/thua.wav");
//                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
//                    Logger.getLogger(TienLen.class.getName()).log(Level.SEVERE, null, ex);
//                }
                //TODO: nút bấm
//                this.nuttamdung.setEnabled(false);
//                this.nutboluot.setEnabled(false);
//                this.nutdanh.setEnabled(false);
                //TODO: thông báo chiến thắng
//                this.timer=new Timer(2000,(ActionEvent e)->{
//                    if(isdelayed){
//                        if(nguoithang==0) this.hienThongBao(2);
//                        else this.hienThongBao(3);
//                        isdelayed=false;
//                        timer.stop();
//                    }else {
//                        isdelayed=true;
//                    }
//                });
//                this.timer.start();
                return true;
            }
        }
        return false;
    }

    private void tinhThuNhap() {
        int dem=0;
        for(int i=0;i<4;i++){
            if(i!=this.nguoiThang) {
                this.nguoiChois[i].thuNhap=-10*this.nguoiChois[i].soLa();
                dem+=this.nguoiChois[i].thuNhap;
            }
        }
        this.nguoiChois[this.nguoiThang].thuNhap=-dem;
    }
    private boolean ketThucLuot() {
        int dem=0;
        for(int i=0;i<4;i++){
            if(this.nguoiChois[i].boLuot) dem++;
        }
        if(dem>=3){
            this.luotMoi();
            return true;
        }else return false;
    }
    /**
     * tải lại dữ liệu như mới
     */
    public void taiLai() {
        giaiDoan= ChoiVoiMay.GiaiDoan.BAT_DAU;
    }
}
