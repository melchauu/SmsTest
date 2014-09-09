package com.example.smstest;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;




public class PaidDialogFragment extends DialogFragment {
	

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Integer num = getArguments().getInt("rowID");
    	final String numString = num.toString();
    	Log.i("paiddialog","row id is " + num);
    	
    	// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());        
        builder.setTitle("This Request Has Been Paid");
        builder.setMessage("This removes the request and if applicable notifies the contact for their confirmation.")
               .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	 
               	    SQLiteAssistant sqlAst = new SQLiteAssistant(getActivity());   //lesson: getActivity will give you parent context
               	    sqlAst.openDB();
               	    // extract the row, to txt the details to contact to notify them 
               	    
               	    
               	    //remove the request 
               	    sqlAst.removeReq(numString);     
               	    sqlAst.close();
               	    //TODO  send the text message! 
               	   // sendMessage("test");
               	    
               	    //launch list of reqs
            		Intent moneyReqsInt = new Intent(getActivity(), MoneyReqsActivity.class);		
            		startActivity(moneyReqsInt);
                      
                   }
               })
               .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   
                	   
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
	public void sendMessage(String phone_num_txt) {
		String SENT_SMS_FLAG = "SENT_SMS";
		String DELIVER_SMS_FLAG = "DELIVER_SMS";
		
		SmsManager mySMS = SmsManager.getDefault();
		String msg = "(\"description\": \"Feed me\",\"type\": \"paid\",\"transide\": \"borrower\",\"amount\": \"50\",\"date\": \"2014-9-1\")";
		//must parse this string have to make the bracket from this  ()  to {} curly brace to comply with json fromat 
		String destination = "6478348213";
		
		// var to check is SMS is sent to the SMS "post office"
		Intent sentIn = new Intent(SENT_SMS_FLAG);
		PendingIntent sentPIn=PendingIntent.getBroadcast(getActivity(),0,sentIn,0);
		
		BroadcastReceiver sentReceiver = new BroadcastReceiver(){
		     @Override public void onReceive(Context c, Intent in) {
		         switch(getResultCode()){
		             case Activity.RESULT_OK:
		                 //sent SMS message successfully;
		            	 Log.i("paiddialog_send_sms", "success");
		                 break;
		             default:
		                 //sent SMS message failed
		            	 Log.i("paiddialog_send_sms", "failure");
		                 break;
		          }
		      }
		};
		
		getActivity().registerReceiver(sentReceiver, new IntentFilter(SENT_SMS_FLAG));
		
		ArrayList<String> multiSMS = mySMS.divideMessage(msg);
		ArrayList<PendingIntent> sentIns = new ArrayList<PendingIntent>();
		

		for(int i=0; i< multiSMS.size(); i++){
		    sentIns.add(sentPIn);		    
		}
		//mySMS.sendTextMessage(destination, null, "test", null, null);
   	 
		//mySMS.sendMultipartTextMessage(destination, null, multiSMS, sentIns, null);
		Log.i("paiddialog send sms", "sending sms test");
		
				
		
	}
}