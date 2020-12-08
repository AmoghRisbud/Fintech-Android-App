package com.example.test2.ways_to_grow;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.test2.R;
import com.example.test2.YouTubeConfig;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class NeedForInvest extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_for_invest);
        youTubePlayerView=(YouTubePlayerView) findViewById(R.id.needforinvest_video);
        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo("Y-Viw4vG6IA");
                //youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(NeedForInvest.this,"Failed to Load Video",Toast.LENGTH_SHORT).show();

            }
        };
        youTubePlayerView.initialize(YouTubeConfig.getApikey(),onInitializedListener);

        /*youTubePlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
}