package com.yourcitydate.poras.datingapp.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yourcitydate.poras.datingapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ProfileEdit extends AppCompatActivity {

    private String[] imgCount;
    private ImageView a, b, c, d, e, f, g, h, i;
    private ProgressBar progressBar;
    private Bitmap b1,b2,b3,b4,b5,b6,b7,b8,b9;
    private Bitmap[] bitmaps;
    final int x = 256;
    final int y = 256;
    final int aspectx = 4;
    final int aspecty = 4;
    private GridLayout gridLayout;
     String SharedPrefs = "prefs";
     SharedPreferences sharedPreferences;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image_edit);
        sharedPreferences = getSharedPreferences(SharedPrefs,MODE_PRIVATE);
        UID = sharedPreferences.getString("UID","0");

        imgCount = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

        bitmaps = new Bitmap[]{b1,b2,b3,b4,b5,b6,b7,b8,b9};

        gridLayout = (GridLayout)findViewById(R.id.grid);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Add Photos");


        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        a = (ImageView) findViewById(R.id.profile1);
        b = (ImageView) findViewById(R.id.profile2);
        c = (ImageView) findViewById(R.id.profile3);
        d = (ImageView) findViewById(R.id.profile4);
        e = (ImageView) findViewById(R.id.profile5);
        f = (ImageView) findViewById(R.id.profile6);
        g = (ImageView) findViewById(R.id.profile7);
        h = (ImageView) findViewById(R.id.profile8);
        i = (ImageView) findViewById(R.id.profile9);



        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent();
                a.setType("image/*");
                a.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(a, 1);

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent();
                b.setType("image/*");
                b.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(b, 2);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent();
                c.setType("image/*");
                c.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(c, 3);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent d = new Intent();
                d.setType("image/*");
                d.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(d, 4);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent();
                e.setType("image/*");
                e.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(e, 5);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent();
                f.setType("image/*");
                f.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(f, 6);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent g = new Intent();
                g.setType("image/*");
                g.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(g, 7);
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent h = new Intent();
                h.setType("image/*");
                h.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(h, 8);
            }
        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent li = new Intent();
                li.setType("image/*");
                li.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(li, 9);
            }
        });

        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                100);
        checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                101);

        loadImagesAlreadyInDatabase();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                // User chose the "Settings" item, show the app settings UI...
                SendImageToDatabase();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                finish();
                return super.onOptionsItemSelected(item);
        }

    }

    private void loadImagesAlreadyInDatabase() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("profileImages");
        //final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                        Map<String, Object> map =  (Map<String,Object>) dataSnapshot.getValue();
                        if (map.get("one") != null){
                            Glide.with(getApplicationContext())
                                    .load(map.get("one").toString())
                                    .into(a);
                        }
                       if (map.get("two") != null){
                            Glide.with(getApplicationContext())
                                    .load(map.get("two").toString())
                                    .into(b);
                        }
                        if (map.get("three") != null){
                            Glide.with(getApplicationContext())
                                    .load(map.get("three").toString())
                                    .into(c);
                        }
                        if (map.get("four") != null){
                            Glide.with(getApplicationContext())
                                    .load(map.get("four").toString())
                                    .into(d);
                        }
                        if (map.get("five") != null){
                            Glide.with(getApplicationContext())
                                    .load(map.get("five").toString())
                                    .into(e);
                        }
                        if (map.get("six") != null){
                            Glide.with(getApplicationContext())
                                    .load(map.get("six").toString())
                                    .into(f);
                        }
                        if (map.get("seven") != null){
                            Glide.with(getApplicationContext())
                                    .load(map.get("seven").toString())
                                    .into(g);
                        }
                        if (map.get("eight") != null){
                            Glide.with(getApplicationContext())
                                    .load(map.get("eight").toString())
                                    .into(h);
                        }
                        if (map.get("nine") != null){
                            Glide.with(getApplicationContext())
                                    .load(map.get("nine").toString())
                                    .into(i);
                        }
                    }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (data != null) {
                Bundle extras = data.getExtras();
                 bitmaps[0]= extras.getParcelable("data");
                a.setImageBitmap(bitmaps[0]);
            }
        }
        if (requestCode == 2000) {
            if (data != null) {
                Bundle extras = data.getExtras();
                bitmaps[1]= extras.getParcelable("data");
                b.setImageBitmap(bitmaps[1]);
            }
        }
        if (requestCode == 3000) {
            if (data != null) {
                Bundle extras = data.getExtras();
                bitmaps[2]= extras.getParcelable("data");
                c.setImageBitmap(bitmaps[2]);
            }
        }
        if (requestCode == 4000) {
            if (data != null) {
                Bundle extras = data.getExtras();
                bitmaps[3]= extras.getParcelable("data");
                d.setImageBitmap(bitmaps[3]);
            }
        }
        if (requestCode == 5000) {
            if (data != null) {
                Bundle extras = data.getExtras();
                bitmaps[4]= extras.getParcelable("data");
                e.setImageBitmap(bitmaps[4]);
            }
        }
        if (requestCode == 6000) {
            if (data != null) {
                Bundle extras = data.getExtras();
                bitmaps[5]= extras.getParcelable("data");
                f.setImageBitmap(bitmaps[5]);
            }
        }
        if (requestCode == 7000) {
            if (data != null) {
                Bundle extras = data.getExtras();
                bitmaps[6]= extras.getParcelable("data");
                g.setImageBitmap(bitmaps[6]);
            }
        }
        if (requestCode == 8000) {
            if (data != null) {
                Bundle extras = data.getExtras();
                bitmaps[7]= extras.getParcelable("data");
                h.setImageBitmap(bitmaps[7]);
            }
        }
        if (requestCode == 9000) {
            if (data != null) {
                Bundle extras = data.getExtras();
                bitmaps[8]= extras.getParcelable("data");
                i.setImageBitmap(bitmaps[8]);
            }
        }

        switch (requestCode) {
            case 1:
                if (data != null) {
                    try {

                        Intent cropIntent = new Intent("com.android.camera.action.CROP");

                        cropIntent.setDataAndType(data.getData(), "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("aspectX", aspectx);
                        cropIntent.putExtra("aspectY", aspecty);
                        cropIntent.putExtra("outputX", x);
                        cropIntent.putExtra("outputY", y);
                        cropIntent.putExtra("max-width",600);
                        cropIntent.putExtra("max-height",600);
                        cropIntent.putExtra("scale", "true");
                        cropIntent.putExtra("return-data", true);
                        cropIntent.putExtra("scaleUpIfNeeded",true);
                        startActivityForResult(cropIntent, 1000);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (ActivityNotFoundException anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case 2:
                if (data != null) {
                    try {

                        Intent cropIntent = new Intent("com.android.camera.action.CROP");

                        cropIntent.setDataAndType(data.getData(), "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("aspectX", aspectx);
                        cropIntent.putExtra("aspectY", aspecty);
                        cropIntent.putExtra("outputX", x);
                        cropIntent.putExtra("outputY", y);
                        cropIntent.putExtra("return-data", true);
                        startActivityForResult(cropIntent, 2000);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (ActivityNotFoundException anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case 3:
                if (data != null) {
                    try {

                        Intent cropIntent = new Intent("com.android.camera.action.CROP");

                        cropIntent.setDataAndType(data.getData(), "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("aspectX", aspectx);
                        cropIntent.putExtra("aspectY", aspecty);
                        cropIntent.putExtra("outputX", x);
                        cropIntent.putExtra("outputY", y);
                        cropIntent.putExtra("return-data", true);
                        startActivityForResult(cropIntent, 3000);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (ActivityNotFoundException anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case 4:
                if (data != null) {
                    try {

                        Intent cropIntent = new Intent("com.android.camera.action.CROP");

                        cropIntent.setDataAndType(data.getData(), "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("aspectX", aspectx);
                        cropIntent.putExtra("aspectY", aspecty);
                        cropIntent.putExtra("outputX", x);
                        cropIntent.putExtra("outputY", y);
                        cropIntent.putExtra("return-data", true);
                        startActivityForResult(cropIntent, 4000);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (ActivityNotFoundException anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case 5:
                if (data != null) {
                    try {

                        Intent cropIntent = new Intent("com.android.camera.action.CROP");

                        cropIntent.setDataAndType(data.getData(), "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("aspectX", aspectx);
                        cropIntent.putExtra("aspectY", aspecty);
                        cropIntent.putExtra("outputX", x);
                        cropIntent.putExtra("outputY", y);
                        cropIntent.putExtra("return-data", true);
                        startActivityForResult(cropIntent, 5000);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (ActivityNotFoundException anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case 6:
                if (data != null) {
                    try {

                        Intent cropIntent = new Intent("com.android.camera.action.CROP");

                        cropIntent.setDataAndType(data.getData(), "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("aspectX", aspectx);
                        cropIntent.putExtra("aspectY", aspecty);
                        cropIntent.putExtra("outputX", x);
                        cropIntent.putExtra("outputY", y);
                        cropIntent.putExtra("return-data", true);
                        startActivityForResult(cropIntent, 6000);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (ActivityNotFoundException anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case 7:
                if (data != null) {
                    try {

                        Intent cropIntent = new Intent("com.android.camera.action.CROP");

                        cropIntent.setDataAndType(data.getData(), "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("aspectX", aspectx);
                        cropIntent.putExtra("aspectY", aspecty);
                        cropIntent.putExtra("outputX", x);
                        cropIntent.putExtra("outputY", y);
                        cropIntent.putExtra("return-data", true);
                        startActivityForResult(cropIntent, 7000);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (ActivityNotFoundException anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case 8:
                if (data != null) {
                    try {

                        Intent cropIntent = new Intent("com.android.camera.action.CROP");

                        cropIntent.setDataAndType(data.getData(), "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("aspectX", aspectx);
                        cropIntent.putExtra("aspectY", aspecty);
                        cropIntent.putExtra("outputX", x);
                        cropIntent.putExtra("outputY", y);
                        cropIntent.putExtra("return-data", true);
                        startActivityForResult(cropIntent, 8000);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (ActivityNotFoundException anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case 9:
                if (data != null) {
                    try {
                        Intent cropIntent = new Intent("com.android.camera.action.CROP");

                        cropIntent.setDataAndType(data.getData(), "image/*");
                        cropIntent.putExtra("crop", "true");
                        cropIntent.putExtra("aspectX", aspectx);
                        cropIntent.putExtra("aspectY", aspecty);
                        cropIntent.putExtra("outputX", x);
                        cropIntent.putExtra("outputY", y);
                        cropIntent.putExtra("return-data", true);
                        startActivityForResult(cropIntent, 9000);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (ActivityNotFoundException anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;




        }
    }


    public void checkPermission(String permission, int requestCode) {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            this,
                            new String[]{permission},
                            requestCode);
        } else {
            Toast
                    .makeText(this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        " Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(this,
                        " Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == 101) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

        private void SendImageToDatabase () {
            progressBar.setVisibility(View.VISIBLE);
            gridLayout.setAlpha(0);

            for (int i = 0; i < bitmaps.length; i++) {
                if (bitmaps[i] != null) {
                    final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("userProfileImages").child(UID).child(imgCount[i]);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmaps[i].compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    byte[] data = byteArrayOutputStream.toByteArray();

                    UploadTask uploadTask = filepath.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error uploading profile image", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    final int finalI = i;
                    final int finalI1 = i;
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(),"image Uploaded",Toast.LENGTH_SHORT).show();
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imagePath = uri.toString();
                                    DatabaseReference reference;
                                    reference = FirebaseDatabase.getInstance().getReference();
                                    Map map = new HashMap<>();
                                    map.put(imgCount[finalI], imagePath);

                                    reference.child("Users").child(UID).child("profileImages").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "oyhoy :D", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    });
                }

            }




        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imagemenu,menu);
        return true;
    }
}
