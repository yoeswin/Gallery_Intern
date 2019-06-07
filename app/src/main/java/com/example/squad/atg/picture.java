package com.example.squad.atg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class picture extends AppCompatActivity {

    ImageView imageFull;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        textView = findViewById(R.id.title1);
        if (!getIntent().getStringExtra("title").isEmpty())
            textView.setText(getIntent().getStringExtra("title"));
        imageFull = findViewById(R.id.imageView);
        Glide.with(picture.this).load(getIntent().getStringExtra("url")).into(imageFull);
    }
}
