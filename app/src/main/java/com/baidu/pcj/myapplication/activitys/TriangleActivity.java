package com.baidu.pcj.myapplication.activitys;

import android.app.Activity;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.baidu.pcj.myapplication.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleActivity extends Activity implements GLSurfaceView.Renderer {

    private GLSurfaceView mTriangle;
    private FloatBuffer vertexBuffer;

    // 设置颜色  RGBA
    float[] mColors = {
            1f, 0f, 0f, 0f,

            0f, 1f, 0f, 0f,

            0f, 0f, 1f, 0f};
    private float triangleCoords[] = {  // 正三角形的三个顶点坐标
            // 顺序分别为  顶点  left  right
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // left
            0.5f, -0.5f, 0.0f};// right

    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    static final int COORDS_PER_VERTEX = 3;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    // 顶点着色器
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";
    // 片元着色器
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    private int Programe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle);
        mTriangle = findViewById(R.id.gl_trangle);
        initData();
    }

    private void initData() {
        mTriangle.setEGLContextClientVersion(2);
        mTriangle.setRenderer(this);
        mTriangle.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    // surface创建时被调用
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
          /*------ Log  start------*/
        Log.e("pcj", "运行了这个onSurfaceCreated method");
         /*------ Log  end------*/
        // 设置清屏颜色 (此为白色)
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        // 申请缓冲区底层空间  *4是因为一个float占用4个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());//设置字节顺序
        vertexBuffer = bb.asFloatBuffer();// 转换为Float型缓冲
        vertexBuffer.put(triangleCoords);//向缓冲区中放入顶点坐标数据
        vertexBuffer.position(0);//设置缓冲区起始位置

        //  设置顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        //  设置片元着色器
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        //  创建一个空的opengl 程序
        Programe = GLES20.glCreateProgram();
        // 加入片元着色器 and 顶点着色器  并连接程序
        GLES20.glAttachShader(Programe, fragmentShader);
        GLES20.glAttachShader(Programe, vertexShader);
        GLES20.glLinkProgram(Programe);
    }

    //surface改变时被调用
    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
         /*------ Log  start------*/
        Log.e("pcj", "运行了这个onSurfaceChanged method");
         /*------ Log  end------*/
        GLES20.glViewport(0, 0, i, i1);
    }


    private int mPositionHandler;// vPosition
    private int mColorHander;  // vColor

    // surface绘制时被调用
    @Override
    public void onDrawFrame(GL10 gl10) {
        //  这个必须加上 设置清除屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //  将程序加入到opengl 环境
        GLES20.glUseProgram(Programe);
        // 取顶点着色器的句柄  vPosition
        mPositionHandler = GLES20.glGetAttribLocation(Programe, "vPosition");
        // 取片元着色器的句柄vColor
        mColorHander = GLES20.glGetUniformLocation(Programe, "vColor");
        // 启用顶点着色器的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        // 三角形坐标数据  参数
        GLES20.glVertexAttribPointer(mPositionHandler, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        // 绘制颜色
        GLES20.glUniform4fv(mColorHander, 1, mColors, 0);
        // 绘制点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandler);

    }

    public int loadShader(int type, String shaderCode) {
        //根据type创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(type);
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

}
