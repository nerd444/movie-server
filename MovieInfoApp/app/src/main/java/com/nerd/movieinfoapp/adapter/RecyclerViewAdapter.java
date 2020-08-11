package com.nerd.movieinfoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nerd.movieinfoapp.R;
import com.nerd.movieinfoapp.SignUpActivity;
import com.nerd.movieinfoapp.model.Movie;
import com.nerd.movieinfoapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

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

        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));    // 위의 시간을 utc로 맞추는것.(우리는 이미 서버에서 utc로 맞춰놔서 안해도 되는데 혹시몰라서 해줌)
        try {
            Date date = df.parse(year);
            df.setTimeZone(TimeZone.getDefault());      // 내 폰의 로컬 타임존으로 바꿔줌.
            String strDate = df.format(date);
            holder.year.setText(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        public ImageButton btn_star;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            genre = itemView.findViewById(R.id.genre);
            attendance = itemView.findViewById(R.id.attendance);
            year = itemView.findViewById(R.id.year);
            btn_star = itemView.findViewById(R.id.btn_star);

            btn_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 로그인 상태인지 확인
                    int position = getAdapterPosition();

                    SharedPreferences sp = context.getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                    final String token = sp.getString("token", null);
                    Log.i("AAA", token);
                    if (token == null){
                        Intent i = new Intent(context, SignUpActivity.class);
                        context.startActivity(i);
                    }else {
                        // 정상적으로 별표 표시를 서버를 보냅니다.
                        // 즐겨찾기 추가하는 API를 호출할건데,
                        // 호출하는 코드는 메인 액티비티에 메소드로 만들고,
                        // 여기에서는 position 값만 넘겨주도록 한다.

                        // 별표가 이미 있으면, 즐겨찾기 삭제 함수 호출!

                        // 별표가 없으면, 즐겨찾기 추가 함수 호출

                    }

                }
            });

        }
    }

}
