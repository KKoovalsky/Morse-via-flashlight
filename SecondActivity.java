package com.example.maly_windows.ptm_secondproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class SecondActivity extends ActionBarActivity {

    Button StartButton;
    EditText Input;
    CheckBox Loop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        StartButton = (Button) findViewById (R.id.start_morse);

        Input = (EditText) findViewById (R.id.enterText_edit);

        Loop = (CheckBox) findViewById (R.id.checkbox);

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;

        Input.setWidth(widthPixels - 30);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
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

    private boolean check_letter_containing(String S)
    {
        for(int i = 0; i < S.length(); i ++) {
            int idx_char = (int)S.charAt(i);
            if (!(((idx_char > 64) && (idx_char < 91)) || ((idx_char > 96) && (idx_char < 123)) || idx_char == 32 ))
                return false;
        }

        return true;
    }

    public void startBroadcasting(View V)
    {
        String sequence = Input.getText().toString();
        if(sequence.length() == 0)
        {
            Toast T = Toast.makeText(getApplicationContext(),"No sequence entered.", Toast.LENGTH_SHORT);
            T.show();
            T.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM,0,0);
            Input.setText("");
            return;
        }
        if(!check_letter_containing(sequence))
        {
            Toast T = Toast.makeText(getApplicationContext(),"Invalid sequence form. (Just A-Z letters)", Toast.LENGTH_SHORT);
            T.show();
            T.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM,0,0);
            Input.setText("");
            return;
        }
        sequence = sequence.toUpperCase();
        sequence = sequence.replaceAll("\\s+", " ");
        if(sequence.charAt(0) == ' ')
            sequence = sequence.substring(1);
        if(sequence.charAt(sequence.length() - 1) == ' ')
            sequence = sequence.substring(0, sequence.length() - 1);

        Intent I = new Intent(this, BroadcastingActivity.class);
        I.putExtra("strToBr", sequence);
        I.putExtra("repeat", Loop.isChecked());
        startActivity(I);
    }
}
