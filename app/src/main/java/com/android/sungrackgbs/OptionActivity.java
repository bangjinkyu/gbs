package com.android.sungrackgbs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
//설정관리
public class OptionActivity extends Activity
{
    private final static String TAG ="OptionActivity";
    Button btnSearch;
    TableLayout managerList;
    ProgressDialog pd;
    private ArrayList<RowItem> rowItems;
    ArrayList<RowItem> rowItemsSearch;
    ArrayList<TableRow> rowMember;
    ArrayList<TableRow> rowMemberSearch;
    String saveMsg = "";
    TableLayout searchList;
    TabHost tabHost;
    EditText txtKeyword;
    String urlAuthList = "http://www.bwm.or.kr/attend/m_auth_req_g.php";
    String urlMemberSearch = "http://www.bwm.or.kr/attend/m_member_search.php";
    String urlSaveAuth = "http://www.bwm.or.kr/attend/m_auth_g_update.php";
    String urlDeleteAuth ="http://www.bwm.or.kr/attend/m_auth_g_delete.php";

 //  String urlAuth = "http://www.bwm.or.kr/attend/m_auth.php?phone=";"
   // "http://www.bwm.or.kr/attend/m_member_save.php?misidx="   &mode=D
    class RowItem {
        String grade = "";
        String memnm = "";
        String misid = "";
        String misnm = "";
        String phone = "";
        String useyn = "";

        RowItem() {
        }

        public String getGrade() {
            return this.grade;
        }

        public String getMemnm() {
            return this.memnm;
        }

        public String getMisid() {
            return this.misid;
        }

        public String getMisnm() {
            return this.misnm;
        }

        public String getPhone() {
            return this.phone;
        }

        public String getUseyn() {
            return this.useyn.trim();
        }

        public void setGrade(String paramString) {
            this.grade = paramString;
        }

        public void setMemnm(String paramString) {
            this.memnm = paramString;
        }

        public void setMisid(String paramString) {
            this.misid = paramString;
        }

        public void setMisnm(String paramString) {
            this.misnm = paramString;
        }

        public void setPhone(String paramString) {
            this.phone = paramString;
        }

        public void setUseyn(String paramString) {
            this.useyn = paramString;
        }
    }

