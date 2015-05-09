/***
 Copyright (c) 2015 CommonsWare, LLC

 Licensed under the Apache License, Version 2.0 (the "License"); you may
 not use this file except in compliance with the License. You may obtain
 a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.commonsware.cwac.cam2;

import android.graphics.SurfaceTexture;
import android.os.Build;
import android.view.Surface;
import com.commonsware.cwac.cam2.util.Size;
import java.util.List;

/**
 * Controller for camera-related functions, designed to be used
 * by CameraFragment or the equivalent.
 */
public class CameraController implements CameraView.StateCallback {
  private CameraEngine engine;
  private CameraDescriptor backCamera;
  private CameraDescriptor frontCamera;
  private CameraView cv;
  private Size previewSize;

  /**
   * @return the engine being used by this fragment to access
   * the camera(s) on the device
   */
  public CameraEngine getEngine() {
    return(engine);
  }

  /**
   * Setter for the engine. Must be called before onCreateView()
   * is called, preferably shortly after constructing the
   * fragment.
   *
   * @param engine the engine to be used by this fragment to access
   * the camera(s) on the device
   */
  public void setEngine(CameraEngine engine) {
    this.engine=engine;

    CameraSelectionCriteria criteria=
        new CameraSelectionCriteria.Builder()
            .facing(CameraSelectionCriteria.Facing.FRONT).build();
    List<CameraDescriptor> cameras=engine.getCameraDescriptors(criteria);

    if (cameras.size()>0) {
      frontCamera=cameras.get(0);
    }

    criteria=
        new CameraSelectionCriteria.Builder()
            .facing(CameraSelectionCriteria.Facing.BACK).build();
    cameras=engine.getCameraDescriptors(criteria);

    if (cameras.size()>0) {
      backCamera=cameras.get(0);
    }
  }

  /**
   * @return true if the device has both a front-facing camera and
   * a back-facing camera
   */
  public boolean hasBothCameras() {
    return(frontCamera!=null && backCamera!=null);
  }

  /**
   * Call this from onStart() of an activity or fragment, or from
   * an equivalent point in time. If the CameraView is ready,
   * the preview should begin; otherwise, the preview will
   * begin after the CameraView is ready.
   */
  public void start() {
    if (cv.isAvailable()) {
      open();
    }
  }

  /**
   * Call this from onStop() of an activity or fragment, or
   * from an equivalent point in time, to indicate that you want
   * the camera preview to stop.
   */
  public void stop() {
    engine.close(backCamera);

    // TODO: support multiple cameras
  }

  /**
   * Call this from onDestroy() of an activity or fragment,
   * or from an equivalent point in time, to tear down the
   * entire controller and engine. A fresh controller should
   * be created if you want to use the camera again in the future.
   */
  public void destroy() {
    if (engine!=null) {
      engine.destroy();
    }
  }

  /**
   * Setter method for the CameraView that this controller controls
   *
   * @param cv a CameraView
   */
  public void setCameraView(CameraView cv) {
    this.cv=cv;
    cv.setStateCallback(this);
  }

  /**
   * Public because Java interfaces are intrinsically public.
   * This method is not part of the class' API and should not
   * be used by third-party developers.
   *
   * @param cv the CameraView that is now ready
   */
  @Override
  public void onReady(CameraView cv) {
    open();
  }

  /**
   * Public because Java interfaces are intrinsically public.
   * This method is not part of the class' API and should not
   * be used by third-party developers.
   *
   * @param cv the CameraView that is now destroyed
   */
  @Override
  public void onDestroyed(CameraView cv) {
    stop();
  }

  private void open() {
    if (previewSize==null) {
      choosePreviewSize();
    }

    SurfaceTexture texture=cv.getSurfaceTexture();

    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1) {
      texture.setDefaultBufferSize(cv.getWidth(), cv.getHeight());
    }

    engine.open(backCamera, new Surface(texture));

    // TODO: support other cameras
  }

  private void choosePreviewSize() {
    List<Size> sizes=engine.getAvailablePreviewSizes(backCamera);
    // TODO: support other cameras
    // TODO: optional limit preview to same aspect ratio as chosen picture size

    Size largest=null;
    long currentLargestArea=0;
    Size smallestBiggerThanPreview=null;
    long currentSmallestArea=Integer.MAX_VALUE;

    for (Size size : sizes) {
      long currentArea=size.getWidth()*size.getHeight();

      if (largest==null || size.getWidth()*size.getHeight()>currentLargestArea) {
        largest=size;
        currentLargestArea=currentArea;
      }

      if (size.getWidth()>=cv.getWidth() && size.getHeight()>=cv.getHeight()) {
        if (smallestBiggerThanPreview==null || currentArea<currentSmallestArea) {
          smallestBiggerThanPreview=size;
          currentSmallestArea=currentArea;
        }
      }
    }

    previewSize=(smallestBiggerThanPreview==null
        ? largest
        : smallestBiggerThanPreview);

    cv.setPreviewSize(previewSize);
  }
}
