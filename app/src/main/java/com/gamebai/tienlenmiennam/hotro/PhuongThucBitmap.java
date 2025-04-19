package com.gamebai.tienlenmiennam.hotro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * giao diện cung cấp sẵn các phương thức để làm việc với bitmap
 */
public interface PhuongThucBitmap {
    BitmapFactory.Options options=new BitmapFactory.Options();

    /**
     * lấy bitmap theo tỉ lệ
     * @param bitmap
     * @param tiLeRong tỉ lệ theo chiều rộng
     * @param tiLeCao tỉ lệ theo chiều cao
     * @param chatluong
     * @return
     */
    default Bitmap getBitmapTheoTiLe(Bitmap bitmap,float tiLeRong, float tiLeCao, boolean chatluong) {
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*tiLeRong), (int) (bitmap.getHeight()*tiLeCao),chatluong);
    }
}
