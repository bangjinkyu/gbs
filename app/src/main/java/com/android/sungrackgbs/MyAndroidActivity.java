package com.android.sungrackgbs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MyAndroidActivity extends Activity {
    private String TAG ="MyAndroidActivity";
    private String Grade = "";
    private String Misidx = "";  //목장순서 31  32 33 34 35 36 37  38 39 40 41 42 42  29 교육팀  46 본부 28새가족팀
    private String Misnm = "";  //목장  네임 BWM1목장 Bwm 2목장 bwm3목장
    private String Useyn = "";
    private LinearLayout baseContainer; //목장 목록
    private GridView mGridView; // 목장리스트
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<HashMap<String, Object>> btnMission;// 슈퍼관리자 권한에서 전체 메뉴 보기
    private Button btn_setting;
    private Button btn_attend;
    private Intent intent;
    private Intent intentOption;
    ProgressDialog pd;
    String urlAuth = "http://www.bwm.or.kr/attend/m_auth_g.php?phone=";//
    String urlMissionList = "http://www.bwm.or.kr/attend/m_mission_g_list.php";  //gbs list

    private final static  int MT_PERMISSION_REQUEST_CODE = 100;
    private String phoneNumber = "";
    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.

    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.READ_PHONE_STATE, // 카메라
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};  // 외부 저장소


    private void getAuth(String paramString) {

            try {
                XmlPullParser localXmlPullParser;
                URL url =  new URL(paramString);
                InputStream is = url.openStream();
                Log.d(TAG, "getAuth to URL : " + url);

                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(is, "UTF-8");

                int parserEvent  = localXmlPullParser.getEventType();

                String str ="";
                boolean isgrade = false, ismisidx = false, ismisnm = false ,isuseun =false;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                   switch (parserEvent) {
                       case XmlPullParser.START_DOCUMENT:
                           break;
                       case XmlPullParser.END_DOCUMENT:
                           break;
                       case XmlPullParser.START_TAG:
                           if (localXmlPullParser.getName().equals("grade")) {
                               isgrade=true;
                               str="grade";
                           }else if(localXmlPullParser.getName().equals("misidx")){
                               ismisidx=true;
                               str="misidx";
                           }else if(localXmlPullParser.getName().equals("misnm")){
                               ismisnm =true;
                               str="misnm";
                           }else if(localXmlPullParser.getName().equals("useyn")){
                               str="useyn";
                           }


                           break;
                       case XmlPullParser.END_TAG:
                           break;
                       case XmlPullParser.TEXT:
                           if(str.equals("grade") &&  this.Grade.equals("")){
                               this.Grade =localXmlPullParser.getText();
                              Log.d(TAG,"Grade= "+Grade);
                           }else if((str.equals("misidx") && this.Misidx.equals(""))){
                               this.Misidx =localXmlPullParser.getText();
                               Log.d(TAG,"Misidx= "+Misidx);
                           }else if((str.equals("misnm") && this.Misnm.equals(""))){
                               this.Misnm =localXmlPullParser.getText();
                               Log.d(TAG,"Misnm= "+Misnm);
                           }else if((str.equals("useyn") && this.Useyn.equals(""))) {
                               this.Useyn = localXmlPullParser.getText();
                               Log.d(TAG, "Useyn= " + Useyn);
                           }
                           break;
                   }
                   parserEvent = localXmlPullParser.next();
               }//end while


            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
    }



   private void getMissionList(String paramString) {

            try {
                XmlPullParser localXmlPullParser;
                URL url =  new URL(paramString);
                url.openConnection().setReadTimeout(10000);
                Log.d(TAG, "getMissionList to URL : " + url);
                InputStream is = url.openStream();

                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(is, "UTF-8");
                int parserEvent  = localXmlPullParser.getEventType();
                String index = "";
                String mNmae="";

                boolean ismisnm = false, ismisidx = false;
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG :
                            if (localXmlPullParser.getName().equals("misidx")) {
                              //  Log.d(TAG,"misidx= "+localXmlPullParser.getName());
                                ismisidx=true;
                            }else if(localXmlPullParser.getName().equals("misnm")){
                               // Log.d(TAG,"misnm= "+localXmlPullParser.getName());
                                 ismisnm=true;
                            }
                            break;
                        case XmlPullParser.END_TAG :
                            break;
                        case XmlPullParser.TEXT:
                                if (ismisidx) {
                                    index = localXmlPullParser.getText();
                                    ismisidx=false;
                                } else if (ismisnm) {
                                     mNmae = localXmlPullParser.getText();
                                    Log.d(TAG,"ismisnm index= "+index);
                                    Log.d(TAG,"ismisnm misnm= "+mNmae);
                                    ismisnm=false;

                                    HashMap<String, Object> object = new HashMap<String, Object>();
                                    object.put("misidx", index); // 조별 idx
                                    object.put("misnm", mNmae); //
                                    btnMission.add(object);
                                }
                            break;
                    }//end Switch
                    parserEvent = localXmlPullParser.next();
                }//end while
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//2130903041
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 항상 켜기
        try {
           // this.baseContainer = (LinearLayout) findViewById(R.id.missionList);//R.id.missionList
            mGridView = (GridView) findViewById(R.id.missionList);
            mGridViewAdapter = new GridViewAdapter(this);

            btnMission = new ArrayList<HashMap<String, Object>>();
            btn_attend = (Button) findViewById(R.id.btn_attend);
            btn_setting = (Button) findViewById(R.id.btn_setting);
            btn_attend.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View paramAnonymousView) {
                      Intent mintent = new Intent(MyAndroidActivity.this,GbsmemberAttendActivity.class);
                      startActivity(mintent);
                  }
          });

            btn_setting.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    MyAndroidActivity.this.intentOption = new Intent(MyAndroidActivity.this, OptionActivity.class);
                    MyAndroidActivity.this.startActivity(MyAndroidActivity.this.intentOption);
                }
            });

