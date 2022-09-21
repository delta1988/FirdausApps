package com.masterweb.firdausapps.quran.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masterweb.firdausapps.R;
import com.masterweb.firdausapps.quran.SuratActivity;
import com.masterweb.firdausapps.quran.model.SuratModel;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class SuratAdapter extends RecyclerView.Adapter<SuratAdapter.ViewHolder> {

    List<SuratModel> dataArrayList;
    Context activity;

    public SuratAdapter(List<SuratModel> dataArrayList, Context activity) {
        this.dataArrayList = dataArrayList;
        this.activity = activity;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setHarga(List<SuratModel> movieList) {
        this.dataArrayList = movieList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_surat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SuratAdapter.ViewHolder holder, int position) {
        SuratModel data = dataArrayList.get(position);
        holder.textView.setText(data.getArab());
        holder.textView1.setText(data.getNama());
        holder.textView2.setText(data.getKeterangan());
        holder.list.setOnClickListener(V->{
            String title[] = dataArrayList.get(position).getNama().split(" - ");
            Intent intent = new Intent(activity, SuratActivity.class);
            intent.putExtra("id",dataArrayList.get(position).getNumber());
            intent.putExtra("title",title[0]);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(dataArrayList != null){
            return dataArrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textView1;
        TextView textView2;
        LinearLayout list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list = itemView.findViewById(R.id.list);
            textView = itemView.findViewById(R.id.arab);
            textView1 = itemView.findViewById(R.id.nama_surat);
            textView2 = itemView.findViewById(R.id.keterangan);

        }
    }
}
