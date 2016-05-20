package com.example.maly_windows.ptm_secondproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


public class BroadcastingActivity extends ActionBarActivity {

    BroadcastThread Broadcasting;
    public GridLayout MainLayout;
    ScrollView ParentView;
    public boolean repeat;
    public String Sequence;
    public ArrayList<TextView> SequenceTxt;
    public ArrayList<TextView> SequenceSigns;
    public ArrayList<SpannableString> SequenceSignsSpannable;
    String [] codes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcasting_acitivty);
        Sequence = getIntent().getExtras().getString("strToBr");
        repeat = getIntent().getExtras().getBoolean("repeat");

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int widthPixelsMargin = widthPixels - 30;

        codes = this.getResources().getStringArray(R.array.morse_code);

        SequenceTxt = new ArrayList<TextView>();
        SequenceSigns = new ArrayList<TextView>();
        SequenceSignsSpannable = new ArrayList<SpannableString>();

        MainLayout = (GridLayout) findViewById (R.id.layoutOnBroadcast);
        ParentView = (ScrollView) findViewById (R.id.parentViewBroadcast);
        MainLayout.setRowCount((int) (Math.ceil(Sequence.length()/7.0) * 2));
        for(int i = 0; i < Sequence.length(); i ++)
        {
            int place_row = 2 * (i/7);
            int place_col = i % 7;
            String letter, temp_letter;
            int letter_id = (int)Sequence.charAt(i) - 65;
            if(letter_id >= 0)
                letter = codes[letter_id];
            else
                letter = " ";

            temp_letter = "";
            for(int j = 0; j < letter.length() - 1; j ++)
            {
                temp_letter += letter.substring(j, j + 1);
                temp_letter += " ";
            }
            temp_letter += letter.substring(letter.length() - 1);

            letter = temp_letter;

            TextView NewTextViewCode = new TextView(this);
            SpannableString Temp = new SpannableString(letter);
            NewTextViewCode.setText(Temp);
            NewTextViewCode.setTextSize(17);
            NewTextViewCode.setGravity(Gravity.CENTER);
            NewTextViewCode.setWidth(widthPixelsMargin/7);
            NewTextViewCode.setHeight(20);
            SequenceSignsSpannable.add(Temp);
            GridLayout.LayoutParams Parameters = new GridLayout.LayoutParams(GridLayout.spec(place_row), GridLayout.spec(place_col));
            Parameters.width = widthPixelsMargin/7;
            Parameters.height = 30;
            NewTextViewCode.setLayoutParams(Parameters);
            MainLayout.addView(NewTextViewCode);
            SequenceSigns.add(NewTextViewCode);

            TextView NewTextViewLetter = new TextView(this);
            NewTextViewLetter.setText(Character.toString(Sequence.charAt(i)));
            NewTextViewLetter.setGravity(Gravity.CENTER);
            NewTextViewLetter.setWidth(widthPixelsMargin/7);
            NewTextViewLetter.setHeight(20);
            Parameters = new GridLayout.LayoutParams(GridLayout.spec(place_row + 1), GridLayout.spec(place_col));
            Parameters.width = widthPixelsMargin/7;
            Parameters.height = 30;
            NewTextViewLetter.setLayoutParams(Parameters);
            MainLayout.addView(NewTextViewLetter);
            SequenceTxt.add(NewTextViewLetter);

        }


        setContentView(ParentView);
        //ParentView.addView(MainLayout);
        Broadcasting = new BroadcastThread(getApplicationContext(), BroadcastingActivity.this, repeat);
        Broadcasting.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_broadcasting_acitivty, menu);
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

    @Override
    public void onPause() {
        super.onPause();
        Broadcasting.continue_running = false;
        try {
            Broadcasting.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
