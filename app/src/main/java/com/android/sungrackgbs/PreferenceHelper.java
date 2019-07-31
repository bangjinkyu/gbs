package com.android.sungrackgbs;

import android.content.Context;
import android.database.Cursor;

public class PreferenceHelper
{
    Context context;
    DBHelper helper;

    public PreferenceHelper(Context paramContext)
    {
        this.context = paramContext;
    }

    public String getCode(String code)
    {
        String str = "";
        this.helper = new DBHelper(this.context, null, null, 0);
        Cursor localCursor = this.helper.getReadableDatabase().rawQuery("select code, value from code where code = '" + code + "'", null);
        for (code = str;; code = localCursor.getString(1)) {
            if (!localCursor.moveToNext()) {
                return code;
            }
        }
    }

    public void removeCode(String paramString)
    {
        this.helper.getWritableDatabase().execSQL("delete from code where code = '" + paramString + "';");
    }

    public void setCode(String paramString1, String paramString2)
    {
        this.helper.getWritableDatabase().execSQL("insert into code (code, value) values ('" + paramString1 + "', '" + paramString2 + "');");
    }
}