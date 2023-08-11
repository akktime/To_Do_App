package com.example.to_do;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddnewTask extends BottomSheetDialogFragment {

    public static final String TAG="Add New Task";

    private TextView setdueDate;
    private EditText mtaskEdit;
    private Button msaveButton;
    private FirebaseFirestore firestore;
    private Context context;
    private String duedate="";
    private  String id="";
    private String dueDateUpdate="";



    public static AddnewTask newinstance(){
        return new AddnewTask();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setdueDate=view.findViewById(R.id.set_due_tv);
        mtaskEdit=view.findViewById(R.id.task_edittext);
        msaveButton=view.findViewById(R.id.save_btn);


        firestore=FirebaseFirestore.getInstance();




        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            id = bundle.getString("id");
            dueDateUpdate  = bundle.getString("due");

            mtaskEdit.setText(task);
            setdueDate.setText(dueDateUpdate);

            if (task.length() > 0){
                msaveButton.setEnabled(false);
                msaveButton .setBackgroundColor(Color.GRAY);
            }
        }

        mtaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    msaveButton.setEnabled(false);
                    msaveButton.setBackgroundColor(Color.GRAY);
                }
                else{
                    msaveButton.setEnabled(true);
                    msaveButton.setBackgroundColor(getResources().getColor(R.color.green_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setdueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();

                int Month=calendar.get(Calendar.MONTH);
                int Year=calendar.get(Calendar.YEAR);
                int Day=calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog= new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month+=1;

                        setdueDate.setText(dayOfMonth + "/" + month + "/" + year);
                        duedate=dayOfMonth + "/" + month + "/" + year;
                    }
                },Year, Month, Day);

                datePickerDialog.show();
            }
        });

        boolean finalIsUpdate = isUpdate;
        boolean finalIsUpdate1 = isUpdate;
        msaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task=mtaskEdit.getText().toString();
                if(finalIsUpdate1) {
                    firestore.collection("task").document(id).update("task" , task , "due" , duedate);
                    Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (task.isEmpty()) {
                        Toast.makeText(context, "Empty task is not Allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, String> taskmap = new HashMap<>();
                        taskmap.put("task", task);
                        taskmap.put("due", duedate);
                        taskmap.put("status", String.valueOf(0));
                        taskmap.put("time", FieldValue.serverTimestamp().toString());

                        firestore.collection("task").add(taskmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Task Saved ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
                dismiss();
            }
        });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogboxListener){
            ((OnDialogboxListener)activity).onDialogClose(dialog);
        }
    }
}
