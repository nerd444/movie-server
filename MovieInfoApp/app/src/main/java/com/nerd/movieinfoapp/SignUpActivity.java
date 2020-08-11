package com.nerd.movieinfoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nerd.movieinfoapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    EditText edit_email;
    EditText edit_passwd;
    EditText edit_passwd_check;
    Button btn_sign_up;
    Button btn_login;

    SharedPreferences sp;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 자동 로그인 = 토큰이 있으면 자동으로 로그인되도록하도록
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE) ;
        String token = sharedPreferences.getString("token", null);
        if (token != null){
            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(i);
            finish();
            return;
        }

        requestQueue = Volley.newRequestQueue(SignUpActivity.this);

        edit_email = findViewById(R.id.edit_email);
        edit_passwd = findViewById(R.id.edit_passwd);
        edit_passwd_check = findViewById(R.id.edit_passwd_check);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        btn_login = findViewById(R.id.btn_login);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edit_email.getText().toString().trim();
                final String passwd = edit_passwd.getText().toString().trim();
                String passwd_check = edit_passwd_check.getText().toString().trim();

                // 클라이언트에서 1차적으로 체크, 서버에서 2차적으로 체크 (== 안그러면, 보안이 취약함)
                if(email.contains("@") == false){
                    Toast.makeText(SignUpActivity.this, "이메일 형식에 맞지 않는 이메일입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd.length() < 4 || passwd.length() > 12){
                    Toast.makeText(SignUpActivity.this, "비밀번호 길이는 4자리 이상, 12자리 이하여야합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd.equalsIgnoreCase(passwd_check) == false){
                    Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("email", email);
                    object.put("passwd", passwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 서버로 이메일과 비밀번호를 전송한다.
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Utils.BASEURL + "/api/v1/users",
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("회원가입", response.toString());
                                try {
                                    String token = response.getString("token");
                                    sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("token", token);
                                    editor.apply();

                                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("회원가입", error.toString());
                            }
                        }
                );
                requestQueue.add(request);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}

