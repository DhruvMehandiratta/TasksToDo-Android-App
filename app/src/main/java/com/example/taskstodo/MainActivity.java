package com.example.taskstodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Empty view
    TextView emptyView;
    static ArrayList<Tasks> tasks;
    MyTasksAdapter adapter;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ToDoHelper toDoHelper;
    int noOfGridColumns = 1;

    //navigation bar variables
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //navigation work
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        //set toolbar as action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Tasks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialization();
    }
    @Override
    protected void onStart() {
        super.onStart();
        setUpViews();
    }

    public void initialization() {
        recyclerView = (RecyclerView) findViewById(R.id.tasksRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(noOfGridColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tasks = new ArrayList<Tasks>();
        toDoHelper = new ToDoHelper(this);
        adapter = new MyTasksAdapter(tasks, this);
        recyclerView.setAdapter(adapter);

        emptyView = (TextView) findViewById(R.id.emptyView);


//        if(toDoHelper.TASKS_TO_DO_COLUMN_TITLE.length() == 0){
//            recyclerView.setVisibility(View.GONE);
//            emptyView.setVisibility(View.VISIBLE);
//        }
        //File f = this.getDatabasePath(String.valueOf(toDoHelper));
        //long dbSize = f.length();
        //if(dbSize == 0){

        //}
        //TODO recyclerView.setEmptyView(findViewById(R.id.emptyElement));

        fab = (FloatingActionButton) findViewById(R.id.addTaskFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, AddTask.class);
                startActivityForResult(i, 1);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.specialdays){
            Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.c1 && noOfGridColumns != 1){
            noOfGridColumns = 1;
        }else if(id == R.id.c2 && noOfGridColumns != 2){
            noOfGridColumns = 2;
        }else if(id == R.id.c3 && noOfGridColumns != 3){
            noOfGridColumns = 3;
        }
        else{
            return true;
        }
        initialization();
        onStart();
        return true;
    }


    private void setUpViews() {
        SQLiteDatabase db =toDoHelper.getWritableDatabase();
      //  Cursor c = db.query(ToDoHelper.TASKS_TO_DO_TABLE_NAME,null,null,null,null,null,null);
        //while(c.moveToNext()){
          String query = "SELECT * FROM "+ToDoHelper.TASKS_TO_DO_TABLE_NAME + ";";
        Cursor c = db.rawQuery(query,null);
        tasks.clear();
        c.moveToFirst();
        while(!c.isAfterLast()){
            long id = c.getInt(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_ID));
            String title = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_COLUMN_TITLE));
            String description = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_DESCRIPTION));
            String date = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_DATE));
            String time = c.getString(c.getColumnIndex(ToDoHelper.TASKS_TO_DO_TABLE_COLUMN_TIME));
            Tasks t = new Tasks(title,description,date,time,id);
            tasks.add(t);
            c.moveToNext();
        }
        db.close();
        adapter.notifyDataSetChanged();
        if(adapter.getItemCount() == 0){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

    }
}