package com.gamebai.tienlenmiennam.uisanhcho;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
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
    private int disabledColor = Color.LTGRAY; // xám khi disable
    private int borderColor = Color.TRANSPARENT; // viền mặc định trong suốt
    private int colorFont = Color.rgb(58, 31, 11); // nâu đậm
    private float fontSize = 18f;
    private Paint.Align textAlign = Paint.Align.CENTER;
    /** Có tự động chia dòng khi text quá dài không */
    private boolean autoLines = true;

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
            float defaultFontSizeInPixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, fontSize, getResources().getDisplayMetrics()
            );
            setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    a.getDimension(R.styleable.BalatroButton_fontSize, defaultFontSizeInPixels));
            int align = a.getInt(R.styleable.BalatroButton_textAlign, 1); // 0: LEFT, 1: CENTER, 2: RIGHT
            switch (align) {
                case 0: textAlign = Paint.Align.LEFT; break;
                case 2: textAlign = Paint.Align.RIGHT; break;
                default: textAlign = Paint.Align.CENTER; break;
            }
            shadowEnabled = a.getBoolean(R.styleable.BalatroButton_shadowEnabled, shadowEnabled);
            pressable = a.getBoolean(R.styleable.BalatroButton_pressable, pressable);
            autoLines = a.getBoolean(R.styleable.BalatroButton_autoLines, autoLines);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float shadowPadding = 24f;

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(getTextSize());
        textPaint.setTypeface(getTypeface());

        int desiredWidth;
        int desiredHeight;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if (autoLines) {
            int availableWidthForText;
            if (widthMode == MeasureSpec.EXACTLY) {
                availableWidthForText = widthSize - getPaddingLeft() - getPaddingRight() - (int) (shadowPadding * 2);
            } else {
                // Nếu là wrap_content, không có giới hạn chiều rộng, không thể ngắt dòng.
                // Đo văn bản trên một dòng duy nhất để xác định chiều rộng.
                availableWidthForText = (int) textPaint.measureText(getText().toString());
            }

            StaticLayout staticLayout = new StaticLayout(getText(), textPaint, Math.max(0, availableWidthForText), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (staticLayout.getLineCount() > 1) {
                staticLayout = new StaticLayout(getText(), textPaint, Math.max(0, availableWidthForText), Layout.Alignment.ALIGN_NORMAL, 0.75f, 0.0f, false);
            }

            float textWidth = 0;
            for (int i = 0; i < staticLayout.getLineCount(); i++) {
                textWidth = Math.max(textWidth, staticLayout.getLineWidth(i));
            }

            int textHeight = staticLayout.getHeight();

            desiredWidth = (int) textWidth + getPaddingLeft() + getPaddingRight() + (int) (shadowPadding * 2);
            desiredHeight = textHeight + getPaddingTop() + getPaddingBottom() + (int) (shadowPadding * 2);

        } else {
            String[] lines = getText().toString().split("\n");
            float maxWidth = 0;
            for (String line : lines) {
                maxWidth = Math.max(maxWidth, textPaint.measureText(line));
            }

            Paint.FontMetrics fm = textPaint.getFontMetrics();
            float lineHeight = (fm.descent - fm.ascent) * (lines.length > 1 ? 0.75f : 1f);
            float totalTextHeight = lines.length * lineHeight;

            desiredWidth = (int) maxWidth + getPaddingLeft() + getPaddingRight() + (int) (shadowPadding * 2);
            desiredHeight = (int) totalTextHeight + getPaddingTop() + getPaddingBottom() + (int) (shadowPadding * 2);
        }

        int finalWidth = resolveSize(desiredWidth, widthMeasureSpec);
        int finalHeight = resolveSize(desiredHeight, heightMeasureSpec);

        setMeasuredDimension(finalWidth, finalHeight);
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
        if(isEnabled()){
            paint.setColor(pressed ? colorPressed : colorNormal);
        } else {
            paint.setColor(disabledColor);
        }
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
        if (!autoLines) {
            Paint.FontMetrics fm = paint.getFontMetrics();
            String[] lines = getText().toString().split("\n");
            float lineHeight = (fm.descent - fm.ascent)*(lines.length>1?0.75f:1f);
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
        }else{
            // Sử dụng TextPaint và StaticLayout để chữ có thể tự động xuống dòng
            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(colorFont);
            textPaint.setTextSize(getTextSize());
            textPaint.setTypeface(getTypeface());
            Layout.Alignment layoutAlignment;
            switch (textAlign) {
                case LEFT:
                    layoutAlignment = Layout.Alignment.ALIGN_NORMAL;
                    break;
                case RIGHT:
                    layoutAlignment = Layout.Alignment.ALIGN_OPPOSITE;
                    break;
                default: // CENTER
                    layoutAlignment = Layout.Alignment.ALIGN_CENTER;
                    break;
            }
            // Chiều rộng có sẵn cho văn bản bên trong nút
            float availableTextWidth = rect.width() - getPaddingLeft() - getPaddingRight();
            // Tạo StaticLayout để xử lý việc ngắt dòng tự động
            StaticLayout staticLayout = new StaticLayout(
                    getText(),
                    textPaint,
                    (int) availableTextWidth,
                    layoutAlignment,
                    1.0f, // giãn cách dòng (multiplier)
                    0.0f, // giãn cách dòng (extra)
                    false // không thêm padding trên và dưới
            );
            // Nếu văn bản có nhiều hơn 1 dòng, giảm khoảng cách giữa các dòng
            if (staticLayout.getLineCount() > 1) {
                staticLayout = new StaticLayout(
                        getText(),
                        textPaint,
                        (int) availableTextWidth,
                        layoutAlignment,
                        0.75f, // Giảm giãn cách dòng cho văn bản nhiều dòng
                        0.0f,
                        false
                );
            }
            // Tính toán vị trí Y để căn giữa khối văn bản theo chiều dọc
            float textBlockHeight = staticLayout.getHeight();
            float textY = rect.top + (rect.height() - textBlockHeight) / 2f;
            // Tính toán vị trí X
            float textX = rect.left + getPaddingLeft();
            // Vẽ văn bản lên canvas
            canvas.save();
            canvas.translate(textX, textY);
            staticLayout.draw(canvas);
            canvas.restore();
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

    public void setColorFont(int color) {
        this.colorFont = color;
        invalidate();
    }
}
