package com.example.smstest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class MoneyDetailActivity extends Activity {

	public final static String ISTOTALLED = "com.example.SmsTest.ISTOTALLED";// 1 means true, 0 false
	public final static String FINALTOTES = "com.example.SmsTest.FINALTOTES";
	public final static String SPECIFIC_CONTACT_NAME = "com.example.SmsTest.SPECIFIC_CONTACT_NAME";
	public String finalAmtStr;
	public String nameToSend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_money_detail);
		
		//*** GET LAYOUT IDS *****/
		TextView nameTv=(TextView)findViewById(R.id.ContactNm);
		TextView dateTv=(TextView)findViewById(R.id.dateFld);
		TextView amtTv=(TextView)findViewById(R.id.amountTxt);
		TextView transTv=(TextView)findViewById(R.id.transTxt);
		TextView forTv=(TextView)findViewById(R.id.forTxt);
		TextView descTv=(TextView)findViewById(R.id.descTxt);
		TextView verfTv=(TextView)findViewById(R.id.verified);
		TextView netTv=(TextView)findViewById(R.id.netTxt);
		TextView totesTv=(TextView)findViewById(R.id.netAmountTxt);
		Button paidBtn=(Button)findViewById(R.id.paid);
		Button delBtn=(Button)findViewById(R.id.del);
		
		// set button pictures
		paidBtn.setBackgroundResource(R.drawable.btn_checkmark_bdbdbd);
		delBtn.setBackgroundResource(R.drawable.btn_x_bdbdbd);
		
		
		Typeface face=Typeface.createFromAsset(getAssets(), "fonts/TREBUC.TTF");
		
	    Intent intent = getIntent();
	    String row = intent.getStringExtra(MoneyReqsActivity.ROWID);
		    
		
	    
	    // ****  GET THOSE SPECIFIC ROWS corresponding to CONTACT NM *****
	    SQLiteAssistant sqlAst = new SQLiteAssistant(this);   
   	    sqlAst.openDB(); 
   	    Log.i("MoneyDeetsReqs", "affter open DB");
	    //String _id String name, String num, String trans, String amt, String desc, String date

	    String selection = "_id ='"+ row +"'";  //
	    //String selection = DISPLAY_NAME+" like'" + givename +"%'";
		String[][] peepsDeets= sqlAst.getSpecific(selection,null);
		Log.i("MoneyDeetsReqs", "peepDeets string array made");
        

		//contact_name, contact_num, transaction_side, amt, description, date_incr, _id 

		String trans=peepsDeets[0][2];
		
		int TextColr;
		int backgrndColr;
		String transOut;
		
		// Set Colours for TextView
		if( trans.equals("lender")){
			//nameTv.setBackgroundColor(Color.parseColor("#66CC33"));
			nameTv.setBackgroundColor(getResources().getColor(R.color.green_dark));//requestor
			backgrndColr= getResources().getColor(R.color.green_dark);
			transOut="You lent";
			//TextColr="#3CB371";//MEDIUMSEAGREEN
			TextColr=getResources().getColor(R.color.green_text);
		}	
		
		
		else{
			nameTv.setBackgroundColor(getResources().getColor(R.color.red_orange_dark));//requestee
			backgrndColr= getResources().getColor(R.color.red_orange_dark);
			transOut="You owe";
			
			//TextColr="#FF7F50";//CORAL
			TextColr=getResources().getColor(R.color.red_orange_text);//CORAL
			
		}
		
		Log.i("MoneyReqs", "determined trans color");
		
		String name=peepsDeets[0][0];
			nameTv.setText(name);
			nameToSend=name;
			nameTv.setTypeface(face, Typeface.NORMAL);
			
		
		String num=peepsDeets[0][1];
		String amt=peepsDeets[0][3];
			amtTv.setText(transOut + " " + amt);
			amtTv.setTextColor(TextColr);
			amtTv.setTypeface(face, Typeface.BOLD);
			
		String desc=peepsDeets[0][4];
		
			forTv.setTextColor(TextColr);
			forTv.setText("for " + desc);
			forTv.setTypeface(face, Typeface.ITALIC);
			
		String date=peepsDeets[0][5];
			dateTv.setText(date);
			dateTv.setTextColor(TextColr);
			dateTv.setTypeface(face, Typeface.ITALIC);
			
		netTv.setTextColor(TextColr);
		netTv.setTypeface(face, Typeface.ITALIC);
		
		totesTv.setTextColor(TextColr);
		totesTv.setTypeface(face, Typeface.NORMAL);
			
		//TODO: need to set ACKNOWLEDGED/verified FIELD in DB (whether or not ther person accepted your request,,, if this is a request  the user accepted, Acknowledged is true.	
		
		verfTv.setText("Contact HAS agreed to request");
		//verfTv.setText("Contact HAS NOT agreed to request");
		verfTv.setBackgroundColor(backgrndColr);
		
		
		//************ Calculate NET Amount ************************/
				
				//go into DB and search for all entries of ContactNm
	    selection = "contact_name ='"+ name + "'";  //
	    //contact_name[0], contact_num[1], transaction_side[2], amt[3], description, date_incur, _id 	    	    		
		String allTrans[][]= sqlAst.getSpecific(selection,"date_incur" + " DESC");

			//temp strings;
			String transTmp;
			float prevAmt=0;
			float amtTmp=0;
			float finalAmt=0;
			
		for(int i = 0; i < allTrans.length; i++){
			// extract transaction_side + amt .... requestor= +'ve              requestee= -'ve
			transTmp=allTrans[i][2];
			
			if (transTmp.equals("lender"))
				amtTmp=prevAmt + Float.parseFloat(allTrans[i][3]);			
			else 
				amtTmp=prevAmt - Float.parseFloat(allTrans[i][3]);
			
			prevAmt=amtTmp;
					
			
		}
		
		finalAmt=amtTmp;
		
		int retval = Float.compare(finalAmt, 0f); // retval > 0  -> finalAmt > 0f ... retval < 0 -->finalAmt <0f  else reval = 0 -> finalAmt = 0f 
		
		
		if (retval > 0) // finalAmt > 0f      +'ve 
			totesTv.setText("  You lent " + Float.toString(finalAmt)  );
		else if (retval < 0){ // finalAmt <0f     -'ve
			finalAmt=finalAmt * -1;
			totesTv.setText("  You owe " + Float.toString(finalAmt) );
		}	
		else 
			totesTv.setText("   " + Float.toString(finalAmt) + "  crystal clear!" );
		sqlAst.close();
		
		finalAmtStr=Float.toString(finalAmt);
		
		//
		Button explainNetTotes = (Button) findViewById(R.id.iron_details);
		explainNetTotes.setBackgroundResource(R.drawable.iron_128);   //setting the image for the button
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.money_detail, menu);
		return true;
	}
	
	public void contactFullTrans(View view){
 		Intent getFull = new Intent(getApplicationContext(), CompleteContactTransActivity.class);
		getFull.putExtra(ISTOTALLED, "1");
		// 1 means true we have totalled the net transactions of contact 
		//( for in case we call the class without totalling the nettrans , ie we call this from the start screen
		getFull.putExtra(FINALTOTES, finalAmtStr);
		getFull.putExtra(SPECIFIC_CONTACT_NAME, nameToSend);
		
		startActivity(getFull);
	}
	

}
