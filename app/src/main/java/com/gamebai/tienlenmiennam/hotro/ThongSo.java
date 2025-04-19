package com.gamebai.tienlenmiennam.hotro;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.main.MainActivity;

/**
 * thông số chung cho game
 */
public class ThongSo {
    public static class ThongSoManHinh {
        private static final float chieuCaoManHinhMacDinh=1344;
        private static final float chieuRongManHinhMacDinh=2992;
        public static float TI_LE_CHIEU_CAO_MAN_HINH=1;
        public static float TI_LE_CHIEU_RONG_MAN_HINH=1;
        public static void setTiLeChieuCaoManHinh(float chieuCaoManHinh){
            TI_LE_CHIEU_CAO_MAN_HINH=chieuCaoManHinh/chieuCaoManHinhMacDinh;
            LaBai.TI_LE_LA_BAI_CAO= LaBai.TI_LE_LA_BAI_CAO*TI_LE_CHIEU_CAO_MAN_HINH;
        }
        public static void setTiLeChieuRongManHinh(float chieuRongManHinh){
            TI_LE_CHIEU_RONG_MAN_HINH=chieuRongManHinh/chieuRongManHinhMacDinh;
            LaBai.TI_LE_LA_BAI_RONG= LaBai.TI_LE_LA_BAI_RONG*TI_LE_CHIEU_RONG_MAN_HINH;
        }

        /**
         * trả về tỉ lệ của cao hoặc rộng nếu nó nhỏ hơn cái kia, dùng co cho hình vuông hay tròn
         * @return
         */
        public static float getTiLe1Chieu(){
            return Math.min(ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,
                    ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH);
        }
    }
    public static class TrangThaiGame{
        public static final int TRANG_THAI_SANH_CHO=1;
        public static final int TRANG_THAI_CHOI_VOI_MAY=2;
        public static final int TRANG_THAI_FARM_DATA=3;
        public static final int TRANG_THAI_CHOI_ONLINE = 4;
    }
    public static class LaBai {
        public static final float KICH_THUOC_LA_BAI_CAO_MAC_DINH =1056;
        public static final float KICH_THUOC_LA_BAI_RONG_MAC_DINH=691;
        public static float TI_LE_LA_BAI_CAO= (float) (1 /3.5);
        public static float TI_LE_LA_BAI_RONG= (float) (1 /3.5);
//        public static float TI_LE_LA_BAI_DANH_RA=0.9f;
        public static final float TI_LE_LA_BAI_NHO=0.6f;
//        public static float TI_LE_LA_BAI=1;
        public static boolean RENDER_CHAT_LUONG=false;
        public static float getKichThuocLaBaiCao(){
            return KICH_THUOC_LA_BAI_CAO_MAC_DINH *TI_LE_LA_BAI_CAO;
        }
        public static float getKichThuocLaBaiRong(){
            return KICH_THUOC_LA_BAI_RONG_MAC_DINH *TI_LE_LA_BAI_RONG;
        }
        public static float getKichThuocLaBaiNhoRong() {
            return KICH_THUOC_LA_BAI_RONG_MAC_DINH *TI_LE_LA_BAI_RONG * TI_LE_LA_BAI_NHO;
        }
        public static float getKichThuocLaBaiNhoCao() {
            return KICH_THUOC_LA_BAI_CAO_MAC_DINH *TI_LE_LA_BAI_CAO * TI_LE_LA_BAI_NHO;
        }
    }
    public static class Avatar{
        public static final float KICH_THUOC_CAO_AVATAR_MAC_DINH=250;
        public static final float KICH_THUOC_RONG_AVATAR_MAC_DINH=250;
        public static float getTiLeKichThuocAvartar(){
            return Math.min(ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,
                    ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH);
        }
        public static float getKichThuocCaoAvatar(){
            return KICH_THUOC_CAO_AVATAR_MAC_DINH*getTiLeKichThuocAvartar();
        }
        public static float getKichThuocRongAvatar(){
            return KICH_THUOC_RONG_AVATAR_MAC_DINH*getTiLeKichThuocAvartar();
        }
    }
    public static class CanhBaoChonBai{
        public static final String NOI_DUNG= MainActivity.getContext().getString(R.string.game_bai_danh_khong_hop_le);
        public static final float VI_TRI_MAC_DINH_X=150;
        public static final float VI_TRI_MAC_DINH_Y=200;
        public static final float KICH_CO_MAC_DINH=55;
        public static float getKichCo(){
            return KICH_CO_MAC_DINH*
                    Math.min(ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,
                            ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH);
        }
        public static float getViTriX(){
            return VI_TRI_MAC_DINH_X*ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH;
        }
        public static float getViTriY(){
            return VI_TRI_MAC_DINH_Y*ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH;
        }
    }
    public static class ThongBaoKetQua{
        public static final String NOI_DUNG_THANG=MainActivity.getContext().getString(R.string.game_ban_thang);
        public static final String NOI_DUNG_THUA=MainActivity.getContext().getString(R.string.game_ban_thua);
        public static final float KICH_CO_MAC_DINH=200;
        public static float getKichCo(){
            return KICH_CO_MAC_DINH*
                    Math.min(ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,
                            ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH);
        }
    }
    public static class ThongTinNguoiChoiTrongTran{
        public static final String NOI_DUNG_TEN=MainActivity.getContext()
                .getString(R.string.game_ten_nguoi_choi_mac_dinh);
        public static final String NOI_DUNG_TIEN=MainActivity
                .getContext().getString(R.string.game_tien_mac_dinh);
        public static final float KICH_CO_MAC_DINH=45;
        public static float getKichCo(){
            return KICH_CO_MAC_DINH*
                    Math.min(ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,
                            ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH);
        }
    }
    public static class DemNguoc{
        public static final String NOI_DUNG_NHAT=MainActivity.getContext().getString(R.string.game_nhat);
        public static final String NOI_DUNG_TU_QUY=MainActivity.getContext().getString(R.string.game_tu_quy_2);
        public static final float KICH_CO_MAC_DINH=155;
        public static float getKichCo(){
            return KICH_CO_MAC_DINH*
                    Math.min(ThongSo.ThongSoManHinh.TI_LE_CHIEU_CAO_MAN_HINH,
                            ThongSo.ThongSoManHinh.TI_LE_CHIEU_RONG_MAN_HINH);
        }
    }
    public static class HoatAnhLaBaiDiChuyen {
        public static final int SO_FRAME=8;
        public static final int FPS_MAC_DINH=60;
        public static final float KHOANG_CACH_THOI_GIAN_GIUA_2_FRAME= (float) 1 /FPS_MAC_DINH;
    }
    public static class TranDau {
        public static final int THOI_GIAN_CHO_CHON=15;
        public static final int THOI_GIAN_HIEN_CANH_BAO=1;
        public static final long THOI_GIAN_CHO_THOAT_GAME_MILISECOND=5000;
        public static final int THOI_GIAN_CHO_BAT_DAU = 5;
        public static final float THOI_GIAN_YEU_CAU_DANH_BAI = 1.5f;
        public static final int SO_TIEN_MOI_LA_BAI=10;
    }
    public static class UI{
        public static final float KHOANG_CACH_GIUA_PHAN_TU_CAO=50;
        public static final float KHOANG_CACH_GIUA_PHAN_TU_NGANG=50;
    }
    public static class Quyen{
        public static final int WRITE_EXTERNAL_STORAGE=1;
    }
}
