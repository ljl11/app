package com.example.newenergyvehicle.homePage;

import android.app.ActivityGroup;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: EDENYIN
 * Date: 12/1/10
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractActivityGroup extends ActivityGroup {

    public List<View> viewHistory;

    public void replaceContentView(View view) {
        viewHistory.add(view);
        setContentView(view);
    }

    public void back() {
        if (!viewHistory.isEmpty()) {
            viewHistory.remove(viewHistory.get(viewHistory.size() - 1));
            if (viewHistory.isEmpty()) {
                finish();
                return;
            }
            setContentView(viewHistory.get(viewHistory.size() - 1));
        } else {
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.DONUT
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
