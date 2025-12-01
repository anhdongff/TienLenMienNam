package com.gamebai.tienlenmiennam.uisanhcho;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gamebai.tienlenmiennam.R;

public class BalatroComfirmInviteFragment extends BalatroDialogFragment {
    String tenNguoiChoi;
    String maPhong;
    BalatroComfirmInviteAction listener;

    public BalatroComfirmInviteFragment() {
    }

    public void init(String tenNguoiChoi, String maPhong) {
        this.tenNguoiChoi = tenNguoiChoi;
        this.maPhong = maPhong;
        setTyLeRong(0.6f);
    }

    public void setAction(BalatroComfirmInviteAction listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balatro_comfirm_invite, container, false);

        TextView thongTinLoiMoi = view.findViewById(R.id.textViewThongTinLoiMoi);
        BalatroButton btnTuChoi = view.findViewById(R.id.btnTuChoi);
        BalatroButton btnThamGia = view.findViewById(R.id.btnThamGia);

        String noiDung = getString(R.string.noi_dung_loi_moi, tenNguoiChoi, maPhong);
        thongTinLoiMoi.setText(noiDung);

        btnTuChoi.setOnClickListener(v -> dismiss());

        btnThamGia.setOnClickListener(v -> {
            if (listener != null) {
                listener.onThamGia();
            }
            dismiss();
        });
//        setCancelable(false);
        return view;
    }

    public interface BalatroComfirmInviteAction {
        void onThamGia();
    }
}
