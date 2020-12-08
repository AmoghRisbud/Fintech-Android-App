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

public class ShareMarket extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_market);
        youTubePlayerView=(YouTubePlayerView) findViewById(R.id.share_market_video);
        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo("L_iJCNzDe-s");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(ShareMarket.this,"Failed to Load Video",Toast.LENGTH_SHORT).show();

            }
        };
        youTubePlayerView.initialize(YouTubeConfig.getApikey(),onInitializedListener);

    }
}