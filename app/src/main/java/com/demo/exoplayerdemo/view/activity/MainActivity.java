package com.demo.exoplayerdemo.view.activity;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.demo.exoplayerdemo.R;
import com.demo.exoplayerdemo.model.VideoInfo;
import com.demo.exoplayerdemo.view.adapter.VideoRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private ExoPlayerRecyclerView recyclerViewFeed;
    private final List<VideoInfo> videoInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
    }

    private void initRecyclerView(){
        recyclerViewFeed = findViewById(R.id.recyclerViewFeed);
        prepareVideoList();
        recyclerViewFeed.setVideoInfoList(videoInfoList);
        VideoRecyclerViewAdapter mAdapter = new VideoRecyclerViewAdapter(videoInfoList, this);
        recyclerViewFeed.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        recyclerViewFeed.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFeed.setAdapter(mAdapter);
    }

    private void prepareVideoList() {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setId(1);
        videoInfo.setTitle("Video 1");
        videoInfo.setUrl("https://player.vimeo.com/external/286837767.m3u8?s=42570e8c4a91b98cdec7e7bfdf0eccf54e700b69");

        VideoInfo videoInfo2 = new VideoInfo();
        videoInfo2.setId(2);
        videoInfo2.setTitle("Video 2");
        videoInfo2.setUrl("https://player.vimeo.com/external/286837810.m3u8?s=610b4fee49a71c2dbf22c01752372ff1c6459b9e");

        VideoInfo videoInfo3 = new VideoInfo();
        videoInfo3.setId(3);
        videoInfo3.setTitle("Video 3");
        videoInfo3.setUrl("https://player.vimeo.com/external/286837810.m3u8?s=610b4fee49a71c2dbf22c01752372ff1c6459b9e");

        VideoInfo videoInfo4 = new VideoInfo();
        videoInfo4.setId(4);
        videoInfo4.setTitle("Video 4");
        videoInfo4.setUrl("https://player.vimeo.com/external/286837810.m3u8?s=610b4fee49a71c2dbf22c01752372ff1c6459b9e");

        VideoInfo videoInfo5 = new VideoInfo();
        videoInfo5.setId(5);
        videoInfo5.setTitle("Video 5");
        videoInfo5.setUrl("https://player.vimeo.com/external/286837810.m3u8?s=610b4fee49a71c2dbf22c01752372ff1c6459b9e");

        videoInfoList.add(videoInfo);
        videoInfoList.add(videoInfo2);
        videoInfoList.add(videoInfo3);
        videoInfoList.add(videoInfo4);
        videoInfoList.add(videoInfo5);
    }

    public void playFirstItemVideo(){
        recyclerViewFeed.playVideo();
    }

    @Override
    protected void onPause() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                recyclerViewFeed.onPausePlayer();
            }
        });
        super.onPause();
    }

    @Override
    protected void onResume() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                recyclerViewFeed.onRestartPlayer(MainActivity.this);
            }
        });
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(recyclerViewFeed!=null)
            recyclerViewFeed.onRelease();
        super.onDestroy();
    }
}
