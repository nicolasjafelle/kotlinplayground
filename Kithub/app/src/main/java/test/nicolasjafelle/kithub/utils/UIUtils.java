package test.nicolasjafelle.kithub.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by nicolas on 7/27/15.
 */
public class UIUtils {

    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }


}
