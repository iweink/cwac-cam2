package com.commonsware.cwac.cam2.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.commonsware.cwac.cam2.R;

public class TextViewFont extends TextView {
  private static final String LOG_TAG = TextViewFont.class.getSimpleName();

  public TextViewFont(Context context) {
    super(context);
  }

  public TextViewFont(Context context, AttributeSet attrs) {
    super(context, attrs);
    setCustomFont(context, attrs);
  }

  public TextViewFont(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setCustomFont(context, attrs);
  }

  private void setCustomFont(final Context ctx, AttributeSet attrs) {
    TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewFont);
    final String customFont = a.getString(R.styleable.TextViewFont_customFont);
    if (customFont == null) {
      return;
    }
    setCustomFont(ctx, customFont);
    a.recycle();
  }

  public boolean setCustomFont(Context ctx, String asset) {
    Typeface tf;
    try {
      tf = Typeface.createFromAsset(ctx.getAssets(), asset);
    } catch (Exception e) {
      Log.e(LOG_TAG, "Could not get typeface: " + Log.getStackTraceString(e));
      return false;
    }
    setTypeface(tf);
    return true;
  }
}