//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            }
            if( ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                       ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                       //이미 퍼미션을 가지고 있다면
                       //안드로이드 6.0. 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식
                       TelephonyManager tm = ((TelephonyManager) MyAndroidActivity.this.getSystemService(Context.TELEPHONY_SERVICE));
                       phoneNumber = tm.getLine1Number();
                       pd = ProgressDialog.show(MyAndroidActivity.this, "", "작업 준비중입니다...", true);
                       new AuthAsyncTask().execute(new String[]{this.urlAuth + phoneNumber});

            }else{  //퍼미션 요청을 허용한적이 없다면
                   // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
                   if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                           || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                       ActivityCompat.requestPermissions( MyAndroidActivity.this, REQUIRED_PERMISSIONS, MT_PERMISSION_REQUEST_CODE);
                   }else {
                       //최초 실행
                       ActivityCompat.requestPermissions(MyAndroidActivity.this,REQUIRED_PERMISSIONS, MT_PERMISSION_REQUEST_CODE);
                       //new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE
                   }
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public class GridViewAdapter extends BaseAdapter {
        Context mContext;


        public GridViewAdapter(Context context) {
            mContext = context;
            Log.d(TAG,"GridViewAdapter");
        }

        @Override
        public int getCount() {
            return btnMission.size();
        }

        @Override
        public Object getItem(int position) {
            return btnMission.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.adapter_gridview_item, parent, false);
                holder = new ViewHolder();
                holder.btn_name = (Button) convertView.findViewById(R.id.btn_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.btn_name.setText((String)btnMission.get(position).get("misnm"));
            holder.btn_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyAndroidActivity.this.intent = new Intent(MyAndroidActivity.this, MainMenuActivity.class);
                    intent.putExtra("misidx", (String)btnMission.get(position).get("misidx"));
                    CommonValue.getInstance().setMisidx((String)btnMission.get(position).get("misidx"));
                    CommonValue.getInstance().setMisNm((String)btnMission.get(position).get("misnm"));
                    startActivity(MyAndroidActivity.this.intent);
                }
            });
            return convertView;
        }
        class ViewHolder {
            Button btn_name;
        }
    }
    private class AuthAsyncTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Integer doInBackground(String... paramVarArgs) {
            MyAndroidActivity.this.getAuth(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Integer paramLong) {

            if ((MyAndroidActivity.this.pd != null) && (MyAndroidActivity.this.pd.isShowing())) {
                MyAndroidActivity.this.pd.dismiss();
            }
            Log.d(TAG,"this.Grade= "+Grade);
            Log.d(TAG,"this.Useyn= "+Useyn);
            if (Grade.equals("000") || Grade.isEmpty() || Useyn.equals("N")) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(MyAndroidActivity.this);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        paramAnonymousDialogInterface.dismiss();
                        MyAndroidActivity.this.finish();
                        System.exit(0);
                    }
                });
                builder.setMessage("현재 사용 권한이 없습니다. 관리자에게 권한신청 되었습니다.");
                builder.show();
                return;
            }


              // CommonValue cvapp = (CommonValue) MyAndroidActivity.this.getApplication();
                CommonValue.getInstance().setMisidx(MyAndroidActivity.this.Misidx);
               CommonValue.getInstance().setAuth(MyAndroidActivity.this.Grade);
                CommonValue.getInstance().setMisNm(MyAndroidActivity.this.Misnm);
                CommonValue.getInstance().setMisNm(Useyn);

                if (MyAndroidActivity.this.Grade.equals("999")) {
                    if ((MyAndroidActivity.this.pd == null) || (!MyAndroidActivity.this.pd.isShowing())) {
                        MyAndroidActivity.this.pd = ProgressDialog.show(MyAndroidActivity.this, "", "선교회 목록을 가져오고 있습니다...", true);
                    }
                    new MissionAsyncTask().execute(new String[]{MyAndroidActivity.this.urlMissionList + "?ishq=N"});

                } else if (MyAndroidActivity.this.Grade.equals("998")) {
                    if ((MyAndroidActivity.this.pd == null) || (!MyAndroidActivity.this.pd.isShowing())) {
                        MyAndroidActivity.this.pd = ProgressDialog.show(MyAndroidActivity.this, "", "선교회 목록을 가져오고 잇습니다...", true);
                    }
                    new MyAndroidActivity.MissionAsyncTask().execute(new String[]{MyAndroidActivity.this.urlMissionList + "?ishq=Y"});
                } else {//일반 권한
                    MyAndroidActivity.this.intent = new Intent(MyAndroidActivity.this, MainMenuActivity.class);
                    MyAndroidActivity.this.intent.putExtra("misidx", MyAndroidActivity.this.Misidx);
                    startActivity(MyAndroidActivity.this.intent);
                }

        }
    }

    private class MissionAsyncTask extends AsyncTask<String, Integer, Long> {
        private MissionAsyncTask() {
        }

        protected Long doInBackground(String... paramVarArgs) {
            MyAndroidActivity.this.getMissionList(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            if ((MyAndroidActivity.this.pd != null) && (MyAndroidActivity.this.pd.isShowing())) {
                        MyAndroidActivity.this.pd.dismiss();
            }
            mGridView.setAdapter(mGridViewAdapter);
            mGridViewAdapter.notifyDataSetChanged();
            btn_attend.setVisibility(View.VISIBLE);
            btn_setting.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MT_PERMISSION_REQUEST_CODE:
                if (grantResults.length  == REQUIRED_PERMISSIONS.length ) {  //[0] == PackageManager.PERMISSION_GRANTED)
                   //권한동의
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager tm = ((TelephonyManager) MyAndroidActivity.this.getSystemService(Context.TELEPHONY_SERVICE));
                        phoneNumber = tm.getLine1Number();

                    pd = ProgressDialog.show(MyAndroidActivity.this, "", "작업 준비중입니다...", true);
                    new AuthAsyncTask().execute(new String[]{this.urlAuth + phoneNumber});
                    }
                }else{
                    //   if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)){
                    //궈한 동의안함 선택
                    AlertDialog.Builder builder  = new AlertDialog.Builder(MyAndroidActivity.this);
                    builder.setMessage("권한사용을 동의해주셔야 이용이 가능합니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                            paramAnonymousDialogInterface.dismiss();
                            MyAndroidActivity.this.finish();
                        }
                    });
                    builder.show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}