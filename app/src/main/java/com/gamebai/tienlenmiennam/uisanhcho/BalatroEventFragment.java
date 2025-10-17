package com.gamebai.tienlenmiennam.uisanhcho;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.gamebai.tienlenmiennam.R;
import com.gamebai.tienlenmiennam.datamodel.SuKien;
import com.gamebai.tienlenmiennam.datamodel.TienTrinhNhiemVu;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BalatroEventFragment extends BalatroDialogFragment{
    private List<SuKien> danhSachSuKien;
    public interface BalatroEventFragmentListener {
        void onClaimReward(String idSuKien, String idNhiemVu, View view);
    }
    private BalatroEventFragmentListener listener;
    public void setListener(BalatroEventFragmentListener listener) {
        this.listener = listener;
    }
    public void setDanhSachSuKien(List<SuKien> danhSachSuKien) {
        this.danhSachSuKien = danhSachSuKien;
        setTyLeRong(0.9f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balatro_event, container, false);

        LinearLayout leftButtonPanel = view.findViewById(R.id.leftButtonPanel);
        LinearLayout rightButtonPanel = view.findViewById(R.id.rightButtonPanel);
        BalatroButton buttonDong = view.findViewById(R.id.ButtonDong);

        buttonDong.setOnClickListener(v -> dismiss());

        if (danhSachSuKien != null && !danhSachSuKien.isEmpty()) {
            for (SuKien suKien : danhSachSuKien) {
                if(suKien.getThoiGianBatDau()>System.currentTimeMillis()||suKien.getThoiGianKetThuc()<System.currentTimeMillis()){
                    continue;
                }
                BalatroButton suKienButton = new BalatroButton(requireContext(),null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) (78 * getResources().getDisplayMetrics().density)
                );
                suKienButton.setLayoutParams(params);
                suKienButton.setText(suKien.getTieuDe());
                suKienButton.setColorFont(ContextCompat.getColor(requireContext(), R.color.white));
                suKienButton.setColorNormal(ContextCompat.getColor(requireContext(), R.color.balatro_red));
                suKienButton.setOnClickListener(v -> {
                    rightButtonPanel.removeAllViews();

                    // Inflate title and description
                    View titleView = inflater.inflate(R.layout.balatro_title_and_text_content_item, rightButtonPanel, false);
                    TextView titleTextView = titleView.findViewById(R.id.titleTextView);
                    TextView contentTextView = titleView.findViewById(R.id.contentTextView);
                    String thoiGian="";
                    if(suKien.getLoai()>1){
                        Date ngayTao=new Date(suKien.getThoiGianBatDau()+7*60*60*1000);
                        Date ngayKetThuc=new Date(suKien.getThoiGianKetThuc()+7*60*60*1000);
                        thoiGian="Thời gian: "+DateFormat.format("hh:mm dd/MM/yyyy", ngayTao)+" - "+
                                DateFormat.format("hh:mm dd/MM/yyyy", ngayKetThuc)+"\n";
                    }
                    titleTextView.setText(suKien.getTieuDe());
                    contentTextView.setText(thoiGian+suKien.getMoTa());
                    rightButtonPanel.addView(titleView);

                    // Inflate quests
                    if (suKien.getTienTrinhNhiemVu() != null) {
                        for (Map.Entry<String, TienTrinhNhiemVu> entry : suKien.getTienTrinhNhiemVu().entrySet()) {
                            String nhiemVuId = entry.getKey();
                            TienTrinhNhiemVu nhiemVu = entry.getValue();

                            View questView = inflater.inflate(R.layout.balatro_event_quest, rightButtonPanel, false);
                            TextView questTitle = questView.findViewById(R.id.titleTextView);
                            TextView questReward = questView.findViewById(R.id.rewardTextView);
                            BalatroButton claimButton = questView.findViewById(R.id.ClaimBalatroButton);
                            questTitle.setText(nhiemVu.getNoiDung()+" ("
                                    + nhiemVu.getGiaTriHienTai() + "/" + nhiemVu.getGiaTriYeuCau() + ")");
                            questReward.setText(String.format("%d$", nhiemVu.getPhanThuong()));
                            if (nhiemVu.isDaNhan()) {
                                claimButton.setText("Đã nhận");
                                claimButton.setEnabled(false);
                            } else {
                                if (nhiemVu.getGiaTriHienTai() >= nhiemVu.getGiaTriYeuCau()) {
                                    claimButton.setEnabled(true);
                                    claimButton.setOnClickListener(claimView -> {
                                        if (listener != null) {
                                            listener.onClaimReward(suKien.getId(), nhiemVuId, view);
                                            claimButton.setText("Đã nhận");
                                            claimButton.setEnabled(false);
                                        }
                                    });
                                } else {
                                    claimButton.setEnabled(false);
                                }
                            }
                            rightButtonPanel.addView(questView);
                        }
                    }
                });

                leftButtonPanel.addView(suKienButton);
            }
        } else {
            EditText emptyTextView = new EditText(getContext());
            emptyTextView.setText("Chưa có sự kiện nào");
            emptyTextView.setGravity(Gravity.CENTER);
            Typeface typeface = ResourcesCompat.getFont(requireContext(), R.font.m6x11plus);
            emptyTextView.setTypeface(typeface);
            leftButtonPanel.addView(emptyTextView);
        }

        return view;
    }
}
