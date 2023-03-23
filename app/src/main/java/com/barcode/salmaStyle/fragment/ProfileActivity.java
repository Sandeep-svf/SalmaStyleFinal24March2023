package com.barcode.salmaStyle.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.barcode.salmaStyle.UpdateProfileActivity;
import com.barcode.salmaStyle.login.SignupActivity;
import com.bumptech.glide.Glide;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.model.ProfileModel;
import com.barcode.salmaStyle.response.ProfileResponse;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.utol.Originator;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ProfileActivity extends Originator {

    CardView edit_profile;
    ProfileResponse profileResponse;
    SharedPreferences sharedPreferences;
    String token="";
    TextView name,email,phone_no;
    ImageView profile;
    String profile_url="";
    TextView toolbar_text;
    ConstraintLayout back;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_profile);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        edit_profile=findViewById(R.id.btn_edt_profile);
        name=findViewById(R.id.txt_profile_name);

        email=findViewById(R.id.txt_profile_email);
        phone_no=findViewById(R.id.txt_profile_phone);
        profile=findViewById(R.id.profile_imageUpdate_profile);
        toolbar_text=findViewById(R.id.toolbar_text);
        back=findViewById(R.id.cons_tool_img);
        toolbar_text.setText(R.string.my_profile);
        Glide.with(profile.getContext()).load(R.drawable.profile_front_image).into(profile);
        sharedPreferences = this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        token=sharedPreferences.getString("token","");
        Log.e("token_check","   "+token);
        backListener();
        edtprofileListener();
        profileApi();







    }

    private void veryfy_email(String email2) {

    }


    private void backListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void profileApi() {

        CustomProgressbar.showProgressBar(this, false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ProfileModel> call = service.getprofile();
        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProfileModel> call, @NonNull retrofit2.Response<ProfileModel> response) {
             CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(ProfileActivity.this, R.string.view_profile, Toast.LENGTH_SHORT).show();
                            // sendVerificationCode(newPhoneNumber);
                            profileResponse = response.body().getProfileResponse();

                            profile_url=profileResponse.getProfile();
                            name.setText(profileResponse.getName());
                            email.setText(profileResponse.getEmail());
                            Log.e("chk_no","      "+profileResponse.getPhone());
                            phone_no.setText(profileResponse.getPhone());
                            if(profile_url.equals("null")){
                                Glide.with(profile.getContext()).load(R.drawable.profile_front_image).into(profile);
                            }else {
                                String image_url =  "http://69.49.235.253:8000" + profile_url;
                                Glide.with(profile.getContext()).load(image_url).placeholder(R.drawable.profile_front_image).into(profile);

                              //  Glide.with(profile.getContext()).load(image_url).into(profile);
                                Log.e("success_my_profile","               "+image_url);
                            }


                        } else {
                            Toast.makeText(ProfileActivity.this,R.string.failed_error, Toast.LENGTH_LONG).show();

                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ProfileActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ProfileActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ProfileActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ProfileActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ProfileActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ProfileActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ProfileActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ProfileActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ProfileModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(ProfileActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ProfileActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void edtprofileListener() {

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

    }

}
