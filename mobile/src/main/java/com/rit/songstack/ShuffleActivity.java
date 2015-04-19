package com.rit.songstack;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Siddesh on 4/18/2015.
 */
public class ShuffleActivity extends Activity {

    private MediaPlayer mMediaPlayer;
    private ImageButton playPause, prev, next, add, stop, heart;
    private TextView heartRate, artistName, currentSong, nextSong;
    private int count = 0;
    private Map<String, String> musicMap;
    private static ArrayList<String> playList, savedPlayList;
    private final MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private final MediaMetadataRetriever mmr2 = new MediaMetadataRetriever();
    private String artistNameCurrent, artistNameNext, currentPlaying, nextPlaying, currentSongPath;
    private int index = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        playList = new ArrayList<String>();
        savedPlayList = new ArrayList<String>();
        musicMap = new HashMap<String, String>();
        mMediaPlayer = new MediaPlayer();
        musicMap.put("Chill", "/sdcard/data/Don Omar - Danza Kuduro.mp3");
        musicMap.put("Party", "/sdcard/data/DJ_Tiesto_Insomnia.mp3");
        musicMap.put("Pop", "/sdcard/data/PSY - GANGNAM STYLE.mp3");
        musicMap.put("Workout", "/sdcard/data/d12 - fight musicmp3.mp3");
        musicMap.put("EDM/Dance", "/sdcard/data/Alexandra Stan - Mr. Saxo beat.mp3");
        musicMap.put("Focus", "/sdcard/data/Paul Kelly - Peace - OST Каждое воскресенье Речь Аль Пачино - [MP3JUICES.COM].mp3");
        musicMap.put("Country", "/sdcard/data/GYPSY JAZZGITAN by Nick Ariondo-Django Reinhardt.mp3");
        musicMap.put("Rock", "/sdcard/data/77 Bombay Street - Long Way.mp3");
        musicMap.put("Folk", "/sdcard/data/NordicFolkMusic.mp3");
        musicMap.put("Classical", "/sdcard/data/13 Symphony No. 9 in D minor (Ode to Joy).mp3");
        musicMap.put("Blues", "/sdcard/data/B.B. King - Blues Boys Tune.mp3");
        musicMap.put("Metal", "/sdcard/data/Du Hast mich(you hate me ).mp3");
        musicMap.put("Reggae", "/sdcard/data/bob marley ganja gun lyrics.mp3");

        playList.add("/sdcard/data/Don Omar - Danza Kuduro.mp3");
        playList.add("/sdcard/data/Alexandra Stan - Mr. Saxo beat.mp3");
        playList.add("/sdcard/data/77 Bombay Street - Long Way.mp3");
        playList.add("/sdcard/data/13 Symphony No. 9 in D minor (Ode to Joy).mp3");
        playList.add("/sdcard/data/Du Hast mich(you hate me ).mp3");

        playPause = (ImageButton) findViewById(R.id.imageButton);
        prev = (ImageButton) findViewById(R.id.imageButton3);
        next = (ImageButton) findViewById(R.id.imageButton2);
        add = (ImageButton) findViewById(R.id.imageButton4);
        stop = (ImageButton) findViewById(R.id.imageButton6);
        heart = (ImageButton) findViewById(R.id.imageButton7);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(ShuffleActivity.this,
                R.anim.fade);
        heart.startAnimation(myFadeInAnimation);
        heartRate = (TextView) findViewById(R.id.textView);
        artistName = (TextView) findViewById(R.id.textView3);
        currentSong = (TextView) findViewById(R.id.textView2);
        nextSong = (TextView) findViewById(R.id.textView5);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Added to your favorites", Toast.LENGTH_SHORT).show();
                savedPlayList.add(currentSongPath);
            }
        });

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                heartRate.setText("");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                playPause.setBackgroundResource(R.drawable.ic_action_av_play_circle_outline);
                count = 0;
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                mMediaPlayer = new MediaPlayer();
                try {
                    index--;
                    currentSongPath = playList.get(index);
                    mmr.setDataSource(currentSongPath);
                    artistNameCurrent = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    artistName.setText(artistNameCurrent);
                    currentPlaying = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    currentSong.setText(currentPlaying);
                    mMediaPlayer.setDataSource(currentSongPath);

                    String lalala = playList.get(index+1);
                    mmr2.setDataSource(lalala);
                    String aba = mmr2.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    nextSong.setText(aba);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.start();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                mMediaPlayer = new MediaPlayer();
                try {
                    index++;
                    currentSongPath = playList.get(index);
                    mmr.setDataSource(currentSongPath);
                    artistNameCurrent = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    artistName.setText(artistNameCurrent);
                    currentPlaying = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    currentSong.setText(currentPlaying);
                    mMediaPlayer.setDataSource(currentSongPath);

                    String lalala = playList.get(index+1);
                    mmr2.setDataSource(lalala);
                    String aba = mmr2.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    nextSong.setText(aba);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.start();

            }
        });

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count == 0) {
                    playPause.setBackgroundResource(R.drawable.ic_action_av_pause);
                    count++;
                    mMediaPlayer = new MediaPlayer();
                    try {
                        currentSongPath = playList.get(index);
                        mmr.setDataSource(currentSongPath);
                        artistNameCurrent = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                        artistName.setText(artistNameCurrent);
                        currentPlaying = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                        currentSong.setText(currentPlaying);
                        mMediaPlayer.setDataSource(currentSongPath);

                        String lalala = playList.get(index + 1);
                        mmr2.setDataSource(lalala);
                        String aba = mmr2.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                        nextSong.setText(aba);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mMediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMediaPlayer.start();
                } else {
                    mMediaPlayer.pause();
                    playPause.setBackgroundResource(R.drawable.ic_action_av_play_circle_outline);
                    count = 0;
                }
            }
        });

    }

}
