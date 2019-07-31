package com.android.sungrackgbs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

//출석체크  화면

public class MemberActivity extends Activity {
    private final String TAG="MemberActivity";
    static final int DATE_DIALOG_ID = 0;
    private int acMis = 0;               //출석한 인원
    private int acCellO = 0;
    private int acCell1 = 0;
    private int acCellM = 0;
    private int acNewbie = 0;
    private String attended = "";
    private ArrayList<ToggleButton> btnMember;
    private int cMis = 0;             //전도사 or 선교사
    private int cCell1 = 0;           //선교목양사
    private int cCellO = 0;           //셀장
    private int mCellM = 0;           //셀원
    private int mNewbie = 0;         //새가족
    private int totalCount = 0;
    private Button datepicker; //날짜
    private int mDay;
    private int mMonth;
    private int mYear;
    private GridView memberList;
    private String misidx;
    private String MisName;
    private ProgressDialog pd;
    private Button reset; //리셋 버큰
    private String saveMsg = "";
    private Button submit; //저장버튼
    private Button btnexcel;//엑셀다운 버튼
    private TextView today;
    TextView tvCellM;//출석한 셀장
    TextView tvCellO;//출석한 셀원
    TextView tvTotal;  //출석한 토탈
    TextView txtresult; //출석한 인원
    String urlMemberList = "http://www.bwm.or.kr/attend/m_member_list.php?misidx=";
    String urlSaveAttendList = "http://www.bwm.or.kr/attend/m_attendance_insert.php?";
    String yyyymmdd = "";
    static final int REQUEST_IMAGE_GET = 1;


