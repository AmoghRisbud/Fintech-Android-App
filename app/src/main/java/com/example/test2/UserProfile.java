package com.example.test2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {

    TextView email,name,salary,phone,occupation;
    ImageView profileimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        salary=findViewById(R.id.et_Salary_up);
        email=findViewById(R.id.et_email_up);
        name=findViewById(R.id.et_name_up);
        phone=findViewById(R.id.et_phone_up);
        occupation=findViewById(R.id.et_Occupation_up);
        profileimg=findViewById(R.id.id_up);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentId=user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        reference=firestore.collection("user").document(currentId);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    String nameresult=task.getResult().getString("name");
                    String emailresult=task.getResult().getString("email");
                    String phoneresult=task.getResult().getString("phone");
                    String salaryresult=task.getResult().getString("salary");

                    String ouccupationresult=task.getResult().getString("occupation");
                    String url=task.getResult().getString("imageuri");

                    Picasso.get().load(url).into(profileimg);
                    name.setText(nameresult);
                    salary.setText(salaryresult);
                    phone.setText(phoneresult);
                    email.setText(emailresult);
                    occupation.setText(ouccupationresult);
                }else{
                    startActivity(new Intent(UserProfile.this,CreateProfile.class));
                }
            }
        });
    }
}