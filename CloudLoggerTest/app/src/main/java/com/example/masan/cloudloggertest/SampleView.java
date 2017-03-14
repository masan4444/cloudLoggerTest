package com.example.masan.cloudloggertest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by masan on 2016/12/24.
 */

public class SampleView extends View {

    public SampleView(Context context) {
        super(context);
    }
    protected float v = 0;

    Paint paint = new Paint();


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        this.drawOption(canvas);
    }

    public void DrawAll(){

    }

    protected void drawOption(Canvas canvas) {
        //Paint paint1 = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setTextSize(100);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        canvas.drawText(v + " a", 400, 400, paint);
    }

    public void setValue(float v){
        this.v = v;
    }
}
