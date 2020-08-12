package com.nerd.movieinfoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nerd.movieinfoapp.adapter.RecyclerViewAdapter;
import com.nerd.movieinfoapp.model.Movie;
import com.nerd.movieinfoapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText movie_search;
    ImageButton btn_search;
    Button btn_array_year;
    Button btn_array_attendance;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Movie> movieArrayList = new ArrayList<>();

    RequestQueue requestQueue;

    // 페이징 처리를 위한 변수
    int offset = 0;
    int limit = 25;
    int cnt;

    //정렬을 위한 변수
    String order = "";
    String path = "/api/v1/movies";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movie_search = findViewById(R.id.movie_search);
        btn_search = findViewById(R.id.btn_search);
        btn_array_year = findViewById(R.id.btn_array_year);
        btn_array_attendance = findViewById(R.id.btn_array_attendance);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // 페이징 처리
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if ((lastPosition + 1) == totalCount){
                    if (cnt == limit){
                        // 네트워크 통해서, 데이터를 더 불러오면 된다.
                        addNetworkData(path);
                    }
                }

            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
        token = sp.getString("token",null);

        if (token != null){
            path = "/api/v1/movies/auth";
        }else {
            path = "/api/v1/movies";
        }


       getNetworkData(path);

        // 연도별로 정렬
        btn_array_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 데이터를 담고 있는, 어레이리스트를 비워준다.
                movieArrayList.clear();
                offset = 0;
                order = "desc";
                path = "/api/v1/movies/year";
                getNetworkData(path);
            }
        });

        // 검색하기
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = movie_search.getText().toString().trim();

                if (movieArrayList.size() > 0) {
                    movieArrayList.clear();
                    return;
                }

                if (key.isEmpty()) {
                    Toast.makeText(MainActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("title", key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Utils.BASEURL + "/api/v1/movies/search?offset="+offset+"&limit="+limit,
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray movies = response.getJSONArray("movies");
                                    for (int i = 0; i < movies.length(); i++) {
                                        JSONObject jsonObject = movies.getJSONObject(i);
                                        int id = jsonObject.getInt("id");
                                        String title = jsonObject.getString("title");
                                        String genre = jsonObject.getString("genre");
                                        String attendance = jsonObject.getString("attendance");
                                        String year = jsonObject.getString("year");

                                        int is_favorite;
                                        if (movies.getJSONObject(i).isNull("is_favorite")){
                                            is_favorite = 0;
                                        }else {
                                            is_favorite = movies.getJSONObject(i).getInt("is_favorite");
                                        }

                                        Log.i("검색", response.toString());

                                        Movie movie = new Movie(id, title, genre, attendance, year, is_favorite);
                                        movieArrayList.add(movie);
                                    }
                                    recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, movieArrayList);
                                    recyclerView.setAdapter(recyclerViewAdapter);

                                    // 페이징을 위해서, 오프셋을 증가 시킨다. 그래야 리스트 끝에가서 네트워크 다시 호출할 때,
                                    // 해당 offset으로 서버에 요청이 가능하다.
                                    offset = offset + response.getInt("count");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("검색", error.toString());
                            }
                        }
                );
                requestQueue.add(request);
            }
        });

    }

    // 영화데이터 전부 가져오기
    private void getNetworkData(String path) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Utils.BASEURL + path +"?offset="+offset+"&limit="+limit,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false){
                                // 유저한테 에러있다고 알리고 리턴.
                                Toast.makeText(MainActivity.this, "success가 false입니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                JSONArray movies = response.getJSONArray("movies");
                                for (int i = 0; i < movies.length(); i++) {
                                    JSONObject jsonObject = movies.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String title = jsonObject.getString("title");
                                    String genre = jsonObject.getString("genre");
                                    String attendance = jsonObject.getString("attendance");
                                    String year = jsonObject.getString("year");
                                    int is_favorite;
                                    if (movies.getJSONObject(i).isNull("is_favorite")){
                                        is_favorite = 0;
                                    }else {
                                        is_favorite = movies.getJSONObject(i).getInt("is_favorite");
                                    }

                                    Log.i("가져와", response.toString());

                                    Movie movie = new Movie(id, title, genre, attendance, year, is_favorite);
                                    movieArrayList.add(movie);
                                }
                                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, movieArrayList);
                                recyclerView.setAdapter(recyclerViewAdapter);

                                // 페이징을 위해서, 오프셋을 증가 시킨다. 그래야 리스트 끝에가서 네트워크 다시 호출할 때,
                                // 해당 offset으로 서버에 요청이 가능하다.
                                offset = offset + response.getInt("count");
                                cnt = response.getInt("count");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("가져와", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        requestQueue.add(request);
    }

    // 영화데이터 전부 가져오기 페이징함수
    private void addNetworkData(String path) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Utils.BASEURL + path +"?offset="+offset+"&limit="+limit,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                // 유저한테 에러있다고 알리고 리턴.
                                Toast.makeText(MainActivity.this, "success가 false입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONArray movies = response.getJSONArray("movies");
                            for (int i = 0; i < movies.length(); i++) {
                                JSONObject jsonObject = movies.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String title = jsonObject.getString("title");
                                String genre = jsonObject.getString("genre");
                                String attendance = jsonObject.getString("attendance");
                                String year = jsonObject.getString("year");
                                int is_favorite;
                                if (movies.getJSONObject(i).isNull("is_favorite")){
                                    is_favorite = 0;
                                }else {
                                    is_favorite = movies.getJSONObject(i).getInt("is_favorite");
                                }

                                Log.i("가져와", response.toString());

                                Movie movie = new Movie(id, title, genre, attendance, year, is_favorite);
                                movieArrayList.add(movie);
                            }

                            recyclerViewAdapter.notifyDataSetChanged();

                            //페이징을 위해서 오프셋을 변경시켜놔야 한다.
                            offset = offset + response.getInt("count");
                            cnt = response.getInt("count");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sign_up){
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.login){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void addFavorite(final int position){

        // position을 통해서, 즐겨찾기 추가할 movie_id 값을 가져올 수 있습니다.
        Movie movie = movieArrayList.get(position);
        int movie_id = movie.getId();

        JSONObject body = new JSONObject();
        try {
            body.put("movie_id", movie_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Utils.BASEURL + "/api/v1/favorites",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA","add favorite : "+response.toString());
                        // 어레이리스트의 값을 변경시켜줘야 한다.
                        Movie movie = movieArrayList.get(position);
                        movie.setIs_favorite(1);

                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                String token = sp.getString("token", null);

                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(request);

    }

    public void deleteFavorite(final int position){
        Movie movie = movieArrayList.get(position);

        // 서버에 보내기 위해서 필요
        int movie_id = movie.getId();

        JSONObject body = new JSONObject();
        try {
            body.put("movie_id", movie_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Utils.BASEURL + "/api/v1/favorites",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Movie movie = movieArrayList.get(position);
                        movie.setIs_favorite(0);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        requestQueue.add(request);
    }
}
