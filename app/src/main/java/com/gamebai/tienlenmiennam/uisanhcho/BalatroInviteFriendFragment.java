package com.gamebai.tienlenmiennam.uisanhcho;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.datamodel.QuanHeNguoiChoi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BalatroInviteFriendFragment extends BalatroDialogFragment {
    HashMap<String, QuanHeNguoiChoi> danhSachBanBe;
    BalatroInviteFriendAction action;

    public void setAction(BalatroInviteFriendAction action) {
        this.action = action;
    }

    public BalatroInviteFriendFragment() {
    }

    /**
     * Khởi tạo danh sách bạn bè
     *
     * @param danhSachBanBe
     */
    public void init(HashMap<String, QuanHeNguoiChoi> danhSachBanBe) {
        this.danhSachBanBe = danhSachBanBe;
        setTyLeRong(0.7f);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balatro_invite_friend, container, false);
        LinearLayout centerButtonPanel = view.findViewById(R.id.centerButtonPanel);
        BalatroButton btnDong = view.findViewById(R.id.ButtonDong);

        btnDong.setOnClickListener(v -> dismiss());

        List<QuanHeNguoiChoi> onlineFriends = new ArrayList<>();
        if (danhSachBanBe != null) {
            for (QuanHeNguoiChoi friend : danhSachBanBe.values()) {
                if (friend.getTrangThai() == 1 && friend.isOnline()) {
                    onlineFriends.add(friend);
                }
            }
        }

        if (onlineFriends.isEmpty()) {
            TextView emptyTextView = new TextView(getContext());
            emptyTextView.setText("không có gì ở đây");
            emptyTextView.setGravity(Gravity.CENTER);
            emptyTextView.setTextColor(getResources().getColor(R.color.white));
            try {
                Typeface typeface = ResourcesCompat.getFont(requireContext(), R.font.m6x11plus);
                emptyTextView.setTypeface(typeface);
            } catch (Exception e) {
                // Font not found, proceed with default.
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            emptyTextView.setLayoutParams(params);
            centerButtonPanel.addView(emptyTextView);
        } else {
            for (QuanHeNguoiChoi friend : onlineFriends) {
                View itemView = inflater.inflate(R.layout.balatro_friend_action_item, centerButtonPanel, false);

                TextView tenTextView = itemView.findViewById(R.id.tenTextView);
                TextView tienTextView = itemView.findViewById(R.id.tienTextView);
                BalatroButton actionButton = itemView.findViewById(R.id.balatroButtonAction);

                tenTextView.setText(friend.getTenBanBe());
                tienTextView.setText(String.valueOf(friend.getTien()));
                if(System.currentTimeMillis() - friend.getLastInviteTime()<=1000*10){
                    actionButton.setText("Đã mời");
                    actionButton.setColorNormal(R.color.balatro_yellow);
                }else{
                    actionButton.setText("Mời");
                }

                actionButton.setOnClickListener(v -> {
                    if (action != null) {
                        action.onInviteFriend(friend);
                        actionButton.setText("Đã mời");
                        actionButton.setColorNormal(R.color.balatro_yellow);
                    }
                });

                centerButtonPanel.addView(itemView);
            }
        }

        return view;
    }

    public interface BalatroInviteFriendAction {
        void onInviteFriend(QuanHeNguoiChoi quanHeNguoiChoi);
    }

}
