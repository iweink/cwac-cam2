package com.commonsware.cwac.cam2.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.commonsware.cwac.cam2.model.RectangleModel;

import java.util.ArrayList;
import java.util.List;

public class RectangleImageView extends ImageView {
  private List<RectangleModel> rectangles = new ArrayList<>();
  private int bitmapHeight = 0;
  private int bitmapWidth = 0;

  public RectangleImageView(Context context) {
    super(context);
    initialize();
  }

  public RectangleImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public void initialize() {
    setDrawingCacheEnabled(true);
  }

  public void addAll(List<RectangleModel> rectangles) {
    this.rectangles.addAll(rectangles);
    invalidate();
  }

  public void clean() {
    this.rectangles.clear();
  }

  @Override
  public void setImageBitmap(Bitmap bm) {
    super.setImageBitmap(bm);
    bitmapHeight = bm.getHeight();
    bitmapWidth = bm.getWidth();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Paint paint = new Paint();
    paint.setColor(Color.RED);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(5);
    float scaleHeightFactor = ((float) canvas.getHeight()) / bitmapHeight;
    float scaleWidthFactor = ((float) canvas.getWidth()) / bitmapWidth;
    for (RectangleModel rectangle : rectangles) {
      canvas.drawRect(
          rectangle.left * scaleWidthFactor, rectangle.top * scaleHeightFactor,
          rectangle.right * scaleWidthFactor, rectangle.bottom * scaleHeightFactor,
          paint);
    }
  }
}
