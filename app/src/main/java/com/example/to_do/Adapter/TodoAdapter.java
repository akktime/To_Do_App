package com.example.to_do.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.AddnewTask;
import com.example.to_do.MainActivity;
import com.example.to_do.Model.TodoModel;
import com.example.to_do.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    private List<TodoModel> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public TodoAdapter(MainActivity mainActivity , List<TodoModel> todoList){
        this.todoList = todoList;
        activity = mainActivity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.eachtask, parent , false);
        firestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        TodoModel toDoModel = todoList.get(position);
        firestore.collection("task").document(toDoModel.Task_Id).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }

    public void editTask(int position){
        TodoModel toDoModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task" , toDoModel.getTask());
        bundle.putString("due" , toDoModel.getDue());
        bundle.putString("id" , toDoModel.Task_Id);

        AddnewTask addNewTask = new AddnewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager() , addNewTask.getTag());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TodoModel toDoModel = todoList.get(position);
        holder.mCheckbox.setText(toDoModel.getTask());

        holder.mDueDatetv.setText("Due On " +  toDoModel.getDue());

        holder.mCheckbox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("task").document(toDoModel.Task_Id).update("status" , 1);
                }else{
                    firestore.collection("task").document(toDoModel.Task_Id).update("status" , 0);
                }
            }
        });


    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDatetv;
        CheckBox mCheckbox;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDatetv = itemView.findViewById(R.id.due_date_tv);
            mCheckbox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
