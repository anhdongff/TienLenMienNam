package com.gamebai.tienlenmiennam.uisanhcho;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.gamebai.tienlenmiennam.R;

/**
 * Fragment hiển thị một dialog với layout tùy chọn và style, animation của balatro
 * Hãy override onCreateView để inflate layout mong muốn và xử lý các sự kiện
 */
public class BalatroDialogFragment extends DialogFragment {
    private float tyLeRong = 0.47f;

    /**
     * đặt tỷ lệ chiều cao của dialog so với chiều cao cửa sổ (mặc định 0.95)
     * @param tyLeCao
     */
    public void setTyLeCao(float tyLeCao) {
        this.tyLeCao = tyLeCao;
    }

    /**
     * đặt tỷ lệ chiều rộng của dialog so với chiều rộng cửa sổ (mặc định 0.47)
     * @param tyLeRong
     */
    public void setTyLeRong(float tyLeRong) {
        this.tyLeRong = tyLeRong;
    }

    private float tyLeCao = 0.95f;

    public float getTyLeRong() {
        return tyLeRong;
    }

    public float getTyLeCao() {
        return tyLeCao;
    }

    public BalatroDialogFragment() {
        // Bắt buộc phải có constructor rỗng
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.BalatroDialogAnimation;
        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();

            // Lấy kích thước màn hình
            DisplayMetrics metrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = (int) (metrics.widthPixels * tyLeRong);
            int height = (int) (metrics.heightPixels * tyLeCao);

            // Áp dụng layout
            window.setLayout(width, height);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Nếu muốn, có thể tự động lấy listener từ context
        // if (context instanceof OnActionSelectedListener) {
        //     listener = (OnActionSelectedListener) context;
        // }
    }
}
