package com.barcode.salmaStyle.uploadimagescreen;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.ScanProductDetailActivity;
import com.barcode.salmaStyle.adapter.AdapterPending;
import com.barcode.salmaStyle.utol.CustomImageVIew;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.utol.NewVamoQrScanner;
import com.barcode.salmaStyle.utol.Originator;
import com.bumptech.glide.Glide;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.model.EditImageModel;
import com.barcode.salmaStyle.model.SubmitImageModel;
import com.google.gson.JsonSyntaxException;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class BarcodeFragment extends Originator {
    TextView submit;
    ImageView imageView1, imageView;
    File photo1=null;
    File photo2=null;
    File photo3=null;
    File photo4=null;
    String img_1="",img_2="",img_3="",img_4="";
    SharedPreferences sharedPreferences;
    String status="",id="";
    String bar_code="";
    TextView upload_image;
    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    TextView previous;
    ConstraintLayout back;
    String url="";
    String photo_fil="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_fragment);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        img_1 = getIntent().getStringExtra("prodImg1");
        img_2 = getIntent().getStringExtra("ingImg2");
        img_3 = getIntent().getStringExtra("nutfacImg3");
        submit=findViewById(R.id.barcode_submit);
        imageView1=findViewById(R.id.img_bar_code);
        upload_image=findViewById(R.id.upload_bar_code);
        previous=findViewById(R.id.barcode_previous);
        back=findViewById(R.id.cons_tool_img);
        bar_code=sharedPreferences.getString("barcode","");
        id=sharedPreferences.getString("id_pending","");
        Log.e("chk_id","      "+id);
        url=sharedPreferences.getString("share_barcode_img","");
        if(url!=null){
            Glide.with(BarcodeFragment.this).load(url).placeholder(R.drawable.rectangle_background).into(imageView1);
        }
        photo1= new File(img_1);
        photo2=new File(img_2);
        photo3=new File(img_3);
        imageListener();
        submitListener();
        previousListerner();
        backListener();
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

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void previousListerner() {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void submitListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("images","       "+img_1+"      "+img_2+"         "+img_3+"      "+img_4);
                Log.e("chk_phto_4","      "+photo4);
                if(photo4==null ){
                    Toast.makeText(BarcodeFragment.this, getString(R.string.please_select_photo), Toast.LENGTH_SHORT).show();
                }else {
                    status=sharedPreferences.getString("upload_status","");
                    Log.e("status","         "+status);

                    if(ScanProductDetailActivity.upload_status.equals("Not Clear Photos")){
                        editphotoApi();
                        Toast.makeText(BarcodeFragment.this, "Not Clear Photos" , Toast.LENGTH_SHORT).show();
                    }else if(AdapterPending.upload_status.equals("not_clear_photo_adapter")){
                        editphotoApi();
                    }else {
                        submitApi();
                    }
                }
            }
        });
    }

    private void imageListener() {
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        boolean result = ContextCompat.checkSelfPermission(BarcodeFragment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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
        boolean result = ContextCompat.checkSelfPermission(BarcodeFragment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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
                        Toast.makeText(BarcodeFragment.this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(BarcodeFragment.this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
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
                photo4 = new File(result.getUri().getPath());
                img_4= String.valueOf(photo4);
                Log.e("uri_check","      "+resultUri);
                sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("share_barcode_img", String.valueOf(resultUri));
                editor.putString("photo_file", String.valueOf(photo4));
                editor.apply();
                Glide.with(BarcodeFragment.this).load(resultUri).into(imageView1);
            }
        }
    }

    private void editphotoApi() {

        MultipartBody.Part multipart_image_one = null;
        MultipartBody.Part multipart_image_two = null;
        MultipartBody.Part multipart_image_three = null;
        MultipartBody.Part multipart_image_four = null;
        try {
            try {
                RequestBody requestfaceFile = RequestBody.create(photo1, MediaType.parse("image/*"));
                multipart_image_three = MultipartBody.Part.createFormData("product_image", photo1.getName(), requestfaceFile);
                RequestBody requestfaceFile2 = RequestBody.create(photo2, MediaType.parse("image/*"));
                multipart_image_one = MultipartBody.Part.createFormData("ingredients_image", photo2.getName(), requestfaceFile2);
                RequestBody requestfaceFile3 = RequestBody.create(photo3, MediaType.parse("image/*"));
                multipart_image_two = MultipartBody.Part.createFormData("nutrition_facts_image", photo3.getName(), requestfaceFile3);
                RequestBody requestfaceFile4 = RequestBody.create(photo3, MediaType.parse("image/*"));
                multipart_image_four = MultipartBody.Part.createFormData("barcode_image", photo4.getName(), requestfaceFile4);

            } catch (Exception e) {
                Log.e("conversionException", "errr" + e.getMessage());
            }
        } catch (Exception e) {
            Log.v("dahgsdhjgdfhjs", "***********************************************" + e);
            //Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
        CustomProgressbar.showProgressBar(this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<EditImageModel> call = service.editimages( multipart_image_one,multipart_image_two,multipart_image_three,multipart_image_four, id);
        call.enqueue(new Callback<EditImageModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<EditImageModel> call, @NonNull retrofit2.Response<EditImageModel> response) {
                CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {

                        String success = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (success.equalsIgnoreCase("True")) {
                            CustomProgressbar.showProgressBar(getApplicationContext(), false);
                            Toast.makeText(BarcodeFragment.this, R.string.submit_feedback, Toast.LENGTH_SHORT).show();
                            SharedPreferences preferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                            Intent intent=new Intent(BarcodeFragment.this, MainActivity.class);
                            preferences.edit().remove("share_prod_img").apply();
                            preferences.edit().remove("share_ing_img").apply();
                            preferences.edit().remove("share_nut_img").apply();
                            preferences.edit().remove("share_barcode_img").apply();
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);

                        } else {
                            Toast.makeText(BarcodeFragment.this, R.string.failed_error, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(BarcodeFragment.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(BarcodeFragment.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(BarcodeFragment.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(BarcodeFragment.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(BarcodeFragment.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(BarcodeFragment.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(BarcodeFragment.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(BarcodeFragment.this, "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<EditImageModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();

                if (t instanceof IOException) {
                    Toast.makeText(BarcodeFragment.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(BarcodeFragment.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void submitApi() {

        MultipartBody.Part multipart_image_one = null;
        MultipartBody.Part multipart_image_two = null;
        MultipartBody.Part multipart_image_three = null;
        MultipartBody.Part multipart_image_four = null;

        try {
            try {
                RequestBody requestfaceFile = RequestBody.create(photo1, MediaType.parse("image/*"));
                multipart_image_three = MultipartBody.Part.createFormData("product_image", photo1.getName(), requestfaceFile);
                RequestBody requestfaceFile2 = RequestBody.create(photo2, MediaType.parse("image/*"));
                multipart_image_one = MultipartBody.Part.createFormData("ingredients_image", photo2.getName(), requestfaceFile2);
                RequestBody requestfaceFile3 = RequestBody.create(photo3, MediaType.parse("image/*"));
                multipart_image_two = MultipartBody.Part.createFormData("nutrition_facts_image", photo3.getName(), requestfaceFile3);

                RequestBody requestfaceFile4 = RequestBody.create(photo4, MediaType.parse("image/*"));
                multipart_image_four = MultipartBody.Part.createFormData("barcode_image", photo4.getName(), requestfaceFile4);
            } catch (Exception e) {
                Log.e("conversionException", "errr" + e.getMessage());
            }
        } catch (Exception e) {
            Log.v("dahgsdhjgdfhjs", "***********************************************" + e);
            //Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
        CustomProgressbar.showProgressBar(this, false);
       Log.e("chk_data","     "+multipart_image_one+"      "+multipart_image_two+"         "+multipart_image_three+"          "+multipart_image_four+"             "+ NewVamoQrScanner.bar_code);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<SubmitImageModel> call = service.submitimages( multipart_image_one,multipart_image_two,multipart_image_three,multipart_image_four, NewVamoQrScanner.bar_code);
        call.enqueue(new Callback<SubmitImageModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SubmitImageModel> call, @NonNull retrofit2.Response<SubmitImageModel> response) {
                CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {

                        String success = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (success.equalsIgnoreCase("True")) {
                            Log.e("chk_suxxss","        ");
                            CustomProgressbar.showProgressBar(getApplicationContext(), false);
                            Toast.makeText(BarcodeFragment.this, R.string.submit_feedback , Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(BarcodeFragment.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);

                        } else {
                            Toast.makeText(BarcodeFragment.this, R.string.failed_error, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(BarcodeFragment.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(BarcodeFragment.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(BarcodeFragment.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(BarcodeFragment.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(BarcodeFragment.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(BarcodeFragment.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(BarcodeFragment.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(BarcodeFragment.this, "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<SubmitImageModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();

                if (t instanceof IOException) {
                    Log.e("chk_fail","        ");
                    Toast.makeText(BarcodeFragment.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(BarcodeFragment.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
