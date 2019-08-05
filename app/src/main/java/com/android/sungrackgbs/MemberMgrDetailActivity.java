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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
//명단관리 - 개인정보
public class MemberMgrDetailActivity extends Activity {
    private static final String TAG = "MemberMgrDetailActivity";
    private static final int DATE_DIALOG_BIRTH = 0;
    private static final int DATE_DIALOG_JOIN = 1;
    private String MODE = "I";
    private String _addr = "";
    private String _birth = "";
    private String _email = "";
    private String _helper = "";
    private String _isactive = "";
    private String _joindt = "";
    private String _lunar = "";
    private String _marriage = "";
    private String _memnm = "";
    private String _phone1 = "";
    private String _phone2 = "";
    private String _phone3 = "";
    private String _remark = "";
    private String _role = "";
    private String _sex = "";

    Button btnBirth; //달력 버튼
    Button btnCall;
    Button btnDel;
    Button btnEmail;
    Button btnJoinDt;
    Button btnSMS;
    Button btnSave;
    private DatePickerDialog.OnDateSetListener mBirthSetListener = new DatePickerDialog.OnDateSetListener() {  //생일
        public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            MemberMgrDetailActivity.this.mYear_B = paramAnonymousInt1;
            MemberMgrDetailActivity.this.mMonth_B = paramAnonymousInt2;
            MemberMgrDetailActivity.this.mDay_B = paramAnonymousInt3;
            MemberMgrDetailActivity.this.updateDisplay(0);
        }
    };
    private int mDay_B;
    private int mDay_J;
    private DatePickerDialog.OnDateSetListener mJoinSetListener = new DatePickerDialog.OnDateSetListener() { //인도일
        public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            MemberMgrDetailActivity.this.mYear_J = paramAnonymousInt1;
            MemberMgrDetailActivity.this.mMonth_J = paramAnonymousInt2;
            MemberMgrDetailActivity.this.mDay_J = paramAnonymousInt3;
            MemberMgrDetailActivity.this.updateDisplay(1);
        }
    };
    private int mMonth_B;
    private int mMonth_J;
    private int mYear_B;
    private int mYear_J;
    private String Grade;
    private String memidx;
    private String misidx;
    private ProgressDialog pd;
    String saveMsg = "";
    Spinner spnRole;
    ToggleButton swIsActive;  //출석부 활성 비활성
    ToggleButton swLunar;   //양력  음력
    ToggleButton swMarried;
    ToggleButton swSex;  //성별
    TableLayout tlTop;
    EditText txtAddr;
    EditText txtBirth;
    EditText txtEmail;
    EditText txtHelper;
    EditText txtJoinDt;
    EditText txtName;
    EditText txtPhone1;
    EditText txtPhone2;
    EditText txtPhone3;
    EditText txtRemark;
    String urlDeleteMember = "http://www.bwm.or.kr/attend/m_member_save_g.php?misidx=";
    String urlGetMember = "http://www.bwm.or.kr/attend/m_member_detail_g.php?misidx=";
    String urlSaveMember = "http://www.bwm.or.kr/attend/m_member_save_g.php?misidx=";

    private String Delete(String paramString) {

        String str = "";
            try {
                XmlPullParser localXmlPullParser;
                URL url = new URL(paramString);
                Log.d(TAG,"Delete= "+url);
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
                        case XmlPullParser.END_TAG :
                            break;
                        case XmlPullParser.TEXT:
                            str = localXmlPullParser.getText();
                            break;
                    }
                    eventType = localXmlPullParser.next();
                }

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return  e.getMessage();
            }

            return txtName.getText().toString()+str;
    }

    private void GetMember(String paramString) {

            try {
                XmlPullParser localXmlPullParser;
                URL url = new URL(paramString);
                Log.d(TAG,"GetMember= "+paramString);
                url.openConnection().setReadTimeout(100000);
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int eventType  = localXmlPullParser.getEventType();


                String mStname="";
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:

                            if (localXmlPullParser.getName().equals("memnm")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("birth")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("lunar")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("sex")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("joindt")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("phone1")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("phone2")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("phone3")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("email")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("addr")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("helper")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("role")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("remark")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("isactive")) {
                                mStname  = localXmlPullParser.getName();
                            } else if (localXmlPullParser.getName().equals("marriage")) {
                                mStname  = localXmlPullParser.getName();
                            }
                            break;
                        case XmlPullParser.END_TAG :
                            break;
                        case XmlPullParser.TEXT:
                            if (mStname.equals("memnm") && _memnm.equals("")) {
                                _memnm = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("birth") &&_birth.equals("")) {
                                _birth = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("lunar") && _lunar.equals("")) {
                                _lunar = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("sex") && _sex.equals("")) {
                                _sex = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("joindt") && _joindt.equals("")) {
                                _joindt = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("phone1") && _phone1.equals("")) {
                                _phone1 = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("phone2") && _phone2.equals("")) {
                                _phone2 = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("phone3") && _phone3.equals("")) {
                                _phone3 = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("email") && _email.equals("")) {
                                _email = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("addr") && _addr.equals("")) {
                               _addr = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("helper") && _helper.equals("")) {
                               _helper = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("role") && _role.equals("")) {
                                _role = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("remark") && _remark.equals("")) {
                                _remark = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("isactive") &&_isactive.equals("") ) {
                                _isactive = localXmlPullParser.getText().replace("\n    ", "");;
                            } else if (mStname.equals("marriage") && _marriage.equals("")) {
                                _marriage = localXmlPullParser.getText().replace("\n    ", "");;
                            }
                            Log.d(TAG,"_memnm"+_memnm);
                            Log.d(TAG,"_birth"+_birth);
                            Log.d(TAG,"_sex"+_sex);
                            Log.d(TAG,"_isactive"+_isactive);

                            break;
                    }
                    eventType= localXmlPullParser.next();
                }//end while
            } catch (Exception e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
    }

    private String Save(String paramString) {
        String mStr = "";
        String mRole ="";

            try {
                int spn = this.spnRole.getSelectedItemPosition();

                if (spn== 0) {
                    mRole = "A";
                }else if (spn== 1) {
                    mRole="B";
                }else if (spn == 2) {
                    mRole="C";
                }else if (spn == 3) {
                    mRole="D";
                }else if (spn == 4) {
                    mRole = "E";
                }else if (spn == 5) {
                    mRole ="F";
                }else{   //spn == 6
                    mRole ="G";
                }
                StringBuilder sb = new StringBuilder("&memnm=")
                         .append(URLEncoder.encode(txtName.getText().toString(), "UTF-8").toString())
                        .append("&birth=" + txtBirth.getText().toString().replace("-", ""))
                        .append("&lunar="+(swLunar.isChecked() ? "S" : "L" ))
                        .append("&sex="+(swSex.isChecked()?"F":"M"))
                        .append("&joindt=").append(txtJoinDt.getText().toString().replace("-", ""))
                        .append("&phone1="+txtPhone1.getText().toString())
                        .append("&phone2="+txtPhone2.getText().toString())
                        .append("&phone3="+txtPhone3.getText().toString())
                         .append("&email="+txtEmail.getText().toString())
                         .append("&addr=").append(URLEncoder.encode(txtAddr.getText().toString(), "UTF-8"))
                        .append("&helper=" + URLEncoder.encode(txtHelper.getText().toString(), "UTF-8"))
                        .append("&role="+mRole)
                         .append( "&remark=" + URLEncoder.encode(txtRemark.getText().toString(), "UTF-8"))
                        .append("&marriage="+(swMarried.isChecked() ? "Y":"N"))
                        .append("&isactive="+(swIsActive.isChecked()?"Y":"N"));
                URL url = new URL(paramString + (String) sb.toString() );
                Log.d(TAG,"Save= "+url);
                url.openConnection().setReadTimeout(100000);
                XmlPullParser localXmlPullParser;
                localXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                localXmlPullParser.setInput(url.openStream(), "UTF-8");
                int eventType = localXmlPullParser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (localXmlPullParser.getName().equals("memnm")) {
                               Log.d(TAG,"memnm= "+ localXmlPullParser.getName());
                            }
                            break;
                        case XmlPullParser.END_TAG :
                            break;
                        case XmlPullParser.TEXT:
                            mStr = localXmlPullParser.getText();
                            break;
                    }
                    eventType = localXmlPullParser.next();
                }
            } catch (Exception e) {

                return e.getMessage();
            }

      return mStr;
    }

    private void setButtonClickEvent() {
        btnBirth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                //MemberMgrDetailActivity.this.showDialog(DATE_DIALOG_BIRTH);
                new DatePickerDialog(MemberMgrDetailActivity.this, mBirthSetListener, mYear_B,mMonth_B, mDay_B).show();
            }
        });
        btnJoinDt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                //MemberMgrDetailActivity.this.showDialog(DATE_DIALOG_JOIN);
                new DatePickerDialog(MemberMgrDetailActivity.this, mJoinSetListener, mYear_J, mMonth_J, mDay_J).show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (txtName.getText().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MemberMgrDetailActivity.this);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            paramAnonymous2DialogInterface.dismiss();
                        }
                    });
                    builder.setMessage("이름을 입력해 주세요");
                    builder.show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MemberMgrDetailActivity.this);
                builder.setMessage("정보를 저장하시겠습니까?").setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                        MemberMgrDetailActivity.this.pd = ProgressDialog.show(MemberMgrDetailActivity.this, "", "정보를 저장하고 있습니다..", true);
                        new MemberMgrDetailActivity.MemberSaveAsyncTask().execute(new String[]{MemberMgrDetailActivity.this.urlSaveMember});
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                    }
                });
                builder.create().show();
            }
        });
       btnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                AlertDialog.Builder builder= new AlertDialog.Builder(MemberMgrDetailActivity.this);
                builder.setMessage("정보를 삭제하시겠습니까?").setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                        MemberMgrDetailActivity.this.pd = ProgressDialog.show(MemberMgrDetailActivity.this, "", "정보를 삭제하고 있습니다...", true);
                        new MemberDeleteAsyncTask().execute(new String[]{urlDeleteMember});
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                    }
                });
                builder.create().show();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                String fulphone = txtPhone1.getText().toString() + txtPhone2.getText().toString() + txtPhone3.getText().toString();
                if (fulphone.length() < 10) {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(MemberMgrDetailActivity.this);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            paramAnonymous2DialogInterface.dismiss();
                        }
                    });
                    builder.setMessage("전화번호를 입력해 주세요");
                    builder.show();
                    return;
                }
                Intent phoneintent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + fulphone));
                 phoneintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(phoneintent);
            }
        });
        this.btnSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                String fullphone = txtPhone1.getText().toString() + txtPhone2.getText().toString() + txtPhone3.getText().toString();
                if (fullphone.length() < 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MemberMgrDetailActivity.this);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            paramAnonymous2DialogInterface.dismiss();
                        }
                    });
                    builder.setMessage("전화번호를 입력해 주세요");
                    builder.show();
                    return;
                }
                Intent localIntent = new Intent("android.intent.action.SENDTO");
                localIntent.setData(Uri.parse("sms:" + fullphone));
                MemberMgrDetailActivity.this.startActivity(localIntent);
            }
        });
        this.btnEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (MemberMgrDetailActivity.this.txtEmail.getText().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MemberMgrDetailActivity.this);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                            paramAnonymous2DialogInterface.dismiss();
                        }
                    });
                    builder.setMessage("이메일 주소를 입력해 주세요 ");
                    builder.show();
                    return;
                }
                Intent emaolIntent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:" + txtEmail.getText().toString()));
                MemberMgrDetailActivity.this.startActivity(emaolIntent);
            }
        });
    }
