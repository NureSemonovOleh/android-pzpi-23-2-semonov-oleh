package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private Context context;
    private int contextMenuPosition;


    public NoteAdapter(List<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
    }


    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView title, time;
        public ImageView icon;
        public View importanceIcon;

        public NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noteTitle);
            time = itemView.findViewById(R.id.noteTime);
            icon = itemView.findViewById(R.id.noteIcon);
            importanceIcon = itemView.findViewById(R.id.noteImportanceIcon);
        }
    }


    public int getContextMenuPosition() {
        return contextMenuPosition;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);


        holder.title.setText(note.getTitle());
        holder.time.setText(note.getDateTime());


        if (note.getImageUri() != null && !note.getImageUri().isEmpty()) {
            holder.icon.setImageURI(Uri.parse(note.getImageUri()));
        } else {
            holder.icon.setImageResource(R.drawable.ic_launcher_background);
        }


        int importanceColor = getImportanceColor(note.getImportance());
        holder.importanceIcon.setBackgroundColor(importanceColor);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewNote.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("description", note.getDescription());
            intent.putExtra("importance", note.getImportance());
            intent.putExtra("dateTime", note.getDateTime());
            intent.putExtra("imageUri", note.getImageUri());
            context.startActivity(intent);
        });


        holder.itemView.setOnLongClickListener(v -> {
            contextMenuPosition = holder.getAdapterPosition();
            ((MainActivity) context).openContextMenu(v);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    private int getImportanceColor(int importance) {
        switch (importance) {
            case 1:
                return context.getColor(R.color.low_importance);
            case 2:
                return context.getColor(R.color.medium_importance);
            case 3:
                return context.getColor(R.color.high_importance);
            default:
                return context.getColor(R.color.default_importance);
        }
    }
    public void updateList(List<Note> newList) {
        this.noteList = newList;
        notifyDataSetChanged();
    }


}
