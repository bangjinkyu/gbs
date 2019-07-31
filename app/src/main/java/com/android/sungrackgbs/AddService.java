package com.android.sungrackgbs;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

//부가정보

public class AddService extends Activity implements RadioGroup.OnCheckedChangeListener {

    private final static String TAG ="AddService";
    ArrayList<StatItem> ageItems;
    TableLayout birthList;
    Button btnMemberList;
    LinearLayout chart1;
    LinearLayout chart2;
    LinearLayout chart3;
    private ArrayList<StatItem> genderItems;
    private ArrayList<StatItem> marriageItems;
    TableLayout memberList;
    String misidx;
    ProgressDialog pd;
    PendingIntent pendingIntent;
    RadioGroup rbStat;
    ArrayList<RowItem> rowBItems;
    ArrayList<TableRow> rowBMember;
    Spinner spnAge;
    Spinner spnGender;
    Spinner spnMarriage;
    Spinner spnMonth;
    Spinner spnRole;
    ArrayList<RowItem> statItems;
    ArrayList<TableRow> statMember;
    ToggleButton swIsAlert;
    TabHost tabHost;
    TextView txtBirthCount;
    TextView txtMemberCount;

    String mSearchMonth;
    String urlBirthList = "http://www.bwm.or.kr/attend/m_birth_month.php?misidx=";
    String urlStatAge = "http://www.bwm.or.kr/attend/m_stat_age.php?misidx=";
    String urlStatGender = "http://www.bwm.or.kr/attend/m_stat_gender.php?misidx=";
    String urlStatMarriage = "http://www.bwm.or.kr/attend/m_stat_marriage.php?misidx=";
    String urlStatList = "http://www.bwm.or.kr/attend/m_stat_list.php?misidx=";


