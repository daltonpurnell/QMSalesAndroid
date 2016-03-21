package com.globalez.djp1989.quickmarsales;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class TrainingOutlinesView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_outlines_view);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.mipmap.training_outlines);
    }
}
