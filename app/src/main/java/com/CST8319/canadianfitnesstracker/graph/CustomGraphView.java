package com.CST8319.canadianfitnesstracker.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Modifier;

//custome view for the graph https://www.youtube.com/watch?v=sb9OEl4k9Dk -AV
public class CustomGraphView extends View {

    private RectF rectangle[];
    private Paint color;
    private float rectangleWidth;
    private float rectangleHeight;
    private boolean height = false;


    public CustomGraphView(Context context) {
        super(context);
        init(null);
    }

    public CustomGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void setHeight(int inbound)
    {
        rectangleHeight=inbound;
        height= true;

        int w = getHeight();
        int h = getHeight();

        Float graphSize = (7 * rectangleWidth) ;
        Float border = (w- graphSize)/8;

        for (int i=0; i<7; i++)
        {

            Float left = (i+1)*border + i * rectangleWidth;
            Float top = h- rectangleHeight;
            Float right = left + rectangleWidth;
            Float bottom = (float) h;


            rectangle[i].set(left, top, right, bottom);
        }

        postInvalidate();
    }

    private void init(@Nullable AttributeSet set)
    {
        rectangle = new RectF[7];
        for (int i=0; i<7; i++)
        {
            rectangle[i]= new RectF();
        }


        color = new Paint(Paint.ANTI_ALIAS_FLAG);

    }
// on size cvhanges https://developer.android.com/reference/android/view/View#onSizeChanged(int,%20int,%20int,%20int)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (!height)
        {
            rectangleWidth = w * 0.12f;
            rectangleHeight = h * 0.5f;
        }
        else
        {
            rectangleWidth = w * 0.12f;
        }


        Float graphSize = (7 * rectangleWidth) ;
        Float border = (w- graphSize)/8;

        for (int i=0; i<7; i++)
        {

            Float left = (i+1)*border + i * rectangleWidth;
            Float top = h- rectangleHeight;
            Float right = left + rectangleWidth;
            Float bottom = (float) h;


            rectangle[i].set(left, top, right, bottom);
        }
    }

    //how to draw objects https://developer.android.com/develop/ui/compose/graphics/draw/overview doesnt rly help too much except visualizing what the top bottom left right mean
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawColor(Color.BLUE);

        color.setColor(Color.RED);

        for (int i=0; i<7; i++)
        {
            canvas.drawRect(rectangle[i], color);
        }

    }
}
