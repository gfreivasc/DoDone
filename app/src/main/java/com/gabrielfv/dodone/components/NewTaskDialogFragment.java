package com.gabrielfv.dodone.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.gabrielfv.dodone.R;
import com.gabrielfv.dodone.utils.TaskController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by gabriel on 04/03/17.
 */

public class NewTaskDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {

    private TextInputLayout mDetailLayout;
    private TextInputEditText mDetailEditText;
    private TextInputLayout mDateLayout;
    private TextInputEditText mDateEditText;
    private AppCompatButton mConfirm;
    private Calendar mCalendar;
    private InputMethodManager mImm;
    private Realm mRealm;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.new_task, null);
        dialog.setContentView(contentView);

        mDetailLayout = (TextInputLayout) contentView.findViewById(R.id.new_task_detail_layout);
        mDetailEditText = (TextInputEditText) contentView.findViewById(R.id.new_task_detail);
        mDateLayout = (TextInputLayout) contentView.findViewById(R.id.new_task_date_layout);
        mDateEditText = (TextInputEditText) contentView.findViewById(R.id.new_task_date);
        mConfirm = (AppCompatButton) contentView.findViewById(R.id.new_task_confirm);

        mCalendar = new GregorianCalendar();
        mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        setDateInputText();
        mRealm = Realm.getDefaultInstance();

        mDateEditText.setOnClickListener(mDateOnClickListener);
        mDateEditText.setOnFocusChangeListener(mDateOnFocusChangeListener);
        mConfirm.setOnClickListener(this);
    }

    private DatePickerDialog.OnDateSetListener mDatePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH, monthOfYear);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    setDateInputText();
                    mConfirm.requestFocus();
                }
            };

    private void callDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(
                getActivity(), mDatePickerListener,
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private View.OnClickListener mDateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callDatePickerDialog();
        }
    };

    private View.OnFocusChangeListener mDateOnFocusChangeListener =
            new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mImm.hideSoftInputFromWindow(mDateEditText.getWindowToken(), 0);
                callDatePickerDialog();
            }
        }
    };

    private void setDateInputText() {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEEE, d 'de' MMMM 'de' y", new Locale("pt", "BR"));
        mDateEditText.setText(sdf.format(mCalendar.getTime()));
    }

    private boolean validateTaskDetail() {
        if (mDetailEditText.getText().toString().length() < 10) {
            mDetailLayout.setError(getString(R.string.new_task_detail_too_short));
            mDetailEditText.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (validateTaskDetail()) {
            TaskController taskController = TaskController.getDefaultInstance();
            taskController.createNewTask(
                    mDetailEditText.getText().toString(),
                    mCalendar, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast toast = Toast.makeText(
                            getActivity().getApplicationContext(),
                            getString(R.string.new_task_create_error),
                            Toast.LENGTH_SHORT);
                    toast.show();
                    Log.e("NewTask", "Error", error);
                }
            });

            dismiss();
        }
    }
}
