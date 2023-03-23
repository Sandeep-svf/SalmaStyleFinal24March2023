package com.barcode.salmaStyle.uploadimagescreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barcode.salmaStyle.utol.CustomImageVIew;
import com.barcode.salmaStyle.utol.Originator;
import com.bumptech.glide.Glide;
import com.barcode.salmaStyle.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

public class NutritionFactFragment extends Originator {

    TextView submit;
    ImageView imageView1, imageView;
    File photo1=null;
    File photo2=null;
    File photo3=null;
    android.app.AlertDialog dialogs;
    SharedPreferences sharedPreferences;
    String img_1="",img_2="",img_3="";
    String bar_code="";
    TextView upload_image;
    String status="",id="";
    public static final int REQUEST_IMAGE = 100;
    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    ConstraintLayout back;
    TextView previous;
    String url="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_fact);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        submit=findViewById(R.id.nutrition_next);
        imageView1=findViewById(R.id.img_nutri_fact);
        upload_image=findViewById(R.id.upload_image_button);
        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        bar_code=sharedPreferences.getString("barcode","");
        back=findViewById(R.id.cons_tool_img);
        previous=findViewById(R.id.nutrition_prev);
        id=sharedPreferences.getString("id_pending","");
        Log.e("id_check","     "+id);
        url=sharedPreferences.getString("share_nut_img","");
        if(url!=null){
            Glide.with(NutritionFactFragment.this).load(url).placeholder(R.drawable.rectangle_background).into(imageView1);
        }

        String value = getIntent().getStringExtra("photo_product_image");
        String value1 =getIntent().getStringExtra("photo2");
        img_1=value;
        img_2=value1;
        Log.e("value_check_nutrition","         "+value+"         "+value1);
        imageListener();
        nextListener();
      //  submitListener();
       backListener();
       previousListener();
        imgZoomListener(url);
    }

    private void imgZoomListener(String url) {
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductImage_full_dilog(url);
            }
        });
    }

    private void ProductImage_full_dilog(String url) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_fullsize_popup);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        CustomImageVIew imag_full = dialog.findViewById(R.id.imag_full);

        ImageView img_cancel = dialog.findViewById(R.id.img_cancel);

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Glide.with(this).load(url).into(imag_full);

        dialog.show();
    }

    private void previousListener() {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void nextListener() {
       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(url==null){
                   Toast.makeText(NutritionFactFragment.this, getString(R.string.select_ingerident_photo), Toast.LENGTH_SHORT).show();
               }else {
                   Intent intent=new Intent(NutritionFactFragment.this,BarcodeFragment.class);
                   intent.putExtra("prodImg1", img_1);
                   intent.putExtra("ingImg2", img_2);
                   intent.putExtra("nutfacImg3", img_3);
                   startActivity(intent);
               }
           }
       });
    }

    private void imageListener() {

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView = imageView1;
                proceedAfterPermission();
            }
        });

    }

    private void proceedAfterPermission() {

        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(NutritionFactFragment.this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(NutritionFactFragment.this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("uri_check1","      ");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                photo1 = new File(result.getUri().getPath());
                img_3= String.valueOf(photo1);
                Log.e("uri_check","      "+resultUri);
                sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("share_nut_img", String.valueOf(resultUri));
                editor.apply();
                Glide.with(this).load(resultUri).into(imageView1);
            }
        }
    }

}