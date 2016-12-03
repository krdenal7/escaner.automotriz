package com.kds.elm.escanerautomotriz.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */

public class ViewGasolina extends View {

    private double TamGasolina=100;
    private double cantidad =1;

    public ViewGasolina(Context context) {
        super(context);
    }

    public ViewGasolina(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") Paint paint=new Paint();

        paint.setStrokeWidth(6);

        int increment=15;
        int tam=50;
        if(cantidad==0){
            paint.setColor(Color.WHITE);
        }

        Double porcentaje=cantidad*10/TamGasolina;

        for (int i=0;i<10;i++){

            if(i<porcentaje){
                paint.setColor(getColor(i));
            }else {
                paint.setColor(Color.WHITE);
            }
            canvas.drawLine(
                    getWidth()-10,  //Este es para la separación del punto derecho
                    getHeight()-increment, //Este es para que suba el punto derecho
                    getWidth()-tam,
                    getHeight()-increment, //Este es para que suba el punte izquierdo
                    paint);
            increment+=10;
            tam+=5;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth = 100;
        int desiredHeight = 140;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    private int getColor(int val){
        String[] color=new String[]{
                "#DF0101",
                "#FF0000",
                "#FF4000",
                "#FF8000",
                "#FFBF00",
                "#FFFF00",
                "#BFFF00",
                "#80FF00",
                "#40FF00",
                "#00FF00"};
        return Color.parseColor(color[val]);
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
        invalidate();
    }
}

