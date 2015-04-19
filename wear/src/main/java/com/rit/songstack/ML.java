package com.rit.songstack;

import android.media.tv.TvContract;
import android.util.JsonWriter;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Rushabh on 4/19/2015.
 */

class Song {
    public String name = "";
    public long length;
    public int genre ;
    public Song() {
        //genre = SongGenres
    }
}
class Data{
    int hearRate;
    final ArrayList<String> prevSongs = new ArrayList<>();
}
public class ML {
    final ArrayList<Data> _data = new ArrayList<>();
    public ML(){

    }

    public void ReadData() {

    }

    public void analyse(){
        ReadData();

    }

    public void WriteData(Data d){
        try {
            _data.add(d);
                File f = new File("saveData.json");
                FileOutputStream fo = new FileOutputStream(f);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fo);
                //outputStreamWriter.close();
                JsonWriter w = new JsonWriter(outputStreamWriter);
                w.beginObject();
                JSONArray arr = new JSONArray();
                int i = -1;
                for(Data s : _data) {
                    i++;
                    String p =s.toString() + "" + i ;
                    w.name(p+ "heartRate").value(s.hearRate);
                    w.name(p + "prevSongs").value(s.prevSongs.toString());
                }
                w.endObject();
                w.flush();
                outputStreamWriter.flush();
                outputStreamWriter.close();


            } catch (Exception e){

        }
    }
}
