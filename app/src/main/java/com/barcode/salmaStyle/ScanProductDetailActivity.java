package com.barcode.salmaStyle;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.utol.CustomImageVIew;
import com.barcode.salmaStyle.utol.Originator;
import com.barcode.salmaStyle.utol.SharedPrefClass;

public class ScanProductDetailActivity extends Originator {

    String image1 = "";
    String Image2 = "";
    String Image3 = "";
    String comment_english = "", comment_farsi = "", str_status = "";
    ImageView img1, img2, img3;
    TextView eng, farsi, status;
    TextView toolbar_text;
    Button home;
    SharedPreferences sharedPreferences;
    ConstraintLayout back;
    TextView upload_image;
    SharedPrefClass sharedPrefClass;

    public static String upload_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        sharedPrefClass = new SharedPrefClass(this);
        image1 = getIntent().getStringExtra("image1");
        Image2 = getIntent().getStringExtra("image2");
        Image3 = getIntent().getStringExtra("image3");
        Log.e("scan_prod_img", "      " + image1 + "       " + Image2 + "          " + Image3);
        comment_english = getIntent().getStringExtra("comment_english");
        comment_farsi = getIntent().getStringExtra("comment_farsi");
        str_status = getIntent().getStringExtra("status");
        String not_key = getIntent().getStringExtra("notification_key");
        // upload_status="Not Clear Photos";
        img1 = findViewById(R.id.product_image);
        img2 = findViewById(R.id.ingerident);
        img3 = findViewById(R.id.nutrition_fact);
        eng = findViewById(R.id.product_brand_value);
        farsi = findViewById(R.id.farsi);
        status = findViewById(R.id.product_status_value);
        toolbar_text = findViewById(R.id.toolbar_text);
        // home=findViewById(R.id.home);
        back = findViewById(R.id.cons_tool_img);
        toolbar_text.setText(getString(R.string.product_detail));
        eng.setText(comment_english);
        farsi.setText(comment_farsi);
        upload_image = findViewById(R.id.upload_image_button);
        Log.e("images1", "        " + image1 + "     " + Image2 + "           " + Image3);

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            eng.setVisibility(View.GONE);
            farsi.setVisibility(View.VISIBLE);
            Log.e("valuecheck", "spanish    " + sharedPrefClass.getString(SharedPrefClass.LANGUAGE));
        } else {
            eng.setVisibility(View.VISIBLE);
            farsi.setVisibility(View.GONE);
        }

        if (str_status.equals("Not Clear Photos")) {
            upload_image.setVisibility(View.VISIBLE);
            //   Toast.makeText(this, "Not Clear Photos" , Toast.LENGTH_SHORT).show();
        } else if (str_status.equals("Pending")) {
            upload_image.setVisibility(View.VISIBLE);
        } else {

        }

        if (str_status.equals("Approved")) {
            status.setText(R.string.salma_approved);
            status.setTextColor(Color.parseColor("#097F04"));
        } else if (str_status.equals("Rejected")) {
            status.setText(R.string.salma_not_approved);
            status.setTextColor(Color.parseColor("#ff0000"));
        } else if (str_status.equals("Rejected While Dieting")) {
            status.setText(R.string.salma_on_diet);
            status.setTextColor(Color.parseColor("#f26531"));
        } else if (str_status.equals("Occasionally")) {
            status.setText(R.string.salma_occ_appr);
            status.setTextColor(Color.parseColor("#097F04"));

        } else if (str_status.equals("Not Clear Photos")) {
            status.setTextColor(Color.parseColor("#ff0000"));
            status.setText(R.string.photo_not_clear);
            status.setTextColor(Color.parseColor("#ff0000"));
        } else {
            status.setTextColor(Color.parseColor("#ff0000"));
            status.setText(R.string.pending);
            status.setTextColor(Color.parseColor("#f26531"));
        }

        String prod_img = "http://69.49.235.253:8000" + "" + image1;
        String ing_img = "http://69.49.235.253:8000" + Image2;
        String nut_img = "http://69.49.235.253:8000" + Image3;

        Log.e("images", "        " + image1 + "     " + Image2 + "           " + Image3);

        Glide.with(this).load(prod_img)
                .thumbnail(0.5f)
                .into(img1);

        Glide.with(this).load(ing_img)
                .thumbnail(0.5f)
                .into(img2);

        Glide.with(this).load(nut_img)
                .thumbnail(0.5f)
                .into(img3);

        homeListener();
        uploadimageListener();
        productimageListener(prod_img);
        ingeridenimageListener(ing_img);
        nutritionfactimageListener(nut_img);

        if (not_key != null) {
            if (not_key.equals("not_value"))
                upload_image.setVisibility(View.GONE);
        }
    }

    private void nutritionfactimageListener(String nut_img) {
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NutritionImage_full_dilog(nut_img);
            }
        });
    }

    private void NutritionImage_full_dilog(String nut_img) {
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
        Glide.with(this).load(nut_img)./*apply(new RequestOptions().placeholder(R.drawable.profile_front_image)).*/into(imag_full);

        dialog.show();
    }

    private void ingeridenimageListener(String ing_img) {
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngeridentImage_full_dilog(ing_img);
            }
        });
    }

    private void IngeridentImage_full_dilog(String ing_img) {
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
        Glide.with(this).load(ing_img)./*apply(new RequestOptions().placeholder(R.drawable.profile_front_image)).*/into(imag_full);

        dialog.show();
    }

    private void productimageListener(String prod_img) {
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductImage_full_dilog(prod_img);
            }
        });
    }

    private void ProductImage_full_dilog(String prod_img) {
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
        Glide.with(this).load(prod_img)./*apply(new RequestOptions().placeholder(R.drawable.profile_front_image)).*/into(imag_full);
        dialog.show();
    }

    private void uploadimageListener() {

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_status = "Not Clear Photos";
                Intent intent = new Intent(ScanProductDetailActivity.this, MainActivity.class);
                intent.putExtra("login_key", "productimage_fragment");
                startActivity(intent);
            }
        });
    }

    private void homeListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}