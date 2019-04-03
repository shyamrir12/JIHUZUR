package com.example.awizom.jihuzur.LoginRegistrationActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.awizom.jihuzur.EmployeeActivity.VerifyPhoneActivityEmployeee;

public class ReadSms extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {

            if (bundle != null)
            {

                final Object[] pdusObj = (Object[]) bundle.get("Login is:");
                for (int i = 0; i < pdusObj.length; i++)
                {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();

                    try
                    {

                        if (senderNum.equals("ADSUPORT"))
                        {
                            VerifyPhoneActivityEmployeee Sms = new VerifyPhoneActivityEmployeee();
                            Sms.recivedSms(message );
                        }
                        else if(senderNum.equals("AXSUPORT"))
                        {VerifyPhoneActivityEmployeee Sms = new VerifyPhoneActivityEmployeee();
                            Sms.recivedSms(message );}

                    } catch(Exception e){}
                }
            }

        } catch (Exception e) {}
    }

}