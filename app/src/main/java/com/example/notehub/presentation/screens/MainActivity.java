package com.example.notehub.presentation.screens;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notehub.R;
import com.example.notehub.data.datasources.local_data_source.NotesDatabase;
import com.example.notehub.domain.NoteModel;
import com.example.notehub.domain.NotesRepository;
import com.example.notehub.presentation.adapters.NotesAdapter;
import com.example.notehub.presentation.listeners.OnNotesSyncCallBack;
import com.example.notehub.presentation.viewmodels.AuthViewModel;
import com.example.notehub.presentation.viewmodels.NotesViewModel;
import com.example.notehub.utils.CacheHelper;
import com.example.notehub.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView notesRV;
    NotesAdapter adapter;
    NotesViewModel notesViewModel;
    List<NoteModel> notesList = new ArrayList<>();
    CacheHelper cacheHelper;
    AuthViewModel authViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
         authViewModel = AuthViewModel.getInstance();
         cacheHelper = new CacheHelper(this);

        //Setup ViewModel
        setupViewModel();
        //Setup Toolbar
        setupToolbar();
        //Setup RecyclerView
        setupRV();
        //Setup FAB
        setupFAB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        MenuItem logoutItem = menu.findItem(R.id.logout_icon_app_bar);
        if(!cacheHelper.contains(Constants.USER_EMAIL_CACHE)){
            logoutItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_icon_app_bar) {
            startActivity(new Intent(this,SearchActivity.class));
            return true;
        }
        else if(id == R.id.logout_icon_app_bar){
            cacheHelper.clearKey(Constants.USER_EMAIL_CACHE);
            cacheHelper.clearKey(Constants.USER_ID_CACHE);
            cacheHelper.clearKey(Constants.USER_FULLNAME_CACHE);
            cacheHelper.clearKey("firstTime");
            authViewModel.logout();
            startActivity(new Intent(this,LoginOrContinueActivity.class));
            finish();

        }
        else if(id == R.id.sync_icon_app_bar){
            if(authViewModel.isLoggedIn()){
                Toast.makeText(this, "Syncing Notes", Toast.LENGTH_SHORT).show();
                notesViewModel.syncNotes(notesList, this, new OnNotesSyncCallBack() {
                    @Override
                    public void onSucess(String msg) {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(String msg) {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,LoginOrContinueActivity.class));
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    void setupRV(){
        notesViewModel.getAllNotes();
        notesRV = findViewById(R.id.rv_notesHomeScreen);
        notesViewModel.allNotes.observe(this, new Observer<List<NoteModel>>() {

            @Override
            public void onChanged(List<NoteModel> noteModels) {


                notesList = noteModels;
                adapter.setData(notesList);
                ImageView emptyNotes = findViewById(R.id.image_noNotesYet);
                TextView noNotes = findViewById(R.id.tv_noNotesYet);

                if(notesList.isEmpty()){
                    emptyNotes.setVisibility(View.VISIBLE);
                    noNotes.setVisibility(View.VISIBLE);
                }
                else{
                    emptyNotes.setVisibility(View.GONE);
                    noNotes.setVisibility(View.GONE);
                }

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
        notesRV.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL));
        notesRV.setAdapter(adapter);


    }

    void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.homeAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView textView = new TextView(this);
        textView.setText("NoteHub");
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextSize(30);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.START);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.START);
        textView.setLayoutParams(layoutParams);

        toolbar.addView(textView);

    }

    void setupFAB(){
        FloatingActionButton fab = findViewById(R.id.btn_addNoteHomeScreen);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(this, AddNoteActivity.class));
        });
    }

    void setupViewModel(){
        NotesDatabase db = NotesDatabase.getInstance(this);
        notesViewModel =  NotesViewModel.getInstance(new NotesRepository(db.notesDao()));
    }
}