    private void SetTotalStatus() {
        try {
            double d1 = (this.acMis + this.acCellO + this.acCell1 + this.acCellM + this.acNewbie)* 1.0D / (this.cMis + this.cCellO + this.cCell1 + this.mCellM + this.mNewbie)* 1.0D;
            double d2 = (this.acMis + this.acCellO + this.acCell1)* 1.0D / (this.cMis + this.cCellO + this.cCell1)* 1.0D;
            double d3 = ((this.acCellM + this.acNewbie)* 1.0D) / ((this.mCellM + this.mNewbie)* 1.0D);
            this.txtresult.setText(String.valueOf(this.acMis + this.acCellO + this.acCell1 + this.acCellM + this.acNewbie));
            this.tvTotal.setText(String.valueOf(this.acMis + this.acCellO + this.acCell1 + this.acCellM + this.acNewbie) + " / " + String.valueOf(this.cMis + this.cCellO + this.cCell1 + this.mCellM + this.mNewbie) + " (" + String.format("%.1f", new Object[]{Double.valueOf(d1 * 100.0D)}) + "%)");
            this.tvCellO.setText(String.valueOf(this.acMis + this.acCellO + this.acCell1) + " / " + String.valueOf(this.cMis + this.cCellO + this.cCell1) + " (" + String.format("%.1f", new Object[]{Double.valueOf(d2 * 100.0D)}) + "%)");
            this.tvCellM.setText(String.valueOf(this.acCellM + this.acNewbie) + " / " + String.valueOf(this.mCellM + this.mNewbie) + " (" + String.format("%.1f", new Object[]{Double.valueOf(d3 * 100.0D)}) + "%)");

        } catch (Exception localException) {
            Toast.makeText(this, localException.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getMemberList(String paramString) {

            try {
                XmlPullParser localXmlPullParser;
                URL url = new URL(paramString);
                Log.d(TAG, "URL to : " + url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int eventType = localXmlPullParser.getEventType();
                String str = "";
                String mMemidx = "";
                String mMemnm = "";
                String mRole = "";
                String mAttend = "";
                boolean ismemidx = false;
                boolean ismemnm =false;
                boolean isrole = false;
                boolean isattend = false;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG :
                            if (localXmlPullParser.getName().equals("memidx")) {
                                  //Log.d(TAG,"misidx= "+localXmlPullParser.getName());
                                ismemidx=true;
                            }else if(localXmlPullParser.getName().equals("memnm")){
                              //  Log.d(TAG,"memnm= "+localXmlPullParser.getName());
                                ismemnm=true;
                            }else if(localXmlPullParser.getName().equals("role")){
                                isrole=true;
                              //  Log.d(TAG,"role= "+localXmlPullParser.getName());
                            }else if(localXmlPullParser.getName().equals("isattend")){
                             //   Log.d(TAG,"isattend= "+localXmlPullParser.getName());
                                isattend = true;
                            }else if(localXmlPullParser.getName().equals("today")){
                                   Log.d(TAG,"today");
                            }

                            break;
                        case XmlPullParser.END_TAG :
                            break;
                        case XmlPullParser.TEXT:
                            if (ismemidx) {

                                mMemidx= localXmlPullParser.getText();
                                ismemidx=false;
                            } else if (ismemnm) {

                                ismemnm =false;
                                mMemnm = localXmlPullParser.getText();
                            } else if(isrole){

                                isrole =false;
                                mRole = localXmlPullParser.getText();
                            } else if(isattend){
                                isattend=false;
                                mAttend = localXmlPullParser.getText();

                                Log.d(TAG," mMemidx" +mMemidx);
                                Log.d(TAG," mMemnm= " +mMemnm);
//                                Log.d(TAG," mRole" +mRole);
                                Log.d(TAG," isattend= " +mAttend);


                                ToggleButton toggleButton = new ToggleButton(this);
                                toggleButton.setBackgroundResource(R.drawable.togglebuttons);
                                toggleButton.setTag(mMemidx);
                                toggleButton.setHint( mRole);
                                toggleButton.setContentDescription(mAttend);
                                toggleButton.setText( mMemnm);
                                toggleButton.setTextOn(( mMemnm));
                                toggleButton.setTextOff( mMemnm);
                                if ((mRole).equals("A")) { //전도사
                                    toggleButton.setTextColor(getResources().getColor(R.color.mis));
                                    cMis += 1;
                                } else if( mRole.equals("B")) { //선교사
                                    toggleButton.setTextColor(getResources().getColor(R.color.mis1));
                                    cMis += 1;
                                }else if (mRole.equals("C")) { //선교 목양사
                                    toggleButton.setTextColor(getResources().getColor(R.color.cell0));
                                    cCellO += 1;
                                } else if (mRole.equals("D")) { // 셀장
                                    toggleButton.setTextColor(getResources().getColor(R.color.cell1));
                                   this.cCell1 += 1;
                                }else if (mRole.equals("E")) {   //셀원
                                    toggleButton.setTextColor(-1);
                                   this.mCellM += 1;
                                }else if(mRole.equals("F")){           //새가족
                                    toggleButton.setTextColor(getResources().getColor(R.color.newbie));
                                   this.mNewbie += 1;
                                }else{    // role  =G   // 관리
                                    toggleButton.setTextColor(getResources().getColor(R.color.red));
                                    this.mNewbie += 1;
                                }


                                toggleButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                    String mroll=((ToggleButton)v).getHint().toString();
                                     if (((ToggleButton) v).isChecked()) { //체크한 부분

                                         switch (mroll) {
                                             case "A":
                                             case "B":
                                                 acMis += 1;
                                                 break;
                                             case  "C":
                                                 acCellO +=1;
                                                 break;
                                             case  "D":
                                                 acCell1 += 1;
                                                 break;
                                             case  "E":
                                                 acCellM += 1;
                                                 break;
                                             case  "F":
                                                 acNewbie += 1;
                                                 break;
                                             case  "G":
                                                 acNewbie += 1;
                                                 break;
                                         }
                                     }else{         //체크 x

                                         switch (mroll) {
                                             case "A":
                                             case "B":
                                                 acMis -= 1;
                                                 break;
                                             case  "C":
                                                 acCellO -=1;
                                                 break;
                                             case  "D":
                                                 acCell1 -= 1;
                                                 break;
                                             case  "E":
                                                 acCellM -= 1;
                                                 break;
                                             case  "F":
                                                 acNewbie -= 1;
                                                 break;
                                             case  "G":
                                                 acNewbie -= 1;
                                                 break;
                                         }
                                     }
                                      MemberActivity.this.SetTotalStatus();
                                    }
                                });


                                this.btnMember.add(toggleButton);

                            }
                        break;
                    }//end swith
                    eventType = localXmlPullParser.next();
                }//end while
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

        }


