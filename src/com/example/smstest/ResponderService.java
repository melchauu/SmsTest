package com.example.smstest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;


// note instead of printf use TOAST! :P
public class ResponderService extends Service {
	
    //the action fired by the Android system when an SMS was received
    private static final String RECEIVED_ACTION =
                              "android.provider.Telephony.SMS_RECEIVED";
    private static final String SENT_ACTION="SENT_SMS";
    private static final String DELIVERED_ACTION="DELIVERED_SMS";
    

    String requester;
    String reply="blank txt";
    String rawText;
    SharedPreferences myprefs;
    String body="";
    String shanniceNum="8948390"; //8948390
    Boolean isShannice=false;
    // receiver is an member object but not a function, the part underneath that looks like 
    //a function is just an anonymous extension or implementation of the BroadcastReceiver class
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent in) {
            Log.v("ResponderService","On Receive");
            reply="";
            if(in.getAction().equals(RECEIVED_ACTION)) {
                Log.v("ResponderService","On SMS RECEIVE");

                Bundle bundle = in.getExtras();
                if(bundle!=null) {
                    Object[] pdus = (Object[])bundle.get("pdus");
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for(int i = 0; i<pdus.length; i++) {
                        Log.v("ResponderService","FOUND MESSAGE");
                        messages[i] =
                                SmsMessage.createFromPdu((byte[])pdus[i]);
                        // you use a byte array to convert something into raw data.. supposedly
                    }
                    for(SmsMessage message: messages) { // for (int item : arrayOfNumbers)  -> where item refers to the item in that indice in array
                        requestReceived(message.getOriginatingAddress());
                        body = message.getMessageBody();
                        body = body.replaceAll("\\s", ""); // remove all spaces 
                       //problem isolate: long reply strings are badd, but why?
                        
                        //body.contains("Shannice")||body.contains("shannice")) &&
                        if ( requester.contains(shanniceNum) && isShannice==false ) {
                        	reply="Hello .. this is a robot. Human mel's learning how to drive. Ah the clue: Instant Noodle _____ . Text me the answer OR 'Hint1'";
                        	isShannice=true;
                        	//"qweoriuqwpoeiruqwoiurqwoiu  rpowwwwwwwww wwwwwwwwww wwwwwwwwwwwwwwwww wwwwwwwwwwwwwww  wwwurpqowieurwoi  rupo  qwuroweiurpoqwurewer  qwer qwerqwerqwreqwerqw0uroqwereqwrqpowurepowiurpoieuropqiurowureopqurepoqiuerpoiwueropiuewropiuewpouropwuerpoiuewroiwueoriuqwpoireuqpowiurpoqwureqoeiurpqoiurepoqiurpqoieuropqieuropqiureopqiurepoiqeeur qwoieur opqieuropiqure opqiue opiu oiue opiu qe";
                        	//"Hello my favourite stalker ... this is a robot. I'm in driving class ( careful out there I might be on the road ;) \n I see your sleuthing skills are quite adeuqate. You mustn't disappoint now. \n Clue: Instant Noodle __________ \n  If stuck text me 'Hint1', otherwise text me your answer and I'll verify it.  ";
                            respond();
                            return ;
                        }
                        else if (body.contains("Hint1") && isShannice==true) {
                        	reply="1)It has four letters. \n Instant noodle _ _ _ _. Text me the answer OR 'Hint2";//\n Wanna another hint ? ( or if you like talking to robot Mel) Text me 'Hint2' \n \n When you've figured it out text me your answer and I'll verify it :)  ";
                            respond(); 
                            return ; 
                        }	
                        else if (body.contains("Hint2")&& isShannice==true){
                        	reply=" 2)I stole this lovely phrase from Fayy. It starts with the letter 'H'. Text me the answer OR 'Hint3.";//\n Anyhow onto 2)It starts with the letter 'H'. \n I doubt you're stuck... but if you want text me 'Hint3'\n \n When you've figured it out text me your answer and I'll verify it!!";
                        	respond();
                        	return ;
                        }
                        else if (body.contains("Hint3")&& isShannice==true){
                        	reply="Last hint, its last letter is 'R'.'Instant noodle H _ _ R. Text me the answer!'";
                        			//\n  \n When you've figured it out text me your answer and I'll verify it!!";   
                        	respond();
                        	return ;
                        }
                        else if ((body.contains("Hair")||body.contains("hair")||body.contains("HAIR"))&& isShannice==true ){
                        	reply="Correct! Happy birthday !!  and thank you for chatting with roboMel"+ requester;   
                        	respond();
                        	return ;
                        }
                        else  
                        	reply = "whatevs";
                       // "clue1: instant noodle \n  Text  'Hint1' for another hint if you need it"
                       // reply = message.getMessageBody(); // echo back msg from sender ... okay it worked
                       // rawText = message.getMessageBody(); 
         // ***** TODO: do the parsing here      
                        

                    }
                    //respond();//orignally placed here (1)
                      
                }
            }
        }
    };

    @Override
    public void onCreate() {
    	
    	// service starts execution here first! 
        super.onCreate();
        Log.d("debug", "onCreate" );
        myprefs = PreferenceManager.getDefaultSharedPreferences(this);

        IntentFilter filter = new IntentFilter(RECEIVED_ACTION);
        registerReceiver(receiver, filter);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return (super.onStartCommand(intent, flags,startId));
    }
    
    public void requestReceived(String f) {
        Log.v("ResponderService","In requestReceived");
        requester=f;
    }
    


    public void respond() {
        Log.v("ResponderService","Responding to " + requester);
        
        //reply = myprefs.getString("reply",
        //                   "Thank you for your message. I am busy now."
        //                   + "I will call you later.");
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
    }
	@Override
	public IBinder onBind(Intent intent) {
		return null;
// not being used so return null 
	}
}
