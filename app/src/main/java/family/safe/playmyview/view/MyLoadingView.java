package family.safe.playmyview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import family.safe.playmyview.R;

/**
 * Created by Administrator on 2016/6/24.
 */
public class MyLoadingView extends View {

    private int mInterval = 1;
    private int mCircleColor;//圆的颜色
    private int mCircleCount = 3;
    private Paint paint; //背景画笔
    private int circleRadio;//圆的半径
    private int width;//控件宽度
    private Paint paint2;//白点画笔
    private int witePoint = -1;//白点数量
    private static boolean isVisit = true;
    private int height;

    public MyLoadingView(Context context) {
        this(context, null);
    }

    public MyLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyLoadingView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MyLoadingView_interval://更新时间
                    mInterval = typedArray.getInt(attr, 3);
                    break;
                case R.styleable.MyLoadingView_circleColor://圆的颜色
                    mCircleColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyLoadingView_circleCount://圆的数量
                    mCircleCount = typedArray.getInt(attr, 3);
                    if (mCircleCount < 3) {
                        mCircleCount = 3;
                    }
                    break;
            }
        }
        typedArray.recycle();
        setPaint();
        new Thread() {
            @Override
            public void run() {
                while (isVisit) {
                    SystemClock.sleep(mInterval * 1000);
                    postInvalidate();
                    Log.e("hlk", "ThreadThreadThreadThreadThread");
                }
            }
        }.start();
    }

    public void dismiss(){

    }

    private void setPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mCircleColor);
        paint.setAlpha(125);
        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(mCircleColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getVisibility() != View.VISIBLE) {
            isVisit = false;
        } else {
            isVisit = true;
        }
        witePoint++;
        FPoint[] getpotions = getpotions(mCircleCount, width);
        for (int i = 0; i < mCircleCount; i++) {
            canvas.drawCircle(getpotions[i].x, getpotions[i].y, circleRadio, paint);
        }
        for (int i = 0; i < witePoint; i++) {
            canvas.drawCircle(getpotions[i].x, getpotions[i].y, circleRadio, paint2);
        }
        if (witePoint == mCircleCount) {
            witePoint = -1;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        if (width / mCircleCount > height) {
            //高的一半为圆的半径
            circleRadio = height / 2;
        }
        if (width / mCircleCount < height) {
            circleRadio = width / mCircleCount / 2;
        }
        if (width / mCircleCount == height) {
            circleRadio = height / 2;
        }
    }

    class FPoint {
        public float x;
        public float y;

        public FPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }


    private FPoint[] getpotions(int circleCount, int width) {
        FPoint[] points = new FPoint[circleCount];

        float spaceNumber = circleCount - 1;
        float allSpaceWidth = width - circleCount * circleRadio * 2;
        float potionSpace = allSpaceWidth / spaceNumber;//圆间距
        for (int i = 0; i < circleCount; i++) {
            if (i == circleCount - 1) {
                points[i] = new FPoint(width - circleRadio, height / 2);
            } else if (i == 0) {
                points[i] = new FPoint(circleRadio, height / 2);
            } else {
                points[i] = new FPoint(points[i - 1].x + circleRadio * 2 + potionSpace, height / 2);
            }
        }
        return points;
    }
}
