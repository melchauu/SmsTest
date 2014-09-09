package com.example.smstest;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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




public class delDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Integer num = getArguments().getInt("rowID");
    	final String numString = num.toString();
    	Log.i("deldialog","row id is " + num);
    	
        // Use the Builder class for convenient dialog construction    
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete this Request?");
        builder.setMessage("This removes the request and if applicable notifies the contact of deletion.")
               .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                   	//remove the request  
                  	    SQLiteAssistant sqlAst = new SQLiteAssistant(getActivity());   //lesson: getActivity will give you parent context
                  	    sqlAst.openDB();
                  	    
                  	    sqlAst.removeReq(numString);     
                  	    sqlAst.close();
                  	    //TODO  send the text message! 
                  	    
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
}