package com.gamebai.tienlenmiennam.thucthe;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * lưu các tay bài được người chơi đánh ra và cung cấp các phương thức kiểm tra chúng có hợp lệ hay không và xác định tay bài được đánh
 * @author Admin
 */
public class KiemTraTayBai {
    public ArrayList<LaBai> laBai;
    public KiemTraTayBai(){
        laBai=new ArrayList<>();
    }
    /**
     * sắp xếp lá bài
     */
    public void sort(){
        this.laBai.sort(Comparator.comparing(LaBai::getSo).thenComparing(LaBai::getChat));
    }
    public boolean checkBo(int doDai){
        boolean check=true;
        if(this.laBai.size()<3) return false;
        if(doDai!=0){
            if(this.laBai.size()!=doDai) check= false;
            else for(int i = 0; i<this.laBai.size()-1; i++){
                if(!this.laBai.get(i).noiNhau(this.laBai.get(i+1))){
                    check=false;break;
                }
            }
        }else{

            for(int i = 0; i<this.laBai.size()-1; i++){
                if(!this.laBai.get(i).noiNhau(this.laBai.get(i+1))){
                    check=false;break;
                }
            }
        }
        return check;
    }
    public boolean checkLe(){
        return this.laBai.size()==1;
    }
    public boolean checkDoi(){
        return this.laBai.size()==2&&this.laBai.get(0).doi(this.laBai.get(1));
    }
    public boolean checkSap(){
        return this.laBai.size()==3&&this.laBai.get(0).doi(this.laBai.get(1))&&this.laBai.get(0).doi(this.laBai.get(2));
    }
    public boolean checkTuQuy(){
        return this.laBai.size()==4&&this.laBai.get(0).doi(this.laBai.get(1))&&this.laBai.get(0).doi(this.laBai.get(2))&&this.laBai.get(0).doi(this.laBai.get(3));
    }
    public boolean checkThong(int doDai){
        boolean check=true;
        if(this.laBai.size()<6||this.laBai.size()%2==1) return false;
        if(doDai!=0){
            if(this.laBai.size()<doDai*2) return false;
            for(int i = 0; i<this.laBai.size(); i+=2){
                if(!this.laBai.get(i).doi(this.laBai.get(i+1))){
                    check=false;break;
                }
                if(i!=this.laBai.size()-2){
                    if(!this.laBai.get(i+1).noiNhau(this.laBai.get(i+2))){
                        check=false;break;
                    }
                }
            }
        }else{
            for(int i = 0; i<this.laBai.size(); i+=2){
                if(!this.laBai.get(i).doi(this.laBai.get(i+1))){
                    check=false;break;
                }
                if(i!=this.laBai.size()-2){
                    if(!this.laBai.get(i+1).noiNhau(this.laBai.get(i+2))){
                        check=false;break;
                    }
                }
            }
        }
        return check;
    }

    /**
     * kiểm tra tay bài có hợp lệ hay không
     * @param tayBai
     * @param doDai
     * @param laCuoi
     * @param chiChat2 người chơi có chỉ được chặt chứ không được đỡ đè 2
     * @return
     */
    public TayBai baiDanhHopLe(int tayBai,int doDai,LaBai laCuoi,boolean chiChat2){
        TayBai temp=TayBai.KhongHopLe;
        if(tayBai==TayBai.KhongCo.ordinal()){
            if(this.checkBo(0)) return TayBai.Bo;
            if(this.checkDoi()) return TayBai.Doi;
            if(this.checkLe()) return TayBai.Le;
            if(this.checkSap()) return TayBai.Sap;
            if(this.checkThong(0)) return TayBai.Thong;
            if(this.checkTuQuy()) return TayBai.TuQuy;
        }
        if(tayBai==TayBai.Bo.ordinal()){
            if(this.checkBo(doDai)) return TayBai.Bo;
        }
        if(tayBai==TayBai.Doi.ordinal()){
            if(laCuoi.getSo()==14){
                if(this.checkThong(doDai)) return TayBai.Thong;
                if(this.checkTuQuy()) return TayBai.TuQuy;
                if(!chiChat2&&this.checkDoi()) return TayBai.Doi;
            }else if(this.checkDoi()) return TayBai.Doi;
        }
        if(tayBai==TayBai.Le.ordinal()){
            if(laCuoi.getSo()==14){
                if(this.checkThong(doDai)) return TayBai.Thong;
                if(this.checkTuQuy()) return TayBai.TuQuy;
                if(!chiChat2&&this.checkLe()) return TayBai.Le;
            }else if(this.checkLe()) return TayBai.Le;
        }
        if(tayBai==TayBai.Sap.ordinal()){
            if(this.checkSap()) return TayBai.Sap;
        }
        if(tayBai==TayBai.Thong.ordinal()){
            if(doDai==3){
                if(this.checkThong(doDai)){
                    if(laBai.get(laBai.size()-1).lonHon(laCuoi)) return TayBai.Thong;
                    else return TayBai.KhongHopLe;
                }
                if(this.checkTuQuy()) return TayBai.TuQuy;
            }
            else if(this.checkThong(doDai)) return TayBai.Thong;
        }
        if(tayBai==TayBai.TuQuy.ordinal()){
            if(this.checkThong(doDai)) return TayBai.Thong;
            if(this.checkTuQuy()) return TayBai.TuQuy;
        }
        return temp;
    }
}
