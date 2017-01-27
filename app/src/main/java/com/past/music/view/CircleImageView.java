package com.past.music.view;/**
 * Created by gaojin on 2017/1/26.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/26 下午7:21
 * 版本：
 * 描述：圆形头像
 * 备注：专辑封面
 * =======================================================
 */
public class CircleImageView extends ImageView {

    //view的宽度
    private int mWidth;

    //圆形的半径
    private int mRadius;

    //位图着色器
    private BitmapShader mBitmapShader;

    //矩阵 用于缩图片以适应view的大小
    private Matrix mMatrix;

    //圆形图像的Paint
    private Paint mBitmapPaint;


    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inial(context, attrs);
    }

    private void inial(Context context, AttributeSet attrs) {
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);    //反锯齿
    }

    /**
     * 强制设置View的高度，因为是圆形，所以宽和高一样
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //view的大小
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        mRadius = mWidth / 2;
        setMeasuredDimension(mWidth, mWidth);
    }

    /**
     * 设置BitmapShader以及Paint
     */
    public void setBitmapShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        //图片转换为bitmap
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();

        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        int bitmapSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        //计算view的大小和图片大小的比例
        scale = mWidth * 1.0f / bitmapSize;
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);     去掉父类的onDraw()
        if (getDrawable() == null) {
            return;
        }
        //绘制内部圆形图片
        setBitmapShader();
        canvas.drawCircle(mWidth / 2, mWidth / 2, mRadius, mBitmapPaint);
    }
}
