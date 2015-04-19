package com.rit.songstack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Rushabh on 4/19/2015.
 */
public class TaskActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TaskActivity", "We're inside again!");
        super.onCreate(savedInstanceState);
    }
}
