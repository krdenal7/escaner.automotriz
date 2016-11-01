package com.kds.elm.escanerautomotriz.CustomViews;

/**
 * Created by isaac on 09/07/2016.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Isaac Martinez on 06/07/2016.
 * APP's
 */
public class VelocimetroLinear extends View {


    private Paint paintLine;
    private Paint paintGraphics;

    private double velocidadMaxima=10000;
    private double velocidad=0;


    public VelocimetroLinear(Context context) {
        super(context);
    }

    public VelocimetroLinear(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    private void onDrawLinears(Canvas canvas){
        int ancho = canvas.getWidth();

        paintLine= new Paint();
        paintLine.setShader(new LinearGradient(0,120,ancho,120,Color.GREEN,Color.RED, Shader.TileMode.MIRROR));
        //canvas.drawLine(0, 120, ancho, 120, paintLine);
        RectF oval3 = new RectF(100, 130,ancho-100, 140);
        canvas.drawOval(oval3,paintLine);
        paintGraphics=new Paint();
        paintGraphics.setColor(Color.WHITE);

        int valPosXDown=0; //Indica el punto en el que inicia la linea en la parte de abajo
        int valPosXUp=0;    //Indica el punto en el que inicia la linea en la parte de arriba
        int valPosYDown=100;
        int valPosYUp=100;
        int stroke=5;
        float val=getMeasuredWidth()/20;
        Double progres=velocidad*20/velocidadMaxima;
        int valDegrade=5;


        for(int i=0;i<20;i++){

            paintGraphics.setStrokeWidth(stroke);
            if(velocidad!=0){
                if(i<=progres.intValue()){
                    paintGraphics.setColor(getColor(i*valDegrade));
                }else if(velocidad<16){
                    if(i==1){
                        paintGraphics.setColor(getColor(i*valDegrade));
                    }else {
                        paintGraphics.setColor(Color.WHITE);
                    }
                }else
                {
                    paintGraphics.setColor(Color.WHITE);
                }
            }else {
                paintGraphics.setColor(Color.WHITE);
            }

            canvas.drawLine(valPosXDown, valPosYUp, valPosXUp, valPosYDown, paintGraphics);
            //Realiza la suma para la separación entre las lineas
            valPosXDown+=val;
            valPosXUp+=val;
            //Realiza la suma para ir de menor a mayor en el tamaño
            valPosYDown-=25;
            valPosYDown+=15;
            stroke+=1;
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

        return Color.rgb(((255 * val) / 100),(255 * (100 - val)) / 100,0);

    }

    private int colorTransparent(){

        int[] colores=new int[20];
        colores[0]= Color.parseColor("#4CFF88");

        return 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawLinears(canvas);

    }

    public double getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public void setVelocidadMaxima(double velocidadMaxima) {
        this.velocidadMaxima = velocidadMaxima;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
        invalidate();
    }


}

