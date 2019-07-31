package com.android.sungrackgbs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


//출석률
public class MemberAttendActivity extends Activity {

    private static final String TAG = "MemberAttendActivity";
    static final int DATE_DIALOG_FROM = 0;
    static final int DATE_DIALOG_MIS_FROM = 4;
    static final int DATE_DIALOG_MIS_TO = 5;
    static final int DATE_DIALOG_SUNDAY = 2;
    static final int DATE_DIALOG_TO = 1;
    Button btnList;
    Button btnMisList;
    Button btnSort;
    LinearLayout conTab1;
    LinearLayout conTab2;
    Boolean isFirst = true;
    private int mDay_F;
    private int mDay_Mis_F;
    private int mDay_Mis_T;
    private int mDay_S;
    private int mDay_T;
    private DatePickerDialog.OnDateSetListener mFromSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            MemberAttendActivity.this.mYear_F = paramAnonymousInt1;
            MemberAttendActivity.this.mMonth_F = paramAnonymousInt2;
            MemberAttendActivity.this.mDay_F = paramAnonymousInt3;
            MemberAttendActivity.this.updateDisplay(0);
            GetPrivateData();
        }
    };
    private DatePickerDialog.OnDateSetListener mToSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            MemberAttendActivity.this.mYear_T = paramAnonymousInt1;
            MemberAttendActivity.this.mMonth_T = paramAnonymousInt2;
            MemberAttendActivity.this.mDay_T = paramAnonymousInt3;
            MemberAttendActivity.this.updateDisplay(1);
            GetPrivateData();
        }
    };
    private DatePickerDialog.OnDateSetListener mSundaySetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            MemberAttendActivity.this.mYear_S = paramAnonymousInt1;
            MemberAttendActivity.this.mMonth_S = paramAnonymousInt2;
            MemberAttendActivity.this.mDay_S = paramAnonymousInt3;
            MemberAttendActivity.this.updateDisplay(2);
            MemberAttendActivity.this.GetSundayData();
        }
    };
    private DatePickerDialog.OnDateSetListener mMisFromSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            MemberAttendActivity.this.mYear_Mis_F = paramAnonymousInt1;
            MemberAttendActivity.this.mMonth_Mis_F = paramAnonymousInt2;
            MemberAttendActivity.this.mDay_Mis_F = paramAnonymousInt3;
            MemberAttendActivity.this.updateDisplay(4);
            GetMissionData();
        }
    };
    private DatePickerDialog.OnDateSetListener mMisToSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            MemberAttendActivity.this.mYear_Mis_T = paramAnonymousInt1;
            MemberAttendActivity.this.mMonth_Mis_T = paramAnonymousInt2;
            MemberAttendActivity.this.mDay_Mis_T = paramAnonymousInt3;
            MemberAttendActivity.this.updateDisplay(5);
            GetMissionData();
        }
    };
    private int mMonth_F;
    private int mMonth_Mis_F;
    private int mMonth_Mis_T;
    private int mMonth_S;
    private int mMonth_T;



    private int mYear_F;
    private int mYear_Mis_F;
    private int mYear_Mis_T;
    private int mYear_S;
    private int mYear_T;
    TableLayout memberList;
    String misidx;

    int ord = 0;
    ProgressDialog pd;
    ArrayList<RowItem> rowItems;
    ArrayList<TableRow> rowMember;
    ArrayList<Integer> sValues = new ArrayList();
    ArrayList<String> sXValues = new ArrayList();
    ArrayList<Integer> mValues = new ArrayList();
    ArrayList<String> mXValues = new ArrayList();
    ArrayList<int[]> sundayValue = new ArrayList();
    ArrayList<int[]> missionValue = new ArrayList();
    LinearLayout tab1;
    LinearLayout tab2;
    LinearLayout tab3;
    TabHost tabHost;
    TextView txtFrom;
    TextView txtMisFrom;
    TextView txtMisTo;
    TextView txtSunday;
    TextView txtTo;
    String urlGetMember_n = "http://www.bwm.or.kr/attend/m_attend_member_n2.php?misidx=";  //이름순
    String urlGetMember_r = "http://www.bwm.or.kr/attend/m_attend_member_r2.php?misidx="; //출석순
    String urlGetMission = "http://www.bwm.or.kr/attend/m_attend_mission2.php?misidx=";
    String urlGetSunday = "http://www.bwm.or.kr/attend/m_attend_holyday.php?day=";

    private void GetMember(String paramString) {

        try {
            XmlPullParser localXmlPullParser;
            URL url = new URL(paramString);
            Log.d(TAG,"GetMember= "+url);
            url.openConnection().setReadTimeout(100000);
            localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            localXmlPullParser.setInput(url.openStream(), "UTF-8");
            int eventType = localXmlPullParser.getEventType();

            String str ="";
            String mMemidx = "";
            String mMemnm = "";
            String mBirth = "";
            String mSex = "";
            String mRole = "";
            String mMarriage = "";
            String mHolcnt = "";
            String mAttcnt = "";

            boolean ismemidx =false ,ismemnm  =false, isbirth = false, issex = false, isrole= false , ismarriage = false , isholcnt =false , isattcnt = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                       if(localXmlPullParser.getName().equals("memidx")){
                           ismemidx = true;
                       }else if(localXmlPullParser.getName().equals("memnm")){
                           ismemnm = true;
                       }else if(localXmlPullParser.getName().equals("birth")){
                           isbirth = true;
                       }else if(localXmlPullParser.getName().equals("sex")){
                           issex = true;
                       }else if(localXmlPullParser.getName().equals("role")) {
                           isrole = true;
                       }else if(localXmlPullParser.getName().equals("marriage")){
                           ismarriage = true;
                       }else if(localXmlPullParser.getName().equals("holcnt")){
                           isholcnt = true;
                       }else if(localXmlPullParser.getName().equals("attcnt")){
                           isattcnt = true;
                       }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        if(ismemidx) {
                            mMemidx = localXmlPullParser.getText();
                            ismemidx = false;
                        }else if(ismemnm){
                            mMemnm = localXmlPullParser.getText();
                            ismemnm =false;
                        }else if(isbirth){
                            mBirth = localXmlPullParser.getText().replace("\n    ", "");
                            isbirth = false;
                        }else if(issex) {
                            mSex = localXmlPullParser.getText();
                            issex = false;
                        }else if(isrole){
                            mRole = localXmlPullParser.getText();
                            isrole = false;
                        }else if(ismarriage){
                            mMarriage = localXmlPullParser.getText();
                            ismarriage = false;
                        }else if(isholcnt){
                            mHolcnt = localXmlPullParser.getText().replace("\n    ", "");;
                            isholcnt =false;
                        }else if(isattcnt){
                            mAttcnt = localXmlPullParser.getText().replace("\n    ", "");;
                            isattcnt = false;

                            Log.d(TAG,"GetMember mMemnm= "+mMemnm);
                            Log.d(TAG,"GetMember mHolcnt= "+mHolcnt);
                            Log.d(TAG,"GetMember mAttcnt= "+mAttcnt);

                            RowItem rowItem = new RowItem();
                            rowItem.setMemidx((String) mMemidx);
                            rowItem.setMemnm((String) mMemnm);
                            rowItem.setBirth((String) mBirth);
                            rowItem.setSex((String) mSex);
                            rowItem.setRole((String) mRole);
                            rowItem.setMarriage((String) mMarriage);
                            rowItem.setHolcnt((String) mHolcnt);
                            rowItem.setAttcnt((String) mAttcnt);
                            this.rowItems.add(rowItem);
                        }
                        break;
                }

                eventType  = localXmlPullParser.next();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

    }

    private void GetMission(String paramString) {

            try {
                XmlPullParser localXmlPullParser;
                URL url = new URL(paramString);
                Log.d(TAG,"GetMission= "+url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int eventType = localXmlPullParser.getEventType();


                String mYear = "";
                String mMonth = "";
                String mDay = "";
                String mCnt =" ";

                boolean isyear = false  ,ismonth =false , isday =false ,iscnt =false;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if(localXmlPullParser.getName().equals("yyyy")){
                                isyear =true;
                            }else if(localXmlPullParser.getName().equals("mm")){
                                ismonth =true;
                            }else if(localXmlPullParser.getName().equals("dd")){
                                isday = true;
                            }else if(localXmlPullParser.getName().equals("cnt")){
                                iscnt =true;
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if(isyear ) {
                                mYear =localXmlPullParser.getText();
                                isyear=false;

                            }else if(ismonth) {
                                mMonth=localXmlPullParser.getText();
                                ismonth=false;

                            }else if(isday) {
                                mDay=localXmlPullParser.getText();
                                isday=false;

                            }else if(iscnt) {
                                mCnt=localXmlPullParser.getText();
                                iscnt = false;

//                                Log.d(TAG,"mYear"+mYear);
//                                Log.d(TAG,"mMonth"+mMonth);
//                                Log.d(TAG,"mDay"+mDay);
//                                Log.d(TAG,"mCnt"+mCnt);

                                int i = 0;
                                if (!((String) mCnt).equals("")) {
                                    i = Integer.parseInt((String) mCnt);
                                    this.mValues.add(Integer.valueOf(i));
                                    this.mXValues.add(mMonth + "/" +  mDay);
                                }

                            }

                            break;

                    }

                    eventType = localXmlPullParser.next();
                }

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
    }



    private void GetSunday(String paramString) {

            try {
                XmlPullParser localXmlPullParser;
                URL url = new URL(paramString);
                Log.d(TAG,"GetSunday= "+url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int eventType = localXmlPullParser.getEventType();


                boolean ismisidx =false;
                boolean ismisnm =false;
                boolean iscnt =false;
                String mMisidx = "";
                String mMisnm = "";
                String mCnt = "";
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if(localXmlPullParser.getName().equals("misidx")){
                                ismisidx = true;
                            }else if(localXmlPullParser.getName().equals("misnm")){
                                ismisnm =true;
                            }else if(localXmlPullParser.getName().equals("cnt")){
                                iscnt =true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                           if(ismisidx){
                               mMisidx = localXmlPullParser.getText();
                               ismisidx = false;
                           }else if(ismisnm) {
                               mMisnm = localXmlPullParser.getText();
                               ismisnm = false;
                           }else if(iscnt) {
                               mCnt = localXmlPullParser.getText();
                               iscnt = false;
                               int i=0;
                               if (!((String) mCnt).equals("")) {

                                   i = Integer.parseInt((String) mCnt);

                                   this.sValues.add(Integer.valueOf(i));
                                   this.sXValues.add(mMisnm);
                                }


                           }
                            break;
                    }
                    eventType = localXmlPullParser.next();
                }

            } catch (Exception e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
    }

    private void GetMissionData() {  //목장별
        if (this.mValues.size() > 0) {
            this.mValues = null;
            this.mValues = new ArrayList();
        }
        if (this.mXValues.size() > 0) {
            this.mXValues = null;
            this.mXValues = new ArrayList();
        }
        String str = this.urlGetMission + this.misidx + "&fromdt=" + this.txtMisFrom.getText().toString().replace("-", "") + "&todt=" + this.txtMisTo.getText().toString().replace("-", "");
        if ((this.pd == null) || (!this.pd.isShowing())) {
            this.pd = ProgressDialog.show(this, "", "정보를 가져오고 있습니다...", true);
        }
        new MissionAttendAsyncTask().execute(new String[]{str});
    }

    private void GetPrivateData() {//개인별
        this.memberList.removeAllViews();

        String str = this.urlGetMember_r; //출석순
        if (this.ord == 0) {  //이름순
            str = this.urlGetMember_n;
        }

        str = str + this.misidx + "&fromdt=" + this.txtFrom.getText().toString().replace("-", "") + "&todt=" + this.txtTo.getText().toString().replace("-", "");


        if ((this.pd == null) || (!this.pd.isShowing())) {
            this.pd = ProgressDialog.show(this, "", "정보를 가져오고 있습니다...", true);
        }
        new MemberAttendAsyncTask(this).execute(new String[]{str});
    }

    private void GetSundayData() {
        if (this.sValues.size() > 0) {
            this.sValues = null;
            this.sValues = new ArrayList();
        }
        if (this.sXValues.size() > 0) {
            this.sXValues = null;
            this.sXValues = new ArrayList();
        }
        String str = this.urlGetSunday + this.txtSunday.getText().toString().replace("-", "");
        if ((this.pd == null) || (!this.pd.isShowing())) {
            this.pd = ProgressDialog.show(this, "", "정보를 가져오고 있습니다....", true);
        }
        new SundayAttendAsyncTask().execute(new String[]{str});
    }

    private void setTab1() {
        try {
            this.conTab1.removeAllViews();
            XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

            String[] arrayOfString = new String[1];
            arrayOfString[0] = "출석인원";
            int [] colors = new int[1];
            colors[0] = Color.GREEN; //FF0100
          //  int j = localObject2.length;

                //상단 표시 제목과 글자크기
                ((XYMultipleSeriesRenderer) renderer).setChartTitle("선교회별 주일 출석현황");
                ((XYMultipleSeriesRenderer) renderer).setChartTitleTextSize(30);
                //분류명 글자 크기 및 생삭 지정
                ((XYMultipleSeriesRenderer) renderer).setLegendTextSize(25.0F);
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                  for(int i=0;i<colors.length; i++) {
                      ((SimpleSeriesRenderer) seriesRenderer).setColor(colors[i]);
                      ((SimpleSeriesRenderer) seriesRenderer).setDisplayChartValues(true);
                      ((SimpleSeriesRenderer) seriesRenderer).setChartValuesTextSize(18.0F);
                      ((XYMultipleSeriesRenderer) renderer).addSeriesRenderer((SimpleSeriesRenderer) seriesRenderer);
                  }

                //X,Y축 항목이름과 글자 크기
                ((XYMultipleSeriesRenderer) renderer).setXTitle("목장");
                ((XYMultipleSeriesRenderer) renderer).setYTitle("인원");
                ((XYMultipleSeriesRenderer) renderer).setAxisTitleTextSize(25.0F);
                //수치값 글자 크기 / X축 최소, 최대값  /Y축 최소,최대값
                ((XYMultipleSeriesRenderer) renderer).setLabelsTextSize(25.0F);
                ((XYMultipleSeriesRenderer) renderer).setYAxisMin(0.0D);
                ((XYMultipleSeriesRenderer) renderer).setYAxisMax(100.0D);
                ((XYMultipleSeriesRenderer) renderer).setXAxisMin(0.0D);
                ((XYMultipleSeriesRenderer) renderer).setXAxisMax(this.sValues.size() + 1);
                //X,Y축 라인 색상
                ((XYMultipleSeriesRenderer) renderer).setAxesColor(Color.WHITE);
                //상단제목, X,Y 축 제목 ,수치값의 글자 색상
                ((XYMultipleSeriesRenderer) renderer).setLabelsColor(Color.CYAN);
                //X축의 표시 간격
                ((XYMultipleSeriesRenderer) renderer).setXLabels(0);
                //X,Y축 정렬방향
                ((XYMultipleSeriesRenderer) renderer).setXLabelsAlign(Paint.Align.CENTER);
                ((XYMultipleSeriesRenderer) renderer).setYLabelsAlign(Paint.Align.CENTER);
                //X,Y축 스크롤 여부 ON/OFF
                ((XYMultipleSeriesRenderer) renderer).setPanEnabled(false, false);
                //X,Y축 ZOOM기능 OM/Off
                ((XYMultipleSeriesRenderer) renderer).setZoomEnabled(false, false);
                //막대간 간격
                ((XYMultipleSeriesRenderer) renderer).setBarSpacing(0.20000000298023224D);

                ((XYMultipleSeriesRenderer) renderer).setShowGrid(true);
                ((XYMultipleSeriesRenderer) renderer).setShowLegend(false);
                ((XYMultipleSeriesRenderer) renderer).setXLabelsAngle(35.0F);

               //설정 정보 설정
               XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                for(int i=0; i<arrayOfString.length;i++){
                    CategorySeries categorySeries = new CategorySeries(arrayOfString[i]);
                    int[] arrayOfInt = (int[]) this.sundayValue.get(i);
                    for(int j=0;j<arrayOfInt.length;j++){
                        ((CategorySeries) categorySeries).add(arrayOfInt[j]);
                    }

                    ((XYMultipleSeriesDataset) dataset).addSeries(((CategorySeries) categorySeries).toXYSeries());
                 }


                for (int k=0;k < this.sXValues.size();k++) {
                    ((XYMultipleSeriesRenderer) renderer).addXTextLabel(k+1, (String) this.sXValues.get(k));
                }


            //그래프 객체 생성
            GraphicalView mChartView = ChartFactory.getBarChartView(this, (XYMultipleSeriesDataset) dataset, (XYMultipleSeriesRenderer) renderer, BarChart.Type.STACKED);
            this.conTab1.addView(mChartView);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setTab2() {
        try {
            this.conTab2.removeAllViews();
            XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
            String[] arrayOfString = new String[1];
            arrayOfString[0] = "출석인";
            int []  colors = new int[1];
            colors[0] = Color.YELLOW;
            //상단 표시 제목과 글자크기
                ((XYMultipleSeriesRenderer) renderer).setChartTitle("주일 출석현황");
                ((XYMultipleSeriesRenderer) renderer).setChartTitleTextSize(30.0F);
            //분류명 글자 크기 및 생삭 지정
            ((XYMultipleSeriesRenderer) renderer).setLegendTextSize(25.0F);
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            for(int i=0;i<colors.length; i++) {
                ((SimpleSeriesRenderer) seriesRenderer).setColor(colors[0]);
                ((SimpleSeriesRenderer) seriesRenderer).setDisplayChartValues(true);
                ((SimpleSeriesRenderer) seriesRenderer).setChartValuesTextSize(18.0F);
                ((XYMultipleSeriesRenderer) renderer).addSeriesRenderer((SimpleSeriesRenderer) seriesRenderer);
            }
            //X,Y축 항목이름과 글자 크기
            ((XYMultipleSeriesRenderer) renderer).setXTitle("날짜(주일)");
            ((XYMultipleSeriesRenderer) renderer).setYTitle("인원");
            ((XYMultipleSeriesRenderer) renderer).setAxisTitleTextSize(25.0F);
            //수치값 글자 크기 / X축 최소, 최대값  /Y축 최소,최대값  //수치값 글자 크기 / X축 최소, 최대값  /Y축 최소,최대값
            ((XYMultipleSeriesRenderer) renderer).setLabelsTextSize(25.0F);
            ((XYMultipleSeriesRenderer) renderer).setYAxisMin(0.0D);
            ((XYMultipleSeriesRenderer) renderer).setYAxisMax(100.0D);
            ((XYMultipleSeriesRenderer) renderer).setXAxisMin(0.0D);
            ((XYMultipleSeriesRenderer) renderer).setXAxisMax(this.mValues.size() + 1);
            //X,Y축 라인 색상
            ((XYMultipleSeriesRenderer) renderer).setAxesColor(Color.WHITE);
            //상단제목, X,Y 축 제목 ,수치값의 글자 색상
            ((XYMultipleSeriesRenderer) renderer).setLabelsColor(Color.MAGENTA);
            //X축의 표시 간격
            ((XYMultipleSeriesRenderer) renderer).setXLabels(0);
            //X,Y축 정렬방향
            ((XYMultipleSeriesRenderer) renderer).setXLabelsAlign(Paint.Align.CENTER);
            ((XYMultipleSeriesRenderer) renderer).setYLabelsAlign(Paint.Align.CENTER);
            //X,Y축 스크롤 여부 ON/OFF
                ((XYMultipleSeriesRenderer) renderer).setPanEnabled(false, false);
            //X,Y축 ZOOM기능 OM/Off
                ((XYMultipleSeriesRenderer) renderer).setZoomEnabled(false, false);
            //막대간 간격
                ((XYMultipleSeriesRenderer) renderer).setBarSpacing(0.4000000059604645D);

                ((XYMultipleSeriesRenderer) renderer).setShowGrid(true);
                ((XYMultipleSeriesRenderer) renderer).setShowLegend(false);
                ((XYMultipleSeriesRenderer) renderer).clearXTextLabels();


                //설정 정보 설정
               XYMultipleSeriesDataset    dataset = new XYMultipleSeriesDataset();

                for (int i = 0;i < arrayOfString.length;i++) {
                    CategorySeries categorySeries = new CategorySeries(arrayOfString[i]);
                    int[] arrayOfInt = (int[]) this.missionValue.get(i);
                    for(int j=0; j<arrayOfInt.length;j++){
                        ((CategorySeries) categorySeries).add(arrayOfInt[j]);
                    }
                    ((XYMultipleSeriesDataset) dataset).addSeries(((CategorySeries) categorySeries).toXYSeries());
                }

            for (int k=0;k < this.mXValues.size();k++) {
                ((XYMultipleSeriesRenderer) renderer).addXTextLabel(k + 1, (String) this.mXValues.get(k));
            }

            //그래프 객체 생성
            GraphicalView mChartView = ChartFactory.getBarChartView(this, (XYMultipleSeriesDataset) dataset, (XYMultipleSeriesRenderer) renderer, BarChart.Type.STACKED);
            this.conTab2.addView((View) mChartView);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private void updateDisplay(int paramInt) {
        String str1;
        String str2;
        Log.d(TAG, "updateDisplay= " +paramInt);
        switch (paramInt) {

            case 0:
                if (this.mMonth_F + 1 < 10) {
                    str1 = "0" + (this.mMonth_F + 1);

                } else {
                    str1 = String.valueOf(this.mMonth_F + 1);
                }
                if (mDay_F < 10) {
                    str2 = "0" + this.mDay_F;
                } else {
                    str2 = String.valueOf(this.mDay_F);
                }

                this.txtFrom.setText(new StringBuilder().append(this.mYear_F).append("-").append(str1).append("-").append(str2));
                break;

            case 1:
                if (this.mMonth_T + 1 < 10) {
                    str1 = "0" + (this.mMonth_T + 1);
                } else {
                    str1 = String.valueOf(this.mMonth_T + 1);
                }

                if (this.mDay_T < 10) {
                    str2 = "0" + this.mDay_T;
                } else {
                    str2 = String.valueOf(this.mDay_T);
                }

                this.txtTo.setText(new StringBuilder().append(this.mYear_T).append("-").append(str1).append("-").append(str2));

                break;

            case 2:  //주일
                if (this.mMonth_S + 1 < 10) {
                    str1 = "0" + (this.mMonth_S + 1);

                } else {
                    str1 = String.valueOf(this.mMonth_S + 1);
                }

                if (this.mDay_S < 10) {
                    str2 = "0" + this.mDay_S;
                } else {
                    str2 = String.valueOf(this.mDay_S);
                }

                this.txtSunday.setText(new StringBuilder().append(this.mYear_S).append("-").append(str1).append("-").append(str2));

                break;



            case 4:   //목장 기간1

                if (this.mMonth_Mis_F + 1 < 10) {
                    str1 = "0" + (this.mMonth_Mis_F + 1);

                } else {
                    str1 = String.valueOf(this.mMonth_Mis_F + 1);
                }

                if (this.mDay_Mis_F < 10) {
                    str2 = "0" + this.mDay_Mis_F;
                } else {
                    str2 = String.valueOf(this.mDay_Mis_F);
                }

                this.txtMisFrom.setText(new StringBuilder().append(this.mYear_Mis_F).append("-").append(str1).append("-").append(str2));

                break;
            case 5:  //목장 기간2
                if (this.mMonth_Mis_T + 1 < 10) {
                    str1 = "0" + (this.mMonth_Mis_T + 1);

                } else {
                    str1 = String.valueOf(this.mMonth_Mis_T + 1);
                }


                if (this.mDay_Mis_T < 10) {
                    str2 = "0" + this.mDay_Mis_T;
                } else {
                    str2 = String.valueOf(this.mDay_Mis_T);
                }

                this.txtMisTo.setText(new StringBuilder().append(this.mYear_Mis_T).append("-").append(str1).append("-").append(str2));


                break;
            default:
                break;
        }


    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.memberattendactivity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 항상 켜기
        this.memberList = ((TableLayout) findViewById(R.id.memberList));
        this.txtFrom = ((TextView) findViewById(R.id.txtFrom));
        this.txtTo = ((TextView) findViewById(R.id.txtTo));
        this.btnList = ((Button) findViewById(R.id.btnList));
        this.btnSort = ((Button) findViewById(R.id.btnSort));
        this.btnList.setTextColor(-16711936);
        this.btnSort.setTextColor(-1);
        this.tabHost = ((TabHost) findViewById(R.id.tabhost));
        this.tabHost.setup();
        TabHost.TabSpec spec1 = this.tabHost.newTabSpec("tag1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("주일전체", getResources().getDrawable(R.drawable.buttons));

        this.tabHost.addTab(spec1);
        TabHost.TabSpec spec2 = this.tabHost.newTabSpec("tag2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator(((CommonValue) getApplication()).getMisNm(), getResources().getDrawable(R.drawable.icon_mi));
        this.tabHost.addTab(spec2);
        TabHost.TabSpec spec3 = this.tabHost.newTabSpec("tag3");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("개인별", getResources().getDrawable(R.drawable.icon_pr));
        this.tabHost.addTab(spec3);
        this.tabHost.setCurrentTab(0);
        this.tab1 = ((LinearLayout) findViewById(R.id.tab1));
        this.tab2 = ((LinearLayout) findViewById(R.id.tab2));
        this.tab3 = ((LinearLayout) findViewById(R.id.tab3));
        this.conTab1 = ((LinearLayout) findViewById(R.id.conTab1));
        this.conTab2 = ((LinearLayout) findViewById(R.id.conTab2));
        this.txtSunday = ((TextView) findViewById(R.id.txtSunday));
        this.txtMisFrom = ((TextView) findViewById(R.id.txtMisFrom));
        this.txtMisTo = ((TextView) findViewById(R.id.txtMisTo));
        this.btnMisList = ((Button) findViewById(R.id.btnMisList));
        try {
            this.misidx = getIntent().getExtras().get("misidx").toString();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            cal.add(Calendar.DAY_OF_WEEK,  1 - cal.get(Calendar.DAY_OF_WEEK));
            this.mYear_F = cal.get(Calendar.YEAR);
            this.mMonth_F = cal.get(Calendar.MONTH);
            this.mDay_F = cal.get(Calendar.DATE);
            updateDisplay(0);
            Calendar calt = Calendar.getInstance();
            calt.add(Calendar.DAY_OF_WEEK,  1 - calt.get(Calendar.DAY_OF_WEEK));
            this.mYear_T = calt.get(Calendar.YEAR);
            this.mMonth_T = calt.get(Calendar.MONTH);
            this.mDay_T = calt.get(Calendar.DATE);
            updateDisplay(1);
            //주일전체
             Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.DAY_OF_WEEK,  1-cal2.get(Calendar.DAY_OF_WEEK));
            this.mYear_S = cal2.get(Calendar.YEAR);
            this.mMonth_S = cal2.get(Calendar.MONTH);
            this.mDay_S = cal2.get(Calendar.DATE);
            updateDisplay(2);

             Calendar calF = Calendar.getInstance();
            calF.add(Calendar.MONTH, -1);
            calF.add(Calendar.DAY_OF_WEEK,  1-calF.get(Calendar.DAY_OF_WEEK));
            this.mYear_Mis_F = calF.get(Calendar.YEAR);
            this.mMonth_Mis_F = calF.get(Calendar.MONTH);
            this.mDay_Mis_F = calF.get(Calendar.DATE);
            updateDisplay(4);

             Calendar calmt = Calendar.getInstance();
            calmt.add(Calendar.DAY_OF_WEEK,  1-calmt.get(Calendar.DAY_OF_WEEK));
            this.mYear_Mis_T = calmt.get(Calendar.YEAR);
            this.mMonth_Mis_T = calmt.get(Calendar.MONTH);
            this.mDay_Mis_T = calmt.get(Calendar.DATE);
            updateDisplay(5);
            this.txtMisFrom.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    MemberAttendActivity.this.showDialog(4);
                }
            });
            this.txtMisTo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    MemberAttendActivity.this.showDialog(5);
                }
            });
            this.txtSunday.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    MemberAttendActivity.this.showDialog(2);
                }
            });
            this.btnMisList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {     //조회
                    MemberAttendActivity.this.GetMissionData();
                }
            });
            this.txtFrom.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    MemberAttendActivity.this.showDialog(0);
                }
            });
            this.txtTo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    MemberAttendActivity.this.showDialog(1);
                }
            });
            this.btnList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {   //이름순
                    MemberAttendActivity.this.ord = 0;
                    MemberAttendActivity.this.btnList.setTextColor(-16711936);
                    MemberAttendActivity.this.btnSort.setTextColor(-1);
                    MemberAttendActivity.this.GetPrivateData();
                }
            });
            this.btnSort.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {  //출석순
                    MemberAttendActivity.this.ord = 1;
                    MemberAttendActivity.this.btnList.setTextColor(-1);
                    MemberAttendActivity.this.btnSort.setTextColor(-16711936);
                    MemberAttendActivity.this.GetPrivateData();
                }
            });
            GetSundayData();
            GetMissionData();
            GetPrivateData();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected Dialog onCreateDialog(int paramInt) {
        switch (paramInt) {
            case 0:
                return new DatePickerDialog(this, this.mFromSetListener, this.mYear_F, this.mMonth_F, this.mDay_F);

            case 1:
                return new DatePickerDialog(this, this.mToSetListener, this.mYear_T, this.mMonth_T, this.mDay_T);

            case 2:
                return new DatePickerDialog(this, this.mSundaySetListener, this.mYear_S, this.mMonth_S, this.mDay_S);
            case 3:

                return null;
            case 4:
                return new DatePickerDialog(this, this.mMisFromSetListener, this.mYear_F, this.mMonth_F, this.mDay_F);
            case 5:
                return new DatePickerDialog(this, this.mMisToSetListener, this.mYear_T, this.mMonth_T, this.mDay_T);
            default:
                return null;
        }

    }

    private class MemberAttendAsyncTask extends AsyncTask<String, Integer, Long> {
        Context ctx;

        public MemberAttendAsyncTask(Context paramContext) {
            this.ctx = paramContext;
        }

        protected Long doInBackground(String... paramVarArgs) {
            MemberAttendActivity.this.rowMember = null;
            MemberAttendActivity.this.rowMember = new ArrayList();
            MemberAttendActivity.this.rowItems = null;
            MemberAttendActivity.this.rowItems = new ArrayList();
            MemberAttendActivity.this.GetMember(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {

            if (MemberAttendActivity.this.ord > 0) {
                Collections.sort(rowItems);
            }

            MemberAttendActivity.this.isFirst = false;
            if ((MemberAttendActivity.this.pd != null) && (MemberAttendActivity.this.pd.isShowing())) {
                MemberAttendActivity.this.pd.dismiss();
            }
            for (int i = 0; i < MemberAttendActivity.this.rowItems.size(); i++) {
                TextView textView = new TextView(this.ctx);
                TextView localObject1 = new TextView(this.ctx);
                TextView localTextView1 = new TextView(this.ctx);
                TextView localTextView2 = new TextView(this.ctx);
                TextView localTextView3 = new TextView(this.ctx);

                RowItem rowItem = MemberAttendActivity.this.rowItems.get(i);
                textView.setText(rowItem.getMemnm());
                textView.setWidth(100);
                textView.setGravity(3);

                localObject1.setText(rowItem.getAge());
                localObject1.setWidth(100);
                localObject1.setGravity(17);
                localObject1.setTextColor(-1);

                localTextView1.setText(rowItem.getSex());
                localTextView1.setWidth(100);
                localTextView1.setGravity(17);
                localTextView1.setTextColor(-1);

                localTextView2.setText(rowItem.getMarriage());
                localTextView2.setWidth(100);
                localTextView2.setGravity(17);
                localTextView2.setTextColor(-1);

                localTextView3.setText(rowItem.getAttRateToString());  //출석률
                localTextView3.setWidth(100);
                localTextView3.setGravity(5);
                localTextView3.setTextColor(-1);

                if (rowItem.getRoleCode().equals("A")) {
                    textView.setTextColor(getResources().getColor(R.color.mis));
                } else if (rowItem.getRoleCode().equals("B")) {
                    textView.setTextColor(getResources().getColor(R.color.mis1));
                } else if (rowItem.getRoleCode().equals("C")) {
                    textView.setTextColor(getResources().getColor(R.color.cell0));
                } else if (rowItem.getRoleCode().equals("D")) {
                    textView.setTextColor(getResources().getColor(R.color.cell1));
                } else if (rowItem.getRoleCode().equals("E")) {
                    textView.setTextColor(-1);
                } else if (rowItem.getRoleCode().equals("F")) {
                    textView.setTextColor(getResources().getColor(R.color.newbie));
                } else if(rowItem.getRoleCode().equals("G")){
                    textView.setTextColor(getResources().getColor(R.color.red));
                }

                TextView localObject2 = new TextView(this.ctx);
                TextView localTextView4 = new TextView(this.ctx);
                TextView localTextView5 = new TextView(this.ctx);
                TextView localTextView6 = new TextView(this.ctx);
                localObject2.setText("/");
                localTextView4.setText("/");
                localTextView5.setText("/");
                localTextView6.setText("/");
                TableRow localTableRow = new TableRow(this.ctx);
                localTableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                localTableRow.setPadding(0, 10, 5, 5);
                localTableRow.addView(textView);
                localTableRow.addView(localObject2);
                localTableRow.addView(localObject1);
                localTableRow.addView(localTextView4);
                localTableRow.addView(localTextView1);
                localTableRow.addView(localTextView5);
                localTableRow.addView(localTextView2);
                localTableRow.addView(localTextView6);
                localTableRow.addView(localTextView3);

                MemberAttendActivity.this.rowMember.add(localTableRow);
                MemberAttendActivity.this.memberList.addView((View) localTableRow);

            }
        }
    }

    private class MissionAttendAsyncTask extends AsyncTask<String, Integer, Long> {
        public MissionAttendAsyncTask() {
        }

        protected Long doInBackground(String... paramVarArgs) {
           GetMission(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            int [] ivalue = new int[MemberAttendActivity.this.mValues.size()];

            if ((MemberAttendActivity.this.pd != null) && (MemberAttendActivity.this.pd.isShowing()) && (!MemberAttendActivity.this.isFirst)) {
                MemberAttendActivity.this.pd.dismiss();
            }


            for(int i = 0; i< ivalue.length; i++){
                ivalue[i] = ((Integer) mValues.get(i)).intValue();
            }

            if (MemberAttendActivity.this.missionValue.size() > 0) {
                MemberAttendActivity.this.missionValue = null;
                MemberAttendActivity.this.missionValue = new ArrayList();
            }
            MemberAttendActivity.this.missionValue.add(ivalue);
            MemberAttendActivity.this.setTab2();

        }

    }

    private class SundayAttendAsyncTask extends AsyncTask<String, Integer, Long> {
        public SundayAttendAsyncTask() {
        }

        protected Long doInBackground(String... paramVarArgs) {
           GetSunday(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            int [] ivalue = new int[MemberAttendActivity.this.sValues.size()];

            if ((MemberAttendActivity.this.pd != null) && (MemberAttendActivity.this.pd.isShowing()) && (!MemberAttendActivity.this.isFirst)) {
                MemberAttendActivity.this.pd.dismiss();
            }

            for (int i = 0;i<ivalue.length;i++  ) {
                ivalue[i] = ((Integer) MemberAttendActivity.this.sValues.get(i)).intValue();
            }
            if (MemberAttendActivity.this.sundayValue.size() > 0) {
                MemberAttendActivity.this.sundayValue = null;
                MemberAttendActivity.this.sundayValue = new ArrayList();
            }
            MemberAttendActivity.this.sundayValue.add(ivalue);
            MemberAttendActivity.this.setTab1();
        }
    }


    class RowItem implements Comparable<RowItem> {
        String attcnt = "";
        String birth = "";
        String holcnt = "";
        String marriage = "";
        String memidx = "";
        String memnm = "";
        String role = "";
        String sex = "";

        RowItem() {
        }

        public int compareTo(RowItem paramRowItem) {
            Integer localInteger1 = Integer.valueOf(0);
            if (!getAttcnt().equals("")) {
                localInteger1 = Integer.valueOf(Integer.parseInt(getAttcnt()));
            }
            Integer localInteger2 = Integer.valueOf(0);
            if (!paramRowItem.getAttcnt().equals("")) {
                localInteger2 = Integer.valueOf(Integer.parseInt(paramRowItem.getAttcnt()));
            }
            return localInteger1.compareTo(localInteger2);
        }

        public String getAge() {
            int j = Calendar.getInstance().get(Calendar.YEAR);
            int i = 0;

            if (birth.equals("") || birth == null) {
                Log.d(TAG,"birth null");
            }else{
                i = j - Integer.parseInt(this.birth) + 1;
            }
            if (i > 0) {
                return i + "세";
            }
            return "없음";
        }

        public double getAttRate() {
            return Integer.parseInt(this.attcnt) * 1.0D / (Integer.parseInt(this.holcnt) * 1.0D) * 100.0D;
        }

        public String getAttRateToString() {
            int i = 0;
            if (!this.holcnt.equals("")) {
                i = Integer.parseInt(this.holcnt);
            }
            int j = 0;
            if (!this.attcnt.equals("")) {
                j = Integer.parseInt(this.attcnt);
            }
            try {
                String str = String.format("%.1f", new Object[]{Double.valueOf(j * 1.0D / (i * 1.0D) * 100.0D)}) + "%";
                return str;
            } catch (Exception localException) {
            }
            return "없음";
        }

        public String getAttcnt() {
            return this.attcnt;
        }

        public String getBirth() {
            return this.birth;
        }

        public String getHolcnt() {
            return this.holcnt;
        }

        public String getMarriage() {
            if (!this.marriage.equals("")) {
                if (this.marriage.equals("Y")) {
                    return "기혼";
                }
                return "미혼";
            }
            return "없음";
        }

        public String getMemidx() {
            return this.memidx;
        }

        public String getMemnm() {
            return this.memnm;
        }

        public String getRole() {
            String str = "";

            if (this.role.equals("A")) {
                str = "전도사";
                return str;
            }else if (this.role.equals("B")) {
                str = "선교사";
                return str;
            }else if(this.role.equals("C")) {
                str = "선교목양사";
                return str;
            }else if (this.role.equals("D")) {
                str = "셀장";
                return str;
            }else if(this.role.equals("E")) {
                str= "셀원";
                return str;
            }else if (!this.role.equals("F")){
                str= "새가족";
                return str;
            }else if (this.role.equals("G")) {
                str= "관리";
                return str;
            }else {
                return "없음";
            }
        }

        public String getRoleCode() {
            return this.role;
        }

        public String getSex() {
            if (!this.sex.equals("")) {
                if (this.sex.equals("M")) {
                    return "형제";
                }
                return "자매";
            }
            return "없음";
        }

        public void setAttcnt(String paramString) {
            this.attcnt = paramString;
        }

        public void setBirth(String paramString) {
            this.birth = paramString;
        }

        public void setHolcnt(String paramString) {
            this.holcnt = paramString;
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

        public void setRole(String paramString) {
            this.role = paramString;
        }

        public void setSex(String paramString) {
            this.sex = paramString;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}