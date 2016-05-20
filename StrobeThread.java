package com.example.maly_windows.ptm_secondproject;

import android.content.Context;
import android.hardware.Camera;
import android.os.SystemClock;

/**
 * Created by maly_windows on 2015-08-07.
 */
public class StrobeThread extends Thread {

    boolean isFlashOn;
    public boolean continue_running;
    public boolean end_broadcasting;
    Camera camera;
    Camera.Parameters params;
    Context ActivityCallingContext;
    ThirdActivity ActivityCalling;

    StrobeThread(Context con, ThirdActivity ActCalling) {
        isFlashOn = false;
        camera = Camera.open();
        params = camera.getParameters();
        isFlashOn = false;
        ActivityCallingContext = con;
        ActivityCalling = ActCalling;
        continue_running = false;
        end_broadcasting = false;
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }

    @Override
    public void run() {
        while(true)
        {
            if(!continue_running)
            {
                turnOffFlash();
            }
            else {
                if (isFlashOn)
                    turnOffFlash();
                else
                    turnOnFlash();
                SystemClock.sleep(100);
            }
            if(end_broadcasting)
            {
                turnOffFlash();
                return;
            }
        }
    }

}
