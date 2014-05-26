package com.example.smstest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MoneyListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_money_list);
		
		Intent intent = getIntent();
		String whoru = intent.getStringExtra(StartActivity.IDENT); 
		
		//TextView uAre =(TextView)findViewById(R.id.uAre_txt);
		//uAre.setText(whoru);
		
		ScrollView sv = new ScrollView(this);		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		
		TextView tv = new TextView(this);
		tv.setText("Dynamic layouts ftw!");
		ll.addView(tv);
		
		Button b = new Button(this);
		b.setText("I don't do anything, but I was added dynamically. :)");
		ll.addView(b);

		if (whoru.equals("Requestor"))		
			for(int i = 0; i < 20; i++) {
				    CheckBox cb = new CheckBox(this);
				    cb.setText("I'm dynamic!");
				    ll.addView(cb);
				}

		
		this.setContentView(sv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.money_list, menu);
		return true;
	}

}
