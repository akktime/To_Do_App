package com.example.to_do.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class Task_Id {

    @Exclude
    public  String  Task_Id;

    public <T extends Task_Id> T withId(@NonNull final String id){
        this.Task_Id=id;
        return  (T) this;
    }
}
