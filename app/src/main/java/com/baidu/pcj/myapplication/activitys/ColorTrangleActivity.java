package com.baidu.pcj.myapplication.activitys;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;

import com.baidu.pcj.myapplication.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 彩色三角形  等腰 使用matrix
 */
public class ColorTrangleActivity extends Activity implements GLSurfaceView.Renderer {

    private GLSurfaceView mGlColorTrangleView;

    // 顶点着色器
    private final String vertexShaderCode = "attribute vec4 vPosition;" +
            "uniform mat4 vMatrix;" +
            "void main() {" +
            "    gl_Position = vMatrix*vPosition;" +
            "}";

    private final String vertexColorShanderCode = "attribute vec4 vPosition;" +
            "uniform mat4 vMatrix;" +
            "varying  vec4 vColor;" +
            "attribute vec4 aColor;" +
            "void main() {" +
            "  gl_Position = vMatrix*vPosition;" +
            "  vColor=aColor;" +
            "}";

    // 片元着色器
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    //  三角形的三个顶点
    private float TriangleCoors[] = {
            0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f
    };

    private static final int COORDS_PER_VERTEX = 3;
    private int mPrograms;


    private int vPositionsHandler;
    private int vColorHandler;

    private float[] mviewMatrix = new float[16];
    private float[] mProjrctMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_trangle);
        mGlColorTrangleView = findViewById(R.id.gl_color_view);
        mGlColorTrangleView.setEGLContextClientVersion(2);
        mGlColorTrangleView.setRenderer(this);
        mGlColorTrangleView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //  设置背景颜色
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        // 透视投影 Matrix.frustumM();


    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        // 设置清除屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // 计算宽高比例
        int ratio = i / i1;


    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }


    private int setShader(int type, String code) {
        int shader = 0;
        //   GLES20.glAttachShader(type,code);

        return shader;
    }

}
