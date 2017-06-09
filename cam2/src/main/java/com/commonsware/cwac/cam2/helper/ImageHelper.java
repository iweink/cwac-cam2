package com.commonsware.cwac.cam2.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.util.ArrayList;
import java.util.List;


public class ImageHelper {
  public boolean isFaceOccupancy(Context context, Bitmap bitmap, float percent) {
    if (bitmap == null) return false;
    List<Face> faces = getFaces(context, bitmap);
    if (faces.size() != 1) return false;
    double areaOfImage = bitmap.getWidth()*bitmap.getHeight();
    double areaOfFace = faces.get(0).getHeight() * faces.get(0).getWidth();
//    Log.e("Percentage Occupied", ""+(areaOfFace/areaOfImage));
//    Log.e("Percentage Required", ""+(percent/100));
//    Log.e("Response", "" + ((areaOfFace/areaOfImage) > (percent/100)));
    return (areaOfFace/areaOfImage) > (percent/100);
  }

  public List<Face> getFaces(Context context, Bitmap bitmap) {
    FaceDetector detector = new FaceDetector
        .Builder(context)
        .setTrackingEnabled(false)
        .build();
    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
    SparseArray<Face> faces = detector.detect(frame);
    List<Face> result = new ArrayList<>();
    for (int i=0; i<faces.size(); i++) {
      result.add(faces.valueAt(i));
    }
    detector.release();
    return result;
  }
}
