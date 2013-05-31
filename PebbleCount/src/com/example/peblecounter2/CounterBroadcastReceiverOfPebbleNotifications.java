package com.example.peblecounter2;

import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

public class CounterBroadcastReceiverOfPebbleNotifications extends
		PebbleDataReceiver {
  //  public static CounterBroadcastReceiverOfPebbleNotifications instance = null;
    
	private static final String LOG_TAG = "GVP";
	public static final String  COUNTER_RECEIVER_STARTED_INTENT = "COUNTER_RECEIVER_STARTED_INTENT";
	public static final String  PEBBLE_DICTIONARY_KEY = "PEBBLE_DICTIONARY";

	//private Object mHandler;  no handler because not ui thread
public CounterBroadcastReceiverOfPebbleNotifications(){
		super(Counter.PEBBLE_APP_UUID);

		Log.i(LOG_TAG," StandAlone CounterBroadcastReceiverOfPebbleNotifications(). Registered CounterBroadcastReceiverOfPebbleNotifications data from Pebble");
		///Intent myIntent = new Intent("com.example.peblecounter2.Counter");
		// Make this receiver available for the Activity.
		 //instance = this;     

	}
private Intent createActivityLaunchingEvent(Context context,Bundle bundle) {

	    Intent intent = new Intent(context, Counter.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    intent.putExtras(bundle);
	    intent.setAction(Intent.ACTION_SEND);
	    intent.putExtra(COUNTER_RECEIVER_STARTED_INTENT, true);
	    return intent;
};
	protected CounterBroadcastReceiverOfPebbleNotifications(UUID subscribedUuid) {
		super(subscribedUuid);
		
		Log.i(LOG_TAG," StandAlone  CounterBroadcastReceiverOfPebbleNotifications("+subscribedUuid+") called from pebble app ");
	///	instance = this;
	}

	@Override
	public void receiveData(Context context, int transactionId,
			PebbleDictionary data) {
		Log.i(LOG_TAG,"StandAlone Received data from Pebble data Json= "+data.toJsonString()+" data tostring="+data.toJsonString());
		Bundle data_bundle= new Bundle();
        // Not UI thread so I think it is okay...
                // All data received from the Pebble must be ACK'd, otherwise you'll hit time-outs in the
                // watch-app which will cause the watch to feel "laggy" during periods of frequent
                // communication.
        PebbleKit.sendAckToPebble(context, transactionId);	               
      
		data_bundle.putString(PEBBLE_DICTIONARY_KEY, data.toJsonString());
		context.startActivity( createActivityLaunchingEvent(context,data_bundle));

	}

}
