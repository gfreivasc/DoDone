package com.gabrielfv.dodone;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielfv.dodone.components.NewTaskDialogFragment;
import com.gabrielfv.dodone.models.Task;
import com.gabrielfv.dodone.utils.TaskController;
import com.gabrielfv.dodone.utils.TaskListAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DailyLogActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mTaskListView;
    private TextView mTaskListEmptyView;
    private FloatingActionButton mNewTaskFAB;
    private ConstraintLayout mDayPicker;
    private TextView mDay;
    private TextView mDateCompletion;
    private TextView mDayName;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log);

        mTaskListView = (ListView) findViewById(R.id.lv_tasks);
        mTaskListEmptyView = (TextView) findViewById(R.id.tv_tasks_empty);
        mNewTaskFAB = (FloatingActionButton) findViewById(R.id.fab_new_task);
        mDayPicker = (ConstraintLayout) findViewById(R.id.day_picker);
        mDay = (TextView) findViewById(R.id.task_list_day);
        mDateCompletion = (TextView) findViewById(R.id.task_list_completion);
        mDayName = (TextView) findViewById(R.id.task_list_day_name);

        mNewTaskFAB.setOnClickListener(this);
        mDayPicker.setOnClickListener(mDateOnClickListener);

        mCalendar = new GregorianCalendar();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);

        mTaskListView.setEmptyView(mTaskListEmptyView);
        updateDateTaskList();
    }

    private DatePickerDialog.OnDateSetListener mDatePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH, month);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateDateTaskList();
                }
            };

    private View.OnClickListener mDateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callDatePickerDialog();
        }
    };

    private void updateDateTaskList() {
        mDay.setText(String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH)));
        mDateCompletion.setText(new SimpleDateFormat("MMMM 'de' y", new Locale("pt", "BR"))
                .format(mCalendar.getTime()));
        mDayName.setText(new SimpleDateFormat("EEEE", new Locale("pt", "BR"))
                .format(mCalendar.getTime()));
        TaskController.getDefaultInstance().getDailyTasks(
                this, mTaskListView, mCalendar
        );
    }

    private void callDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(
                this, mDatePickerListener,
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    @Override
    public void onClick(View v) {
        BottomSheetDialogFragment newTaskDialogFragment = new NewTaskDialogFragment();
        newTaskDialogFragment.show(getSupportFragmentManager(), newTaskDialogFragment.getTag());
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }
}
