package com.koloce.kulibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.koloce.kulibrary.R;


/**
 * Created by koloces on 2019/6/22.
 */

public class RoundImageView extends ImageView {
    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private float[] rids = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,};

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView, defStyleAttr, 0);

        int round = a.getDimensionPixelSize(R.styleable.RoundImageView_round, -1);
        if (round != -1) {
            rids = new float[]{round, round, round, round, round, round, round, round,};
        } else {
            int topLeft = a.getDimensionPixelSize(R.styleable.RoundImageView_topLeftRound, 0);
            int topRight = a.getDimensionPixelSize(R.styleable.RoundImageView_topRightRound, 0);
            int bottomLeft = a.getDimensionPixelSize(R.styleable.RoundImageView_bottomLeftRound, 0);
            int bottomRight = a.getDimensionPixelSize(R.styleable.RoundImageView_bottomRightRound, 0);
            rids = new float[]{topLeft, topLeft, topRight, topRight, bottomLeft, bottomLeft, bottomRight, bottomRight,};
        }

    }


    /**
     * 画图
     * by Hankkin at:2015-08-30 21:15:53
     *
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
        path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
