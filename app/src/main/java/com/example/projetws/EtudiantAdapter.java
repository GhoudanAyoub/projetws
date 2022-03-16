package com.example.projetws;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    String deleteUrl = "http://192.168.1.1/BackEnd/ws/deleteEtudiant.php";
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
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(EtudiantList.get(position).getVille());
        holder.textView2.setText(EtudiantList.get(position).getNom());
        holder.textView3.setText(EtudiantList.get(position).getPrenom());
        holder.textView4.setText(EtudiantList.get(position).getSexe());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditActivity.class);
            EditActivity.id=EtudiantList.get(position).getId();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.button.setOnClickListener(view -> {

            requestQueue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.POST,
                    deleteUrl, response -> {
                        Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
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

        TextView textView,textView2,textView3,textView4;
        Button button;
        public viewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView5);
            textView2 = itemView.findViewById(R.id.textView6);
            textView3 = itemView.findViewById(R.id.textView7);
            textView4 = itemView.findViewById(R.id.textView8);
            button = itemView.findViewById(R.id.remove);
        }
    }
}