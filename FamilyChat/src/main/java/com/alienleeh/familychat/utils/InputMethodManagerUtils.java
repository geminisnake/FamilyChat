package com.alienleeh.familychat.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by AlienLeeH on 2016/6/21.
 */
public class InputMethodManagerUtils {

    private static InputMethodManager inputMethodManager;


    public static void hideIfActive(Context context, View currentFocus) {
            initInputMethodManager(context);
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private static void initInputMethodManager(Context context) {
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
