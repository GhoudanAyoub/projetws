package com.example.projetws;

import static com.example.projetws.Commun.IP;
import static com.example.projetws.Commun.compressBitmap;
import static com.example.projetws.Commun.getStringImage;
import static com.example.projetws.Commun.insertUrl;
import static com.example.projetws.Commun.showImageChooserPopup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ActionBottomDialogFragment.ItemClickListener {
    private EditText nom;
    private EditText prenom;
    private Spinner ville;
    private RadioButton m;
    private RadioButton f;
    private Button add;
    private ImageView add_tier_img;
    private FrameLayout add_profile_camera_container;
    RequestQueue requestQueue;
    private Object link;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view();
        add.setOnClickListener(this);
        add_profile_camera_container.setOnClickListener(view -> showImageChooserPopup(getSupportFragmentManager()));
        findViewById(R.id.buttonshow).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity2.class)));

    }

    private void view() {
        add_profile_camera_container = findViewById(R.id.add_profile_camera_container);
        add_tier_img = findViewById(R.id.add_tier_img);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        ville = (Spinner) findViewById(R.id.ville);
        add = (Button) findViewById(R.id.add);
        m = (RadioButton) findViewById(R.id.m);
        f = (RadioButton) findViewById(R.id.f);
    }

    @Override
    public void onClick(View v) {
        Log.d("ok", "ok");
        if (v == add) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = new StringRequest(Request.Method.POST,
                    insertUrl, response -> {
                Log.d("TAG", response);
                Toast.makeText(getApplicationContext(), "Ajouter avec succès", Toast.LENGTH_SHORT).show();
                clearErea();
                try {
                    Type type = new TypeToken<Collection<Etudiant>>() {
                    }.getType();
                    Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                    Log.d("TAG", etudiants.toString());
                    for (Etudiant e : etudiants) {
                        Log.d("TAG", e.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams() {
                    String sexe = "";
                    if (m.isChecked())
                        sexe = "homme";
                    else
                        sexe = "femme";
                    HashMap<String, String> params = new HashMap<>();
                    params.put("nom", nom.getText().toString());
                    params.put("prenom", prenom.getText().toString());
                    params.put("ville", ville.getSelectedItem().toString());
                    params.put("sexe", sexe);
                    if (link != null) {
                        if (link.getClass().equals(String.class))
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(link.toString()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        else
                            bitmap = (Bitmap) link;

                        if (bitmap != null)
                            try {
                                params.put("img", getStringImage(bitmap));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    } else {
                        params.put("img", "no");
                    }
                    return params;
                }
            };
            requestQueue.add(request);
        }
    }

    private void clearErea() {
        nom.setText("");
        prenom.setText("");
        add_tier_img.setVisibility(View.GONE);
    }


    @Override
    public void onItemClick(Object item) {
        if (item.getClass().equals(String.class)) {
            link = item.toString();
            Glide
                    .with(getApplicationContext())
                    .load(link)
                    .centerCrop()
                    .into(add_tier_img);
        } else {
            link = item;
            add_tier_img.setImageBitmap(
                    compressBitmap((Bitmap) item));
        }
        add_tier_img.setVisibility(View.VISIBLE);
    }
}