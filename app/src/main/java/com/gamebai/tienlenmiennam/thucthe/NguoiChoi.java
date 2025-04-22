package com.gamebai.tienlenmiennam.thucthe;

import java.util.ArrayList;

public class NguoiChoi {
    private boolean active;
    /**
     * thông tin người chơi
     */
    private String uid;
    private String email;
    private String tenDangNhap;
    private String idGame;
    private long tien;
    /**
     * firestore field's name
     */
    public static String TEN_COLLECTION="TaiKhoan";
    public static String TEN_TRUONG_EMAIL="Email";
    public static String TEN_TRUONG_TEN_DANG_NHAP="TenDangNhap";
    public static String TEN_TRUONG_ID_GAME="IdGame";
    public static String TEN_TRUONG_TIEN="Tien";
    public static String TEN_TRUONG_LAN_CUOI_DANG_NHAP="LanCuoiDangNhap";
    public static String TEN_TRUONG_LAN_CUOI_NHAN_QUA="LanCuoiNhanQua";
    /**
     * realtime field's name
     */
    public static String TEN_BANG_TRONG_TRAN="NguoiChoi";
    public static String TEN_TRUONG_BO_LUOT="BoLuot";
    public static String TEN_TRUONG_SO_LA_BAI="SoLaBaiTrenTay";
    public static String TEN_TRUONG_SO_THU_TU="SoThuTu";
    public static String TEN_TRUONG_TEN="Ten";
    public static String TEN_TRUONG_BAI_TREN_TAY="BaiTrenTay";
    public static String TEN_TRUONG_TRANG_THAI_ONLINE="TrangThai";
    public static String TEN_TRUONG_DANH_DAU_TIEN="DanhDauTien";
    public static String TEN_TRUONG_SAN_SANG="SanSang";
    public static String TEN_TRUONG_CHUOI_CHAT_2="ChiChat2";
    /**
     * thông tin trong trận đấu
     */
    private ArrayList<LaBai> trenTay =new ArrayList<>();
    public boolean sanSang;
    public boolean boLuot;
    public boolean chiChat2;
    public boolean anhLaKeMayMan;//Tứ quý 2
    public int thuNhap;
    public int soLaBai,soThuTu;
    public BoBai.ChiSoSoHuu chiSoSoHuu;
    ArrayList<LaBai> baiDanhCu;
    public int[] trangThaiGame;
    public int[] quyetDinh;
    public NguoiChoi(BoBai.ChiSoSoHuu chiSoSoHuu) {
        khoiTaoThongSoTranDau(chiSoSoHuu);
    }
    public NguoiChoi(){}
    private void khoiTaoThongSoTranDau(BoBai.ChiSoSoHuu chiSoSoHuu) {
        boLuot=false;
        chiChat2=false;
        anhLaKeMayMan=false;
        thuNhap=0;
        this.chiSoSoHuu=chiSoSoHuu;
        active=true;
        baiDanhCu=new ArrayList<>();
        trenTay=new ArrayList<>();
    }

