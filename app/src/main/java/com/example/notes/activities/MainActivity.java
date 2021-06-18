package com.example.notes.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.adapter.NotesAdapter;
import com.example.notes.database.DatabaseHelper;
import com.example.notes.model.Note;
import com.example.notes.utils.MyDividerItemDecoration;
import com.example.notes.utils.RecyclerTouchListener;

 import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends AppCompatActivity  {
     NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;

    private DatabaseHelper db;

    private static int SORT_VALUE=0;
    private final int IMAGE_REQ_CODE = 101;
    private final int GALLERY_IMAGE_REQ_CODE = 102;
    private final int CAMERA_IMAGE_REQ_CODE = 103;

    public static String NOTE_TYPE="text";
    public static String IMAGE_FILE_PATH=null;

    public ImageView imageView;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 2);


        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noNotesView = findViewById(R.id.empty_notes_view);

        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllNotes());

         FloatingActionButton fab =  findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NOTE_TYPE="text";

                if (checkPermissionREAD_EXTERNAL_STORAGE(MainActivity.this)) {
                    // do your stuff..

                    showNoteDialog(false, null, -1);
                }

            }
        });









        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                //goto details
                Intent intent= new Intent(MainActivity.this,NoteDetailsActivity.class);
                intent.putExtra("data",notesList.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                //if (notesList.get(position).getType().equals("text")) {

                    showActionsDialog(position);
               // }
            }
        }));



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }


    }

    /////////////////Location////////////////
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS == null) {
                locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Log.e("myLoc","Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                //showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /////////////


    ////Permission///
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(MainActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    ////



    private void createNote(String title,String desc,String category) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(title,desc,category,latitude,longitude);

        // get the newly inserted note from db
        Note n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }

    private void updateNote(String title,String desc,String category, int position) {
        Note n = notesList.get(position);
        // updating note text
        n.setTitle(title);
        n.setDescription(desc);
        n.setCategory(category);

        // updating note in db
        db.updateNote(n);

        // refreshing the list
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    private void deleteNote(int position) {
         db.deleteNote(notesList.get(position));

         notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }



    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    if (which == 0) {
                        showNoteDialog(true, notesList.get(position), position);
                    } else {
                        deleteNote(position);
                    }

            }
        });
        builder.show();
    }

    private void showActionsDialogForOtherType(final int position) {
        CharSequence colors[] = new CharSequence[]{"Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                    if (which == 0) {

                        deleteNote(position);
                    }

            }
        });
        builder.show();
    }

    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText title_et = view.findViewById(R.id.title_et);
        final EditText desc_et = view.findViewById(R.id.desc_et);
        final EditText category_et = view.findViewById(R.id.category_et);
         imageView = view.findViewById(R.id.imageView);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        final Button camera = view.findViewById(R.id.camera_btn);
        final Button audio = view.findViewById(R.id.audio_btn);

        //If Update Notes
        if (shouldUpdate)
        {
            camera.setVisibility(View.GONE);
            audio.setVisibility(View.GONE);
        }
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
                //new GligarPicker().requestCode(IMAGE_REQ_CODE).limit(1).withActivity(MainActivity.this).show();

            }
        });


        if (shouldUpdate && note != null) {
            title_et.setText(note.getTitle());
            desc_et.setText(note.getDescription());
            category_et.setText(note.getCategory());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(title_et.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter title!", Toast.LENGTH_SHORT).show();
                    return;
                }    if (TextUtils.isEmpty(desc_et.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter Description!", Toast.LENGTH_SHORT).show();
                    return;
                }    if (TextUtils.isEmpty(category_et.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter category!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    updateNote(title_et.getText().toString(),desc_et.getText().toString(),category_et.getText().toString(), position);
                } else {
                    // create new note
                    if (NOTE_TYPE.equals("text"))
                    {
                        createNote(title_et.getText().toString(),desc_et.getText().toString(),category_et.getText().toString());

                    }else
                    {

                        createImageNote(title_et.getText().toString(),desc_et.getText().toString(),category_et.getText().toString(),IMAGE_FILE_PATH);
                        IMAGE_FILE_PATH=null;
                    }
                }
            }
        });
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }


    ////////////////////////////////////////// FILTEr/////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchNotes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchNotes(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter_menu:
                sortNotes();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchNotes(String keyword) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        List<Note> contacts = databaseHelper.search(keyword);
        if (contacts != null) {
            mAdapter = new NotesAdapter(this, contacts);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
            recyclerView.setAdapter(mAdapter);
         }
    }

    public   void sortNotes()
    {
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View dialogView = factory.inflate(R.layout.custom_filter_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(dialogView);

        RadioGroup rg= dialogView.findViewById(R.id.sort_rg);
        RadioButton rb_one= dialogView.findViewById(R.id.title_rb);
        RadioButton rb_two= dialogView.findViewById(R.id.dateTime_rb);
        Button cancel= dialogView.findViewById(R.id.cancel_btn);




        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.title_rb) {

                    Collections.sort(notesList, new Comparator<Note>() {
                        @Override
                        public int compare(Note note, Note t1) {
                            return note.getTitle().compareTo(t1.getTitle());
                        }





                    });
                    mAdapter.notifyDataSetChanged();
                    SORT_VALUE=1;

                    dialog.dismiss();
                } else {

                    SORT_VALUE=0;
                    finish();
                    startActivity(getIntent());
                    dialog.dismiss();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               dialog.dismiss();
            }
        });

        dialog.show();
    }

    //////////////////////////////// GET Image Path ///////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
     /*   switch (requestCode){
            case IMAGE_REQ_CODE : {
                String pathsList[]= data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT); // return list of selected images paths.
                Log.e("CheckPath",pathsList[0]);
                NOTE_TYPE="image";
                IMAGE_FILE_PATH=pathsList[0];

                if (IMAGE_FILE_PATH!=null)
                {
                    imageView.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_FILE_PATH);
                    imageView.setImageBitmap(bitmap);
                }
               //createImageNote(pathsList[0]);
                //imagesCount.text = "Number of selected Images: " + pathsList.length;
                break;
            }
        }*/
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == IMAGE_REQ_CODE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout


                    IMAGE_FILE_PATH=getImageFilePath(selectedImageUri);
                    NOTE_TYPE="image";

                    if (IMAGE_FILE_PATH!=null)
                    {
                        imageView.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_FILE_PATH);
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }
    private void createImageNote(String title,String desc,String category,String image) {

        Bitmap bitmap = BitmapFactory.decodeFile(image);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bytesImage = byteArrayOutputStream.toByteArray();
        long id = db.insertImageNote(title,desc,category,image,latitude,longitude);
        Note n = db.getNote(id);

        if (n != null) {
             notesList.add(0, n);
            mAdapter.notifyDataSetChanged();
             toggleEmptyNotes();
        }
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), IMAGE_REQ_CODE);
    }


    public String getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            cursor.close();
            return imagePath;
        }
        return null;
    }
    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("sds", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}