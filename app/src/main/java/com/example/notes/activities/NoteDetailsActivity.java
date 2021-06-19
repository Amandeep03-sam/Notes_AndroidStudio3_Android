package com.example.notes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
<<<<<<< HEAD
import android.media.MediaPlayer;
=======
>>>>>>> f6f1d5bb6bc7d8a233865e3251b13c9565be6714
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notes.R;
import com.example.notes.model.Note;

public class NoteDetailsActivity extends AppCompatActivity {

    TextView title_tv,desc_tv,category_tv;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        imageView=findViewById(R.id.imageView);
        title_tv=findViewById(R.id.title_tv);
        desc_tv=findViewById(R.id.desc_tv);
        category_tv=findViewById(R.id.category_tv);
        Button openMap = findViewById(R.id.button);
<<<<<<< HEAD
        Button playAudio = findViewById(R.id.button2);
        Button pauseAudio = findViewById(R.id.button3);
        Intent intent= getIntent();
        Note note= (Note) intent.getSerializableExtra("data");
        if (note.getAudio() == null){
            playAudio.setVisibility(View.GONE);
            pauseAudio.setVisibility(View.GONE);
        }
        else{
            playAudio.setVisibility(View.VISIBLE);
        }
        final MediaPlayer[] mp = new MediaPlayer[1];
        playAudio.setOnClickListener(view -> {
            if (note.getAudio() != null){
                //set up MediaPlayer
                mp[0] = new MediaPlayer();

                try {
                    mp[0].setDataSource(note.getAudio());
                    mp[0].prepare();
                    mp[0].start();
                    pauseAudio.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

        pauseAudio.setOnClickListener(view -> mp[0].pause());


        openMap.setOnClickListener(view -> {

            if (note.getLatitude() != null && note.getLongitude() != null) {
                // do your stuff..
                Intent intent1 = new Intent(NoteDetailsActivity.this,MarkerActivity.class);
                intent1.putExtra("data",note);
                startActivity(intent1);
            }

=======
        Intent intent= getIntent();
        Note note= (Note) intent.getSerializableExtra("data");

        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (note.getLatitude() != null && note.getLongitude() != null) {
                    // do your stuff..
                    Intent intent= new Intent(NoteDetailsActivity.this,MarkerActivity.class);
                    intent.putExtra("data",note);
                    startActivity(intent);
                }

            }
>>>>>>> f6f1d5bb6bc7d8a233865e3251b13c9565be6714
        });

        if (note.getType().contentEquals("text")) {

             imageView.setVisibility(View.GONE);
           title_tv.setText(note.getTitle());
           category_tv.setText(note.getCategory());
           desc_tv.setText(note.getDescription());
        }
        else {

           imageView.setVisibility(View.VISIBLE);
            title_tv.setText(note.getTitle());
            category_tv.setText(note.getCategory());
            desc_tv.setText(note.getDescription());
            Bitmap bitmapImage = BitmapFactory.decodeFile(note.getImage());
             imageView.setImageBitmap(bitmapImage);

        }




    }
}