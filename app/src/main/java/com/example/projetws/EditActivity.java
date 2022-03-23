package com.example.projetws;

import static com.example.projetws.Commun.DecodeFromString;
import static com.example.projetws.Commun.compressBitmap;
import static com.example.projetws.Commun.getStringImage;
import static com.example.projetws.Commun.loadUrl;
import static com.example.projetws.Commun.showImageChooserPopup;
import static com.example.projetws.Commun.updateUrl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity implements View.OnClickListener, ActionBottomDialogFragment.ItemClickListener {
    private EditText nom;
    private EditText prenom;
    private Spinner ville;
    private RadioButton m;
    private RadioButton f;
    private Button update;
    static int id = 0;
    private ImageView add_tier_img;
    private FrameLayout add_profile_camera_container;
    RequestQueue requestQueue;
    private Object link;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        view();
        add_profile_camera_container.setOnClickListener(view -> showImageChooserPopup(getSupportFragmentManager()));
        update.setOnClickListener(this);
        fetchData();
    }

    private void fetchData() {

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
                        link = e.getImg();

                        if (e.getVille().equals("Marrakech")) {
                            ville.setSelection(0);
                        } else if (e.getVille().equals("Casablanca")) {
                            ville.setSelection(3);
                        } else if (e.getVille().equals("Agadir")) {
                            ville.setSelection(1);
                        } else if (e.getVille().equals("Essaouira")) {
                            ville.setSelection(2);
                        } else {
                            ville.setSelection(4);
                        }

                        if (e.getSexe().equals("homme")) {
                            m.setChecked(true);
                        } else {
                            f.setChecked(true);
                        }
                        try {
                            Bitmap decodedByte = DecodeFromString(e.getImg());
                            link = decodedByte;
                            Glide
                                    .with(getApplicationContext())
                                    .load(decodedByte)
                                    .apply(RequestOptions.fitCenterTransform())
                                    .into(add_tier_img);
                            add_tier_img.setVisibility(View.VISIBLE);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            add_tier_img.setVisibility(View.GONE);
                        }
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
    }

    private void view() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        add_profile_camera_container = findViewById(R.id.add_profile_camera_container);
        add_tier_img = findViewById(R.id.add_tier_img);
        nom = findViewById(R.id.nomE);
        prenom = findViewById(R.id.prenomE);
        ville = findViewById(R.id.villeE);
        update = findViewById(R.id.update);
        m = findViewById(R.id.mE);
        f = findViewById(R.id.fE);
    }

    @Override
    public void onClick(View v) {
        Log.d("ok", "ok");
        if (v == update) {
            updateData();
        }
    }

    private void updateData() {

        StringRequest request = new StringRequest(Request.Method.POST,
                updateUrl, response -> {
            Type type = new TypeToken<Collection<Etudiant>>() {
            }.getType();
            Toast.makeText(EditActivity.this, "Modification avec succès", Toast.LENGTH_SHORT).show();
            clearErea();
            super.onBackPressed();
            try {
                Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                for (Etudiant e : etudiants) {
                    Log.d("TAG", e.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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