    private String sendAttendList(String paramString) {
        String str = "";

            try {
                XmlPullParser localXmlPullParser;
                URL url = new URL(paramString);
                Log.d(TAG,"sendAttendList= "+url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int parserEvent  = localXmlPullParser.getEventType();
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            str = localXmlPullParser.getText();
                            break;
                    }
                    parserEvent = localXmlPullParser.next();
                }

                return str;

            } catch (Exception e) {
                return e.getMessage();
            }
    }


    private void updateDisplay() {
        String str1 = String.valueOf(this.mMonth+1);
        String str2 = String.valueOf(this.mDay);;

        if (this.mMonth + 1 < 10) {
            str1 = "0" + (this.mMonth + 1);

        }

        if (this.mDay <10) {
             str2 = "0" + this.mDay;
        }


        this.today.setText(new StringBuilder().append(this.mYear).append("-").append(str1).append("-").append(str2));
        this.yyyymmdd = (this.mYear + str1 + str2);
        this.cMis = 0;
        this.cCellO = 0;
        this.cCell1 = 0;
        this.mCellM = 0;
        this.mNewbie = 0;
        this.acMis = 0;
        this.acCellO = 0;
        this.acCell1 = 0;
        this.acCellM = 0;
        this.acNewbie = 0;
        Log.d(TAG,"yyyymmdd"+yyyymmdd);
        MemberActivity.this.pd = ProgressDialog.show(this, "", "출석부를 만들고 있습니다....", true);
        new MemberAsyncTask(MemberActivity.this).execute(new String[]{this.urlMemberList + this.misidx + "&today=" + this.yyyymmdd});
         totalCount = 0;
        attended = "";
        saveMsg = "";
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.memberactivity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 항상 켜기
        memberList = ((GridView) findViewById(R.id.memberList));
        btnMember = new ArrayList<ToggleButton>();
        misidx = getIntent().getExtras().get("misidx").toString();
        MisName = ((CommonValue)getApplication()).getMisNm();
        Log.d(TAG,"misidx"+misidx);
        urlSaveAttendList = (urlSaveAttendList + "misidx=" + this.misidx + "&memidx=");
        txtresult = (TextView) findViewById(R.id.result);
        today = (TextView) findViewById(R.id.today);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvCellO = (TextView) findViewById(R.id.tvCellO);
        tvCellM = (TextView) findViewById(R.id.tvCellM);
        btnexcel = (Button)findViewById(R.id.btnexcel);
        reset = (Button) findViewById(R.id.btnreset);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MemberActivity.this);
                builder.setMessage("설정된 데이터를 초기화 하시겠습니까?");
                builder.setCancelable(false);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                        MemberActivity.this.attended = "";
                        MemberActivity.this.totalCount = 0;
                        MemberActivity.this.acMis = 0;
                        MemberActivity.this.acCellO = 0;
                        MemberActivity.this.acCell1 = 0;
                        MemberActivity.this.acCellM = 0;
                        MemberActivity.this.acNewbie = 0;
                        MemberActivity.this.SetTotalStatus();
                        paramAnonymous2Int = 0;
                        for (; ; ) {
                            if (paramAnonymous2Int >= MemberActivity.this.btnMember.size()) {
                                return;
                            }
                            ((ToggleButton) MemberActivity.this.btnMember.get(paramAnonymous2Int)).setChecked(false);
                            paramAnonymous2Int += 1;
                        }
                    }
                });
                 builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                    }
                });
                builder.create().show();
            }
        });
        this.submit = ((Button) findViewById(R.id.btnsubmit));
        this.submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(MemberActivity.this);
                builder.setMessage("출석체크 정보를 저장하시겠습니까?");
                builder.setCancelable(false);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int which) {
                         pd = ProgressDialog.show(MemberActivity.this, "", "출석정보를 저장하고 있습니다...", true);

                        attended = "";
                          for (int i=0;i<btnMember.size();i++ ) {

                                  if (((ToggleButton) btnMember.get(i)).isChecked()) {

                                     attended = attended  + ((ToggleButton) btnMember.get(i)).getTag().toString()+"!";

                                  }
                           }
                        Log.d(TAG,"attended= "+urlSaveAttendList+attended+"&today=" +yyyymmdd);;
                          new MemberActivity.SaveAttendAsyncTask().execute(new String[]{urlSaveAttendList + attended +"&today=" +yyyymmdd});

                        paramAnonymousDialogInterface.dismiss();
                        }
                    });

                 builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                         paramAnonymous2DialogInterface.dismiss();
                     }
                 });

                builder.create().show();
            }
        });
        this.datepicker = (Button) findViewById(R.id.btndatepicker);
        this.datepicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
            //    MemberActivity.this.showDialog(0);
                new DatePickerDialog(MemberActivity.this,mDateSetListener, mYear, mMonth, mDay).show();
            }
        });
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DATE);

         updateDisplay();
        btnexcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ismember =false;
                for(int i = 0; i < btnMember.size() ; i++) {
                    if (!((ToggleButton) btnMember.get(i)).getContentDescription().toString().equals("X"))  {
                        ismember = true;
                    }
                }

                if(ismember) {
                   saveExcel();
                }else {
                    Toast.makeText(MemberActivity.this, "출석인원이 없습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            MemberActivity.this.mYear = paramAnonymousInt1;
            MemberActivity.this.mMonth = paramAnonymousInt2;
            MemberActivity.this.mDay = paramAnonymousInt3;
            MemberActivity.this.updateDisplay();
        }
    };

