package com.gamebai.tienlenmiennam.uisanhcho;

import android.graphics.Typeface;
import android.os.Bundle;
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
import com.gamebai.tienlenmiennam.datamodel.YeuCauHoTro;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Fragment hiển thị giao diện yêu cầu hỗ trợ với các nút chức năng.
 * Sử dụng layout balatro_support_request_dialog.xml.
 */
public class BalatroSupportRequestFragment extends BalatroDialogFragment{
    /**
     * các hành động có thể chọn trong yêu cầu hỗ trợ
     */
    public enum Action {REQUEST_SUPPORT}
    /**
     * lớp trừu tượng để lắng nghe sự kiện người dùng chọn hành động,
     * phải được implement (sửa các hàm trong đây) ở Activity hoặc Fragment sử dụng
     */
    public interface OnRequestSupportActionSelectedListener {
        void onActionSelected(Action action);
    }
    private OnRequestSupportActionSelectedListener listener;
    private List<YeuCauHoTro> danhSachYeuCauHoTro;
    private LinearLayout leftButtonPanel;
    public List<YeuCauHoTro> getDanhSachYeuCauHoTro() {
        return danhSachYeuCauHoTro;
    }

    public void setDanhSachYeuCauHoTro(List<YeuCauHoTro> danhSachYeuCauHoTro) {
        this.danhSachYeuCauHoTro = danhSachYeuCauHoTro;
    }
    public BalatroSupportRequestFragment() {}
    public BalatroSupportRequestFragment(List<YeuCauHoTro> danhSachYeuCauHoTro) {
        this.danhSachYeuCauHoTro = danhSachYeuCauHoTro;
        setTyLeRong(0.7f);
    }
    public void setOnActionSelectedListener(OnRequestSupportActionSelectedListener listener) {
        this.listener = listener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balatro_request_support, container, false);

