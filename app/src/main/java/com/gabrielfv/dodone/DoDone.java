package com.gabrielfv.dodone;

import android.app.Application;

import com.gabrielfv.dodone.models.Task;

import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by gabriel on 21/02/17.
 */

public class DoDone extends Application {

    public static AtomicLong taskID;

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
        Realm realm = Realm.getDefaultInstance();
        try {
            taskID = new AtomicLong(realm.where(Task.class).max("id").longValue());
        }
        catch (NullPointerException e) {
            taskID = new AtomicLong(0);
        }
        realm.close();
    }
}
