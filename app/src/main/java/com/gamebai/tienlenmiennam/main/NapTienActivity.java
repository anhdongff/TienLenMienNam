package com.gamebai.tienlenmiennam.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.api.ApiService;
import com.gamebai.tienlenmiennam.api.NoiDungGiaoDichZaloPay;
import com.gamebai.tienlenmiennam.api.PhanHoiGiaoDichZaloPay;
import com.gamebai.tienlenmiennam.api.RetrofitClient;
import com.gamebai.tienlenmiennam.ui.ManHinhDangTai;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class NapTienActivity extends AppCompatActivity {
    FirebaseFirestore firestoreDatabase;
    FirebaseAuth auth;
    Button buttonNap10k,buttonNap50k,buttonNap100k,buttonNap500k,buttonNap200k,buttonNap20k;
    ManHinhDangTai manHinhDangTai;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nap_tien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nap_tien), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        manHinhDangTai=new ManHinhDangTai(this,null);
        manHinhDangTai.hienThi();
        /**
         * khởi tạo về database và zalopay sdk
         */
        firestoreDatabase=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        apiService= RetrofitClient.getInstance().create(ApiService.class);
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        buttonNap10k=findViewById(R.id.button_nap_10k);
        buttonNap10k.setOnClickListener(v->{
            manHinhDangTai.hienThi();
            napTien(10000);
        });
        buttonNap50k=findViewById(R.id.button_nap_50k);
        buttonNap50k.setOnClickListener(v->{
            manHinhDangTai.hienThi();
            napTien(50000);
        });
        buttonNap100k=findViewById(R.id.button_nap_100k);
        buttonNap100k.setOnClickListener(v->{
            manHinhDangTai.hienThi();
            napTien(100000);
        });
        buttonNap500k=findViewById(R.id.button_nap_500k);
        buttonNap500k.setOnClickListener(v->{
            manHinhDangTai.hienThi();
            napTien(500000);
        });
        buttonNap200k=findViewById(R.id.button_nap_200k);
        buttonNap200k.setOnClickListener(v->{
            manHinhDangTai.hienThi();
            napTien(2000000);
        });
        buttonNap20k=findViewById(R.id.button_nap_20k);
        buttonNap20k.setOnClickListener(v->{
            manHinhDangTai.hienThi();
            napTien(20000);
        });
        manHinhDangTai.an();
    }

    private void napTien(int soTien) {
        FirebaseUser user=auth.getCurrentUser();
        if(user==null) {
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.sanhCho),R.string.dang_nhap_het_han,
                    Snackbar.LENGTH_SHORT).show();
            finish();
        }
        /**
         * lấy token của user để xác thực trong middleware XacThucNguoiDung
         */
        user.getIdToken(false).addOnSuccessListener(tokenResult -> {
            NoiDungGiaoDichZaloPay noiDungGiaoDichZaloPay=
                    new NoiDungGiaoDichZaloPay(soTien,"Gói "+soTien/10+"$");
            apiService.taoDonGiaoDichZaloPay("Bearer "+tokenResult.getToken(),noiDungGiaoDichZaloPay)
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<PhanHoiGiaoDichZaloPay> call,
                                               Response<PhanHoiGiaoDichZaloPay> response) {
                            manHinhDangTai.an();
                            if(response.isSuccessful()){
                                if(response.body().getReturnCode()==1){
                                    /**
                                     * link phía dưới là deeplink để mở lại activity này, được định nghĩa trong manifest
                                     */
                                    ZaloPaySDK.getInstance().payOrder(NapTienActivity.this,
                                            response.body().getZpTransToken(),"tienlenmiennam://naptien",
                                            new PayOrderListener(){

                                                /**
                                                 * @param s
                                                 * @param s1
                                                 * @param s2
                                                 */
                                                @Override
                                                public void onPaymentSucceeded(String s, String s1, String s2) {
                                                    AlertDialog.Builder builder=new AlertDialog.Builder(NapTienActivity.this);
                                                    builder.setTitle(R.string.thanh_toan_thanh_cong);
                                                    builder.setMessage(R.string.thanh_toan_them);
                                                    builder.setPositiveButton(R.string.co,
                                                            (dialog, which) -> dialog.dismiss());
                                                    builder.setNegativeButton(R.string.khong,
                                                            (dialog, which) -> NapTienActivity.this.finish());
                                                    builder.show();
                                                }

                                                /**
                                                 * @param s
                                                 * @param s1
                                                 */
                                                @Override
                                                public void onPaymentCanceled(String s, String s1) {
                                                    AlertDialog.Builder builder=new AlertDialog.Builder(NapTienActivity.this);
                                                    builder.setTitle(R.string.thanh_toan_bi_huy);
                                                    builder.setMessage(R.string.thanh_toan_them);
                                                    builder.setPositiveButton(R.string.co,
                                                            (dialog, which) -> dialog.dismiss());
                                                    builder.setNegativeButton(R.string.khong,
                                                            (dialog, which) -> NapTienActivity.this.finish());
                                                    builder.show();
                                                }

                                                /**
                                                 * @param zaloPayError
                                                 * @param s
                                                 * @param s1
                                                 */
                                                @Override
                                                public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                                    int maLoi=0;
                                                    if(zaloPayError==ZaloPayError.PAYMENT_APP_NOT_FOUND)
                                                        maLoi=R.string.chua_tai_zalopay;
                                                    AlertDialog.Builder builder=new AlertDialog.Builder(NapTienActivity.this);
                                                    builder.setTitle(R.string.thanh_toan_that_bai);
                                                    builder.setMessage(getString(R.string.loi)+" "
                                                            +(maLoi==0?zaloPayError.toString():getString(maLoi))+"\n"
                                                            +getString(R.string.thanh_toan_them));
                                                    builder.setPositiveButton(R.string.co,
                                                            (dialog, which) -> dialog.dismiss());
                                                    builder.setNegativeButton(R.string.khong,
                                                            (dialog, which) -> NapTienActivity.this.finish());
                                                    builder.show();
                                                }
                                            });
                                }else{
                                    Snackbar.make(findViewById(R.id.nap_tien),
                                            getString(R.string.loi) + " " + response.body().getReturnMessage(),
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            }else{
                                try {
                                    JSONObject jsonObject=new JSONObject(response.errorBody().string());
                                    Snackbar.make(findViewById(R.id.nap_tien),
                                            getString(R.string.loi) + " " + jsonObject.getString("loi"),
                                            Snackbar.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Snackbar.make(findViewById(R.id.nap_tien),
                                            getString(R.string.loi) + " " + response.message(),
                                            Snackbar.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Snackbar.make(findViewById(R.id.nap_tien),
                                            getString(R.string.loi) + " " + response.message(),
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<PhanHoiGiaoDichZaloPay> call, Throwable t) {
                            manHinhDangTai.an();
                            Snackbar.make(findViewById(R.id.nap_tien),
                                    getString(R.string.loi) + " " + t.getMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });
        }).addOnFailureListener(task->{
            manHinhDangTai.an();
            Snackbar.make(findViewById(R.id.nap_tien),
                    getString(R.string.loi) + " " + task.getMessage(),
                    Snackbar.LENGTH_SHORT).show();
        });
    }

    /**
     * Đón intent khi activity đã được tạo, được override để đón intent của zalo
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}