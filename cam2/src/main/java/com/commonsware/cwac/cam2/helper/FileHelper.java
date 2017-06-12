package com.commonsware.cwac.cam2.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class FileHelper {
  public File getFile(String path) {
    File file = new File(path);
    if (file.exists()) return file;
    return null;
  }

  public Bitmap getBitmap(String photoPath) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    return BitmapFactory.decodeFile(photoPath, options);
  }
}
