package com.gabrielfv.dodone.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.gabrielfv.dodone.R;
import com.gabrielfv.dodone.models.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by gabriel on 04/03/17.
 */

public class TaskController {

    private static AtomicLong mTaskTop;
    private static TaskController defaultInstance = null;
    private Realm mRealm;

    protected TaskController() {
        mRealm = Realm.getDefaultInstance();
        try {
            mTaskTop = new AtomicLong(mRealm.where(Task.class).max("id").longValue());
        }
        catch (NullPointerException e) {
            mTaskTop = new AtomicLong(0);
        }
    }

    public long getTopPrimaryKey() {
        return mTaskTop.incrementAndGet();
    }

    public void createNewTask(final String taskDesc, final Calendar taskDate,
                              Realm.Transaction.OnError onError) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task task = realm.createObject(
                        Task.class,
                        getTopPrimaryKey());
                task.setDescription(taskDesc);
                task.setDate(taskDate.getTime());
                task.setStatus(Task.TASK);
            }
        }, onError);
    }

    public void getAllTasks(final Context context, final ListView handler) {
        RealmResults<Task> results = mRealm.where(Task.class).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<Task>>() {
            @Override
            public void onChange(RealmResults<Task> element) {
                final TaskListAdapter adapter = new TaskListAdapter(context, element);
                handler.setAdapter(adapter);
            }
        });
    }

    public void getDailyTasks(final Context context, final ListView handler, final Calendar day) {
        Calendar tomorrow = new GregorianCalendar(
                day.get(Calendar.YEAR),
                day.get(Calendar.MONTH),
                day.get(Calendar.DAY_OF_MONTH) + 1
        );
        RealmResults<Task> results = mRealm.where(Task.class)
                .greaterThanOrEqualTo("date", day.getTime())
                .lessThanOrEqualTo("date", tomorrow.getTime()).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<Task>>() {
            @Override
            public void onChange(RealmResults<Task> element) {
                final TaskListAdapter adapter = new TaskListAdapter(context, element);
                handler.setAdapter(adapter);
            }
        });
    }

    public static TaskController getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new TaskController();
        }

        return defaultInstance;
    }
}
