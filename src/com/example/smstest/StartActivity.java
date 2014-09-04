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
import android.graphics.Typeface;

public class StartActivity extends Activity {
	 

	 TextView appTitle; 
	 Button lender;
	 Button borrower;
	 Button seeReqs;
	
	public final static String IDENT = "com.example.SmsTest.IDENT";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// name = friendTab
		setContentView(R.layout.activity_start);		

	    int pad_titleTop = dpToPix(25) ;  
	    int pad_titleBtm = dpToPix(35) ;
	    int pad_buttonTB = dpToPix(10) ;
	    
	    
	    
		appTitle = (TextView)findViewById(R.id.appTitle);
		Typeface face=Typeface.createFromAsset(getAssets(), "fonts/TREBUC.TTF");
		appTitle.setTypeface(face, Typeface.BOLD);
		appTitle.setPadding(0,pad_titleTop,0,pad_titleBtm);	//int left, int top, int right, int bottom
		appTitle.setTextColor(getResources().getColor(R.color.ocean));
		
		borrower = (Button)findViewById(R.id.borrower);
		borrower.setTypeface(face, Typeface.NORMAL);
		borrower.setBackgroundResource(R.drawable.button_moneyreqs_requestee); // orange
		borrower.setTextColor(getResources().getColor(R.color.white));
		
		lender = (Button)findViewById(R.id.lender);
		lender.setTypeface(face, Typeface.NORMAL);
		lender.setBackgroundResource(R.drawable.button_moneyreqs_requestor); // green	
		lender.setTextColor(getResources().getColor(R.color.white));
		
		seeReqs = (Button)findViewById(R.id.seeList_butn);
		seeReqs.setTypeface(face, Typeface.NORMAL);
		seeReqs.setBackgroundResource(R.drawable.button_brown_rect);
		seeReqs.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	


	
	
	//all functions tht respond to buttons must have arg (View vw)!!!! 

	
	public void listenMsg(View view){
		Intent moneyInt = new Intent(this, MoneyActivity.class);
		Button retb = (Button)view;
		
		moneyInt.putExtra(IDENT, retb.getText().toString());
		Log.i("StartActivity", "IDENT is" + retb.getText().toString() );
		startActivity(moneyInt);
		


	}
	
	public void seeReqs(View view){
		
		Intent moneyReqInt = new Intent(this, MoneyReqsActivity.class);		
		startActivity(moneyReqInt);

	}
	

	public int dpToPix(int dp){

	    final float scale = getResources().getDisplayMetrics().density;
	    int padding_in_px = (int) (dp * scale + 0.5f);
	    
	    return padding_in_px;
	}
	

}
