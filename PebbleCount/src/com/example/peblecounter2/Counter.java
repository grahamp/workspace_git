package com.example.peblecounter2;

import java.util.Random;
import java.util.UUID;

import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
public class Counter extends Activity {
    private final String TAG = "Counter";
    public static int current_count=0;
    public static int saved_count=0;
   @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
protected void onRestart() {
	// TODO Auto-generated method stub
		
	super.onRestart();
}
@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onRestoreInstanceState(savedInstanceState);
}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.i(LOG_TAG,"Called onNewIntent" );
		handleInit(intent);
	}
	boolean launchedByWatch =false;
	PebbleDictionary pebbleDictionary =null;
	final static private String LOG_TAG="GVP";
@Override
    protected void onResume() {
        super.onResume();
        
      
       }
private void handleInit(Intent launchingIntent) {
	launchedByWatch=false;
	
	Bundle bundleWithDictionary=null;
	if (launchingIntent != null){
		launchedByWatch =launchingIntent.getBooleanExtra(CounterBroadcastReceiverOfPebbleNotifications.COUNTER_RECEIVER_STARTED_INTENT, false);
		if (!launchedByWatch){
			// Launch watch
			startWatchApp(null);
		} else {
			bundleWithDictionary=launchingIntent.getExtras();
			if (bundleWithDictionary != null){
				String serializedDictionary=(String) bundleWithDictionary.getString(CounterBroadcastReceiverOfPebbleNotifications.PEBBLE_DICTIONARY_KEY);
				if (serializedDictionary!=null)
					try {
						pebbleDictionary= PebbleDictionary.fromJson(serializedDictionary);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if (pebbleDictionary != null){
					Log.i(LOG_TAG,"Counter onResume got data from receiver= "+pebbleDictionary.toString()+" data tostring="+pebbleDictionary.toJsonString());
			      
			        current_count = pebbleDictionary.getInteger(COUNT_KEY).intValue();
			        Log.i(TAG,"count ="+current_count);
			        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			        SharedPreferences.Editor editor = sharedPref.edit();
			        editor.putInt(COUNT_KEY+"", current_count);     
			        editor.putLong(TIME_KEY+"",System.currentTimeMillis());  
			        currentCount.setText(""+current_count);
			        //String entryCountString = ""+entryCount%10;
			       // editor.putInt(entryCountString+COUNT_KEY, global_count);     
			       // editor.putInt(entryCountString+TIME_KEY,System.currentTimeMillis());
			       
					editor.commit();
					updateTitle();
				} else
					Log.e(LOG_TAG,"PebbleDictionary null....Unexpected.");
				
			}
		}
	}
}

   // Send a broadcast to launch the specified application on the connected Pebble
   public void startWatchApp(View view) {
       PebbleKit.startAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
   }

   // Send a broadcast to close the specified application on the connected Pebble
   public void stopWatchApp(View view) {
       PebbleKit.closeAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
   }
   
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// So if I do that the watch can't wake up the app.
		 // Always deregister any Activity-scoped BroadcastReceivers when the Activity is paused
//        if (gvpPebbleDataReceiver != null) {
//            unregisterReceiver(gvpPebbleDataReceiver);
//            gvpPebbleDataReceiver = null;
//        }

	}


    public final static UUID PEBBLE_APP_UUID = UUID.fromString("EC7EE5C6-8DDF-4089-AA84-C3396A11CC99");
    private final static int CMD_KEY = 0x00;
    private final static int COUNT_KEY = 0x01;
    private final static int TIME_KEY = 0x02;
    private final static int CMD_UP = 0x01;
    private final static int CMD_DOWN = 0x02;
 
   // private Handler mHandler;
	private EditText currentCount;
	private EditText savedCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counter);
		
        //.getApplication().get
        currentCount   = (EditText)findViewById(R.id.edit_field_count);    
        savedCount   = (EditText)findViewById(R.id.lastValue);  
       
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        if (sharedPref != null){
        	 
        	saved_count= sharedPref.getInt(COUNT_KEY+"", 0);   
        	savedCount.setText(""+saved_count) ;     
        }
        String versionName = "none found";
		try {
			versionName =getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(LOG_TAG,"Called onCreate v="+versionName);
		setTitle("Counter v="+versionName);
        handleInit(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.counter, menu);
		return true;
	}   
public static void vibrateWatch(Context c) {
        PebbleDictionary data = new PebbleDictionary();
        data.addUint8(CMD_KEY, (byte) CMD_UP);
        data.addInt32(COUNT_KEY, current_count);
        PebbleKit.sendDataToPebble(c, PEBBLE_APP_UUID, data);
    }
public void updateTitle() {
	this.setTitle("Count="+current_count);
	currentCount.setText(""+current_count);
}

public class PebbleConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String pebbleAddress = intent.getStringExtra("address");
        Log.i(TAG, "GVP counter... Pebble  CONNECTED "+ pebbleAddress);
        setTitle("Pebble connected "+pebbleAddress);
    }
}

	
}
