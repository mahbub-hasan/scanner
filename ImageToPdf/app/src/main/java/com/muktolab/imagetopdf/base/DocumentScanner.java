package com.muktolab.imagetopdf.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.muktolab.imagetopdf.R;

import org.w3c.dom.DocumentType;

/**
 * TODO: document your custom view class.
 */
public class DocumentScanner extends SurfaceView implements SurfaceHolder.Callback {
    private Paint paintForCameraOutField;
    private Rectangle rectangle;


    public DocumentScanner(Context context) {
        super(context);
        init(null, 0);
    }

    public DocumentScanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DocumentScanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        paintForCameraOutField = new Paint();
        paintForCameraOutField.setColor(Color.RED);
        // Load attributes
        /*final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DocumentScanner, defStyle, 0);
        rectangle = PageSize.getRectangle(a.getString(R.styleable.DocumentScanner_documentType));

        a.recycle();*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
