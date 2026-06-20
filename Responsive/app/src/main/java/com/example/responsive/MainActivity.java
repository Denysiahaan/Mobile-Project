package com.example.responsive;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ivExplore = findViewById(R.id.ivExplore);
        ImageView ivDestinations = findViewById(R.id.ivDestinations);

        // Load images using Picasso
        Picasso.get()
                .load("https://picsum.photos/800/600?image=1043")
                .into(ivExplore);

        Picasso.get()
                .load("https://picsum.photos/800/600?image=1047")
                .into(ivDestinations);
    }
}
