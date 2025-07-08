package com.gamebai.tienlenmiennam.trangthaigame;

import static com.gamebai.tienlenmiennam.trangthaigame.ChoiVoiMay.GiaiDoan.BAT_DAU;
import static com.gamebai.tienlenmiennam.trangthaigame.ChoiVoiMay.GiaiDoan.CHIA_BAI;
import static com.gamebai.tienlenmiennam.trangthaigame.ChoiVoiMay.GiaiDoan.CHO_CHON;
import static com.gamebai.tienlenmiennam.trangthaigame.ChoiVoiMay.GiaiDoan.CHO_NGUOI_CHOI;
import static com.gamebai.tienlenmiennam.trangthaigame.ChoiVoiMay.GiaiDoan.DANH_BAI;
import static com.gamebai.tienlenmiennam.trangthaigame.ChoiVoiMay.GiaiDoan.KET_THUC;
import static com.gamebai.tienlenmiennam.trangthaigame.ChoiVoiMay.GiaiDoan.LAT_BAI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.hotro.PhuongThucBitmap;
import com.gamebai.tienlenmiennam.hotro.ThongSo;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoiOnline extends TrangThaiCoBan implements TrangThaiGame, PhuongThucBitmap {
    private MainActivity mainActivity;
    /**
     * UI
     */
    private Bitmap playingBackground,avatar;
    private int demLaBaiDiChuyen,demLaDenDich,demSoFrame;
    private List<NutLaBai> baiTrenTay,baiDanhCu,baiDanhMoi,boBai;
    private Chu chuDemNguoc, chuCanhBao, chuDemLaBai,chuThuNhap,
            chuThongBaoKetQua,chuTen,chuTien, chuMaPhong,chuDangChoNguoiChoi;
    private Paint paintKhongBamDuoc,paintDaDanh,paintThuNhapThang,paintThuNhapThua;
    private Nut nutDanh,nutBoLuot,nutThoat,nutRiengTuCongKhai, nutSanSang;
    private VienDemNguoc vienDemNguoc;
    /**
     * Các tên trường trong realtime database
     */
    public static String TEN_BANG="Phong";
    public String maPhong;
    public static String TEN_TRUONG_GIAI_DOAN="GiaiDoan";
    private ChoiVoiMay.GiaiDoan giaiDoan;
    public static String TEN_TRUONG_KET_THUC_DEM_NGUOC="KetThucDemNguoc";
    private long ketThucDemNguoc;
    public static String TEN_TRUONG_NGUOI_CHOI="NguoiChoi";
    private NguoiChoi[] nguoiChois;
    public static final String TEN_TRUONG_NGUOI_DANG_DANH_THEO_SO_THU_TU="NguoiDangDanh";
    public static final String TEN_TRUONG_RIENG_TU="RiengTu";
    public static final String TEN_TRUONG_NGUOI_DANH_CUOI_THEO_SO_THU_TU="NguoiDanhCuoi";
    public static final String TEN_TRUONG_TAY_BAI_CU="TayBaiCu";
    public static final String TEN_TRUONG_TAY_BAI_MOI="TayBaiMoi";
    public static final String TEN_TRUONG_YEU_CAU_DANH_BAI="YeuCauDanhBai";
    private BoBai thongTinBoBai;
    private KiemTraTayBai kiemTraBaiDanh;
    private LaBai laCuoi;
    private TayBai tayBaiCuoi;
    private ArrayList<Integer> tayBaiMoi;
    private boolean hopLe,hoanThanhNhanBai;
    private boolean daCapNhatNguoiDangDanh,daCapNhatThoiGianKetThuc;
    private int nguoiThang,nguoiDanhCuoi,nguoiDangDanh,doDaiTayBaiCuoi,soNguoiChoi;
    private float thoiGianDemNguoc,thoiGianHienCanhBao;
    private long thoiDiemThoatGame,doLechThoiGianVoiServer=0;
    /**
     * firebase
     */
    private FirebaseFirestore firestoreDatabase;
    private FirebaseAuth auth;
    private FirebaseDatabase realtimeDatabase;
    /**
     * realtime database reference
     */
    public DatabaseReference nguoiChoiOnlineReference;
    private DatabaseReference nguoiChoiKhacReference;
    private DatabaseReference giaiDoanReference;
    private DatabaseReference ketThucDemNguocReference;
    private DatabaseReference riengTuReference;
    private DatabaseReference tayBaiMoiReference;
    private DatabaseReference ketNoiReference;
    private DatabaseReference nguoiDangDanhReference;
    private DatabaseReference doLechThoiGianReference;
//    private DatabaseReference chiChat2Reference;
    private DatabaseReference nguoiChoiReference;
    /**
     * realtime listener
     */
    private ChildEventListener nguoiChoiKhacListener;
    private ValueEventListener giaiDoanListener;
    private ValueEventListener riengTuListener;
    private ValueEventListener ketThucDemNguocListener;
    private ValueEventListener tayBaiMoiListener;
    private ValueEventListener theoDoiNguoiChoiOnlineListener;
    private ValueEventListener nguoiDangDanhListener;
    private ValueEventListener doLechThoiGianListener;
//    private ValueEventListener chiChat2Listener;
    private ValueEventListener nguoiChoiListener;
    /**
     * kiểm tra các listener đã hoàn thành lấy dữ liệu lần đầu chưa
     */
    private boolean daKetNoi;
    private boolean daTaiNguoiChoi;
    private boolean daTaiNguoiChoiKhac;
    private int demNguoiChoiDaThem;
    private boolean daTaiGiaiDoan;
    private boolean taiLaiGame;
    private boolean daTaiRiengTu;
    private boolean daTaiKetThucDemNguoc;
    private boolean daTaiTayBaiMoi;
    private boolean daTaiBaiDanhCu;
    private boolean daTaiBaiTrenTay;
    private boolean daTaiNguoiDangDanh;
    private boolean daBatDauNhanBai;
    private boolean daTaiDoLechThoiGian;
    private boolean dauLuot;
    private boolean daYeuCauDanh;

    public ChoiOnline(Game game, MainActivity mainActivity){
        super(game);
        this.mainActivity=mainActivity;
        /**
         * firebase
         */

        firestoreDatabase =FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        realtimeDatabase=FirebaseDatabase
                .getInstance("https://tienlenmiennam-d2c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
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
        chuMaPhong =new Chu(mainActivity.getString(R.string.game_ma_phong),paintCanhBao);
        Paint paintDangChoNguoiChoi=new Paint(paintCanhBao);
        paintDangChoNguoiChoi.setTextAlign(Paint.Align.CENTER);
        paintDangChoNguoiChoi.setTextSize(ThongSo.CanhBaoChonBai.getKichCo()+10);
        chuDangChoNguoiChoi=new Chu(MainActivity.getContext()
                .getString(R.string.game_dang_cho_nguoi_choi),paintDangChoNguoiChoi);
        chuDangChoNguoiChoi.setViTri((float) MainActivity.chieuRongManHinh /2,
                (MainActivity.chieuCaoManHinh+chuDangChoNguoiChoi.getHitBox().height())/2);
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
        chuThongBaoKetQua.setViTri((float) (MainActivity.chieuRongManHinh) /2,
                (MainActivity.chieuCaoManHinh +chuThongBaoKetQua.getHitBox().height()) /2);
        Paint paintChuTen=new Paint();
        paintChuTen.setTextAlign(Paint.Align.CENTER);
        paintChuTen.setColor(Color.LTGRAY);
        paintChuTen.setTextSize(ThongSo.ThongTinNguoiChoiTrongTran.getKichCo());
        paintChuTen.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        chuTen =new Chu(ThongSo.ThongTinNguoiChoiTrongTran.NOI_DUNG_TEN,paintChuTen);
        Paint paintChuTien=new Paint();
        paintChuTien.setTextAlign(Paint.Align.CENTER);
        paintChuTien.setColor(Color.LTGRAY);
        paintChuTien.setTextSize(ThongSo.ThongTinNguoiChoiTrongTran.getKichCo());
        paintChuTien.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC));
        chuTien =new Chu(ThongSo.ThongTinNguoiChoiTrongTran.NOI_DUNG_TIEN,paintChuTien);
        paintThuNhapThang=new Paint();
        paintThuNhapThang.setTextAlign(Paint.Align.CENTER);
        paintThuNhapThang.setColor(Color.GREEN);
        paintThuNhapThang.setTextSize(ThongSo.ThongTinNguoiChoiTrongTran.getKichCo()+15);
        paintThuNhapThang.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        paintThuNhapThua=new Paint(paintThuNhapThang);
        paintThuNhapThua.setColor(Color.RED);
        chuThuNhap =new Chu(String.valueOf(0),paintThuNhapThang);
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
                        R.drawable.tro_lai_da_bam,options),ThongSo.ThongSoManHinh.getTiLe1Chieu(),
                ThongSo.ThongSoManHinh.getTiLe1Chieu(),true);
        nutThoat=new NutBatTat(temp,
                getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),
                                R.drawable.tro_lai_chua_bam,options),ThongSo.ThongSoManHinh.getTiLe1Chieu(),
                        ThongSo.ThongSoManHinh.getTiLe1Chieu(),true),
                0,0,temp.getWidth(),temp.getHeight());
        temp=getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.button_cong_khai,options),ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH,
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,true);
        nutRiengTuCongKhai=new NutBatTat(
                getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),
                                R.drawable.button_rieng_tu,options),
                        ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH,
                        ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,true),
                temp,nutThoat.getHitbox().right
                        +ThongSo.UI.KHOANG_CACH_GIUA_PHAN_TU_NGANG
                        *ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH,
                (nutThoat.getHitbox().height()-temp.getHeight())/2,
                temp.getWidth(),temp.getHeight());
        chuMaPhong.setViTri(nutRiengTuCongKhai.getHitbox().right
                        +ThongSo.UI.KHOANG_CACH_GIUA_PHAN_TU_NGANG
                        *ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH,
                (nutThoat.getHitbox().height()+ chuMaPhong.getHitBox().height())/2);
        temp=getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.button_san_sang,options),
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH,
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,true);
        nutSanSang=new NutBatTat(getBitmapTheoTiLe(BitmapFactory.decodeResource(mainActivity
                        .getResources(),R.drawable.button_chua_san_sang,options),
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH,
                ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,true), temp,
                (float) (MainActivity.chieuRongManHinh- temp.getWidth()) /2,
                (float) (MainActivity.chieuCaoManHinh - temp.getHeight()) /2,
                temp.getWidth(),temp.getHeight());
        /**
         * cờ kiểm tra tải
         */
        taiLaiGame=false;
        daKetNoi=false;
        daTaiNguoiChoi=false;
        daTaiNguoiChoiKhac=false;
        demNguoiChoiDaThem=0;
        daTaiGiaiDoan=false;
        daTaiRiengTu=false;
        daTaiKetThucDemNguoc=false;
        daTaiTayBaiMoi=false;
        daTaiBaiDanhCu=false;
        daTaiBaiTrenTay=false;
        daTaiNguoiDangDanh=false;
        /**
         * start game
         */
        hopLe=true;
        kiemTraBaiDanh=new KiemTraTayBai();
    }

    /**
     * xử lý sự kiện chạm màn hình
     *
     * @param event
     */
    @Override
    public void touchEvent(MotionEvent event) {
        if (hoanTatTaiLai()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN
                    &&hoanTatTaiLai()) {
                if (giaiDoan == CHO_CHON&&nguoiDangDanh==0) {
                    for(NutLaBai nutLaBai: baiTrenTay){
                        if(nguoiChois[0].getLaBai(baiTrenTay.indexOf(nutLaBai)).isCoTheChon()
                                &&nutLaBai.isEventHere(new PointF(event.getX(),event.getY()))) {
                            if (nutLaBai.isDuocBam()) {
                                chonLaBai(baiTrenTay.indexOf(nutLaBai));
                            } else {
                                boChonLaBai(baiTrenTay.indexOf(nutLaBai));
                            }
                            break;
                        }
                    }
                    if(nutDanh.isEventHere(new PointF(event.getX(),event.getY()))) {
                        danh();
                    }
                    if(nguoiDanhCuoi!=0&&nutBoLuot.isEventHere(new PointF(event.getX(),event.getY()))) {
                        boLuot();
                    }
                }
                if(nutThoat.isEventHere(new PointF(event.getX(),event.getY()))) {
                    if (giaiDoan== ChoiVoiMay.GiaiDoan.CHO_NGUOI_CHOI) {
                        if(nutThoat.isDuocBam()) {
                            Toast.makeText(mainActivity, mainActivity.getString(R.string.game_da_san_sang),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            thoat();
                        }
                    }
                }
                if(nutRiengTuCongKhai.isEventHere(new PointF(event.getX(),event.getY()))) {
                    if (giaiDoan==ChoiVoiMay.GiaiDoan.CHO_NGUOI_CHOI
                            &&nguoiChois[0].soThuTu==0) {
                        if(nutRiengTuCongKhai.isDuocBam()) {
                            nutRiengTuCongKhai.setDuocBam(false);
                            thayDoiRiengTuCongKhai();
                        }
                        else {
                            nutRiengTuCongKhai.setDuocBam(true);
                            thayDoiRiengTuCongKhai();
                        }
                    }
                }
                if(nutSanSang.isEventHere(new PointF(event.getX(),event.getY()))){
                    if(giaiDoan== ChoiVoiMay.GiaiDoan.CHO_NGUOI_CHOI) {
                        if(nutSanSang.isDuocBam()){
                            realtimeDatabase.getReference(TEN_BANG+"/"
                                            +maPhong+"/"+TEN_TRUONG_NGUOI_CHOI+"/"+nguoiChois[0].getUid()+"/"
                                            +NguoiChoi.TEN_TRUONG_SAN_SANG)
                                    .setValue(false);
                            nutSanSang.setDuocBam(false);
                        }else{
                            realtimeDatabase.getReference(TEN_BANG+"/"
                                            +maPhong+"/"+TEN_TRUONG_NGUOI_CHOI+"/"+nguoiChois[0].getUid()+"/"
                                            +NguoiChoi.TEN_TRUONG_SAN_SANG)
                                    .setValue(true);
                            nutSanSang.setDuocBam(true);
                        }
                    }
                }
            }
        }
    }

    private void thayDoiRiengTuCongKhai() {
        realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_RIENG_TU)
                .setValue(nutRiengTuCongKhai.isDuocBam());
    }

    /**
     * thoát game, đặt trạng thái người chơi trong trận là offline, server sẽ xử lý phần còn lại
     */
    public void thoat() {
        huyDangKyListener(true);
        HashMap<String,Object> nguoiChoiOffline=new HashMap<>();
        nguoiChoiOffline.put(NguoiChoi.TEN_TRUONG_TRANG_THAI_ONLINE,"offline");
        nguoiChoiOnlineReference.updateChildren(nguoiChoiOffline);
        game.setTrangThaiGame(ThongSo.TrangThaiGame.TRANG_THAI_SANH_CHO,null);
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
        if(!hoanTatTaiLai()){
            chuDangChoNguoiChoi.setNoiDung(mainActivity
                    .getString(R.string.game_dang_tai));
            chuDangChoNguoiChoi.ve(c);
            return;
        }
        switch (giaiDoan){
            case CHO_NGUOI_CHOI:
                nutThoat.ve(c);
                veAvartar(c);
                if(soNguoiChoi>1){
                    nutSanSang.ve(c);
                }else{
                    chuDangChoNguoiChoi.setNoiDung(mainActivity.getString(R.string.game_dang_cho_nguoi_choi));
                    chuDangChoNguoiChoi.ve(c);
                }
                nutRiengTuCongKhai.ve(c);
                chuMaPhong.ve(c);
                break;
            case CHUAN_BI:
                veAvartar(c);
                nutRiengTuCongKhai.ve(c);
                chuMaPhong.ve(c);
                break;
            case BAT_DAU:
                veAvartar(c);
                nutRiengTuCongKhai.ve(c);
                chuMaPhong.ve(c);
                chuDangChoNguoiChoi.setNoiDung(mainActivity.getString(R.string.game_bat_dau_sau)
                        +" "+(int)thoiGianDemNguoc);
                chuDangChoNguoiChoi.ve(c);
                break;
            case CHIA_BAI:
                for(int i=boBai.size()-1;i>-1;i--){
                    NutLaBai nutLaBai=boBai.get(i);
                    if ((!nutLaBai.isDaDenDich()||
                            nutLaBai.getChiSoSoHuu()==BoBai.ChiSoSoHuu.NGUOI_CHOI_1)
                            &&nutLaBai.getChiSoSoHuu().getChiSo()<=soNguoiChoi) {
                        c.rotate(nutLaBai.getGocQuay(),nutLaBai.getHitbox().centerX(),
                                nutLaBai.getHitbox().centerY());
                        nutLaBai.veLungBai(c);
                        c.rotate(-nutLaBai.getGocQuay(),nutLaBai.getHitbox().centerX(),
                                nutLaBai.getHitbox().centerY());
                    }
                }
                veAvartar(c);
                nutRiengTuCongKhai.ve(c);
                chuMaPhong.ve(c);
                demSoFrame++;
                break;
            case LAT_BAI:
                for(NutLaBai nutLaBai: baiTrenTay){
                    if(nutLaBai.isKickHoat()) nutLaBai.ve(c);
                    else nutLaBai.veLungBai(c);
                }
                veAvartar(c);
                nutRiengTuCongKhai.ve(c);
                chuMaPhong.ve(c);
                veDemLaBai(c);
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
                    if (nguoiDanhCuoi!=0) {
                        nutBoLuot.ve(c);
                    }
                }
                veAvartar(c);
                veDemLaBai(c);
                veDemNguoc(c);
                nutRiengTuCongKhai.ve(c);
                chuMaPhong.ve(c);
                if(!hopLe&&thoiGianHienCanhBao>0) {
                    chuCanhBao.ve(c);
                }
                break;
            case DANH_BAI:
                synchronized (baiTrenTay){
                    for(NutLaBai nutLaBai: baiDanhCu) {
                        nutLaBai.ve(c, paintDaDanh);
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
                veAvartar(c);
                veDemLaBai(c);
                nutRiengTuCongKhai.ve(c);
                chuMaPhong.ve(c);
                break;
            case KET_THUC:
                for(NutLaBai nutLaBai: baiTrenTay)
                    nutLaBai.ve(c);
                veDemLaBai(c);
                veAvartar(c);
                nutRiengTuCongKhai.ve(c);
                chuMaPhong.ve(c);
                veDemNguoc(c);
                veThongBaoKetThuc(c);
                break;
        }
    }

    private void veThongBaoKetThuc(Canvas c) {
        chuThongBaoKetQua.ve(c);
    }

    /**
     * vẽ các ô đếm lá bài cho đối thủ, khi kết thúc trận sẽ hiện các lá bài còn lại của đối thủ
     * @param c
     */
    private void veDemLaBai(Canvas c) {
        if (giaiDoan != KET_THUC) {
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
            chuDemLaBai.setNoiDung(String.valueOf(nguoiChois[1].soLaBai));
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
                chuDemLaBai.setNoiDung(String.valueOf(nguoiChois[2].soLaBai));
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
                chuDemLaBai.setNoiDung(String.valueOf(nguoiChois[3].soLaBai));
                chuDemLaBai.ve(c);
            }
        }else{
            for(int i=1;i<nguoiChois.length;i++){
                if(nguoiChois[i].isActive()){
                    float viTriX=nguoiChois[i].getChiSoSoHuu().getViTriDemLaBaiX();
                    float viTriY=nguoiChois[i].getChiSoSoHuu().getViTriDemLaBaiY()+
                            ThongSo.LaBai.getKichThuocLaBaiNhoCao()/2-
                            ThongSo.LaBai.getKichThuocLaBaiNhoCao()*
                                    ((float) (nguoiChois[i].soLaBai - 1) /4+1)/2;
                    if(i==2){
                        /**
                         * trường hợp người chơi phía trên
                         */
                        viTriX=nguoiChois[i].getChiSoSoHuu().getViTriDemLaBaiX();
                        viTriY=nguoiChois[i].getChiSoSoHuu().getViTriDemLaBaiY();
                        synchronized (nguoiChois[i].getBaiTrenTay()){
                            for(LaBai laBai:nguoiChois[i].getBaiTrenTay()){
                                c.drawBitmap(laBai.getHinhAnhTheoTiLe(ThongSo.LaBai.TI_LE_LA_BAI_NHO,
                                                ThongSo.LaBai.TI_LE_LA_BAI_NHO),
                                        viTriX+nguoiChois[i].getBaiTrenTay().indexOf(laBai)*
                                                ThongSo.LaBai.getKichThuocLaBaiNhoRong()/3,
                                        viTriY,
                                        null);
                            }
                        }
                    }else{
                        /**
                         * trường hợp người chơi phía 2 bên
                         */
                        synchronized (nguoiChois[i].getBaiTrenTay()){
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
    }

    /**
     * vẽ các avatar người chơi cùng các thông tin lên màn hình
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
        chuTien.setNoiDung(nguoiChois[0].getTien()+"$");
        chuTien.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarX()
                        +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarY()
                        -vienDemNguoc.getPaint().getStrokeWidth());
        chuTien.ve(c);
        if (nguoiChois[0].soThuTu==0) {
            chuTen.setNoiDung(nguoiChois[0].getTenDangNhap()+"\n(Chủ)");
        }else{
            chuTen.setNoiDung(nguoiChois[0].getTenDangNhap());
        }
        chuTen.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarX()
                        +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                chuTien.getHitBox().top);
        chuTen.ve(c);
        if(giaiDoan==KET_THUC){
            if(nguoiThang==0) chuThuNhap.setPaint(paintThuNhapThang);
            else chuThuNhap.setPaint(paintThuNhapThua);
            chuThuNhap.setNoiDung(nguoiChois[0].thuNhap+"$");
            chuThuNhap.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarX()
                            +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                    chuTen.getHitBox().top);
            chuThuNhap.ve(c);
        }
        if (nguoiChois[1].isActive()) {
            if(nguoiChois[1].boLuot) temp= paintKhongBamDuoc;
            else temp=null;
            c.drawBitmap(avatar,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarX(),
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarY(),
                    temp);
            chuTien.setNoiDung(nguoiChois[1].getTien()+"$");
            chuTien.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarX()
                            +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarY()
                            -vienDemNguoc.getPaint().getStrokeWidth());
            chuTien.ve(c);
            if (nguoiChois[1].soThuTu==0) {
                chuTen.setNoiDung(nguoiChois[1].getTenDangNhap()+"\n(Chủ)");
            }else{
                chuTen.setNoiDung(nguoiChois[1].getTenDangNhap());
            }
            chuTen.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarX()
                            +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                    chuTien.getHitBox().top);
            chuTen.ve(c);
            if(giaiDoan==KET_THUC){
                if(nguoiThang==1) chuThuNhap.setPaint(paintThuNhapThang);
                else chuThuNhap.setPaint(paintThuNhapThua);
                chuThuNhap.setNoiDung(nguoiChois[1].thuNhap+"$");
                chuThuNhap.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_2.getViTriAvartarX()
                                +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        chuTen.getHitBox().top);
                chuThuNhap.ve(c);
            }
        }
        if(nguoiChois[2].isActive()) {
            if(nguoiChois[2].boLuot) temp= paintKhongBamDuoc;
            else temp=null;
            c.drawBitmap(avatar,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarX(),
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarY(),
                    temp);
            if (nguoiChois[2].soThuTu==0) {
                chuTen.setNoiDung(nguoiChois[2].getTenDangNhap()+"\n(Chủ)");
            }else{
                chuTen.setNoiDung(nguoiChois[2].getTenDangNhap());
            }
            chuTen.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarX()
                            +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarY()
                            +ThongSo.Avatar.getKichThuocCaoAvatar()
                            +vienDemNguoc.getPaint().getStrokeWidth()
                            +chuTien.getHitBox().height());
            chuTen.ve(c);
            chuTien.setNoiDung(nguoiChois[2].getTien()+"$");
            chuTien.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarX()
                            +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                    chuTen.getHitBox().bottom+chuTien.getHitBox().height());
            chuTien.ve(c);
            if(giaiDoan==KET_THUC){
                if(nguoiThang==2) chuThuNhap.setPaint(paintThuNhapThang);
                else chuThuNhap.setPaint(paintThuNhapThua);
                chuThuNhap.setNoiDung(nguoiChois[2].thuNhap+"$");
                chuThuNhap.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_3.getViTriAvartarX()
                                +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        chuTien.getHitBox().bottom+chuThuNhap.getHitBox().height());
                chuThuNhap.ve(c);
            }
        }
        if(nguoiChois[3].isActive()) {
            if(nguoiChois[3].boLuot) temp= paintKhongBamDuoc;
            else temp=null;
            c.drawBitmap(avatar,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarX(),
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarY(),
                    temp);
            chuTien.setNoiDung(nguoiChois[3].getTien()+"$");
            chuTien.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarX()
                            +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                    BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarY()
                            -vienDemNguoc.getPaint().getStrokeWidth());
            chuTien.ve(c);
            if (nguoiChois[3].soThuTu==0) {
                chuTen.setNoiDung(nguoiChois[3].getTenDangNhap()+"\n(Chủ)");
            }else{
                chuTen.setNoiDung(nguoiChois[3].getTenDangNhap());
            }
            chuTen.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarX()
                            +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                    chuTien.getHitBox().top);
            chuTen.ve(c);
            if(giaiDoan==KET_THUC){
                if(nguoiThang==3) chuThuNhap.setPaint(paintThuNhapThang);
                else chuThuNhap.setPaint(paintThuNhapThua);
                chuThuNhap.setNoiDung(nguoiChois[3].thuNhap+"$");
                chuThuNhap.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_4.getViTriAvartarX()
                                +ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        chuTen.getHitBox().top);
                chuThuNhap.ve(c);
            }
        }
        if(giaiDoan==CHO_CHON) vienDemNguoc.ve(c);
    }
    /**
     * vẽ đếm ngược lên người chơi tương ứng
     * @param c
     */
    private void veDemNguoc(Canvas c) {
        switch (nguoiDangDanh){
            case 0:
                chuDemNguoc.setViTri(BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarX()+
                                ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getViTriAvartarY()+
                                (ThongSo.Avatar.getKichThuocCaoAvatar()+
                                        chuDemNguoc.getHitBox().height())/2);
                break;
            case 1:
                chuDemNguoc.setViTri(MainActivity.chieuRongManHinh-ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        (MainActivity.chieuCaoManHinh + chuDemNguoc.getHitBox().height()) /2);
                break;
            case 2:
                chuDemNguoc.setViTri((float) MainActivity.chieuRongManHinh /2,
                        (ThongSo.Avatar.getKichThuocCaoAvatar()+ chuDemNguoc.getHitBox().height())/2);
                break;
            case 3:
                chuDemNguoc.setViTri(ThongSo.Avatar.getKichThuocRongAvatar()/2,
                        (MainActivity.chieuCaoManHinh + chuDemNguoc.getHitBox().height()) /2);
                break;
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
        if(!hoanTatTaiLai())
            return;
        else taiLaiGame=false;
        switch (giaiDoan){
            case CHO_NGUOI_CHOI:
                ketThucDemNguoc=0;
                break;
            case CHUAN_BI:
                hoanThanhNhanBai=false;
                daBatDauNhanBai=false;
                if(ketThucDemNguoc!=0) {
                    thoiGianDemNguoc= ThongSo.TranDau.THOI_GIAN_CHO_BAT_DAU;
                    giaiDoan=BAT_DAU;
                }
                break;
            case BAT_DAU:
                if (!hoanThanhNhanBai&&!daBatDauNhanBai) {
                    daBatDauNhanBai=true;
                    realtimeDatabase.getReference(TEN_BANG+"/"+maPhong +"/"+TEN_TRUONG_NGUOI_CHOI
                            +"/"+nguoiChois[0].getUid()+"/"+NguoiChoi.TEN_TRUONG_BAI_TREN_TAY).get()
                            .addOnSuccessListener(dataSnapshot -> {
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
                                this.laCuoi= LaBai.getTempInstance(2,-1);
                                this.tayBaiCuoi=TayBai.KhongCo;
                                this.doDaiTayBaiCuoi =0;
                                dauLuot=true;
                                demLaBaiDiChuyen=0;
                                demLaDenDich=0;
                                demSoFrame=0;
                                thoiDiemThoatGame=0;
                                nguoiDanhCuoi=nguoiDangDanh;
                                boBai=thongTinBoBai.danhSachNutLaBaiFake();
                                thongTinBoBai.xoaMaTran();
                                /**
                                 * lấy bài trên tay
                                 */
                                int dem=0,so,chat,demLaTrenTay=0;
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if(dataSnapshot1.getValue(Long.class)==1){
                                        so=dem/4;
                                        chat=dem%4;
                                        thongTinBoBai.setSoHuu(so+2,chat,nguoiChois[0]
                                                .getChiSoSoHuu().getChiSo());
                                        LaBai laBai=LaBai.timLaBaiTheoSoVaChat(so+2,chat);
                                        laBai.setDuocChon(false);
                                        laBai.setCoTheChon(false);
                                        nguoiChois[0].getBaiTrenTay().add(laBai);
                                        baiTrenTay.add(new NutLaBai(laBai,
                                                BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichX()
                                                        +ThongSo.LaBai.getKichThuocLaBaiRong()*demLaTrenTay,
                                                BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichY(),false));
                                        demLaTrenTay++;
                                        if(demLaTrenTay==13) break;
                                    }
                                    dem++;
                                }
                                hoanThanhNhanBai=true;
                            }).addOnFailureListener( e -> {
                                Toast.makeText(mainActivity,
                                        mainActivity.getString(R.string.loi)+" "+e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                hoanThanhNhanBai=false;
                            });
                }
                thoiGianDemNguoc-= (float) (delta);
                if(thoiGianDemNguoc<=0&&hoanThanhNhanBai)
                    giaiDoan= ChoiVoiMay.GiaiDoan.CHIA_BAI;
                break;
            case CHIA_BAI:
                /**
                 * do để đếm số frame nên ta cần đếm trong render,
                 * do đó ta cần đồng bộ nếu render chạy thì capNhap mới chạy
                 * nhưng mà chơi online thì vẫn phải chạy thôi
                 */
                if (!isCanvasNull) {
                    demSoFrame++;
                }
                for(int i=0;i<boBai.size();i++){
                    NutLaBai temp=boBai.get(i);
                    if(i<= demLaBaiDiChuyen &&!temp.isDaDenDich()){
                        temp.diChuyen(delta,true);
                        if(temp.isDaDenDich()){
                            demLaDenDich++;
                        }
                        if(demSoFrame>=2){
                            demLaBaiDiChuyen++;
                            demSoFrame=0;
                        }
                    }
                }
                if(demLaDenDich==52){
//                    for(NutLaBai nutLaBai: boBai){
//                        if(nutLaBai.getChiSoSoHuu()== BoBai.ChiSoSoHuu.NGUOI_CHOI_1)
//                            baiTrenTay.add(nutLaBai);
//                    }
//                    Collections.reverse(baiTrenTay);
                    demLaDenDich=1;
                    demSoFrame=0;
                    demLaBaiDiChuyen=0;
                    baiTrenTay.get(0).setKickHoat(true);
                    giaiDoan= ChoiVoiMay.GiaiDoan.LAT_BAI;
                }
                break;
            case LAT_BAI:
                if(demLaDenDich<baiTrenTay.size()){
                    baiTrenTay.get(demLaDenDich).setKickHoat(true);
                    demLaDenDich++;

                }else {
                    luotMoi();
                }
                break;
            case CHO_CHON:
                thoiGianDemNguoc-= (float) (delta);
                vienDemNguoc.tinhDuongVe(thoiGianDemNguoc);
                if(thoiGianHienCanhBao>0) thoiGianHienCanhBao-=(float) (delta);
                if(thoiGianDemNguoc>0){
                    chuDemNguoc.setNoiDung(String.valueOf((int)thoiGianDemNguoc));
                }else{
                    chuDemNguoc.setNoiDung("0");
                    if (nguoiDangDanh==0) {
                        /**
                         * mở đầu lượt mới không được bỏ lượt
                         */
                        if(nguoiDangDanh==nguoiDanhCuoi){
                            boChonAll();
                            nguoiChois[0].setDuocChon(0,true);
                            danh();
                        }else {
                            boLuot();
                        }
                    }else{
                        if (thoiGianDemNguoc<=-ThongSo.TranDau.THOI_GIAN_YEU_CAU_DANH_BAI) {
                            if (!daCapNhatThoiGianKetThuc&&!daCapNhatNguoiDangDanh
                                    &&!daYeuCauDanh) {
                                daYeuCauDanh =true;
                                realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"
                                                +TEN_TRUONG_NGUOI_CHOI+"/"+nguoiChois[0].getUid(
                                        +"/"+NguoiChoi.TEN_TRUONG_YEU_CAU_DANH_BAI).updateChildren(new HashMap<String,Object>());
                            }
                        }
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
                /**
                 * đảm bảo thời gian kết thúc đã được cập nhật
                 */
                if(done&&daCapNhatThoiGianKetThuc&&daCapNhatNguoiDangDanh) raBai();
                break;
            case KET_THUC:
                if(System.currentTimeMillis()-thoiDiemThoatGame>=0){
                    taiLai();
                }
                break;
        }
    }
    /**
     * thực hiện thao tác đánh đối với các người chơi khác nhau đặc biệt với bot
     */
    private void danh(){
        kiemTraBaiDanh.laBai.clear();
        if(this.nguoiDangDanh==0){
            for(int i=0;i<this.nguoiChois[0].soLa();i++){
                if(this.nguoiChois[0].getLaBai(i).isDuocChon()) {
                    kiemTraBaiDanh.laBai.add(this.nguoiChois[0].getLaBai(i));
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
                tayBaiCuoi=tayBaiMoi;
                doDaiTayBaiCuoi=0;
                laCuoi= kiemTraBaiDanh.laBai.get(kiemTraBaiDanh.laBai.size()-1);
                hopLe=true;
            }
            /**
             * đưa lên database
             */
            ArrayList<Integer> listBai=new ArrayList<>();
            for(NutLaBai x: baiDanhMoi){
                listBai.add(x.getLaBai().getSo());
                listBai.add(x.getLaBai().getChat());
            }
            /**
             * lưu người đánh cuối
             */
            nguoiDanhCuoi=nguoiDangDanh;
            realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_TAY_BAI_CU)
                    .setValue(listBai);
            listBai.clear();
            for(LaBai laBai: kiemTraBaiDanh.laBai){
                listBai.add(laBai.getSo());
                listBai.add(laBai.getChat());
            }
            realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_TAY_BAI_MOI)
                    .setValue(listBai);
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
            xepLaiBaiTrenTay();
        }else{
            for(int i=0;i<tayBaiMoi.size();i+=2){
                kiemTraBaiDanh.laBai.add(LaBai.timLaBaiTheoSoVaChat(tayBaiMoi.get(i),tayBaiMoi.get(i+1)));
            }
            kiemTraBaiDanh.sort();
            TayBai tayBaiMoi= kiemTraBaiDanh.baiDanhHopLe(tayBaiCuoi.ordinal(),
                    doDaiTayBaiCuoi,
                    laCuoi,
                    nguoiChois[nguoiDangDanh].chiChat2);
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
            if((laCuoi.getSo()==14&&tayBaiCuoi.ordinal()<=2)||
                    tayBaiCuoi==TayBai.TuQuy||
                    tayBaiCuoi==TayBai.Thong) nguoiChois[0].boLuot=false;
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
                for(int i=0;i<kiemTraBaiDanh.laBai.size();i++){
                    BoBai.ChiSoSoHuu temp1=BoBai.ChiSoSoHuu.timTheoSoHuu(nguoiChois[nguoiDangDanh]
                            .getChiSoSoHuu().getChiSo());
                    baiDanhMoi.add(new NutLaBai(kiemTraBaiDanh.laBai.get(i),
                            temp1.getDichX(),
                            temp1.getDichY(),
                            temp));
                    temp.x+= ThongSo.LaBai.getKichThuocLaBaiRong();
                }
            }
        }
//        if(nguoiDangDanh==soNguoiChoi-1) nguoiDangDanh=0;
//        else nguoiDangDanh++;
        dauLuot=false;
        giaiDoan= ChoiVoiMay.GiaiDoan.DANH_BAI;
    }
    private void boLuot() {
        dauLuot=false;
        if(nguoiDangDanh==0){
            nguoiChois[0].boLuot=true;
            ketThucLuot();
            realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"
                    +TEN_TRUONG_NGUOI_CHOI+"/"+nguoiChois[0].getUid()+"/"+NguoiChoi.TEN_TRUONG_BO_LUOT)
                    .setValue(true);
        }
        giaiDoan=DANH_BAI;
    }
    /**
     * tạo vị trí mới cho các lá bài trên tay sau khi đánh bài
     */
    private void xepLaiBaiTrenTay(){
        /**
         * căn giữa bài trên tay
         */
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
        khoiTaoLuotMoi();
        raBai();
    }

    /**
     * khởi tạo các giá trị cho lượt mới
     */
    private void khoiTaoLuotMoi() {
        this.laCuoi= LaBai.getTempInstance(2,-1);
        this.tayBaiCuoi=TayBai.KhongCo;
        this.doDaiTayBaiCuoi =0;
        dauLuot=true;
        if(!this.baiDanhMoi.isEmpty()){
            synchronized (baiDanhMoi){
                baiDanhMoi.clear();
            }
        }
        if(!this.baiDanhCu.isEmpty()){
            synchronized (baiDanhCu){
                this.baiDanhCu.clear();
            }
        }
        for(int i=0;i<soNguoiChoi;i++){
            if(this.nguoiChois[i].boLuot){
                this.nguoiChois[i].boLuot=false;
                nguoiChois[i].chiChat2=false;
            }
        }
    }

    /**
     * chuyển người chơi đánh bài
     */
    private void raBai() {
        daYeuCauDanh =false;
        /**
         * chặt hai kể cả khi đã bỏ lượt
         */
//        if(chuoiChat2){
//            for(int i=0;i<soNguoiChoi;i++){
//                if(nguoiChois[i].boLuot) nguoiChois[i].chiChat2=true;
//                this.nguoiChois[i].boLuot=false;
//            }
//        }
        /**
         * bỏ qua người chơi bỏ lượt
         */
        if (!this.ketThuc()) {
            if(nguoiDangDanh==0){
                this.boChonAll();
                this.nguoiChois[0].timCoTheChon(this.laCuoi,
                        this.tayBaiCuoi.ordinal(),
                        this.doDaiTayBaiCuoi,
                        this.thongTinBoBai);
            }
            batDauChoChon();
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
        thoiGianDemNguoc= (float) (ketThucDemNguoc - thoiGianHienTai()) /1000;
        vienDemNguoc.tinhDuongVe(thoiGianDemNguoc);
        chuDemNguoc.setNoiDung(String.valueOf((int)thoiGianDemNguoc));
        daCapNhatNguoiDangDanh=false;
        daCapNhatThoiGianKetThuc=false;
        giaiDoan= CHO_CHON;
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
        for(int i=0;i<soNguoiChoi;i++){
            if((i==0&&nguoiChois[i].soLa()==0)|| (i!=0&&nguoiChois[i].soLaBai==0)) {
                /**
                 * đã có người thắng, tiếp tục DANH_BAI cho đến khi lấy được đủ thông tin
                 */
                daCapNhatNguoiDangDanh=false;
                daCapNhatThoiGianKetThuc=false;
                /**
                 * huỷ hết listener trừ theo dõi online
                 */
                huyDangKyListener(false);
                nguoiChois[i].boLuot=false;
                nguoiDangDanh=i;
                nguoiThang=i;
                tinhThuNhap();
                chuDemNguoc.setNoiDung(ThongSo.DemNguoc.NOI_DUNG_NHAT);
                if(i==0) chuThongBaoKetQua.setNoiDung(ThongSo.ThongBaoKetQua.NOI_DUNG_THANG);
                else chuThongBaoKetQua.setNoiDung(ThongSo.ThongBaoKetQua.NOI_DUNG_THUA);
                /**
                 * Lấy lá bài của người chơi khác
                 */
                realtimeDatabase.getReference(TEN_BANG+"/"+maPhong).get()
                        .addOnSuccessListener(dataSnapshot -> {
                            for(int j=1;j<soNguoiChoi;j++){
                                if(j!=nguoiThang){
                                    if(dataSnapshot.child(String.valueOf(nguoiChois[j].soThuTu)).exists()){
                                        DataSnapshot dataSnapshot1=dataSnapshot
                                                .child(String.valueOf(nguoiChois[j].soThuTu));
                                        int dem=0,so,chat,soLaBai=0;
                                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                                            if (soLaBai<nguoiChois[j].soLaBai) {
                                                if(dataSnapshot2.getValue(Long.class)==1){
                                                    so=dem/4;
                                                    chat=dem%4;
                                                    synchronized (nguoiChois[j].getBaiTrenTay()){
                                                        nguoiChois[j].getBaiTrenTay()
                                                                .add(LaBai.timLaBaiTheoSoVaChat(so+2,chat));
                                                    }
                                                    soLaBai++;
                                                }
                                                dem++;
                                            }else break;
                                        }
                                    }
                                }
                            }
                            thoiDiemThoatGame=System.currentTimeMillis()
                                    + ThongSo.TranDau.THOI_GIAN_CHO_THOAT_GAME_MILISECOND;
                            /**
                             * hết tiền thì cook
                             */
                            if(nguoiChois[0].getTien()+nguoiChois[0].thuNhap<130){
                                new Handler(Looper.getMainLooper())
                                        .postDelayed(()->{
                                            thoat();
                                        }, 3000);
                            }
                            giaiDoan= KET_THUC;
                        }).addOnFailureListener(e -> {
                            Toast.makeText(mainActivity,mainActivity.getString(R.string.game_ket_thuc),
                                    Toast.LENGTH_SHORT).show();
                            taiLai();
                        });
                return true;
            }
        }
        return false;
    }

    private void tinhThuNhap() {
        int dem=0;
        for(int i=0;i<soNguoiChoi;i++){
            if(i!=this.nguoiThang) {
                if (i==0) {
                    this.nguoiChois[i].thuNhap=-ThongSo.TranDau.SO_TIEN_MOI_LA_BAI
                            *this.nguoiChois[i].soLa();
                }else{
                    this.nguoiChois[i].thuNhap=-ThongSo.TranDau.SO_TIEN_MOI_LA_BAI
                            *this.nguoiChois[i].soLaBai;
                }
                dem+=this.nguoiChois[i].thuNhap;
            }
        }
        this.nguoiChois[this.nguoiThang].thuNhap=-dem;
    }

    /**
     * kiểm tra liệu lượt có kết thúc và khởi tạo luôn lượt mới
     * @return
     */
    private boolean ketThucLuot() {
        int dem=0;
        for(int i=0;i<soNguoiChoi;i++){
            if(this.nguoiChois[i].boLuot) dem++;
        }
        if(dem>=soNguoiChoi-1){
            khoiTaoLuotMoi();
            return true;
        }else return false;
    }
    /**
     * tải lại dữ liệu phòng chơi
     */
    public void taiLai() {
        /**
         * tạm dừng vòng lặp nếu nó đang chạy và chạy lại ở cuối phương thức,
         * nếu nó đã dừng thì thôi bởi ta sẽ chạy lại ở ngoài
         */
        boolean tiepTucVongLap=false;
        if(!game.isTamDungVongLapGame()){
            game.setTamDungVongLapGame(true);
            tiepTucVongLap=true;
        }
        chuMaPhong.setNoiDung(mainActivity.getString(R.string.game_ma_phong)+" "+maPhong);
        taiLaiGame=true;
        daKetNoi=false;
        daTaiNguoiChoi=false;
        daTaiNguoiChoiKhac=false;
        demNguoiChoiDaThem=0;
        daTaiGiaiDoan=false;
        daTaiRiengTu=false;
        daTaiKetThucDemNguoc=false;
        daTaiTayBaiMoi=false;
        daTaiBaiDanhCu=true;
        daTaiBaiTrenTay=true;
        daTaiNguoiDangDanh=false;
        daTaiDoLechThoiGian=false;
        theoDoiOnline();
        /**
         * 1 số biến local
         */
        nutThoat.setDuocBam(false);
        if(tiepTucVongLap) game.setTamDungVongLapGame(false);
    }

    /**
     * huỷ đng ký các listener
     * @param huyTheoDoiOnline có hay không huỷ theo dõi trạng thái online của client
     */
    public void huyDangKyListener(boolean huyTheoDoiOnline) {
        if(nguoiChoiReference!=null&&nguoiChoiListener!=null){
            nguoiChoiReference.removeEventListener(nguoiChoiListener);
        }
        if(nguoiChoiOnlineReference!=null&&huyTheoDoiOnline){
            nguoiChoiOnlineReference.onDisconnect().cancel();
        }
        if (nguoiChoiKhacListener !=null&& nguoiChoiKhacReference !=null) {
            nguoiChoiKhacReference.removeEventListener(nguoiChoiKhacListener);
        }
        if(giaiDoanListener!=null&&giaiDoanReference!=null) {
            giaiDoanReference.removeEventListener(giaiDoanListener);
        }
        if(riengTuListener!=null&&riengTuReference!=null) {
            riengTuReference.removeEventListener(riengTuListener);
        }
        if(ketThucDemNguocListener!=null&&ketThucDemNguocReference!=null) {
            ketThucDemNguocReference.removeEventListener(ketThucDemNguocListener);
        }
        if(tayBaiMoiListener!=null&&tayBaiMoiReference!=null) {
            tayBaiMoiReference.removeEventListener(tayBaiMoiListener);
        }
        if(theoDoiNguoiChoiOnlineListener!=null&&ketNoiReference!=null) {
            ketNoiReference.removeEventListener(theoDoiNguoiChoiOnlineListener);
        }
        if(nguoiDangDanhListener!=null&&nguoiDangDanhReference!=null) {
            nguoiDangDanhReference.removeEventListener(nguoiDangDanhListener);
        }
        if(doLechThoiGianListener!=null&& doLechThoiGianReference !=null) {
            doLechThoiGianReference.removeEventListener(doLechThoiGianListener);
        }
//        if(chiChat2Listener!=null&&chiChat2Reference!=null) {
//            chiChat2Reference.removeEventListener(chiChat2Listener);
//        }
    }

    /**
     * tải lại bài đánh trên sân và bài trên tay, người đang đánh
     */
    private void taiLaiBai() {
        /**
         * tải lại bài đánh cũ là mảng gồm các cặp 2 phần tử
         * phần tử 1 là số bài, phần tử 2 là chất bài
         */
//        if(!baiDanhCu.isEmpty()) {
//            baiDanhCu.clear();
//        }
//        ArrayList<Integer> tayBaiCu=new ArrayList<>();
//        realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_TAY_BAI_CU)
//                .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                new Thread(()->{
//                    for (DataSnapshot x : dataSnapshot.getChildren()) {
//                        tayBaiCu.add(x.getValue(Long.class).intValue());
//                    }
//                    float tempX=(MainActivity.chieuRongManHinh-
//                            ThongSo.LaBai.getKichThuocLaBaiRong()*tayBaiCu.size()/2)/2;
//                    for(int i=0;i<tayBaiCu.size();i+=2){
//                        baiDanhCu.add(new NutLaBai(LaBai.timLaBaiTheoSoVaChat(tayBaiCu.get(i),tayBaiCu.get(i+1)),
//                                tempX,
//                                BoBai.ChiSoSoHuu.DA_DANH.getDichY()+ThongSo.LaBai.getKichThuocLaBaiCao()/2));
//                        tempX+=ThongSo.LaBai.getKichThuocLaBaiRong();
//                    }
//                    daTaiBaiDanhCu=true;
//                }).start();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
//                        + " " + e.getMessage(), Toast.LENGTH_LONG).show();
//                huyDangKyListener();
//                taiLai();
//            }
//        });

        /**
         * tải lại bài đánh mới là mảng gồm các cặp 2 phần tử
         * phần tử 1 là số bài, phần tử 2 là chất bài
         */
        if(!baiDanhMoi.isEmpty()) {
            baiDanhMoi.clear();
        }
        float tempX=(MainActivity.chieuRongManHinh-
                ThongSo.LaBai.getKichThuocLaBaiRong()*tayBaiMoi.size()/2)/2;
        for(int i=0;i<tayBaiMoi.size();i+=2){
            baiDanhMoi.add(new NutLaBai(LaBai.timLaBaiTheoSoVaChat(tayBaiMoi.get(i),tayBaiMoi.get(i+1)),
                    tempX,
                    BoBai.ChiSoSoHuu.DA_DANH.getDichY()));
            tempX+=ThongSo.LaBai.getKichThuocLaBaiRong();
        }
        /**
         * tải lại bài trên tay
         * bài trên tay được lưu trên database ở dạng mảng 52 phần tử, tương ứng 1 lá bài
         * =1 là đang sở hữu
         */
        if(!baiTrenTay.isEmpty()) {
            baiTrenTay.clear();
        }
        realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"
                        +TEN_TRUONG_NGUOI_CHOI+"/"+nguoiChois[0].getUid()
                        +"/"+NguoiChoi.TEN_TRUONG_BAI_TREN_TAY)
                .get().addOnSuccessListener(dataSnapshot -> new Thread(()->{
                    int so,chat,dem=0,demLaTrenTay=0;
                    for (DataSnapshot x : dataSnapshot.getChildren()) {
                        if(x.getValue(Long.class).intValue()==1){
                            so=dem/4;
                            chat=dem%4;
                            LaBai laBai=LaBai.timLaBaiTheoSoVaChat(so+2,chat);
                            laBai.setDuocChon(false);
                            laBai.setCoTheChon(false);
                            nguoiChois[0].getBaiTrenTay().add(laBai);
                            baiTrenTay.add(new NutLaBai(laBai,
                                    BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichX()
                                            +ThongSo.LaBai.getKichThuocLaBaiNhoRong()*demLaTrenTay,
                                    BoBai.ChiSoSoHuu.NGUOI_CHOI_1.getDichY()));
                            demLaTrenTay++;
                        }
                        dem++;
                    }
                    daTaiBaiTrenTay=true;
                }).start()).addOnFailureListener(onFailureListener -> {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
                    + " " + onFailureListener.getMessage(), Toast.LENGTH_LONG).show();
                    huyDangKyListener(true);
            mainActivity.finish();
        });

    }
    /**
     * tải lại người đang đánh
     */
    private void taiNguoiDangDanh(){
        nguoiDangDanhReference=realtimeDatabase
                .getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_NGUOI_DANG_DANH_THEO_SO_THU_TU);
        nguoiDangDanhListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                new Thread(()->{
                    nguoiDangDanh=snapshot.getValue(Long.class).intValue();
                    System.out.println("SoNguoiChoi khi dang tai nguoiDangDanh: "+soNguoiChoi);
                    for(int i=0;i<soNguoiChoi;i++){
                        if(nguoiChois[i].soThuTu==nguoiDangDanh) {
                            nguoiDangDanh=i;
                            daCapNhatNguoiDangDanh=true;
                            daTaiNguoiDangDanh=true;
                            break;
                        }
                    }
                    if(nguoiDangDanh==-1) {//kết thúc
                        daCapNhatNguoiDangDanh=true;
                        daTaiNguoiDangDanh=true;
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
                    + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                huyDangKyListener(true);
                 mainActivity.finish();
            }
        };
        nguoiDangDanhReference.addValueEventListener(nguoiDangDanhListener);
    }
    /**
     * theo dõi trạng thái online của người chơi, khi bị mất kết nối sẽ cập nhật trạng thái offline ngay lập tức
     */
    private void theoDoiOnline(){
        String uid=auth.getCurrentUser().getUid();
        nguoiChoiOnlineReference = realtimeDatabase
                .getReference(TEN_BANG+"/"+maPhong+"/"+NguoiChoi.TEN_BANG_TRONG_TRAN
                        +"/"+uid);
        ketNoiReference=realtimeDatabase.getReference(".info/connected");
        theoDoiNguoiChoiOnlineListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Boolean.class)==null|| !snapshot.getValue(Boolean.class)){
                    if (daKetNoi) {
                        mainActivity.toast(mainActivity.getString(R.string.khong_co_internet));
                        huyDangKyListener(true);
                        mainActivity.finish();
                    }
                }else{
                    new Thread(()->{
                        HashMap<String,Object> nguoiChoiOnline=new HashMap<>();
                        nguoiChoiOnline.put(NguoiChoi.TEN_TRUONG_TRANG_THAI_ONLINE,"online");
                        HashMap<String,Object> nguoiChoiOffline=new HashMap<>();
                        nguoiChoiOffline.put(NguoiChoi.TEN_TRUONG_TRANG_THAI_ONLINE,"offline");
                        nguoiChoiOnlineReference.updateChildren(nguoiChoiOnline);
                        nguoiChoiOnlineReference.onDisconnect().updateChildren(nguoiChoiOffline);
                        if(!daKetNoi) {
                            daKetNoi=true;
                            taiDoLechThoiGianVoiServer();
                            taiNguoiChoi();
                            taiRiengTu();
                        }
                    }).start();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                daKetNoi=false;
                Toast.makeText(mainActivity,mainActivity.getString(R.string.loi)+" "
                        +error.getMessage(),Toast.LENGTH_SHORT).show();
                huyDangKyListener(true);
                mainActivity.finish();
            }
        };
        ketNoiReference.addValueEventListener(theoDoiNguoiChoiOnlineListener);
    }

    /**
     * tải người chơi của client
     */
    private void taiNguoiChoi() {
        /**
         * soNguoiChoi
         */
        realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"
                +TEN_TRUONG_NGUOI_CHOI).get().addOnSuccessListener(dataSnapshot -> {
            soNguoiChoi= (int) dataSnapshot.getChildrenCount();
            /**
             * người chơi
             */
            nguoiChois=new NguoiChoi[4];
            nguoiChois[0]=new NguoiChoi(BoBai.ChiSoSoHuu.NGUOI_CHOI_1);
            nguoiChoiReference= realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"
                    +TEN_TRUONG_NGUOI_CHOI+"/"+auth.getCurrentUser().getUid());
            nguoiChoiListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    new Thread(()->{
                        if(!daTaiNguoiChoi){
                            nguoiChois[0].setUid(auth.getCurrentUser().getUid());
                            nguoiChois[0].soThuTu=snapshot.child(NguoiChoi.TEN_TRUONG_SO_THU_TU)
                                    .getValue(Long.class).intValue();
                            nguoiChois[0].boLuot=snapshot.child(NguoiChoi.TEN_TRUONG_BO_LUOT)
                                    .getValue(Boolean.class).booleanValue();
                            nguoiChois[0].sanSang=snapshot.child(NguoiChoi.TEN_TRUONG_SAN_SANG)
                                    .getValue(Boolean.class).booleanValue();
                            nguoiChois[0].chiChat2=snapshot.child(NguoiChoi.TEN_TRUONG_BO_LUOT)
                                    .getValue(Boolean.class).booleanValue();
                            nguoiChois[0].soLaBai=snapshot.child(NguoiChoi.TEN_TRUONG_SO_LA_BAI)
                                    .getValue(Long.class).intValue();
                            nguoiChois[0].setTenDangNhap(snapshot.child(NguoiChoi.TEN_TRUONG_TEN)
                                    .getValue(String.class));
                            nguoiChois[0].setTien(snapshot.child(NguoiChoi.TEN_TRUONG_TIEN)
                                    .getValue(Long.class).intValue());
                            daTaiNguoiChoi=true;
                            nutSanSang.setDuocBam(nguoiChois[0].sanSang);
                            taiGiaiDoan();
                        }else{
                            nguoiChois[0].boLuot=snapshot.child(NguoiChoi.TEN_TRUONG_BO_LUOT)
                                    .getValue(Boolean.class).booleanValue();
                            nguoiChois[0].chiChat2=snapshot.child(NguoiChoi.TEN_TRUONG_CHUOI_CHAT_2)
                                    .getValue(Boolean.class).booleanValue();
                        }
                    }).start();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
                            + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                    huyDangKyListener(true);
                    mainActivity.finish();
                }
            };
            nguoiChoiReference.addValueEventListener(nguoiChoiListener);
        });
    }

