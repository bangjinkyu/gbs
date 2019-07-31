package com.android.sungrackgbs;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


/**
 * 생년월일 8자리 입력 지원
 */
public class CalendarWatcher implements TextWatcher {

    int cursor = 0;
    boolean result = false;

    EditText et;
    String beforeText;

    public CalendarWatcher(EditText et) {
        this.et = et;
    }

    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

        String keyword = s.toString();
        keyword.trim();
        int a = -1;
        int cpoint = et.getSelectionStart();
        a = keyword.indexOf("-");
        int keyCnt = keyword.length();

        if (keyCnt < 5 && a != -1) {
            keyword = keyword.replaceAll("-", "");
            setText(keyword, cpoint);
        }

//        if(keyword.length() == 4) {
//            setText(keyword);
//        }

        if (keyCnt >= 5 && keyCnt < 8 && a != 4) {
            keyword = keyword.replaceAll("-", "");
            String leftKeyword = keyword.substring(0, 4);
            String rightKeyword = keyword.substring(4, keyword.length());
            keyword = leftKeyword + "-" + rightKeyword;
            if (cpoint == 5) {
                cpoint++;
            }
            setText(keyword, cpoint);
        }

//        if(keyword.length() == 7) {
//            setText(keyword);
//        }

        int b = -1;
        b = keyword.indexOf("-", 6);
        if (keyCnt >= 8 && b != 7) {
            keyword = keyword.replaceAll("-", "");
            String leftKeyword = keyword.substring(0, 4);
            String middleKeyword = keyword.substring(4, 6);
            String rightKeyword = keyword.substring(6, keyword.length());
            keyword = leftKeyword + "-" + middleKeyword + "-" + rightKeyword;
            if (cpoint == 8) {
                cpoint++;
            }
            if (cpoint == 5 && beforeText.length() < keyword.length()) {
                cpoint++;
            }
            setText(keyword, cpoint);
        }

//        if(keyword.length() == 10) {
//            setText(keyword);
//        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub
        beforeText = s.toString();
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    private void setText(String keyword, int cpoint) {

//        String tempKeyword = "";
//        int keyCnt = keyword.length();
//
//        if(keyCnt == 4) {
//            int leftKeywordInt = Integer.parseInt(keyword.substring(0, 4));
//            if (leftKeywordInt < 1900) {
//                keyword = "1900";
//                et.setText(keyword);
//            }
//        }
//
//        else if(keyCnt == 7) {
//            tempKeyword = keyword.replaceAll("-", "");
//            String leftKeyword = tempKeyword.substring(0, 4);
//            String rightKeyword = tempKeyword.substring(4, 6);
//            int rightKeywordInt = Integer.parseInt(rightKeyword);
//            if (rightKeywordInt <= 0 || rightKeywordInt > 12) {
//                rightKeyword = "12";
//                keyword = leftKeyword + "-" + rightKeyword;
//                et.setText(keyword);
//            }
//        }
//
//        else if(keyCnt == 10){
//            tempKeyword = keyword.replaceAll("-", "");
//            String leftKeyword = tempKeyword.substring(0, 4);
//            String middleKeyword = tempKeyword.substring(4, 6);
//            int calInt = 0;
//              if(Config.DEBUG) {
//                  Log.d(TAG, "keyword.length() = " + keyword.length());
//              }
//            int rightKeywordInt = Integer.parseInt(tempKeyword.substring(6, 8));
//        if(Config.DEBUG) {
//            Log.d(TAG, "rightKeywordInt = " + rightKeywordInt);
//        }
////
//            Calendar cal = Calendar.getInstance();
//            cal.set(Calendar.YEAR, Integer.parseInt(leftKeyword));
//            cal.set(Calendar.MONTH, Integer.parseInt(middleKeyword)-1);
//            calInt = cal.getActualMaximum(Calendar.DATE);
////
//            if(rightKeywordInt <= 0 || rightKeywordInt > calInt){
////                String rightKeyword = String.valueOf(calInt);
////                keyword = leftKeyword + "-" + middleKeyword + "-" + rightKeyword;
////                et.setText(keyword);
//            }
//        }
//        else {
        et.setText(keyword);
//        }
        et.setSelection(cpoint);
    }
}
