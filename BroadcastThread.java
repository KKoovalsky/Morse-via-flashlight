package com.example.maly_windows.ptm_secondproject;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;



/**
 * Created by maly_windows on 2015-07-15.
 */
public class BroadcastThread extends Thread {

    final int T = 250;              //Podstawa czasu dla kodu Morse'a w milisekundach.
    String [] codes;
    String sequence;
    Camera camera;
    Camera.Parameters params;
    boolean isFlashOn;
    Context ActivityCallingContext;
    BroadcastingActivity ActivityCalling;
    TextView TempCode;
    SpannableString TempSpanString;
    TextView TempLetter;
    int InnerIterator, SecInnerIterator;

    public boolean continue_running;
    public boolean repeat;

    public BroadcastThread(Context con, BroadcastingActivity ActCalling, boolean looping) {

        ActivityCallingContext = con;
        codes = ActivityCallingContext.getResources().getStringArray(R.array.morse_code);
        ActivityCalling = ActCalling;
        camera = Camera.open();
        params = camera.getParameters();
        isFlashOn = false;

        continue_running = true;
        repeat = looping;
        sequence = ActivityCalling.Sequence;
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


    private boolean set_timer_flash_action(long interval)
    {
        turnOnFlash();
        for(int i = 0; i < interval; i ++) {
            SystemClock.sleep(T);
            if(!continue_running)
                return false;
        }
        turnOffFlash();
        return true;
    }

    private boolean set_timer_no_flash_action(long interval)
    {
        for(int i = 0; i < interval; i ++) {
            SystemClock.sleep(T);
            if(!continue_running)
                return false;
        }
        return true;
    }

    private boolean broadcast_long() {
        return set_timer_flash_action(3);
    }
    private boolean broadcast_short() {
        return set_timer_flash_action(1);
    }
    private boolean broadcast_short_stop() {
        return set_timer_no_flash_action(1);
    }
    private boolean broadcast_letter_stop() {
        return set_timer_no_flash_action(3);
    }
    private boolean broadcast_word_stop() {
        return set_timer_no_flash_action(9);
    }
    private boolean broadcast_sentence_stop() {
        return set_timer_no_flash_action(18);
    }


    @Override
    public void run() {
        TempLetter = ActivityCalling.SequenceTxt.get(0);
        while (true) {
            for (InnerIterator = 0; InnerIterator < sequence.length(); InnerIterator++) {
                int letter_id = (int) sequence.charAt(InnerIterator) - 65;
                String letter = codes[letter_id];

                ActivityCalling.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TempLetter.setTextColor(Color.GRAY);
                        TempLetter = ActivityCalling.SequenceTxt.get(InnerIterator);
                        TempLetter.setTextColor(Color.BLUE);
                    }
                });

                TempCode = ActivityCalling.SequenceSigns.get(InnerIterator);
                TempSpanString = ActivityCalling.SequenceSignsSpannable.get(InnerIterator);
                for (SecInnerIterator = 0; SecInnerIterator < letter.length(); SecInnerIterator++) {
                    TempSpanString.setSpan(new ForegroundColorSpan(Color.BLUE), SecInnerIterator * 2, SecInnerIterator * 2 + 1, 0);
                    ActivityCalling.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TempCode.setText(TempSpanString);
                        }
                    });
                    if (letter.charAt(SecInnerIterator) == '·') {
                        if (!broadcast_short()) {
                            turnOffFlash();
                            return;
                        }
                    } else {
                        if (!broadcast_long()) {
                            turnOffFlash();
                            return;
                        }
                    }
                    TempSpanString.removeSpan(ForegroundColorSpan.class);
                    TempSpanString.setSpan(new ForegroundColorSpan(Color.GRAY), SecInnerIterator * 2, SecInnerIterator * 2 + 1, 0);
                    ActivityCalling.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TempCode.setText(TempSpanString);
                        }
                    });
                    if (SecInnerIterator != letter.length() - 1) {
                        if (!broadcast_short_stop()) {
                            turnOffFlash();
                            return;
                        }
                    }
                }

                if (InnerIterator != sequence.length() - 1 && sequence.charAt(InnerIterator + 1) != ' ') {
                    if (!broadcast_letter_stop()) {
                        turnOffFlash();
                        return;
                    }
                } else {
                    if (!broadcast_word_stop()) {
                        turnOffFlash();
                        return;
                    }
                    InnerIterator++;
                }
            }
            if(!repeat)
                break;
            broadcast_sentence_stop();
        }
        ActivityCalling.finish();
    }
}
