package com.example.smstest;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;


public class CompleteContactTransActivity extends Activity {

	public final static String ROWID_NET = "com.example.SmsTest.ROWID_NET";	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		
	    Intent intent = getIntent();
	    Integer isTotalled = Integer.parseInt(intent.getStringExtra(MoneyDetailActivity.ISTOTALLED));
	    String contactNm = intent.getStringExtra(MoneyDetailActivity.SPECIFIC_CONTACT_NAME);
	    Float finalTotal = 0f;
	    
	    
		//temp strings to calc NetTotal;
		String transTmp;
		float prevAmt=0;
		float amtTmp=0;
		float finalAmt=0;
	    
 	    
  // ************************* UGLY LAYOUT STRUCTURE CREATION ******************************* 
		ScrollView sv = new ScrollView(this);		
			LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.VERTICAL);

			
				sv.addView(ll);
			
				TextView contactAsTitle = new TextView(this);
				Typeface face=Typeface.createFromAsset(getAssets(), "fonts/TREBUC.TTF");
				contactAsTitle.setTypeface(face, Typeface.BOLD);
				int contactPadLeft= dpToPix(10);
				int contactPadBottom= dpToPix(5);
				int contactPadTop= dpToPix(10);
				//left, top, right, bottom
				contactAsTitle.setPadding(contactPadLeft,contactPadTop , 0, contactPadBottom);
				contactAsTitle.setTextSize(30f);
				contactAsTitle.setTextColor(getResources().getColor(R.color.chocolate));
				contactAsTitle.setText(contactNm);				
					ll.addView(contactAsTitle);
				
				LinearLayout wrapHalves  = new LinearLayout(this);
				wrapHalves.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams param= new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1.0f);
				wrapHalves.setLayoutParams(param);
				int pad = dpToPix(5);
				wrapHalves.setPadding(pad, pad, pad, pad);
				wrapHalves.setBaselineAligned(false);
				wrapHalves.setVisibility(View.VISIBLE);
					ll.addView(wrapHalves);
				
				
					LinearLayout leftHalf = new LinearLayout(this);
					leftHalf.setOrientation(LinearLayout.VERTICAL);
					// android.widget.LinearLayout.LayoutParams.LayoutParams(int width, int height, float weight)
					//set layout_width , layout_height (in pixels/Match_parent/wrap_content), layout_weight					
					leftHalf.setLayoutParams(param);
					wrapHalves.setVisibility(View.VISIBLE);
						wrapHalves.addView(leftHalf);
						
					LinearLayout rightHalf = new LinearLayout(this);
					rightHalf.setOrientation(LinearLayout.VERTICAL);
					rightHalf.setLayoutParams(param);
					//lesson learned: weight for righthalf and lefthalf are 1:1  so they will take up the same width since width is set to 0
					wrapHalves.setVisibility(View.VISIBLE);					
						wrapHalves.addView(rightHalf);
					
