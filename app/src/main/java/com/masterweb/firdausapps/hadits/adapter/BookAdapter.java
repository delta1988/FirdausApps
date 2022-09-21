package com.masterweb.firdausapps.hadits.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.masterweb.firdausapps.R;
import com.masterweb.firdausapps.hadits.DetailHaditsActivity;
import com.masterweb.firdausapps.hadits.model.BukuHaditsModel;

import java.text.DecimalFormat;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    Context context;
    List<BukuHaditsModel> list_today;
    Intent intent;
    public BookAdapter(Context context, List<BukuHaditsModel> listSurat) {
        this.context = context;
        this.list_today = listSurat;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setHarga(List<BukuHaditsModel> movieList) {
        this.list_today = movieList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public BookAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_buku_hadits,parent,false);
        return new BookAdapter.MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull BookAdapter.MyViewHolder holder, final int position) {
        holder.name.setText("Hadits "+list_today.get(position).getName());
        holder.jumlah.setText(Rupiah(list_today.get(position).getTotal())+" Hadits");
        holder.list_report.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailHaditsActivity.class);
            intent.putExtra("slug",list_today.get(position).getSlug());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }
    public String Rupiah(String angka){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(Integer.valueOf(angka));
        return yourFormattedString.replace(",", ".");
    }
    @Override
    public int getItemCount() {
        if(list_today != null){
            return list_today.size();
        }
        return 0;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,jumlah;
        LinearLayout list_report;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            jumlah = itemView.findViewById(R.id.jumlah);
            list_report = itemView.findViewById(R.id.list);
        }
    }
}
