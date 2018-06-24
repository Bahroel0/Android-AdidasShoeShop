package com.example.bahroel.adidasshoeshop.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bahroel.adidasshoeshop.Api.ApiInterface;
import com.example.bahroel.adidasshoeshop.Api.ApiRequest;
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

public class GantiPasswordActivity extends AppCompatActivity {
    EditText edtNewPassword;
    ImageView back;
    Button btn_ubah_password;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        back = findViewById(R.id.imgMenu);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btn_ubah_password = findViewById(R.id.btn_ubah_password);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GantiPasswordActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from",1);
                startActivity(intent);
            }
        });

        btn_ubah_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = edtNewPassword.getText().toString().trim();
                if (TextUtils.isEmpty(newPass)){
                    edtNewPassword.setError("Password tidak boleh kosong");
                    edtNewPassword.requestFocus();
                }

                realm = RealmController.with(GantiPasswordActivity.this).getRealm();
                RealmResults<UserLogged> result = realm.where(UserLogged.class).findAll();
                realm.beginTransaction();
                int id = result.get(0).getId();
                String api_token = result.get(0).getApi_token();
                realm.commitTransaction();

                RealmController.with(GantiPasswordActivity.this).refresh();

                final ApiInterface request = ApiRequest.getRetrofit().create(ApiInterface.class);
                Call<UserResponse> call = request.update(id,newPass,api_token);
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        UserResponse jsonresponse = response.body();
                        if(response.isSuccessful()){
                            Toast.makeText(GantiPasswordActivity.this,""+jsonresponse.getMessage(),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(GantiPasswordActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("from",1);
                            startActivity(intent);
                        }else{
                            Toast.makeText(GantiPasswordActivity.this,""+jsonresponse.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(GantiPasswordActivity.this,"Koneksi ke server gagal",Toast.LENGTH_LONG).show();
                    }
                });




            }
        });


    }
}
