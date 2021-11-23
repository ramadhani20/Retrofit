package com.rizky.belajarretrofit.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rizky.belajarretrofit.API.APIRequestData;
import com.rizky.belajarretrofit.API.RetroServer;
import com.rizky.belajarretrofit.Activity.MainActivity;
import com.rizky.belajarretrofit.Activity.UbahActivity;
import com.rizky.belajarretrofit.Model.DataModel;
import com.rizky.belajarretrofit.Model.ResponseModel;
import com.rizky.belajarretrofit.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private Context ctx;
    private List<DataModel> listData;
    private List<DataModel> listSiswa;
    private int idSiswa;

    public AdapterData(Context ctx, List<DataModel> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent,false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listData.get(position);
        holder.tvid.setText(String.valueOf(dm.getId()));
        holder.tvnim.setText(dm.getNim());
        holder.tvnama.setText(dm.getNama());
        holder.tvjurusan.setText(dm.getJurusan());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView  tvid, tvnim, tvnama, tvjurusan;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            tvid = itemView.findViewById(R.id.tv_id);
            tvnim = itemView.findViewById(R.id.tv_nim);
            tvnama = itemView.findViewById(R.id.tv_nama);
            tvjurusan = itemView.findViewById(R.id.tv_jurusan);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih Operasi yang akan dilakukan");
                    dialogPesan.setIcon(R.mipmap.ic_launcher);
                    dialogPesan.setCancelable(true);

                    idSiswa = Integer.parseInt(tvid.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                         Handler hand = new Handler();
                         hand.postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                 ((MainActivity)ctx).retrieveData();
                             }
                         },1000);
                        }
                    });

                    dialogPesan.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                        }
                    });

                    dialogPesan.show();

                    return false;
                }
            });
        }
        private void deleteData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idSiswa);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "kode :" +kode+ "|pesan", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal menghubungi server :" +t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void getData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idSiswa);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                   listSiswa = response.body().getData();

                    int varId = listData.get(0).getId();
                    String varNim = listData.get(0).getNim();
                    String varNama = listData.get(0).getNama();
                    String varJurusan = listData.get(0).getJurusan();

                    Intent ambil = new Intent(ctx, UbahActivity.class);
                    ambil.putExtra("xId", varId);
                    ambil.putExtra("xNim",varNim);
                    ambil.putExtra("xNama",varNama);
                    ambil.putExtra("xJurusan", varJurusan);
                    ctx.startActivity(ambil);
//                    Toast.makeText(ctx, "Kode : " + kode+"| Pesan : " +pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}