//***********************  GOING INTO DATABASE ***********************
					
		Log.i("CompleteContactTrans", "before sqlAst");     
		SQLiteAssistant sqlAst = new SQLiteAssistant(this);   // Lesson: need to use 'this' NOT moneyReqsActivity.this because we are in the oncreate part so we can acess context directly
	    
	    sqlAst.openDB(); //Lesson: you MUSTTT open the db before manipulating it! 
	    Log.i("CompleteContactTrans", "affter open DB");   
	    String selection = "contact_name ='"+ contactNm + "'";  //
	    
		String[][] peeps= sqlAst.getSpecific(selection,"date_incur" + " DESC");
	    Log.i("CompleteContactTrans", "peeps [][]");     

		
		Button [] bR = new Button[peeps.length];  //button right
		Button [] bL = new Button[peeps.length]; //button left
		//padding stuff
	    int padding_in_dp = 6;  // 6 dps
	    final float scale = getResources().getDisplayMetrics().density;
	    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
		
		for(int i = 0; i < peeps.length; i++){
			//(String name, String num, String trans, String amt, String desc, String date
			//peeps[i][0] + peeps[i][1] + peeps[i][2] + peeps[i][3] + peeps[i][4] + peeps[i][5];
			
			 bR[i] = new Button(this);
			 bL[i] = new Button(this);
			 
			 String dateRaw= peeps[i][5];
			 String[] dateFrag=dateRaw.split("-");
			 String yr=dateFrag[0].substring(2);  // year is 4 chars, 0,1,2,3  I want yr's last two digits
			 
			 String dateSmall=dateFrag[1]+ "/" +dateFrag[2] + "/" + yr;  // Month/Day
			 
			
			bL[i].setText( dateSmall + " " + peeps[i][4]);
						
			bR[i].setHint(peeps[i][6]);// the hint will be the rowID in sql :D 
			bL[i].setHint(peeps[i][6]);// the hint will be the rowID in sql :D
			 
			bR[i].setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
			bL[i].setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);			
			
			 
			bR[i].setTypeface(face, Typeface.BOLD);
			bL[i].setTypeface(face, Typeface.NORMAL);
			bR[i].setTextColor(Color.parseColor("#ffffff"));
			
			bR[i].setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 // lesson: View V is actually the  thing that we're listening on.. in this case its the button :D 
	            	 Button retb = (Button)v;
	            	 String buttonText = retb.getText().toString();
	            	 Log.i("MoneyReqs", buttonText);
	            	 Log.i("MoneyReqs", retb.getHint().toString());  // retrieving row id 
	            	 

	            	 Intent moneyDeets = new Intent(getApplicationContext(), MoneyDetailActivity.class);
	            	 moneyDeets.putExtra(MoneyReqsActivity.ROWID, retb.getHint().toString());
	            	 startActivity(moneyDeets);
	             }
	         });
			
			bL[i].setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 // lesson: View V is actually the  thing that we're listening on.. in this case its the button :D 
	            	 Button retb = (Button)v;
	            	 String buttonText = retb.getText().toString();
	            	 Log.i("MoneyReqs", buttonText);
	            	 Log.i("MoneyReqs", retb.getHint().toString());  // retrieving row id 
	            	 

	            	 Intent moneyDeets = new Intent(getApplicationContext(), MoneyDetailActivity.class);
	            	 moneyDeets.putExtra(MoneyReqsActivity.ROWID, retb.getHint().toString());
	            	 startActivity(moneyDeets);
	             }
	         });
			
			if ( peeps[i][2].equals("lender")){     
				bR[i].setBackgroundResource(R.drawable.button_cctrans_requestor_dark);
				bL[i].setBackgroundResource(R.drawable.button_cctrans_requestor_light);
				bL[i].setTextColor(getResources().getColor(R.color.green_text)); //lesson: okay colour resource MUST be named colors.xml
				
				bR[i].setText( " + $" + peeps[i][3] );
				
				amtTmp=prevAmt + Float.parseFloat(peeps[i][3]);
				Log.i("MoneyReqs", "requestor");
			}	
				//http://stackoverflow.com/questions/7991494/how-to-change-button-and-text-color-dynamically-on-android
			else {// you must be the requesteee
				bR[i].setBackgroundResource(R.drawable.button_cctrans_requestee_dark);
				bL[i].setBackgroundResource(R.drawable.button_cctrans_requestee_light);
				bL[i].setTextColor(getResources().getColor(R.color.red_orange_text));
				
				bR[i].setText( " - $" + peeps[i][3] );
				
				amtTmp=prevAmt - Float.parseFloat(peeps[i][3]);
				Log.i("MoneyReqs",  "requestee");
				
			}	
			
			prevAmt=amtTmp;
			
			leftHalf.addView(bL[i]);
			rightHalf.addView(bR[i]);
			
		}
		
		finalAmt=amtTmp;
		
		
		
		//Draw Line
		/*View line = new View(this);
		//set layout_width , layout_height (in pixels/Match_parent/wrap_content), layout_weight	
		LinearLayout.LayoutParams paramLine= new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT,dpToPix(5), 1.0f);		
		line.setLayoutParams(paramLine);
		line.setPadding(contactPadLeft, contactPadLeft, contactPadLeft, contactPadLeft);
		line.setBackgroundColor(getResources().getColor(R.color.chocolate));
			ll.addView(line);*/
		
		//final amount textview 
		TextView finalTv = new TextView(this);
		
		finalTv.setTypeface(face, Typeface.BOLD);
		////left, top, right, bottom
		finalTv.setPadding(contactPadLeft,contactPadTop , dpToPix(16), contactPadTop);
		finalTv.setTextSize(20f);
		finalTv.setTextColor(getResources().getColor(R.color.chocolate));
		finalTv.setGravity(Gravity.RIGHT);
		
		int retval = Float.compare(finalAmt, 0f); // retval > 0  -> finalAmt > 0f ... retval < 0 -->finalAmt <0f  else reval = 0 -> finalAmt = 0f 
				
		if (retval > 0) // finalAmt > 0f      +'ve 
			finalTv.setText(" You lent " +  Float.toString(finalAmt));
			
		else if (retval < 0){ // finalAmt <0f     -'ve
			finalAmt=finalAmt * -1;
			finalTv.setText("  You owe " + Float.toString(finalAmt) );
		}	
		else 
			finalTv.setText("   " + Float.toString(finalAmt) + "  crystal clear!" );
		
		sqlAst.close();
		
			ll.addView(finalTv);
		



			

		
		this.setContentView(sv);
	    
	}

	
	
	
	
	
	public int dpToPix(int dp){

	    final float scale = getResources().getDisplayMetrics().density;
	    int padding_in_px = (int) (dp * scale + 0.5f);
	    
	    return padding_in_px;
	}
	
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complete_contact_trans, menu);
		return true;
	}

}
