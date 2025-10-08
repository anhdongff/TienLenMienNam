package com.gamebai.tienlenmiennam.shader;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ShaderRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private int program;
    private int width, height;
    private float time = 0f;
    private float spinRotation = -2.0f;
    private float spinSpeed = 7.0f;
    float[] offset = {0.0f, 0.0f};
    String color1 = "#DE443B", color2 = "#006BB4", color3 = "#162325";
    private float contrast = 3.5f, lighting = 0.4f, spinAmount = 0.25f, pixelFilter = 2000f, spinEase = 1.0f;
    private int isRotate = 0; // false

    public ShaderRenderer(Context context) {
        this.context = context;
    }
    private float[] hexToRgba(String hex) {
        if (hex == null) return new float[]{0f, 0f, 0f, 1f};
        String h = hex.replace("#", "").trim();
        int r = 0, g = 0, b = 0, a = 255;
        try {
            if (h.length() == 6) {
                r = Integer.parseInt(h.substring(0, 2), 16);
                g = Integer.parseInt(h.substring(2, 4), 16);
                b = Integer.parseInt(h.substring(4, 6), 16);
            } else if (h.length() == 8) {
                a = Integer.parseInt(h.substring(0, 2), 16);
                r = Integer.parseInt(h.substring(2, 4), 16);
                g = Integer.parseInt(h.substring(4, 6), 16);
                b = Integer.parseInt(h.substring(6, 8), 16);
            } else if (h.length() == 3) {
                r = Integer.parseInt("" + h.charAt(0) + h.charAt(0), 16);
                g = Integer.parseInt("" + h.charAt(1) + h.charAt(1), 16);
                b = Integer.parseInt("" + h.charAt(2) + h.charAt(2), 16);
            } else if (h.length() == 4) {
                a = Integer.parseInt("" + h.charAt(0) + h.charAt(0), 16);
                r = Integer.parseInt("" + h.charAt(1) + h.charAt(1), 16);
                g = Integer.parseInt("" + h.charAt(2) + h.charAt(2), 16);
                b = Integer.parseInt("" + h.charAt(3) + h.charAt(3), 16);
            } else {
                return new float[]{0f, 0f, 0f, 1f};
            }
        } catch (NumberFormatException e) {
            return new float[]{0f, 0f, 0f, 1f};
        }
        return new float[]{r / 255f, g / 255f, b / 255f, a / 255f};
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShaderCode = "attribute vec2 aPosition;\n" +
                "varying vec2 vUv;\n" +
                "void main() {\n" +
                "  vUv = aPosition * 0.5 + 0.5;\n" +
                "  gl_Position = vec4(aPosition, 0.0, 1.0);\n" +
                "}";

        String fragmentShaderCode =
                "precision highp float;\n" +
                        "\n" +
                        "#define PI 3.14159265359\n" +
                        "\n" +
                        "uniform float iTime;\n" +
                        "uniform vec3 iResolution;\n" +
                        "uniform float uSpinRotation;\n" +
                        "uniform float uSpinSpeed;\n" +
                        "uniform vec2 uOffset;\n" +
                        "uniform vec4 uColor1;\n" +
                        "uniform vec4 uColor2;\n" +
                        "uniform vec4 uColor3;\n" +
                        "uniform float uContrast;\n" +
                        "uniform float uLighting;\n" +
                        "uniform float uSpinAmount;\n" +
                        "uniform float uPixelFilter;\n" +
                        "uniform float uSpinEase;\n" +
                        "uniform bool uIsRotate;\n" +
                        "uniform vec2 uMouse;\n" +
                        "\n" +
                        "varying vec2 vUv;\n" +
                        "\n" +
                        "vec4 effect(vec2 screenSize, vec2 screen_coords) {\n" +
                        "    float pixel_size = length(screenSize.xy) / uPixelFilter;\n" +
                        "    vec2 uv = (floor(screen_coords.xy * (1.0 / pixel_size)) * pixel_size - 0.5 * screenSize.xy) / length(screenSize.xy) - uOffset;\n" +
                        "    float uv_len = length(uv);\n" +
                        "    \n" +
                        "    float speed = (uSpinRotation * uSpinEase * 0.2);\n" +
                        "    if(uIsRotate){\n" +
                        "       speed = iTime * speed;\n" +
                        "    }\n" +
                        "    speed += 302.2;\n" +
                        "    \n" +
                        "    float mouseInfluence = (uMouse.x * 2.0 - 1.0);\n" +
                        "    speed += mouseInfluence * 0.1;\n" +
                        "    \n" +
                        "    float new_pixel_angle = atan(uv.y, uv.x) + speed - uSpinEase * 20.0 * (uSpinAmount * uv_len + (1.0 - uSpinAmount));\n" +
                        "    vec2 mid = (screenSize.xy / length(screenSize.xy)) / 2.0;\n" +
                        "    uv = (vec2(uv_len * cos(new_pixel_angle) + mid.x, uv_len * sin(new_pixel_angle) + mid.y) - mid);\n" +
                        "    \n" +
                        "    uv *= 30.0;\n" +
                        "    float baseSpeed = iTime * uSpinSpeed;\n" +
                        "    speed = baseSpeed + mouseInfluence * 2.0;\n" +
                        "    \n" +
                        "    vec2 uv2 = vec2(uv.x + uv.y);\n" +
                        "    \n" +
                        "    for(int i = 0; i < 5; i++) {\n" +
                        "        uv2 += sin(max(uv.x, uv.y)) + uv;\n" +
                        "        uv += 0.5 * vec2(\n" +
                        "            cos(5.1123314 + 0.353 * uv2.y + speed * 0.131121),\n" +
                        "            sin(uv2.x - 0.113 * speed)\n" +
                        "        );\n" +
                        "        uv -= cos(uv.x + uv.y) - sin(uv.x * 0.711 - uv.y);\n" +
                        "    }\n" +
                        "    \n" +
                        "    float contrast_mod = (0.25 * uContrast + 0.5 * uSpinAmount + 1.2);\n" +
                        "    float paint_res = min(2.0, max(0.0, length(uv) * 0.035 * contrast_mod));\n" +
                        "    float c1p = max(0.0, 1.0 - contrast_mod * abs(1.0 - paint_res));\n" +
                        "    float c2p = max(0.0, 1.0 - contrast_mod * abs(paint_res));\n" +
                        "    float c3p = 1.0 - min(1.0, c1p + c2p);\n" +
                        "    float light = (uLighting - 0.2) * max(c1p * 5.0 - 4.0, 0.0) + uLighting * max(c2p * 5.0 - 4.0, 0.0);\n" +
                        "    \n" +
                        "    return (0.3 / uContrast) * uColor1 + (1.0 - 0.3 / uContrast) * (uColor1 * c1p + uColor2 * c2p + vec4(c3p * uColor3.rgb, c3p * uColor1.a)) + light;\n" +
                        "}\n" +
                        "\n" +
                        "void main() {\n" +
                        "    vec2 uv = vUv * iResolution.xy;\n" +
                        "    gl_FragColor = effect(iResolution.xy, uv);\n" +
                        "}";

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        time += 0.016f; // ~60fps
        GLES10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(program);
        //handle uniforms
        int resolutionHandle = GLES20.glGetUniformLocation(program, "iResolution");
        int timeHandle = GLES20.glGetUniformLocation(program, "iTime");
        int uSpinRotationHandle = GLES20.glGetUniformLocation(program, "uSpinRotation");
        int uSpinSpeedHandle = GLES20.glGetUniformLocation(program, "uSpinSpeed");
        int uOffsetHandle = GLES20.glGetUniformLocation(program, "uOffset");
        int uColor1Handle = GLES20.glGetUniformLocation(program, "uColor1");
        int uColor2Handle = GLES20.glGetUniformLocation(program, "uColor2");
        int uColor3Handle = GLES20.glGetUniformLocation(program, "uColor3");
        int uContrastHandle = GLES20.glGetUniformLocation(program, "uContrast");
        int uLightingHandle = GLES20.glGetUniformLocation(program, "uLighting");
        int uSpinAmountHandle = GLES20.glGetUniformLocation(program, "uSpinAmount");
        int uPixelFilterHandle = GLES20.glGetUniformLocation(program, "uPixelFilter");
        int uSpinEaseHandle = GLES20.glGetUniformLocation(program, "uSpinEase");
        int uIsRotateHandle = GLES20.glGetUniformLocation(program, "uIsRotate");
        int uMouseHandle = GLES20.glGetUniformLocation(program, "uMouse");

        if (resolutionHandle >= 0) {
            GLES20.glUniform3f(resolutionHandle,
                    (float) width,
                    (float) height,
                    (float) width / (float) height);
        }
        GLES20.glUniform1f(timeHandle, time);
        GLES20.glUniform1f(uSpinRotationHandle, spinRotation);
        GLES20.glUniform1f(uSpinSpeedHandle, spinSpeed);
        GLES20.glUniform2f(uOffsetHandle, offset[0], offset[1]);
        // Convert hex colors to RGBA
        float[] color1Rgba = hexToRgba(color1);
        float[] color2Rgba = hexToRgba(color2);
        float[] color3Rgba = hexToRgba(color3);
        GLES20.glUniform4f(uColor1Handle, color1Rgba[0], color1Rgba[1], color1Rgba[2], color1Rgba[3]);
        GLES20.glUniform4f(uColor2Handle, color2Rgba[0], color2Rgba[1], color2Rgba[2], color2Rgba[3]);
        GLES20.glUniform4f(uColor3Handle, color3Rgba[0], color3Rgba[1], color3Rgba[2], color3Rgba[3]);
        GLES20.glUniform1f(uContrastHandle, contrast);
        GLES20.glUniform1f(uLightingHandle, lighting);
        GLES20.glUniform1f(uSpinAmountHandle, spinAmount);
        GLES20.glUniform1f(uPixelFilterHandle, pixelFilter); // avoid div by zero
        GLES20.glUniform1f(uSpinEaseHandle, spinEase);
        GLES20.glUniform1i(uIsRotateHandle, isRotate); // false
        GLES20.glUniform2f(uMouseHandle, 0.5f, 0.5f);
        int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");

        float[] vertices = {
                -1f, -1f,
                1f, -1f,
                -1f,  1f,
                1f,  1f
        };

        java.nio.FloatBuffer vertexBuffer = java.nio.ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(java.nio.ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;
    }
    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}

