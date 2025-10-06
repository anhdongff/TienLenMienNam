package com.gamebai.tienlenmiennam.uisanhcho;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.gamebai.tienlenmiennam.R;

/**
 * Fragment hiển thị giao diện cài đặt với các nút chức năng.
 * Sử dụng layout balatro_setting_dialog.xml.
 * Khi người dùng bấm nút đỏ, callback onActionSelected sẽ được gọi.
 */
public class BalatroSettingsFragment extends BalatroDialogFragment {
    public enum Action {
        AM_THANH, AM_NHAC, MA_QUA, HO_TRO, DANG_XUAT
    }

    public interface OnActionSelectedListener {
        void onActionSelected(Action action);
    }

    private OnActionSelectedListener listener;

    public BalatroSettingsFragment() {
        // Bắt buộc phải có constructor rỗng
    }

    public void setOnActionSelectedListener(OnActionSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balatro_setting_dialog, container, false);

        view.findViewById(R.id.AmThanhButton).setOnClickListener(v -> {
            if (listener != null) listener.onActionSelected(Action.AM_THANH);
        });
        view.findViewById(R.id.AmNhacButton).setOnClickListener(v -> {
            if (listener != null) listener.onActionSelected(Action.AM_NHAC);
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

