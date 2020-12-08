package com.example.test2.ways_to_grow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test2.R;
import com.example.test2.YouTubeConfig;

import com.firebase.ui.auth.AuthUI;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity2 extends YouTubeBaseActivity {
    //private SimpleExoPlayer player;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    public static final int RC_SIGN_IN=1;
    //private boolean playWhenReady = true;
    //private int currentWindow = 0;
    //private long playbackPosition = 0;
    //PlayerView playerView;
    ImageView imageView;
    YouTubeThumbnailLoader youTubeThumbnailLoader;
    YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener;
    YouTubeThumbnailView youTubeThumbnailView;
    private static final String TAG = MainActivity2.class.getSimpleName();

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth = FirebaseAuth.getInstance();

        imageView=(ImageView)findViewById(R.id.imageView);

        youTubePlayerView=(YouTubePlayerView) findViewById(R.id.youtubeplay);
        //youTubePlayerView.setVisibility(View.INVISIBLE);;

       /*youTubeThumbnailView.initialize(YouTubeConfig.getApikey(), new YouTubeThumbnailView.OnInitializedListener() {

           @Override
           public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

               youTubeThumbnailLoader.setVideo("hsbhN7i7H8E");
               youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                   @Override
                   public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                       youTubeThumbnailLoader.release();
                   }

                   @Override
                   public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                   }
               });
           }

           @Override
           public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

           }
       });*/

        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo("8A3s9WP_7l4");
               // youTubePlayerView.setVisibility(View.VISIBLE);
               // imageView.setVisibility(View.GONE);
                //youTubeThumbnailView.setVisibility(View.GONE);




            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(MainActivity2.this,"Failed to Load Video",Toast.LENGTH_SHORT).show();

            }
        };
        youTubePlayerView.initialize(YouTubeConfig.getApikey(),onInitializedListener);
        /*youTubePlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity2.this, YouTubePlayActivity.class);

                intent.putExtra("id","hsbhN7i7H8E");
                startActivity(intent);

            }
        });*/

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
    }

    /*private void initializePlayer() {

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        Uri uri = Uri.parse(getString(R.string.media_url_mp3));
        MediaSource mediaSource = buildMediaSource(uri);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }*/
    protected void onPause(){
        super.onPause();
        /*if (Util.SDK_INT < 24) {
            releasePlayer();
        }*/

        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    protected void onResume(){
        super.onResume();
        /*hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }*/

        firebaseAuth.addAuthStateListener(authStateListener);
    }
    @Override
    protected void onStart() {
        super.onStart();
        /*if (Util.SDK_INT >= 24) {
            initializePlayer();
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*if (Util.SDK_INT >= 24) {
            releasePlayer();
        }*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(MainActivity2.this,"Signed IN",Toast.LENGTH_SHORT).show();
            }else if (resultCode==RESULT_CANCELED){
                Toast.makeText(MainActivity2.this,"Sign In cancelled",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


   /* @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }*/

}