package com.example.notes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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