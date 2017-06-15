package com.commonsware.cwac.cam2.helper;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

public class ImageHelper {
  public Bitmap flipImage(Bitmap original) {
    Matrix m = new Matrix();
    m.preScale(-1, 1);
    Bitmap flipped = Bitmap
        .createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), m, false);
    flipped.setDensity(DisplayMetrics.DENSITY_DEFAULT);
    return flipped;
  }
}
