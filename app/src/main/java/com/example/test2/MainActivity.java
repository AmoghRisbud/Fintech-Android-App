package com.example.test2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.test2.Fragments.AboutFragment;
import com.example.test2.Fragments.DashboardFragment;
import com.example.test2.Fragments.HomeFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    public static final int RC_SIGN_IN=1;
    PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;



    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistner);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork=manager.getActiveNetworkInfo();
        if(activenetwork!=null){
            if (user!=null) {
                String currentId = user.getUid();
                DocumentReference reference;
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                reference = firestore.collection("user").document(currentId);
                reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {
                            startActivity(new Intent(MainActivity.this, CreateProfile.class));
                            MainActivity.this.finish();
                        }
                    }
                });
            }
        }else{
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.appbar,menu);
        return true;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment =null;
            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment=new HomeFragment();
                    break;
                case R.id.nav_dash:
                    selectedFragment=new DashboardFragment();
                    break;
                case R.id.nav_about:
                    selectedFragment=new AboutFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;

        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.signout:
                AuthUI.getInstance().signOut(this);
                Toast.makeText(MainActivity.this,"Signed Out",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.profile:

                ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activenetwork=manager.getActiveNetworkInfo();
                if(activenetwork!=null){
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    String currentId=user.getUid();
                    DocumentReference reference;
                    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                    reference=firestore.collection("user").document(currentId);
                    reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()){
                                startActivity(new Intent(MainActivity.this,UserProfile.class));
                            }else{
                                startActivity(new Intent(MainActivity.this,CreateProfile.class));
                            }
                        }
                    });
                }

                break;

        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null) {
                    String currentId = user.getUid();
                    DocumentReference reference;
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    reference = firestore.collection("user").document(currentId);
                    reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.getResult().exists()) {
                                startActivity(new Intent(MainActivity.this, CreateProfile.class));
                            }
                        }
                    });
                }
                Toast.makeText(MainActivity.this,"Signed IN",Toast.LENGTH_SHORT).show();
            }else if (resultCode==RESULT_CANCELED){
                Toast.makeText(MainActivity.this,"Sign In cancelled",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    protected void onPause(){
        super.onPause();

        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    protected void onResume(){
        super.onResume();

        firebaseAuth.addAuthStateListener(authStateListener);
    }


}