    private void getAuth(String paramString)
    {
            try
            {
                XmlPullParser localXmlPullParser;
                URL url= new URL(paramString);
                Log.d(TAG, "getAuth to URL : " + url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int parserEvent = localXmlPullParser.getEventType();

                String str ="";
                String mPhone = "";
                String mMisidx= "";
                String mMisnm = "";
                String mMemnm = "";
                String mGrade = "";
                String mUseyn ="";

                boolean isphone =false ,ismemnm  =false, isgrade = false, ismisidx = false, ismisnm = false , isuseyn = false;
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if(localXmlPullParser.getName().equals("phone")){
                                isphone  = true;
                                str="phone";
                            }else if (localXmlPullParser.getName().equals("misidx")){
                                ismisidx  = true;
                                str="misidx";
                            }else if(localXmlPullParser.getName().equals("misnm")) {
                                   ismisnm =true;
                                str="misnm";
                            }else if(localXmlPullParser.getName().equals("memnm")){
                                  ismemnm = true;
                                str="memnm";
                            }else if(localXmlPullParser.getName().equals("grade")){
                                   isgrade = true;
                                str="grade";
                            }else if (localXmlPullParser.getName().equals("useyn") ) {
                                   isuseyn =true;
                                str="useyn";
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:

                            if(isphone ) {
                                mPhone =localXmlPullParser.getText();
                                isphone=false;

                            }else if(ismisidx) {
                                mMisidx=localXmlPullParser.getText();
                                ismisidx=false;

                            }else if(ismisnm) {
                                mMisnm=localXmlPullParser.getText();
                                ismisnm=false;

                            }else if(ismemnm) {
                                mMemnm=localXmlPullParser.getText();

                                ismemnm=false;
                            }else if(isgrade){
                                mGrade=localXmlPullParser.getText();

                                isgrade=false;
                            }else if(isuseyn){
                                mUseyn=localXmlPullParser.getText();
                                isuseyn=false;
                                Log.d(TAG,"phone= "+mPhone);
                                Log.d(TAG,"misidx= "+mMisidx);
                                Log.d(TAG,"misnm= "+mMisnm);
//                                Log.d(TAG,"memnm= "+mMemnm);
//                                Log.d(TAG,"grade= "+mGrade);
//                                Log.d(TAG,"useyn= "+mUseyn);
                                RowItem rowItem = new RowItem();
                                rowItem.setPhone(mPhone);
                                rowItem.setMisid(mMisidx);
                                rowItem.setMisnm(mMisnm);
                                rowItem.setMemnm(mMemnm);
                                rowItem.setGrade(mGrade);
                                rowItem.setUseyn(mUseyn);

                                rowItems.add(rowItem);
                            }


                            break;
                    }//end switch
                    parserEvent = localXmlPullParser.next();
                }///end while

            }
            catch (Exception e)
            {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

        }


    private void getSearch(String paramString)
    {
        try
        {
            HttpGet httpGet = new HttpGet(paramString);
            httpGet.setHeader("Connection", "Keep-Alive");
            DefaultHttpClient  httpclient  = new DefaultHttpClient();

            HttpResponse response = httpclient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() != 200) {
                Toast.makeText(this, "결과수신실패", Toast.LENGTH_LONG).show();
            }

            String entity=  EntityUtils.toString(response.getEntity(), "euc-kr");

            Log.d(TAG,"entity"+entity);

            String [] mlocalObject= entity.split("§");


                        for(int i=0;i<mlocalObject.length;i++)
                        {

                            Log.d(TAG,"mlocalObject= "+mlocalObject[i]);
                            String [] mstr = mlocalObject[i].split("†");
                            Log.d(TAG,"mstr= "+mstr[0]);
                            RowItem localRowItem = new RowItem();
                            localRowItem.setPhone(mstr[2] + "-" + mstr[3] + "-" + mstr[4]);
                            localRowItem.setMisnm(mstr[0]);
                            localRowItem.setMemnm(mstr[1]);
                            this.rowItemsSearch.add(localRowItem);
                        }


        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String sendAuth()  //권한 수정용
    {
        return sendAuth();
    }

    private String sendAuth(String paramString)  //권한 수정용
    {
        String str = "";

            try
            {
                XmlPullParser localXmlPullParser;
                URL url  = new URL(paramString);
                Log.d(TAG,"sendAuth= "+url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int eventType  = localXmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
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
                    eventType = localXmlPullParser.next();
                }
            }
            catch (Exception e)
            {
                return e.getMessage();
            }

        return str;
    }

    private String DeleteAuth(String paramString)  //삭제
    {
        String str = "";

        try
        {
            XmlPullParser localXmlPullParser;
            URL url  = new URL(paramString);
            Log.d(TAG,"DeleteAuth= "+url);
            url.openConnection().setReadTimeout(100000);
            localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            localXmlPullParser.setInput(url.openStream(), "UTF-8");
            int eventType  = localXmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
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
                eventType = localXmlPullParser.next();
            }
        }
        catch (Exception e)
        {
            return e.getMessage();
        }

        return str;
    }

    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.optionactivity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 항상 켜기
        this.tabHost = ((TabHost)findViewById(R.id.tabhost));
        this.tabHost.setup();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("tab1");
        spec1.setContent(R.id.tab1);//R.id.tab1
        spec1.setIndicator("권한관리");
        this.tabHost.addTab(spec1);
        TabHost.TabSpec spec2 = this.tabHost.newTabSpec("tag2");
        spec2.setContent(R.id.tab2);//tab2
        spec2.setIndicator("검색");
        this.tabHost.addTab(spec2);
        TabHost.TabSpec spec3 = this.tabHost.newTabSpec("tag3");
        spec3.setContent(R.id.tab3);//tab3
        spec3.setIndicator("준비중");
        this.tabHost.addTab(spec3);
        this.tabHost.setCurrentTab(0);
        this.managerList = ((TableLayout)findViewById(R.id.managerList));
        this.searchList = ((TableLayout)findViewById(R.id.searchList));//
        this.txtKeyword = ((EditText)findViewById(R.id.txtKeyword));//
        this.btnSearch = ((Button)findViewById(R.id.btnSearch));//
        this.btnSearch.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
               OptionActivity.this.pd = ProgressDialog.show(OptionActivity.this, "", "검색중입니다....", true);

               if(!txtKeyword.getText().toString().equals("")) {
                   new OptionActivity.SearchAsyncTask(OptionActivity.this).execute(new String[]{OptionActivity.this.urlMemberSearch + "?keyword=" + txtKeyword.getText()});
               }else{
                   if ((OptionActivity.this.pd != null) && (OptionActivity.this.pd.isShowing())) {
                       OptionActivity.this.pd.dismiss();
                   }
                   Toast.makeText(OptionActivity.this, getResources().getString(R.string.message), Toast.LENGTH_LONG).show();
               }
            }
        });
       this.pd = ProgressDialog.show(this, "", "작업 준비중입니다....", true);
        new AuthAsyncTask(this).execute(new String[] { this.urlAuthList });
    }
