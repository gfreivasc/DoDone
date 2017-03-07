package com.gabrielfv.dodone.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gabriel on 22/02/17.
 */

public class Task extends RealmObject {

    public static final int TASK = 0;
    public static final int DONE_TASK = 1;
    public static final int EVENT = 2;
    public static final int MIGRATED_TASK = 3;
    public static final int SCHEDULED_TASK = 4;

    @IntDef({TASK, DONE_TASK, EVENT, MIGRATED_TASK, SCHEDULED_TASK})
    @Retention(RetentionPolicy.CLASS)
    public  @interface TaskStatus{};

    @PrimaryKey
    private long id;
    private String description;
    private @TaskStatus int status;
    private Date date;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @TaskStatus int getStatus() {
        return this.status;
    }

    public void setStatus(@TaskStatus int status) {
        this.status = status;
    }

    public Date getDate() { return  this.date; }

    public void setDate(Date date) { this.date = date; }

    public long getId() { return this.id; }

    public void setId(long id) { this.id = id; }
}
