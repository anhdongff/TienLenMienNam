package com.gamebai.tienlenmiennam.hotro;

import android.os.Environment;

import com.gamebai.tienlenmiennam.thucthe.BoBai;

import java.io.File;
import java.io.FileWriter;

public class ThuThapDuLieu {
    public static void luuVaoFile(String tenFile, int[] trangThai,int[] ketQua) {
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),tenFile);
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            StringBuilder sb=new StringBuilder();
            for (int j : trangThai) {
                sb.append(j).append(",");
            }
            for (int i = 0; i < ketQua.length; i++) {
                sb.append(ketQua[i]);
                if(i!=ketQua.length-1) sb.append(",");
            }
            sb.append("\n");
            fileWriter.write(sb.toString());
            fileWriter.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void luuBoBaiLoi(String tenFile, BoBai boBai){
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),tenFile);
        StringBuilder sb=new StringBuilder();
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            for (int i = 0; i < 15; i++) {
                sb.append(i).append("{");
                for (int j = 0; j < 4; j++) {
                    if(j!=3) sb.append(boBai.laBaiBanDau[i][j]).append(",");
                    else sb.append(boBai.laBaiBanDau[i][j]);
                }
                sb.append("},\n");
            }
            sb.append("\n");
            fileWriter.write(sb.toString());
            fileWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
