package com.example.notehub.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import com.example.notehub.data.NotesDatabase;
import com.example.notehub.domain.NoteModel;
import com.example.notehub.domain.NotesRepository;
import com.example.notehub.presentation.viewmodels.NotesViewModel;

import java.time.LocalDateTime;

public class EditNoteActivity extends AppCompatActivity {
    NotesViewModel notesViewModel;
    TextView title,content;
    String noteTitle,noteContent;
    double noteTimestamp;
    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
         noteTitle = getIntent().getStringExtra("title");
         noteContent = getIntent().getStringExtra("content");
         noteTimestamp = getIntent().getDoubleExtra("timestamp",-1);
         noteId = getIntent().getIntExtra("id",-1);


        //Setup Toolbar
        setupAppBar();
        //Setup Views
        setupViews();
        //Setup ViewModel
        setupViewModel();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int btnSaveId = R.id.btn_save_editNote;
        if(item.getItemId()==android.R.id.home){
            if(!title.getText().toString().equals(noteTitle)||!content.getText().toString().equals(noteContent)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Note Update")
                        .setMessage("Note hasn't been saved . Are you sure you want to continue?")
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
        else if(item.getItemId()==btnSaveId){
            if(!title.getText().toString().isEmpty()){
                Log.d("hey",noteContent);
                if(noteTitle!=title.getText().toString() || noteContent!=content.getText().toString()){
                    for (NoteModel note : notesViewModel.allNotes.getValue()) {
                        if (note.getId() == noteId) {
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            long epochSeconds = currentDateTime.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
                            double daysSinceEpoch = (double) epochSeconds / (24 * 60 * 60); // 24 hours in a day, 60 minutes in an hour, 60 seconds in a minute
                            double fractionOfDay = (double) currentDateTime.toLocalTime().toSecondOfDay() / (24 * 60 * 60);
                            double dateTimeAsDouble = daysSinceEpoch + fractionOfDay;
                            note.setTitle(title.getText().toString());
                            note.setContent(content.getText().toString());
                            note.setTimestamp(dateTimeAsDouble);
                            notesViewModel.updateNote(note);
                            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                    }
                finish();

            }
            else{
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }

        }
        else if(item.getItemId()==R.id.btn_delete_editNote){
            for (NoteModel note : notesViewModel.allNotes.getValue()) {
                if (note.getId() == noteId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Note Deletion")
                            .setMessage("You are about to delete this note. Are you sure?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    notesViewModel.deleteNote(note);
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
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void setupViewModel(){
        NotesDatabase db = NotesDatabase.getInstance(this);
        notesViewModel =  NotesViewModel.getInstance(new NotesRepository(db.notesDao()));
    }
    void setupAppBar(){
        Toolbar toolbar = findViewById(R.id.editNoteAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.back_icon);
        getSupportActionBar().setHomeAsUpIndicator(backIcon);
    }

    void setupViews(){
        title = findViewById(R.id.et_title_editNote);
        content = findViewById(R.id.et_content_editNote);
        title.setText(noteTitle);
        content.setText(noteContent);
    }


}
