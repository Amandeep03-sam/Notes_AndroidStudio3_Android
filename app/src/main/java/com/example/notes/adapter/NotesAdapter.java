package com.example.notes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

<<<<<<< HEAD
    private final List<Note> notesList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView note;
        public final TextView dot;
        public final TextView timestamp;
        public final TextView desc;
        final ImageView imageView;

        public final LinearLayout text_type_layout;
=======
    private Context context;
    private List<Note> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView dot;
        public TextView timestamp;
        public TextView desc;
        ImageView imageView;

        public LinearLayout text_type_layout;
>>>>>>> f6f1d5bb6bc7d8a233865e3251b13c9565be6714

        public MyViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.note);
            desc = view.findViewById(R.id.desc);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
            text_type_layout = view.findViewById(R.id.text_type_layout);
            imageView = view.findViewById(R.id.imageView);
        }
    }


    public NotesAdapter(Context context, List<Note> notesList) {
<<<<<<< HEAD
=======
        this.context = context;
>>>>>>> f6f1d5bb6bc7d8a233865e3251b13c9565be6714
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = notesList.get(position);

        if (note.getType().contentEquals("text")) {

            holder.imageView.setVisibility(View.GONE);
             holder.note.setText(note.getTitle() + " (" + note.getCategory() + ")");
            holder.desc.setText(note.getDescription());




        }
        else {

            holder.imageView.setVisibility(View.VISIBLE);
            holder.note.setText(note.getTitle() + " (" + note.getCategory() + ")");
            holder.desc.setText(note.getDescription());
            Bitmap bitmapImage = BitmapFactory.decodeFile(note.getImage());
            holder.imageView.setImageBitmap(bitmapImage);

        }

        holder.dot.setText(Html.fromHtml("&#8226;"));
        holder.timestamp.setText(formatDate(note.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d , yyyy");
            return fmtOut.format(date);
        } catch (ParseException e) {
<<<<<<< HEAD
            e.printStackTrace();
=======

>>>>>>> f6f1d5bb6bc7d8a233865e3251b13c9565be6714
        }

        return "";
    }
}