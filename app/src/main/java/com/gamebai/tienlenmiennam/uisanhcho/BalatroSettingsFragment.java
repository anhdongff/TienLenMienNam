package com.gamebai.tienlenmiennam.uisanhcho;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.hotro.SoundManager;

/**
 * Fragment hiển thị giao diện cài đặt với các nút chức năng.
 * Sử dụng layout balatro_setting_dialog.xml.
 * Khi người dùng bấm nút đỏ, callback onActionSelected sẽ được gọi.
 */
public class BalatroSettingsFragment extends BalatroDialogFragment {
    /**
     * các hành động có thể chọn trong cài đặt
     */
    public enum Action {
        AM_THANH, AM_NHAC, MA_QUA, HO_TRO, DANG_XUAT
    }
    /**
     * lớp trừu tượng để lắng nghe sự kiện người dùng chọn hành động,
     * phải được implement (sửa các hàm trong đây) ở Activity hoặc Fragment sử dụng
     */
    public interface OnSettingActionSelectedListener {
        void onActionSelected(Action action);
    }

    private OnSettingActionSelectedListener listener;
    private SoundManager soundManager=SoundManager.getInstance(getContext());

    public BalatroSettingsFragment() {
        // Bắt buộc phải có constructor rỗng
    }

    public void setOnActionSelectedListener(OnSettingActionSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balatro_setting_dialog, container, false);
        BalatroButton amThanhButton = view.findViewById(R.id.AmThanhButton);
        BalatroButton amNhacButton = view.findViewById(R.id.AmNhacButton);
        amThanhButton.setText("Âm thanh: "+(soundManager.isSfxEnabled() ? "Bật" : "Tắt"));
        amNhacButton.setText("Âm nhạc: "+(soundManager.isMusicEnabled() ? "Bật" : "Tắt"));
        amThanhButton.setOnClickListener(v -> {
            if (listener != null) listener.onActionSelected(Action.AM_THANH);
            amThanhButton.setText("Âm thanh: "+(soundManager.isSfxEnabled() ? "Bật" : "Tắt"));
        });
        amNhacButton.setOnClickListener(v -> {
            if (listener != null) listener.onActionSelected(Action.AM_NHAC);
            amNhacButton.setText("Âm nhạc: "+(soundManager.isMusicEnabled() ? "Bật" : "Tắt"));
        });
        view.findViewById(R.id.MaQuaButton).setOnClickListener(v -> {
            if (listener != null) listener.onActionSelected(Action.MA_QUA);
        });
        view.findViewById(R.id.HoTroButton).setOnClickListener(v -> {
            if (listener != null) listener.onActionSelected(Action.HO_TRO);
        });
        view.findViewById(R.id.DangXuatButton).setOnClickListener(v -> {
            if (listener != null) listener.onActionSelected(Action.DANG_XUAT);
        });
        view.findViewById(R.id.QuayLaiButton).setOnClickListener(v -> dismiss());

        return view;
    }
}

