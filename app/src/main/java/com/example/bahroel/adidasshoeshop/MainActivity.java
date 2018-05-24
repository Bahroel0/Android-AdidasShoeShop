package com.example.bahroel.adidasshoeshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {

    ViewFlipper viewFlipper;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int images[] = {R.drawable.flip1,R.drawable.flip2,R.drawable.flip3};
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        RelativeLayout.LayoutParams parameter =  (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
        parameter.setMargins(0, 0, 0, 30); // left, top, right, bottom
        scrollView.setLayoutParams(parameter);
        viewFlipper = (ViewFlipper)findViewById(R.id.viewflipper1);
        for (int image:images){
            flipperImage(image);
        }
    }
    private void flipperImage(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
}
