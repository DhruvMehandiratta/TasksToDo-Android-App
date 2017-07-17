package com.example.taskstodo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    //date
    Calendar mcalendar;
    DatePickerDialog datePickerDialog;
    int year1, month, day;
    Button dialog_date_button;
    //time
    TimePickerDialog timePickerDialog;
    int hour,min;
    Button dialog_time_button;

    Boolean today = false;
    Boolean dateSet = false;
    CheckBox repeatCheckBox;

    // adding list item to adapter
    FloatingActionButton doneFAB;
    String date ;
    String time ;
    EditText title;
    EditText description;

    //Alarm
    int AlarmYear, AlarmMonth, AlarmDate, AlarmHour, AlarmMin, AlarmSec;
    long epoch; // time from 1980

    Intent i;
    Spinner spinner;
    long repeatInterval;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
          i = getIntent();

        Toolbar toolbar = (Toolbar)findViewById(R.id.add_task_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Tasks");

        //Done FAB
       dialog_date_button = (Button) findViewById(R.id.dialog_date_button);
        dialog_time_button = (Button) findViewById(R.id.dialog_time_button);
        title = (EditText) findViewById(R.id.addTaskTitle);
        description = (EditText) findViewById(R.id.addTaskDescription);
        doneFAB = (FloatingActionButton) findViewById(R.id.doneFAB);
        doneFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               date = dialog_date_button.getText().toString();
               time = dialog_time_button.getText().toString();

                if(date.equals("Date") || time.equals("Time")){
                    Toast.makeText(AddTask.this,"Please Select Date And Time!",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    setAlarm();
                    ToDoHelper toDoHelper = new ToDoHelper(AddTask.this);
                    toDoHelper.addEntry(title.getText().toString(),description.getText().toString(),date,time);
                    Toast.makeText(AddTask.this,"Reminder Added!",Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK,i);
                    finish();
                }
            }
        });



        // checkbox for repeat
        repeatCheckBox = (CheckBox) findViewById(R.id.repeatCheckBox);
        repeatCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatCheckBox.isChecked()) {
                    ArrayList<String> arraylist = new ArrayList<String>();
                    arraylist.add(0, "Every minute");
                    arraylist.add(1, "Every hour");
                    arraylist.add(2, "Every day");
                    arraylist.add(3, "Every week");

                    spinner = (Spinner) findViewById(R.id.spinnerAdd);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddTask.this, android.R.layout.simple_spinner_item, arraylist);
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
                }else{
                    spinner = (Spinner) findViewById(R.id.spinnerAdd);
                    spinner.setVisibility(View.INVISIBLE);
                }
            }
        });


        //date
        mcalendar = Calendar.getInstance();
        year1 = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);

        dialog_date_button = (Button) findViewById(R.id.dialog_date_button);
        dialog_date_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                datePickerDialog = DatePickerDialog.newInstance(AddTask.this,year1,month,day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#0072BA"));
                datePickerDialog.setTitle("Schedule Your Task!");
                datePickerDialog.show(getFragmentManager(),"DatePickerDialog");

            }
        });

        //time
        hour = mcalendar.get(Calendar.HOUR_OF_DAY);
        min = mcalendar.get(Calendar.MINUTE);
        dialog_time_button = (Button)findViewById(R.id.dialog_time_button);
        dialog_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dateSet){
                    Toast.makeText(AddTask.this,"Set Date First!",Toast.LENGTH_SHORT).show();
                    return;
                }
                timePickerDialog = timePickerDialog.newInstance(AddTask.this,hour,min,true);
                timePickerDialog.setThemeDark(false);
                timePickerDialog.setAccentColor(Color.parseColor("#0072BA"));
                timePickerDialog.setTitle("Set Time For The Task");
                timePickerDialog.show(getFragmentManager(),"TImePickerDialog");
            }
        });

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Button dialog_date_button = (Button)findViewById(R.id.dialog_date_button);
        if(year < year1 || (year == year1 && monthOfYear < month) ||(year == year1 && monthOfYear == month && dayOfMonth < day) ){
            Toast.makeText(AddTask.this,"Invalid date!",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(year == year1 && monthOfYear == month && dayOfMonth == day){
            today = true;
        }
        String date =  dayOfMonth + "-" +(monthOfYear+1) + "-" + year ;
        Toast.makeText(AddTask.this,date,Toast.LENGTH_SHORT).show();
        dialog_date_button.setText(date);

        AlarmYear = year;
        AlarmMonth = monthOfYear;
        AlarmDate = dayOfMonth;
        dateSet = true;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Button dialog_time_button = (Button) findViewById(R.id.dialog_time_button);
        if (today && hourOfDay < hour && minute < min) {
            Toast.makeText(AddTask.this, "Invalid Time!", Toast.LENGTH_SHORT).show();
            return;
        }
        String time = "";
            if(hourOfDay > 10){
                time += hourOfDay + ":" ;
            }
            if (hourOfDay < 10) {
                time += "0" + hourOfDay + ":";
            }
            if (minute < 10) {
                time += "0" + minute + "";
            }
            if(minute > 10){
                time += minute + "";
            }
        Toast.makeText(AddTask.this, time, Toast.LENGTH_SHORT).show();
        AlarmHour = hourOfDay;
        AlarmMin = minute;
        AlarmSec = second;
        mcalendar = Calendar.getInstance();
        mcalendar.set(AlarmYear, AlarmMonth, AlarmDate, AlarmHour, AlarmMin, AlarmSec);
        epoch = mcalendar.getTimeInMillis();
        Log.d("epoch value",epoch+"");
        dialog_time_button.setText(time);

    }

    private void setAlarm() {
        //Alarm
        AlarmManager am = (AlarmManager)
                this.getSystemService(Context.ALARM_SERVICE);
        if(title.getText() == null){
            title.setText("");
        }
        if(description.getText() == null){
            description.setText("");
        }
        Log.d("TitleText",title.getText().toString());
        Log.d("Descrtdgd",description.getText().toString());
        Intent j = new Intent(AddTask.this, AlarmReceiver.class);

        j.putExtra("Title",title.getText().toString());

        j.putExtra("Description", description.getText().toString());
        PendingIntent operation = PendingIntent.getBroadcast(this, 0, j, 0);
        if(!repeatCheckBox.isChecked()){
        am.set(AlarmManager.RTC_WAKEUP, epoch, operation);}
        else{
            am.setRepeating(AlarmManager.RTC_WAKEUP,epoch,repeatInterval,operation);
        }
    }
}
