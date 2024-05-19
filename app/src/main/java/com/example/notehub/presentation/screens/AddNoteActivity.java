package com.example.notehub.presentation.screens;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notehub.R;
import com.example.notehub.data.datasources.local_data_source.NotesDatabase;
import com.example.notehub.domain.NoteModel;
import com.example.notehub.domain.NotesRepository;
import com.example.notehub.presentation.listeners.OnNotesSyncCallBack;
import com.example.notehub.presentation.viewmodels.NotesViewModel;

import java.time.LocalDateTime;

public class AddNoteActivity extends AppCompatActivity {
    NotesViewModel notesViewModel;
    TextView title,content;
    boolean saved=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Setup AppBar
        setupAppBar();
        //Setup Views
        setupViews();
        //Setup ViewModel
        setupViewModel();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int btnSaveId = R.id.btn_save;
        if(item.getItemId()==android.R.id.home){
            if(!title.getText().toString().isEmpty()||!content.getText().toString().isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save Note")
                        .setMessage( "Note hasn't been saved . Are you sure you want to continue?")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.dismiss(); // Close the dialog
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss(); // Close the dialog
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                finish();
            }


        }
        else if(item.getItemId()==R.id.btn_save){
            if(!title.getText().toString().isEmpty()){
                LocalDateTime currentDateTime = LocalDateTime.now();
                long epochSeconds = currentDateTime.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
                double daysSinceEpoch = (double) epochSeconds / (24 * 60 * 60); // 24 hours in a day, 60 minutes in an hour, 60 seconds in a minute
                double fractionOfDay = (double) currentDateTime.toLocalTime().toSecondOfDay() / (24 * 60 * 60);
                double dateTimeAsDouble = daysSinceEpoch + fractionOfDay;
                NoteModel note = new NoteModel(title.getText().toString(),content.getText().toString(), dateTimeAsDouble);
                notesViewModel.insertNote(note, new OnNotesSyncCallBack() {
                    @Override
                    public void onSucess(String msg) {
                        Toast.makeText(AddNoteActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(String msg) {
                        Toast.makeText(AddNoteActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }
                });
                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    void setupViewModel(){
        NotesDatabase db = NotesDatabase.getInstance(this);
        notesViewModel =  NotesViewModel.getInstance(new NotesRepository(db.notesDao()));
    }
    void setupAppBar(){
        Toolbar toolbar = findViewById(R.id.addNoteAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.back_icon);
        getSupportActionBar().setHomeAsUpIndicator(backIcon);
    }

    void setupViews(){
        title = findViewById(R.id.et_title_addNote);
        content = findViewById(R.id.et_content_addNote);
    }

}