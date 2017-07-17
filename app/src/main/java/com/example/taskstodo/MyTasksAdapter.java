package com.example.taskstodo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
     * Created by Dhruv on 21-03-2017.
     */

    public class MyTasksAdapter extends RecyclerView.Adapter<MyTasksAdapter.MyViewHolder> {
        private ArrayList<Tasks> tasks ;
        Context ctx;


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, Toolbar.OnMenuItemClickListener {
            public TextView title, description, date, time;
            ArrayList<Tasks> tasks = new ArrayList<Tasks>();
            Context context;

            public MyViewHolder(View itemView, Context context, ArrayList<Tasks> tasks) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                title = (TextView) itemView.findViewById(R.id.addTaskTitle);
                description = (TextView) itemView.findViewById(R.id.addTaskDescription);
                date = (TextView) itemView.findViewById(R.id.addTaskDate);
                time = (TextView) itemView.findViewById(R.id.addTaskTime);
                this.tasks = tasks;
                this.context = context;
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Tasks task = tasks.get(position);
                Intent intent = new Intent(this.context, DisplayListItem.class);
                intent.putExtra("Id", task.getId());
                this.context.startActivity(intent);
            }


            @Override
            public boolean onLongClick(View v) {
                final int position = getAdapterPosition();
                AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
                final CharSequence[] items = {"View Task", "Edit Task", "Delete Task"};
                dialog.setTitle("").setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 2) {
                            // delete task work
                            AlertDialog.Builder dialog2 = new AlertDialog.Builder(context);
                            dialog2.setMessage("Delete this task?");
                            dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ToDoHelper toDoHelper = new ToDoHelper(context);
                                    toDoHelper.deleteEntry(tasks.get(position).getId());
                                    tasks.remove(position);
                                    notifyDataSetChanged();

                                }
                            });
                            dialog2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog2.create().show();
                        }
                        if (which == 0) {
                            Intent intent = new Intent(context,DisplayListItem.class);
                            intent.putExtra("Id",tasks.get(position).getId());
                            context.startActivity(intent);
                        }
                        if (which == 1) {
                            Intent intent = new Intent(context,EditListItem.class);
                            intent.putExtra("Id",tasks.get(position).getId());
                            context.startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.create().show();
                return true;
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //PopupMenu popup = new PopupMenu(context, mImageButton);
                //MenuInflater inflater = popup.getMenuInflater();
                //inflater.inflate(R.menu.popup_menu,menu);
                return true;
            }
        }
        public MyTasksAdapter(ArrayList<Tasks> tasks, Context ctx){
            this.ctx = ctx;
            this.tasks = tasks;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

            return new MyViewHolder(itemView,ctx,tasks);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Tasks task = tasks.get(position);
            holder.title.setText(task.getTitle());
            holder.description.setText(task.getDescription());
            holder.date.setText(task.getDate());
            holder.time.setText(task.getTime());
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

    }