package com.example.taskstodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Dhruv on 18-02-2017.
 */
public class DisplayListItem extends AppCompatActivity {
    TextView title, description, date, time;
    FloatingActionButton fab;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_list_item);

        Toolbar toolbar = (Toolbar)findViewById(R.id.display_toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.editTaskFAB);
        title = (TextView) findViewById(R.id.display_title);
        description = (TextView) findViewById(R.id.display_description);
        date = (TextView) findViewById(R.id.display_date);
        time = (TextView) findViewById(R.id.display_time);

        final Intent i = getIntent();
        id = i.getLongExtra("Id",0);
        ToDoHelper toDoHelper = new ToDoHelper(this);
        SQLiteDatabase db = toDoHelper.getReadableDatabase();
        Cursor c =  db.query(ToDoHelper.TASKS_TO_DO_TABLE_NAME,null,ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_ID+"="+id,null,null,null,null);
        c.moveToNext();
        String titleText = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_COLUMN_TITLE));
        String descriptionText = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_DESCRIPTION));
        String dateText = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_DATE));
        String timeText = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_TIME));

        title.setText(titleText);
        getSupportActionBar().setTitle(titleText);
        description.setText(descriptionText);
        date.setText(dateText);
        time.setText(timeText);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(DisplayListItem.this,EditListItem.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.deleteItem){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Delete this task?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ToDoHelper toDoHelper = new ToDoHelper(DisplayListItem.this);
                    toDoHelper.deleteEntry(id);
                    finish();
                }
            });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

}

