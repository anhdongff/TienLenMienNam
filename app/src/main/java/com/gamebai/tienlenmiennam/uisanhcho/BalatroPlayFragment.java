package com.gamebai.tienlenmiennam.uisanhcho;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gamebai.tienlenmiennam.R;
import com.google.android.material.snackbar.Snackbar;

public class BalatroPlayFragment extends BalatroDialogFragment {

    public enum Action {
        JOIN_WITH_CODE,
        QUICK_PLAY,
        CREATE_ROOM
    }

    public interface OnPlayActionSelectedListener {
        void onActionSelected(Action action, @Nullable String roomCode);
    }

    private OnPlayActionSelectedListener listener;

    public void setOnPlayActionSelectedListener(OnPlayActionSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balatro_play_dialog, container, false);

        BalatroEditText balatroEditTextRoomCode = view.findViewById(R.id.balatroEditTextRoomCode);
        BalatroButton balatroButtonPlay = view.findViewById(R.id.balatroButtonPlay);
        BalatroButton balatroButtonCreateRoom = view.findViewById(R.id.balatroButtonCreateRoom);
        BalatroButton balatroButtonQuayLai = view.findViewById(R.id.balatroButtonQuayLai);

        balatroButtonPlay.setOnClickListener(v -> {
            if (listener == null) return;

            String roomCode = balatroEditTextRoomCode.getText().toString().trim();

            if (!TextUtils.isEmpty(roomCode)) {
                // Hành động: Tìm trận với mã phòng
                try {
                    // Kiểm tra xem mã có phải là số không
                    Long.parseLong(roomCode);
                    listener.onActionSelected(Action.JOIN_WITH_CODE, roomCode);
                    dismiss();
                } catch (NumberFormatException e) {
                    // Nếu không phải là số, hiển thị lỗi
                    Snackbar.make(view, "Mã phòng phải là một dãy số.", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                // Hành động: Tìm trận ngay
                listener.onActionSelected(Action.QUICK_PLAY, null);
                dismiss();
            }
        });

        balatroButtonCreateRoom.setOnClickListener(v -> {
            if (listener != null) {
                // Hành động: Tạo phòng
                listener.onActionSelected(Action.CREATE_ROOM, null);
                dismiss();
            }
        });

        balatroButtonQuayLai.setOnClickListener(v -> dismiss());

        return view;
    }
}
