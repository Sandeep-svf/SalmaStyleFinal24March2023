package com.barcode.salmaStyle.login;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.utol.Originator;
import com.barcode.salmaStyle.model.ChangePasswordModel;
import com.barcode.salmaStyle.R;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class ChangePasswordActivity extends Originator {

    EditText edt_old_password,edt_new_password,edt_confirm_passsword;
    String old_password="",new_password="",confirm_pass="";
    CardView confirm;
    LinearLayout cons_old,cons_new,cons_confirm;
    ImageView password_show_old,password_show_new,password_show_confirm;
    ConstraintLayout back;
    TextView toolbar_text;
    private boolean isClick = false;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    ImageView salmaLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        edt_old_password=findViewById(R.id.edt_old_password);
        edt_new_password=findViewById(R.id.edt_password);
        edt_confirm_passsword=findViewById(R.id.edt_confirm_password);
        confirm=findViewById(R.id.btn_update);
        cons_old=findViewById(R.id.passwordLayout_old);
        cons_new=findViewById(R.id.passwordLayout_new);
        cons_confirm=findViewById(R.id.passwordLayout_confirm);
        password_show_old=findViewById(R.id.password_show_old);
        password_show_new=findViewById(R.id.password_show_new);
        password_show_confirm=findViewById(R.id.password_show_confirm);
        back=findViewById(R.id.cons_tool_img);
        toolbar_text=findViewById(R.id.toolbar_text);
        salmaLogo=findViewById(R.id.img_salma);
        Glide.with(this).load(R.drawable.logosalma)
                .thumbnail(0.5f)
                .into(salmaLogo);
        toolbar_text.setText(getString(R.string.change_pass));
        consoldListener();
        consnewListener();
        consconfirmListener();
        confirmListerner();
        backListener();
    }

    private void backListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void consconfirmListener() {

        cons_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClick) {
                    isClick = true;
                    password_show_confirm.setImageResource(R.drawable.visibility_off_icon);
                    edt_confirm_passsword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_confirm_passsword.setSelection(edt_confirm_passsword.getText().length());
                } else {
                    isClick = false;
                    password_show_confirm.setImageResource(R.drawable.visibility_icon);
                    edt_confirm_passsword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_confirm_passsword.setSelection(edt_confirm_passsword.getText().length());
                }
            }
        });

    }

    private void consnewListener() {

        cons_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClick) {
                    isClick = true;
                    password_show_new.setImageResource(R.drawable.visibility_off_icon);
                    edt_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_new_password.setSelection(edt_new_password.getText().length());
                } else {
                    isClick = false;
                    password_show_new.setImageResource(R.drawable.visibility_icon);
                    edt_new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_new_password.setSelection(edt_new_password.getText().length());
                }
            }
        });

    }

    private void consoldListener() {

        cons_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClick) {
                    isClick = true;

                    password_show_old.setImageResource(R.drawable.visibility_off_icon);
                    edt_old_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_old_password.setSelection(edt_old_password.getText().length());
                } else {
                    isClick = false;
                    password_show_old.setImageResource(R.drawable.visibility_icon);
                    edt_old_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_old_password.setSelection(edt_old_password.getText().length());
                }
            }
        });

    }

    private void confirmListerner() {

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                old_password=edt_old_password.getText().toString();
                new_password=edt_new_password.getText().toString();
                confirm_pass=edt_confirm_passsword.getText().toString();

                if (old_password.equals("")) {
                    Toast toast = Toast.makeText(ChangePasswordActivity.this, getString(R.string.enter_old_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(new_password.equals("")){
                    Toast toast = Toast.makeText(ChangePasswordActivity.this, getString(R.string.enter_new_pas), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(confirm_pass.equals("")){
                    Toast toast = Toast.makeText(ChangePasswordActivity.this, getString(R.string.enter_confirm_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else if (!new_password.equals(confirm_pass)) {
                    Toast toast = Toast.makeText(ChangePasswordActivity.this, getString(R.string.pls_enter_same_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    changepasswordApi();
                }


            }
        });
    }

    private void changepasswordApi() {

        CustomProgressbar.showProgressBar(this, false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ChangePasswordModel> call = service.changepassword(old_password,new_password);
        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ChangePasswordModel> call, @NonNull retrofit2.Response<ChangePasswordModel> response) {
              CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(ChangePasswordActivity.this, R.string.password_changed_successfully, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);

                        } else {
                            Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();

                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ChangePasswordActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ChangePasswordActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ChangePasswordActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ChangePasswordActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ChangePasswordActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ChangePasswordActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ChangePasswordActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ChangePasswordActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ChangePasswordModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(ChangePasswordActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ChangePasswordActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}