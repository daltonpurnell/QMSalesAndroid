package com.globalez.djp1989.quickmarsales;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class SampleProjectPlanView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_project_plan_view);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.mipmap.sample_project_plan);
    }
}
