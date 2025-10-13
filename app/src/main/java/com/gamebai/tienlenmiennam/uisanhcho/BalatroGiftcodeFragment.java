package com.gamebai.tienlenmiennam.uisanhcho;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gamebai.tienlenmiennam.R;

/**
 * Fragment hiển thị giao diện nhập mã giftcode với các nút chức năng.
 * Sử dụng layout balatro_gitfcode_dialog.xml.
 */
public class BalatroGiftcodeFragment extends BalatroDialogFragment {
    /**
     * lớp trừu tượng để lắng nghe sự kiện người dùng chọn hành động,
     * phải được implement (sửa các hàm trong đây) ở Activity hoặc Fragment sử dụng
     */
    public interface OnGiftcodeActionSelectedListener {
        void onUseGiftcode(String giftcode, View v);
        void onCancel();
    }
    private OnGiftcodeActionSelectedListener listener;
    public void setOnActionSelectedListener(OnGiftcodeActionSelectedListener listener) {
        this.listener = listener;
    }
    public BalatroGiftcodeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balatro_gitfcode_dialog, container, false);

        BalatroEditText editText = view.findViewById(R.id.balatroEditText);
        view.findViewById(R.id.btnQuayLai).setOnClickListener(v -> {
            if (listener != null) listener.onCancel();
        });

        view.findViewById(R.id.btnSuDung).setOnClickListener(v -> {
            if (listener != null && editText != null) {
                String code = editText.getText() != null ? editText.getText().toString().trim() : "";
                listener.onUseGiftcode(code, v);
            }
        });

        return view;
    }
}
