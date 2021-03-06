package com.example.bahroel.adidasshoeshop.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
import com.example.bahroel.adidasshoeshop.CartActivity;
import com.example.bahroel.adidasshoeshop.MainActivity;
import com.example.bahroel.adidasshoeshop.Model.UserLogged;
import com.example.bahroel.adidasshoeshop.R;
import com.example.bahroel.adidasshoeshop.Realm.RealmController;
import com.example.bahroel.adidasshoeshop.Response.UserResponse;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ImageView back;
    TextView tvDaftar;
    EditText edtEmail, edtPassword;
    Button btnMasuk;
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = (ImageView) findViewById(R.id.imgMenu);
        tvDaftar = (TextView) findViewById(R.id.tv_daftar);
        edtEmail = (EditText)findViewById(R.id.edtEmailLogin);
        edtPassword = (EditText) findViewById(R.id.edtPasswordLogin);
        btnMasuk = (Button)findViewById(R.id.btnMasuk);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from",1);
                startActivity(intent);
            }
        });

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, DaftarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    edtEmail.setError("Email tidak boleh kosong");
                    edtEmail.requestFocus();
                }else
                if (TextUtils.isEmpty(password)){
                    edtPassword.setError("Password tidak boleh kosong");
                    edtPassword.requestFocus();
                }else{
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Login . . .");
                    progressDialog.show();
                    final ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                    Call<UserResponse> call = request.login(email,password);
                    call.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if(response.isSuccessful()){
                                UserResponse jsonresponse = response.body();
                                // store cache to realm
                                if(jsonresponse.isSuccess()){
                                    progressDialog.dismiss();
                                    realm = RealmController.with(LoginActivity.this).getRealm();
                                    RealmResults<UserLogged> result = realm.where(UserLogged.class).findAll();
                                    realm.beginTransaction();
                                    result.get(0).setId(jsonresponse.getUser().getId());
                                    result.get(0).setUsername(jsonresponse.getUser().getName());
                                    result.get(0).setApi_token(jsonresponse.getUser().getApi_token());
                                    result.get(0).setRemember_token(jsonresponse.getUser().getRemember_token());
                                    realm.commitTransaction();

                                    RealmController.with(LoginActivity.this).refresh();

                                    // move to main activity
                                    Toast.makeText(LoginActivity.this,""+jsonresponse.getMessage(),Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("from",1);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this,""+jsonresponse.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this,"Koneksi ke server gagal",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this,"Koneksi ke server gagal",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
