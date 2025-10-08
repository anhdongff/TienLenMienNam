package com.gamebai.tienlenmiennam.shader;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class ShaderView extends GLSurfaceView {
    // Constructor used when creating the view programmatically in code.
    public ShaderView(Context context) {
        super(context);
        init(context, null);
    }

    // Constructor that is called when inflating the view from XML.
    // This is the one that was missing.
    public ShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    // Constructor that is also called when inflating from XML and a style is specified.
    public ShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        // 1. Yêu cầu một bề mặt (surface) hỗ trợ kênh alpha (trong suốt)
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        // 2. Di chuyển GLSurfaceView ra phía sau (lớp dưới cùng)
        setZOrderOnTop(false);
        setEGLContextClientVersion(2); // Dùng OpenGL ES 2.0
        setRenderer(new ShaderRenderer(context));
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
