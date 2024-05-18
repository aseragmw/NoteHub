package com.example.notehub.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notehub.R;
import com.example.notehub.domain.NoteModel;
import com.example.notehub.presentation.listeners.OnNoteClickedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<NoteModel> notesList;
    private OnNoteClickedListener onNoteClickedListener;
    public NotesAdapter(List<NoteModel> notes) {
        this.notesList=notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        List<Integer>colors = new ArrayList();
        colors.add(R.color.noteColor1);
        colors.add(R.color.noteColor2);
        colors.add(R.color.noteColor3);
        colors.add(R.color.noteColor4);
        colors.add(R.color.noteColor5);
        holder.noteTitle.setText(notesList.get(position).getTitle());
        int colorIndex = position % colors.size();
        holder.noteCard.setCardBackgroundColor(holder.itemView.getResources().getColor(colors.get(colorIndex),null));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void setData(List<NoteModel> notes){
        this.notesList = notes;
        notifyDataSetChanged();
    }

//    public int getRandomColor(){
//
//        Random random = new Random();
//        int randomColor = random.nextInt(colors.size());
//        return colors.get(randomColor);
//    }

    public void setOnNoteClickedListener(OnNoteClickedListener listener){
        this.onNoteClickedListener = listener;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle;
        CardView noteCard;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteCard = itemView.findViewById(R.id.noteCard);
            noteTitle = itemView.findViewById(R.id.tv_noteTitleHomeScreen);
            itemView.setOnClickListener(i->{
                int position =getBindingAdapterPosition();
                if(position != RecyclerView.NO_POSITION && onNoteClickedListener != null){
                    onNoteClickedListener.onNoteClicked(notesList.get(position));
                }
            });
        }
    }
}
