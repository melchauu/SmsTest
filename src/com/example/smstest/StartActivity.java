package com.example.smstest;

import android.os.Bundle;
import android.app.Activity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.Context;
import android.content.Intent;

public class StartActivity extends Activity {
	
	 String phone_num_txt; // make public?? 
	 EditText txtfld;
	 TextView txt; 
	 //Intent intent;
	 Spinner spinnerTask;
	 Spinner spinnerIdent;
	 
	String selTask=null;
	String selPers=null;
	
	public final static String IDENT = "com.example.SmsTest.IDENT";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		txtfld = (EditText) findViewById(R.id.edit_message);
		txt = (TextView)findViewById(R.id.text);
		
		// spinner
		spinnerTask = (Spinner) findViewById(R.id.t_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterTask = ArrayAdapter.createFromResource(this,
		        R.array.tasks_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterTask.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinnerTask.setAdapter(adapterTask);
		
		
		
		spinnerIdent = (Spinner) findViewById(R.id.p_spinner);
		
		ArrayAdapter<CharSequence> adapterIdent = ArrayAdapter.createFromResource(this,
		        R.array.ident_array, android.R.layout.simple_spinner_item);
		
		adapterIdent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerIdent.setAdapter(adapterIdent);
		
		spinnerTask.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		spinnerIdent.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	


	public void sendMessage(View view) {
	    // get text from textbox
		phone_num_txt = txtfld.getText().toString();
		txt.setText(phone_num_txt);
		
		Log.d("test" ,phone_num_txt); 
		
		//send sms
		SmsManager mySMS = SmsManager.getDefault();
		
		String destination = phone_num_txt;
		//  \ is esc char
		//{\"description\": \"Feed me\",\"type\": \"accept\"}
		//“description”: “Feed me”, “type”:  “accept”
		
		// {}  == ()
		//{“} == {“}
		String msg = "{\"description\": \"Feed me\",\"type\": \"accept\"}";
		//{"description": "Feed me","type": "accept");
		mySMS.sendTextMessage(destination, null, msg, null, null);
		txt.setText("sending msg");
		

		
	}
	
	//all functions tht respond to buttons must have arg (View vw)!!!! 
	public void listenMsg(View view){
		
		Intent RespInt = new Intent(this, ResponderService.class);
		Log.d("debug", "beforeStartService" );
		startService(RespInt);

	}
	
	public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
		        long id) {
			

			
			Log.d("test" , "here");
		    if (parent.getId() ==  R.id.t_spinner) //task
		    {
		    	 selTask = (String) parent.getItemAtPosition(pos);
		    	 Log.d("selTask" , selTask);
		    	
		    }	
		    
		    if (parent.getId() == R.id.p_spinner)//
		    {
		    	selPers = (String)parent.getItemAtPosition(pos);
		    	Log.d("selPers" , selPers);
		    	
		    }	
		 
	    	if (selTask != null && selPers != null){
	    		
			    if ( !(selTask.equals("Select Task")) && !(selPers.equals("Select a Side")))   // if seltask is not Select task and if selPers is not select a side 
			    {	
		    		Context context = getApplicationContext();
		    		CharSequence text = selTask + ',' + selPers;
		    		int duration = Toast.LENGTH_SHORT;

		    		Toast toast = Toast.makeText(context, text, duration);
		    		toast.show();
		    		
		    		if (selTask.equals("Money"))
		    			getTaskDeets(2);

		    		
			    }
	    	}
		}
		
	
	    public void onNothingSelected(AdapterView<?> parent) {
	        // Another interface callback
	    }	
	
	}

	public void getTaskDeets(int taskType){
		// ask for description and amount select contact --> launch new layout ? Yes
		
		// do the processing for each task
		// launch new activity
		
		Intent moneyInt = new Intent(this, MoneyActivity.class);
		moneyInt.putExtra(IDENT, selPers);
		startActivity(moneyInt);
	}

}
