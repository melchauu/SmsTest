package com.example.smstest;


import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;

public class MoneyActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_money);
		
		Intent intent = getIntent();
		String whoru = intent.getStringExtra(StartActivity.IDENT);
		
		setContentView(R.layout.activity_money);
		
		TextView uAre =(TextView)findViewById(R.id.uAre_txt);
		uAre.setText(whoru);
		
		Button editDate = (Button) findViewById(R.id.DateMny_butn);
		editDate.setBackgroundResource(R.drawable.cal);
		/*ScrollView sv = new ScrollView(this);		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		
		TextView tv = new TextView(this);
		tv.setText("Dynamic layouts ftw money act!");
		ll.addView(tv);
		
		Button b = new Button(this);
		b.setText("I don't do anything, but I was added dynamically. :) money act");
		ll.addView(b);

		if (whoru.equals("Requestor"))		
			for(int i = 0; i < 20; i++) {
				    CheckBox cb = new CheckBox(this);
				    cb.setText("I'm dynamic!");
				    ll.addView(cb);
				}

		
		this.setContentView(sv);

		
		Context context = getApplicationContext();
		CharSequence text = "should have made buttons n stuff";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show(); */



		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.money, menu);
		return true;
	}
	
	public void SelDate(View view){
		
		//Intent RespInt = new Intent(this, ResponderService.class);
		//Log.d("debug", "beforeStartService" );
		//startService(RespInt);
		
		
		Context context = getApplicationContext();
		CharSequence text = "in SelDate";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
		

	}
	

}
