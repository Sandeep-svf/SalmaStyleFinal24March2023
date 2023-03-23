package com.barcode.salmaStyle.uploadimagescreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.ScanProductDetailActivity;
import com.barcode.salmaStyle.adapter.AdapterPending;
import com.barcode.salmaStyle.adapter.ItemClick;
import com.barcode.salmaStyle.adapter.ProductAdapter;
import com.barcode.salmaStyle.imagepicker.ImagePickerActivity;
import com.barcode.salmaStyle.model.EditImageModel;
import com.barcode.salmaStyle.model.ProductModel;
import com.barcode.salmaStyle.model.SubmitImageModel;
import com.barcode.salmaStyle.utol.CustomImageVIew;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.utol.NewVamoQrScanner;
import com.barcode.salmaStyle.utol.Originator;
import com.bumptech.glide.Glide;

import com.google.gson.JsonSyntaxException;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ProductImageFragment extends Originator implements ItemClick {

    TextView next, previous;
    ImageView imageView1, imageView;
    File photo1 = null;
    SharedPreferences sharedPreferences;
    TextView upload_image;
    File photo2 = null;
    File photo3 = null;
    File photo4 = null;
    String img_1 = "", img_2 = "", img_3 = "", img_4 = "";
    public static final int REQUEST_IMAGE = 100;
    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    ViewPager2 viewPager_data;
    ConstraintLayout back;
    String url = "";
    ProductAdapter productAdapter;
    ItemClick itemClick;
    ArrayList<ProductModel> productimgList = new ArrayList<>();
    int pos = 0;
    int viewpage_pos = 0;
    String status = "", id = "";
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        itemClick = ProductImageFragment.this;
        next = findViewById(R.id.product_image_next);
        previous = findViewById(R.id.previous_prod_img);
        viewPager_data = findViewById(R.id.view_pager);
        imageView1 = findViewById(R.id.img_prod);
        upload_image = findViewById(R.id.upload_image_button);
        back = findViewById(R.id.cons_tool_img);

        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        id = sharedPreferences.getString("id_pending", "");
        imageListener();
        Log.e("image_1", "       " + photo1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // zoomImageListener(url);
        viewPager_data.setUserInputEnabled(false);
        viewPager_data.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                viewpage_pos = position;

                int lastReal = viewPager_data.getAdapter().getItemCount() - 1;
                if (position == 0 && lastReal != 0) {
                    next.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.INVISIBLE);
                    upload_image.setVisibility(View.GONE);
                } else if (lastReal == 0 && position == 0) {
                    previous.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                    upload_image.setVisibility(View.GONE);
                } else if (position == lastReal) {
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.GONE);
                    upload_image.setVisibility(View.VISIBLE);

                } else {
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    upload_image.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        ProductModel productModel1 = new ProductModel();
        ProductModel productModel2 = new ProductModel();
        ProductModel productModel3 = new ProductModel();
        ProductModel productModel4 = new ProductModel();
        productimgList.add(productModel1);
        productimgList.add(productModel2);
        productimgList.add(productModel3);
        productimgList.add(productModel4);

        productAdapter = new ProductAdapter(ProductImageFragment.this, itemClick, productimgList);
        viewPager_data.setAdapter(productAdapter);

        nextListener();
        previousListener();
    }

    private void previousListener() {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager_data.setCurrentItem(viewPager_data.getCurrentItem() - 1);
            }
        });
    }

    private void zoomImageListener(String url) {
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

    private void imageListener() {

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < productimgList.size(); i++) {
                    Log.e("chk_url", "   " + productimgList.get(i).getFile_url());
                    img_1 = String.valueOf(productimgList.get(0).getFile_url());
                    img_2 = String.valueOf(productimgList.get(1).getFile_url());
                    img_3 = String.valueOf(productimgList.get(2).getFile_url());
                    img_4 = String.valueOf(productimgList.get(3).getFile_url());
                }
                photo1 = new File(img_1);
                photo2 = new File(img_2);
                photo3 = new File(img_3);
                photo4 = new File(img_4);

                if (productimgList.get(3).getFile_url() == null) {
                    Toast.makeText(ProductImageFragment.this, getString(R.string.please_select_photo), Toast.LENGTH_SHORT).show();
                } else if (ScanProductDetailActivity.upload_status.equals("Not Clear Photos")) {
                    editphotoApi(photo1, photo2, photo3, photo4);
                    //  Toast.makeText(BarcodeFragment.this, "Not Clear Photos" , Toast.LENGTH_SHORT).show();
                } else if (AdapterPending.upload_status.equals("not_clear_photo_adapter")) {
                    editphotoApi(photo1, photo2, photo3, photo4);
                } else {
                    submitApi(photo1, photo2, photo3, photo4);
                }
            }

        });
    }

    private void proceedAfterPermission() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    cameraIntent();

                } else if (options[item].equals("Choose from Gallery")) {

                    galleryIntent();

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
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


    private void nextListener() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("view_pager_pos", "       " + productimgList.get(0).getFile_url());

                if (productimgList.get(viewpage_pos).getFile_url() == null) {
                    Toast.makeText(ProductImageFragment.this, getString(R.string.please_select_photo), Toast.LENGTH_SHORT).show();
                } else if (productimgList.get(0).getFile_url() != null) {
                    viewPager_data.setCurrentItem(viewPager_data.getCurrentItem() + 1);
                }

            }
        });
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
                        Toast.makeText(ProductImageFragment.this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ProductImageFragment.this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                // String sel_path = getpath(uri);
                productimgList.get(pos).setFile_url(uri.getPath());
                productAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void sendclickEvent(int position) {
        pos = position;
        imageView = imageView1;
        proceedAfterPermission();
    }


    private void submitApi(File Photo1, File Photo2, File Photo3, File Photo4) {

        MultipartBody.Part multipart_image_one = null;
        MultipartBody.Part multipart_image_two = null;
        MultipartBody.Part multipart_image_three = null;
        MultipartBody.Part multipart_image_four = null;

        try {
            try {
                RequestBody requestfaceFile = RequestBody.create(Photo1, MediaType.parse("image/*"));
                multipart_image_three = MultipartBody.Part.createFormData("product_image", Photo1.getName(), requestfaceFile);
                RequestBody requestfaceFile2 = RequestBody.create(Photo2, MediaType.parse("image/*"));
                multipart_image_one = MultipartBody.Part.createFormData("ingredients_image", Photo2.getName(), requestfaceFile2);
                RequestBody requestfaceFile3 = RequestBody.create(Photo3, MediaType.parse("image/*"));
                multipart_image_two = MultipartBody.Part.createFormData("nutrition_facts_image", Photo3.getName(), requestfaceFile3);

                RequestBody requestfaceFile4 = RequestBody.create(Photo4, MediaType.parse("image/*"));
                multipart_image_four = MultipartBody.Part.createFormData("barcode_image", Photo4.getName(), requestfaceFile4);
            } catch (Exception e) {
                Log.e("conversionException", "errr" + e.getMessage());
            }
        } catch (Exception e) {
            Log.v("dahgsdhjgdfhjs", "***********************************************" + e);
            //Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
        CustomProgressbar.showProgressBar(ProductImageFragment.this, false);
        Log.e("chk_data", "     " + multipart_image_one + "      " + multipart_image_two + "         " + multipart_image_three + "          " + multipart_image_four + "             " + NewVamoQrScanner.bar_code);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<SubmitImageModel> call = service.submitimages(multipart_image_one, multipart_image_two, multipart_image_three, multipart_image_four, NewVamoQrScanner.bar_code);
        call.enqueue(new Callback<SubmitImageModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SubmitImageModel> call, @NonNull retrofit2.Response<SubmitImageModel> response) {
                CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {

                        String success = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (success.equalsIgnoreCase("True")) {
                            Log.e("chk_suxxss", "        ");
                            CustomProgressbar.showProgressBar(ProductImageFragment.this, false);
                            Toast.makeText(ProductImageFragment.this, R.string.submit_feedback, Toast.LENGTH_SHORT).show();
                            SharedPreferences preferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                            preferences.edit().remove("share_prod_img").apply();
                            preferences.edit().remove("share_ing_img").apply();
                            preferences.edit().remove("share_nut_img").apply();
                            preferences.edit().remove("share_barcode_img").apply();

                            Intent intent = new Intent(ProductImageFragment.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);

                        } else {
                            Toast.makeText(ProductImageFragment.this, R.string.failed_error, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProductImageFragment.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProductImageFragment.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProductImageFragment.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProductImageFragment.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProductImageFragment.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProductImageFragment.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProductImageFragment.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProductImageFragment.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Log.e("chk_fail", "        ");
                    Toast.makeText(ProductImageFragment.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProductImageFragment.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editphotoApi(File Photo1, File Photo2, File Photo3, File Photo4) {

        MultipartBody.Part multipart_image_one = null;
        MultipartBody.Part multipart_image_two = null;
        MultipartBody.Part multipart_image_three = null;
        MultipartBody.Part multipart_image_four = null;
        try {
            try {
                RequestBody requestfaceFile = RequestBody.create(Photo1, MediaType.parse("image/*"));
                multipart_image_three = MultipartBody.Part.createFormData("product_image", Photo1.getName(), requestfaceFile);
                RequestBody requestfaceFile2 = RequestBody.create(Photo2, MediaType.parse("image/*"));
                multipart_image_one = MultipartBody.Part.createFormData("ingredients_image", Photo2.getName(), requestfaceFile2);
                RequestBody requestfaceFile3 = RequestBody.create(Photo3, MediaType.parse("image/*"));
                multipart_image_two = MultipartBody.Part.createFormData("nutrition_facts_image", Photo3.getName(), requestfaceFile3);
                RequestBody requestfaceFile4 = RequestBody.create(Photo4, MediaType.parse("image/*"));
                multipart_image_four = MultipartBody.Part.createFormData("barcode_image", Photo4.getName(), requestfaceFile4);

            } catch (Exception e) {
                Log.e("conversionException", "errr" + e.getMessage());
            }
        } catch (Exception e) {
            Log.v("dahgsdhjgdfhjs", "***********************************************" + e);
            //Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

        CustomProgressbar.showProgressBar(this, false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<EditImageModel> call = service.editimages(multipart_image_one, multipart_image_two, multipart_image_three, multipart_image_four, id);
        call.enqueue(new Callback<EditImageModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<EditImageModel> call, @NonNull retrofit2.Response<EditImageModel> response) {
                CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {

                        String success = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (success.equalsIgnoreCase("True")) {
                            CustomProgressbar.showProgressBar(ProductImageFragment.this, false);
                            Toast.makeText(ProductImageFragment.this, R.string.submit_feedback, Toast.LENGTH_SHORT).show();

                            SharedPreferences preferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                            Intent intent = new Intent(ProductImageFragment.this, MainActivity.class);
                            preferences.edit().remove("share_prod_img").apply();
                            preferences.edit().remove("share_ing_img").apply();
                            preferences.edit().remove("share_nut_img").apply();
                            preferences.edit().remove("share_barcode_img").apply();
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);

                        } else {
                            Toast.makeText(ProductImageFragment.this, R.string.failed_error, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProductImageFragment.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProductImageFragment.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProductImageFragment.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProductImageFragment.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProductImageFragment.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProductImageFragment.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProductImageFragment.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProductImageFragment.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProductImageFragment.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProductImageFragment.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}