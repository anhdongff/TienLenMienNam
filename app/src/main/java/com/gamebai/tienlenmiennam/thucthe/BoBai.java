package com.gamebai.tienlenmiennam.thucthe;

import android.graphics.PointF;

import com.gamebai.tienlenmiennam.hotro.ThongSo;
import com.gamebai.tienlenmiennam.main.MainActivity;
import com.gamebai.tienlenmiennam.ui.NutLaBai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * ma trận bộ bài lưu trữ thông tin sở hữu mỗi lá bài
 */
public class BoBai {
    /**
     * chỉ số sở hữu đối với mỗi lá bài, với một số sở hữu sẽ (người chơi, đánh) sẽ có các điểm đặt bài
     */
    public enum ChiSoSoHuu{
        VUA_DANH(-2,0,0,0,0,0,0),
        UNKNOW(0,0,0,0,0,0,0),
        DA_DANH(-1,
                (MainActivity.chieuRongManHinh - ThongSo.LaBai.getKichThuocLaBaiRong())/2,
                (MainActivity.chieuCaoManHinh- ThongSo.LaBai.getKichThuocLaBaiCao())/2,
                0,0,0,0),
        NGUOI_CHOI_1(1,
                (MainActivity.chieuRongManHinh -
                        (ThongSo.LaBai.getKichThuocLaBaiRong()*((float) 13)+ThongSo.Avatar.getKichThuocRongAvatar()))/2+
                        ThongSo.Avatar.getKichThuocRongAvatar()+10*ThongSo.Avatar.getTiLeKichThuocAvartar(),
                MainActivity.chieuCaoManHinh- ThongSo.LaBai.getKichThuocLaBaiCao(),
                (MainActivity.chieuRongManHinh -
                        (ThongSo.LaBai.getKichThuocLaBaiRong()*((float) 13)+ThongSo.Avatar.getKichThuocRongAvatar()))/2,
                MainActivity.chieuCaoManHinh-
                        (ThongSo.LaBai.getKichThuocLaBaiCao()+ThongSo.Avatar.getKichThuocCaoAvatar())/2,
                0,0),
        NGUOI_CHOI_2(2,
                MainActivity.chieuRongManHinh - ThongSo.LaBai.getKichThuocLaBaiRong(),
                (MainActivity.chieuCaoManHinh- ThongSo.LaBai.getKichThuocLaBaiCao())/2,
                MainActivity.chieuRongManHinh-ThongSo.Avatar.getKichThuocRongAvatar(),
                (MainActivity.chieuCaoManHinh- ThongSo.Avatar.getKichThuocCaoAvatar())/2,
                MainActivity.chieuRongManHinh-ThongSo.Avatar.getKichThuocRongAvatar()-
                        ThongSo.LaBai.getKichThuocLaBaiNhoRong()-10*ThongSo.Avatar.getTiLeKichThuocAvartar(),
                (MainActivity.chieuCaoManHinh-ThongSo.LaBai.getKichThuocLaBaiNhoCao())/2),
        NGUOI_CHOI_3(3,
                (MainActivity.chieuRongManHinh - ThongSo.LaBai.getKichThuocLaBaiRong())/2,
                0,
                (MainActivity.chieuRongManHinh-ThongSo.Avatar.getKichThuocRongAvatar())/2,
                0,
                (MainActivity.chieuRongManHinh+ThongSo.Avatar.getKichThuocRongAvatar())/2
                +10*ThongSo.Avatar.getTiLeKichThuocAvartar(),
                (ThongSo.Avatar.getKichThuocCaoAvatar()-ThongSo.LaBai.getKichThuocLaBaiNhoCao())/2),
        NGUOI_CHOI_4(4,
                0,
                (MainActivity.chieuCaoManHinh- ThongSo.LaBai.getKichThuocLaBaiCao())/2,
                0,
                (MainActivity.chieuCaoManHinh- ThongSo.Avatar.getKichThuocCaoAvatar())/2,
                ThongSo.Avatar.getKichThuocRongAvatar()+10*ThongSo.Avatar.getTiLeKichThuocAvartar(),
                (MainActivity.chieuCaoManHinh-ThongSo.LaBai.getKichThuocLaBaiNhoCao())/2);
        private int soHuu;
        private float dichX;
        private float dichY;
        private float viTriAvartarX;
        private float viTriAvartarY;
        private float viTriDemLaBaiX;
        private float viTriDemLaBaiY;
        private static Map<String,ChiSoSoHuu> chiSoMap=new HashMap<>();
        ChiSoSoHuu(int soHuu,float dichX,float dichY,float viTriAvartarX,float viTriAvartarY,
                   float viTriDemLaBaiX,float viTriDemLaBaiY){
            this.soHuu=soHuu;
            this.dichX=dichX;
            this.dichY=dichY;
            this.viTriAvartarX=viTriAvartarX;
            this.viTriAvartarY=viTriAvartarY;
            this.viTriDemLaBaiX=viTriDemLaBaiX;
            this.viTriDemLaBaiY=viTriDemLaBaiY;
        }
        static {
            for(ChiSoSoHuu chiSoSoHuu:values()){
                chiSoMap.put(String.valueOf(chiSoSoHuu.soHuu),chiSoSoHuu);
            }
        }
        public static ChiSoSoHuu timTheoSoHuu(int soHuu){
            return chiSoMap.get(String.valueOf(soHuu));
        }
        public float getDichX(){
            return dichX;
        }
        public float getDichY(){
            return dichY;
        }
        public int getChiSo(){
            return soHuu;
        }
        public float getViTriAvartarX(){
            return viTriAvartarX;
        }
        public float getViTriAvartarY() {
            return viTriAvartarY;
        }
        public float getViTriDemLaBaiX() {
            return viTriDemLaBaiX;
        }
        public float getViTriDemLaBaiY() {
            return viTriDemLaBaiY;
        }
    }
    /**
     * mỗi vị trí x tưng ứng với lá, vị trí y tương ứng với chất
     */
    private int[][] laBai =new int[15][4];
    public int[][] laBaiBanDau =new int[15][4];
    public int getSoHuu(int x,int y){return this.laBai[x][y];}
    public void setSoHuu(int so,int chat,int a){this.laBai[so][chat]=a;}
    public BoBai(){
        for(int i=0;i<15;i++){
            for(int j=0;j<4;j++){
                this.laBai[i][j]=0;
            }
        }
    }
    public void xoaMaTran(){
        for(int i=0;i<15;i++){
            for(int j=0;j<4;j++){
                this.laBai[i][j]=0;
            }
        }
    }
    public BoBai(int[][] temp){
        for(int i=0;i<15;i++){
            System.arraycopy(temp[i], 0, this.laBai[i], 0, 4);
        }
    }

