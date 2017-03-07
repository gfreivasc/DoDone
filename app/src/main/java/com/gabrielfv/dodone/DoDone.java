package com.gabrielfv.dodone;

import android.app.Application;

import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by gabriel on 21/02/17.
 */

public class DoDone extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initalize Calligraphy
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Slabo27px-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        // initialize Realm
        Realm.init(this);
    }
}
