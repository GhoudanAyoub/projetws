package com.example.projetws;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.viewHolder> {

    private ArrayList<Etudiant> EtudiantList = new ArrayList<>();

    private Context context;
    public EtudiantAdapter(Context applicationContext) {
        context= applicationContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_lay, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.textView.setText(EtudiantList.get(position).getVille());
        holder.textView2.setText(EtudiantList.get(position).getNom());
        holder.textView3.setText(EtudiantList.get(position).getPrenom());
        holder.textView4.setText(EtudiantList.get(position).getSexe());
    }

    @Override
    public int getItemCount() {
        return EtudiantList.size();
    }

    public void setList(ArrayList<Etudiant> EtudiantList) {
        this.EtudiantList = EtudiantList;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView textView,textView2,textView3,textView4;
        public viewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView5);
            textView2 = itemView.findViewById(R.id.textView6);
            textView3 = itemView.findViewById(R.id.textView7);
            textView4 = itemView.findViewById(R.id.textView8);
        }
    }
}