package com.group11.kth.foreignfriend;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Created by HENRY on 3/2/2017.
 */

public class Utils {

    public static String Dir = "images";
    public static String image_name = "user_profile_picture";
    public StorageReference storageRef;

    public static void saveInternalStorage(Bitmap image, Context context){

        /*ContextWrapper cw = new ContextWrapper(context);
        File dir = cw.getDir(Dir, Context.MODE_PRIVATE);
        File path = new File(dir, image_name);
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(path);
            // compress the picture
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.d("Save File Intern", dir.getAbsolutePath());
        }
        catch (Exception e ){
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }*/




    }

    public static void loadImage( ImageView v, String user_id, Context context){

        StorageReference storageRef = MapsActivity.storage.getReference();
        StorageReference imagePath = storageRef.child("images/user_profile_"+user_id+".jpg");

        // Load with Glide
        Glide.with(context).using(new FirebaseImageLoader()).load(imagePath).into(v);
    }
}