//    private void theoDoiChiChat2() {
//        chiChat2Reference=realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"
//                +TEN_TRUONG_NGUOI_CHOI+"/"+nguoiChois[0].getUid()+"/"+NguoiChoi.TEN_TRUONG_CHUOI_CHAT_2);
//        chiChat2Listener=new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                new Thread(() -> {
//                    nguoiChois[0].chiChat2 = snapshot.getValue(Boolean.class);
//                    daTaiChiChat2 = true;
//                }).start();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
//                        + " " + error.getMessage(), Toast.LENGTH_LONG).show();
//                mainActivity.finish();
//            }
//        };
//        chiChat2Reference.addValueEventListener(chiChat2Listener);
//    }

    /**
     * tải giai đoạn
     */
    private void taiGiaiDoan() {
        giaiDoanReference= realtimeDatabase
                .getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_GIAI_DOAN);
        giaiDoanListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                new Thread(()->{
                    if (!daTaiGiaiDoan) {
                        giaiDoan= ChoiVoiMay.GiaiDoan.timGiaiDoan(snapshot.getValue(Long.class).intValue());
                        daTaiGiaiDoan=true;
                        System.out.println("giaiDoan dang tai:"+giaiDoan.ordinal());
                        taiNguoiChoiKhac();
                        taiTayBaiMoi();
                        taiKetThucDemNguoc();
                    }else{
                        if(giaiDoan==CHO_NGUOI_CHOI)
                            giaiDoan=ChoiVoiMay.GiaiDoan.timGiaiDoan(snapshot.getValue(Long.class).intValue());
                    }
                }).start();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
                        + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                huyDangKyListener(true);
                mainActivity.finish();
            }
        };
        giaiDoanReference.addValueEventListener(giaiDoanListener);
    }

    /**
     * tải những người chơi còn lại
     */
    private void taiNguoiChoiKhac() {
        for(int i=soNguoiChoi;i<4;i++){
            nguoiChois[i]=new NguoiChoi();
            nguoiChois[i].setActive(false);
        }
        nguoiChoiKhacReference = realtimeDatabase
                .getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_NGUOI_CHOI);
        nguoiChoiKhacListener =new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                new Thread(()->{
                    if (!daTaiNguoiChoiKhac) {
                        if(!snapshot.getKey().equals(nguoiChois[0].getUid())){
                            int temp=snapshot.child(NguoiChoi.TEN_TRUONG_SO_THU_TU)
                                    .getValue(Long.class).intValue()-nguoiChois[0].soThuTu;
                            if(temp<0) temp+=soNguoiChoi;
                            System.out.println("so thu tu:"+nguoiChois[0].soThuTu+" soNguoiChoi:"+soNguoiChoi);
                            System.out.println("nguoi choi "+(demNguoiChoiDaThem+1)+": soThuTu "
                                    +snapshot.child(NguoiChoi.TEN_TRUONG_SO_THU_TU)+"soThuTuLocal:"+temp);
                            System.out.println(nguoiChois[0].getUid()+"/"+snapshot.getKey());
                            nguoiChois[temp]=new NguoiChoi(BoBai.ChiSoSoHuu.timTheoSoHuu(temp+1));
                            nguoiChois[temp].setUid(snapshot.getKey());
                            nguoiChois[temp].soThuTu=snapshot.child(NguoiChoi.TEN_TRUONG_SO_THU_TU)
                                    .getValue(Long.class).intValue();
                            nguoiChois[temp].boLuot=snapshot.child(NguoiChoi.TEN_TRUONG_BO_LUOT)
                                    .getValue(Boolean.class).booleanValue();
                            nguoiChois[temp].sanSang=snapshot.child(NguoiChoi.TEN_TRUONG_SAN_SANG)
                                    .getValue(Boolean.class).booleanValue();
                            nguoiChois[temp].soLaBai=snapshot.child(NguoiChoi.TEN_TRUONG_SO_LA_BAI)
                                    .getValue(Long.class).intValue();
                            nguoiChois[temp].setTenDangNhap(snapshot.child(NguoiChoi.TEN_TRUONG_TEN)
                                    .getValue(String.class));
                            nguoiChois[temp].setTien(snapshot.child(NguoiChoi.TEN_TRUONG_TIEN)
                                    .getValue(Long.class).intValue());
                            demNguoiChoiDaThem++;
                        }
                        if(!daTaiNguoiChoiKhac){
                            if(demNguoiChoiDaThem==soNguoiChoi-1){
                                daTaiNguoiChoiKhac=true;
                                taiNguoiDangDanh();
                            }
                        }
                    }else{
                        if(giaiDoan==CHO_NGUOI_CHOI){
                            System.out.println("cho nguoi choi sau khi tai xong nguoi choi khac: "+soNguoiChoi);
                            for(int i=0;i<soNguoiChoi;i++){
                                if(nguoiChois[i].getUid().equals(snapshot.getKey())) return;
                            }
                            int temp=snapshot.child(NguoiChoi.TEN_TRUONG_SO_THU_TU)
                                    .getValue(Long.class).intValue()-nguoiChois[0].soThuTu;
                            if(temp<0) temp+=soNguoiChoi+1;
                            NguoiChoi nguoiChoi=new NguoiChoi(BoBai.ChiSoSoHuu.timTheoSoHuu(soNguoiChoi+1));
                            nguoiChoi.setUid(snapshot.getKey());
                            nguoiChoi.soThuTu=snapshot.child(NguoiChoi.TEN_TRUONG_SO_THU_TU)
                                    .getValue(Long.class).intValue();
                            nguoiChoi.boLuot=snapshot.child(NguoiChoi.TEN_TRUONG_BO_LUOT)
                                    .getValue(Boolean.class).booleanValue();
                            nguoiChoi.sanSang=snapshot.child(NguoiChoi.TEN_TRUONG_SAN_SANG)
                                    .getValue(Boolean.class).booleanValue();
                            nguoiChoi.chiChat2=snapshot.child(NguoiChoi.TEN_TRUONG_CHUOI_CHAT_2)
                                    .getValue(Boolean.class).booleanValue();
                            nguoiChoi.soLaBai=snapshot.child(NguoiChoi.TEN_TRUONG_SO_LA_BAI)
                                    .getValue(Long.class).intValue();
                            nguoiChoi.setTenDangNhap(snapshot.child(NguoiChoi.TEN_TRUONG_TEN)
                                    .getValue(String.class));
                            nguoiChoi.setTien(snapshot.child(NguoiChoi.TEN_TRUONG_TIEN)
                                    .getValue(Long.class).intValue());
                            for(int i=soNguoiChoi;i>=temp;i--) {
                                if(i!=temp){
                                    nguoiChois[i]=nguoiChois[i-1];
                                    nguoiChois[i].setChiSoSoHuu(BoBai.ChiSoSoHuu.timTheoSoHuu(i+1));
                                }else{
                                    nguoiChois[i]=nguoiChoi;
                                    nguoiChois[i].setChiSoSoHuu(BoBai.ChiSoSoHuu.timTheoSoHuu(i+1));
                                }
                            }
                            soNguoiChoi++;
                        }
                    }
                }).start();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                new Thread(()->{
                    if (!snapshot.getKey().equals(nguoiChois[0].getUid())&&giaiDoan!=CHO_NGUOI_CHOI) {
                        for(int i=1;i<soNguoiChoi;i++){
                            if(nguoiChois[i].getUid().equals(snapshot.getKey())){
                                nguoiChois[i].soLaBai=snapshot.child(NguoiChoi.TEN_TRUONG_SO_LA_BAI)
                                        .getValue(Long.class).intValue();
                                boolean boLuotCu=nguoiChois[i].boLuot;
                                nguoiChois[i].boLuot=snapshot.child(NguoiChoi.TEN_TRUONG_BO_LUOT)
                                        .getValue(Boolean.class).booleanValue();
                                nguoiChois[i].chiChat2=snapshot.child(NguoiChoi.TEN_TRUONG_CHUOI_CHAT_2)
                                        .getValue(Boolean.class).booleanValue();
                                if (!taiLaiGame) {
                                    if(!boLuotCu&&nguoiChois[i].boLuot) {
                                        boLuot();
                                    }
                                    if(boLuotCu&&!nguoiChois[i].boLuot
                                            &&!dauLuot&&!nguoiChois[i].chiChat2) khoiTaoLuotMoi();
                                }
                                break;
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                new Thread(()->{
                    if(giaiDoan==CHO_NGUOI_CHOI){
                        nutSanSang.setDuocBam(false);
                        int indexBiXoa=-1;
                        int soThuTuBiXoa=snapshot.child(NguoiChoi.TEN_TRUONG_SO_THU_TU)
                                .getValue(Long.class).intValue();
                        for(int i=0;i<soNguoiChoi;i++){
                            if (indexBiXoa==-1) {
                                if(nguoiChois[i].soThuTu==soThuTuBiXoa){
                                    indexBiXoa=i;
                                    if(i+1<soNguoiChoi) {
                                        nguoiChois[i]=nguoiChois[i+1];
                                        nguoiChois[i].setChiSoSoHuu(BoBai.ChiSoSoHuu.timTheoSoHuu(i+1));
                                        if(nguoiChois[i].soThuTu>soThuTuBiXoa){
                                            nguoiChois[i].soThuTu--;
                                        }
                                    }
                                    else {
                                        nguoiChois[i]=new NguoiChoi();
                                        nguoiChois[i].setActive(false);
                                    }
                                }else if(nguoiChois[i].soThuTu>soThuTuBiXoa) nguoiChois[i].soThuTu--;
                            }else{
                                if(i+1<soNguoiChoi) {
                                    nguoiChois[i]=nguoiChois[i+1];
                                    nguoiChois[i].setChiSoSoHuu(BoBai.ChiSoSoHuu.timTheoSoHuu(i+1));
                                    if(nguoiChois[i].soThuTu>soThuTuBiXoa){
                                        nguoiChois[i].soThuTu--;
                                    }
                                }
                                else {
                                    nguoiChois[i]=new NguoiChoi();
                                    nguoiChois[i].setActive(false);
                                }
                            }
                        }
                        soNguoiChoi--;
                    }
                }).start();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity,mainActivity.getString(R.string.loi)
                        +" "+error.getMessage(),Toast.LENGTH_LONG).show();
                huyDangKyListener(true);
                mainActivity.finish();
            }
        };
        nguoiChoiKhacReference.addChildEventListener(nguoiChoiKhacListener);
    }

    /**
     * tải thuộc tính riêng tư của phòng
     */
    private void taiRiengTu() {
        riengTuReference= realtimeDatabase.getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_RIENG_TU);
        riengTuListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                new Thread(()->{
                    nutRiengTuCongKhai.setDuocBam(snapshot.getValue(Boolean.class));
                    if(!daTaiRiengTu){
                        daTaiRiengTu=true;
                    }
                }).start();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
                        + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                huyDangKyListener(true);
                mainActivity.finish();
            }
        };
        riengTuReference.addValueEventListener(riengTuListener);
    }

    /**
     * tải thời điểm kết thúc đếm ngược
     */
    private void taiKetThucDemNguoc() {
        ketThucDemNguocReference=realtimeDatabase
                .getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_KET_THUC_DEM_NGUOC);
        ketThucDemNguocListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                new Thread(()->{
                    ketThucDemNguoc=snapshot.getValue(Long.class);
                    daCapNhatThoiGianKetThuc=true;
                    if(!daTaiKetThucDemNguoc){
                        if (giaiDoan!=CHO_NGUOI_CHOI) {
                            /**
                             * khởi tạo thời gian chờ khi kết nối lại ở giai đoạn khác chờ chọn
                             */
                            daCapNhatThoiGianKetThuc=false;
                            thoiGianDemNguoc=ketThucDemNguoc-thoiGianHienTai();
                        }
                        daTaiKetThucDemNguoc=true;
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
                        + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                huyDangKyListener(true);
                mainActivity.finish();
            }
        };
        ketThucDemNguocReference.addValueEventListener(ketThucDemNguocListener);
    }

    /**
     * tải tay bài mới
     */
    private void taiTayBaiMoi() {
        tayBaiMoiReference=realtimeDatabase
                .getReference(TEN_BANG+"/"+maPhong+"/"+TEN_TRUONG_TAY_BAI_MOI);
        tayBaiMoiListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                new Thread(()->{
                    if((giaiDoan==CHO_CHON||giaiDoan==DANH_BAI||
                            giaiDoan==CHIA_BAI||giaiDoan==LAT_BAI)&&nguoiDangDanh!=0){
                        tayBaiMoi=new ArrayList<>();
                        for(DataSnapshot x:snapshot.getChildren()){
                            tayBaiMoi.add(x.getValue(Long.class).intValue());
                        }
                        if(!daTaiTayBaiMoi){
                            daTaiBaiDanhCu=false;
                            daTaiBaiTrenTay=false;
                            daTaiTayBaiMoi=true;
                            taiLaiBai();
                        }else{
                            /**
                             * lưu người đánh cuối
                             */
                            nguoiDanhCuoi=nguoiDangDanh;
                            danh();
                        }
                    }
                    else{
                        daTaiTayBaiMoi=true;
                    }
                }).start();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
                        + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                huyDangKyListener(true);
                mainActivity.finish();
            }
        };
        tayBaiMoiReference.addValueEventListener(tayBaiMoiListener);
    }
    private void taiDoLechThoiGianVoiServer(){
        doLechThoiGianReference =realtimeDatabase.getReference(".info/serverTimeOffset");
        doLechThoiGianListener=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        new Thread(()->{
                            Long offset = snapshot.getValue(Long.class);
                            if (offset != null) {
                                doLechThoiGianVoiServer = offset;
                                daTaiDoLechThoiGian=true;
                            }
                        }).start();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(mainActivity, mainActivity.getString(R.string.loi)
                                + " " + error.getMessage(), Toast.LENGTH_LONG).show();
                        huyDangKyListener(true);
                        mainActivity.finish();
                    }
                };
        doLechThoiGianReference.addValueEventListener(doLechThoiGianListener);
    }
    public boolean hoanTatTaiLai(){
//        System.out.println(daKetNoi+" daKetNoi"+
//                daTaiNguoiChoi+" daTaiNguoiChoi"+
//                daTaiNguoiChoiKhac+" daTaiNguoiChoiKhac"+
//                daTaiGiaiDoan+" daTaiGiaiDoan"+
//                daTaiRiengTu+" daTaiRiengTu"+
//                daTaiKetThucDemNguoc+" daTaiKetThucDemNguoc"+
//                daTaiTayBaiMoi+" daTaiTayBaiMoi"+
//                daTaiBaiDanhCu+" daTaiTayBaiMoi"+
//                daTaiBaiTrenTay+" daTaiBaiTrenTay"+
//                daTaiNguoiDangDanh+" daTaiNguoiDangDanh"+
//                daTaiDoLechThoiGian+" daTaiDoLechThoiGian");
        return (daKetNoi&&
        daTaiNguoiChoi&&
        daTaiNguoiChoiKhac&&
        daTaiGiaiDoan&&
        daTaiRiengTu&&
        daTaiKetThucDemNguoc&&
        daTaiTayBaiMoi&&
        daTaiBaiDanhCu&&
        daTaiBaiTrenTay&&
        daTaiNguoiDangDanh&&
        daTaiDoLechThoiGian);
    }
    private long thoiGianHienTai(){return System.currentTimeMillis()+doLechThoiGianVoiServer;}
}
