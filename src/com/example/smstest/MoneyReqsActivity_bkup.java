/*package com.example.smstest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MoneyReqsActivity extends Activity {

	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
// look up LIMIt in sqllite /sql lite query  http://www.tutorialspoint.com/sqlite/sqlite_limit_clause.htm
		//http://stackoverflow.com/questions/2497677/using-the-limit-statement-in-a-sqlite-query

		ScrollView sv = new ScrollView(this);		
		LinearLayout ll = new LinearLayout(this);
		Log.i("MoneyReqs", "before sqlAst");     
		SQLiteAssistant sqlAst = new SQLiteAssistant(this);   // Lesson: need to use 'this' NOT moneyReqsActivity.this because we are in the oncreate part so we can acess context directly
	    
	    sqlAst.openDB(); //Lesson: you MUSTTT open the db before manipulating it! 
	    Log.i("MoneyReqs", "affter open DB");   
		String[][] peeps= sqlAst.getAllReqs();
	    Log.i("MoneyReqs", "peeps [][]");     
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		
		
		//padding stuff
	    int padding_in_dp = 6;  // 6 dps
	    final float scale = getResources().getDisplayMetrics().density;
	    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
		
		for(int i = 0; i < peeps.length; i++){
			//(String name, String num, String trans, String amt, String desc, String date
			//peeps[i][0] + peeps[i][1] + peeps[i][2] + peeps[i][3] + peeps[i][4] + peeps[i][5];
			
			Button b = new Button(this);
			b.setText(peeps[i][0] + peeps[i][3] + peeps[i][4] + peeps[i][5] );
			
			b.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
			b.setTextColor(Color.parseColor("#ffffff"));
			
			Typeface face=Typeface.createFromAsset(getAssets(), "fonts/TREBUC.TTF"); 
			b.setTypeface(face, Typeface.BOLD);
			
			if ( peeps[i][2].equals("Requestor")){    // note for the colors check out color website http://www.pajbk.com/clrw.htm on the phone... because it looks different on the pphone 
				b.setBackgroundResource(R.drawable.button_moneyreqs_requestor);
				Log.i("MoneyReqs", "requestor");
			}	
				//http://stackoverflow.com/questions/7991494/how-to-change-button-and-text-color-dynamically-on-android
			else {// you must be the requesteee
				b.setBackgroundResource(R.drawable.button_moneyreqs_requestee);
				Log.i("MoneyReqs", peeps[i][2] + "requestee");
			}	
			
			ll.addView(b);
			
		}
		TextView tv = new TextView(this);
		tv.setText("Dynamic layouts ftw!");
		ll.addView(tv);
		

			
		/*	for(int i = 0; i < 20; i++) {
				    CheckBox cb = new CheckBox(this);
				    cb.setText("I'm dynamic!");
				    ll.addView(cb);
				} */

/*		
		this.setContentView(sv);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.money_reqs, menu);
		return true;
	}

}*/
