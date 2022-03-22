package com.example.projetws;

import static com.example.projetws.Commun.IP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EtudiantAdapter etudiantAdapter;
    private ProgressBar progressBar;

    RequestQueue requestQueue;
    String insertUrl = "http://"+IP+"/BackEnd/ws/loadEtudiant.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        etudiantAdapter = new EtudiantAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(etudiantAdapter);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        showAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAll();
    }

    private void showAll() {
        StringRequest request = new StringRequest(Request.Method.POST,
                insertUrl, response -> {
            try {
                Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
                Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                try {
                    for (Etudiant e : etudiants) {
                        Log.d("TAG", e.toString());
                    }
                    etudiantAdapter.setList((ArrayList<Etudiant>) etudiants);
                    progressBar.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Check You BackEnd Connection", Toast.LENGTH_SHORT).show();
            }
        }, error -> {

        }) ;
        requestQueue.add(request);
    }
}
