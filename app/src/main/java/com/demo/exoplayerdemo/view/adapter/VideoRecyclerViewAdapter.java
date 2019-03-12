package com.demo.exoplayerdemo.view.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.exoplayerdemo.R;
import com.demo.exoplayerdemo.model.VideoInfo;
import com.demo.exoplayerdemo.view.activity.MainActivity;
import com.demo.exoplayerdemo.view.activity.VideoDetailActivity;

import java.util.List;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.MyViewHolder> {

    private String videoUrl = "";
    private final List<VideoInfo> mInfoList;
    private final Context mContext;

    public VideoRecyclerViewAdapter(List<VideoInfo> infoList, Context ctx) {
        mInfoList = infoList;
        mContext = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
        VideoInfo videoInfo = mInfoList.get(position);
        myViewHolder.textViewTitle.setTag(videoInfo.getUrl());
        myViewHolder.textViewTitle.setText(videoInfo.getTitle());
        myViewHolder.mCover.setBackgroundResource(R.drawable.video_loading);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(myViewHolder.textViewTitle.getTag().toString())){
                    videoUrl = myViewHolder.textViewTitle.getTag().toString();
                }
                Intent intent = new Intent(mContext, VideoDetailActivity.class);
                intent.putExtra("URL",videoUrl);
                mContext.startActivity(intent);
            }
        });
        ((MainActivity)mContext).playFirstItemVideo();
    }

    @Override
    public int getItemCount() {
        if (mInfoList != null && mInfoList.size() > 0) {
            return mInfoList.size();
        } else {
            return 1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView textViewTitle;
        final FrameLayout videoLayout;
        public final ImageView mCover;
        final View parent;

        MyViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
            parent.setTag(this);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            videoLayout = itemView.findViewById(R.id.video_layout);
            mCover = itemView.findViewById(R.id.cover);
        }
    }

}