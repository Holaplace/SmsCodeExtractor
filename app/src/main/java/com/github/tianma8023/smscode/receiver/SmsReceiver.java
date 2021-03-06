package com.github.tianma8023.smscode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;

import com.github.tianma8023.smscode.entity.SmsMsg;
import com.github.tianma8023.smscode.service.SmsCodeHandleService;
import com.github.tianma8023.smscode.utils.SmsMessageUtils;
import com.github.tianma8023.smscode.utils.XLog;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

    @Override
    public void onReceive(Context context, Intent intent) {
        XLog.d("SmsReceiver#onReceived() - {}", intent.getAction());
        if (SMS_RECEIVED.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus == null)
                    return;

                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                if (messages.length != 0) {
                    String body = SmsMessageUtils.getMessageBody(messages);
                    String sender = messages[0].getOriginatingAddress();
                    long date = System.currentTimeMillis();

                    SmsMsg smsMsg = new SmsMsg();
                    smsMsg.setBody(body);
                    smsMsg.setSender(sender);
                    smsMsg.setDate(date);

                    Intent smsCodeHandleSvc = new Intent(context, SmsCodeHandleService.class);
                    smsCodeHandleSvc.putExtra(SmsCodeHandleService.EXTRA_KEY_SMS_MESSAGE_DATA, smsMsg);
                    ContextCompat.startForegroundService(context, smsCodeHandleSvc);
                }
            }
        }
    }
}
