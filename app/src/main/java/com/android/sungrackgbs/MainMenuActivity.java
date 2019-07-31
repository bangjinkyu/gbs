package com.android.sungrackgbs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends Activity
{
    private final String TAG="MainMenuActivity";
    String UserGrade;
    Button btnAttendCheck;
    Button btnAttendRate;
    Button btnMemberMgr;
    Button btnSettings;
    Intent intentAddService;
    Intent intentAttendCheck;
    Intent intentAttendRate;
    Intent intentMemberMgr;
    String misidx;
    TextView txtMission;

    public void onBackPressed()
    {
        if (!this.UserGrade.equals("999"))
        {
            moveTaskToBack(true);
            Process.killProcess(Process.myPid());
            return;
        }
        super.onBackPressed();
    }

    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.mainmenuactivity);
        this.txtMission =(TextView)findViewById(R.id.txtMission);//문구
        this.btnMemberMgr = ((Button)findViewById(R.id.btnMemberMgr));//명단괸리
        this.btnAttendCheck = (Button)findViewById(R.id.btnAttendCheck);//출석체크
        this.btnAttendRate = (Button)findViewById(R.id.btnAttendRate);//출석률
        this.btnSettings = (Button)findViewById(R.id.btnSettings);//부가정보
        this.btnSettings.setVisibility(View.GONE);
        this.misidx = getIntent().getExtras().get("misidx").toString();
        this.UserGrade = ((CommonValue)getApplication()).getAuth();
        Log.d(TAG,"UserGrade"+UserGrade);
        txtMission.setText(((CommonValue)getApplication()).getMisNm());
        intentMemberMgr = new Intent(this, MemberMgrActivity.class);
        intentAttendCheck = new Intent(this, MemberActivity.class);
        intentAttendCheck.putExtra("misidx", this.misidx);
        intentMemberMgr.putExtra("misidx", this.misidx);
        //intentMemberMgr.putExtra("grade",this.UserGrade);
        this.intentAttendRate = new Intent(this, MemberAttendActivity.class);
        this.intentAttendRate.putExtra("misidx", this.misidx);
        this.intentAddService = new Intent(this, AddService.class);
        this.intentAddService.putExtra("misidx", this.misidx);

        this.btnMemberMgr.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                MainMenuActivity.this.startActivity(MainMenuActivity.this.intentMemberMgr);
            }
        });
        this.btnAttendCheck.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                MainMenuActivity.this.startActivity(MainMenuActivity.this.intentAttendCheck);
            }
        });
        this.btnAttendRate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                MainMenuActivity.this.startActivity(MainMenuActivity.this.intentAttendRate);
            }
        });
        this.btnSettings.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                MainMenuActivity.this.startActivity(MainMenuActivity.this.intentAddService);
            }
        });
    }
}