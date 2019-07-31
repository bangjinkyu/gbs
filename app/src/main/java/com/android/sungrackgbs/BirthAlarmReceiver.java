package com.android.sungrackgbs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class BirthAlarmReceiver extends BroadcastReceiver
{
    String Misidx = "";
    int count = 0;
    ArrayList<String> names = new ArrayList();
    Context pContext;
    String urlBirthList = "http://www.bwm.or.kr/attend/m_birth_today.php?misidx=";

    public void onReceive(Context paramContext, Intent paramIntent)
    {
        this.pContext = paramContext;
        this.count = 0;
        this.Misidx = paramIntent.getExtras().get("misidx").toString();
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.MONTH);
        int j = cal.get(Calendar.DATE);
       String str1;
       String str2;

        if (i + 1 < 10)
        {
            str1 = "0" + (i + 1);

        }else{
            str1 =String.valueOf(i + 1);
        }

        if (j  < 10) {
            str2 = "0" + j;
        }else{
            str2 = String.valueOf(j);
        }


        this.urlBirthList = (this.urlBirthList + this.Misidx + "&today=" + str1 + str2);
         new BirthMonthAsyncTask().execute(new String[] { this.urlBirthList });
    }

    private void getBirth(String paramString)
    {


            try
            {
                XmlPullParser localXmlPullParser;
                URL url =  new URL(paramString);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int eventType = localXmlPullParser.getEventType();
                String mMemidx = "";
                String mMemnm = "";
                String mPhone ="";
                String mBirth = "";
                String mSex = "";
                String mRole = "";
                String mMarriage = "";
                boolean ismemidx = false ,ismemnm=false,isphone = false ,isrole =false ,isbirth=false,issex=false,ismarriage=false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (localXmlPullParser.getName().equals("memidx")) {
                                ismemidx = true;
                            } else if (localXmlPullParser.getName().equals("memnm")) {
                                ismemnm = true;
                            } else if (localXmlPullParser.getName().equals("phone")) {
                                isphone = true;
                            } else if (localXmlPullParser.getName().equals("role")) {
                                isrole = true;
                            } else if (localXmlPullParser.getName().equals("birth")) {
                                isbirth = true;
                            } else if (localXmlPullParser.getName().equals("sex")) {
                                issex = true;
                            } else if (localXmlPullParser.getName().equals("marriage")) {
                                ismarriage = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if (ismemidx) {
                                mMemidx = localXmlPullParser.getText().replace("\n    ", "");
                                ismemidx = false;
                            } else if (ismemnm) {
                                mMemnm = localXmlPullParser.getText().replace("\n    ", "");
                                ismemnm = false;
                            } else if (isphone) {
                                mPhone = localXmlPullParser.getText().replace("\n    ", "");
                                isphone = false;
                            } else if (isrole) {
                                mRole = localXmlPullParser.getText().replace("\n    ", "");
                                isrole = false;
                            } else if (isbirth) {
                                mBirth = localXmlPullParser.getText().replace("\n    ", "");
                                isbirth=false;
                            }else if(issex){
                                mSex = localXmlPullParser.getText().replace("\n    ", "");
                                issex =false;
                            }else if(ismarriage){
                                mMarriage = localXmlPullParser.getText().replace("\n    ", "");
                                ismarriage=false;
                                this.count += 1;
                                this.names.add(mMemnm);
                            }


                            break;
                    }
                    eventType = localXmlPullParser.next();
                }


            } catch (Exception e) {

                return;
            }
    }



    private class BirthMonthAsyncTask extends AsyncTask<String, Integer, Long>
    {
        public BirthMonthAsyncTask() {}

        protected Long doInBackground(String... paramVarArgs)
        {
            BirthAlarmReceiver.this.count = 0;
            BirthAlarmReceiver.this.names = null;
            BirthAlarmReceiver.this.names = new ArrayList();
            BirthAlarmReceiver.this.getBirth(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong)
        {
             String  str = "";;
            if (BirthAlarmReceiver.this.count > 0)
            {

            }
            for (int i=0;i < BirthAlarmReceiver.this.names.size();i++) {

                    Intent intent  = new Intent(BirthAlarmReceiver.this.pContext, AddService.class);
                    ((Intent)intent).putExtra("misidx", BirthAlarmReceiver.this.Misidx);
                    PendingIntent  pendingIntent = PendingIntent.getActivity(BirthAlarmReceiver.this.pContext, 1, (Intent)intent, 0);
                     str = str + (String)BirthAlarmReceiver.this.names.get(i) + " ";

                     Notification.Builder builder = new Notification.Builder(BirthAlarmReceiver.this.pContext);
                         //    Notification(R.drawable.ic_notify, "모임에 오늘 생일자가 있습니다..", System.currentTimeMillis());
                    builder.setSmallIcon(R.drawable.ic_notify);
                    builder.setContentIntent(pendingIntent);
                   builder.setContentTitle("모임에 오늘 생일자가 있습니다..");
                     builder.setContentText(str + " 님이 생일 입니다" );

                     builder.setColor(Color.BLUE);
                      builder.setLights(Color.BLUE,1000,100);
                     builder.setTicker("모임에 오늘 생일자가 있습니다.."); //상태바
                   // localNotification.setLatestEventInfo(BirthAlarmReceiver.this.pContext, "모임에 오늘 생일자가 있습니다..", str, (PendingIntent)pendingIntent);
               //     localNotification.flags = 16;
                        builder.setAutoCancel(true);
                     NotificationManager localNotificationManager = (NotificationManager)BirthAlarmReceiver.this.pContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    localNotificationManager.notify(1, builder.build());

            }
        }
    }
}