//
    private void setTextBox() {
        this.txtPhone1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable paramAnonymousEditable) {
                if (paramAnonymousEditable.length() == 3) {
                    txtPhone2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            }

            public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            }
        });
        this.txtPhone2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable paramAnonymousEditable) {
                if (paramAnonymousEditable.length() == 4) {
                   txtPhone3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            }

            public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            }
        });
        this.txtPhone3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable paramAnonymousEditable) {
                if (paramAnonymousEditable.length() == 4) {
                    ((InputMethodManager) MemberMgrDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtPhone3.getWindowToken(), 0);
                }
            }

            public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            }

            public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            }
        });
    }

    private void updateDisplay(int paramInt) {
        String str1;
        String str2;
        switch (paramInt) {
            default:
                return;
            case DATE_DIALOG_BIRTH:
                if (mMonth_B + 1 < 10) {
                    str1 = "0" + (this.mMonth_B + 1);

                }else{
                    str1 = String.valueOf((this.mMonth_B + 1));
                }

                if (this.mDay_B < 10) {
                    str2 = "0" + this.mDay_B;
                }else{
                    str2 = String.valueOf(this.mDay_B);
                }


                txtBirth.setText(new StringBuilder().append(this.mYear_B).append("-").append(str1).append("-").append(str2));

                break;
            case DATE_DIALOG_JOIN:
                if (this.mMonth_J + 1 < 10) {
                    str1 = "0" + (this.mMonth_J + 1);
                }else {
                    str1 =  String.valueOf(this.mMonth_J + 1);
                }
                if (this.mDay_J < 10) {
                    str2 = "0" + this.mDay_J;
                }else {
                    str2 = String.valueOf(this.mDay_J);
                }

               txtJoinDt.setText(new StringBuilder().append(this.mYear_J).append("-").append(str1).append("-").append(str2));
                break;
        }
    }
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.membermgrdetailactivity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 항상 켜기
        this.txtName = ((EditText) findViewById(R.id.txtName));
        this.txtPhone1 = ((EditText) findViewById(R.id.txtPhone1));
        this.txtPhone2 = ((EditText) findViewById(R.id.txtPhone2));
        this.txtPhone3 = ((EditText) findViewById(R.id.txtPhone3));
        this.txtEmail = ((EditText) findViewById(R.id.txtEmail));
        this.txtAddr = ((EditText) findViewById(R.id.txtAddr));
        this.txtHelper = ((EditText) findViewById(R.id.txtHelper));
        this.txtRemark = ((EditText) findViewById(R.id.txtRemark));
        this.txtBirth = ((EditText) findViewById(R.id.txtBirth));
        txtBirth.addTextChangedListener(new CalendarWatcher(txtBirth));
        txtBirth.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        this.txtJoinDt = ((EditText) findViewById(R.id.txtJoinDt));
        txtJoinDt.addTextChangedListener(new CalendarWatcher(txtJoinDt));
        txtJoinDt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        this.btnBirth = ((Button) findViewById(R.id.btnBirth));
        this.btnJoinDt = ((Button) findViewById(R.id.btnJoinDt));
        this.btnSave = ((Button) findViewById(R.id.btnSave));
        this.btnDel = ((Button) findViewById(R.id.btnDel));
        this.swLunar = ((ToggleButton) findViewById(R.id.swLunar));
        this.swSex = ((ToggleButton) findViewById(R.id.swSex));
        this.swMarried = ((ToggleButton) findViewById(R.id.swMarried));
        this.swIsActive = ((ToggleButton) findViewById(R.id.swIsActive));
        this.tlTop = ((TableLayout) findViewById(R.id.tlTop));
        this.btnCall = ((Button) findViewById(R.id.btnCall));
        this.btnSMS = ((Button) findViewById(R.id.btnSMS));
        this.btnEmail = ((Button) findViewById(R.id.btnEmail));
        this.btnCall.setTextColor(getResources().getColor(R.color.call));
        this.btnSMS.setTextColor(getResources().getColor(R.color.sms));
        this.btnEmail.setTextColor(-16711681);
        this.Grade = ((CommonValue)getApplication()).getAuth();
        if(Grade.equals("100")){
            this.btnDel.setVisibility(View.GONE);
        }

        this.spnRole = ((Spinner) findViewById(R.id.spnRole));
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.roles,  android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spnRole.setAdapter(adapter);
        this.spnRole.setSelection(5);

            try {
                this.misidx = getIntent().getExtras().get("misidx").toString();
                this.memidx = getIntent().getExtras().get("memidx").toString();
                this.urlGetMember += this.misidx;
                this.urlSaveMember += this.misidx;
                this.urlDeleteMember += this.misidx;

                setButtonClickEvent();
                if (!this.memidx.equals("")) {
                    MODE = "U";
                }else {
                    MODE = "I";
                     this.btnDel.setVisibility(View.INVISIBLE);
                    this.btnCall.setVisibility(View.INVISIBLE);
                    this.btnSMS.setVisibility(View.INVISIBLE);
                    this.btnEmail.setVisibility(View.INVISIBLE);
                    this.txtBirth.setText("");
                    Calendar cal  = Calendar.getInstance();
                    this.mYear_B = cal.get(Calendar.YEAR);
                    this.mMonth_B =cal.get(Calendar.MONTH);;
                    this.mDay_B = cal.get(Calendar.DATE);

                    updateDisplay(DATE_DIALOG_BIRTH);
                    setTextBox();

                    this.txtJoinDt.setText("");

                    this.mYear_J = cal.get(Calendar.YEAR);
                    this.mMonth_J = cal.get(Calendar.MONTH);
                    this.mDay_J = cal.get(Calendar.DATE);
                    updateDisplay(DATE_DIALOG_JOIN);
                }

                this.urlSaveMember = (this.urlSaveMember + "&mode=" + this.MODE);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

            }


            this.urlGetMember = (this.urlGetMember + "&memidx=" + this.memidx);
            this.urlSaveMember = (this.urlSaveMember + "&memidx=" + this.memidx);
            this.urlDeleteMember = (this.urlDeleteMember + "&memidx=" + this.memidx + "&mode=D");

            MemberMgrDetailActivity.this.pd = ProgressDialog.show(this, "", "정보를 가져오고 있습니다....", true);
            new MemberDetailAsyncTask().execute(new String[]{this.urlGetMember});

    }

