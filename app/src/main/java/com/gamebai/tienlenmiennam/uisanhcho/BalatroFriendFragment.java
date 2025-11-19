package com.gamebai.tienlenmiennam.uisanhcho;

import android.app.AlertDialog;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BalatroFriendFragment extends BalatroDialogFragment {
    HashMap<String, QuanHeNguoiChoi> danhSachQuanHeNguoiChoi;
    BalatroFriendFragmentAction action;
    /**
     * id của người chơi
     */
    String userId;
    private LinearLayout rightPanel;
    private LayoutInflater inflater;

    public void setAction(BalatroFriendFragmentAction action) {
        this.action = action;
    }

    public BalatroFriendFragment() {
    }

    /**
     * khởi tạo các giá trị cần thiết cho fragment
     *
     * @param danhSachQuanHeNguoiChoi danh sách bạn bè
     * @param userId                  id của người chơi
     */
    public void init(HashMap<String, QuanHeNguoiChoi> danhSachQuanHeNguoiChoi, String userId) {
        this.danhSachQuanHeNguoiChoi = danhSachQuanHeNguoiChoi;
        this.userId = userId;
        setTyLeRong(0.9f);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.balatro_friend, container, false);

        rightPanel = view.findViewById(R.id.rightPanel);
        BalatroButton btnBanBe = view.findViewById(R.id.btnBanBe);
        BalatroButton btnLoiMoi = view.findViewById(R.id.btnLoiMoi);
        BalatroButton btnTimBan = view.findViewById(R.id.btnTimBan);
        BalatroButton btnDong = view.findViewById(R.id.ButtonDong);

        btnDong.setOnClickListener(v -> dismiss());

        btnBanBe.setOnClickListener(v -> showFriendsList());
        btnLoiMoi.setOnClickListener(v -> showFriendRequests());
        btnTimBan.setOnClickListener(v -> showSearchFriend());

        // Show friends list by default
        showFriendsList();

        return view;
    }

    private void showFriendsList() {
        rightPanel.removeAllViews();
        List<QuanHeNguoiChoi> friendList = new ArrayList<>();
        if (danhSachQuanHeNguoiChoi != null) {
            for (QuanHeNguoiChoi qh : danhSachQuanHeNguoiChoi.values()) {
                if (qh.getTrangThai() == 1) {
                    friendList.add(qh);
                }
            }
        }

        if (friendList.isEmpty()) {
            showEmptyMessage();
            return;
        }

        friendList.sort((o1, o2) -> Boolean.compare(o2.isOnline(), o1.isOnline()));

        for (QuanHeNguoiChoi friend : friendList) {
            View itemView = inflater.inflate(R.layout.balatro_friend_item, rightPanel, false);

            TextView tenTextView = itemView.findViewById(R.id.tenTextView);
            TextView tienTextView = itemView.findViewById(R.id.tienTextView);
            TextView onlineTextView = itemView.findViewById(R.id.onlineTextView);
            BalatroButton huyKetBanButton = itemView.findViewById(R.id.balatroButtonHuyKetBan);

            tenTextView.setText(friend.getTenBanBe());
            tienTextView.setText(friend.getTien()+" "+getString(R.string.xu));
            onlineTextView.setText(friend.isOnline() ? "online" : "offline");

            huyKetBanButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc muốn huỷ kết bạn với người chơi này?");
                builder.setPositiveButton(R.string.co, (dialog, which) -> {
                    huyKetBanButton.setEnabled(false);
                    if (action != null) {
                        action.onAction(Action.UNFRIEND, friend, getView());
                    }
                });
                builder.setNegativeButton(R.string.huy, (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
            });
            rightPanel.addView(itemView);
        }
    }

    private void showFriendRequests() {
        rightPanel.removeAllViews();
        List<QuanHeNguoiChoi> requestList = new ArrayList<>();
        if (danhSachQuanHeNguoiChoi != null) {
            for (QuanHeNguoiChoi qh : danhSachQuanHeNguoiChoi.values()) {
                if (qh.getTrangThai() == 0 && qh.getUserIds().get(1).equals(userId)) {
                    requestList.add(qh);
                }
            }
        }

        if (requestList.isEmpty()) {
            showEmptyMessage();
            return;
        }

        for (QuanHeNguoiChoi request : requestList) {
            View itemView = inflater.inflate(R.layout.balatro_friend_request_item, rightPanel, false);

            TextView tenTextView = itemView.findViewById(R.id.tenTextView);
            TextView tienTextView = itemView.findViewById(R.id.tienTextView);
            BalatroButton chapNhanButton = itemView.findViewById(R.id.balatroButtonChapNhan);
            BalatroButton tuChoiButton = itemView.findViewById(R.id.balatroButtonTuChoi);

            tenTextView.setText(request.getTenBanBe());
            tienTextView.setText(request.getTien()+" "+getString(R.string.xu));

            chapNhanButton.setOnClickListener(v -> {
                chapNhanButton.setEnabled(false);
                tuChoiButton.setEnabled(false);
                if (action != null) {
                    action.onAction(Action.ACCEPT_FRIEND, request, getView());
                }
            });

            tuChoiButton.setOnClickListener(v -> {
                chapNhanButton.setEnabled(false);
                tuChoiButton.setEnabled(false);
                if (action != null) {
                    action.onAction(Action.REJECT_FRIEND, request, getView());
                }
            });

            rightPanel.addView(itemView);
        }
    }

    private void showSearchFriend() {
        rightPanel.removeAllViews();
        View searchView = inflater.inflate(R.layout.balatro_friend_search, rightPanel, false);
        rightPanel.addView(searchView);

        BalatroEditText searchEditText = searchView.findViewById(R.id.balatroEditTextSearch);
        BalatroButton searchButton = searchView.findViewById(R.id.balatroButtonTimKiem);
        LinearLayout searchResultContainer = new LinearLayout(getContext());
        searchResultContainer.setOrientation(LinearLayout.VERTICAL);
        rightPanel.addView(searchResultContainer);


        searchButton.setOnClickListener(v -> {
            String searchText = searchEditText.getText().toString().trim();
            if (searchText.length() >= 6 && searchText.length() <= 12) {
                if (action != null) {
                    action.searchFriend(searchText, new SearchFriendCallback() {
                        @Override
                        public void onResult(QuanHeNguoiChoi result) {
                            searchResultContainer.removeAllViews();
                            if (result!=null) {
                                View actionItemView = inflater.inflate(R.layout.balatro_friend_action_item, searchResultContainer, false);

                                TextView tenTextView = actionItemView.findViewById(R.id.tenTextView);
                                TextView tienTextView = actionItemView.findViewById(R.id.tienTextView);
                                BalatroButton actionButton = actionItemView.findViewById(R.id.balatroButtonAction);

                                tenTextView.setText(result.getTenBanBe());
                                tienTextView.setText(result.getTien()+" "+getString(R.string.xu));
                                actionButton.setText(R.string.them_ban_be);

                                actionButton.setOnClickListener(view -> {
                                    for(QuanHeNguoiChoi quanHeNguoiChoi : danhSachQuanHeNguoiChoi.values()){
                                        if(quanHeNguoiChoi.getUserIds().contains(result.getUserIds().get(1))){
                                            if(quanHeNguoiChoi.getTrangThai()==1){
                                                Snackbar.make(view, R.string.ban_da_la_ban_be, Snackbar.LENGTH_SHORT).show();
                                                return;
                                            }else{
                                                if(quanHeNguoiChoi.getUserIds().get(0).equals(result.getUserIds().get(1))){
                                                    Snackbar.make(view, R.string.nguoi_choi_da_gui_loi_moi_ket_ban, Snackbar.LENGTH_SHORT).show();
                                                }else{
                                                    Snackbar.make(view, R.string.ban_da_gui_loi_moi_ket_ban, Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                            return;
                                        }
                                    }
                                    action.onAction(Action.ADD_FRIEND, result, view);
                                    actionButton.setEnabled(false);
                                });
                                searchResultContainer.addView(actionItemView);
                            }else{
                                showEmptyMessage(searchResultContainer);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            searchResultContainer.removeAllViews();
                            showEmptyMessage(searchResultContainer);
                        }
                    });
                }
            }
        });
    }

    private void showEmptyMessage() {
        showEmptyMessage(rightPanel);
    }
    private void showEmptyMessage(ViewGroup container) {
        container.removeAllViews();
        TextView emptyTextView = new TextView(getContext());
        emptyTextView.setText("không có gì ở đây");
        emptyTextView.setGravity(Gravity.CENTER);
        emptyTextView.setTextColor(getResources().getColor(R.color.gray));

        try {
            Typeface typeface = ResourcesCompat.getFont(requireContext(), R.font.m6x11plus);
            emptyTextView.setTypeface(typeface);
        } catch (Exception e) {
            // Font not found, proceed with default.
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        emptyTextView.setLayoutParams(params);
        container.addView(emptyTextView);
    }


    public enum Action {
        UNFRIEND,
        ADD_FRIEND,
        ACCEPT_FRIEND,
        REJECT_FRIEND
    }

    public interface SearchFriendCallback {
        void onResult(QuanHeNguoiChoi result);

        void onError(Exception e);
    }

    public interface BalatroFriendFragmentAction {
        void onAction(Action action, QuanHeNguoiChoi quanHeNguoiChoi, View view);

        void searchFriend(String name, SearchFriendCallback callback);
    }
}
