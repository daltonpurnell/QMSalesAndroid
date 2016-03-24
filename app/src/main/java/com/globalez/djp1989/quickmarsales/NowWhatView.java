package com.globalez.djp1989.quickmarsales;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class NowWhatView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_what_view);

        TouchImageView imageView = (TouchImageView)findViewById(R.id.imageView);
//        imageView.setImageResource(R.mipmap.brochure);
    }
}