//    protected Dialog onCreateDialog(int paramInt) {
//        switch (paramInt) {
//            case 0:
//                return new DatePickerDialog(this, this.mBirthSetListener, this.mYear_B, this.mMonth_B, this.mDay_B);
//            case 1:
//                return new DatePickerDialog(this, this.mJoinSetListener, this.mYear_J, this.mMonth_J, this.mDay_J);
//            default:
//                return null;
//        }
//    }

    private class MemberDeleteAsyncTask extends AsyncTask<String, Integer, Long> {
        public MemberDeleteAsyncTask() {

        }

        protected Long doInBackground(String... paramVarArgs) {
            saveMsg = Delete(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            MemberMgrDetailActivity.this.pd.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(MemberMgrDetailActivity.this);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    paramAnonymousDialogInterface.dismiss();
                    MemberMgrDetailActivity.this.finish();
                }
            });
            builder.setMessage(saveMsg);
            builder.show();

        }
    }

    private class MemberDetailAsyncTask extends AsyncTask<String, Integer, Long> {
        public MemberDetailAsyncTask() {
        }

        protected Long doInBackground(String... paramVarArgs) {
            MemberMgrDetailActivity.this.GetMember(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            setTextBox();
            txtName.setText(_memnm);
            if (MemberMgrDetailActivity.this._birth.length() > 7) {
                txtBirth.setText(_birth.substring(0, 4) + "-" + _birth.substring(4, 6) + "-" +_birth.substring(6, 8));
                mYear_B = Integer.parseInt(_birth.substring(0, 4));
                mMonth_B = (Integer.parseInt(_birth.substring(4, 6)) - 1);
                mDay_B = Integer.parseInt(_birth.substring(6, 8));
            }


            if (_lunar.equals("S")) {  //양력
                swLunar.setChecked(true);
            }else{
                swLunar.setChecked(false);
            }

            if (_sex.equals("F")) {
                swSex.setChecked(true);
            }else{
                swSex.setChecked(false);
            }

            if (_joindt.length() > 7) {
                txtJoinDt.setText(_joindt.substring(0, 4) + "-" + _joindt.substring(4, 6) + "-" + _joindt.substring(6, 8));
                mYear_J = Integer.parseInt(_joindt.substring(0, 4));
                mMonth_J = (Integer.parseInt(_joindt.substring(4, 6)) - 1);
               mDay_J = Integer.parseInt(_joindt.substring(6, 8));
            }


            if (_phone1.length() > 0) {
                txtPhone1.setText(_phone1);
            }
            if (_phone2.length() > 0) {
               txtPhone2.setText(_phone2);
            }
            if (MemberMgrDetailActivity.this._phone3.length() > 0) {
                MemberMgrDetailActivity.this.txtPhone3.setText(_phone3);
            }
            if (MemberMgrDetailActivity.this._email.length() > 0) {
                MemberMgrDetailActivity.this.txtEmail.setText(_email);
            }
            if (MemberMgrDetailActivity.this._addr.length() > 0) {
                MemberMgrDetailActivity.this.txtAddr.setText(_addr);
            }
            if (MemberMgrDetailActivity.this._helper.length() > 0) {
                MemberMgrDetailActivity.this.txtHelper.setText(_helper);
            }
            if (MemberMgrDetailActivity.this._remark.length() > 0) {
                MemberMgrDetailActivity.this.txtRemark.setText(_remark);
            }

            if (MemberMgrDetailActivity.this._isactive.equals("Y")) {
                swIsActive.setChecked(true);
            }else{
                swIsActive.setChecked(false);
            }

            if (MemberMgrDetailActivity.this._marriage.equals("Y")) {
                swMarried.setChecked(true);
            }else {
                swMarried.setChecked(false);
            }


            if (_role.equals("A")) {
                MemberMgrDetailActivity.this.spnRole.setSelection(0);
            }else if (_role.equals("B")) {
                MemberMgrDetailActivity.this.spnRole.setSelection(1);
            } else if (_role.equals("C")) {
                MemberMgrDetailActivity.this.spnRole.setSelection(2);
            } else if (_role.equals("D")) {
                MemberMgrDetailActivity.this.spnRole.setSelection(3);
            } else if (_role.equals("E")) {
                MemberMgrDetailActivity.this.spnRole.setSelection(4);
            } else if (_role.equals("F")) {
                MemberMgrDetailActivity.this.spnRole.setSelection(5);
            } else { //_role = G
                MemberMgrDetailActivity.this.spnRole.setSelection(6);
            }
            MemberMgrDetailActivity.this.tlTop.requestFocus();
            MemberMgrDetailActivity.this.pd.dismiss();
        }
    }

    private class MemberSaveAsyncTask extends AsyncTask<String, Integer, Long> {
        public MemberSaveAsyncTask() {
        }

        protected Long doInBackground(String... paramVarArgs) {
           saveMsg = Save(paramVarArgs[0]);
            return null;
        }

        protected void onPostExecute(Long paramLong) {
            MemberMgrDetailActivity.this.pd.dismiss();
            AlertDialog.Builder builder  = new AlertDialog.Builder(MemberMgrDetailActivity.this);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    paramAnonymousDialogInterface.dismiss();
                    MemberMgrDetailActivity.this.finish();
                    Intent intent = new Intent(MemberMgrDetailActivity.this, MemberMgrActivity.class);
                    intent.putExtra("misidx",misidx);
                    startActivity(intent);

                }
            });
            builder.setMessage(saveMsg);
            builder.show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG,"onBackPressed()");
        Intent intent = new Intent(MemberMgrDetailActivity.this, MemberMgrActivity.class);
        intent.putExtra("misidx",misidx);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
    }
}