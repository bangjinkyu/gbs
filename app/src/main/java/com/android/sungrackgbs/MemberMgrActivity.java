package com.android.sungrackgbs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

 //명단관리 activity
public class MemberMgrActivity extends Activity {
     private final String TAG="MemberMgrActivity";
     private ArrayList<Button> btnMember;
     private Button btnadd;
     private Intent intent;
     private GridView memberList;
     private String misidx;
     private String Grade;
     private ProgressDialog pd;
     private String urlMemberList = "http://www.bwm.or.kr/attend/m_member_mgr_list_g.php?misidx=";
//
    private void getMemberList(String paramString) {

            try {
                XmlPullParser localXmlPullParser;
                URL url = new URL(paramString);
                Log.d(TAG, "URL getMemberList : " + url);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int eventType  = localXmlPullParser.getEventType();


                String mMemidx = "";
                String mMemnm = "";
                String mRole = "";
                String mAttend = "";
                String mPhone = "";
                boolean ismemidx = false;
                boolean ismemnm =false;
                boolean isrole = false;
                boolean isattend = false;
                boolean isPhone = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (localXmlPullParser.getName().equals("memidx")) {
                                ismemidx=true;
                            }else if(localXmlPullParser.getName().equals("memnm")){
                                ismemnm=true;
                            }else if(localXmlPullParser.getName().equals("isattend")){
                                isattend = true;
                             }else if(localXmlPullParser.getName().equals("role")){
                                isrole=true;
                            }else if (localXmlPullParser.getName().equals("phone")) {
                                isPhone=true;
                            }
                            break;
                        case XmlPullParser.END_TAG :
                            break;
                        case XmlPullParser.TEXT:
                            if (ismemidx) {

                                mMemidx= localXmlPullParser.getText();
                                ismemidx=false;
                            } else if (ismemnm) {
                                mMemnm = localXmlPullParser.getText();
                                ismemnm =false;
                            } else if(isrole){
                                mRole = localXmlPullParser.getText();
                                isrole =false;

                            } else if(isPhone) {
                                isPhone = false;
                                mPhone = localXmlPullParser.getText();

//                                Log.d(TAG," mMemidx" +mMemidx);
//                                Log.d(TAG," mMemnm" +mMemnm);
//                                Log.d(TAG," mRole" +mRole);
//                                Log.d(TAG," mPhone" +mPhone);

                                Button button = new Button(this);
                                button.setBackgroundResource(R.drawable.buttons);
                                button.setContentDescription(mPhone);
                                button.setTextSize(13.0F);
                                button.setTag(mMemidx);
                                button.setText(mMemnm);
                                if (mRole.equals("A")  ) {
                                    button.setTextColor(getResources().getColor(R.color.mis));
                                }else if(mRole.equals("B")){
                                    button.setTextColor(getResources().getColor(R.color.mis1));
                                }else  if (mRole.equals("C")) {
                                    button.setTextColor(getResources().getColor(R.color.cell0));

                                }else if (mRole.equals("D")) {
                                    button.setTextColor(getResources().getColor(R.color.cell1));

                                } else if (mRole.equals("E")) {
                                    button.setTextColor(-1);
                                }else if(mRole.equals("F")) {
                                    button.setTextColor(getResources().getColor(R.color.newbie));
                                } else{  // role = G
                                    button.setTextColor(getResources().getColor(R.color.red));
                                }

                                button.setOnLongClickListener(new View.OnLongClickListener() {
                                    public boolean onLongClick(View paramAnonymousView) {
                                        String mContent = ((Button) paramAnonymousView).getContentDescription().toString();
                                        Log.d(TAG,"mContent"+mContent);
                                        if (mContent.length() < 10) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MemberMgrActivity.this);
                                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                                                    paramAnonymous2DialogInterface.dismiss();
                                                }
                                            });
                                            builder.setMessage("입력된 전화번호가 없습니다....");
                                            builder.show();
                                        }else{

                                            Intent inten = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mContent));
                                            inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            MemberMgrActivity.this.startActivity(inten);
                                        }
                                        return false;
                                    }
                                });

                                button.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View paramAnonymousView) {   //이름 클릭
                                        String memnum = ((Button) paramAnonymousView).getTag().toString();
                                      intent.putExtra("memidx", paramAnonymousView.getTag().toString());
                                        intent.putExtra("grade", Grade);
                                      startActivity(MemberMgrActivity.this.intent);
                                      finish();
                                    }
                                });
                                this.btnMember.add(button);
                            }
                            break;
                    }//end switch
                    eventType = localXmlPullParser.next();
                }//end while

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
    }
//
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.membermgractivity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 항상 켜기
        this.misidx = getIntent().getExtras().get("misidx").toString();
        this.Grade = ((CommonValue)getApplication()).getAuth();
        Log.d(TAG,"misidx"+misidx);
        Log.d(TAG,"Grade"+Grade);
        memberList = (GridView) findViewById(R.id.memberList);
        intent = new Intent(this, MemberMgrDetailActivity.class);
        intent.putExtra("misidx", this.misidx);
        btnadd = ((Button) findViewById(R.id.btnAdd));
        btnadd.setVisibility(View.GONE);
         if(Grade.equals("999")) {   // 황재필 전도사님 요구 사항 :  슈퍼관리자가 아니면 추가 하지 못함
             btnadd.setVisibility(View.VISIBLE);
         }
        btnadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
             intent.putExtra("memidx", "");
              startActivity(MemberMgrActivity.this.intent);
              finish();
            }
        });
        this.pd = ProgressDialog.show(this, "", "선교회 명단을 만들고 있습니다...", true);
        new MemberAsyncTask(this).execute(new String[]{this.urlMemberList + this.misidx});
    }
//
    private class MemberAdapter extends BaseAdapter {
        ArrayList<Button> btnMember;

        public MemberAdapter(ArrayList<Button> paramArrayList) {

            this.btnMember = paramArrayList;
        }

        public int getCount() {
            if (this.btnMember == null) {
                return 0;
            }
            return this.btnMember.size();
        }

        public Button getItem(int paramInt) {
            return (Button) this.btnMember.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            return (View) this.btnMember.get(paramInt);
        }
    }
//
    private class MemberAsyncTask extends AsyncTask<String, Integer, Long> {
        Context context;

        public MemberAsyncTask(Context paramContext) {
            this.context = paramContext;
        }

        protected Long doInBackground(String... paramVarArgs) {
            MemberMgrActivity.this.btnMember = null;
            MemberMgrActivity.this.btnMember = new ArrayList();
            MemberMgrActivity.this.getMemberList(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            MemberAdapter adapter = new MemberMgrActivity.MemberAdapter( MemberMgrActivity.this.btnMember);
            MemberMgrActivity.this.memberList.setAdapter(adapter);
            MemberMgrActivity.this.pd.dismiss();
        }
    }

     @Override
     public void onBackPressed() {
         super.onBackPressed();
         finish();
     }

     @Override
     protected void onStop() {
         super.onStop();
         Log.d(TAG,"onStop()");
     }

     @Override
     protected void onDestroy() {
         super.onDestroy();
         Log.d(TAG,"onDestroy");
     }
 }