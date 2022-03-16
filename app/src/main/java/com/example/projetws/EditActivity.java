package com.example.projetws;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nom;
    private EditText prenom;
    private Spinner ville;
    private RadioButton m;
    private RadioButton f;
    private Button update;
    static int id = 0;
    RequestQueue requestQueue;
    String loadUrl = "http://192.168.1.6/BackEnd/ws/loadEtudiant.php";
    String updateUrl = "http://192.168.1.6/BackEnd/ws/updateEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        nom = findViewById(R.id.nomE);
        prenom = findViewById(R.id.prenomE);
        ville = findViewById(R.id.villeE);
        update = findViewById(R.id.update);
        m = findViewById(R.id.m);
        f = findViewById(R.id.f);

        Log.d("TAG", "onCreate: "+id);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST,
                loadUrl, response -> {
            try {
                 Log.d("TAG", response);
                Type type = new TypeToken<Collection<Etudiant>>() {
                }.getType();
                Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                for (Etudiant e : etudiants) {
                    if (e.getId() == id) {
                        nom.setText(e.getNom());
                        prenom.setText(e.getPrenom());
                        if (e.getSexe() == "homme") {
                            m.setSelected(true);
                        } else {
                            f.setSelected(true);
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Check You BackEnd Connection", Toast.LENGTH_SHORT).show();
            }
        }, error -> error.printStackTrace()) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        requestQueue.add(request);

        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("ok", "ok");
        if (v == update) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = new StringRequest(Request.Method.POST,
                    updateUrl, response -> {
                Log.d("TAG", response);
                Type type = new TypeToken<Collection<Etudiant>>() {
                }.getType();
                Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                for (Etudiant e : etudiants) {
                    Log.d("TAG", e.toString());
                }
                Toast.makeText(EditActivity.this, "Modification avec succès", Toast.LENGTH_SHORT).show();
            }, error -> Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() {
                    String sexe = "";
                    if (m.isChecked())
                        sexe = "homme";
                    else
                        sexe = "femme";
                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(id));
                    params.put("nom", nom.getText().toString());
                    params.put("prenom", prenom.getText().toString());
                    params.put("ville", ville.getSelectedItem().toString());
                    params.put("sexe", sexe);
                    return params;
                }
            };
            requestQueue.add(request);
        }
    }
}