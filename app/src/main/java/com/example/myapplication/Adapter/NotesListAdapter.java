package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Listeners.NotesClickListener;
import com.example.myapplication.Models.Notes;
import com.example.myapplication.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Заметки
 */
public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder> {
    Context context;
    List<Notes> listNotes;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> listNotes, NotesClickListener listener) {
        this.context = context;
        this.listNotes = listNotes;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotesViewHolder holder, int position) {
        holder.text_title.setText(listNotes.get(position).getTitle());
        holder.text_title.setSelected(true);
        holder.text_view_notes.setText(listNotes.get(position).getNotes());
        holder.text_view_notes.setSelected(true);
        holder.text_view_date.setText(listNotes.get(position).getDate());
        holder.text_view_date.setSelected(true);
        if (listNotes.get(position).isPinned()) {
            holder.image_pin.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            // todo что нить другое
            holder.image_pin.setImageResource(0);
        }
        int colorCode = getRandomColor();
//        holder.notes_container.setCardBackgroundColor(colorCode);
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));
        holder.notes_container.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(listNotes.get(holder.getAdapterPosition()));
                    }
                }
        );
        holder.notes_container.setOnLongClickListener(view -> {
            listener.onLongClick(listNotes.get(holder.getAdapterPosition()), holder.notes_container);
            return true;
        });
    }

    private int getRandomColor() {
        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.color1);
        colorList.add(R.color.color4);
        colorList.add(R.color.color2);
        colorList.add(R.color.color3);
        Random random = new Random();
        int randomColor = random.nextInt(colorList.size());
        return colorList.get(randomColor);
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Notes> filtereList) {
        listNotes=filtereList;
        notifyDataSetChanged();
        }
}

class NotesViewHolder extends RecyclerView.ViewHolder {
    CardView notes_container;
    TextView text_title;
    ImageView image_pin;
    TextView text_view_notes;
    TextView text_view_date;

    public NotesViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        notes_container = itemView.findViewById(R.id.notes_container);
        text_title = itemView.findViewById(R.id.text_title);
        image_pin = itemView.findViewById(R.id.image_pin);
        text_view_notes = itemView.findViewById(R.id.text_view_notes);
        text_view_date = itemView.findViewById(R.id.text_view_date);
    }
}