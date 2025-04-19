package com.gamebai.tienlenmiennam.thucthe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.hotro.PhuongThucBitmap;
import com.gamebai.tienlenmiennam.hotro.ThongSo;
import com.gamebai.tienlenmiennam.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum LaBai implements PhuongThucBitmap {
    HAI_CO(R.drawable.card_143,14,3),HAI_RO(R.drawable.card_142,14,2),HAI_TEP(R.drawable.card_141,14,1),HAI_BICH(R.drawable.card_140,14,0),
    BA_CO(R.drawable.card_23,2,3),BA_RO(R.drawable.card_22,2,2),BA_TEP(R.drawable.card_21,2,1),BA_BICH(R.drawable.card_20,2,0),
    BON_CO(R.drawable.card_33,3,3),BON_RO(R.drawable.card_32,3,2),BON_TEP(R.drawable.card_31,3,1),BON_BICH(R.drawable.card_30,3,0),
    NAM_CO(R.drawable.card_43,4,3),NAM_RO(R.drawable.card_42,4,2),NAM_TEP(R.drawable.card_41,4,1),NAM_BICH(R.drawable.card_40,4,0),
    SAU_CO(R.drawable.card_53,5,3),SAU_RO(R.drawable.card_52,5,2),SAU_TEP(R.drawable.card_51,5,1),SAU_BICH(R.drawable.card_50,5,0),
    BAY_CO(R.drawable.card_63,6,3),BAY_RO(R.drawable.card_62,6,2),BAY_TEP(R.drawable.card_61,6,1),BAY_BICH(R.drawable.card_60,6,0),
    TAM_CO(R.drawable.card_73,7,3),TAM_RO(R.drawable.card_72,7,2),TAM_TEP(R.drawable.card_71,7,1),TAM_BICH(R.drawable.card_70,7,0),
    CHIN_CO(R.drawable.card_83,8,3),CHIN_RO(R.drawable.card_82,8,2),CHIN_TEP(R.drawable.card_81,8,1),CHIN_BICH(R.drawable.card_80,8,0),
    MUOI_CO(R.drawable.card_93,9,3),MUOI_RO(R.drawable.card_92,9,2),MUOI_TEP(R.drawable.card_91,9,1),MUOI_BICH(R.drawable.card_90,9,0),
    J_CO(R.drawable.card_103,10,3),J_RO(R.drawable.card_102,10,2),J_TEP(R.drawable.card_101,10,1),J_BICH(R.drawable.card_100,10,0),
    Q_CO(R.drawable.card_113,11,3),Q_RO(R.drawable.card_112,11,2),Q_TEP(R.drawable.card_111,11,1),Q_BICH(R.drawable.card_110,11,0),
    K_CO(R.drawable.card_123,12,3),K_RO(R.drawable.card_122,12,2),K_TEP(R.drawable.card_121,12,1),K_BICH(R.drawable.card_120,12,0),
    A_CO(R.drawable.card_133,13,3),A_RO(R.drawable.card_132,13,2),A_TEP(R.drawable.card_131,13,1),A_BICH(R.drawable.card_130,13,0),
    LUNG_BAI(R.drawable.back_card,-1,-1);
    private Bitmap hinhAnh;
    private int so,chat;
    private boolean coTheChon;
    private boolean duocChon;
    private static Map<String,LaBai> laBaiMap =new HashMap<>();
    LaBai(int ResID,int so,int chat){
        options.inScaled=false;
        hinhAnh=getBitmapTheoTiLe(BitmapFactory.decodeResource(MainActivity.getContext().getResources(),ResID,options),
                ThongSo.LaBai.TI_LE_LA_BAI_RONG,
                ThongSo.LaBai.TI_LE_LA_BAI_CAO,
                ThongSo.LaBai.RENDER_CHAT_LUONG);
        this.so=so;
        this.chat=chat;
        coTheChon =false;
        duocChon =false;
    }

    /**
     * đưa lá bài vào hash map nhằm tìm kiếm nhanh hơn
     * khối static chỉ chạy một lần duy nhất khi lớp được tải
     */
    static {
        for(LaBai laBai:values()){
            laBaiMap.put(laBai.so+"_"+laBai.chat,laBai);
        }
    }

    /**
     * @deprecated CẢNH BÁO: ĐÂY LÀ ENUM, NẾU 2 BIẾN CÙNG THAM CHIẾU (=) ĐẾN PHƯƠNG THỨC NÀY VỚI CÙNG GIÁ TRỊ SỐ VÀ CHẤT CÓ NGHĨA LÀ 2 BIẾN ĐANG CÙNG THAM CHIẾU 1 LaBai DUY NHẤT
     * @param so
     * @param chat
     * @return
     */
    public static LaBai timLaBaiTheoSoVaChat(int so,int chat){
        return laBaiMap.get(so+"_"+chat);
    }
    public static ArrayList<LaBai> getAllLaBai(){
        return new ArrayList<>(laBaiMap.values());
    }
    /**
     * trả về LaBai có số và chất cho trước (ảnh bài là mặt lưng)
     * @deprecated CẢNH BÁO: ĐÂY LÀ ENUM, NẾU 2 BIẾN CÙNG THAM CHIẾU (=) ĐẾN PHƯƠNG THỨC NÀY CÓ NGHĨA LÀ 2 BIẾN ĐANG CÙNG THAM CHIẾU 1 LaBai DUY NHẤT (LUNG_BAI)
     * @param so
     * @param chat
     * @return
     */
    public static LaBai getTempInstance(int so,int chat){
        LaBai temp= com.gamebai.tienlenmiennam.thucthe.LaBai.LUNG_BAI;
        temp.setChat(chat);
        temp.setSo(so);
        return temp;
    }

    /**
     * trả về LaBai có số và chất là -1 (ảnh bài là mặt lưng)
     * @deprecated CẢNH BÁO: ĐÂY LÀ ENUM, NẾU 2 BIẾN CÙNG THAM CHIẾU (=) ĐẾN PHƯƠNG THỨC NÀY CÓ NGHĨA LÀ 2 BIẾN ĐANG CÙNG THAM CHIẾU 1 LaBai DUY NHẤT (LUNG_BAI)
     * @return
     */
    public static LaBai getTempInstance(){
        LaBai temp= com.gamebai.tienlenmiennam.thucthe.LaBai.LUNG_BAI;
        temp.setChat(-1);
        temp.setSo(-1);
        return temp;
    }

    /**
     *
     * @return ảnh lá bài cỡ thường
     */
    public Bitmap getHinhAnh(){
        return hinhAnh;
    }

    /**
     *
     * @param tiLeRong
     * @param tiLeCao
     * @return ảnh lá bài theo tỉ lệ mới (ảnh trước khi theo tỉ lệ mới đã có theo tỉ lệ bình thường)
     */
    public Bitmap getHinhAnhTheoTiLe(float tiLeRong,float tiLeCao){
        return getBitmapTheoTiLe(hinhAnh,tiLeRong,tiLeCao,ThongSo.LaBai.RENDER_CHAT_LUONG);
    }
    public int getSo(){
        return so;
    }
    public int getChat(){
        return chat;
    }

    public void setSo(int so) {
        this.so = so;
    }

    public void setChat(int chat) {
        this.chat = chat;
    }

    public boolean isCoTheChon() {
        return coTheChon;
    }
    public void setCoTheChon(boolean coTheChon) {
        this.coTheChon = coTheChon;
    }
    public boolean isDuocChon() {
        return duocChon;
    }
    public void setDuocChon(boolean duocChon) {
        this.duocChon = duocChon;
    }
    public boolean dongChat(LaBai a){
        return this.chat==a.getChat();
    }
    public boolean doi(LaBai a){
        return this.so==a.getSo();
    }
    public boolean noiNhau(LaBai a){
        return Math.abs(this.so-a.getSo())==1;
    }
    public boolean noiNhauDongChat(LaBai a){
        return Math.abs(this.so-a.getSo())==1&&this.dongChat(a);
    }
    public boolean lonHon(LaBai a){
        return this.getSo()>a.getSo()||(this.getSo()==a.getSo()&&this.getChat()>a.getChat());
    }
}
