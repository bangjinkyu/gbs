package com.android.sungrackgbs;

import android.app.Application;

public class CommonValue extends Application
{
    private static CommonValue instance = null;
    private String _auth;
    private String _misidx;
    private String _misnm;
    private String _useyn;

    public CommonValue() {
        instance = this;
    }

    public static CommonValue getInstance() {
        if (null == instance) {
            instance = new CommonValue();
        }
        return instance;
    }

    public String getAuth()
    {
        return this._auth;
    }

    public String getMisNm()
    {
        return this._misnm;
    }

    public String getMisidx()
    {
        return this._misidx;
    }

    public String getYseyn()
    {
        return this._useyn;
    }

    public void setAuth(String paramString)
    {
        this._auth = paramString;
    }

    public void setMisNm(String paramString)
    {
        this._misnm = paramString;
    }

    public void setMisidx(String paramString)
    {
        this._misidx = paramString;
    }

    public void setYseyn(String paramString)
    {
        this._useyn = paramString;
    }
}
