package com.general.imagebannerview;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ImageBannerView.OnClickIndexOfBanner {

    private int bvIds[] = new int[]{R.drawable.ic_logo, R.drawable.ic_siji};
    private ImageBannerFrameLayout bf;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.bf = (ImageBannerFrameLayout) findViewById(R.id.bf);
        bf.addBanner(bvIds);
        bf.setOnClickBannerView(this);
    }

    @Override
    public void clickIndexOfBanner(int index, View view) {
        Toast.makeText(this, index + "," + view.getId(), Toast.LENGTH_SHORT).show();
    }
}
