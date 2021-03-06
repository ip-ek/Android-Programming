package com.ipk.mobilodev;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.app.ActivityCompat;

public class Receiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            Log.d("takip", "Hello");
            if (bundle != null) {
                Log.d("takip", "Hello2");
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                   // long timstamp=messages[i].getTimestampMillis();
                    Date timstamp=new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    String formattedDate = sdf.format(timstamp);
                    String str="Gelen Mesaj\n"+"Tel No: " + messages[i].getDisplayOriginatingAddress() + "\nMesaj: " + messages[i].getMessageBody()+ "\nTarih:"+ formattedDate;
                    Toast.makeText(context, str,  Toast.LENGTH_LONG).show();
                    writeText(context,str);
                }
            }
        }else{  //aramadır (android.intent.action.PHONE_STATE)
            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) { //READ_CALL_LOG ????
                try {
                    Bundle bundle = intent.getExtras();
                    String phone_number = bundle.getString("incoming_number");
                    String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);

                    if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        Date callStartTime = new Date();
                        String callTime = sdf.format(callStartTime);
                        String str = "Gelen Arama: \n"+ "Tel No: " + phone_number + "\nTarih: " + callTime;
                        Toast.makeText(context, str , Toast.LENGTH_LONG).show();
                        writeText(context,str);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void writeText(Context context, String str){
        try{
            FileOutputStream fos = context.openFileOutput("receiver.txt", Context.MODE_APPEND);
            fos.write(("\n"+str+"\n").getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