    public int[][] getLaBai() {
        return laBai;
    }

    /**
     * Chia bài cho đử 4 người
     */
    public void chiaBai(){
        int temp;
        Random r=new Random();
        /**
         * test
         */
//        laBai=new int[][]{
//                {0,0,0,0},
//                {0,0,0,0},
//                {2,3,3,3},
//                {4,4,4,2},
//                {2,2,2,3},
//                {2,4,2,2},
//                {3,2,4,3},
//                {4,4,2,3},
//                {2,3,1,3},
//                {2,1,1,4},
//                {1,1,1,4},
//                {1,3,4,2},
//                {3,3,1,1},
//                {1,1,4,4},
//                {1,1,4,3},
//        };
        int lucky=-1;
        while (lucky!=1){
            int[] dem ={0,0,0,0};
            for(int i=2;i<15;i++){
                for(int j=0;j<4;j++){
                    temp=r.nextInt(4);
                    if(dem[temp]<13){
                        dem[temp]++;
                        this.laBai[i][j]=temp+1;
                    }else j--;
                }
                if(i==14&&laBai[i][0]==laBai[i][1]&&laBai[i][1]==laBai[i][2]&&laBai[i][2]==laBai[i][3])
                    lucky=r.nextInt(7);//Thay tỉ lệ đi
                else lucky=1;
            }
        }
        /**
         * deep copy
         */
        for(int i=0;i<15;i++){
            for(int j=0;j<4;j++){
                this.laBaiBanDau[i][j]=this.laBai[i][j];
            }
        }
    }
    /**
     * trả về một bộ gồm 52 nút lá bài và đặt chúng ở giữa bàn và xếp đặt sẵn đích đến của chúng (hoạt ảnh)
     * @return
     */
    public List<NutLaBai> danhSachNutLaBai(){
        NutLaBai[] list=new NutLaBai[52];
        int[] demLaNguoiChoi=new int[5];
        float rong=0,cao=0;
        for(int i=2;i<15;i++) {
            for (int j = 0; j < 4; j++) {
                LaBai laBaiTemp = com.gamebai.tienlenmiennam.thucthe.LaBai.timLaBaiTheoSoVaChat(i, j);
                NutLaBai temp=new NutLaBai(laBaiTemp,
                        (MainActivity.chieuRongManHinh - ThongSo.LaBai.getKichThuocLaBaiRong())/2+rong,
                        (MainActivity.chieuCaoManHinh- ThongSo.LaBai.getKichThuocLaBaiCao())/2+cao,
                        ChiSoSoHuu.timTheoSoHuu(this.laBai[i][j]));
                int chiSoTemp=temp.getChiSoSoHuu().getChiSo();
                if(chiSoTemp==ChiSoSoHuu.NGUOI_CHOI_1.getChiSo()){
                    temp.setDiemDich(new PointF(temp.getDiemDich().x+
                            ThongSo.LaBai.getKichThuocLaBaiRong()*demLaNguoiChoi[1],
                            temp.getDiemDich().y));
                }
                list[chiSoTemp-1+demLaNguoiChoi[chiSoTemp]*4]=temp;
                demLaNguoiChoi[chiSoTemp]++;
                rong-=0.3f;
                cao-=0.3f;
            }
        }
        List<NutLaBai> listTemp = new ArrayList<>(List.of(list));
        Collections.reverse(listTemp);
        return listTemp;
    }

