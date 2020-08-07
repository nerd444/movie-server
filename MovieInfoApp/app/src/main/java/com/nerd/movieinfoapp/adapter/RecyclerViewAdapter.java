package com.nerd.movieinfoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerd.movieinfoapp.R;
import com.nerd.movieinfoapp.model.Movie;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    Context context;
    ArrayList<Movie> movieArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row,parent,false); //inflate=만들라는 뜻
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);
        String title = movie.getTitle();
        String genre = movie.getGenre();
        String attendance = movie.getAttendance();
        String year = movie.getYear();

        holder.title.setText(title);
        holder.genre.setText(genre);
        holder.attendance.setText(attendance);
        holder.year.setText(year);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView genre;
        public TextView attendance;
        public TextView year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            genre = itemView.findViewById(R.id.genre);
            attendance = itemView.findViewById(R.id.attendance);
            year = itemView.findViewById(R.id.year);

        }
    }
}
