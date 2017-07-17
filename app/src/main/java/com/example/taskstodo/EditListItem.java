package com.example.taskstodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;


public class EditListItem extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    EditText title, description;
    Button date, time;
    CheckBox repeatCheckBox;
    FloatingActionButton editFAB;
    long id, repeatInterval;
    Spinner spinner;
    Calendar calendar, mcalendar;
    //date
    DatePickerDialog datePickerDialog;
    int year1, month, day;
    //time
    TimePickerDialog timePickerDialog;
    int hour, min;
    Boolean today = false;

    //Alarm
    int AlarmYear, AlarmMonth, AlarmDate, AlarmHour, AlarmMin, AlarmSec;
    long epoch; // time from 1980


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_list_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_task_toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        id = i.getLongExtra("Id", 0);
        ToDoHelper toDoHelper = new ToDoHelper(this);
        SQLiteDatabase db = toDoHelper.getReadableDatabase();
        Cursor c = db.query(ToDoHelper.TASKS_TO_DO_TABLE_NAME, null, ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_ID + "=" + id, null, null, null, null);
        c.moveToNext();
        String titleText = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_COLUMN_TITLE));
        String descriptionText = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_DESCRIPTION));
        String dateText = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_DATE));
        String timeText = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_TIME));

        Log.d("ye hai title ",titleText);
        Log.d("ye hai desc ",descriptionText);
        getSupportActionBar().setTitle(titleText);
        title = (EditText) findViewById(R.id.edit_title);
        description = (EditText) findViewById(R.id.edit_description);
        date = (Button) findViewById(R.id.edit_date);
        time = (Button) findViewById(R.id.edit_time);
        repeatCheckBox = (CheckBox) findViewById(R.id.edit_repeat_checkbox);
        editFAB = (FloatingActionButton) findViewById(R.id.edit_doneFAB);
        title.setText(titleText);
        description.setText(descriptionText);
        date.setText(dateText);
        time.setText(timeText);

        editFAB.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           String titleText = title.getText().toString();
                                           String descriptionText = description.getText().toString();
                                           String dateText = date.getText().toString();
                                           String timeText = time.getText().toString();
                                           setAlarm();
                                           ToDoHelper toDoHelper = new ToDoHelper(EditListItem.this);
                                           SQLiteDatabase db = toDoHelper.getWritableDatabase();
                                           ContentValues values = new ContentValues();
                                           values.put(ToDoHelper.TASKS_TO_DO_COLUMN_TITLE, titleText);
                                           values.put(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_DESCRIPTION, descriptionText);
                                           values.put(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_TIME, timeText);
                                           values.put(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_DATE, dateText);
                                           db.update(ToDoHelper.TASKS_TO_DO_TABLE_NAME, values, ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_ID + "=" + id, null);
                                           Intent i = new Intent(EditListItem.this, MainActivity.class);
                                           Toast.makeText(EditListItem.this, "Alarm Added", Toast.LENGTH_SHORT).show();
                                           startActivity(i);
                                       }
                                   }
        );

        // checkbox for repeat

        repeatCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatCheckBox.isChecked()) {
                    ArrayList<String> arraylist = new ArrayList<String>();
                    arraylist.add(0, "Every minute");
                    arraylist.add(1, "Every hour");
                    arraylist.add(2, "Every day");
                    arraylist.add(3, "Every week");

                    spinner = (Spinner) findViewById(R.id.spinnerEdit);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditListItem.this, android.R.layout.simple_spinner_item, arraylist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setVisibility(View.VISIBLE);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    repeatInterval = 60000;
                                    break;
                                case 1:
                                    repeatInterval = 3600000;
                                    break;
                                case 3:
                                    repeatInterval = 86400000;
                                    break;
                                case 4:
                                    repeatInterval = 604800000;
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    spinner = (Spinner) findViewById(R.id.spinnerEdit);
                    spinner.setVisibility(View.INVISIBLE);
                }
            }
        });

        //date
        calendar = Calendar.getInstance();
        year1 = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog = DatePickerDialog.newInstance(EditListItem.this, year1, month, day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#0072BA"));
                datePickerDialog.setTitle("Schedule Your Task!");
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });

        //time
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = timePickerDialog.newInstance(EditListItem.this, hour, min, true);
                timePickerDialog.setThemeDark(false);
                timePickerDialog.setAccentColor(Color.parseColor("#0072BA"));
                timePickerDialog.setTitle("Set Time For The Task");
                timePickerDialog.show(getFragmentManager(), "TImePickerDialog");
            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = (Button) findViewById(R.id.edit_date);
        if (year < year1 || (year == year1 && monthOfYear < month) || (year == year1 && monthOfYear == month && dayOfMonth < day)) {
            Toast.makeText(EditListItem.this, "Invalid date!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (year == year1 && monthOfYear == month && dayOfMonth == day) {
            today = true;
        }
        String dateText = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        Toast.makeText(EditListItem.this, dateText, Toast.LENGTH_SHORT).show();
        date.setText(dateText);

        AlarmYear = year;
        AlarmMonth = monthOfYear;
        AlarmDate = dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        time = (Button) findViewById(R.id.edit_time);
        if (today && hourOfDay < hour && minute < min) {
            Toast.makeText(EditListItem.this, "Invalid Time!", Toast.LENGTH_SHORT).show();
            return;
        }
        String timeText = "";
        if (hourOfDay > 10) {
            timeText += hourOfDay + ":";
        }
        if (hourOfDay < 10) {
            timeText += "0" + hourOfDay + ":";
        }
        if (minute < 10) {
            timeText += "0" + minute + "";
        }
        if (minute > 10) {
            timeText += minute + "";
        }
        Toast.makeText(EditListItem.this, timeText, Toast.LENGTH_SHORT).show();
        AlarmHour = hourOfDay;
        AlarmMin = minute;
        AlarmSec = second;
        mcalendar = Calendar.getInstance();
        mcalendar.set(AlarmYear, AlarmMonth, AlarmDate, AlarmHour, AlarmMin, AlarmSec);
        epoch = mcalendar.getTimeInMillis();
        Log.d("epoch value", epoch + "");
        time.setText(timeText);
    }

    private void setAlarm() {
        //Alarm
        AlarmManager am = (AlarmManager)
                this.getSystemService(Context.ALARM_SERVICE);
        title = (EditText) findViewById(R.id.edit_title);
        if(title.getText() == null){
            title.setText("");
        }
        if(description.getText() == null){
            description.setText("");
        }
        Intent j = new Intent(this, AlarmReceiver.class);
        j.putExtra("Title", title.getText().toString());
        j.putExtra("Description",description.getText().toString());
        j.putExtra("Ringtone",getResources().getResourceName(R.raw.ringtone));
        PendingIntent operation = PendingIntent.getBroadcast(this, 0, j, 0);
        if (repeatCheckBox.isChecked()) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, epoch, repeatInterval, operation);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, epoch, operation);
        }
    }
}
