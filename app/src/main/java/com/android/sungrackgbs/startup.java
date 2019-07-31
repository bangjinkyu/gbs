package com.android.sungrackgbs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class startup extends BroadcastReceiver
{
    PendingIntent pendingIntent;

    public void onReceive(Context paramContext, Intent paramIntent)
    {
       PreferenceHelper preferenceHelper = new PreferenceHelper(paramContext);
        String localObject = preferenceHelper.getCode("BWM_BIRTH_ALARM");
        if ((localObject != null) && ((localObject).equals("RUN")))
        {
            Intent intent = new Intent(paramContext, BirthAlarmReceiver.class);
            intent.putExtra("misidx", preferenceHelper.getCode("MISIDX"));
            this.pendingIntent = PendingIntent.getBroadcast(paramContext, 1, intent, 0);
            Calendar calendar = Calendar.getInstance();
            ((AlarmManager)paramContext.getSystemService(ALARM_SERVICE)).setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 28800000L, this.pendingIntent);
        }
    }
}