        leftButtonPanel = view.findViewById(R.id.leftButtonPanel);
        BalatroButton buttonDong = view.findViewById(R.id.ButtonDong);
        BalatroButton buttonThemMoiYeuCau = view.findViewById(R.id.ButtonThemMoiYeuCau);
        buttonDong.setOnClickListener(v -> dismiss());
        if (listener != null) {
            buttonThemMoiYeuCau.setOnClickListener(v -> listener.onActionSelected(Action.REQUEST_SUPPORT));
        }
        if (danhSachYeuCauHoTro != null && !danhSachYeuCauHoTro.isEmpty()) {
            for (YeuCauHoTro yeuCau : danhSachYeuCauHoTro) {
                BalatroButton yeuCauButton = new BalatroButton(requireContext(),null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) (48 * getResources().getDisplayMetrics().density)
                );
                yeuCauButton.setLayoutParams(params);
                if (yeuCau.getThoiGianTao() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    String formattedDate = sdf.format(yeuCau.getThoiGianTao().toDate());
                    yeuCauButton.setText(formattedDate);
                } else {
                    yeuCauButton.setText("Không có thời gian");
                }
                yeuCauButton.setColorFont(ContextCompat.getColor(requireContext(), R.color.white));
                yeuCauButton.setColorNormal(ContextCompat.getColor(requireContext(), R.color.balatro_red));
                yeuCauButton.setOnClickListener(l->{
                    LinearLayout rightButtonPanel = view.findViewById(R.id.rightButtonPanel);
                    View tieuDeNoiDungYeuCauView=inflater.inflate(R.layout.balatro_title_and_text_content_item, null);
                    TextView tieuDe=tieuDeNoiDungYeuCauView.findViewById(R.id.titleTextView);
                    TextView noiDung=tieuDeNoiDungYeuCauView.findViewById(R.id.contentTextView);
                    tieuDe.setText(yeuCau.getTieuDe());
                    noiDung.setText(yeuCau.getNoiDung());
                    rightButtonPanel.removeAllViews();
                    rightButtonPanel.addView(tieuDeNoiDungYeuCauView);
                    View phanHoiYeuCauView=inflater.inflate(R.layout.balatro_title_and_text_content_item, null);
                    TextView tieuDePhanHoi=phanHoiYeuCauView.findViewById(R.id.titleTextView);
                    TextView noiDungPhanHoi=phanHoiYeuCauView.findViewById(R.id.contentTextView);
                    tieuDePhanHoi.setText(R.string.phan_hoi);
                    if (yeuCau.getTrangThai()==2) {
                        noiDungPhanHoi.setText(yeuCau.getPhanHoi());
                    }else{
                        noiDungPhanHoi.setText(R.string.yeu_cau_chua_duoc_phan_hoi);
                    }
                    rightButtonPanel.addView(phanHoiYeuCauView);
                });
                leftButtonPanel.addView(yeuCauButton);
            }
        } else {
            TextView emptyTextView = new TextView(getContext());
            emptyTextView.setText(R.string.chua_co_gi_o_day);
            emptyTextView.setGravity(Gravity.CENTER);
            Typeface typeface = ResourcesCompat.getFont(requireContext(), R.font.m6x11plus);
            emptyTextView.setTypeface(typeface);
            leftButtonPanel.addView(emptyTextView);
        }
        return view;
    }
    /**
     * Phương thức công khai để cập nhật toàn bộ danh sách yêu cầu và vẽ lại UI.
     * Sẽ được gọi từ Activity/Fragment cha khi có dữ liệu mới.
     * @param newList Danh sách yêu cầu hỗ trợ mới.
     */
    public void updateListAndRefreshUI(List<YeuCauHoTro> newList) {
        this.danhSachYeuCauHoTro = newList;

        // Kiểm tra xem view đã được tạo chưa để tránh NullPointerException
        if (getView() == null) {
            return; // View chưa sẵn sàng, không làm gì cả.
        }

        // Lấy LayoutInflater từ context và gọi hàm vẽ lại
        LayoutInflater inflater = LayoutInflater.from(getContext());
        updateSupportRequestList(inflater);
    }

    /**
     * Lõi logic để vẽ lại danh sách các nút yêu cầu hỗ trợ.
     * Tách ra từ onCreateView để có thể tái sử dụng.
     */
    private void updateSupportRequestList(LayoutInflater inflater) {
        // Luôn xóa các view cũ trước khi thêm view mới
        leftButtonPanel.removeAllViews();

        if (danhSachYeuCauHoTro != null && !danhSachYeuCauHoTro.isEmpty()) {
            for (YeuCauHoTro yeuCau : danhSachYeuCauHoTro) {
                // ... Toàn bộ code tạo 'BalatroButton yeuCauButton' của bạn ở đây ...
                // (Giữ nguyên không thay đổi)
                BalatroButton yeuCauButton = new BalatroButton(requireContext(), null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) (48 * getResources().getDisplayMetrics().density)
                );
                yeuCauButton.setLayoutParams(params);
                if (yeuCau.getThoiGianTao() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    String formattedDate = sdf.format(yeuCau.getThoiGianTao().toDate());
                    yeuCauButton.setText(formattedDate);
                } else {
                    yeuCauButton.setText("Không có thời gian");
                }
                yeuCauButton.setColorFont(ContextCompat.getColor(requireContext(), R.color.white));
                yeuCauButton.setColorNormal(ContextCompat.getColor(requireContext(), R.color.balatro_red));
                yeuCauButton.setOnClickListener(l -> {
                    // Logic xử lý khi click vào một yêu cầu
                    LinearLayout rightButtonPanel = getView().findViewById(R.id.rightButtonPanel);
                    // ... (Phần code này giữ nguyên) ...
                    View tieuDeNoiDungYeuCauView=inflater.inflate(R.layout.balatro_title_and_text_content_item, null);
                    TextView tieuDe=tieuDeNoiDungYeuCauView.findViewById(R.id.titleTextView);
                    TextView noiDung=tieuDeNoiDungYeuCauView.findViewById(R.id.contentTextView);
                    tieuDe.setText(yeuCau.getTieuDe());
                    noiDung.setText(yeuCau.getNoiDung());
                    rightButtonPanel.removeAllViews();
                    rightButtonPanel.addView(tieuDeNoiDungYeuCauView);
                    View phanHoiYeuCauView=inflater.inflate(R.layout.balatro_title_and_text_content_item, null);
                    TextView tieuDePhanHoi=phanHoiYeuCauView.findViewById(R.id.titleTextView);
                    TextView noiDungPhanHoi=phanHoiYeuCauView.findViewById(R.id.contentTextView);
                    tieuDePhanHoi.setText(R.string.phan_hoi);
                    if (yeuCau.getTrangThai()==2) {
                        noiDungPhanHoi.setText(yeuCau.getPhanHoi());
                    }else{
                        noiDungPhanHoi.setText(R.string.yeu_cau_chua_duoc_phan_hoi);
                    }
                    rightButtonPanel.addView(phanHoiYeuCauView);
                });
                leftButtonPanel.addView(yeuCauButton);
            }
        } else {
            // ... Code hiển thị "Chưa có gì ở đây" của bạn ...
            // (Giữ nguyên không thay đổi)
            TextView emptyTextView = new TextView(getContext());
            emptyTextView.setText(R.string.chua_co_gi_o_day);
            emptyTextView.setGravity(Gravity.CENTER);
            Typeface typeface = ResourcesCompat.getFont(requireContext(), R.font.m6x11plus);
            emptyTextView.setTypeface(typeface);
            leftButtonPanel.addView(emptyTextView);
        }
    }
}
