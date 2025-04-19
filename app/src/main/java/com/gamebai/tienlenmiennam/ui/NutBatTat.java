package com.gamebai.tienlenmiennam.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class NutBatTat extends Nut{
    Bitmap anhNutFalse;
    public NutBatTat(Bitmap anhNutTrues, Bitmap anhNutFalses, float viTriX, float viTriY, float rong, float cao) {
        super(anhNutTrues, viTriX, viTriY, rong, cao);
        anhNutFalse=anhNutFalses;
    }
    @Override
    public Bitmap getAnhNut() {
        if(duocBam) return anhNut;
        else return anhNutFalse;
    }
    @Override
    public void ve(Canvas canvas){
        canvas.drawBitmap(getAnhNut(),hitbox.left,hitbox.top,null);
    }
    @Override
    public void ve(Canvas canvas, Paint paint){
        canvas.drawBitmap(getAnhNut(),hitbox.left,hitbox.top,paint);
    }
}
