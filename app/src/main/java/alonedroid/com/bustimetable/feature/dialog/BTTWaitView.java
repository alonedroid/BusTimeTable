package alonedroid.com.bustimetable.feature.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import alonedroid.com.bustimetable.R;

/**
 * 処理中画面を表示します。
 */
public class BTTWaitView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    /**
     * View本体
     */
    private SurfaceHolder mSurfaceHolder;
    /**
     * メインスレッド
     */
    private Thread mThread;
    /**
     * アニメーションする画像一覧
     */
    private static int[] image = new int[]{R.mipmap.wait_1, R.mipmap.wait_2, R.mipmap.wait_3, R.mipmap.wait_4};

    /**
     * コンストラクタ
     */
    public BTTWaitView(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mThread = null;
    }

    @Override
    public void run() {
        // init
        Bitmap bmp;
        Canvas canvas;
        int i = 0;
        while (mThread != null) {
            // animation
            try {
                // canvas lock
                canvas = mSurfaceHolder.lockCanvas();

                // image change
                bmp = BitmapFactory.decodeResource(getResources(), image[i % image.length]);
                float m_posX = (getWidth() - bmp.getWidth()) * 0.5f;
                float m_posY = (getHeight() - bmp.getHeight()) * 0.5f;
                canvas.drawBitmap(bmp, m_posX, m_posY, null);

                // canvas unlock
                mSurfaceHolder.unlockCanvasAndPost(canvas);

                Thread.sleep(500);

                i++;
                bmp.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