    /**
     * trả về danh sách nút lá bài fake
     * @return
     */
    public List<NutLaBai> danhSachNutLaBaiFake(){
        laBai=new int[][]{
                {0,0,0,0},
                {0,0,0,0},
                {4,3,2,3},
                {4,1,1,1},
                {3,1,1,2},
                {3,2,1,4},
                {4,3,2,2},
                {2,2,1,1},
                {4,1,3,2},
                {2,4,1,4},
                {4,3,2,3},
                {4,4,1,3},
                {1,3,3,2},
                {1,3,3,2},
                {2,4,4,4},
        };
        NutLaBai[] list=new NutLaBai[52];
        int[] demLaNguoiChoi=new int[5];
        float rong=0,cao=0;
        for(int i=2;i<15;i++) {
            for (int j = 0; j < 4; j++) {
                LaBai laBaiTemp = com.gamebai.tienlenmiennam.thucthe.LaBai.timLaBaiTheoSoVaChat(i, j);
                NutLaBai temp=new NutLaBai(laBaiTemp,
                        (MainActivity.chieuRongManHinh - ThongSo.LaBai.getKichThuocLaBaiRong())/2+rong,
                        (MainActivity.chieuCaoManHinh- ThongSo.LaBai.getKichThuocLaBaiCao())/2+cao,
                        ChiSoSoHuu.timTheoSoHuu(this.laBai[i][j]));
                int chiSoTemp=temp.getChiSoSoHuu().getChiSo();
                if(chiSoTemp==ChiSoSoHuu.NGUOI_CHOI_1.getChiSo()){
                    temp.setDiemDich(new PointF(temp.getDiemDich().x+
                            ThongSo.LaBai.getKichThuocLaBaiRong()*demLaNguoiChoi[1],
                            temp.getDiemDich().y));
                }
                list[chiSoTemp-1+demLaNguoiChoi[chiSoTemp]*4]=temp;
                demLaNguoiChoi[chiSoTemp]++;
                rong-=0.3f;
                cao-=0.3f;
            }
        }
        List<NutLaBai> listTemp = new ArrayList<>(List.of(list));
        Collections.reverse(listTemp);
        return listTemp;
    }
}
