package com.barcode.salmaStyle.login;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.TokenResponse;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.RetroifitErrorResponse.ApiClientNoToken;

import com.barcode.salmaStyle.model.LoginModel;
import com.barcode.salmaStyle.utol.Originator;
import com.barcode.salmaStyle.utol.SharedPrefClass;
import com.barcode.salmaStyle.utol.Singleton;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends Originator {

    ConstraintLayout sign_up;
    TextView forget;
    CardView sign_in;
    EditText edt_email,edt_password;
    String email="",password="";
    TokenResponse tokenResponse;
    SharedPreferences sharedPreferences;
    ImageView password_show_login;
    String id="";
    TextView lang_txt;
    SharedPrefClass sharedPrefClass;
    private boolean isClick = false;
    LinearLayout passwordLayout_login;
    AlertDialog dialogs;
    private Context context;
   public static String token="",refresh="";
   public static String shared_token="";
    RadioButton radio_farsi,radio_english;
    String str_lanuage = "1";
    SharedPreferences sharedPreferencesLanguageName;
    Singleton singleton = Singleton.getInstance();
    private Context context1;
    public static final  String LANGUAGE_ENGLISH   = "en";
    public static final String LANGUAGE_PERSIAN       = "ps";
    String token_firebase="";
    boolean isFinsihActivity = false;
    ImageView salmaLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPrefClass=new SharedPrefClass(this);
        context1 = LoginActivity.this;
        sign_up=findViewById(R.id.cons_signup);
        forget=findViewById(R.id.txt_forgot);
        sign_in=findViewById(R.id.btn_signin);
        salmaLogo=findViewById(R.id.img_salma);
        password_show_login=findViewById(R.id.password_show);
        edt_email=findViewById(R.id.edt_email);
        edt_password=findViewById(R.id.edt_password);
        passwordLayout_login = findViewById(R.id.passwordLayout_login);
        radio_farsi=findViewById(R.id.rdo_individual_farsi);
        radio_english=findViewById(R.id.rdo_individual_eng);
        //  password_show_login.setImageResource(R.drawable.visibility_off_icon);
        Glide.with(this).load(R.drawable.logosalma)
                .thumbnail(0.5f)
                .into(salmaLogo);
        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        context=LoginActivity.this;
        initt();

        sharedPreferencesLanguageName = getSharedPreferences("LANGUAGE_NAME", Context.MODE_PRIVATE);
        if (sharedPreferencesLanguageName.getString("language_text", "").equals("en")) {
            radio_english.setText(getString(R.string.english));
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ps")) {
            radio_farsi.setText(getString(R.string.farsi));
        }
        edt_email.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        singleton.language_name = sharedPreferencesLanguageName.getString("language_text", "");

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            radio_farsi.setChecked(true);
            Log.e("valuecheck","spanish    " +sharedPrefClass.getString(SharedPrefClass.LANGUAGE));
        } else  {
            radio_english.setChecked(true);
        }


        radiofarsiListener();
        radioenglishListener();
        passwordlayListener();

        signupListener();
        forgetListener();
        signinListener();
    }

    private void initt() {

      /*  FirebaseApp.getApps(context1);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            //   Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token_firebase = task.getResult();
                        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token_firebase);
                        editor.apply();
                        Log.e("tokens", "tok12   " + token_firebase);
                        // Log and toast

                    }
                });*/

        FirebaseApp.getApps(context);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                success(instanceIdResult);
            }

            private void success(InstanceIdResult instanceIdResult) {
                // Get new Instance ID token
                token_firebase = instanceIdResult.getToken();

                sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", token_firebase);
                editor.apply();
                Log.e("tokens", "tok12   " + token_firebase);

                Log.e("tokens1", "tok" + token_firebase);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("tokens", "tok" + e.getMessage());

                if (Build.VERSION_CODES.KITKAT > Build.VERSION.SDK_INT) {
                    failure();
                } else {
                    token_firebase = "123456";
                }
            }

            private void failure() {
                initt();
            }
        });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);


    }

    private void radioenglishListener() {

        radio_english.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){

                    radio_farsi.setChecked(false);
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGE,"en");
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGEVARIABLE,"en");

                    Locale locale2 = new Locale("en");
                    Locale.setDefault(locale2);
                    Configuration config2 = new Configuration();
                    config2.locale = locale2;
                    getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());

                    str_lanuage = "1";
                    singleton.language_name = "en";

                    sharedPreferencesLanguageName = getSharedPreferences("LANGUAGE_NAME", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
                    editor.putString("language_text", "en");
                    editor.putString("language_id", str_lanuage);
                    editor.apply();

                    Intent intents = new Intent(context, SplashActivity.class);
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intents);

                }else {
                    radio_farsi.setChecked(true);
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGE,"ps");
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGEVARIABLE,"ps");
                }
            }
        });


    }

    private void radiofarsiListener() {

        radio_farsi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){

                    radio_english.setChecked(false);
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGE,"ps");
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGEVARIABLE,"es");

                    Locale locale2 = new Locale("ps");
                    Locale.setDefault(locale2);
                    Configuration config2 = new Configuration();
                    config2.locale = locale2;
                    getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());

                    str_lanuage = "2";
                    singleton.language_name = "ps";
                    sharedPreferencesLanguageName = getSharedPreferences("LANGUAGE_NAME", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
                    editor.putString("language_text", "ps");
                    editor.putString("language_id", str_lanuage);
                    editor.apply();

                    Intent intents = new Intent(context, SplashActivity.class);
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intents);

                }else {
                    radio_english.setChecked(true);
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGE,"en");
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGEVARIABLE,"en");
                }
            }
        });

    }




    private void passwordlayListener() {

        passwordLayout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClick) {
                    isClick = true;
                    password_show_login.setImageResource(R.drawable.visibility_off_icon);
                    edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_password.setSelection(edt_password.getText().length());
                } else {
                    isClick = false;
                    password_show_login.setImageResource(R.drawable.visibility_icon);
                    edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_password.setSelection(edt_password.getText().length());
                }
            }
        });

    }

    private void signinListener() {

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_email.setSelectAllOnFocus(true);
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
               email=edt_email.getText().toString();
               password=edt_password.getText().toString();

                if(!email.matches(emailPattern)){
                    Toast toast = Toast.makeText(LoginActivity.this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(password.equals("")){
                    Toast toast = Toast.makeText(LoginActivity.this, getString(R.string.enter_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                     signinApi();
                }
            }
        });

    }

    private void signinApi() {

        CustomProgressbar.showProgressBar(this, false);

        APIService service = ApiClientNoToken.getClient().create(APIService.class);
        retrofit2.Call<LoginModel> call = service.getLogin(password,email,token_firebase);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<LoginModel> call, @NonNull retrofit2.Response<LoginModel> response) {
                CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(LoginActivity.this,R.string.user_login_successful, Toast.LENGTH_SHORT).show();

                            tokenResponse=response.body().getTokenResponse();
                            token=tokenResponse.getAccess();
                            refresh=tokenResponse.getRefresh();
                            sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();


                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            editor.putString("token", token);
                            editor.putString("id_key", id);
                            editor.putString("refresh", refresh);
                            editor.apply();
                            startActivity(intent);



                            finish();
                        } else {
                           // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this,R.string.user_login_failed, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(LoginActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(LoginActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(LoginActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(LoginActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(LoginActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(LoginActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(LoginActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<LoginModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(LoginActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(LoginActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void forgetListener() {

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signupListener() {

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFinsihActivity) {
            super.onBackPressed();
        }
        isFinsihActivity = true;
    }

}