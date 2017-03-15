package com.general.imagebannerview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by zhouwei on 2017/3/14.
 */

public class ImageBannerFrameLayout extends FrameLayout implements ImageBannerView.OnChangeBanner {
    private ImageBannerView bannerView;
    private LinearLayout linearLayout;

    public ImageBannerFrameLayout(@NonNull Context context) {
        super(context);
        initBannerView();
        initDotLinearLayout();
    }

    public ImageBannerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBannerView();
        initDotLinearLayout();
    }

    public ImageBannerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBannerView();
        initDotLinearLayout();
    }

    private void initBannerView() {
        bannerView = new ImageBannerView(getContext());
        bannerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        bannerView.setChangeBanner(this);
        addView(bannerView);
    }

    private void initDotLinearLayout() {

        linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        addView(linearLayout);

        FrameLayout.LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(layoutParams);

        linearLayout.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_red_light));
        linearLayout.setAlpha(0.3f);

    }

    public void addBanner(int[] bvIds) {
        for (int i = 0; i < bvIds.length; i++) {
            bannerView.addImageToBannerView(bvIds[i]);
            addDot();
        }
        if(bvIds.length > 0) {
            bannerView.addImageToBannerView(bvIds[0]);
            linearLayout.getChildAt(0).setBackgroundResource(R.drawable.dot_nomal);
        }
    }

    private void addDot() {
        ImageView iv = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        iv.setLayoutParams(lp);
        iv.setBackgroundResource(R.drawable.dot_nomal);
        iv.setAlpha(1f);
        linearLayout.addView(iv);
    }

    @Override
    public void changeBanner(int index) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            linearLayout.getChildAt(i).setBackgroundResource(R.drawable.dot_nomal);
        }
        linearLayout.getChildAt(index).setBackgroundResource(R.drawable.dot_select);
    }

    public void setOnClickBannerView(ImageBannerView.OnClickIndexOfBanner onClickIndexOfBanner){
        bannerView.setClickIndeOfBanner(onClickIndexOfBanner);
    }
}
