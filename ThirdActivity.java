package com.example.maly_windows.ptm_secondproject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class ThirdActivity extends ActionBarActivity {

    Button StartStrobe;
    Button StopStrobe;
    StrobeThread Strobing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        StartStrobe = (Button)findViewById(R.id.start_strobe);
        StopStrobe = (Button)findViewById(R.id.stop_strobe);

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;

        RelativeLayout.LayoutParams ParametersStart, ParametersStop;
        ParametersStart = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        ParametersStop = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        ParametersStart.setMargins(0, heightPixels/4, 0, 0);
        ParametersStop.setMargins(0, 0, 0, heightPixels/4);
        ParametersStart.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        ParametersStop.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ParametersStart.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ParametersStop.addRule(RelativeLayout.CENTER_HORIZONTAL);

        StartStrobe.setLayoutParams(ParametersStart);
        StopStrobe.setLayoutParams(ParametersStop);

        StartStrobe.setEnabled(true);
        StopStrobe.setEnabled(false);



        Strobing = new StrobeThread(getApplicationContext(), ThirdActivity.this);
        Strobing.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_third, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStartButtonClick(View V) {
        Strobing.continue_running = true;
        StartStrobe.setEnabled(false);
        StopStrobe.setEnabled(true);
    }

    public void onStopButtonClick(View V) {
        StopStrobe.setEnabled(false);
        StartStrobe.setEnabled(true);
        Strobing.continue_running = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Strobing.end_broadcasting = true;
        try {
            Strobing.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
