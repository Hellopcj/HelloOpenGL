package com.baidu.pcj.myapplication.activitys;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.pcj.myapplication.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 *  正方形和圆形
 */
public class RoundActivity extends Activity implements GLSurfaceView.Renderer{


   private GLSurfaceView mView;
   private int mPrograme;
   private int COORDS_PER_VERTRIX_ = 4;

    /**
     * 顶点坐标  正方形
     */
    static float triangleCoords[] = {
            -0.5f,  0.5f, 0.0f, // top left
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f, // bottom right
            0.5f,  0.5f, 0.0f  // top right
    };

    /**
     * 顶点坐标  圆形
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        mView = findViewById(R.id.gl_circle_view);
        mView.setRenderer(this);
        mView.setEGLContextClientVersion(2);//  egls20  gl版本号
        mView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
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
