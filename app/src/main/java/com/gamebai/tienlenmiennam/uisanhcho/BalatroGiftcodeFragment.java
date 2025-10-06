package com.gamebai.tienlenmiennam.uisanhcho;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gamebai.tienlenmiennam.R;

public class BalatroGiftcodeFragment extends BalatroDialogFragment {
    public interface OnGiftcodeActionListener {
        void onUseGiftcode(String giftcode, View v);
        void onCancel();
    }
    public void setOnActionSelectedListener(OnGiftcodeActionListener listener) {
        this.listener = listener;
    }
    private OnGiftcodeActionListener listener;

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
            dismiss();
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
