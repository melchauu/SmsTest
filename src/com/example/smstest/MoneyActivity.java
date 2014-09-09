package com.example.smstest;


import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;





import java.math.BigDecimal;
import java.util.*;



public class MoneyActivity extends Activity {

	public AutoCompleteTextView actv; 

	public Spinner numSpinner;
	public EditText AmountElmt;
	public EditText DescElmt;
	public Button	DateElmt;
	
	public String transSide;


	public TextView contactSrchRes;
	//public TextView outputText;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		
		
		
		
		Intent intent = getIntent();
		String whoru = intent.getStringExtra(StartActivity.IDENT);
		transSide=whoru;
		
		Log.d("MoneyActivity",  "who r u " + transSide );
		
		if( transSide.equals("borrower")){
			setContentView(R.layout.activity_money_orange);
		}					
		else
			setContentView(R.layout.activity_money_green);
		
		// getting all the fields that we'll need to insert records
		AmountElmt=(EditText)findViewById(R.id.Amount_fill);
		DescElmt=(EditText)findViewById(R.id.Desc_fill);
		DateElmt=(Button)findViewById(R.id.Date_fill);
		TextView TitleTrans=(TextView)findViewById(R.id.transSide);
		
		//set font
		Typeface face=Typeface.createFromAsset(getAssets(), "fonts/TREBUC.TTF");
		TitleTrans.setTypeface(face, Typeface.NORMAL);
		DateElmt.setTypeface(face, Typeface.NORMAL);
		DescElmt.setTypeface(face, Typeface.NORMAL);
		AmountElmt.setTypeface(face, Typeface.NORMAL);
		//actv.setTypeface(face, Typeface.NORMAL);

		

		 actv = (AutoCompleteTextView) findViewById(R.id.autocompleteContact);

		 ArrayAdapter<String> autotv_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.tv_ContactName, getAllContactsList()); 
		 
		 actv.setAdapter(autotv_adapter);
		 //http://stackoverflow.com/questions/16923663/autocompletetextview-setonitemselectedlistener-not-working/16940518#16940518
		 
		 numSpinner = (Spinner) findViewById(R.id.num_spinner);
		 
		 // make dummy adapter  wiht dummy array of spaces
		 //for numSpinner so the field has the field has the right padding
		 ArrayList<String> dummyArray = new ArrayList<String>();
		 dummyArray.add("    ");
		 ArrayAdapter<String> dummy_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.tv_ContactName, dummyArray);     		 
 		 numSpinner.setAdapter(dummy_adapter);
		
		    actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		        @Override
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {

		        	
		        	String selName = actv.getText().toString();
		        	
		        	List<String> contactNums = fetchNumbers(selName);	        	
		        	ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.tv_ContactName, contactNums);     		 
		    		numSpinner.setAdapter(spin_adapter);    		
		        	
		        	
		        }
		    });
		    		

		 //tv_ContactName is the name of the textview in list_item.xml
		 //getAllContactsList returns a list
		 
		 
		
		
	}
	
	public List<String> fetchNumbers(String contactNm ) {
		String givename = contactNm; 
    	String phoneNumber = null;
    	   	
    	ArrayList<String> numbers = new ArrayList<String>();  // array for autocompletetextview 

    	
        Uri CONTACTS_CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;		       	       
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        // all these strings above  contactscontract.contacts.XXX is just an address for a row of where we expect the desired for XXX data to be
         ContentResolver contentResolver = getContentResolver();
        
        // query 
        /*public final Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal)
         *
         * 	- uri				The URI, using the content:// scheme, for the content to retrieve.
			- projection		A list of which columns to return. Passing null will return all columns, which is inefficient.
			- selection			A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given URI.    ie column_name like '%query%';  
			- selectionArgs		You may include ?s in selection, which will be replaced by the values from selectionArgs, in the order that they appear in the selection. The values will be bound as Strings.
			- sortOrder		choose a column to sort the rows by. ie if you choose  the DISPLAY_NAME collumn, then it will be alphabetical
			
			Use question mark parameter markers such as 'phone=?' instead of explicit values in the selection parameter, so that queries that differ only by those values will be recognized as the same for caching purposes.

         * */
        
        //http://stackoverflow.com/questions/6330151/how-to-get-a-contacts-number-from-contact-name-in-android       
         String selection = DISPLAY_NAME+" like'" + givename +"%'";
         Cursor cursor = contentResolver.query(CONTACTS_CONTENT_URI,null, selection, null, DISPLAY_NAME);
        
        
        if ( cursor.getCount() > 0 ) {
        		
            while ( cursor.moveToNext()) {

                //String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                                  
                    //String exactSelection =  DISPLAY_NAME+" like'%" + name +"%'";
                    String exactSelection =  DISPLAY_NAME+" = '" + name +"'";
                    // Query and loop for every phone number of the contact
                    
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, exactSelection, null, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        
                        if (phoneNumber != null){
                        	
                        	numbers.add("  "+ phoneNumber);
                        }	
                        
                        else {
                        	
                        	numbers.add("\n Phone number: is empty" );
                        }
                    }

                    phoneCursor.close(); 		                             		                	
            }            
        }  
        cursor.close();
        
        return numbers;
	}
	

