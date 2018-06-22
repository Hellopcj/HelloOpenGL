package com.baidu.pcj.myapplication.activitys;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.pcj.myapplication.R;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by puchunjie .
 * 正三角形
 * 使用到变量矩阵
 */

public class RegularTriangle extends Activity implements GLSurfaceView.Renderer {

    private GLSurfaceView mView;

    private FloatBuffer verteBuffer;

    private int mProcess;
    private int mPositionHandler; //  顶点
    private int mColorHandler; // 颜色 片元


    // 增加Matrix 矩阵变换   顶点着色器code
    private final String verTextShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    "    gl_Position = vMatrix*vPosition;" +
                    "}";

    // 片元
    private final String vertextFragmentCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_triangle);
        mView = findViewById(R.id.gl_view);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }

}