    public NguoiChoi(String email, String tenDangNhap, String idGame, long tien) {
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.idGame = idGame;
        this.tien = tien;
        active=true;
        anhLaKeMayMan=false;
        trenTay=new ArrayList<>();
    }
    public void setChiSoSoHuu(BoBai.ChiSoSoHuu chiSoSoHuu) {
        this.chiSoSoHuu = chiSoSoHuu;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public void setTien(long tien) {
        this.tien = tien;
    }

    public String getEmail() {
        return email;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getIdGame() {
        return idGame;
    }

    public long getTien() {
        return tien;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * nhận bài từ bộ bài sau khi chia bài
     * @param a bộ bài
     */
    public void sauChiaBai(BoBai a) {
        trangThaiGame=new int[56];
        int dem=0;
        for (int i = 2; i < 15; i++) {
            for (int j = 0; j < 4; j++) {
                if (a.getSoHuu(i, j) == chiSoSoHuu.getChiSo()) {
                    LaBai temp = LaBai.timLaBaiTheoSoVaChat(i, j);
                    temp.setDuocChon(false);
                    temp.setCoTheChon(false);
                    this.trenTay.add(temp);
                    trangThaiGame[dem]=1;
                }
                dem++;
            }
        }
        trangThaiGame[55]=13;
        trangThaiGame[54]=13;
        trangThaiGame[53]=13;
        trangThaiGame[52]=13;
    }
    public int soLa(){return this.trenTay.size();}
    public LaBai getLaBai(int index){return this.trenTay.get(index);}
    public int getSo(int index){return this.trenTay.get(index).getSo();}
    public int getChat(int index){return this.trenTay.get(index).getChat();}

    public ArrayList<LaBai> getBaiTrenTay() {
        return trenTay;
    }

    public BoBai.ChiSoSoHuu getChiSoSoHuu() {
        return chiSoSoHuu;
    }

    public void setDuocChon(int index, boolean chon){this.trenTay.get(index).setDuocChon(chon);}
    /**
     * đặt trạng thái có thể chọn của lá bài trên tay theo chỉ số của chúng trên tay người chơi
     * @param index chỉ số lá đó trên tay người chơi
     * @param chon được chọn hay không được chọn
     */
    public void setCoTheChon(int index,boolean chon){this.trenTay.get(index).setCoTheChon(chon);}
    /**
     * đặt trạng thái có thể chọn của lá bài trên tay theo số và chất của chúng
     * (số và chất của chúng được quy định theo hàng và cột của ma trận bộ bài)
     * @param so số trên lá bài
     * @param chat chất của lá bài
     * @param chon được chọn hay không được chọn
     * @return chỉ số của lá đó trên tay người cầm nó
     */
    public int setCoTheChon(int so,int chat,boolean chon){
        int index=-1;
        for(int i=0;i<this.soLa();i++){
            if(this.getChat(i)==chat&&this.getSo(i)==so){
                this.setCoTheChon(i, chon);
                index=i;
                break;
            }
        }
        return index;
    }
    /**
     * lấy trạng thái được chọn của lá bài trên tay theo số và chất của chúng
     * (số và chất của chúng được quy định theo hàng và cột của ma trận bộ bài)
     * @param so số trên lá bài
     * @param chat chất của lá bài
     * @return có thể hay không thể chọn
     */
    public boolean isCoTheChon(int so,int chat){
        boolean temp=true;
        for(int i=0;i<this.soLa();i++){
            if(this.getChat(i)==chat&&this.getSo(i)==so){
                temp=this.isCoTheChon(i);
                break;
            }
        }
        return temp;
    }
    public boolean isCoTheChon(int index){return this.trenTay.get(index).isCoTheChon();}
    public boolean isDuocChon(int index){return this.trenTay.get(index).isDuocChon();}
    public void boLaBai(int i){this.trenTay.remove(i);}
    /**
     * tìm lá bài trên tay người chơi với điều kiện lá đó lớn hơn lá bài cần chặn và khiến chúng có thể chọn
     * @param canChan lá bài cần chặn
     * @param coTheChon có thể chọn hay không thể chọn
     * @return có hay không có lá bài phù hợp
     */
    public boolean timLa(LaBai canChan,boolean coTheChon){
        boolean timThay=false;
        for(int i=0;i<this.soLa();i++){
            if(this.trenTay.get(i).lonHon(canChan)){
                timThay=true;
                this.trenTay.get(i).setCoTheChon(coTheChon);
            }
            else this.trenTay.get(i).setCoTheChon(!coTheChon);
        }
        return timThay;
    }

    /**
     * tìm tất cả những lá chặn được lá bài cần chặn nhưng bị "lẻ" ra hay không thuộc các tay bài khác, trừ đôi
     * @param canChan lá cần chặn
     * @return false nếu không có lá nào thoả mãn
     */
    public boolean timLaLe(LaBai canChan,BoBai boBai){
        if(!timLa(canChan,true)) return false;
        int so=-1;
        int index=-1;
        for(int i=0;i<this.soLa();i++){
            if(trenTay.get(i).lonHon(canChan)) {
                so=trenTay.get(i).getSo();
                index=i;
                break;
            }
        }
        if(so==-1) return false;
        /**
         * bỏ chọn bộ
         */
        if(timBo(LaBai.getTempInstance(2, -1), 3, boBai, false)){
            for(int i=so;i<14;i++){
                boKhongThechonLaCungSo(i, boBai,1);
            }
        }
        /**
         * bỏ chọn đôi thông
         */
        if(timDoiThong(LaBai.getTempInstance(2, -1), 3, boBai, false)){
            for(int i=so;i<14;i++){
                boKhongThechonLaCungSo(i, boBai,2);
            }
        }
        timTuQuy(LaBai.getTempInstance(2, -1), false);
        timSap(LaBai.getTempInstance(2, -1), false);
        for(int i=index;i<this.soLa();i++){
            if(this.trenTay.get(i).isCoTheChon()) return true;
        }
        return false;
    }
    /**
     * tìm tất cả đôi trên tay người chơi với điều kiện đôi đó lớn đôi cần chặn khiến chúng có thể chọn
     * @param canChan lá bài lớn nhất trong tay bài cần chặn
     * @param coTheChon có thể chọn hay không được chọn
     * @return có hay không có tay bài phù hợp
     */
    public boolean timDoi(LaBai canChan,boolean coTheChon){
        if(this.soLa()<2) return false;
        boolean timThay=false;
        for(int i=0;i<this.soLa()-1;i++){
            if(this.trenTay.get(i).doi(this.trenTay.get(i+1))&&this.getLaBai(i+1).lonHon(canChan)){
                this.getLaBai(i).setCoTheChon(coTheChon);
                this.getLaBai(i+1).setCoTheChon(coTheChon);
                timThay=true;
            }
        }
        return timThay;
    }
    /**
     * tìm tất cả đôi trên tay người chơi với điều kiện không phải đôi 2 đôi đó lớn đôi cần chặn khiến chúng có thể chọn
     * @param canChan lá bài lớn nhất trong tay bài cần chặn
     * @param coTheChon có thể chọn hay không được chọn
     * @return có hay không có tay bài phù hợp
     */
    public boolean timDoiKhac2(LaBai canChan,boolean coTheChon){
        if(this.soLa()<2) return false;
        boolean timThay=false;
        for(int i=0;i<this.soLa()-1;i++){
            if(this.trenTay.get(i).doi(this.trenTay.get(i+1))&&this.getLaBai(i+1).lonHon(canChan)&&this.getSo(i)!=14){
                this.getLaBai(i).setCoTheChon(coTheChon);
                this.getLaBai(i+1).setCoTheChon(coTheChon);
                timThay=true;
            }
        }
        return timThay;
    }
    /**
     * tìm tất cả bộ có độ dài >= độ dài bộ đã cho và lớn hơn bộ đã cho
     * @param canChan lá bài lớn nhất trong tay bài cần chặn
     * @param doDaiBo độ dài của tay bài
     * @param boBai bộ bài của trận
     * @param coTheChon có thể chọn hay không được chọn
     * @return true nếu trên tay người chơi tay bài phù hợp để chặn
     */
    public boolean timBo(LaBai canChan,int doDaiBo,BoBai boBai,boolean coTheChon){
        if(this.soLa()<doDaiBo) return false;
        ArrayList<LaBai> laDuocChon=new ArrayList<>();
        boolean timThay=false;
        boolean coBo=false;
        for(int i=canChan.getSo();i<14;i++){
            if(i==canChan.getSo()){
                for(int j=3;j>canChan.getChat();j--){
                    if(boBai.getSoHuu(i, j)==chiSoSoHuu.getChiSo()){
                        laDuocChon.add(LaBai.timLaBaiTheoSoVaChat(i, j));
                        timThay=true;
                    }
                }
                if(timThay){
                    for(int h=i-1;h>i-doDaiBo;h--){
                        timThay=false;
                        for(int k=0;k<4;k++){
                            if(boBai.getSoHuu(h, k)==chiSoSoHuu.getChiSo()){
                                laDuocChon.add(LaBai.timLaBaiTheoSoVaChat(h, k));
                                timThay=true;
                            }
                        }
                        if(!timThay){
                            laDuocChon.clear();
                            break;
                        }
                    }
                    if(timThay){
                        for(LaBai x:laDuocChon){
                            setCoTheChon(x.getSo(), x.getChat(), coTheChon);
                        }
                        laDuocChon.clear();
                        coBo=true;
                    }
                }
            }else if(timThay){
                timThay=false;
                for(int j=0;j<4;j++){
                    if(boBai.getSoHuu(i, j)==chiSoSoHuu.getChiSo()){
                        this.setCoTheChon(i, j, coTheChon);
                        timThay=true;
                    }
                }
                if(!timThay){
                    if(13-i>=doDaiBo) i+=doDaiBo-1;
                }
            }else{
                for(int h=i;h>i-doDaiBo;h--){
                    timThay=false;
                    for(int k=0;k<4;k++){
                        if(boBai.getSoHuu(h, k)==chiSoSoHuu.getChiSo()){
                            laDuocChon.add(LaBai.timLaBaiTheoSoVaChat(h, k));
                            timThay=true;
                        }
                    }
                    if(!timThay){
                        laDuocChon.clear();
                        break;
                    }
                }
                if(timThay){
                    for(LaBai x:laDuocChon){
                        setCoTheChon(x.getSo(), x.getChat(), coTheChon);
                    }
                    laDuocChon.clear();
                    coBo=true;
                }
            }
        }
        return coBo;
    }
    /**
     * chỉ một số lá cùng số trên tay có thể chọn cùng lúc
     * @param i giá trị số của lá bài (theo giá trị cột của ma trận bộ bài) (>=1)
     * @param boBai bộ bài của trò chơi
     * @param soLaDuocChon số các lá cùng số có thể chọn cùng lúc
     * @return false nếu số lá có thể chọn cùng lúc < soLaDuocChon
     */
    public boolean boChonLaCungSo(int i,BoBai boBai,int soLaDuocChon){
        boolean co=false;
        int dem=0;
        for(int j=3;j>=0;j--){
            if(boBai.getSoHuu(i, j)==chiSoSoHuu.getChiSo()){
                if(this.isCoTheChon(i,j)){
                    if(!co){
                        dem++;
                        if(dem>=soLaDuocChon) co=true;
                    }
                    else this.setCoTheChon(i, j, false);
                }
            }
        }
        return co;
    }
    /**
     * chỉ một số lá cùng số trên tay có không thể chọn cùng lúc
     * @param i giá trị số của lá bài (theo giá trị cột của ma trận bộ bài) (>=1)
     * @param boBai bộ bài của trò chơi
     * @param soLaKhongTheChon số các lá cùng số không thể chọn cùng lúc
     * @return false nếu số lá có thể không thể chọn cùng lúc < soLaKhongTheChon
     */
    public boolean boKhongThechonLaCungSo(int i, BoBai boBai, int soLaKhongTheChon){
        boolean co=false;
        int dem=0;
        for(int j=3;j>=0;j--){
            if(boBai.getSoHuu(i, j)==chiSoSoHuu.getChiSo()){
                if(!this.isCoTheChon(i,j)){
                    if(!co){
                        dem++;
                        if(dem>=soLaKhongTheChon) co=true;
                    }
                    else this.setCoTheChon(i, j, true);
                }
            }
        }
        return co;
    }
    /**
     * tìm bộ dài nhất trong bài của người chơi và mỗi số chỉ có thể chọn 1 lá (phục vụ máy)
     * @param boBai bộ bài của trò chơi
     * @return có hay không có tay bài phù hợp
     */
    public boolean chonBoDaiNhat(BoBai boBai){
        if(!this.timBo(LaBai.getTempInstance(2,-1), 3, boBai,true)) return false;
        LaBai laDau=LaBai.getTempInstance();int daiNhat=2;
        int soLaBaiTemp=0;int doDai=0;
        for(int i=2;i<14;i++){
            boolean co=this.boChonLaCungSo(i, boBai,1);
            if(co){
                if(doDai==0) {
                    soLaBaiTemp=i;
                    doDai++;
                }else doDai++;
                if(i==13){//Trường hợp vòng cuối
                    if(doDai>daiNhat) {
                        daiNhat=doDai;
                        laDau.setSo(soLaBaiTemp);
                    }
                    soLaBaiTemp=0;
                    doDai=0;
                }
            }else{
                if(doDai!=0){
                    if(doDai>daiNhat) {
                        daiNhat=doDai;
                        laDau.setSo(soLaBaiTemp);
                    }
                    soLaBaiTemp=0;
                    doDai=0;
                }
            }
        }/*System.out.print(ladau.getSo()+"/"+dainhat);*/
        for(int i=0;i<this.soLa();i++){
            if(this.isCoTheChon(i)&&(this.getSo(i)<laDau.getSo()||this.getSo(i)>=laDau.getSo()+daiNhat))
                this.setCoTheChon(i, false);
        }
        return true;
    }
    /**
     * tìm bộ nhỏ nhất có thể chặn tay bài cần chặn và mỗi số chỉ có thể chọn 1 lá
     * @param canChan lá lớn nhất trong tay bài cần chặn
     * @param doDai độ dài của bộ
     * @param boBai bộ bài của trò chơi
     * @return có hay không có tay bài phù hợp
     */
    public boolean timBoNhoNhat(LaBai canChan,int doDai,BoBai boBai){
        if(!this.timBo(canChan, doDai, boBai,true)) return false;
        for(int i=2;i<14;i++) this.boChonLaCungSo(i, boBai,1);
        return true;
    }
    /**
     * tìm các lá bài có thể tạo thành đôi thông trên tay người chơi và đặt chúng có thể chọn
     * @param canChan: lá lớn nhất trong tay bài cần chặn
     * @param doDaiBo: độ dài của đôi thông (số lá /2)
     * @param boBai: bộ bài của trận
     * @param coTheChon: có thể chọn hay không thể chọn
     * @return người chơi có hay không có đôi thông
     */
    public boolean timDoiThong(LaBai canChan,int doDaiBo,BoBai boBai,boolean coTheChon){
        if(this.soLa()<doDaiBo*2) return false;
        ArrayList<LaBai> laDuocChon=new ArrayList<>();
        boolean timThay=false;
        boolean coBo=false;
        for(int i=canChan.getSo();i<14;i++){
            int dem=0;
            if(i==canChan.getSo()){
                for(int j=3;j>canChan.getChat();j--){
                    if(boBai.getSoHuu(i, j)==chiSoSoHuu.getChiSo()){
                        laDuocChon.add(LaBai.timLaBaiTheoSoVaChat(i, j));
                        dem++;
                    }
                    if(dem>=2) timThay=true;
                }
                if(timThay){
                    for(int h=i-1;h>i-doDaiBo;h--){
                        dem=0;
                        timThay=false;
                        for(int k=0;k<4;k++){
                            if(boBai.getSoHuu(h, k)==chiSoSoHuu.getChiSo()){
                                laDuocChon.add(LaBai.timLaBaiTheoSoVaChat(h, k));
                                dem++;
                            }
                        }
                        if(dem>=2) timThay=true;
                        if(!timThay){
                            laDuocChon.clear();
                            break;
                        }
                    }
                    if(timThay){
                        for(LaBai x: laDuocChon){
                            setCoTheChon(x.getSo(), x.getChat(), coTheChon);
                        }
                        laDuocChon.clear();
                        coBo=true;
                    }
                }
            }else if(timThay){
                timThay=false;
                dem=0;
                for(int j=0;j<4;j++){
                    if(boBai.getSoHuu(i, j)==chiSoSoHuu.getChiSo()){
                        laDuocChon.add(LaBai.timLaBaiTheoSoVaChat(i, j));
                        dem++;
                    }
                }
                if(dem>=2){
                    timThay=true;
                    for(LaBai x: laDuocChon){
                        setCoTheChon(x.getSo(), x.getChat(), coTheChon);
                    }
                    laDuocChon.clear();
                }
                if(!timThay){
                    laDuocChon.clear();
                    if(13-i>=doDaiBo) i+=doDaiBo-1;
                }
            }else{
                for(int h=i;h>i-doDaiBo;h--){
                    timThay=false;
                    dem=0;
                    for(int k=0;k<4;k++){
                        if(boBai.getSoHuu(h, k)==chiSoSoHuu.getChiSo()){
                            laDuocChon.add(LaBai.timLaBaiTheoSoVaChat(h, k));
                            dem++;
                        }
                        if(dem>=2){
                            timThay=true;
                        }
                    }
                    if(!timThay){
                        laDuocChon.clear();
                        break;
                    }
                }
                if(timThay){
                    for(LaBai x: laDuocChon){
                        setCoTheChon(x.getSo(), x.getChat(), coTheChon);
                    }
                    laDuocChon.clear();
                    coBo=true;
                }
            }
        }
        return coBo;
    }
    /**
     * tìm đôi thông nhỏ nhất có thể chặn tay bài cần chặn và mỗi số chỉ có thể chọn 2 lá
     * @param canChan lá lớn nhất trong tay bài cần chặn
     * @param doDai độ dài của bộ
     * @param boBai bộ bài của trò chơi
     * @return có hay không có tay bài phù hợp
     */
    public boolean timDoiThongNhoNhat(LaBai canChan,int doDai,BoBai boBai){
        if(!this.timDoiThong(canChan, doDai, boBai,true)) return false;
        for(int i=2;i<14;i++) this.boChonLaCungSo(i, boBai,2);
        return true;
    }
    /**
     * tìm các sáp lớn hơn tay bài cần chặn và đặt chúng có thể chọn
     * @param canChan lá lớn nhất trong tay bài cần chặn
     * @param coTheChon có thể chọn hay không được chọn
     * @return có hay không có tay bài phù hợp
     */
    public boolean timSap(LaBai canChan,boolean coTheChon){
        if(this.soLa()<3) return false;
        boolean timThay=false;
        for(int i=0;i<this.soLa()-2;i++){
            if(this.trenTay.get(i).doi(this.trenTay.get(i+1))&&this.trenTay.get(i).doi(this.trenTay.get(i+2))&&this.getLaBai(i+1).lonHon(canChan)){
                this.getLaBai(i).setCoTheChon(coTheChon);
                this.getLaBai(i+1).setCoTheChon(coTheChon);
                this.getLaBai(i+2).setCoTheChon(coTheChon);
                timThay=true;
            }
        }
        return timThay;
    }
    /**
     * tìm các sáp không chứa lá 2 lớn hơn tay bài cần chặn và đặt chúng có thể chọn
     * @param canChan lá lớn nhất trong tay bài cần chặn
     * @param coTheChon có thể chọn hay không thể chọn
     * @return có hay không có tay bài phù hợp
     */
    public boolean timSapKhac2(LaBai canChan,boolean coTheChon){
        if(this.soLa()<3) return false;
        boolean timThay=false;
        for(int i=0;i<this.soLa()-2;i++){
            if(this.trenTay.get(i).doi(this.trenTay.get(i+1))&&this.trenTay.get(i).doi(this.trenTay.get(i+2))&&this.getLaBai(i+1).lonHon(canChan)&&this.getSo(i)!=14){
                this.getLaBai(i).setCoTheChon(coTheChon);
                this.getLaBai(i+1).setCoTheChon(coTheChon);
                this.getLaBai(i+2).setCoTheChon(coTheChon);
                timThay=true;
            }
        }
        return timThay;
    }
    /**
     * tìm các tứ quý lớn hơn tay bài cần chặn và đặt chúng có thể chọn
     * @param canChan lá lớn nhất trong tay bài cần chặn
     * @param coTheChon có thể chọn hay không thể chọn
     * @return có hay không có tay bài phù hợp
     */
    public boolean timTuQuy(LaBai canChan,boolean coTheChon){
        if(this.soLa()<4) return false;
        boolean timThay=false;
        for(int i=0;i<this.soLa()-3;i++){
            if(this.trenTay.get(i).doi(this.trenTay.get(i+1))&&this.trenTay.get(i).doi(this.trenTay.get(i+2))&&this.trenTay.get(i).doi(this.trenTay.get(i+3))&&this.getLaBai(i+1).lonHon(canChan)){
                this.getLaBai(i).setCoTheChon(coTheChon);
                this.getLaBai(i+1).setCoTheChon(coTheChon);
                this.getLaBai(i+2).setCoTheChon(coTheChon);
                this.getLaBai(i+3).setCoTheChon(coTheChon);
                timThay=true;
            }
        }
        return timThay;
    }
    /**
     * tìm các lá bài có thể chọn trên tay người chơi
     * @param canChan lá lớn nhất trong tay bài cần chặn
     * @param tayBai loại tay bài cần chặn
     * @param doDai độ dài tay bài (với đôi thông là số lá trong tay bài /2)
     * @param boBai bộ bài của trò chơi
     * @return có hay không có tay bài phù hợp
     */
    public boolean timCoTheChon(LaBai canChan,int tayBai,int doDai,BoBai boBai){
        boolean check=false;
        if(tayBai==TayBai.KhongCo.ordinal()){
            for(LaBai temp:this.trenTay) temp.setCoTheChon(true);
            check=true;
        }
        if(tayBai==TayBai.Le.ordinal()){
            if(canChan.getSo()==14){
                boolean a=false;
                if(!chiChat2) a=this.timLa(canChan,true);
                boolean b=this.timDoiThong(LaBai.getTempInstance(2,-1), 3, boBai, true);
                boolean c=this.timTuQuy(LaBai.getTempInstance(2,-1), true);
                if(a||b||c) check=true;
            }else{
                if(this.timLa(canChan,true)) check=true;
            }
        }
        if(tayBai==TayBai.Doi.ordinal()){
            if(canChan.getSo()==14){
                boolean a=false;
                if(!chiChat2) a=this.timDoi(canChan, true);
                boolean b=this.timDoiThong(LaBai.getTempInstance(2,-1), 4, boBai, true);
                if(a||b) check=true;
            }else{
                if(this.timDoi(canChan, true)) check=true;
            }
        }
        if(tayBai==TayBai.Sap.ordinal()){
            if(this.timSap(canChan, true)) check=true;
        }
        if(tayBai==TayBai.TuQuy.ordinal()){
            boolean a=this.timTuQuy(canChan, true);
            boolean b=this.timDoiThong(LaBai.getTempInstance(2,-1), 4, boBai, true);
            if(a||b) check=true;
        }
        if(tayBai==TayBai.Bo.ordinal()){
            if(this.timBo(canChan, doDai, boBai, true)) check=true;
        }
        if(tayBai==TayBai.Thong.ordinal()){
            if(doDai==3){
                boolean b,c;
                boolean a=this.timDoiThong(LaBai.getTempInstance(2,-1), doDai+1, boBai, true);
                b=this.timDoiThong(canChan, doDai, boBai, true);
                c=this.timTuQuy(LaBai.getTempInstance(2,-1), true);
                if(a||b||c) check=true;
            }else{
                boolean a=this.timDoiThong(LaBai.getTempInstance(2,-1), doDai+1, boBai,true);
                boolean b=this.timDoiThong(canChan, doDai, boBai, true);
                if(a||b) check=true;
            }
        }
        return check;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    /**
     * lưu lại các lá bài được chọn để đánh
     */
    public void luuLaiQuyetDinh(){
        quyetDinh=new int[13];
        for(LaBai x:trenTay){
            if(x.isDuocChon()){
                quyetDinh[trenTay.indexOf(x)]=1;
            }
        }
    }

    /**
     * thay đổi vector trạng thái của các lá bài và số lá bài của người chơi
     * @param moiDanh danh sách các lá bài mới được đánh đánh
     * @param viTri vị trí người chơi thay đổi số lá bài trên tay
     *              (nếu người đó là người chơi thứ 3 kể từ người chơi này theo vòng đánh bài thì vị trí là 3)
     */
    public void thayDoiTrangThai(ArrayList<LaBai> moiDanh,int viTri){
        for(LaBai x:baiDanhCu){
            trangThaiGame[(x.getSo()-2)*4+x.getChat()]=-1;
        }
        baiDanhCu.clear();
        for(LaBai x:moiDanh){
             trangThaiGame[(x.getSo()-2)*4+x.getChat()]=-2;
        }
        trangThaiGame[52+viTri]-=moiDanh.size();
        baiDanhCu.addAll(moiDanh);
    }
}