package com.gamebai.tienlenmiennam.hotro;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * khung sườn của một lớp trạng thái game
 * trạng thái game ở đây chỉ các giao diện khác nhau của game như trận đấu, ngoài sảnh ...
 * các trạng thái game cần kế thừa lớp này
 */
public interface TrangThaiGame {
    /**
     * cập nhật mọi thông số trong trạng thái game
     * @param delta thời gian giữa 2 lần cập nhật tính bằng giây
     * @param isCanvasNull true nếu không có "vải căng khung tranh" để vẽ lên màn hình, có thể có lúc cần dùng để đồng bộ với render
     */
    void capNhat(double delta,boolean isCanvasNull);

    /**
     * vẽ lên màn hình
     * @param c "vải căng khung tranh"
     */
    void render(Canvas c);

    /**
     * xử lý sự kiện chạm màn hình
     * @param event
     */
    void touchEvent(MotionEvent event);
}
