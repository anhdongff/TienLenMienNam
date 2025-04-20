package com.gamebai.tienlenmiennam.aingu;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Model4096 {
    private Interpreter tfliteInterpreter;
    public static final int DO_DAI_DU_LIEU_DAU_VAO=56;
    public static final int DO_DAI_DU_LIEU_RA=13;
    public static final int MIN_GIA_TRI_DAU_VAO=-2;
    public static final int MAX_GIA_TRI_DAU_VAO=13;
    public static final int MIN_GIA_TRI_RA=0;
    public static final int MAX_GIA_TRI_RA=1;
    public Model4096(Context context) {
        try {
            // Tải mô hình tflite từ assets
            Interpreter.Options options = new Interpreter.Options();
            options.setNumThreads(4);  // Cấu hình số lượng luồng (có thể tùy chỉnh)
            tfliteInterpreter = new Interpreter(loadModelFile(context), options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Đọc mô hình từ file
     * @param context
     * @return
     * @throws IOException
     */
    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("model_4096.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /**
     * Hàm dự đoán
     * @param inputData mảng trạng thái trò chơi với các phần tử đã được chuẩn hoá giá trị
     * @return
     */
    public float[] duDoan(float[] inputData) {
        float[][] outputData = new float[1][13];  // 13 là kích thước đầu ra của mô hình
        tfliteInterpreter.run(inputData, outputData);
        return outputData[0];
    }

    /**
     * Chuẩn hóa dữ liệu đầu vào về khoảng [0, 1]
     * @param inputData
     * @return
     */
    public static float[] chuanHoaDuLieu(int[] inputData){
        float[] normalizedData = new float[inputData.length];
        for(int i=0;i<inputData.length;i++){
            normalizedData[i]= (float) (inputData[i] - MIN_GIA_TRI_DAU_VAO) /
                    (MAX_GIA_TRI_DAU_VAO-MIN_GIA_TRI_DAU_VAO);
        }
        return normalizedData;
    }
}
