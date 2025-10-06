package com.gamebai.tienlenmiennam.uisanhcho;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.gamebai.tienlenmiennam.R;

public class BalatroButton extends AppCompatTextView {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rect = new RectF();
    private boolean pressed = false;

    // Màu chính của button (có thể thay đổi)
    private int colorNormal = Color.rgb(255, 216, 168); // sáng mặc định
    private int colorPressed; // sẽ tự động tính dựa trên colorNormal
    private int borderColor = Color.TRANSPARENT; // viền mặc định trong suốt
    private int colorFont = Color.rgb(58, 31, 11); // nâu đậm
    private float fontSize = 18f;
    private Paint.Align textAlign = Paint.Align.CENTER;

    // các chức năng khác
    private boolean shadowEnabled = true;
    private boolean pressable = true;

    public BalatroButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(32, 16, 32, 16);
        setTextColor(Color.rgb(58, 31, 11)); // nâu đậm
        Typeface typeface = ResourcesCompat.getFont(context, com.gamebai.tienlenmiennam.R.font.m6x11plus);
        setTypeface(typeface);
        // Lấy màu từ xml nếu có
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BalatroButton);
            colorNormal = a.getColor(R.styleable.BalatroButton_colorNormal, colorNormal);
            colorFont = a.getColor(R.styleable.BalatroButton_colorFont, colorFont);
            borderColor=a.getColor(R.styleable.BalatroButton_borderColor,borderColor);
            setTextSize(a.getFloat(R.styleable.BalatroButton_fontSize, fontSize));
            int align = a.getInt(R.styleable.BalatroButton_textAlign, 1); // 0: LEFT, 1: CENTER, 2: RIGHT
            switch (align) {
                case 0: textAlign = Paint.Align.LEFT; break;
                case 2: textAlign = Paint.Align.RIGHT; break;
                default: textAlign = Paint.Align.CENTER; break;
            }
            shadowEnabled = a.getBoolean(R.styleable.BalatroButton_shadowEnabled, shadowEnabled);
            pressable = a.getBoolean(R.styleable.BalatroButton_pressable, pressable);
            a.recycle();
        }
        colorPressed = darkenColor(colorNormal);

        // Cho phép vẽ shadow
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setClipToOutline(false);
    }

    // Hàm thiết lập màu nền bình thường
    public void setColorNormal(int color) {
        this.colorNormal = color;
        this.colorPressed = darkenColor(color);
        invalidate();
    }

    // Hàm làm tối màu
    private int darkenColor(int color) {
        float factor = 0.85f;
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.rgb(r, g, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float shadowPadding = 24f; // same as your shadow padding
        rect.set(
                shadowPadding,
                shadowPadding,
                getWidth() - shadowPadding,
                getHeight() - shadowPadding
        );

        // Tính toán vị trí nút trên màn hình
        int[] location = new int[2];
        getLocationOnScreen(location);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        float centerX = location[0] + getWidth() / 2f;
        float percentFromCenter = (centerX - screenWidth / 2f) / (screenWidth / 2f);

        // Đổ bóng
        float shadowRadius = 1f;
        float shadowDx = -percentFromCenter * 24f; // lệch bóng theo vị trí
        float shadowDy = 16f;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(pressed&&isEnabled() ? colorPressed : colorNormal);
        if(!pressed&&isEnabled()&&shadowEnabled) paint.setShadowLayer(shadowRadius, shadowDx, shadowDy, Color.argb(80, 0, 0, 0));
        canvas.drawRoundRect(rect, 24, 24, paint);
        paint.clearShadowLayer();

        // Viền
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        paint.setColor(borderColor);
        canvas.drawRoundRect(rect, 24, 24, paint);

        // Vẽ chữ
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorFont);
        paint.setTextAlign(textAlign);
        paint.setTextSize(getTextSize());
        paint.setTypeface(getTypeface());
        Paint.FontMetrics fm = paint.getFontMetrics();
        String[] lines = getText().toString().split("\n");
        float lineHeight = (fm.descent - fm.ascent)*0.75f;
        float totalTextHeight = lines.length * lineHeight;
        float textY = getHeight() / 2f - totalTextHeight / 2f - fm.ascent;

        float textX;
        switch (textAlign) {
            case LEFT:
                textX = getPaddingLeft() + 4; // hoặc 0 tuỳ ý
                break;
            case RIGHT:
                textX = getWidth() - getPaddingRight() - 4;
                break;
            default:
                textX = getWidth() / 2f;
                break;
        }

        for (String line : lines) {
            canvas.drawText(line, textX, textY, paint);
            textY += lineHeight;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(pressable&&isEnabled()){
                    pressed = true;
                    startScaleAnimation(0.95f); // nhỏ lại 5%
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if(pressable&&isEnabled()) {
                    pressed = false;
                    startScaleAnimation(1.0f);
                    invalidate();
                    performClick(); // gọi sự kiện click
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                if(pressable&&isEnabled()) {
                    pressed = false;
                    startScaleAnimation(1.0f);
                    invalidate();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void startScaleAnimation(float targetScale) {
        ScaleAnimation anim = new ScaleAnimation(
                getScaleX(), targetScale,
                getScaleY(), targetScale,
                getWidth() / 2f, getHeight() / 2f
        );
        anim.setFillAfter(true);
        anim.setDuration(80);
        startAnimation(anim);
    }
}
