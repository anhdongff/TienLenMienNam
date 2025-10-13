package com.gamebai.tienlenmiennam.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.api.ApiService;
import com.gamebai.tienlenmiennam.api.NoiDungMaQua;
import com.gamebai.tienlenmiennam.api.PhanHoiDonGian;
import com.gamebai.tienlenmiennam.api.RetrofitClient;
import com.gamebai.tienlenmiennam.datamodel.YeuCauHoTro;
import com.gamebai.tienlenmiennam.hotro.Internet;
import com.gamebai.tienlenmiennam.hotro.ThongSo;
import com.gamebai.tienlenmiennam.thucthe.NguoiChoi;
import com.gamebai.tienlenmiennam.ui.ManHinhDangTai;
import com.gamebai.tienlenmiennam.uisanhcho.BalatroCreateSupportRequestFragment;
import com.gamebai.tienlenmiennam.uisanhcho.BalatroGiftcodeFragment;
import com.gamebai.tienlenmiennam.uisanhcho.BalatroSettingsFragment;
import com.gamebai.tienlenmiennam.uisanhcho.BalatroSupportRequestFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static Context gameContext;
    ManHinhDangTai manHinhDangTai;
    /**
     * Đăng nhập
     */
    private EditText emailDangNhap,matKhauDangNhap;
    private CheckBox checkBoxLuuThongTinDangNhap;
    private Button buttonDangNhap,buttonDangKy;
    private TextView textViewQuenMatKhau;
    private long lanCuoiResetMatKhau, lanCuoiGuiLaiEmailXacThuc;
    private boolean dialogDangMo;
    /**
     * firebase
     */
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseDatabase realtimeDatabase;
    /** tham chiếu đến node NguoiChoiTimTran/{UserID} trong realtime database */
    private DatabaseReference nguoiChoiTimTranReference;
    /** dữ liệu yêu cầu hỗ trợ */
    private List<YeuCauHoTro> danhSachYeuCauHoTro;
    /**
     * Sảnh chờ
     */
    Button buttonChoiVoiMay;
    ImageView buttonDangXuat;
    Button buttonTimPhongTheoIdPhong,buttonTaoPhong,buttonTimTran;
    EditText editTextMaPhong;
    TextView textViewTenNguoiChoi,textViewTien,textViewHuongDan,textViewFree,textViewFirst, textViewSecond,textViewThird,textViewFouth,textViewFifth;
    NguoiChoi nguoiChoi;
    ListenerRegistration taiKhoanListener;
    ListenerRegistration dangNhapListener;
    BalatroSupportRequestFragment supportRequestFragment;
    /** theo dõi sự kiện tìm được trận trong NguoiChoiTimTran/{UserID} */
    ValueEventListener theoDoiKetQuaTimTranListener;
    /**
     * Game
     */
    private GamePanel gamePanel;
    private Game game;
    public static int chieuCaoManHinh,chieuRongManHinh;
    private long lanCuoiDangNhap;
    /**
     * Retrofit
     */
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        gameContext=this;
        DisplayMetrics thongTinManHinh=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(thongTinManHinh);
        chieuCaoManHinh=thongTinManHinh.heightPixels;
        chieuRongManHinh=thongTinManHinh.widthPixels;
        ThongSo.ThongSoManHinh.setTiLeChieuCaoManHinh(chieuCaoManHinh);
        ThongSo.ThongSoManHinh.setTiLeChieuRongManHinh(chieuRongManHinh);
        /**
         * ẩn thanh điều khiển, thanh thông báo
         */
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|//ẩn thanh điều khiển
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|//thanh điều khiển có thể tự động biến mất
                View.SYSTEM_UI_FLAG_FULLSCREEN|//màn hình không có thanh thông báo
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|//kéo dài view xuống dưới thanh điều khiển, không ẩn thanh điều khiển
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|//kéo dài view xuống dưới thanh thông báo, không ẩn thanh thông báo
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//giữ view ổn định khi có thay đổi về UI
        /**
         * cài đặt hiển thị lên đoạn camera trong màn hình
         */
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode= WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
        }
        /**
         * yêu cầu quyền (chỉ chạy với android 9 trở xuống)
         */
        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.P){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ThongSo.Quyen.WRITE_EXTERNAL_STORAGE);
                }else{
                    Toast.makeText(this,getString(R.string.cap_quyen_trong_cai_dat),
                            Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:"+getPackageName()));
                    startActivity(intent);
                    finish();
                }
            }
        }
        /**
         * chờ tải thông tin đăng nhập cũ
         */
        manHinhDangTai=new ManHinhDangTai(this,null);
        manHinhDangTai.hienThi();
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        /**
         * đăng nhập đăng ký
         */
        khoiTaoViewDangNhap();
        /**
         * firebase
         */
        auth=FirebaseAuth.getInstance();
        firestore =FirebaseFirestore.getInstance();
        realtimeDatabase=FirebaseDatabase.getInstance(
                "https://tienlenmiennam-d2c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        realtimeDatabase.setPersistenceEnabled(false);
        /**
         * tải thông tin đăng nhập nếu đã lưu trước đó
         */
        taiThongTin();
        /**
         * Retrofit
         */
        apiService= RetrofitClient.getAPIInstance().create(ApiService.class);
        /**
         * game
         */
        gamePanel=new GamePanel(this);
        game=gamePanel.getGame();
    }

    /**
     * Khởi tạo, bind các component trong layout đăng nhập
     */
    private void khoiTaoViewDangNhap() {
        dialogDangMo=false;
        emailDangNhap=findViewById(R.id.editTextTextEmailTaiKhoan);
        matKhauDangNhap=findViewById(R.id.editTextTextPasswordDangNhap);
        buttonDangNhap=findViewById(R.id.buttonDangNhap);
        buttonDangNhap.setOnClickListener(v -> dangNhap());
        buttonDangKy=findViewById(R.id.buttonDangKy);
        buttonDangKy.setOnClickListener(v -> dangKy());
        checkBoxLuuThongTinDangNhap =findViewById(R.id.checkBox);
        textViewQuenMatKhau=findViewById(R.id.textView3);
        textViewQuenMatKhau.setOnClickListener(v -> quenMatKhau());
    }
    /**
     * Khởi tạo, bind các component trong layout sảnh chờ
     */
    private void khoiTaoViewSanhCho(){
        buttonChoiVoiMay=findViewById(R.id.buttonChoiVoiMay);
        buttonChoiVoiMay.setOnClickListener(v -> choiVoiMay());
        buttonDangXuat=findViewById(R.id.imageViewDangXuat);
        buttonDangXuat.setOnClickListener(v -> {
            BalatroSettingsFragment fragment = new BalatroSettingsFragment();
            fragment.setOnActionSelectedListener(action -> {
                switch (action){
                    case DANG_XUAT:
                        dangXuat();
                        fragment.dismiss();
                        break;
                    case MA_QUA:
                        nhapMaQua();
                        break;
                    case HO_TRO:
                        hienThiYeuCauHoTro();
                }
            });
            fragment.show(getSupportFragmentManager(), "BalatroSettings");
        });
        textViewTenNguoiChoi=findViewById(R.id.textViewTenNguoiChoi);
        textViewTien=findViewById(R.id.TextViewTien);
        textViewTien.setOnClickListener(v -> {
            Intent intent=new Intent(this,NapTienActivity.class);
            startActivity(intent);
        });
        buttonTimTran=findViewById(R.id.buttonTimTran);
        buttonTimTran.setOnClickListener(v->{
            manHinhDangTai.hienThi(getString(R.string.dang_tim_phong));
            timTranGame();});
        buttonTaoPhong=findViewById(R.id.buttonTaoPhong);
        buttonTaoPhong.setOnClickListener(v->{
            manHinhDangTai.hienThi(getString(R.string.dang_tao_phong));
            taoPhongGame();});
        buttonTimPhongTheoIdPhong=findViewById(R.id.buttonTimPhongTheoIdPhong);
        buttonTimPhongTheoIdPhong.setOnClickListener(v->{
            /**
             * tạo dialog nhập mã phòng
             */
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            editTextMaPhong=new EditText(this);
            editTextMaPhong.setHint(getString(R.string.game_ma_phong));
            editTextMaPhong.setSingleLine();
            editTextMaPhong.setInputType(InputType.TYPE_CLASS_NUMBER);
            editTextMaPhong.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(4)});
            builder.setView(editTextMaPhong);
            builder.setPositiveButton(getString(R.string.ok),(dialog,which)->{
                if(editTextMaPhong.getText().toString().isEmpty()){
                    Snackbar.make(findViewById(R.id.sanhCho),R.string.hay_nhap_ma_phong,Snackbar.LENGTH_SHORT).show();
                    return;
                }
                manHinhDangTai.hienThi(getString(R.string.dang_tim_phong));
                timPhongTheoIdPhongGame();});
            builder.setNegativeButton(getString(R.string.huy),(dialog,which)-> dialog.dismiss());
            builder.show();
            });
        textViewHuongDan=findViewById(R.id.textViewHuongDan);
        textViewHuongDan.setOnClickListener(v->{
            ScrollView scrollView = new ScrollView(this);
            TextView textView = new TextView(this);
            textView.setText(R.string.game_noi_dung_huong_dan);
            textView.setPadding(32, 32, 32, 32); // Padding tùy ý
            textView.setTextSize(16);
            scrollView.addView(textView);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.game_huong_dan);
            builder.setView(scrollView);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
            builder.show();
        });
        textViewFree=findViewById(R.id.textViewFree);
        textViewFree.setOnClickListener(v->{
            manHinhDangTai.hienThi(getString(R.string.game_dang_tai));
            freeMoney(500);
        });
        textViewFirst=findViewById(R.id.textViewFirst);
        textViewSecond =findViewById(R.id.textViewSecond);
        textViewThird=findViewById(R.id.textViewThird);
        textViewFouth=findViewById(R.id.textViewFouth);
        textViewFifth=findViewById(R.id.textViewFifth);
        textViewFirst.setOnClickListener(v->farmData());
    }

    /**
     * hiển thị dialog yêu cầu hỗ trợ
     */
    private void hienThiYeuCauHoTro() {
        supportRequestFragment=new BalatroSupportRequestFragment(danhSachYeuCauHoTro);
        supportRequestFragment.setOnActionSelectedListener(new BalatroSupportRequestFragment.OnRequestSupportActionSelectedListener() {
            @Override
            public void onActionSelected(BalatroSupportRequestFragment.Action action) {
                if(action== BalatroSupportRequestFragment.Action.REQUEST_SUPPORT){
                    taoYeuCauHoTro();
                }
            }
        });
        supportRequestFragment.show(getSupportFragmentManager(),"BalatroSupportRequest");
    }
    /**
     * tạo yêu cầu hỗ trợ mới
     */
    private void taoYeuCauHoTro() {
        BalatroCreateSupportRequestFragment fragment=new BalatroCreateSupportRequestFragment();
        fragment.setOnActionSelectedListener(new BalatroCreateSupportRequestFragment
                                                     .OnCreateSupportRequestActionSelectedListener() {
            @Override
            public void onCreateRequest(String tieuDe, String noiDung, View v) {
                for(YeuCauHoTro yeuCauHoTro:danhSachYeuCauHoTro){
                    if(yeuCauHoTro.getTrangThai()==1){//đang chờ phản hồi
                        Snackbar.make(v,
                                R.string.ban_co_yeu_cau_ho_tro_dang_cho_phan_hoi,
                                Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(tieuDe.isEmpty()){
                    Snackbar.make(v,R.string.hay_nhap_tieu_de,Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(noiDung.isEmpty()){
                    Snackbar.make(v,R.string.hay_nhap_noi_dung,Snackbar.LENGTH_SHORT).show();
                    return;
                }
                manHinhDangTai.hienThi(getString(R.string.dang_gui_yeu_cau));
                FirebaseUser user=auth.getCurrentUser();
                if(user==null) {
                    manHinhDangTai.an();
                    Snackbar.make(findViewById(R.id.sanhCho),R.string.dang_nhap_het_han,
                            Snackbar.LENGTH_SHORT).show();
                    dangXuat();
                    return;
                }
                YeuCauHoTro yeuCauHoTro=new YeuCauHoTro();
                yeuCauHoTro.setTieuDe(tieuDe);
                yeuCauHoTro.setNoiDung(noiDung);
                yeuCauHoTro.setThoiGianTao(Timestamp.now());
                yeuCauHoTro.setTrangThai(1);
                yeuCauHoTro.setUserId(user.getUid());
                yeuCauHoTro.setTenDangNhap(nguoiChoi.getTenDangNhap());
                DocumentReference docRef=firestore.collection("ChamSocKhachHang").document();
                yeuCauHoTro.setId(docRef.getId());
                docRef.set(yeuCauHoTro.toHashMap()).addOnCompleteListener(task -> {
                            manHinhDangTai.an();
                            if(task.isSuccessful()){
                                docRef.addSnapshotListener((value, error) -> {
                                    if (error != null || value == null || !value.exists()) {
                                        return;
                                    }
                                    YeuCauHoTro updatedRequest = value.toObject(YeuCauHoTro.class);
                                    if (updatedRequest != null) {
                                        int index = -1;
                                        for (int i = 0; i < danhSachYeuCauHoTro.size(); i++) {
                                            if (danhSachYeuCauHoTro.get(i).getId().equals(updatedRequest.getId())) {
                                                index = i;
                                                break;
                                            }
                                        }
                                        if (index != -1) {
                                            danhSachYeuCauHoTro.set(index, updatedRequest);
                                        } else {
                                            danhSachYeuCauHoTro.add(0, updatedRequest);
                                        }
                                        if(supportRequestFragment!=null) {//cập nhật lại danh sách nếu đang mở
                                            supportRequestFragment.updateListAndRefreshUI(danhSachYeuCauHoTro);
                                        }
                                    }
                                });
                                Snackbar.make(v,
                                        R.string.gui_yeu_cau_ho_tro_thanh_cong,
                                        Snackbar.LENGTH_SHORT).show();
                            }else{
                                Snackbar.make(v,
                                        getString(R.string.loi)+" "+task.getException().getMessage(),
                                        Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
            @Override
            public void onCancel() {
                fragment.dismiss();
            }
        });
        fragment.show(getSupportFragmentManager(),"BalatroCreateSupportRequest");
    }
    /**
     * mở dialog nhập mã quà
     */
    private void nhapMaQua() {
        BalatroGiftcodeFragment fragment=new BalatroGiftcodeFragment();
        fragment.setOnActionSelectedListener(new BalatroGiftcodeFragment.OnGiftcodeActionSelectedListener(){
            @Override
            public void onUseGiftcode(String giftcode, View view) {
                if (giftcode.isEmpty() || !giftcode.matches("^[a-zA-Z0-9]+$")) {
                    Snackbar.make(view, R.string.ma_qua_khong_hop_le,
                            Snackbar.LENGTH_SHORT).show();
                }else{
                    manHinhDangTai.hienThi(getString(R.string.dang_xu_ly));
                    FirebaseUser user=auth.getCurrentUser();
                    String token=user.getIdToken(false).getResult().getToken();
                    if(token==null||token.isEmpty()){
                        manHinhDangTai.an();
                        Snackbar.make(view,"lỗi token",
                                Snackbar.LENGTH_SHORT).show();
                    }
                    NoiDungMaQua maQua=new NoiDungMaQua(giftcode);
                    apiService.suDungMaQua("Bearer "+token,maQua)
                            .enqueue(new retrofit2.Callback<com.gamebai.tienlenmiennam.api.PhanHoiDonGian>() {
                                @Override
                                public void onResponse(@NonNull retrofit2.Call<com.gamebai.tienlenmiennam.api.PhanHoiDonGian> call,
                                                       @NonNull retrofit2.Response<com.gamebai.tienlenmiennam.api.PhanHoiDonGian> response) {
                                    manHinhDangTai.an();
                                    if (response.isSuccessful() && response.body() != null) {
                                        PhanHoiDonGian phanHoi = response.body();
                                        Snackbar.make(view, phanHoi.getOk(),
                                                Snackbar.LENGTH_LONG).show();
                                    } else {
                                        try {
                                            String errorJson = response.errorBody().string();
                                            Gson gson = new Gson();
                                            PhanHoiDonGian errorResponse = gson.fromJson(errorJson, PhanHoiDonGian.class);
                                            Snackbar.make(view,
                                                    getString(R.string.loi) + " " + errorResponse.getLoi(),
                                                    Snackbar.LENGTH_LONG).show();
                                        }catch (Exception e){
                                            Snackbar.make(view,
                                                    getString(R.string.loi),
                                                    Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull retrofit2.Call<com.gamebai.tienlenmiennam.api.PhanHoiDonGian> call,
                                                      @NonNull Throwable t) {
                                    manHinhDangTai.an();
                                    Toast.makeText(MainActivity.this,
                                            getString(R.string.loi) + " " + t.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
            @Override
            public void onCancel() {
                fragment.dismiss();
            }

        });
        fragment.show(getSupportFragmentManager(),"BalatroGiftcode");
    }

    /**
     * nhận số tiền i free, nếu hôm nay đã nhận thì cút
     * @param i
     */
    private void freeMoney(int i) {
        FirebaseUser user=auth.getCurrentUser();
        if(user==null) {
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.dang_nhap_het_han,
                    Snackbar.LENGTH_SHORT).show();
            dangXuat();
            return;
        }
        firestore.collection(NguoiChoi.TEN_COLLECTION).document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean daNhan=false;
                    if(documentSnapshot.contains(NguoiChoi.TEN_TRUONG_LAN_CUOI_NHAN_QUA)){
                        Calendar calendar=Calendar.getInstance();
                        Calendar calendar1=Calendar.getInstance();
                        calendar.setTime(documentSnapshot.getDate(NguoiChoi.TEN_TRUONG_LAN_CUOI_NHAN_QUA));
                        calendar1.setTime(new Date());
                        if(calendar.get(Calendar.DAY_OF_YEAR)==calendar1.get(Calendar.DAY_OF_YEAR)
                                &&calendar.get(Calendar.YEAR)==calendar1.get(Calendar.YEAR)){
                            daNhan=true;
                        }
                    }
                    if(!daNhan){
                        long tien=documentSnapshot.getLong(NguoiChoi.TEN_TRUONG_TIEN);
                        documentSnapshot.getReference().update(NguoiChoi.TEN_TRUONG_TIEN,tien+i);
                        documentSnapshot.getReference().update(NguoiChoi.TEN_TRUONG_LAN_CUOI_NHAN_QUA,new Date());
                        manHinhDangTai.an();
                        Snackbar.make(findViewById(R.id.sanhCho),getString(R.string.nhan_tien_thanh_cong)+" :"+i+"$",
                                Snackbar.LENGTH_SHORT).show();
                    }else{
                        manHinhDangTai.an();
                        Snackbar.make(findViewById(R.id.sanhCho),getString(R.string.da_nhan_tien_hom_nay),
                                Snackbar.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(task->{
                    manHinhDangTai.an();
                    Snackbar.make(findViewById(R.id.sanhCho),
                            getString(R.string.loi) + " " + task.getMessage(),
                            Snackbar.LENGTH_SHORT).show();
                });
    }
    private void quenMatKhau() {
        if(emailDangNhap.getText().toString().trim().isEmpty()){
            Snackbar.make(findViewById(R.id.main),R.string.thieu_email,Snackbar.LENGTH_SHORT).show();
            return;
        }
        Pattern pattern=Pattern.compile(getString(R.string.bieu_thuc_chinh_quy_kiem_tra_email));
        String email=emailDangNhap.getText().toString().trim();
        if(!pattern.matcher(email).matches()||email.contains("..")){
            Snackbar.make(findViewById(R.id.main),R.string.sai_dinh_dang_email,Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(!Internet.coKetNoi(this)){
            Toast.makeText(this,getString(R.string.khong_co_internet),Toast.LENGTH_LONG).show();
            return;
        }
        long time=System.currentTimeMillis()-lanCuoiResetMatKhau;
        long waitingTime=Long.parseLong(getString(R.string.thoi_gian_gui_lai_email_milisecond));
        if(time<waitingTime){
            Toast.makeText(this,getString(R.string.gui_lai_email_sau)
                            +" "+(waitingTime-time)/1000+"s",
                    Toast.LENGTH_LONG).show();
            return;
        }
        auth.sendPasswordResetEmail(emailDangNhap.getText().toString().trim())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        lanCuoiResetMatKhau=System.currentTimeMillis();
                        luuThoiDiemResetMatKhau();
                        Toast.makeText(this,getString(R.string.gui_email_reset_mat_khau_thanh_cong),
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Exception exception=task.getException();
                        if (exception instanceof FirebaseNetworkException) {
                            Toast.makeText(this,getString(R.string.khong_co_internet),
                                    Toast.LENGTH_LONG).show();
                        }
                        else if (exception instanceof FirebaseAuthInvalidUserException) {
                            Snackbar.make(findViewById(R.id.main),R.string.email_khong_ton_tai,
                                    Snackbar.LENGTH_SHORT).show();
                        }
                        else if (exception instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(this,getString(R.string.thu_qua_nhieu_lan),
                                    Toast.LENGTH_LONG).show();
                        }
                        else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            Snackbar.make(findViewById(R.id.main),R.string.email_hoac_mat_khau_khong_hop_le,
                                    Snackbar.LENGTH_SHORT).show();
                        }
                        else if (exception != null) {
                            Snackbar.make(findViewById(R.id.main),getString(R.string.loi)+" "+exception.getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void dangKy() {
        /**
         * Thay đổi layout, bind lại các các component có trong layout
         */
        setContentView(R.layout.dang_ky);
        EditText emailDangKy=findViewById(R.id.editTextTextEmail);
        EditText matKhauDangKy=findViewById(R.id.editTextTextPasswordDangKy);
        EditText matKhauDangKyComfirm=findViewById(R.id.editTextTextPasswordComfirm);
        Button buttonTaoTaiKhoan=findViewById(R.id.buttonTaoTaiKhoan);
        Button buttonHuy=findViewById(R.id.buttonHuy);
        Button buttonGuiLaiEmail=findViewById(R.id.buttonGuiLaiEmail);
        /**
         * dang xuat
         */
        auth.signOut();
        /**
         * delay gui lai email
         */
        CountDownTimer timer= new CountDownTimer(Long.parseLong(getString(R.string.thoi_gian_gui_lai_email_milisecond)),
                1000){
            @Override
            public void onTick(long millisUntilFinished) {
                buttonGuiLaiEmail.setText(getString(R.string.gui_lai_email_sau) +" "+ (millisUntilFinished / 1000) + "s");
            }
            @Override
            public void onFinish() {
                buttonGuiLaiEmail.setEnabled(true);
                buttonGuiLaiEmail.setText(getString(R.string.gui_lai_email));
            }
        };
        buttonGuiLaiEmail.setEnabled(false);
        buttonGuiLaiEmail.setOnClickListener(view -> {
            if(!Internet.coKetNoi(this)){
                Snackbar.make(findViewById(R.id.main),getString(R.string.khong_co_internet),
                        Snackbar.LENGTH_SHORT).show();
                return;
            }
            guiEmailXacThuc();
            buttonGuiLaiEmail.setEnabled(false);
            timer.start();
        });
        buttonHuy.setOnClickListener(v->{
            setContentView(R.layout.activity_main);
            /**
             * khởi tạo lại các thành phần của view đăng nập
             */
            khoiTaoViewDangNhap();
        });
        buttonTaoTaiKhoan.setOnClickListener(v->{
            String email=emailDangKy.getText().toString().trim();
            String matKhau1=matKhauDangKy.getText().toString().trim();
            String matKhau2=matKhauDangKyComfirm.getText().toString().trim();
            if(matKhau1.length()>=6&&matKhau1.length()<=32){
                if(matKhau1.equals(matKhau2)){
                    Pattern pattern=Pattern.compile(getString(R.string.bieu_thuc_chinh_quy_kiem_tra_email));
                    if(pattern.matcher(email).matches()){
                        if(!Internet.coKetNoi(this))
                            Toast.makeText(this,getString(R.string.khong_co_internet),Toast.LENGTH_SHORT).show();
                        else {
                            manHinhDangTai.hienThi(getString(R.string.dang_dang_ky));
                            auth.createUserWithEmailAndPassword(email,matKhau1)
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()) {
                                            guiEmailXacThuc();
                                            timer.start();
                                        }else{
                                            manHinhDangTai.an();
                                            Exception exception=task.getException();
                                            if (exception instanceof
                                                    FirebaseAuthInvalidCredentialsException) {
                                                Snackbar.make(findViewById(R.id.dangKy),R.string.
                                                        email_hoac_mat_khau_khong_hop_le,Snackbar.LENGTH_SHORT).show();
                                            }
                                            else if (exception instanceof FirebaseAuthUserCollisionException) {
                                                Snackbar.make(findViewById(R.id.dangKy),R.string.email_da_ton_tai,
                                                        Snackbar.LENGTH_SHORT).show();
                                            }
                                            else if (exception instanceof FirebaseTooManyRequestsException) {
                                                Snackbar.make(findViewById(R.id.dangKy),R.string.thu_qua_nhieu_lan,
                                                        Snackbar.LENGTH_SHORT).show();
                                            }
                                            else if (exception instanceof FirebaseNetworkException) {
                                                Snackbar.make(findViewById(R.id.dangKy),R.string.khong_co_internet,
                                                        Snackbar.LENGTH_SHORT).show();
                                            }
                                            else if (exception != null) {
                                                Snackbar.make(findViewById(R.id.dangKy),getString(R.string.loi)+" "+exception.getMessage(),
                                                        Snackbar.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }else{
                        emailDangKy.setError(getString(R.string.sai_dinh_dang_email));
                    }
                }else{
                    matKhauDangKyComfirm.setError(getString(R.string.mat_khau_khong_khop));
                }
            }else {
                matKhauDangKy.setError(getString(R.string.sai_dinh_dang_mat_khau));
            }
        });
    }
    private void guiEmailXacThuc() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        manHinhDangTai.an();
                        if (task.isSuccessful()) {
                            Toast.makeText(this, getString(R.string.gui_email_xac_thuc_thanh_cong), Toast.LENGTH_SHORT).show();
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseTooManyRequestsException) {
                                Toast.makeText(this,
                                        getString(R.string.thu_qua_nhieu_lan),
                                        Toast.LENGTH_LONG).show();
                            }
                            else if (exception instanceof FirebaseNetworkException) {
                                Toast.makeText(this,
                                        getString(R.string.khong_co_internet),
                                        Toast.LENGTH_SHORT).show();
                            }
                            else if (exception != null) {
                                Toast.makeText(this,
                                                getString(R.string.loi) +" "+ task.getException().getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
        } else {
            manHinhDangTai.an();
            Toast.makeText(this, R.string.tai_khoan_khong_tai_duoc_xac_thuc, Toast.LENGTH_SHORT).show();
        }
    }

    private void dangNhap() {
        String email=emailDangNhap.getText().toString().trim();
        String matKhau=matKhauDangNhap.getText().toString().trim();
        if(!email.isEmpty()&&!matKhau.isEmpty()){
            if (matKhau.length() >= 6 && matKhau.length() <= 32) {
                Pattern pattern=Pattern.compile(getString(R.string.bieu_thuc_chinh_quy_kiem_tra_email));
                if(!Internet.coKetNoi(this))
                    Snackbar.make(findViewById(R.id.main),R.string.khong_co_internet,
                            Snackbar.LENGTH_SHORT).show();
                else if(pattern.matcher(email).matches()){
                    manHinhDangTai.hienThi(getString(R.string.dang_dang_nhap));
                    dangNhapVoiEmail(email,matKhau);
                }else{
                    if (email.length() >= 6 && email.length() <= 12) {
                        manHinhDangTai.hienThi(getString(R.string.dang_dang_nhap));
                        firestore.collection(NguoiChoi.TEN_COLLECTION)
                                .whereEqualTo(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP,email)
                                .get().addOnCompleteListener(task ->{
                                    if(task.isSuccessful()&&!task.getResult().isEmpty()){
                                        String foundEmail = task
                                                .getResult()
                                                .getDocuments()
                                                .get(0)
                                                .getString(NguoiChoi.TEN_TRUONG_EMAIL);
                                        dangNhapVoiEmail(foundEmail,matKhau);
                                    }else{
                                        manHinhDangTai.an();
                                        Snackbar.make(findViewById(R.id.main),R.string.dang_nhap_that_bai_loi,
                                                Snackbar.LENGTH_SHORT).show();
                                        if (task.getException() != null) {
                                            Toast.makeText(this,getString(R.string.loi)+" "
                                                    +task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }else {
                        emailDangNhap.setError(getString(R.string.tai_khoan_qua_ngan_qua_dai));
                    }
                }
            }else{
                matKhauDangNhap.setError(getString(R.string.sai_dinh_dang_mat_khau));
            }
        }else{
            matKhauDangNhap.setError(getString(R.string.thieu_tai_khoan_mat_khau));
            emailDangNhap.setError(getString(R.string.thieu_tai_khoan_mat_khau));
        }
    }
    private void dangNhapVoiEmail(String email, String matKhau) {
        auth.signInWithEmailAndPassword(email,matKhau).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser user=auth.getCurrentUser();
                if(user==null){
                    manHinhDangTai.an();
                    Snackbar.make(findViewById(R.id.main),R.string.dang_nhap_that_bai,
                            Snackbar.LENGTH_SHORT).show();
                } else if (!user.isEmailVerified()) {
                    if (!dialogDangMo) {
                        dialogDangMo = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setMessage(R.string.tai_khoan_chua_xac_thuc_gui_lai_email)
                                .setPositiveButton(R.string.gui_lai_email, (dialog, which) -> {
                                    long time=System.currentTimeMillis()- lanCuoiGuiLaiEmailXacThuc;
                                    long waitingTime=Long.parseLong(getString(
                                            R.string.thoi_gian_gui_lai_email_milisecond));
                                    if(time<waitingTime){
                                        Snackbar.make(findViewById(R.id.main),getString(R.string.gui_lai_email_sau)+" "+
                                                (waitingTime-time)/1000+"s",Snackbar.LENGTH_SHORT).show();
                                    }else {
                                        guiEmailXacThuc();
                                        lanCuoiGuiLaiEmailXacThuc = System.currentTimeMillis();
                                        luuThoiDiemGuiLaiEmailXacThuc();
                                        dialogDangMo=false;
                                        dialog.dismiss();
                                        dangXuat();
                                    }
                                })
                                .setNegativeButton(R.string.khong, (dialog, which) -> {
                                    dialogDangMo=false;
                                    dialog.dismiss();
                                    dangXuat();
                                });
                        AlertDialog dialog=builder.create();
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        manHinhDangTai.an();
                        dialog.show();
                    }
                }else{
                    hoanThanhDangNhap();
                }
            }else{
                Exception exception=task.getException();
                manHinhDangTai.an();
                if (exception instanceof FirebaseNetworkException) {
                    Toast.makeText(this,getString(R.string.khong_co_internet),
                            Toast.LENGTH_LONG).show();
                }else Snackbar.make(findViewById(R.id.main),R.string.dang_nhap_that_bai,
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * hoàn thành đăng nhập, thực hiện tuần tự các bước để vào sảnh chờ
     */
    public void hoanThanhDangNhap() {
        luuThongTinDangNhap(checkBoxLuuThongTinDangNhap.isChecked());
        khoiTaoTaiKhoanMoi();
    }

    /**
     * theo dõi offline khi tìm trận đấu
     * @param enable true để bật theo dõi, false để tắt theo dõi
     */
    public void theoDoiOfflineKhiTimTran(boolean enable){
        if(enable) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("DaVaoPhong", "offline");
            nguoiChoiTimTranReference.onDisconnect().updateChildren(hashMap);
        }else{
            nguoiChoiTimTranReference.onDisconnect().cancel();
        }
    }
    /**
     * tìm trận đấu bằng node chuyên biệt thay vì api
     * @param taoMoi true nếu tạo phòng mới
     * @param maPhong nếu tìm phòng đã có còn không thì -1
     */
    public void timTranDau(boolean taoMoi,String maPhong){
        String uid=auth.getUid();
        nguoiChoiTimTranReference=realtimeDatabase.getReference("NguoiChoiTimTran/"+uid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("ThoiGian",System.currentTimeMillis());
        hashMap.put("DaVaoPhong", "wait result");
        if(taoMoi) hashMap.put("TaoPhong",taoMoi);
        int temp=Integer.parseInt(maPhong);
        if(temp>0&&temp<10000) hashMap.put("MaPhong",maPhong);
        nguoiChoiTimTranReference.setValue(hashMap).addOnSuccessListener(success -> {
            //theo dõi để lấy kết quả tìm trậm
            theoDoiKetQuaTimTranListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        if(snapshot.child("KetQua").exists()){
                            boolean ketQua=snapshot.child("KetQua").getValue(Boolean.class);
                            if(ketQua){
                                choiOnline(snapshot.child("MaPhong").getValue(Long.class).toString());
                                //tắt theo dõi kết quả
                                nguoiChoiTimTranReference.removeEventListener(theoDoiKetQuaTimTranListener);
                            }else{
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("DaVaoPhong", "cancelled");
                                nguoiChoiTimTranReference.updateChildren(hashMap);
                                nguoiChoiTimTranReference.removeEventListener(theoDoiKetQuaTimTranListener);
                                theoDoiOfflineKhiTimTran(false);
                                manHinhDangTai.an();
                                Snackbar.make(findViewById(R.id.sanhCho),((temp>0&&temp<10000)
                                                ?R.string.phong_da_day_hoac_khong_ton_tai:R.string.loi_tim_phong),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }catch (Exception e){
                        manHinhDangTai.an();
                        Snackbar.make(findViewById(R.id.sanhCho),R.string.loi_tim_phong,
                                Snackbar.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    manHinhDangTai.an();
                    Snackbar.make(findViewById(R.id.sanhCho), R.string.loi_tim_phong,
                            Snackbar.LENGTH_SHORT).show();
                }
            };
            nguoiChoiTimTranReference.addValueEventListener(theoDoiKetQuaTimTranListener);
        }).addOnFailureListener(failure -> {
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.loi_tim_phong,
                    Snackbar.LENGTH_SHORT).show();
        });
        theoDoiOfflineKhiTimTran(true);
    }
    /**
     * tìm phòng theo id phòng
     */
    private void timPhongTheoIdPhongGame() {
        FirebaseUser user=auth.getCurrentUser();
        /**
         * kiểm tra người chơi
         */
        if(user==null) {
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.dang_nhap_het_han,
                    Snackbar.LENGTH_SHORT).show();
            dangXuat();
            return;
        }
        /**
         * kiểm tra tiền
         */
        if(nguoiChoi.getTien()<130){
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.game_khong_du_tien,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        /**
         * kiểm tra idGame
         */
        if(!nguoiChoi.getIdGame().equals("-1")){
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.game_cho_tran_dau_ket_thuc,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        timTranDau(false,editTextMaPhong.getText().toString());
    }

    /**
     * tạo phòng
     */
    private void taoPhongGame() {
        FirebaseUser user=auth.getCurrentUser();
        /**
         * kiểm tra người chơi
         */
        if(user==null) {
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.dang_nhap_het_han,
                    Snackbar.LENGTH_SHORT).show();
            dangXuat();
            return;
        }
        /**
         * kiểm tra tiền
         */
        if(nguoiChoi.getTien()<130){
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.game_khong_du_tien,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        /**
         * kiểm tra idGame
         */
        if(!nguoiChoi.getIdGame().equals("-1")){
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.game_cho_tran_dau_ket_thuc,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        timTranDau(true,"-1");
    }

    /**
     * gọi đến api tìm trận
     */
    private void timTranGame() {
        /**
         * kiểm tra người chơi
         */
        FirebaseUser user=auth.getCurrentUser();
        if(user==null) {
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.dang_nhap_het_han,
                    Snackbar.LENGTH_SHORT).show();
            dangXuat();
            return;
        }
        /**
         * kiểm tra tiền
         */
        if(nguoiChoi.getTien()<130){
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.game_khong_du_tien,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        /**
         * kiểm tra idGame
         */
        if(!nguoiChoi.getIdGame().equals("-1")){
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.game_cho_tran_dau_ket_thuc,
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        timTranDau(false,"-1");
    }

    private void farmData() {
        if (taiKhoanListener != null) {
            taiKhoanListener.remove();
        }
        setContentView(gamePanel);
        game.setTrangThaiGame(ThongSo.TrangThaiGame.TRANG_THAI_FARM_DATA,"");
    }

    private void choiVoiMay() {
        if (taiKhoanListener != null) {
            taiKhoanListener.remove();
        }
        setContentView(gamePanel);
        game.setTrangThaiGame(ThongSo.TrangThaiGame.TRANG_THAI_CHOI_VOI_MAY,"");
    }

    /**
     * vào game online
     * @param idPhong mã phòng được trả về từ các api tìm trận
     */
    private void choiOnline(String idPhong) {
        /**
         * bỏ theo dõi thay đổi tài khoản
         */
        if (taiKhoanListener != null) {
            taiKhoanListener.remove();
        }
        setContentView(gamePanel);
        manHinhDangTai.an();
        game.setTrangThaiGame(ThongSo.TrangThaiGame.TRANG_THAI_CHOI_ONLINE,idPhong);
    }

    /**
     * khởi tạo các giá trị cho tài khoản nếu đây là tài khoản mới
     */
    private void khoiTaoTaiKhoanMoi() {
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            firestore.collection(NguoiChoi.TEN_COLLECTION)
                    .whereEqualTo(NguoiChoi.TEN_TRUONG_EMAIL,user.getEmail())
                    .get().addOnCompleteListener(task ->{
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){
                                /**
                                 * nếu tài khoản mới tạo chưa hề có thông tin gì
                                 */
                                if (!dialogDangMo) {
                                    dialogDangMo = true;
                                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                                    LayoutInflater inflater=LayoutInflater.from(this);
                                    View view=inflater.inflate(R.layout.ten_dang_nhap,null);
                                    builder.setView(view);
                                    EditText editText=view.findViewById(R.id.editTextTextTenDangNhap);
                                    TextView textViewCanhBaoTenDangNhap=view.findViewById(
                                            R.id.textViewCanhBaoTenDangNhap);
                                    Button button=view.findViewById(R.id.buttonTenDangNhap);
                                    AlertDialog dialog=builder.create();
                                    button.setOnClickListener(v->{
                                        textViewCanhBaoTenDangNhap.setText("");
                                        String tenDangNhap=editText.getText().toString().trim();
                                        Pattern pattern=Pattern.compile(getString(
                                                R.string.bieu_thuc_chinh_quy_kiem_tra_ten_dang_nhap));
                                        if(pattern.matcher(tenDangNhap).matches()){
                                            firestore.collection(NguoiChoi.TEN_COLLECTION)
                                                    .whereEqualTo(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP,tenDangNhap)
                                                    .get().addOnCompleteListener(task1 ->{
                                                        if(task1.isSuccessful()){
                                                            if(task1.getResult().isEmpty()){
                                                                Map<String,Object> data=new HashMap<>();
                                                                data.put(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP,tenDangNhap);
                                                                data.put(NguoiChoi.TEN_TRUONG_EMAIL,user.getEmail());
                                                                data.put(NguoiChoi.TEN_TRUONG_ID_GAME,"-1");
                                                                data.put(NguoiChoi.TEN_TRUONG_TIEN,700);
                                                                firestore.collection(NguoiChoi.TEN_COLLECTION)
                                                                        .document(user.getUid())
                                                                        .set(data).addOnSuccessListener(task2->{
                                                                                manHinhDangTai.hienThi();
                                                                                theoDoiDangNhap();})
                                                                        .addOnFailureListener(task2->{
                                                                            Toast.makeText(this,
                                                                                    getString(R.string.loi)
                                                                                    +" "+task2.getMessage(),
                                                                                    Toast.LENGTH_SHORT)
                                                                                    .show();
                                                                            dangXuat();
                                                                        });
                                                                dialogDangMo=false;
                                                                dialog.dismiss();
                                                            }else{
                                                                textViewCanhBaoTenDangNhap.setText(R.string.ten_dang_nhap_da_ton_tai);
                                                            }
                                                        }else{
                                                            if (task1.getException() != null) {
                                                                Toast.makeText(this,getString(R.string.loi)
                                                                        +task1.getException().getMessage(),
                                                                        Toast.LENGTH_SHORT)
                                                                        .show();
                                                                dangXuat();
                                                            }
                                                        }
                                                    });
                                        }else{
                                            textViewCanhBaoTenDangNhap.setText(R.string.ten_dang_nhap_khong_hop_le);
                                        }
                                    });
                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(false);
                                    manHinhDangTai.an();
                                    dialog.show();
                                }
                            }else{
                                theoDoiDangNhap();
                            }
                        }else{
                            if (task.getException() != null) {
                                Toast.makeText(this,getString(R.string.loi)
                                                        +task.getException().getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                                dangXuat();
                            }
                        }
                    });
        }else{
            Snackbar.make(findViewById(R.id.main),R.string.dang_nhap_that_bai,
                    Snackbar.LENGTH_SHORT).show();
        }
    }
    /**
     * lưu thời điểm đăng nhập và theo dõi thay đổi để xác định xem có bị đăng nhập ở thiết bị khác không
     */
    private void theoDoiDangNhap() {
        FirebaseUser user=auth.getCurrentUser();
        if(user==null) {
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.dang_nhap_het_han,
                    Snackbar.LENGTH_SHORT).show();
            dangXuat();
            return;
        }
        DocumentReference documentReference= firestore.collection(NguoiChoi.TEN_COLLECTION)
                .document(user.getUid());
        lanCuoiDangNhap=System.currentTimeMillis();
        documentReference.update(NguoiChoi.TEN_TRUONG_LAN_CUOI_DANG_NHAP,lanCuoiDangNhap)
                .addOnSuccessListener(task->{
                    /**
                     * theo dõi thiết bị khác đăng nhập
                     */
                    dangNhapListener=documentReference
                            .addSnapshotListener((value, error) -> {
                                if(error!=null||value==null||!value.exists()) return;
                                if(lanCuoiDangNhap!=value.getLong("LanCuoiDangNhap")) {
                                    Toast.makeText(this,R.string.dang_nhap_tren_thiet_bi_khac,
                                            Toast.LENGTH_SHORT).show();
                                    if(game.getTrangThaiGame()==ThongSo.TrangThaiGame.TRANG_THAI_CHOI_ONLINE){
                                        game.choiOnline.thoat();
                                    }
                                    dangXuat();
                                }
                            });
                }).addOnFailureListener(task->{
                    Snackbar.make(findViewById(R.id.main),
                            R.string.dang_nhap_that_bai,Snackbar.LENGTH_SHORT).show();
                    dangXuat();
                });
        khoiTaoDuLieuKhac();
    }
    /**
     * khởi tạo các các dữ liệu cần có trước các để view hoạt động được
     */
    private void khoiTaoDuLieuKhac(){
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            // Xử lý trường hợp người dùng chưa đăng nhập
            dangXuat();
            return;
        }
        Task<QuerySnapshot> layDanhSachYeuCauHoTroTask= firestore.collection("ChamSocKhachHang")
                .whereEqualTo("UserId", user.getUid())
                .whereGreaterThan("TrangThai", 0)
                .orderBy("ThoiGianTao", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get();
        List<Task<?>> allTasks = new ArrayList<>();
        allTasks.add(layDanhSachYeuCauHoTroTask);
        Tasks.whenAllSuccess(allTasks).addOnSuccessListener(result -> {
            //dữ liệu yêu cầu hỗ trợ
            QuerySnapshot danhSachYeuCauHoTroSnashot=(QuerySnapshot) result.get(0);
            if (danhSachYeuCauHoTro == null) {
                danhSachYeuCauHoTro = new java.util.ArrayList<>();
            }
            danhSachYeuCauHoTro.clear();
            for (com.google.firebase.firestore.QueryDocumentSnapshot document : danhSachYeuCauHoTroSnashot) {
                YeuCauHoTro yeuCau = document.toObject(YeuCauHoTro.class);
                if (yeuCau.getTrangThai() == 1) {
                    document.getReference().addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Snackbar.make(findViewById(R.id.main),
                                    R.string.loi_tai_du_lieu,Snackbar.LENGTH_SHORT).show();
                            dangXuat();
                            return;
                        }
                        if (value != null && value.exists()) {
                            YeuCauHoTro updatedYeuCau = value.toObject(YeuCauHoTro.class);
                            // Tìm và cập nhật yêu cầu trong danh sách
                            for (int i = 0; i < danhSachYeuCauHoTro.size(); i++) {
                                if (danhSachYeuCauHoTro.get(i).getId().equals(updatedYeuCau.getId())) {
                                    danhSachYeuCauHoTro.set(i, updatedYeuCau);
                                    break;
                                }
                            }
                        }
                    });
                }
                danhSachYeuCauHoTro.add(yeuCau);
            }
            kiemTraIdGame();
        }).addOnFailureListener(e -> {
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.main),
                    R.string.loi_tai_du_lieu,Snackbar.LENGTH_SHORT).show();
            dangXuat();
        });
    }
    private void kiemTraIdGame() {
//        FirebaseUser user=auth.getCurrentUser();
//        if(user==null) {
//            manHinhDangTai.an();
//            Snackbar.make(findViewById(R.id.sanhCho),R.string.dang_nhap_het_han,
//                    Snackbar.LENGTH_SHORT).show();
//            dangXuat();
//            return;
//        }
//        db.collection(NguoiChoi.TEN_COLLECTION)
//                .document(user.getUid()).get()
//                .addOnSuccessListener(task->{
//                    if(!task.getString(NguoiChoi.TEN_TRUONG_ID_GAME).equals("-1")){
//                        choiOnline(task.getString(NguoiChoi.TEN_TRUONG_ID_GAME));
//                    }else{
//                        vaoSanhCho();
//                    }
//        }).addOnFailureListener(task->{
//            Snackbar.make(findViewById(R.id.main),
//                    getString(R.string.loi)+" "+task.getMessage(),Snackbar.LENGTH_SHORT).show();
//            dangXuat();
//        });
        vaoSanhCho();
    }

    /**
     * khởi tạo, tải thông tin lên view sảnh chờ
     */
    public void vaoSanhCho() {
        FirebaseUser user=auth.getCurrentUser();
        if(user==null) {
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.dang_nhap_het_han,
                    Snackbar.LENGTH_SHORT).show();
            dangXuat();
            return;
        }
        /**
         * khởi tạo lại, bind lại các component trong sảnh chờ
         */
        setContentView(R.layout.sanh_cho);
        khoiTaoViewSanhCho();
        /**
         * tải thông tin top đại gia lên view sảnh chờ
         */
        firestore.collection(NguoiChoi.TEN_COLLECTION)
                .orderBy(NguoiChoi.TEN_TRUONG_TIEN, Query.Direction.DESCENDING).limit(5)
                .get().addOnSuccessListener(task->{
                    int dem=0;
                    for(DocumentSnapshot documentSnapshot:task.getDocuments()){
                        switch (dem){
                            case 0:
                                textViewFirst.setText(documentSnapshot.getString(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP)
                                        +"\n"+documentSnapshot.getLong(NguoiChoi.TEN_TRUONG_TIEN)+"$");
                                dem++;
                                break;
                            case 1:
                                textViewSecond.setText(documentSnapshot.getString(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP)
                                        +"\n"+documentSnapshot.getLong(NguoiChoi.TEN_TRUONG_TIEN)+"$");
                                dem++;
                                break;
                            case 2:
                                textViewThird.setText(documentSnapshot.getString(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP)
                                        +"\n"+documentSnapshot.getLong(NguoiChoi.TEN_TRUONG_TIEN)+"$");
                                dem++;
                                break;
                            case 3:
                                textViewFouth.setText(documentSnapshot.getString(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP)
                                        +"\n"+documentSnapshot.getLong(NguoiChoi.TEN_TRUONG_TIEN)+"$");
                                dem++;
                                break;
                            case 4:
                                textViewFifth.setText(documentSnapshot.getString(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP)
                                        +"\n"+documentSnapshot.getLong(NguoiChoi.TEN_TRUONG_TIEN)+"$");
                                dem++;
                                break;
                        }
                    }
                }).addOnFailureListener(task->{
                    Snackbar.make(findViewById(R.id.main),
                            getString(R.string.loi)+" "+task.getMessage(),Snackbar.LENGTH_SHORT).show();
                    /**
                     * ẩn đi nếu gặp lỗi
                     */
                    textViewFirst.setVisibility(View.INVISIBLE);
                    textViewSecond.setVisibility(View.INVISIBLE);
                    textViewThird.setVisibility(View.INVISIBLE);
                    textViewFouth.setVisibility(View.INVISIBLE);
                    textViewFifth.setVisibility(View.INVISIBLE);
                });
        /**
         * tải thông tin tài khoản lên view sảnh chờ, theo dõi thông tin này nếu có thay đổi trên server thì sẽ thay đổi theo
         */
        taiKhoanListener= firestore.collection(NguoiChoi.TEN_COLLECTION)
                .document(user.getUid())
                .addSnapshotListener((value, error) -> {
                    if(error!=null||value==null||!value.exists()) {
                        Toast.makeText(this,error.getMessage(),Toast.LENGTH_SHORT).show();
                        dangXuat();
                    }
                    if (nguoiChoi == null) {
                        nguoiChoi=new NguoiChoi(value.getString(NguoiChoi.TEN_TRUONG_EMAIL),
                                value.getString(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP),
                                value.getString(NguoiChoi.TEN_TRUONG_ID_GAME),
                                value.getLong(NguoiChoi.TEN_TRUONG_TIEN));
                    }else{
                        nguoiChoi.setTien(value.getLong(NguoiChoi.TEN_TRUONG_TIEN));
                        nguoiChoi.setIdGame(value.getString(NguoiChoi.TEN_TRUONG_ID_GAME));
                        nguoiChoi.setTenDangNhap(value.getString(NguoiChoi.TEN_TRUONG_TEN_DANG_NHAP));
                        nguoiChoi.setEmail(value.getString(NguoiChoi.TEN_TRUONG_EMAIL));
                    }
                    textViewTenNguoiChoi.setText(nguoiChoi.getTenDangNhap());
                    textViewTien.setText(String.valueOf(nguoiChoi.getTien()));
                    manHinhDangTai.an();
                });
    }
    /**
     * tải thông tin đăng nhập nếu trước đó đã bấm "lưu thông tin đăng nhập"
     */
    private void taiThongTin() {
        SharedPreferences sharedPreferences = getSharedPreferences("thongTinDangNhap", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("luuThongTinDangNhap",false)
                &&Internet.coKetNoi(this)){
            checkBoxLuuThongTinDangNhap.setChecked(true);
            FirebaseUser user=auth.getCurrentUser();
            if(user!=null){
                hoanThanhDangNhap();
                sharedPreferences=getSharedPreferences("thoiGian",MODE_PRIVATE);
                lanCuoiResetMatKhau=sharedPreferences.getLong("lanCuoiResetMatKhau",0);
                lanCuoiGuiLaiEmailXacThuc =sharedPreferences.getLong("lanCuoiGuiLaiEmail",0);
            }else{
                sharedPreferences=getSharedPreferences("thoiGian",MODE_PRIVATE);
                lanCuoiResetMatKhau=sharedPreferences.getLong("lanCuoiResetMatKhau",0);
                lanCuoiGuiLaiEmailXacThuc =sharedPreferences.getLong("lanCuoiGuiLaiEmail",0);
                manHinhDangTai.an();
            }
        }else{
            manHinhDangTai.an();
        }
    }
    /**
     * lưu thông tin đăng nhập nếu checkbox đã được chọn, các lần sau sẽ t động đăng nhập
     */
    private void luuThongTinDangNhap(boolean luuThongTinDangNhap) {
        SharedPreferences sharedPreferences=getSharedPreferences("thongTinDangNhap",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("luuThongTinDangNhap",luuThongTinDangNhap);
        editor.apply();
    }

    /**
     * lưu thời điểm lần cuối gửi lại email xác thực vào để sử dụng cả khi vào lại game (xoá dữ liệu app là cút:)
     */
    private void luuThoiDiemGuiLaiEmailXacThuc() {
        SharedPreferences sharedPreferences=getSharedPreferences("thoiGian",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong("lanCuoiGuiLaiEmail", lanCuoiGuiLaiEmailXacThuc);
        editor.apply();
    }

    /**
     * lưu thời điểm lần cuối reset mật khẩu vào để sử dụng cả khi vào lại game (xoá dữ liệu app là cút:)
     */
    private void luuThoiDiemResetMatKhau(){
        SharedPreferences sharedPreferences=getSharedPreferences("thoiGian",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong("lanCuoiResetMatKhau",lanCuoiResetMatKhau);
        editor.apply();
    }
    public static Context getContext(){
        return gameContext;
    }

    /**
     * đăng xuất ra màn hình đăng nhập
     */
    public void dangXuat(){
        manHinhDangTai.an();
        auth.signOut();
        nguoiChoi=null;
        lanCuoiDangNhap=0;
        /**
         * huỷ các listener trong game trường hợp bị log acc
         */
        game.huyTheoDoiNguoiChoiOnline();
        if (dangNhapListener != null) {
            dangNhapListener.remove();
        }
        if (taiKhoanListener != null) {
            taiKhoanListener.remove();
        }
        setContentView(R.layout.activity_main);
        /**
         * khởi tạo lại các thành phần của view đăng nhập
         */
        khoiTaoViewDangNhap();
    }

    /**
     * Yêu cầu quyền ghi vào bộ nhớ
     * @param requestCode The request code passed in {@link
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==ThongSo.Quyen.WRITE_EXTERNAL_STORAGE){
            if(grantResults.length==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                finish();
            }
        }
    }

    public void toast(String string) {
        Toast.makeText(this, string,
                Toast.LENGTH_SHORT).show();
    }
}