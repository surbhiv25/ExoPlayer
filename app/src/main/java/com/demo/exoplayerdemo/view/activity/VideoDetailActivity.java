package com.demo.exoplayerdemo.view.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.demo.exoplayerdemo.R;
import com.demo.exoplayerdemo.util.CacheDataSourceHandler;
import com.demo.exoplayerdemo.util.CommonFunc;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

public class VideoDetailActivity extends AppCompatActivity {

    private PlayerView video_view;
    private SimpleExoPlayer player;
    private String videoUrl = "";
    private long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean playWhenReady;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        videoUrl = getIntent().getStringExtra("URL");
        video_view = findViewById(R.id.playerView);
        progress_bar = findViewById(R.id.progress_bar);
    }

    private void initVideoPlayer(){
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        video_view.setPlayer(player);

        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

        player.seekTo(currentWindow, playbackPosition);
        if(playbackPosition != 0){
            player.setPlayWhenReady(true);
        }else{
            player.setPlayWhenReady(playWhenReady);
        }

        playerStateChanges();
    }

    private void playerStateChanges() {
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

            @Override
            public void onLoadingChanged(boolean isLoading) {}

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(CommonFunc.isNetworkConnected(VideoDetailActivity.this)){
                    if (playbackState == Player.STATE_BUFFERING){
                        progress_bar.setVisibility(View.VISIBLE);
                    } else {
                        progress_bar.setVisibility(View.INVISIBLE);
                    }
                }else{
                    progress_bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Please ensure internet connectivity",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Toast.makeText(getApplicationContext(),getString(R.string.internet_connection),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        //DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        //DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                //Util.getUserAgent(getApplicationContext(), getResources().getString(R.string.app_name)), defaultBandwidthMeter);
        Handler mainHandler = new Handler();
        CacheDataSourceHandler cacheDataSourceFactory = new CacheDataSourceHandler(this, 100 * 1024 * 1024, 5 * 1024 * 1024);
        return new HlsMediaSource(uri,cacheDataSourceFactory,mainHandler, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            playbackPosition = ExoPlayerRecyclerView.lastPlayedPosition;
            initVideoPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            playbackPosition = ExoPlayerRecyclerView.lastPlayedPosition;
            initVideoPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        video_view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
