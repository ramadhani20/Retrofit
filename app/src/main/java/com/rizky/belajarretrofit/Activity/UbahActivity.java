package com.rizky.belajarretrofit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rizky.belajarretrofit.API.APIRequestData;
import com.rizky.belajarretrofit.API.RetroServer;
import com.rizky.belajarretrofit.Model.ResponseModel;
import com.rizky.belajarretrofit.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {
    private int xId;
    private String xNim, xNama, xJurusan;
    private EditText edtNim, edtNama, edtJurusan;
    private Button btnUbah;
    private String yNim, yNama, yJurusan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xNim = terima.getStringExtra("xNim");
        xNama = terima.getStringExtra("xNama");
        xJurusan = terima.getStringExtra("xJurusan");

        edtNim = findViewById(R.id.edt_nim);
        edtNama = findViewById(R.id.edt_nama);
        edtJurusan = findViewById(R.id.edt_jurusan);
        btnUbah = findViewById(R.id.btn_ubah);

        edtNim.setText(xNim);
        edtNama.setText(xNama);
        edtJurusan.setText(xJurusan);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yNim = edtNim.getText().toString();
                yNama = edtNama.getText().toString();
                yJurusan = edtJurusan.getText().toString();
                updateData();
            }
        });
    }
    private void updateData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ubahData = ardData.ardupdateData(xId,yNim,yNama,yJurusan);

        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, "Kode : " + kode + "| Pesan : " +pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal Menghubungi Server" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
