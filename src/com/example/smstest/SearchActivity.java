package com.example.smstest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SearchActivity extends Activity {

	public final static String ROWID = "com.example.SmsTest.ROWID";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
	    Intent intent = getIntent();
	     String selection= intent.getStringExtra(MoneyReqsActivity.SQLSELECTION);

		ScrollView sv = new ScrollView(this);		
		LinearLayout ll = new LinearLayout(this);
		Log.i("Search", "before sqlAst");     
		SQLiteAssistant sqlAst = new SQLiteAssistant(this);   // Lesson: need to use 'this' NOT moneyReqsActivity.this because we are in the oncreate part so we can acess context directly
	    
	    sqlAst.openDB(); //Lesson: you MUSTTT open the db before manipulating it! 
	    Log.i("MoneyReqs", "affter open DB"); 
	    
	    	   
	    
		String[][] peeps= sqlAst.getSpecific(selection,"date_incur" + " DESC");		
	    Log.i("MoneyReqs", "peeps [][]");     
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		
		Button [] b = new Button[peeps.length];
		//padding stuff
	    int padding_in_dp = 6;  // 6 dps
	    final float scale = getResources().getDisplayMetrics().density;
	    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
		
		for(int i = 0; i < peeps.length; i++){
			//(String name, String num, String trans, String amt, String desc, String date
			//peeps[i][0] + peeps[i][1] + peeps[i][2] + peeps[i][3] + peeps[i][4] + peeps[i][5];
			
			 b[i] = new Button(this);
			 String dateRaw= peeps[i][5];
			 String[] dateFrag=dateRaw.split("-");
			 String dateSmall=dateFrag[1]+ "/" +dateFrag[2];  // Month/Day
			 
			b[i].setText(peeps[i][0] + " $" + peeps[i][3] +" " + peeps[i][4]+ " " + dateSmall );
			b[i].setHint(peeps[i][6]);// the hint will be the rowID in sql :D 
			 
			b[i].setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
			b[i].setTextColor(Color.parseColor("#ffffff"));
			
			Typeface face=Typeface.createFromAsset(getAssets(), "fonts/TREBUC.TTF"); 
			b[i].setTypeface(face, Typeface.BOLD);
			b[i].setTextSize(20f);
			b[i].setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 // lesson: View V is actually the  thing that we're listening on.. in this case its the button :D 
	            	 Button retb = (Button)v;
	            	 String buttonText = retb.getText().toString();
	            	 Log.i("MoneyReqs", buttonText);
	            	 Log.i("MoneyReqs", retb.getHint().toString());  // retrieving row id 
	            	 
	         		Intent moneyDeets = new Intent(getApplicationContext(), MoneyDetailActivity.class);
	        		moneyDeets.putExtra(ROWID, retb.getHint().toString());
	        		startActivity(moneyDeets);
	             }
	         });
			
			if ( peeps[i][2].equals("lender")){    // note for the colors check out color website http://www.pajbk.com/clrw.htm on the phone... because it looks different on the pphone 
				b[i].setBackgroundResource(R.drawable.button_moneyreqs_requestor);
				Log.i("MoneyReqs", "requestor");
			}	
				//http://stackoverflow.com/questions/7991494/how-to-change-button-and-text-color-dynamically-on-android
			else {// you must be the requesteee
				b[i].setBackgroundResource(R.drawable.button_moneyreqs_requestee);
				Log.i("MoneyReqs",  "requestee");
				
			}	
			
			ll.addView(b[i]);
			
		}
		TextView tv = new TextView(this);
		tv.setText("Dynamic layouts ftw!");
		ll.addView(tv);
		

			
		/*	for(int i = 0; i < 20; i++) {
				    CheckBox cb = new CheckBox(this);
				    cb.setText("I'm dynamic!");
				    ll.addView(cb);
				} */

		
		this.setContentView(sv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_home:
	        	Log.i(" menu", "home");
         		Intent hometime = new Intent(getApplicationContext(), StartActivity.class);        		
        		startActivity(hometime);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	

}