//
    private class AuthAsyncTask extends AsyncTask<String, Integer, Long> {
    Context ctx;

    public AuthAsyncTask(Context paramContext) {
        this.ctx = paramContext;
    }

    protected Long doInBackground(String... paramVarArgs) {
        OptionActivity.this.rowMember = null;
        OptionActivity.this.rowMember = new ArrayList();
        OptionActivity.this.rowItems = null;
        OptionActivity.this.rowItems = new ArrayList<RowItem>();
        getAuth(paramVarArgs[0]);
        return null;
    }

    protected void onPostExecute(Long paramLong) {


        if (managerList.getChildCount() > 0) {
            managerList.removeAllViewsInLayout();
        }


        if ((OptionActivity.this.pd != null) && (OptionActivity.this.pd.isShowing())) {
                        OptionActivity.this.pd.dismiss();
        }
        for (int i = 0; i <rowItems.size(); i++) {

            TextView phonetextView = new TextView(this.ctx);
            TextView MisnmObject1 = new TextView(this.ctx);
            final EditText misinedit = new EditText(this.ctx);
            final EditText editText = new EditText(this.ctx);
            TextView  MemTextView = new TextView(this.ctx);

           final ToggleButton usebt = new ToggleButton(this.ctx);
            Button localButton = new Button(this.ctx);


            RowItem localRowItem = (RowItem) rowItems.get(i);

            phonetextView.setText(localRowItem.getPhone());
             phonetextView.setTextColor(getResources().getColor(R.color.blak));
            phonetextView.setWidth(180);
            phonetextView.setGravity(3);
            phonetextView.setTextSize(12);


            MisnmObject1.setText(localRowItem.getMisnm()); //목장이름
            MisnmObject1.setWidth(140);
            MisnmObject1.setGravity(3);
            MisnmObject1.setTextSize(12);

            misinedit.setText(localRowItem.getMisid());  //목장번호
            misinedit.setWidth(100);
            misinedit.setGravity(3);
            misinedit.setTextSize(12);

            MemTextView.setText(localRowItem.getMemnm());//이름
            MemTextView.setWidth(100);
            MemTextView.setGravity(3);
            MemTextView.setTextSize(11);

            editText.setText(localRowItem.getGrade());  //권한
            editText.setWidth(110);
            editText.setTextSize(11);
            editText.setGravity(3);

            usebt.setWidth(40);
            usebt.setHeight(40);
            usebt.setGravity( Gravity.CENTER);
            usebt.setBackgroundResource(R.drawable.togglebuttons);
            usebt.setTextColor(-1);
            usebt.setTextSize(12);
            usebt.setTextOn("사용");
            usebt.setTextOff("미사용");

            if (localRowItem.getUseyn().equals("Y")) {
                usebt.setChecked(true);
                usebt.setText("사용");
            }else{
                usebt.setChecked(false);
            }



            usebt.setHint(localRowItem.getMisid());
            usebt.setContentDescription(localRowItem.getPhone());
            usebt.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                                OptionActivity.this.pd = ProgressDialog.show(OptionActivity.this, "", "저장하고 있습니다....", true);

                                 String  str = ((Button)paramAnonymousView).getContentDescription().toString();
                          //      String str1 = ((Button)paramAnonymousView).getTag().toString();
                                String str2 = ((Button)paramAnonymousView).getHint().toString();
                               Log.d(TAG,"phone"+str);
                    Log.d(TAG,"str2"+str2);
                                    Log.d(TAG,"misinedit"+misinedit.getText().toString());
                                  Log.d(TAG,"useyn"+usebt.isChecked());
                                new SaveAuthAsyncTask().execute(new String[] {
                                        OptionActivity.this.urlSaveAuth + "?phone=" + str + "&useyn=" + (usebt.isChecked() ? "Y":"N") +"&misidx=" +misinedit.getText().toString() + "&grade="+editText.getText().toString()});
                }
           });
            localButton.setText("삭제");
            localButton.setTextSize(12);
            localButton.setTextColor(-1);
            localButton.setBackgroundResource(R.drawable. buttons);
            localButton.setWidth(40);
            localButton.setGravity( Gravity.CENTER);
            localButton.setHint(localRowItem.getMisid());
            localButton.setContentDescription(localRowItem.getPhone());
            localButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                    OptionActivity.this.pd = ProgressDialog.show(OptionActivity.this, "", "삭제하고 있습니다....", true);
                    String  str = ((Button)paramAnonymousView).getContentDescription().toString();  //phone
                //    String str2 = ((Button)paramAnonymousView).getHint().toString(); //목장번호
                    Log.d(TAG,"delete phone "+str);
                    new DeleteAuthAsyncTask().execute(new String[]{
                    OptionActivity.this.urlDeleteAuth + "?phone=" + str });

                 }
           });

            TableRow tableRow = new TableRow(this.ctx);
            ((TableRow) tableRow).setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ((TableRow) tableRow).setPadding(0, 5, 3, 3);
            ((TableRow) tableRow).addView(phonetextView);
            ((TableRow) tableRow).addView(MisnmObject1);
            ((TableRow) tableRow).addView(misinedit);
            ((TableRow) tableRow).addView(MemTextView);
            ((TableRow) tableRow).addView(editText);
            ((TableRow) tableRow).addView(usebt);
            ((TableRow) tableRow).addView(localButton);
           rowMember.add(tableRow);

           managerList.addView((View) tableRow);

        }
        }


    private class SaveAuthAsyncTask extends AsyncTask<String, Integer, Long>
    {
        public SaveAuthAsyncTask() {}

        protected Long doInBackground(String... paramVarArgs)
        {
           saveMsg = sendAuth(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong)
        {


            if ((OptionActivity.this.pd != null) && (OptionActivity.this.pd.isShowing())) {
                OptionActivity.this.pd.dismiss();
            }


            new AuthAsyncTask(OptionActivity.this).execute(new String[] { OptionActivity.this.urlAuthList });
        }
    }

    private class DeleteAuthAsyncTask extends AsyncTask<String, Integer, Long>
    {
        public DeleteAuthAsyncTask() {}

        protected Long doInBackground(String... paramVarArgs)
        {
            DeleteAuth(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong)
        {


            if ((OptionActivity.this.pd != null) && (OptionActivity.this.pd.isShowing())) {
                OptionActivity.this.pd.dismiss();
            }


            new AuthAsyncTask(OptionActivity.this).execute(new String[] { OptionActivity.this.urlAuthList });
        }
    }

}

    private class SearchAsyncTask extends AsyncTask<String, Integer, Long>
    {
        Context ctx;

        public SearchAsyncTask(Context paramContext)
        {
            this.ctx = paramContext;
        }

        protected Long doInBackground(String... paramVarArgs)
        {

            OptionActivity.this.rowMemberSearch = null;
            OptionActivity.this.rowMemberSearch = new ArrayList();
            OptionActivity.this.rowItemsSearch = null;
            OptionActivity.this.rowItemsSearch = new ArrayList();
            OptionActivity.this.getSearch(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong)
        {

            if (OptionActivity.this.searchList.getChildCount() > 0) {
                OptionActivity.this.searchList.removeAllViewsInLayout();
            }

            if ((OptionActivity.this.pd != null) && (OptionActivity.this.pd.isShowing())) {
                OptionActivity.this.pd.dismiss();
            }
            for ( int i = 0;i<OptionActivity.this.rowItemsSearch.size();i++)
            {

                    TextView textView = new TextView(this.ctx);  //목장
                    TextView localObject1 = new TextView(this.ctx); //이름
                    TextView localTextView = new TextView(this.ctx);
                    RowItem localObject2 = (OptionActivity.RowItem)OptionActivity.this.rowItemsSearch.get(i);
                    textView.setText(((OptionActivity.RowItem)localObject2).getMisnm());
                    textView.setWidth(200);
                    textView.setGravity(3);

                    ((TextView)localObject1).setText(((OptionActivity.RowItem)localObject2).getMemnm());
                    ((TextView)localObject1).setWidth(160);
                    ((TextView)localObject1).setGravity(3);

                    localTextView.setText(((OptionActivity.RowItem)localObject2).getPhone());
                    localTextView.setWidth(360);
                    localTextView.setGravity(3);


                    TableRow tableRow = new TableRow(this.ctx);
                    ((TableRow)tableRow).setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    ((TableRow)tableRow).setPadding(0, 10, 5, 5);
                    ((TableRow)tableRow).addView(textView);
                    ((TableRow)tableRow).addView((View)localObject1);
                    ((TableRow)tableRow).addView(localTextView);
                    OptionActivity.this.rowMemberSearch.add(tableRow);
                    OptionActivity.this.searchList.addView((View)tableRow);
            }
        }
    }
}
