package com.gamebai.tienlenmiennam.uisanhcho;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gamebai.tienlenmiennam.R;

public class BalatroCreateSupportRequestFragment extends BalatroDialogFragment{
    /**
     * lớp trừu tượng để lắng nghe sự kiện người dùng chọn hành động,
     * phải được implement (sửa các hàm trong đây) ở Activity hoặc Fragment sử dụng
     */
    public interface OnCreateSupportRequestActionSelectedListener {
        void onCreateRequest(String tieuDe,String noiDung, View v);
        void onCancel();
    }
    private OnCreateSupportRequestActionSelectedListener listener;
    public void setOnActionSelectedListener(OnCreateSupportRequestActionSelectedListener listener) {
        this.listener = listener;
    }
    public BalatroCreateSupportRequestFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balatro_create_request_support, container, false);

        BalatroEditText editTextTieuDe = view.findViewById(R.id.balatroEditTextTieuDe);
        BalatroEditText editTextNoiDung = view.findViewById(R.id.balatroEditTextContent);
        view.findViewById(R.id.btnQuayLai).setOnClickListener(v -> {
            if (listener != null) listener.onCancel();
        });

        view.findViewById(R.id.btnThemMoi).setOnClickListener(v -> {
            if (listener != null && editTextTieuDe != null&& editTextNoiDung != null) {
                String tieuDe = editTextTieuDe.getText() != null ? editTextTieuDe.getText().toString().trim() : "";
                String noiDung = editTextNoiDung.getText() != null ? editTextNoiDung.getText().toString().trim() : "";
                listener.onCreateRequest(tieuDe,noiDung, v);
            }
        });

        return view;
    }
}