    private void getBirth(String paramString) {

            XmlPullParser localXmlPullParser;
            try {
                URL url = new URL(paramString);
                url.openConnection().setReadTimeout(100000);
                Log.d(TAG,"getBirth= "+url);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int parserEvent = localXmlPullParser.getEventType();


                String mMemidx = "";
                String mMemnm = "";
                String mPhone = "";
                String mRole = "";
                String mBirth = "";
                String mSex = "";
                String mMarriage = "";
                String mLunar ="";
                boolean ismemidx =false ,ismemnm =false,isphone = false, isrole = false , isbirth = false , issex =false ,ismarriage =false ,islunar =false  ;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if(localXmlPullParser.getName().equals("memidx")){
                                ismemidx =true;
                            }else if(localXmlPullParser.getName().equals("memnm")){
                                ismemnm = true;
                            }else if(localXmlPullParser.getName().equals("phone")){
                                isphone = true;
                            }else if(localXmlPullParser.getName().equals("role")){
                                isrole = true;
                            }else if(localXmlPullParser.getName().equals("birth")){
                                isbirth = true;
                            }else if(localXmlPullParser.getName().equals("lunar")){
                                islunar = true;
                            }else if(localXmlPullParser.getName().equals("sex")){
                                issex =  true;
                            }else if(localXmlPullParser.getName().equals("marriage")){
                                ismarriage = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:

                            if(ismemidx){
                                mMemidx = localXmlPullParser.getText().replace("\n    ", "");
                                ismemidx = false;
                            }else if(ismemnm){
                                mMemnm =  localXmlPullParser.getText().replace("\n    ", "");
                                ismemnm = false;
                            }else if(isphone){
                                mPhone = localXmlPullParser.getText().replace("\n    ", "");
                                isphone = false;
                            }else if(isrole){
                                mRole = localXmlPullParser.getText().replace("\n    ", "");
                                isrole = false;
                            }else if(isbirth){
                                mBirth = localXmlPullParser.getText().replace("\n    ", "");
                                isbirth = false;
                            }else if(issex){
                                mSex =  localXmlPullParser.getText().replace("\n    ", "");
                                issex = false;
                            }else if(ismarriage){
                                mMarriage =  localXmlPullParser.getText().replace("\n    ", "");
                                ismarriage = false;
                            }else if(islunar) {
                                mLunar = localXmlPullParser.getText().replace("\n    ", "");
                                islunar = false;
                                Log.d(TAG,"getBirth mMemnm"+mMemnm);
                                Log.d(TAG,"getBirth mRole= "+mRole);
                                Log.d(TAG,"getBirth mBirth= "+mBirth);
                                Log.d(TAG,"getBirth mSex= "+mSex);
                                Log.d(TAG,"getBirth mMarriage= "+mMarriage);
                                RowItem rowItem = new RowItem();
                                rowItem.setMemidx((String) mMemidx);
                                rowItem.setMemnm((String) mMemnm);
                                rowItem.setPhone((String) mPhone);
                                rowItem.setRole((String) mRole);
                                rowItem.setBirth((String) mBirth);
                                rowItem.setSex((String) mSex);
                                rowItem.setMarriage((String) mMarriage);
                                rowItem.setLunar((String) mLunar);
                                this.rowBItems.add(rowItem);
                            }

                            break;
                    }
                    parserEvent = localXmlPullParser.next();
                }



            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }


    }


    private void getAge(String paramString) {

        try {
            XmlPullParser localXmlPullParser;
            URL url = new URL(paramString);
            Log.d(TAG,"getAge= "+url);
            url.openConnection().setReadTimeout(100000);
            localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            localXmlPullParser.setInput(url.openStream(), "UTF-8");
            int parserEvent= localXmlPullParser.getEventType();

            String str1 = "";
            String str2 = "";
            boolean isage =false ,iscnt =false;
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (localXmlPullParser.getName().equals("age")) {
                            isage = true;
                         }else  if (localXmlPullParser.getName().equals("cnt")) {
                            iscnt = true;
;                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        if(isage){
                            str1 = localXmlPullParser.getText().replace("\n    ", "");
                            isage =false;

                        }else if (iscnt){
                            iscnt =false;
                            str2 = localXmlPullParser.getText().replace("\n    ", "");

                            StatItem statItem = new StatItem();
                            statItem.setId(str1);
                            statItem.setCnt(Integer.parseInt((String) str2));
                            this.ageItems.add(statItem);
                        }

                        break;
                }
                parserEvent = localXmlPullParser.next();
            }

        } catch (Exception e) {


            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }


    }

    private void getGender(String paramString) {

            try {
                XmlPullParser localXmlPullParser;
                URL url = new URL(paramString);
                Log.d(TAG,"getGender= " + url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int parserEvent = localXmlPullParser.getEventType();
                String str1 = "";
                String str2 = "";

                boolean issex =false,iscnt =false;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (localXmlPullParser.getName().equals("sex")) {
                                issex = true;
                            } else if (localXmlPullParser.getName().equals("cnt")) {
                                iscnt = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if (issex) {
                                str1 = localXmlPullParser.getText().replace("\n    ", "");
                                issex = false;
                            } else if (iscnt) {
                                str2 = localXmlPullParser.getText().replace("\n    ", "");
                                iscnt = false;

                                StatItem statItem = new StatItem();
                                statItem.setId((String) str1);
                                statItem.setCnt(Integer.parseInt((String) str2));
                                this.genderItems.add(statItem);
                            }

                    }
                    parserEvent = localXmlPullParser.next();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        }


    private void getMarriage(String paramString) {

            try {
                XmlPullParser localXmlPullParser;
                URL url = new URL(paramString);
                Log.d(TAG,"getMarriage= "+url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int parserEvent = localXmlPullParser.getEventType();
                String str1 = "";
                String str2 = "";
                boolean ismarriage = false , iscnt =false;
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (localXmlPullParser.getName().equals("marriage")) {
                                ismarriage = true;
                            } else if (localXmlPullParser.getName().equals("cnt")) {
                                iscnt = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if(ismarriage){
                                str1 = localXmlPullParser.getText().replace("\n    ", "");
                                ismarriage = false;
                            }else if(iscnt){
                                str2 = localXmlPullParser.getText().replace("\n    ", "");
                                iscnt = false;
                                StatItem statItem = new StatItem();
                                statItem.setId((String) str1);
                                statItem.setCnt(Integer.parseInt((String) str2));
                                this.marriageItems.add(statItem);
                            }

                            break;
                    }

                    parserEvent = localXmlPullParser.next();
                }

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
    }
//통계목록
    private void getStatMember(String paramString) {


            try {
                XmlPullParser localXmlPullParser;
                URL url  = new URL(paramString);
                Log.d(TAG,"getStatMember= "+url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int parserEvent = localXmlPullParser.getEventType();

                String mMemisd = "";
                String mMemnm =  "";
                String mRole = "";
                String mBirth = "";
                String mSex = "";
                String mMarriage  ="";

                boolean ismemidx = false, ismemnm =false ,isrole =false ,isbirth = false, issex =false ,ismarriage =false;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (localXmlPullParser.getName().equals("memidx")) {
                                ismemidx = true;
                            } else if (localXmlPullParser.getName().equals("memnm")) {
                                ismemnm = true;
                            }else if(localXmlPullParser.getName().equals("role")){
                                isrole = true;
                            }else if (localXmlPullParser.getName().equals("birth")){
                                isbirth = true;
                            }else if(localXmlPullParser.getName().equals("sex")){
                                issex = true;
                            }else if(localXmlPullParser.getName().equals("marriage")){
                                ismarriage =true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if(ismemidx) {
                                mMemisd = localXmlPullParser.getText().replace("\n    ", "");
                                ismemidx =false;
                            }else if(ismemnm){
                                mMemnm = localXmlPullParser.getText().replace("\n    ", "");
                                ismemnm = false;
                            }else if(isrole){
                                mRole = localXmlPullParser.getText().replace("\n    ", "");
                                isrole =false;
                            }else if(isbirth){
                                mBirth = localXmlPullParser.getText().replace("\n    ", "");
                                isbirth =false;
                            }else if(issex){
                                mSex =  localXmlPullParser.getText().replace("\n    ", "");
                                issex = false;
                            }else if(ismarriage){
                                mMarriage =  localXmlPullParser.getText().replace("\n    ", "");
                                ismarriage = false;

                                Log.d(TAG,"mBirth= "+mBirth);
                                RowItem rowItem = new RowItem();
                                rowItem.setMemidx((String) mMemisd);
                                rowItem.setMemnm((String) mMemnm);
                                rowItem.setRole((String) mRole);
                                rowItem.setBirth((String) mBirth);
                                rowItem.setSex((String) mSex);
                                rowItem.setMarriage((String) mMarriage);
                                this.statItems.add(rowItem);
                            }

                            break;
                    }
                    parserEvent = localXmlPullParser.next();
                }

            } catch (Exception e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

    }

    public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt) {
        switch (paramInt) {

            case R.id.rbGender:
                this.chart1.setVisibility(View.VISIBLE);
                this.chart2.setVisibility(View.INVISIBLE);
                this.chart3.setVisibility(View.INVISIBLE);
                return;
            case R.id.rbMarriage:
                this.chart1.setVisibility(View.INVISIBLE);
                this.chart2.setVisibility(View.VISIBLE);
                this.chart3.setVisibility(View.INVISIBLE);
                return;
            case R.id.rbAge:
                this.chart1.setVisibility(View.INVISIBLE);
                this.chart2.setVisibility(View.INVISIBLE);
                this.chart3.setVisibility(View.VISIBLE);
                break;
            default:

                return;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.addservice);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 항상 켜기
        this.misidx = getIntent().getExtras().get("misidx").toString();
        this.tabHost = ((TabHost) findViewById(R.id.tabhost));
        this.tabHost.setup();
        TabHost.TabSpec spec1  = this.tabHost.newTabSpec("tag1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("생일자", getResources().getDrawable(R.drawable.icon_br));
        this.tabHost.addTab(spec1);
        TabHost.TabSpec spec2 = this.tabHost.newTabSpec("tag2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("통계(차트)", getResources().getDrawable(R.drawable.icon_lt));
        this.tabHost.addTab(spec2);
        TabHost.TabSpec spec3 = this.tabHost.newTabSpec("tag3");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("통계(목록)", getResources().getDrawable(R.drawable.icon_pi));
        this.tabHost.addTab(spec3);
        this.tabHost.setCurrentTab(0);
        this.birthList = ((TableLayout) findViewById(R.id.birthList));
        this.spnMonth = ((Spinner) findViewById(R.id.spnMonth));
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spnMonth.setAdapter(adapter);
        this.txtBirthCount = ((TextView) findViewById(R.id.txtBirthCount));
        this.swIsAlert = ((ToggleButton) findViewById(R.id.swIsAlert));    //생일 알람


        swIsAlert.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    PreferenceHelper helper = new PreferenceHelper(AddService.this);
                    String localObject = helper.getCode("BWM_BIRTH_ALARM");
                    Log.d(TAG,"BWM_BIRTH_ALARM= "+localObject);

                    if ((localObject == null) || (localObject.equals(""))) {
                       swIsAlert.setChecked(true);
                        Intent intent = new Intent(AddService.this, BirthAlarmReceiver.class);
                        intent.putExtra("misidx", AddService.this.misidx);
                        pendingIntent = PendingIntent.getBroadcast(AddService.this, 1,intent, 0);
                        Calendar calendar = Calendar.getInstance();
                        Log.d(TAG,"calender"+(calendar).getTimeInMillis());
                        ((AlarmManager) AddService.this.getSystemService(ALARM_SERVICE)).setRepeating(AlarmManager.RTC_WAKEUP, (calendar).getTimeInMillis(), 28800000L, AddService.this.pendingIntent);
                        helper.setCode("BWM_BIRTH_ALARM", "RUN");
                        helper.setCode("MISIDX", AddService.this.misidx);
                        Toast.makeText(AddService.this, "생일자 알림이 등록되었습니다", Toast.LENGTH_SHORT).show();

                    }else {
                        swIsAlert.setChecked(false);
                        helper.removeCode("BWM_BIRTH_ALARM");
                        helper.removeCode("MISIDX");
                        Toast.makeText(AddService.this, "생일자 알림이 해제되었습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        int i = Calendar.getInstance().get(Calendar.MONTH);
        String mMon ="";
        if (i + 1 < 10) {
            mMon = "0" + (i + 1);
        }else{
            mMon = String.valueOf(i + 1);
        }

        this.spnMonth.setSelection(Integer.parseInt(mMon) - 1);
        this.spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                AddService.this.pd = ProgressDialog.show(AddService.this, "", "생일자 목록을 가져오고 있습니다....", true);
                mSearchMonth =String.valueOf(paramAnonymousInt + 1);
                if (paramAnonymousInt + 1 < 10) {
                    mSearchMonth = "0" + (paramAnonymousInt + 1);
                }

                //생일자
                new BirthMonthAsyncTask(AddService.this).execute(new String[] { urlBirthList + misidx + "&month=" +  mSearchMonth});


            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
            this.rbStat = ((RadioGroup) findViewById(R.id.rbStat));
            this.rbStat.setOnCheckedChangeListener(this);
            this.chart1 = ((LinearLayout) findViewById(R.id.chart1));
            this.chart2 = ((LinearLayout) findViewById(R.id.chart2));
            this.chart3 = ((LinearLayout) findViewById(R.id.chart3));
            this.chart1.setVisibility(View.VISIBLE);
            this.chart2.setVisibility(View.INVISIBLE);
            this.chart3.setVisibility(View.INVISIBLE);
            this.urlStatGender += this.misidx;
            this.urlStatMarriage += this.misidx;
            this.urlStatAge += this.misidx;
            //통계 차트
            new StatGenderAsyncTask(this).execute(new String[]{this.urlStatGender});
            new StatMarriageAsyncTask(this).execute(new String[]{AddService.this.urlStatMarriage});
            new StatAgeAsyncTask( this).execute(new String[]{AddService.this.urlStatAge});

            this.spnGender = ((Spinner) findViewById(R.id.spnGender));
            this.spnMarriage = ((Spinner) findViewById(R.id.spnMarriage));
            this.spnAge = ((Spinner) findViewById(R.id.spnAge));
            this.spnRole = ((Spinner) findViewById(R.id.spnRole));
            this.btnMemberList = ((Button) findViewById(R.id.btnMemberList));  //통계목록 조회
            this.memberList = ((TableLayout) findViewById(R.id.memberList));
            this.txtMemberCount = ((TextView) findViewById(R.id.txtMemberCount));


            ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spnGender.setAdapter(adapter1);

            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.marriages,  android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spnMarriage.setAdapter(adapter2);

            ArrayAdapter adapter3  = ArrayAdapter.createFromResource(this, R.array.ages, android.R.layout.simple_spinner_item);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spnAge.setAdapter(adapter3);

            ArrayAdapter adapter4 = ArrayAdapter.createFromResource(this, R.array.roleK,  android.R.layout.simple_spinner_item);
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spnRole.setAdapter(adapter4);

            this.btnMemberList.setTextColor(-1);
            this.btnMemberList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    AddService.this.pd = ProgressDialog.show(AddService.this, "", "목록을 가져오고 있습니다....", true);
                    String str1 = "";
                    String str2 = "";
                    String str3 = "";
                    String str4 = "";
                    switch (AddService.this.spnGender.getSelectedItemPosition()) {
                        case 1:
                        str1 = "F";
                        break;
                        case 2:
                        str1 = "M";
                        break;
                        default:
                        break;
                    }

                    switch (AddService.this.spnMarriage.getSelectedItemPosition()) {
                        case 1:
                            str2 ="N";
                            break;
                        case 2:
                            str2 ="Y";
                            break;
                        default:
                            break;
                    }



                    switch (AddService.this.spnAge.getSelectedItemPosition()) {
                        case 1:
                            str3 = "20";
                            break;
                        case 2:
                            str3 = "30";
                            break;
                        case 3:
                            str3 = "40";
                            break;
                        default:
                            break;

                    }

                    switch (AddService.this.spnRole.getSelectedItemPosition()) {

                        case 1:
                            str4 = "S";
                            break;
                        case 2:
                            str4 = "J";
                            break;

                    }
                   //통계 목록
                   String murl = AddService.this.urlStatList + AddService.this.misidx + "&gender=" + str1 + "&marriage=" + str2 + "&age=" + str3 + "&role=" + str4 ;
                    new AddService.StatMemberAsyncTask( AddService.this).execute(new String[]{murl});

                }
            });


        }


//생일자 탭
    private class BirthMonthAsyncTask extends AsyncTask<String, Integer, Long>
    {
        Context ctx;

        public BirthMonthAsyncTask(Context paramContext)
        {
            this.ctx = paramContext;
        }

        protected Long doInBackground(String... paramVarArgs)
        {

            AddService.this.rowBMember = null;
            AddService.this.rowBMember = new ArrayList();

            AddService.this.rowBItems = null;
            AddService.this.rowBItems = new ArrayList();
            AddService.this.getBirth(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong)
        {
            if (birthList.getChildCount() > 0) {
                AddService.this.birthList.removeAllViewsInLayout();
            }
            Collections.sort(AddService.this.rowBItems);

            if (0 < rowBItems.size()) {
                rowBMember.clear();
            }

            AddService.this.txtBirthCount.setText(AddService.this.rowBItems.size() + "명");
            if ((AddService.this.pd != null) && (AddService.this.pd.isShowing())) {
                AddService.this.pd.dismiss();
            }
            for ( int i = 0; i< rowBItems.size(); i++ ) {
                RowItem localRowItem = (RowItem) AddService.this.rowBItems.get(i);

                TextView localTextView1 = new TextView(this.ctx);
                localTextView1.setText(localRowItem.getMemnm() + localRowItem.getAge());
                localTextView1.setWidth(180);
                localTextView1.setGravity(3);
                localTextView1.setTextColor(-1);

                TextView localTextView = new TextView(this.ctx);
                localTextView.setText(localRowItem.getPhone());
                localTextView.setWidth(210);
                localTextView.setGravity(3);
                localTextView.setTextColor(-1);

                TextView localTextView2 = new TextView(this.ctx); //생년월일
                localTextView2.setText(localRowItem.getBirth());
                localTextView2.setWidth(200);
                localTextView2.setGravity(3);
                localTextView2.setTextColor(getResources().getColor(R.color.blak));


                TextView localTextView3 = new TextView(this.ctx);
                localTextView3.setText(localRowItem.getSex());  //남/여
                localTextView3.setWidth(70);
                localTextView3.setGravity(3);
                localTextView3.setTextColor(getResources().getColor(R.color.blak));


                TextView localTextView4 = new TextView(this.ctx);   //결혼여부
                localTextView4.setText(localRowItem.getMarriage());
                localTextView4.setWidth(70);
                localTextView4.setGravity(3);
                localTextView4.setTextColor(getResources().getColor(R.color.blak));


                Button localButton1 = new Button(this.ctx);
                localButton1.setText("Call");
                localButton1.setTextSize(13.0F);
                localButton1.setTextColor(getResources().getColor(R.color.call));
                localButton1.setBackgroundResource(R.drawable.smallbuttons);
                localButton1.setGravity(17);
                localButton1.setWidth(90);

                Button localButton2 = new Button(this.ctx);
                localButton2.setText("SMS");
                localButton2.setTextSize(13.0F);
                localButton2.setTextColor(getResources().getColor(R.color.sms));
                localButton2.setBackgroundResource(R.drawable.smallbuttons);
                localButton2.setGravity(17);
                localButton2.setWidth(90);

                if (localRowItem.getRole().equals("A")) {
                    localTextView1.setTextColor(getResources().getColor(R.color.mis));
                } else if (localRowItem.getRole().equals("B")) {
                    localTextView1.setTextColor(getResources().getColor(R.color.mis1));
                } else if (localRowItem.getRole().equals("C")) {
                    localTextView1.setTextColor(getResources().getColor(R.color.cell0));
                } else if (localRowItem.getRole().equals("D")) {
                    localTextView1.setTextColor(getResources().getColor(R.color.cell1));
                } else if (localRowItem.getRole().equals("E")) {
                    localTextView1.setTextColor(Color.WHITE);
                } else if (localRowItem.getRole().equals("F")) {
                    localTextView1.setTextColor(getResources().getColor(R.color.newbie));
                }else if(localRowItem.getRole().equals("G")){
                    localTextView1.setTextColor(getResources().getColor(R.color.red));
                }

//                Calendar calendar = Calendar.getInstance();
//                int j = calendar.get(Calendar.MONTH);
//                int k = calendar.get(Calendar.DATE);
//
//                String str1 = "";
//                String str2 = "";
//
//                if (j + 1 < 10) {
//                    str1 = "0" + (j + 1);
//                } else {
//                    str1 = String.valueOf(j + 1);
//                }
//
//                if (k < 10) {
//                    str2 = "0" + k;
//                } else {
//                    str2 = String.valueOf(k);
//                }


                localButton1.setTag(localRowItem.getPhone());
                localButton1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        String mphone = ((Button) paramAnonymousView).getTag().toString();
                        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mphone));
                        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                        AddService.this.startActivity(intent);
                    }
                });
                localButton2.setTag(localRowItem.getPhone());
                localButton2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        String mphone = ((Button) paramAnonymousView).getTag().toString();
                        Intent localIntent = new Intent("android.intent.action.SENDTO");
                        localIntent.setData(Uri.parse("sms:" + mphone));
                        AddService.this.startActivity(localIntent);
                    }
                });
  //              Log.d(TAG, "localRowItem.getBirthMonth()" + localRowItem.getBirthMonth());
