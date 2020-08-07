package com.nerd.movieinfoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.nerd.movieinfoapp.adapter.RecyclerViewAdapter;
import com.nerd.movieinfoapp.model.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText movie_search;
    ImageButton btn_search;
    Button btn_array_year;
    Button btn_array_attendance;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Movie> movieArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movie_search = findViewById(R.id.movie_search);
        btn_search = findViewById(R.id.btn_search);
        btn_array_year = findViewById(R.id.btn_array_year);
        btn_array_attendance = findViewById(R.id.btn_array_attendance);


    }
}