//    public void showDialog(int paramInt) {
//        switch (paramInt) {
//            default:
//                return null;
//        }
//    }



    private class MemberAdapter extends BaseAdapter {
       // ArrayList<ToggleButton> btnMember;

        public MemberAdapter() {  //ArrayList<ToggleButton> paramArrayList

            //this.btnMember = paramArrayList;
        }

        public int getCount() {
            if (btnMember == null) {
                return 0;
            }
            return btnMember.size();
        }

        public ToggleButton getItem(int paramInt) {
            return (ToggleButton) btnMember.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {

            ToggleButton toggleButton;
            if(paramView ==null){
                toggleButton =new ToggleButton(MemberActivity.this);
                toggleButton.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                toggleButton.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                toggleButton=((ToggleButton) btnMember.get(paramInt));


                if (!toggleButton.getContentDescription().toString().equals("X")) {
                       Log.d(TAG, "getView= " + toggleButton.getText().toString());
                     toggleButton.setChecked(true);
                    String roll = toggleButton.getHint().toString();

                    switch (roll) {
                        case "A":
                        case "B":
                            acMis += 1;
                            break;
                        case "C":
                            acCellO += 1;
                            break;
                        case "D":
                            acCell1 += 1;
                            break;
                        case "E":
                            acCellM += 1;
                            break;
                        case "F":
                            acNewbie += 1;
                            break;
                        case "G":
                            acNewbie += 1;
                            break;
                    }

                }

                SetTotalStatus();
            }else{
                toggleButton= (ToggleButton)paramView;
            }
            return toggleButton;// (View)btnMember.get(paramInt);
        }
    }

    private class MemberAsyncTask extends AsyncTask<String, Integer, Long> {
        Context context;

        public MemberAsyncTask(Context paramContext) {
            this.context = paramContext;
        }

        protected Long doInBackground(String... paramVarArgs) {

            MemberActivity.this.btnMember = null;
            MemberActivity.this.btnMember = new ArrayList<ToggleButton>();
            MemberActivity.this.getMemberList(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            Log.d(TAG,"onPostExecute");
            if ((MemberActivity.this.pd != null) && (MemberActivity.this.pd.isShowing())) {
                pd.dismiss();
            }


            if (memberList.getChildCount() > 0) {
                memberList.removeAllViewsInLayout();
            }

            MemberAdapter adapter= new MemberAdapter();
            memberList.setAdapter(adapter);
            //SetTotalStatus();


        }
    }

    private class SaveAttendAsyncTask extends AsyncTask<String, Integer, Long> {
        public SaveAttendAsyncTask() {
        }

        protected Long doInBackground(String... paramVarArgs) {
           saveMsg = sendAttendList(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            MemberActivity.this.pd.dismiss();
           AlertDialog.Builder builder = new AlertDialog.Builder(MemberActivity.this);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    paramAnonymousDialogInterface.dismiss();
                }
            });
            builder.setMessage(MemberActivity.this.saveMsg);
            builder.show();
        }
    }

    private void saveExcel() {
        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet(); // 새로운 시트 생성

        Row row = sheet.createRow(0); // 새로운 행 생성
        Cell cell;                    // 열

        cell = row.createCell(0); // 1번 셀 생성
        cell.setCellValue("이름"); // 1번 셀 값 입력

//        cell = row.createCell(1); // 2번 셀 생성
//        cell.setCellValue("나이"); // 2번 셀 값 입력

        ArrayList<String> paramArrayList  =new ArrayList<String>();
        for (int i = 0; i < btnMember.size(); i++) {
             if (!((ToggleButton) btnMember.get(i)).getContentDescription().toString().equals("X"))  {  //출석한 인원만
                  paramArrayList.add( ((ToggleButton) btnMember.get(i)).getText().toString())  ;
            }
        }

        for (int i = 0; i < paramArrayList.size(); i++) { // 데이터 엑셀에 입력

            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(paramArrayList.get(i));
            //                cell = row.createCell(1);  2열의 값
//                cell.setCellValue(mItems.get(i).getAge()); 2열의 값
        }

        File xlsFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/", MisName + ".xls");

        try {
            FileOutputStream os = new FileOutputStream(xlsFile);
            workbook.write(os); // 외부 저장소에 엑셀 파일 생성
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), xlsFile.getAbsolutePath() + "에 저장되었습니다", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "xlsFile " + xlsFile.getAbsolutePath());

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                + "/Download/");
        intent.setDataAndType(uri, "application/*");
        startActivityForResult(intent,REQUEST_IMAGE_GET);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/*");
            startActivity(intent);
        }
    }
}
