package a3d.demo.com.blogthree3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.ContentValues.TAG;

public class MySurfaceView extends GLSurfaceView {
    private SceneRenderer mRenderer;
    public Context context;
    private int one = 0x00010000;
    private Bitmap bitmap;
    private int[] textureids;
    private IntBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private IntBuffer texBuffer;
    // 旋转方向
    private float xrot, yrot, zrot;
    // 正方体顶点
    private int[] vertices = {
       //右
       one,-one,one,
       one,one,one,
       one,-one,-one,
       one,one,-one,
       //左
       -one,-one,one,
       -one,one,one,
       -one,-one,-one,
       -one,one,-one,
       //下
       -one,-one,one,
       one,-one,one,
       -one,-one,-one,
       one,-one,-one,
       //上
       one,one,one,
       -one,one,one,
       one,one,-one,
       -one,one,-one,
       //前
       -one,-one,one,
       -one,one,one,
       one,-one,one,
       one,one,one,
       //后
       -one,-one,-one,
       -one,one,-one,
       one,-one,-one,
       one,one,-one
    };

    //  颜色数组
    float []  cubeColors = {
            1f,0f,0f,1f ,
            0f,1f,0f,1f,
            0f,0f,1f,1f,
            1f,0f,0f,1f,

            1f,0f,0f,1f ,
            0f,1f,0f,1f,
            0f,0f,1f,1f,
            1f,0f,0f,1f,

            1f,0f,0f,1f ,
            0f,1f,0f,1f,
            0f,0f,1f,1f,
            1f,0f,0f,1f,

            1f,0f,0f,1f ,
            0f,1f,0f,1f,
            0f,0f,1f,1f,
            1f,0f,0f,1f,


            1f,0f,0f,1f ,
            0f,1f,0f,1f,
            0f,0f,1f,1f,
            1f,0f,0f,1f,

            1f,0f,0f,1f ,
            0f,1f,0f,1f,
            0f,0f,1f,1f,
            1f,0f,0f,1f,
    };


    //纹理点
    private int[] texCoords = {
            0, one,
            one, one,
            0, 0,
            one, 0
    };
    public MySurfaceView(Context context) {
        super(context);
        //设置自定义渲染器完成视图绑定
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        //设置为帧绘制模式，持续调用完成
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        // 初始化
        textureids = new int[1];
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mofang3);
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asIntBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer colorVbb = ByteBuffer.allocateDirect(cubeColors.length * 4);
        colorVbb.order(ByteOrder.nativeOrder());
        colorBuffer = colorVbb.asFloatBuffer();
        colorBuffer.put(cubeColors);
        colorBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4 * 6);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asIntBuffer();
        //为每一个面贴上纹理
//        for (int i = 0; i < 6; i++) {
//            texBuffer.put(texCoords);
//        }
        texBuffer.position(0);
    }
    private class SceneRenderer implements Renderer {
        Celestial celestialSmall;
        Celestial celestialBig;
        int i = 0;
        @Override
        public void onDrawFrame(GL10 gl) {
            if(i==0) {
                Log.i(TAG, "onDrawFrame: ");
                i++;
            }
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glDisable(GL10.GL_LIGHTING);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0f, -3.6f);
            gl.glPushMatrix();
            gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 3.5f);
            gl.glLoadIdentity();
            //启用顶点数据
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            // 启用顶点颜色数据
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
            gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, texBuffer); // Define
            gl.glPushMatrix();
            //向z轴里移入6.0f
            gl.glTranslatef(0.0f, 0.0f, -6.0f);
            // 设置3个方向的旋转
            gl.glRotatef(xrot, one, 0.0f, 0.0f);
            gl.glRotatef(yrot, 0.0f, one, 0.0f);
            gl.glRotatef(zrot, 0.0f, 0.0f, one);
            //缩放
            gl.glScalef(0.75f,0.75f, 0.75f);
            // 绘制正方体
            for (int i = 0; i < 6; i++) {
                gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,i*4, 4);
            }
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            // 设置旋转角度
            xrot += 0.5f;
            yrot += 0.6f;
            zrot += 0.3f;
            gl.glPopMatrix();
            gl.glPushMatrix();
            gl.glTranslatef(0, -24.0f, 0.0f);
//            celestialSmall.drawSelf(gl);
//            celestialBig.drawSelf(gl);
            gl.glPopMatrix();
        }
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.i(TAG, "onSurfaceChanged: ");
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            float ratio = (float) width / height;
            gl.glFrustumf(-ratio * 0.5f, ratio * 0.5f, -0.5f, 0.5f, 1, 100);
        }
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.i(TAG, "onSurfaceCreated: ");
            gl.glDisable(GL10.GL_DITHER);
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
            gl.glClearColor(0, 0, 0, 0);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glEnable(GL10.GL_DEPTH_TEST);
//            gl.glEnable(GL10.GL_CULL_FACE);
//            celestialSmall = new Celestial(0, 0, 3, 0, 1250);
//            celestialBig = new Celestial(0, 0, 5, 0,800);
//            gl.glEnable(GL10.GL_TEXTURE_2D);
//            // 创建纹理
//            gl.glGenTextures(1, textureids, 0);
//            // 绑定要使用的纹理
//            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[0]);
//            // 生成纹理
//            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
//            // 线性滤波
//            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
//                    GL10.GL_LINEAR);
//            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
//                    GL10.GL_LINEAR);
//            new Thread() {
//                public void run() {
//                    while (true) {
//                        celestialSmall.yAngle += 0.5;
//                        if (celestialSmall.yAngle >= 360) {
//                            celestialSmall.yAngle = 0;
//                        }
//                        celestialBig.yAngle += 0.5;
//                        if (celestialBig.yAngle >= 360) {
//                            celestialBig.yAngle = 0;
//                        }
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
        }
    }
}
