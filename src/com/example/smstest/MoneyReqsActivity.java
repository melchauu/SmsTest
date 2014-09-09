package com.example.smstest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MoneyReqsActivity extends Activity {
	
	public final static String ROWID = "com.example.SmsTest.ROWID";
	public final static String SQLSELECTION = "com.example.SmsTest.SQLSELECTION";
	
	//lesson learned: you MUST call this string above when rerunning the activity that this activity launches, if you call it multiple times
	// you must call moneyReqsActivty.ROWID.... ( format  is  class.string ) 
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
		String[][] peeps= sqlAst.getAllReqs("date_incur" + " DESC");//lesson: append DESC or ACS to col name , so its sorted by col name DESC or col name ACE respectively
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
		getMenuInflater().inflate(R.menu.money_reqs, menu);
		
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_home:
	        	Log.i("MoneyReqs menu", "home");
         		Intent hometime = new Intent(getApplicationContext(), StartActivity.class);        		
        		startActivity(hometime);
	            return true;
	            
	            
	        case R.id.action_Search:
	        	Log.i("MoneyReqs menu", "search");
	        	
	        	final Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.dialog_search);
				dialog.setTitle("Search for Requests");					 
				// set the custom dialog components - text, image and button
				 final AutoCompleteTextView actv = (AutoCompleteTextView) dialog.findViewById(R.id.autocompleteContact);
				 ArrayAdapter<String> autotv_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.tv_ContactName, getAllContactsList()); 
		 		 actv.setAdapter(autotv_adapter);	 					 

				
				final TextView amtTv = (TextView) dialog.findViewById(R.id.Amount_fill);
				final TextView descTv = (TextView) dialog.findViewById(R.id.Desc_fill);
				
				Button goBtn = (Button)dialog.findViewById(R.id.btn_go);
				Button cancelBtn=(Button)dialog.findViewById(R.id.btn_cancel);
				
				cancelBtn.setOnClickListener(new OnClickListener() {@Override
					public void onClick(View v) {
					dialog.dismiss();
				}});
				
				goBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						
						String amtStr=null;
						String amtStr_sql= "\n";
						boolean amtInt=false;
						amtStr= amtTv.getText().toString();
						if (!amtStr.equals("") ){ 
							
							amtStr_sql=" amt = '" + amtStr + "'" ;
							amtInt=true;
						}
						
													
						
						
						String descStr = null;
						String descStr_sql= "\n";
						boolean descInt=false;
						descStr=descTv.getText().toString();
							if (!descStr.equals("")  ){
								
								descStr_sql=" description = '" + descStr + "'" ;
								descInt=true;
							}	
						
						String acStr=null;
						String acStr_sql= "\n";
						boolean acInt=false;
						acStr=actv.getText().toString();
							if (!acStr.equals("")){
								
								acStr_sql = " contact_name = '" + acStr + "'" ;	
								acInt=true;
							}	
						
						// construct selection 
							String selectionRaw = null;
							String[] selectionFrag=null;
							String selection = null;
							
							 
							
							if (amtInt && descInt && acInt){
								//all fields filled yay easy 
								selection = amtStr_sql + " AND " + descStr_sql + " AND " + acStr_sql;
								Log.i("moneyreqsactivity", selection);
							}
							
							else if (amtInt == false && descInt == false && acInt == false){
								
								dialog.dismiss(); // going back to all reqs
								
							}
							
							else {
								selectionRaw = amtStr_sql + "\n" + descStr_sql + "\n" + acStr_sql;	
								 selectionFrag=selectionRaw.split("\n");								  
								 List<String> listFrag = new LinkedList<String>(Arrays.asList(selectionFrag));// make a linked list bc when calling Arrays.asList yout can't add or remove from this list :(
								 int numFrags = selectionFrag.length;
								 int newNumFrags = numFrags;
								 String temp=null;
								 for (int i =0; i < newNumFrags ; i++)  
							      {  
									 temp=listFrag.get(i);
							         if (listFrag.get(i).equals("")||listFrag.get(i).equals(" ")){
							        	 listFrag.remove(i);	
							        	 newNumFrags=newNumFrags-1;
							        	 i=i-1;
							         }
							      }  
								 
								 // add  all the strings back up
								 selectionRaw = listFrag.get(0);
								 for ( int i = 1 ; i< newNumFrags; i++)
									 selectionRaw = selectionRaw + " AND " + listFrag.get(i);
	

									 selection=selectionRaw ;
									 
								 
								 Log.i("moneyreqsactivity", selection);
							}
							
			         		Intent search = new Intent(getApplicationContext(), SearchActivity.class);
			        		search.putExtra(SQLSELECTION, selection);
			        		startActivity(search);
						
					}});

					 
					 
				
					dialog.show();
	            return true;
	            

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	




public List<String> getAllContactsList() {
	//http://beginandroiddev.blogspot.ca/2013/04/display-contacts-in-autocompletetextview.html

			    	ArrayList<String> names = new ArrayList<String>();  // array for autocompletetextview 

			    	
			        Uri CONTACTS_CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;		       	       


			        // all these strings above  contactscontract.contacts.XXX is just an address for a row of where we expect the desired for XXX data to be
			        
			       ContentResolver contentResolver = getContentResolver();
			        
			        Cursor cursor = contentResolver.query(CONTACTS_CONTENT_URI,null, null, null, null);
			        
			        // look in the contact_content (table) specifically and look for something that has display_name like givename 
			        
			        if ( cursor.getCount() > 0 ) {
			        		
			            while ( cursor.moveToNext() ) {

			                //String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
			                String name = cursor.getString(cursor.getColumnIndex( ContactsContract.Contacts.DISPLAY_NAME ));

			                


			                 
			                    names.add( name);
			                   

			                   
		  		                names.add("\n");
		  		               
			
			            }

			            
			        }
			        
			        

			        
			        return names;
			        
			    }
}

