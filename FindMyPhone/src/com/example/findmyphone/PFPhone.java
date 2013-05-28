package com.example.findmyphone;

import java.util.UUID;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.WindowManager;

public class PFPhone extends Activity {
	 private static final String LOG_TAG = "GVP";
	//EC7EE5C6-8DDF-4089-AA84-C3396A11CC99
	public static UUID PEBBLE_APP_UUID= UUID.fromString("DD0CDA50-C1AE-444B-AE5A-DCECDF70D1DF");
	 //0xDD, 0x0C, 0xDA, 0x50, 0xC1, 0xAE, 0x44, 0x4B, 0xAE, 0x5A, 0xDC, 0xEC, 0xDF, 0x70, 0xD1, 0xDF
	 //DD0CDA50C1AE444BAE5ADCECDF70D1DF
	AudioManager audio;
	NotificationManager notification;
	AlarmManager alarmManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pfphone);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		audio =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		notification =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		alarmManager =  (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		String versionName = "none found";
		try {
			versionName =getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(LOG_TAG,"Called onNewIntent v="+versionName);
		setTitle("PFPhone v="+versionName);
		
		if (getIntent().getAction().equalsIgnoreCase(PFPhoneBroadcastReceiverOfPebbleNotifications.ACTION_LAUNCH)){
			Log.i(LOG_TAG,"Notifier to onCreate");
			makeNotifySound();
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pfphone, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(LOG_TAG,"Called onResume");
		makeNotifySound();
	}

	private void makeNotifySound() {
		try {
		        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		        r.play();
		    } catch (Exception e) {}
	}
    private void turnOnlight(){
    	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    	PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Find ME");
    	wl.acquire();
    	setTitle("Lights On!");
    	
    	//Do whatever you need right here
    	wl.release(); 

    }
	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	   
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.i(LOG_TAG,"Called onNewIntent");
		makeNotifySound();
		turnOnlight();
		audio.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			Log.i(LOG_TAG,"Touch event "+event);
		  if (event.getY() < 200)
			  buildNotification();
		  if (event.getY() > 200)
			  makeNotifySound(); 
		  if (event.getX() < 200)
			  audio.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
			  else
			audio.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
		}
		return super.onTouchEvent(event);
	}

	protected void buildNotification(){
	    NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
	    nb.setContentTitle("title");
	    nb.setContentText("message");
	   // nb.setSmallIcon(getResources().getIdentifier("drawable/alert", null, packageName));
	    nb.setWhen(System.currentTimeMillis());
	    nb.setAutoCancel(true);
	    nb.setTicker("message");

	   // final Uri ringtone = Uri.parse(PreferenceManager.getDefaultSharedPreferences(this).getString("ringtone", getString(R.string.settings_default_ringtone)));

	    nb.setDefaults(Notification.DEFAULT_VIBRATE);
	    //nb.setSound(ringtone);      
	    nb.setDefaults(Notification.DEFAULT_LIGHTS);
	    nb.setDefaults(Notification.DEFAULT_SOUND);
	   

	    NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

	    final Intent notificationIntent = new Intent(this, PFPhone.class);
	    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

	    final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	    nb.setContentIntent(contentIntent);

	    Notification notification = nb.build();
	   

	    nm.notify(0, notification);
	}
	

}
