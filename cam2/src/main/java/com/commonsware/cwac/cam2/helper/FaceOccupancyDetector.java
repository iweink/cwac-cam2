package com.commonsware.cwac.cam2.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.util.ArrayList;
import java.util.List;

public class FaceOccupancyDetector {
  public OccupancyResult isFacePresentWithMinimumOccupancy(Context context, Bitmap bitmap,
                                                           float minimumOccupancyPercentage) {
    if (bitmap == null) return OccupancyResult.NO_FACE;
    List<Face> faces = getFaces(context, bitmap, true);
    if (faces.size() == 0) return OccupancyResult.NO_FACE;
    if (faces.size() != 1) return OccupancyResult.MANY_FACES;
    double areaOfImage = bitmap.getWidth()*bitmap.getHeight();
    double areaOfFace = faces.get(0).getHeight() * faces.get(0).getWidth();
//    Log.e("Percentage Occupied", ""+(areaOfFace/areaOfImage));
//    Log.e("Percentage Required", ""+(percent/100));
//    Log.e("Response", "" + ((areaOfFace/areaOfImage) > (percent/100)));
    return (areaOfFace/areaOfImage) > (minimumOccupancyPercentage/100)
        ? OccupancyResult.FACE_WITH_CONDITION
        : OccupancyResult.FACE_WITHOUT_CONDITION;
  }

  public List<Face> getFaces(Context context, Bitmap bitmap, boolean singleFace) {
    SafeFaceDetector detector = new SafeFaceDetector(new FaceDetector
        .Builder(context)
        .setLandmarkType(FaceDetector.NO_LANDMARKS)
        .setTrackingEnabled(false)
        .setProminentFaceOnly(singleFace)
        .build());
    List<Face> result = new ArrayList<>();
    if (!detector.isOperational()) return result;
    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
    SparseArray<Face> faces = detector.detect(frame);
    for (int i=0; i<faces.size(); i++) {
      result.add(faces.valueAt(i));
    }
    detector.release();
    return result;
  }
}
