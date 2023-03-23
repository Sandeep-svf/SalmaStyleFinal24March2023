package com.barcode.salmaStyle.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import android.widget.Toast;

import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.RetroifitErrorResponse.ApiClientNoToken;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.model.VarifyModel;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgetChangePasswordActivity extends AppCompatActivity {

    LinearLayout new_pass,confirm_pass;
    private boolean isClick = false;
    EditText edt_pass_new,edt_pass_confirm;
    ImageView password_show_new,password_show_confirm;
    CardView btn_update;
    String str_new_pass="",str_confirm_pass="";
    String phone="";
    ImageView salmaLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_change_password);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new_pass=findViewById(R.id.passwordLayout_new_forget);
        confirm_pass=findViewById(R.id.passwordLayout_confirm_forget);
        edt_pass_new=findViewById(R.id.edt_password_forget);
        edt_pass_confirm=findViewById(R.id.edt_confirm_password_forget);
        password_show_new=findViewById(R.id.password_show_new);
        password_show_confirm=findViewById(R.id.password_show_confirm);
        btn_update=findViewById(R.id.btn_update_forget);
        salmaLogo=findViewById(R.id.img_salma);
        Glide.with(this).load(R.drawable.logosalma)
                .thumbnail(0.5f)
                .into(salmaLogo);
        phone=getIntent().getStringExtra("mob_no_country");
        forgetnewpassLIstener();
        forgetconfirmpassListener();
        btnupdateListener();
      }

    private void btnupdateListener() {

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_new_pass=edt_pass_new.getText().toString();
                str_confirm_pass=edt_pass_confirm.getText().toString();


                if(str_new_pass.equals("")){
                    Toast toast = Toast.makeText(ForgetChangePasswordActivity.this, getString(R.string.enter_new_pas), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(confirm_pass.equals("")){
                    Toast toast = Toast.makeText(ForgetChangePasswordActivity.this, getString(R.string.enter_confirm_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }else if (!str_new_pass.equals(str_confirm_pass)) {
                    Toast toast = Toast.makeText(ForgetChangePasswordActivity.this, getString(R.string.pls_enter_same_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    forgetchangeApi();
                }


            }
        });

    }

    private void forgetchangeApi() {

        CustomProgressbar.showProgressBar(this, false);

        APIService service = ApiClientNoToken.getClient().create(APIService.class);
        retrofit2.Call<VarifyModel> call = service.reset_pass(phone,str_new_pass);
        call.enqueue(new Callback<VarifyModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<VarifyModel> call, @NonNull retrofit2.Response<VarifyModel> response) {
               CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                             Toast.makeText(ForgetChangePasswordActivity.this,R.string.pass_changed, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgetChangePasswordActivity.this,LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            Toast.makeText(ForgetChangePasswordActivity.this,message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ForgetChangePasswordActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ForgetChangePasswordActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ForgetChangePasswordActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ForgetChangePasswordActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ForgetChangePasswordActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ForgetChangePasswordActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ForgetChangePasswordActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ForgetChangePasswordActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<VarifyModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(ForgetChangePasswordActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ForgetChangePasswordActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void forgetconfirmpassListener() {

        confirm_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isClick) {
                            isClick = true;
                            password_show_confirm.setImageResource(R.drawable.visibility_off_icon);
                            edt_pass_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            edt_pass_confirm.setSelection(edt_pass_confirm.getText().length());
                        } else {
                            isClick = false;
                            password_show_confirm.setImageResource(R.drawable.visibility_icon);
                            edt_pass_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            edt_pass_confirm.setSelection(edt_pass_confirm.getText().length());
                        }
                    }
                });
            }
        });

    }

    private void forgetnewpassLIstener() {

        new_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isClick) {
                    isClick = true;

                    password_show_new.setImageResource(R.drawable.visibility_off_icon);
                    edt_pass_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_pass_new.setSelection(edt_pass_new.getText().length());
                } else {
                    isClick = false;
                    password_show_new.setImageResource(R.drawable.visibility_icon);
                    edt_pass_new.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_pass_new.setSelection(edt_pass_new.getText().length());
                }

            }
        });

    }
}