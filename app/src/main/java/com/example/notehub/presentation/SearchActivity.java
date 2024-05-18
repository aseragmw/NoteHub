package com.example.notehub.presentation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notehub.R;
import com.example.notehub.data.NotesDatabase;
import com.example.notehub.domain.NoteModel;
import com.example.notehub.domain.NotesRepository;
import com.example.notehub.presentation.adapters.NotesAdapter;
import com.example.notehub.presentation.viewmodels.NotesViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView notesRV;
    NotesAdapter adapter;
    NotesViewModel notesViewModel;
    List<NoteModel> notesList = new ArrayList<>();
    List<NoteModel> searchResult = new ArrayList<>();

    EditText searchEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Setup ViewModel
        setupViewModel();
        //Setup Toolbar
        setupAppBar();
        //Setup RecyclerView
        setupRV();

        //Setup Views
        setupViews();
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchResult.clear();
                String userInput = editable.toString();
                for (NoteModel note : notesList) {
                    if ( !userInput.isEmpty() && (note.getTitle().toLowerCase().contains(userInput.toLowerCase()) || note.getContent().toLowerCase().contains(userInput.toLowerCase()))) {
                        searchResult.add(note);
                    }
                }
                adapter.setData(searchResult);
                if (searchResult.isEmpty() && !userInput.isEmpty()) {
                    TextView noResults = findViewById(R.id.noSearchResultText);
                    noResults.setVisibility(View.VISIBLE);
                    ImageView noResultsImage = findViewById(R.id.noSearchResultImage);
                    noResultsImage.setVisibility(View.VISIBLE);
                }
                else if(!userInput.isEmpty() && !searchResult.isEmpty()){
                    TextView noResults = findViewById(R.id.noSearchResultText);
                    noResults.setVisibility(View.GONE);
                    ImageView noResultsImage = findViewById(R.id.noSearchResultImage);
                    noResultsImage.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    void setupViewModel(){
        NotesDatabase db = NotesDatabase.getInstance(this);
        notesViewModel =  NotesViewModel.getInstance(new NotesRepository(db.notesDao()));
    }
    void setupAppBar(){
        Toolbar toolbar = findViewById(R.id.searchAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.back_icon);
        getSupportActionBar().setHomeAsUpIndicator(backIcon);
    }

    void setupViews(){
        searchEditText = findViewById(R.id.et_searchScreen);
    }

    void setupRV(){
        notesViewModel.getAllNotes();
        notesRV = findViewById(R.id.rv_searchScreen);
        notesViewModel.allNotes.observe(this, new Observer<List<NoteModel>>() {

            @Override
            public void onChanged(List<NoteModel> noteModels) {
                notesList = noteModels;
                adapter.setData(searchResult);
            }
        });

        adapter = new NotesAdapter(notesList);
        adapter.setOnNoteClickedListener(note -> {
            Intent intent = new Intent(this, EditNoteActivity.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            intent.putExtra("id", note.getId());
            intent.putExtra("timestamp", note.getTimestamp());
            startActivity(intent);
        });
        notesRV.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesRV.setAdapter(adapter);
    }
}