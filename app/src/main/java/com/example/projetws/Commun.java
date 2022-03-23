package com.example.projetws;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;

public class Commun {

    public static String IP = "192.168.1.9";
    public static String loadUrl = "http://" + IP + "/BackEnd/ws/loadEtudiant.php";
    public static String updateUrl = "http://" + IP + "/BackEnd/ws/updateEtudiant.php";
    public static String insertUrl = "http://" + IP + "/BackEnd/ws/createEtudiant.php";
    public static String deleteUrl = "http://"+IP+"/BackEnd/ws/deleteEtudiant.php";

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    public static Bitmap DecodeFromString(String image) {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static void showImageChooserPopup(FragmentManager fragmentManager) {

        ActionBottomDialogFragment addPhotoBottomDialogFragment =
                ActionBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(fragmentManager,
                ActionBottomDialogFragment.TAG);
    }

    public static Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
    }
}
