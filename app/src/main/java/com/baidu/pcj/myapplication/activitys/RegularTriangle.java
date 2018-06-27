package com.baidu.pcj.myapplication.activitys;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.pcj.myapplication.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by puchunjie .
 * 等腰直角三角形
 * 使用到变量矩阵
 */

public class RegularTriangle extends Activity implements GLSurfaceView.Renderer {

    private GLSurfaceView mView;

    // buffer缓冲
    private FloatBuffer verteBuffer;

    private int mProgram;

    private int mMatrixHandler; // 矩阵
    private int mPositionHandle;
    private int mColorHandle;

    // 顶点坐标  遵循右手坐标
    private float trianglecoods[] = {
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    // 顶点个数
    private final int COORS_PER_VERTRIX = 3;
    // 顶点偏移量
    private final int vertrixStride = COORS_PER_VERTRIX * 4;
    private final int VERTRIX_COUNT = trianglecoods.length / COORS_PER_VERTRIX;

    //设置颜色，依次为红绿蓝和透明通道
    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};

    // 增加Matrix 矩阵变换   顶点着色器code
    private final String verTextShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    "    gl_Position = vMatrix*vPosition;" +
                    "}";

    // 片元 code
    private final String vertextFragmentCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    // 矩阵数组 设置相机相关
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16]; // 计算后的矩阵 容器


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_triangle);
        mView = findViewById(R.id.gl_view);

        mView.setEGLContextClientVersion(2);
        mView.setRenderer(this);
        // 自动模式和脏模式（脏模式：用户需要重绘的时候 需要主动调用 类似于自定义view的invalidate（）方法 节省cpu）
        mView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    private void triglewithshader() {
        // 将顶点坐标转 输入到字节缓冲区
        ByteBuffer bb = ByteBuffer.allocateDirect(trianglecoods.length * 4);
        bb.order(ByteOrder.nativeOrder());// 设置字节顺序
        verteBuffer = bb.asFloatBuffer();
        verteBuffer.put(trianglecoods);
        verteBuffer.position(0);

        int vertrixShader = loadShader(GLES20.GL_VERTEX_SHADER,
                verTextShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                vertextFragmentCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertrixShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
     /* --  设置清屏颜色（黑色）可以不设置  -- */
        // 设置清屏颜色 (此为白色)
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        triglewithshader();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        //  作用绘制后的图形的可见区域  （绘制后的图形放在哪个具体的位置）i:width i1:heigth
        GLES20.glViewport(0,0,i,i1);
        // 计算宽高比
        float ratio = (float) i / i1;
        // 设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        // 设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // 计算变换矩阵
        Matrix.multiplyMV(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //  这个必须加上 设置清除屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        // 绘制
        ondraw();

    }

    private void ondraw() {
        // program应用到GLES环境
        GLES20.glUseProgram(mProgram);
        // 获取 矩阵 句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        // 顶点psitiosn 句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // 片元颜色  句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");


        // 指定matrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);
        //  三角形顶点坐标处理  并启用
        GLES20.glVertexAttribPointer(mPositionHandle, COORS_PER_VERTRIX, GLES20.GL_FLOAT, false, vertrixStride, verteBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 设置三角形颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        //绘制三角形  并禁止顶点数组的句柄
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTRIX_COUNT);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }


    private int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;

    }

}
