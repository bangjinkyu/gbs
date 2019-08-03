package com.android.sungrackgbs;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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

public class ThirdFragment extends Fragment {

    private static final String TAG = "ThirdFragment";
    private LinearLayout Tab3;
    private String urlGetSunday = "http://www.bwm.or.kr/attend/m_attend_holyday_g3.php?day=";
    private ArrayList<Integer> sValues = new ArrayList();
    private ArrayList<String> sXValues = new ArrayList();
    private ArrayList<int[]> sundayValue = new ArrayList();
    private TextView txtSunday;
    private ProgressDialog pd;
    private Context mContext;
    private String misidx;
    private Boolean isFirst = true;
    private int mMonth_S;
    private int mYear_S;
    private int mDay_S;

    private DatePickerDialog.OnDateSetListener mSundaySetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            mYear_S = paramAnonymousInt1;
            mMonth_S = paramAnonymousInt2;
            mDay_S = paramAnonymousInt3;
            updateDisplay(2);
            GetSundayData();
        }
    };

    private void updateDisplay(int paramInt) {
        String str1;
        String str2;
        Log.d(TAG, "updateDisplay= " +paramInt);

        //주일
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =  context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        Tab3 = (LinearLayout)view.findViewById(R.id.Tab3);
        txtSunday = (TextView) view.findViewById(R.id.txtSunday);
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_WEEK,  1-cal2.get(Calendar.DAY_OF_WEEK));
        this.mYear_S = cal2.get(Calendar.YEAR);
        this.mMonth_S = cal2.get(Calendar.MONTH);
        this.mDay_S = cal2.get(Calendar.DATE);
        updateDisplay(2);
        txtSunday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                //onCreateDialog(2);
                new DatePickerDialog(mContext, mSundaySetListener,mYear_S,mMonth_S, mDay_S).show();
            }
        });
        GetSundayData();
    }

    private void GetSundayData() {
        if (sValues.size() > 0) {
            sValues = null;
            sValues = new ArrayList();
        }
        if (this.sXValues.size() > 0) {
            this.sXValues = null;
            this.sXValues = new ArrayList();
        }
        String str = urlGetSunday + txtSunday.getText().toString().replace("-", "");
        if ((this.pd == null) || (!this.pd.isShowing())) {
            this.pd = ProgressDialog.show(mContext, "", "정보를 가져오고 있습니다....", true);
        }
        new SundayAttendAsyncTask().execute(new String[]{str});
    }

    private class SundayAttendAsyncTask extends AsyncTask<String, Integer, Long> {
        public SundayAttendAsyncTask() {
        }

        protected Long doInBackground(String... paramVarArgs) {
            GetSunday(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            int [] ivalue = new int[sValues.size()];

            if ((pd != null) && (pd.isShowing()) ) {
                pd.dismiss();
            }

            for (int i = 0;i<ivalue.length;i++  ) {
                ivalue[i] = ((Integer) sValues.get(i)).intValue();
            }
            if (sundayValue.size() > 0) {
                sundayValue = null;
                sundayValue = new ArrayList();
            }
            sundayValue.add(ivalue);
            setTab3();
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

                                sValues.add(Integer.valueOf(i));
                                sXValues.add(mMisnm);
                            }


                        }
                        break;
                }
                eventType = localXmlPullParser.next();
            }

        } catch (Exception e) {

            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void setTab3() {
        try {
            Tab3.removeAllViews();
            XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

            String[] arrayOfString = new String[1];
            arrayOfString[0] = "출석인원";
            int [] colors = new int[1];
            colors[0] = Color.GREEN; //FF0100
            //  int j = localObject2.length;

            //상단 표시 제목과 글자크기
            ((XYMultipleSeriesRenderer) renderer).setChartTitle("GBS별 주일 출석현황");
            ((XYMultipleSeriesRenderer) renderer).setChartTitleTextSize(40);
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
            ((XYMultipleSeriesRenderer) renderer).setXTitle("조별");
            ((XYMultipleSeriesRenderer) renderer).setYTitle("인원");
            ((XYMultipleSeriesRenderer) renderer).setAxisTitleTextSize(35.0F);
            //수치값 글자 크기 / X축 최소, 최대값  /Y축 최소,최대값
            ((XYMultipleSeriesRenderer) renderer).setLabelsTextSize(25.0F);
            ((XYMultipleSeriesRenderer) renderer).setYAxisMin(0.0D);
            ((XYMultipleSeriesRenderer) renderer).setYAxisMax(40.0D);
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
            GraphicalView mChartView = ChartFactory.getBarChartView(mContext, (XYMultipleSeriesDataset) dataset, (XYMultipleSeriesRenderer) renderer, BarChart.Type.STACKED);
            Tab3.addView(mChartView);
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
