package com.gamebai.tienlenmiennam.uisanhcho;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import android.view.Gravity;
import android.graphics.Color;

import com.gamebai.tienlenmiennam.R;

public class BalatroEditText extends AppCompatEditText {

    public BalatroEditText(Context context) {
        super(context);
        init();
    }

    public BalatroEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BalatroEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.bg_balatro_edittext);
//        setTextSize(18);
        setPadding(24, 16, 24, 16);
//        setGravity(Gravity.CENTER_VERTICAL);
        setTypeface(ResourcesCompat.getFont(getContext(), com.gamebai.tienlenmiennam.R.font.m6x11plus)); // hoặc font pixel tuỳ bạn
    }
}
