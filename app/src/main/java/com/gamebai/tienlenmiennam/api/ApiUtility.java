package com.gamebai.tienlenmiennam.api;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtility {
    // Bạn có thể đặt hàm này ở đâu đó tiện lợi, ví dụ như cuối file MainActivity
    // hoặc trong một lớp tiện ích (utility class)
    public static  <T> Task<T> wrapRetrofitCall(final Call<T> call) {
        // 1. Tạo một TaskCompletionSource. Nó sẽ quản lý Task của chúng ta.
        final TaskCompletionSource<T> tcs = new TaskCompletionSource<>();

        // 2. Thực hiện lệnh gọi API bất đồng bộ
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 3. Nếu thành công, báo cho Task biết kết quả
                    tcs.setResult(response.body());
                } else {
                    // Nếu server trả về lỗi (ví dụ 404, 500), báo cho Task biết là đã thất bại
                    tcs.setException(new Exception("API call failed with code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                // 4. Nếu có lỗi mạng hoặc lỗi khác, báo cho Task biết là đã thất bại
                tcs.setException(new Exception(t));
            }
        });

        // 5. Trả về đối tượng Task mà TaskCompletionSource đang quản lý.
        // Task này sẽ ở trạng thái "đang chạy" cho đến khi setResult hoặc setException được gọi.
        return tcs.getTask();
    }

}
