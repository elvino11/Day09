package com.example.day09.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.day09.R;
import com.example.day09.api.ApiConfig;
import com.example.day09.data.User;
import com.example.day09.data.UserPreferences;
import com.example.day09.databinding.ActivityMainBinding;
import com.example.day09.response.LoginResponse;
import com.example.day09.ui.home.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isLogin();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = String.valueOf(binding.tieUsername.getText());
                String password = String.valueOf(binding.tiePassword.getText());

                if (username.isEmpty()) {
                    binding.tilUsername.setError("ISI KOCAK");
                } else if (password.isEmpty()) {
                    binding.tilPassword.setError("ISI KOCAK");
                } else {
                    processLogin(username, password);
                }
            }
        });
    }

    private void isLogin() {
        //Inisialisasi Object
        UserPreferences userPreferences = new UserPreferences(this);

        user = userPreferences.getUser();

        //cek login
        if (user.isLogin()) {
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        }
    }

    private void processLogin(String username, String password) {
        //hidupkan progressBar
        showLoading(true);
        Call<LoginResponse> client = ApiConfig.getApiService().login(username, password);

        client.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                //Kalau Berhasil
                //matikan progress bar
                showLoading(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //mengambil nilai status (BERHASIL LOGIN)
                        if (response.body().isStatus()) {
                            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                            Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                            //Mengambil Data Dari API LOGIN
                            String username = response.body().getData().getUsername();
                            String name = response.body().getData().getName();
                            String id = response.body().getData().getId();
                            boolean isLogin = true;

                            //SIMPAN DATA
                            saveUser(username, name, id, isLogin);

                        } else {
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //Kalau Gagal
                showLoading(false);
            }
        });
    }

    private void saveUser(String username, String name, String id, boolean isLogin) {
        //Inisialisasi Object
        UserPreferences userPreferences = new UserPreferences(this);

        //Buat LAGI USER
        user.setName(name);
        user.setUsername(username);
        user.setId(id);
        user.setLogin(isLogin);

        //TERSIMPAN
        userPreferences.setUser(user);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}