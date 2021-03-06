package com.rit.songstack;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends Activity implements SensorEventListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = MainActivity.class.getName();

    private TextView rate;
    private TextView accuracy;
    private TextView sensorInformation;
    private static final int SENSOR_TYPE_HEARTRATE = Sensor.TYPE_HEART_RATE;
    private Sensor mHeartRateSensor;
    private SensorManager mSensorManager;
    GoogleApiClient mGoogleApiClient;
    public static final String START_ACTIVITY_PATH = "/start/MainActivity";
    final HashSet<String> nodes = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "We're in!");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //latch = new CountDownLatch(1);
            final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
            stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
                @Override
                public void onLayoutInflated(WatchViewStub stub) {
                    rate = (TextView) stub.findViewById(R.id.rate);
                    rate.setText("Reading...");

                    accuracy = (TextView) stub.findViewById(R.id.accuracy);
                    sensorInformation = (TextView) stub.findViewById(R.id.sensor);

                    //latch.countDown();
                }
            });

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
            mHeartRateSensor = mSensorManager.getDefaultSensor(SENSOR_TYPE_HEARTRATE); // using Sensor Lib2 (Samsung Gear Live)
            //mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE); // using Sensor Lib (Samsung Gear Live)
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(Wearable.API).build();

            Log.d("my", " Initialized google plus api client");
        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        getNodes();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    private void getNodes() {
        PendingResult<NodeApi.GetConnectedNodesResult> res =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        res.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                nodes.clear();
                for(Node s : getConnectedNodesResult.getNodes())
                    nodes.add(s.getId());
            }
        });

    }

    private void sendStartActivityMessage(String nodeId, String message) {
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, nodeId, START_ACTIVITY_PATH, message.getBytes()).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );
    }

    public void SendToAll(String message){
        for(String s : nodes){
            sendStartActivityMessage(s , message);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager.registerListener(this, this.mHeartRateSensor,SENSOR_TYPE_HEARTRATE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        try {
            //latch.await();
            if(sensorEvent.values[0] > 0){
                Log.d(TAG, "sensor event: " + sensorEvent.accuracy + " = " + sensorEvent.values[0]);
                rate.setText(String.valueOf(sensorEvent.values[0]));
                accuracy.setText("Accuracy: "+sensorEvent.accuracy);
                SendToAll(String.valueOf(sensorEvent.values[0]));
                //sensorInformation.setText(sensorEvent.sensor.toString());
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(TAG, "accuracy changed: " + i);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mSensorManager.unregisterListener(this);
    }
}