// this lovely example is from : http://examples.javacodegeeks.com/android/core/provider/android-contacts-example/
	// keep this :) we made it...but then we didn't need it.... lol. 
	/*
		    public void fetchContacts(View view ) {

		    	
		    	EditText contactgiven = (EditText) findViewById(R.id.Contact_fill);
		    	 
		    	
		    	
		    	String givename = contactgiven.getText().toString();// name that we shall search for 
		    	String phoneNumber = null;
		    	
		    	int iter=0;

		    	ArrayList<String> names = new ArrayList<String>();  // array for autocompletetextview 

		    	
		        Uri CONTACTS_CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;		       	       
		        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
		        // all these strings above  contactscontract.contacts.XXX is just an address for a row of where we expect the desired for XXX data to be
		        
		        StringBuffer output = new StringBuffer();
		        ContentResolver contentResolver = getContentResolver();
		        
		        // query 
		        /*public final Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal)
		         *
		         * 	- uri				The URI, using the content:// scheme, for the content to retrieve.
					- projection		A list of which columns to return. Passing null will return all columns, which is inefficient.
					- selection			A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given URI.    ie column_name like '%query%';  
					- selectionArgs		You may include ?s in selection, which will be replaced by the values from selectionArgs, in the order that they appear in the selection. The values will be bound as Strings.
					- sortOrder		choose a column to sort the rows by. ie if you choose  the DISPLAY_NAME collumn, then it will be alphabetical
					
					Use question mark parameter markers such as 'phone=?' instead of explicit values in the selection parameter, so that queries that differ only by those values will be recognized as the same for caching purposes.

		         
		        
		        //http://stackoverflow.com/questions/6330151/how-to-get-a-contacts-number-from-contact-name-in-android

		        
		     // >>> search by firstname    
		        
		        String selection = DISPLAY_NAME+" like'" + givename +"%'";
		        Cursor cursor = contentResolver.query(CONTACTS_CONTENT_URI,null, selection, null, DISPLAY_NAME);
		        
		        // look in the contact_content (table) specifically and look for something that has display_name like givename 
		        
		        if ( cursor.getCount() > 0 ) {
		        		
		            while ( (cursor.moveToNext()) &&  ( iter < 2) ) {

		                //String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
		                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

		                

		                    output.append("\n First Name:" + name);
		                    //String exactSelection =  DISPLAY_NAME+" like'%" + name +"%'";
		                    String exactSelection =  DISPLAY_NAME+" = '" + name +"'";
		                    // Query and loop for every phone number of the contact
		                    
		                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, exactSelection, null, null);


		                    while (phoneCursor.moveToNext()) {
		                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
		                        
		                        if (phoneNumber != null){
		                        	output.append("\n Phone number:" + phoneNumber);
		                        	names.add("\n Phone number:" + phoneNumber);
		                        }	
		                        
		                        else {
		                        	output.append("\n Phone number:" + "is empty");
		                        	names.add("\n Phone number: is empty" );
		                        }


		                    }

		                    phoneCursor.close(); 
	  		                output.append("\n");
	  		                names.add("\n");
	  		                iter=iter+1;	
		
		            }

		            
		        }
		        
		        
		        iter=0; //reset the count
		         
		        
		     // >>> search by lastname note the space  in the var selection.   Display_name  like '%SPACE givename %'    
		        
		         selection = DISPLAY_NAME+" like '% " + givename +"%'";
		         cursor = contentResolver.query(CONTACTS_CONTENT_URI,null, selection, null, DISPLAY_NAME);		        
		        
		        
		        // look in the contact_content (table) specifically and look for something that has display_name like givename 
		        
		         if ( cursor.getCount() > 0   )  {
	
		            while ((cursor.moveToNext()) &&  ( iter < 2) ) {

		                //String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
		                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

		                

		                    output.append("\n First Name:" + name);
		                    //String exactSelection =  DISPLAY_NAME+" like'%" + name +"%'";
		                    String exactSelection =  DISPLAY_NAME+" = '" + name +"'";
		                    // Query and loop for every phone number of the contact
		                    
		                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, exactSelection, null, null);

		                    while (phoneCursor.moveToNext()) {
		                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
		                        
		                        if (phoneNumber != null){
		                        	output.append("\n Phone number:" + phoneNumber);
		                        	names.add("\n Phone number:" + phoneNumber);
		                        }
		                        
		                        else {
		                        	output.append("\n Phone number:" + "is empty");
		                        	names.add("\n Phone number: is empty");
		                        }		
		                    }

		                    phoneCursor.close();
		 
	        	
		                output.append("\n");
		                names.add("\n");
		                iter=iter+1;
		            }
		            
		           
		        }
		        
		        
		        // todo add entryies into string array 'names'
		        outputText.setText(output); 
		        cursor.close();
		    }

	*/	    
		    
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
		 
		    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.money, menu);
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
	
	public void sendReq(View view){
		
		//TODO: text the number

		

		// save this request into persistent data
		SQLiteAssistant sqlAst; 
        sqlAst = new SQLiteAssistant(MoneyActivity.this);
        //TODO: DELETE thE LINE BELOW LATER:  currently for debugging purposes so I have a fresh db every run
       // getApplicationContext().deleteDatabase("usingsqlite.db");
        
        sqlAst.openDB();
        
        //getting all the fields for the row
        //BigDecimal amount=new BigDecimal(AmountElmt.getText().toString());
        
        String amount=AmountElmt.getText().toString();
        Log.i("Amount is", amount);
        String description=DescElmt.getText().toString();
        String dateRaw=DateElmt.getText().toString();
        // must format raw date into this format YYYY-MM-DD currently raw date is MM/DD/YYYY
        String[] dateFrag=dateRaw.split("/");
        String month = dateFrag[0];
        String day = dateFrag[1];
        String year = dateFrag[2];
        String date = year + "-" + month + "-" + day;
        
        
        String contactName=actv.getText().toString();
        String contactNum=numSpinner.getSelectedItem().toString();
        
        sqlAst.insertReq(contactName,contactNum,transSide,amount,description,date);

        String[][] countries = sqlAst.getAllReqs(null);
        
        // Print out the values to the log
        for(int i = 0; i < countries.length; i++)
        {
            Log.i(this.toString(), countries[i][0] + countries[i][1] + countries[i][2] + countries[i][3] + countries[i][4] + countries[i][5] + countries[i][6] );
        }
 
        sqlAst.close();
		// launch new activity that wil displayy all requests
		Intent moneyReqsInt = new Intent(this, MoneyReqsActivity.class);		
		startActivity(moneyReqsInt);

	  

		
	}
	
	// fix this function up to properly text the number
	 /* public void respond() {
	        Log.v("ResponderService","Responding to " + requester);
	        

			   //getString(String key , String defValue)  -> note this getString applies only to type sharedPreferences
			   //key	The name of the preference to retrieve.
			   //defValue	Value to return if this preference does not exist
			   // sharedPrefrerences => Store private primitive data in key-value pairs
	        SmsManager sms = SmsManager.getDefault();
	        Intent sentIn = new Intent(SENT_ACTION);
	        PendingIntent sentPIn = PendingIntent.getBroadcast(this,
	                                                                0,sentIn,0);

	        Intent deliverIn = new Intent(DELIVERED_ACTION);
	        PendingIntent deliverPIn = PendingIntent.getBroadcast(this,
	                                                           0,deliverIn,0);
	        ArrayList<String> Msgs = sms.divideMessage(reply);
	        ArrayList<PendingIntent> sentIns = new ArrayList<PendingIntent>();
	        ArrayList<PendingIntent> deliverIns =
	                                         new ArrayList<PendingIntent>();

	        for(int i=0; i< Msgs.size(); i++) {
	            sentIns.add(sentPIn);
	            deliverIns.add(deliverPIn);
	        }

	        sms.sendMultipartTextMessage(requester, null,
	                                        Msgs, sentIns, deliverIns);
	    } */

}
