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
import com.masterweb.firdausapps.hadits.model.DetailHaditsModel;

import java.text.DecimalFormat;
import java.util.List;

public class DetailHaditsAdapter extends RecyclerView.Adapter<DetailHaditsAdapter.MyViewHolder> {
    Context context;
    List<DetailHaditsModel> list_today;
    Intent intent;
    public DetailHaditsAdapter(Context context, List<DetailHaditsModel> listSurat) {
        this.context = context;
        this.list_today = listSurat;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setHarga(List<DetailHaditsModel> movieList) {
        this.list_today = movieList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DetailHaditsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_detailhadits,parent,false);
        return new DetailHaditsAdapter.MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull DetailHaditsAdapter.MyViewHolder holder, final int position) {
        holder.number.setText("Nomor Hadits : "+list_today.get(position).getNumber());
        holder.arab.setText(list_today.get(position).getArab());
        holder.id.setText(list_today.get(position).getId());
        holder.list_report.setOnClickListener(v -> {

        });

    }
    public String huruf_arab(String val) {
        char[] arabicChars = {'٠','١','٢','٣','٤','٥','٦','٧','٨','٩'};
        StringBuilder builder = new StringBuilder();
        for(int i =0;i<val.length();i++){
            if(Character.isDigit(val.charAt(i))){
                builder.append(arabicChars[(int)(val.charAt(i))-48]);
            }
            else{
                builder.append(val.charAt(i));
            }
        }
        return builder.toString();
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
        TextView number,id,arab;
        LinearLayout list_report;

        public MyViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            id = itemView.findViewById(R.id.id);
            arab = itemView.findViewById(R.id.arab);
            list_report = itemView.findViewById(R.id.list);
        }
    }
}
