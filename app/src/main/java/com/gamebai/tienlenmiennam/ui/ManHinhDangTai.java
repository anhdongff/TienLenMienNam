package com.gamebai.tienlenmiennam.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gamebai.tienlenmiennam.R;

/**
 * tạo màn hình đang tải
 */
public class ManHinhDangTai {
    private AlertDialog dialog;
    private Context context;
    private String noiDung;
    private boolean isDisplayed;
    public ManHinhDangTai(Context context,String noiDung) {
        this.context=context;
        this.noiDung=noiDung;
        isDisplayed =false;
    }
    public void hienThi(){
        if(isDisplayed){
            return;
        }
        isDisplayed =true;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dang_tai,null);
        builder.setView(view);
        TextView textViewDangTai=view.findViewById(R.id.textViewDangTai);
        if(noiDung!=null&&!noiDung.isEmpty()){
            textViewDangTai.setText(noiDung);
        }
        dialog=builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public void hienThi(String noiDung){
        if(isDisplayed){
            return;
        }
        isDisplayed =true;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dang_tai,null);
        builder.setView(view);
        TextView textViewDangTai=view.findViewById(R.id.textViewDangTai);
        if(noiDung!=null&&!noiDung.isEmpty()){
            textViewDangTai.setText(noiDung);
        }
        dialog=builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public void an(){
        if(dialog!=null&&dialog.isShowing()&&isDisplayed){
            isDisplayed =false;
            dialog.dismiss();
        }
    }
    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getNoiDung() {
        return noiDung;
    }
}
