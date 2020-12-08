package com.example.test2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextInputLayout etname,etoccupation,etemail,etsalary,contact_no;
    Button button;
    ImageView imageView;
    ProgressBar progressBar;
    TextView dob;
    Uri imageuri;
    UploadTask uploadTask;
    String dateofBirth;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DocumentReference documentReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static final int PICK_IMAGE=1;
    All_UserMember member;
    String CurrentuserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        member=new All_UserMember();
        imageView=findViewById(R.id.id_cp);
        etemail=findViewById(R.id.et_email_cp);
        etname=findViewById(R.id.et_name_cp);
        etsalary=findViewById(R.id.et_Salary_cp);
        contact_no=findViewById(R.id.et_Contact_number_cp);

        dob=findViewById(R.id.DOB);
        button=findViewById(R.id.button_cp);
        etoccupation=findViewById(R.id.et_Occupation_cp);
        progressBar=findViewById(R.id.ProgressBar_cp);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CurrentuserId=user.getUid();

        documentReference=db.collection("user").document(CurrentuserId);
        storageReference= FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference=firebaseDatabase.getReference("All Users");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadData();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==PICK_IMAGE|| resultCode==RESULT_OK || data!=null ||data.getData() !=null){

                imageuri=data.getData();

                imageView.setImageURI(imageuri);
                //Picasso.get().load(imageuri).into(imageView);
            }
        }catch (Exception e){
            Toast.makeText(this,"Error:"+e,Toast.LENGTH_SHORT).show();
        }



    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }
    private void UploadData() {
        String name=etname.getEditText().getText().toString().trim();
        String occupation=etoccupation.getEditText().getText().toString().trim();
        String email=etemail.getEditText().getText().toString().trim();
        String salary=etsalary.getEditText().getText().toString().trim();
        String phone=contact_no.getEditText().getText().toString().trim();
        String dob=dateofBirth;

        if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(occupation) || !TextUtils.isEmpty(email) ||!TextUtils.isEmpty(salary) ||!TextUtils.isEmpty(phone)){
            progressBar.setVisibility(View.VISIBLE);
            if (imageuri!=null){
                final StorageReference reference=storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageuri));
                uploadTask=reference.putFile(imageuri);
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri=task.getResult();



                        }
                    }
                });
            }else{
                Map<String,Object> profile=new HashMap<>();
                profile.put("name",name);
                profile.put("imageuri","no image uploaded");
                profile.put("email",email);
                profile.put("salary",salary);
                profile.put("occupation",occupation);
                profile.put("phone",phone);
                profile.put("date of birth",dob);


                member.setName(name);
                member.setEmail(email);
                member.setOccupation(occupation);
                member.setSalary(salary);
                member.setDob(dob);
                member.setPhone(phone);
                member.setUrl("no image uploaded");
                databaseReference.child(CurrentuserId).setValue(member);
                documentReference.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(CreateProfile.this, "Profile Saved", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(CreateProfile.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }


                });
            }

        }else{
            Toast.makeText(this,"Plaease fill all fields",Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        DialogFragment datepicker=new DatePickerFragment();
        datepicker.show(getSupportFragmentManager(),"date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentdatestring= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        dateofBirth=currentdatestring;

        dob.setText(currentdatestring);

    }
}