//                if (mSearchMonth.equals(localRowItem.getBirthMonth()))
//                {
                    TableRow localTableRow = new TableRow(this.ctx);
                    localTableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    localTableRow.setPadding(0, 5, 5, 5);
                    localTableRow.addView(localTextView1);
                    localTableRow.addView(localTextView2);
                    localTableRow.addView(localTextView3);
                    localTableRow.addView(localTextView4);

                    localTableRow.addView(localButton1);
                    localTableRow.addView(localButton2);

                    AddService.this.rowBMember.add(localTableRow);
                    AddService.this.birthList.addView((View) localTableRow);
               //  }
                }//for end

            }
        }


    class StatItem {
        int cnt = 0;
        String id = "";

        StatItem() {
        }

        public int getCnt() {
            return this.cnt;
        }

        public String getId() {
            return this.id;
        }

        public void setCnt(int paramInt) {
            this.cnt = paramInt;
        }

        public void setId(String paramString) {
            this.id = paramString;
        }
    }

    class RowItem implements Comparable<RowItem> {
        String birth = "";
        String lunar = "";
        String marriage = "";
        String memidx = "";
        String memnm = "";
        String phone = "";
        String role = "";
        String sex = "";

        RowItem() {
        }

        public int compareTo(RowItem paramRowItem) {
            return Integer.valueOf(Integer.parseInt(this.birth.substring(4, 8))).compareTo(Integer.valueOf(Integer.parseInt(paramRowItem.birth.substring(4, 8))));
        }

        public String getAge() {
            int j = Calendar.getInstance().get(Calendar.YEAR);
            int i = 0;
            if (!this.birth.equals("")) {
                i = j - Integer.parseInt(this.birth.substring(0, 4)) + 1;
            }
            if (i > 0) {
                return "(" + i + ")";
            }
            return "없음";
        }

        public String getBirth() {
            String str2 = this.birth.substring(0, 4) + "/" + this.birth.substring(4, 6) + "/" + this.birth.substring(6, 8);
            String str1 = "";
            if (this.lunar.equals("-")) {
                str1 = str2 + "(-)";
            }else{
                str1 = str2 + "(+)";
            }
            return str1;
        }

        public String getBirthDate() {
            return this.birth.substring(4, 6) + this.birth.substring(6, 8);
        }

        public String getBirthMonth() {
            return this.birth.substring(4, 6);
        }

        public String getLunar() {
            return this.lunar;
        }

        public String getMarriage() {

                if (this.marriage.equals("Y")) {
                    return "기혼";
                }else if(this.marriage.equals("N")){
                    return "미혼";
                }else{
                    return "없음";
                }
        }

        public String getMemidx() {
            return this.memidx;
        }

        public String getMemnm() {
            return this.memnm;
        }

        public String getPhone() {
            return this.phone;
        }

        public String getRole() {
            return this.role;
        }

        public String getSex() {
            if (this.sex.equals("M")) {
                return "형제";
            }else if(this.sex.equals("F")) {
                return "자매";
            }else{
                return "없음";
            }
        }

        public void setBirth(String paramString) {
            this.birth = paramString;
        }

        public void setLunar(String paramString) {
            this.lunar = paramString;
        }

        public void setMarriage(String paramString) {
            this.marriage = paramString;
        }

        public void setMemidx(String paramString) {
            this.memidx = paramString;
        }

        public void setMemnm(String paramString) {
            this.memnm = paramString;
        }

        public void setPhone(String paramString) {
            this.phone = paramString;
        }

        public void setRole(String paramString) {
            this.role = paramString;
        }

        public void setSex(String paramString) {
            this.sex = paramString;
        }
    }

    private class StatAgeAsyncTask extends AsyncTask<String, Integer, Long> {
        Context ctx;

        public StatAgeAsyncTask(Context paramContext) {
            this.ctx = paramContext;
        }

        private DefaultRenderer buildCategoryRenderer(int[] paramArrayOfInt) {
            DefaultRenderer localDefaultRenderer = new DefaultRenderer();
            int j = paramArrayOfInt.length;
            int i = 0;
            for (; ; ) {
                if (i >= j) {
                    return localDefaultRenderer;
                }
                int k = paramArrayOfInt[i];
                SimpleSeriesRenderer localSimpleSeriesRenderer = new SimpleSeriesRenderer();
                localSimpleSeriesRenderer.setColor(k);
                localDefaultRenderer.addSeriesRenderer(localSimpleSeriesRenderer);
                i += 1;
            }
        }

        protected Long doInBackground(String... paramVarArgs) {
            AddService.this.ageItems = new ArrayList();
            AddService.this.getAge(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {

            if (AddService.this.ageItems.size() <= 0) {
                return;
            }

            DefaultRenderer localDefaultRenderer = buildCategoryRenderer(new int[]{getResources().getColor(R.color.age20), getResources().getColor(R.color.age30), getResources().getColor(R.color.age40)});
            localDefaultRenderer.setLegendTextSize(30.0F);
            localDefaultRenderer.setLabelsTextSize(30.0F);
            localDefaultRenderer.setLabelsColor(Color.CYAN);
            localDefaultRenderer.setZoomEnabled(false);
            localDefaultRenderer.setClickEnabled(true);
            localDefaultRenderer.setScale(0.8F);
            double d1 = 0.0D;
            double d2 = 0.0D;
            String str ="";

            CategorySeries localCategorySeries  = new CategorySeries("연령대비율");
            for (int i =0;i < AddService.this.ageItems.size();i++) {
                str = ((AddService.StatItem) AddService.this.ageItems.get(i)).getId() + "대";
                d1 = ageItems.get(0).getCnt()+ageItems.get(1).getCnt()+ageItems.get(2).getCnt();
                d2 = Math.round(((AddService.StatItem) AddService.this.ageItems.get(i)).getCnt() / d1 * 100.0D);
                localCategorySeries.add(new StringBuilder(String.valueOf(str)).append(":").append(((AddService.StatItem) AddService.this.ageItems.get(i)).getCnt()).append("명").toString() + "(" + d2 + "%)", ((AddService.StatItem) AddService.this.ageItems.get(i)).getCnt());
            }

            GraphicalView mChartView = ChartFactory.getPieChartView(this.ctx, localCategorySeries, localDefaultRenderer);
            AddService.this.chart3.addView(mChartView);
        }
    }

    private class StatGenderAsyncTask extends AsyncTask<String, Integer, Long> {
        Context ctx;

        public StatGenderAsyncTask(Context paramContext) {
            this.ctx = paramContext;
        }

        private DefaultRenderer buildCategoryRenderer(int[] paramArrayOfInt) {
            DefaultRenderer localDefaultRenderer = new DefaultRenderer();
            int j = paramArrayOfInt.length;
            int i = 0;
            for (; ; ) {
                if (i >= j) {
                    return localDefaultRenderer;
                }
                int k = paramArrayOfInt[i];
                SimpleSeriesRenderer localSimpleSeriesRenderer = new SimpleSeriesRenderer();
                localSimpleSeriesRenderer.setColor(k);
                localDefaultRenderer.addSeriesRenderer(localSimpleSeriesRenderer);
                i += 1;
            }
        }

        protected Long doInBackground(String... paramVarArgs) {
            AddService.this.genderItems = new ArrayList();
            AddService.this.getGender(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            if (AddService.this.genderItems.size() <= 0) {
                return;
            }
            try {
            DefaultRenderer localDefaultRenderer = buildCategoryRenderer(new int[]{getResources().getColor(R.color.gender_m), getResources().getColor(R.color.gender_f)});
            localDefaultRenderer.setLegendTextSize(30.0F);
            localDefaultRenderer.setLabelsTextSize(30.0F);
            localDefaultRenderer.setLabelsColor(Color.CYAN);
            localDefaultRenderer.setZoomEnabled(false);
            localDefaultRenderer.setClickEnabled(true);
            localDefaultRenderer.setScale(0.8F);
            double d1 = 0.0D;
            double d2 ;
            String mgender ="";

                CategorySeries  localCategorySeries = new CategorySeries("남녀비율");
            for (int i = 0;i < AddService.this.genderItems.size();i++ ) {

                if (genderItems.get(i).getId().equals("M")) {
                    mgender = "형제";
                }else {
                    mgender = "자매";

                }
                d1 = genderItems.get(0).getCnt()+genderItems.get(1).getCnt() ;
                d2 = Math.round(genderItems.get(i).getCnt() / d1 * 100.0D);
                localCategorySeries.add(new StringBuilder(String.valueOf(mgender)).append(":").append(genderItems.get(i).getCnt()).append("명").toString() + "(" + d2 + "%)", genderItems.get(i).getCnt());

            }

            GraphicalView mChartView = ChartFactory.getPieChartView(this.ctx, localCategorySeries, localDefaultRenderer);
            AddService.this.chart1.addView(mChartView);

            } catch (Exception e) {

            }

        }
    }

    private class StatMarriageAsyncTask extends AsyncTask<String, Integer, Long> {
        Context ctx;

        public StatMarriageAsyncTask(Context paramContext) {
            this.ctx = paramContext;
        }

        private DefaultRenderer buildCategoryRenderer(int[] paramArrayOfInt) {
            DefaultRenderer localDefaultRenderer = new DefaultRenderer();
            int j = paramArrayOfInt.length;
            int i = 0;
            for (; ; ) {
                if (i >= j) {
                    return localDefaultRenderer;
                }
                int k = paramArrayOfInt[i];
                SimpleSeriesRenderer localSimpleSeriesRenderer = new SimpleSeriesRenderer();
                localSimpleSeriesRenderer.setColor(k);
                localSimpleSeriesRenderer.setChartValuesTextSize(18.0F);
                localDefaultRenderer.addSeriesRenderer(localSimpleSeriesRenderer);
                i += 1;
            }
        }

        protected Long doInBackground(String... paramVarArgs) {
            marriageItems = new ArrayList();
            AddService.this.getMarriage(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            if (AddService.this.marriageItems.size() <= 0) {
                return;
            }
            DefaultRenderer localDefaultRenderer = buildCategoryRenderer(new int[]{-11709051, -1852844});
            localDefaultRenderer.setLegendTextSize(30.0F);
            localDefaultRenderer.setLabelsTextSize(30.0F);
            localDefaultRenderer.setLabelsColor(Color.CYAN);
            localDefaultRenderer.setZoomEnabled(false);
            localDefaultRenderer.setClickEnabled(true);
            localDefaultRenderer.setScale(0.8F);

            double d1 = 0.0D;
            double d2 = 0.0D;
             String str;


                try {
                    CategorySeries  localCategorySeries = new CategorySeries("혼인비율");

                    for (  int i = 0; i < AddService.this.marriageItems.size();i++) {
                        if (((AddService.StatItem) AddService.this.marriageItems.get(i)).getId().equals("Y")) {
                            str = "기혼";
                            d1 = marriageItems.get(0).getCnt() + marriageItems.get(1).getCnt();
                        }else {
                            d1 = marriageItems.get(0).getCnt() + marriageItems.get(1).getCnt();
                            str = "미혼";
                        }

                        d2 = Math.round(((AddService.StatItem) AddService.this.marriageItems.get(i)).getCnt() / d1 * 100.0D);
                        localCategorySeries.add(new StringBuilder(String.valueOf(str)).append(":").append(((AddService.StatItem) AddService.this.marriageItems.get(i)).getCnt()).append("명").toString() + "(" + d2 + "%)", ((AddService.StatItem) AddService.this.marriageItems.get(i)).getCnt());
                    }

                    GraphicalView mChartView = ChartFactory.getPieChartView(this.ctx, localCategorySeries, localDefaultRenderer);
                    AddService.this.chart2.addView(mChartView);


                } catch (Exception e) {

                }
        }
    }
//통계 목록
    private class StatMemberAsyncTask extends AsyncTask<String, Integer, Long> {
        Context ctx;

        public StatMemberAsyncTask(Context paramContext) {
            this.ctx = paramContext;
        }

        protected Long doInBackground(String... paramVarArgs) {

            AddService.this.statMember = null;
            AddService.this.statMember = new ArrayList();
            AddService.this.statItems = null;
            AddService.this.statItems = new ArrayList();
            AddService.this.getStatMember(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {

            if (AddService.this.memberList.getChildCount() > 0) {
                AddService.this.memberList.removeAllViewsInLayout();
            }

            if (0 < AddService.this.statItems.size()) {
                 AddService.this.statMember.clear();
            }
            AddService.this.txtMemberCount.setText(AddService.this.statItems.size() + "명");
            if ((AddService.this.pd != null) && (AddService.this.pd.isShowing())) {
                AddService.this.pd.dismiss();
            }
            for ( int i = 0; i< AddService.this.statItems.size()  ; i++){
                TextView textView = new TextView(this.ctx);
                TextView localObject1 = new TextView(this.ctx);
                TextView localTextView1 = new TextView(this.ctx);
                TextView localTextView2 = new TextView(this.ctx);

                RowItem localObject2 = (AddService.RowItem) AddService.this.statItems.get(i);
                textView.setText(((AddService.RowItem) localObject2).getMemnm() + ((AddService.RowItem) localObject2).getAge());
                textView.setWidth(180);
                textView.setGravity(3);

               localObject1.setText(((AddService.RowItem) localObject2).getBirth());
               localObject1.setWidth(200);
                localObject1.setGravity(3);
                localObject1.setTextColor(getResources().getColor(R.color.blak));

                localTextView1.setText(((AddService.RowItem) localObject2).getSex());
                localTextView1.setWidth(70);
                localTextView1.setGravity(3);
                localTextView1.setTextColor(getResources().getColor(R.color.blak));

                localTextView2.setText(((AddService.RowItem) localObject2).getMarriage());
                localTextView2.setWidth(70);
                localTextView2.setGravity(3);
                localTextView2.setTextColor(getResources().getColor(R.color.blak));

                if (((AddService.RowItem) localObject2).getRole().equals("A")) {
                    textView.setTextColor(getResources().getColor(R.color.mis));
                }else if(((AddService.RowItem) localObject2).getRole().equals("B")) {
                    textView.setTextColor(getResources().getColor(R.color.mis1));
                } if (((AddService.RowItem) localObject2).getRole().equals("C")) {
                    textView.setTextColor(getResources().getColor(R.color.cell0));
                } else if (((AddService.RowItem) localObject2).getRole().equals("D")) {
                    textView.setTextColor(getResources().getColor(R.color.cell1));
                } else if (((AddService.RowItem) localObject2).getRole().equals("E")) {
                    textView.setTextColor(Color.WHITE);
                } else if (((AddService.RowItem) localObject2).getRole().equals("F")) {
                    textView.setTextColor(getResources().getColor(R.color.newbie));
                } else if(((AddService.RowItem) localObject2).getRole().equals("G")){
                    textView.setTextColor(getResources().getColor(R.color.red));
                }

                TableRow tableRow = new TableRow(this.ctx);
                ((TableRow) tableRow).setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ((TableRow) tableRow).setPadding(0, 5, 5, 5);
                ((TableRow) tableRow).addView(textView);
                ((TableRow) tableRow).addView((View) localObject1);
                ((TableRow) tableRow).addView(localTextView1);
                ((TableRow) tableRow).addView(localTextView2);
                AddService.this.statMember.add(tableRow);
                AddService.this.memberList.addView((View) tableRow);

            }

        }
    }
}

