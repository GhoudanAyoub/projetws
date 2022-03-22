package com.example.projetws;

import static com.example.projetws.Commun.IP;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
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
    public static final int PICK_IMAGE = 1;
    RequestQueue requestQueue;
    String insertUrl = "http://" + IP + "/BackEnd/ws/createEtudiant.php";
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_profile_camera_container = findViewById(R.id.add_profile_camera_container);
        add_tier_img = findViewById(R.id.add_tier_img);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        ville = (Spinner) findViewById(R.id.ville);
        add = (Button) findViewById(R.id.add);
        m = (RadioButton) findViewById(R.id.m);
        f = (RadioButton) findViewById(R.id.f);
        add.setOnClickListener(this);
        add_profile_camera_container.setOnClickListener(view -> showImageChooserPopup());
        findViewById(R.id.buttonshow).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity2.class)));

    }

    @Override
    public void onClick(View v) {
        Log.d("ok", "ok");
        if (v == add) {
            if(link!=null) {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest request = new StringRequest(Request.Method.POST,
                        insertUrl, response -> {
                    Log.d("TAG", response);
                    Toast.makeText(getApplicationContext(), "Ajouter avec succès", Toast.LENGTH_SHORT).show();
                    Type type = new TypeToken<Collection<Etudiant>>() {
                    }.getType();
                    Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                    try {
                        Log.d("TAG", etudiants.toString());
                        for (Etudiant e : etudiants) {
                            Log.d("TAG", e.toString());
                        }
                    }catch (Exception e){
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
                        params.put("img", link);
                        return params;
                    }
                };
                requestQueue.add(request);
            }
            else
                Toast.makeText(getApplicationContext(), "Try To Add Picture ", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImageChooserPopup() {

        ActionBottomDialogFragment addPhotoBottomDialogFragment =
                ActionBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                ActionBottomDialogFragment.TAG);
    }


    private Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
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
            link="";
            add_tier_img.setImageBitmap(
                    compressBitmap((Bitmap) item));
        }
        add_tier_img.setVisibility(View.VISIBLE);
    }
}