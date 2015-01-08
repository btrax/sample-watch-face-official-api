package com.btrax.newwatch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;

public class MyWatchFaceService extends CanvasWatchFaceService {

    private final static String TAG = MyWatchFaceService.class.getSimpleName();

    /**
     * provide your watch face implementation
     * CanvasWatchFaceService.Engineを継承したクラスを返す
     */
    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    /**
     * implement service callback methods
     */
    private class Engine extends CanvasWatchFaceService.Engine {
        float HOUR_HAND_LENGTH = 80.0f;
        final float MINUTE_HAND_LENGTH = 120.0f;
        final int[] BACKGROUND_RES_ID = {
                R.drawable.image_0,
                R.drawable.image_1,
                R.drawable.image_2,
                R.drawable.image_3,
        };

        Paint mHourPaint;
        Paint mMinutePaint;
        Bitmap mBackgroundBitmap;
        Bitmap mBackgroundScaledBitmap;

        Time mTime;

        /**
         * initialize
         */
        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            Log.d(TAG, "onCreate");
            setWatchFaceStyle(new WatchFaceStyle.Builder(MyWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            mHourPaint = new Paint();
            mHourPaint.setStrokeWidth(5f);
            mHourPaint.setAntiAlias(true);
            mHourPaint.setStrokeCap(Paint.Cap.ROUND);

            mMinutePaint = new Paint();
            mMinutePaint.setStrokeWidth(3f);
            mMinutePaint.setAntiAlias(true);
            mMinutePaint.setStrokeCap(Paint.Cap.ROUND);

            mTime = new Time();
        }

        /**
         * the time changed. called every minute.
         * 毎分実行される
         */
        @Override
        public void onTimeTick() {
            super.onTimeTick();
            Log.d(TAG, "onTimeTick: ambient = " + isInAmbientMode());
            invalidate();
        }

        /**
         * called when ambient mode changed.
         * アンビエントモード(スクリーンオフ状態)が切り替わったときに呼ばれる
         */
        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            Log.d(TAG, "onAmbientModeChanged: " + inAmbientMode);
            invalidate();
        }

        /**
         * draw watch face
         * 盤面を描写する
         */
        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setToNow();

            int width = bounds.width();
            int height = bounds.height();
            float centerX = width / 2f;
            float centerY = height / 2f;

            // draw background
            // 背景の描写
            Resources resources = MyWatchFaceService.this.getResources();
            int imgResId;
            if (isInAmbientMode()) {
                imgResId = R.drawable.black_background;
            } else {
                imgResId = BACKGROUND_RES_ID[mTime.minute % BACKGROUND_RES_ID.length];
            }
            Drawable backgroundDrawable = resources.getDrawable(imgResId);
            mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();
            mBackgroundScaledBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap,
                    width, height, true /* filter */);
            canvas.drawBitmap(mBackgroundScaledBitmap, 0, 0, null);

            // draw minute hand
            // 長針の描画
            float minRot = mTime.minute / 30f * (float) Math.PI;
            if (isInAmbientMode()) {
                mMinutePaint.setARGB(255, 200, 200, 200);
            } else {
                mMinutePaint.setARGB(255, 255, 255, 255);
            }
            float minX = (float) Math.sin(minRot) * MINUTE_HAND_LENGTH;
            float minY = (float) -Math.cos(minRot) * MINUTE_HAND_LENGTH;
            canvas.drawLine(centerX, centerY, centerX + minX, centerY + minY, mMinutePaint);

            // draw hour hand
            // 短針の描画
            float hrRot = ((mTime.hour + (mTime.minute / 60f)) / 6f) * (float) Math.PI;
            if (isInAmbientMode()) {
                mHourPaint.setARGB(255, 200, 200, 200);
            } else {
                mHourPaint.setARGB(255, 255, 0, 0);
            }
            float hrX = (float) Math.sin(hrRot) * HOUR_HAND_LENGTH;
            float hrY = (float) -Math.cos(hrRot) * HOUR_HAND_LENGTH;
            canvas.drawLine(centerX, centerY, centerX + hrX, centerY + hrY, mHourPaint);
        }
    }
}
