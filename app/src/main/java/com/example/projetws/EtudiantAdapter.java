package com.example.projetws;

import static com.example.projetws.Commun.DecodeFromString;
import static com.example.projetws.Commun.deleteUrl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.viewHolder> {

    private ArrayList<Etudiant> EtudiantList = new ArrayList<>();

    RequestQueue requestQueue;
    private Context context;

    public EtudiantAdapter(Context applicationContext) {
        context = applicationContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_lay, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(EtudiantList.get(position).getVille());
        holder.textView2.setText(EtudiantList.get(position).getNom() + " " + EtudiantList.get(position).getPrenom());
        holder.textView4.setText(EtudiantList.get(position).getSexe());
        if (EtudiantList.get(position).getImg() != null) {
            Bitmap decodedByte = DecodeFromString(EtudiantList.get(position).getImg());
            Glide
                    .with(context)
                    .load(decodedByte)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(holder.add_tier_img);
            holder.add_tier_img.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditActivity.class);
            EditActivity.id = EtudiantList.get(position).getId();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.button.setOnClickListener(view -> {

            requestQueue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.POST,
                    deleteUrl, response -> {
                Type type = new TypeToken<Collection<Etudiant>>() {
                }.getType();
                Collection<Etudiant> etudiants = new Gson().fromJson(response, type);

                EtudiantList = (ArrayList<Etudiant>) etudiants;

                notifyDataSetChanged();
            }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("id", String.valueOf(EtudiantList.get(position).getId()));
                    return params;
                }
            };
            requestQueue.add(request);
        });
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

        TextView textView, textView2, textView4;
        FrameLayout button;
        ShapeableImageView add_tier_img;

        public viewHolder(View itemView) {
            super(itemView);


            add_tier_img = itemView.findViewById(R.id.add_tier_img);
            textView = itemView.findViewById(R.id.textView5);
            textView2 = itemView.findViewById(R.id.textView6);
            textView4 = itemView.findViewById(R.id.textView8);
            button = itemView.findViewById(R.id.remove);
        }
    }
}