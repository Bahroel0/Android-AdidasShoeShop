package com.example.bahroel.adidasshoeshop.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
import com.example.bahroel.adidasshoeshop.MainActivity;
import com.example.bahroel.adidasshoeshop.Model.User;
import com.example.bahroel.adidasshoeshop.Model.UserLogged;
import com.example.bahroel.adidasshoeshop.R;
import com.example.bahroel.adidasshoeshop.Realm.RealmController;
import com.example.bahroel.adidasshoeshop.Response.ProdukResponse;
import com.example.bahroel.adidasshoeshop.Response.UserResponse;

import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {

    ImageView back;
    EditText edtUsername,edtEmail,edtPassword,edtKonfPassword;
    Button btnDaftar;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        back = findViewById(R.id.imgMenu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DaftarActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from",1);
                startActivity(intent);
            }
        });

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtKonfPassword = findViewById(R.id.edtKonfPassword);
        btnDaftar = findViewById(R.id.btnDaftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = edtUsername.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String konfpass = edtKonfPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)){
                    edtUsername.setError("Username tidak boleh kosong");
                    edtUsername.requestFocus();
                }
                if (TextUtils.isEmpty(email)){
                    edtEmail.setError("Email tidak boleh kosong");
                    edtEmail.requestFocus();
                }
                if (TextUtils.isEmpty(password)){
                    edtPassword.setError("Password tidak boleh kosong");
                    edtPassword.requestFocus();
                }
                if (TextUtils.isEmpty(konfpass)){
                    edtKonfPassword.setError("Konfirmasi password tidak boleh kosong");
                    edtKonfPassword.requestFocus();
                }

                if (password.equals(konfpass)){
                    // request to server
                    final ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                    Call<UserResponse> call = request.register(email,password);
                    call.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if(response.isSuccessful()){
                                UserResponse jsonresponse = response.body();
                                // store cache to realm
                                if(jsonresponse.isSuccess()){
                                    realm = RealmController.with(DaftarActivity.this).getRealm();
                                    RealmResults<UserLogged> result = realm.where(UserLogged.class).findAll();
                                    realm.beginTransaction();
                                    result.get(0).setId(jsonresponse.getUser().getId());
                                    result.get(0).setUsername(username);
                                    result.get(0).setApi_token(jsonresponse.getUser().getApi_token());
                                    result.get(0).setRemember_token(jsonresponse.getUser().getRemember_token());
                                    realm.commitTransaction();

                                    RealmController.with(DaftarActivity.this).refresh();

                                    // move to main activity
                                    Toast.makeText(DaftarActivity.this,""+jsonresponse.getMessage(),Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(DaftarActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(DaftarActivity.this,""+jsonresponse.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(DaftarActivity.this,"Koneksi ke server gagal",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            Toast.makeText(DaftarActivity.this,"Koneksi ke server gagal",Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    Toast.makeText(DaftarActivity.this,"Konfirmasi password salah !",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
