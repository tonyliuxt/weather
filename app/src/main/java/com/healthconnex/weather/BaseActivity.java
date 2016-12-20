package com.healthconnex.weather;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Tony Liu on 2016/12/7.
 */

public class BaseActivity extends FragmentActivity {

    // hideKeyBoard
    protected void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
