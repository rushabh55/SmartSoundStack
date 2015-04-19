package com.rit.songstack;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.larswerkman.holocolorpicker.*;
import android.util.*;
import android.widget.Toast;


public class MainActivity extends Activity implements MessageApi.MessageListener {
    public static final int min = Math.abs(-21760);
    public static final int max = Math.abs(-16777216);
    ColorPicker freqSelector;
    TextView freqView;
    TextView ampView;
    ValueBar valueBar;
    /* Corresponds to the genre codes on the SongGenres.java */
    private static int _genre = SongGenres.NONE;

    private Button letsShuffle;

    double style = 0d;
    double amplitude = 0d;

    double Style, Amplitude;

    public static int getGenre(){
        return _genre;
    }
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            // Display message in UI
            ampView.setText(message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        freqView = (TextView) findViewById(R.id.textView);
        freqSelector = (ColorPicker) findViewById( R.id.picker );
        valueBar = (ValueBar) findViewById(R.id.VB1);
        ampView = (TextView) findViewById(R.id.textView2);

        letsShuffle = (Button) findViewById(R.id.button);

        letsShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShuffleActivity.class);
                startActivity(intent);
            }
        });


        freqSelector.addValueBar(valueBar);
        freqSelector.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int i) {
                style = i;
                Style = updateTempo();
                String val = ""+ Style;
                freqView.setText(val.toCharArray(), 0, val.length());
                //Toast.makeText(getApplicationContext(), "" + Style, Toast.LENGTH_SHORT).show();
            }
        });
        freqSelector.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                style = i;
                Style = updateTempo();
                //Toast.makeText(getApplicationContext(), "" + Style, Toast.LENGTH_SHORT).show();
                String val = ""+ Style;
                freqView.setText(val.toCharArray(), 0, val.length());
            }
        });

        valueBar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
            @Override
            public void onValueChanged(int i) {
                amplitude = i;
                String val = "" + i;
                ampView.setText(val.toCharArray(), 0, val.length());
                //updateGenre();
            }
        });

        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }
    public double updateTempo() {
        if ( style < -1 || style > 1 ){
            Log.d("SongStack","inside update tempo");
            style -= min;
            style = Math.abs(style);
            double max = this.max - this.min;
            style = style * 1 / max;
            Log.d("SongStack", "" + style);
        }
        return style;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        {
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
            byte[] p= messageEvent.getData();
            String w = new String(p);
            Toast.makeText(getApplicationContext(), w, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
