package com.joe.zoomdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.joe.zoomdemo.model.FeedItem;
import com.joe.zoomdemo.utils.ZoomAnimationUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_FEED_ITEM = "extra_feed_item";
    private static final String EXTRA_ZOOM_INFO = "extra_zoom_info";
    private ImageView mImageView;
    private View mBackgroundView;
    private FeedItem mFeedItem;

    public static void startActivity(Activity from, View sharedView, FeedItem item){
        if (item == null) return;
        Intent intent = new Intent(from, DetailActivity.class);
        intent.putExtra(EXTRA_FEED_ITEM, item);
        intent.putExtra(EXTRA_ZOOM_INFO, ZoomAnimationUtils.getZoomInfo(sharedView));
        from.startActivity(intent);
        from.overridePendingTransition(0,0);
    }

    private ZoomAnimationUtils.ZoomInfo mZoomInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initArguments();
        initView();
    }

    private void initArguments() {
        mFeedItem = getIntent().getParcelableExtra(EXTRA_FEED_ITEM);
        mZoomInfo = getIntent().getParcelableExtra(EXTRA_ZOOM_INFO);
    }

    private void initView() {
        mBackgroundView = findViewById(android.R.id.content);
        mImageView = (ImageView) findViewById(R.id.iv_detail);
        Picasso.with(this)
                .load(mFeedItem.getImageUrl())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        setBitmap(bitmap);
                        tryEnterAnimation();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {}

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setBitmap(Bitmap bitmap) {
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (width*(mFeedItem.getHeight()*1f/mFeedItem.getWidth()));
        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
        params.width = width;
        params.height = height;
        mImageView.setLayoutParams(params);
        mImageView.setImageBitmap(bitmap);
    }

    private void tryEnterAnimation() {
        ZoomAnimationUtils.startZoomUpAnim(mZoomInfo, mImageView, null);
        ZoomAnimationUtils.startBackgroundAlphaAnim(mBackgroundView,
                new ColorDrawable(getResources().getColor(android.R.color.black)), 0, 255);
    }

    private void tryExitAnimation() {
        ZoomAnimationUtils.startBackgroundAlphaAnim(mBackgroundView,
                new ColorDrawable(getResources().getColor(android.R.color.black)), 255, 0);
        ZoomAnimationUtils.startZoomDownAnim(mZoomInfo, mImageView, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        tryExitAnimation();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
