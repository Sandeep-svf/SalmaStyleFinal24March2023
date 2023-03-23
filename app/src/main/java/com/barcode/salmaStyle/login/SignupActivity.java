package com.barcode.salmaStyle.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.barcode.salmaStyle.response.CountryCodeResponse;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.RetroifitErrorResponse.ApiClientNoToken;
import com.barcode.salmaStyle.model.CountryCodeMOdel;
import com.barcode.salmaStyle.model.SignupModel;
import com.barcode.salmaStyle.model.VarifyModel;
import com.barcode.salmaStyle.utol.Originator;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class SignupActivity extends Originator {

    String name="",phone="",email="",password="",str_confirm_pass="";
    EditText edt_name,edt_email,edt_pass,edt_phone,edt_confirm_pass;
    CardView sign_up;
    EditText numOne, numTwo, numThree, numFour, numFive, numSix;
    String emailPattern ="";
    ImageView password_show_old,password_show_new;
    ConstraintLayout signin_listener;
    LinearLayout signup_new,signup_confirm;
    String countr_code="";
    ViewFlipper viewFlipper;
    private FirebaseAuth mAuth;
    AppCompatButton verify_button;
    ImageView salmalogo;
    private boolean isClick = false;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    Spinner sp_country;
    List<String> countryList = new ArrayList<>();
    List<CountryCodeResponse> countryCodeResponseList = new ArrayList<>();
    String mobno="";
    private String verificationId;
    Context context;
    TextView verify_layout;
    String phonewithcountry="";
    private String verifiedData = "No Data";
    private Boolean verifiedFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        edt_name=findViewById(R.id.edt_name);
        edt_email=findViewById(R.id.edt_email);
        verify_button=findViewById(R.id.verify_button);
        edt_pass=findViewById(R.id.edt_password);
        edt_phone=findViewById(R.id.edt_phone);
        edt_confirm_pass=findViewById(R.id.edt_confirm_password);
        sign_up=findViewById(R.id.btn_signin);
        password_show_old=findViewById(R.id.password_show_signup_confirm);
        password_show_new=findViewById(R.id.password_show_signup_new);
        signin_listener=findViewById(R.id.cons_sign_click);
        signup_new=findViewById(R.id.signup_new_pass);
        salmalogo=findViewById(R.id.img_salma);
        signup_confirm=findViewById(R.id.passwordLayout_confirm);
        password_show_new=findViewById(R.id.password_show_signup_new);
        sp_country=findViewById(R.id.spinner_country);
        viewFlipper=findViewById(R.id.view_fkip);
        context=SignupActivity.this;
        mAuth = FirebaseAuth.getInstance();
        verify_layout = findViewById(R.id.Verify_layout);
        Glide.with(this).load(R.drawable.logosalma)
                .thumbnail(0.5f)
                .into(salmalogo);
        verify_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCodes(v);
            }
        });



        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.e("test_sam_check_data",edt_email.getText().toString());
                Log.e("test_sam_check_data",verifiedData);

                if(!edt_email.getText().toString().equals(verifiedData)){
                    verify_button.setText(getResources().getString(R.string.verify));
                    verify_button.setTextColor(getResources().getColor(R.color.red));
                }else{
                    verify_button.setText(getResources().getString(R.string.verified));
                    verify_button.setTextColor(getResources().getColor(R.color.green));
                }

            }
        });


        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                // email verifivation APIS
                if(edt_email.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SignupActivity.this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(!edt_email.getText().toString().matches(emailPattern)){
                    Toast toast = Toast.makeText(SignupActivity.this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else{
                    Log.e("test_sam_otpdata",edt_email.getText().toString()+"ok");
                    String email = edt_email.getText().toString();

                    if(verifiedData.equals(edt_email.getText().toString())){
                        Toast.makeText(SignupActivity.this, getResources().getString(R.string.your_email_already_verified), Toast.LENGTH_SHORT).show();
                    }else{
                        veryfy_email(email);
                    }

                }


            }
        });

       signup_new.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               signup_new.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if (!isClick) {
                           isClick = true;
                           password_show_new.setImageResource(R.drawable.visibility_off_icon);
                           edt_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                           edt_pass.setSelection(edt_pass.getText().length());
                       } else {
                           isClick = false;
                           password_show_new.setImageResource(R.drawable.visibility_icon);
                           edt_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                           edt_pass.setSelection(edt_pass.getText().length());
                       }
                   }
               });

           }
       });

       signup_confirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (!isClick) {
                   isClick = true;
                   password_show_old.setImageResource(R.drawable.visibility_off_icon);
                   edt_confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                   edt_confirm_pass.setSelection(edt_confirm_pass.getText().length());
               } else {
                   isClick = false;
                   password_show_old.setImageResource(R.drawable.visibility_icon);
                   edt_confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                   edt_confirm_pass.setSelection(edt_confirm_pass.getText().length());
               }
           }
       });
        codenumber();
        countryApi();

        signinListener();
        signup_Listener();

    }



    private void verifyCodes(View v) {

        String code = "" + numOne.getText().toString() + numTwo.getText().toString() + numThree.getText().toString() + numFour.getText().toString() + numFive.getText().toString() + numSix.getText().toString();
        // Toast.makeText(SignUpActivity.this, code, Toast.LENGTH_SHORT).show();
        if (!code.equals("")) {

            Log.e("bhu", "running phase 1");
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(verificationId, code);

            signInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(this, "Enter the Correct verification Code", Toast.LENGTH_SHORT).show();
        }

    }

    private void codenumber() {
        numOne = findViewById(R.id.numone);
        numTwo = findViewById(R.id.numtwo);
        numThree = findViewById(R.id.numthree);
        numFour = findViewById(R.id.numfour);
        numFive = findViewById(R.id.numfive);
        numSix = findViewById(R.id.numsix);

        numOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numOne.getText().toString().length() == 0) {
                    numTwo.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        numTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numTwo.getText().toString().length() == 0) {
                    numThree.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numTwo.getText().toString().length() == 0) {
                    numOne.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numTwo.getText().toString().length() == 0) {
                    numOne.requestFocus();
                }
            }
        });

        numThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numThree.getText().toString().length() == 0) {
                    numFour.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numThree.getText().toString().length() == 0) {
                    numTwo.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numThree.getText().toString().length() == 0) {
                    numTwo.requestFocus();
                }

            }
        });

        numFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numFour.getText().toString().length() == 0) {
                    numFive.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numFour.getText().toString().length() == 0) {
                    numThree.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numFour.getText().toString().length() == 0) {
                    numThree.requestFocus();
                }

            }
        });

        numFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numFive.getText().toString().length() == 0) {
                    numSix.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numFive.getText().toString().length() == 0) {
                    numFour.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numFive.getText().toString().length() == 0) {
                    numFour.requestFocus();
                }
            }
        });

        numSix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (numSix.getText().toString().length() == 0) {
                    numFive.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (numSix.getText().toString().length() == 0) {
                    numFive.requestFocus();
                }
            }
        });

    }

    private void countryApi() {
     //   CustomProgressbar.showProgressBar(this, false);
        APIService service = ApiClientNoToken.getClient().create(APIService.class);
        retrofit2.Call<CountryCodeMOdel> call = service.countryList();
        call.enqueue(new Callback<CountryCodeMOdel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CountryCodeMOdel> call, @NonNull retrofit2.Response<CountryCodeMOdel> response) {
            //   CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                         //   Toast.makeText(SignupActivity.this,R.string.user_login_successful, Toast.LENGTH_SHORT).show();
                            countryCodeResponseList=response.body().getCountryCodeResponseList();

                            for(int i=0;i<countryCodeResponseList.size();i++){
                                countryList.add(countryCodeResponseList.get(i).getCountryCode());
                            }

                            sp_country.setAdapter(new ArrayAdapter<String>(SignupActivity.this, R.layout.simple_spinner_row, R.id.rowTextView, countryList));
                            sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    countr_code=countryList.get(position).toString();
                                    Log.e("prov_check","    "+countr_code);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    //  Toasty.error(EditProfileActivity.this, "002255555", Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            Toast.makeText(SignupActivity.this,R.string.failed_error, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                         //   CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignupActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignupActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignupActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignupActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignupActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignupActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignupActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignupActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
              //  CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<CountryCodeMOdel> call, Throwable t) {
              //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signinListener() {

        signin_listener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
               startActivity(intent);
            }
        });

    }

    private void signup_Listener() {

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                name=edt_name.getText().toString();
                phone=edt_phone.getText().toString();
                 phonewithcountry=countr_code+""+phone;
                email=edt_email.getText().toString();
                password=edt_pass.getText().toString();
                str_confirm_pass=edt_confirm_pass.getText().toString();
                if (name.equals("")) {
                    Toast toast = Toast.makeText(SignupActivity.this, getString(R.string.enter_name), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(!email.matches(emailPattern)){
                    Toast toast = Toast.makeText(SignupActivity.this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(password.equals("")){
                    Toast toast = Toast.makeText(SignupActivity.this, getString(R.string.enter_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if(str_confirm_pass.equals("")){
                    Toast toast = Toast.makeText(SignupActivity.this, getString(R.string.enter_confirm_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (!password.equals(str_confirm_pass)) {
                    Toast toast = Toast.makeText(SignupActivity.this, getString(R.string.pls_enter_same_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(phonewithcountry.equals("")){
                    Toast toast = Toast.makeText(SignupActivity.this, getString(R.string.enter_phone), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    checkphoneApi(phonewithcountry);
                }

            }
        });

    }

    private void checkphoneApi(String phonewithcountry) {
        CustomProgressbar.showProgressBar(this, false);

        APIService service = ApiClientNoToken.getClient().create(APIService.class);
        retrofit2.Call<VarifyModel> call = service.varify(phonewithcountry);
        call.enqueue(new Callback<VarifyModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<VarifyModel> call, @NonNull retrofit2.Response<VarifyModel> response) {
                CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(SignupActivity.this,R.string.phone_no_exist, Toast.LENGTH_LONG).show();
                        } else {
                            // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            next_button(viewFlipper);
                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignupActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignupActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignupActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignupActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignupActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignupActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignupActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignupActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void next_button(ViewFlipper viewFlipper) {

        String fbno=countr_code+""+phone;
        sendVerificationCode(fbno);


    }

    private void sendVerificationCode(String mobno) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobno,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            viewFlipper.setDisplayedChild(1);
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            signInWithPhoneAuthCredential(phoneAuthCredential);


            // set code on text view ....

            Log.e("test_sam","->"+code);

            // checking if the code
            // is null or not.

            //verificationId = s;
            //viewFlipper.setDisplayedChild(1);

            if (code != null) {
                //   Forget_Login();
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                //edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                // verifyCode(code);

                        try {
                            int i;
                            for(i = 0; i < code.length(); i++){

                                Log.e("test_sam","i: "+code.charAt(i));

                                if(i == 0){
                                    numOne.setText( String.valueOf(code.charAt(i)));
                                }else if(i == 1){
                                    numTwo.setText(String.valueOf( code.charAt(i)));
                                }else if(i == 2){
                                    numThree.setText( String.valueOf(code.charAt(i)));
                                }else if(i == 3){
                                    numFour.setText(String.valueOf( code.charAt(i)));
                                }else if(i == 4){
                                    numFive.setText( String.valueOf(code.charAt(i)));
                                }else if(i == 5) {
                                    numSix.setText( String.valueOf(code.charAt(i)));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("test_sam","e: "+e);
                        }

            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            //   Toast.makeText(SignUpActivity.this,"Please enter correct no", Toast.LENGTH_LONG).show();

        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"OTP verification successfull",Toast.LENGTH_SHORT).show();

                            //  progressDialog.dismiss();
                            signupApi(phonewithcountry);
                        } else {
                            // progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Wrong OTP !", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void signupApi(String phonewithcountry) {

        CustomProgressbar.showProgressBar(this, false);
        APIService service = ApiClientNoToken.getClient().create(APIService.class);
        retrofit2.Call<SignupModel> call = service.getSignup(name,phonewithcountry,password,email);
        call.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SignupModel> call, @NonNull retrofit2.Response<SignupModel> response) {
                CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(SignupActivity.this,R.string.signup_successfully, Toast.LENGTH_SHORT).show();
                            // sendVerificationCode(newPhoneNumber);
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, R.string.failed_error, Toast.LENGTH_LONG).show();

                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignupActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignupActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignupActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignupActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignupActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignupActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignupActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignupActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<SignupModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void veryfy_email(String email) {

        CustomProgressbar.showProgressBar(this, false);
        APIService service = ApiClientNoToken.getClient().create(APIService.class);
        retrofit2.Call<SignupModel> call = service.verify_email(email);
        call.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SignupModel> call, @NonNull retrofit2.Response<SignupModel> response) {
                CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG).show();

                            // show popup
                            popup();
                        } else {
                            Toast.makeText(SignupActivity.this, R.string.failed_error, Toast.LENGTH_LONG).show();

                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignupActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignupActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignupActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignupActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignupActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignupActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignupActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignupActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<SignupModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


        private void popup() {

            AlertDialog dialogs;


            final LayoutInflater inflater = SignupActivity.this.getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.email_verification_otp_layout, null);
            final TextView continue_button = alertLayout.findViewById(R.id.continue_button);
            final EditText numOne = alertLayout.findViewById(R.id.numone);
            final EditText numTwo = alertLayout.findViewById(R.id.numtwo);
            final EditText numThree = alertLayout.findViewById(R.id.numthree);
            final EditText numFour = alertLayout.findViewById(R.id.numfour);
            final EditText numFive = alertLayout.findViewById(R.id.numfive);
            final EditText numSix = alertLayout.findViewById(R.id.numsix);


            numOne.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (numOne.getText().toString().length() == 0) {
                        numTwo.requestFocus();
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });

            numTwo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (numTwo.getText().toString().length() == 0) {
                        numThree.requestFocus();
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (numTwo.getText().toString().length() == 0) {
                        numOne.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            numThree.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (numThree.getText().toString().length() == 0) {
                        numFour.requestFocus();
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (numThree.getText().toString().length() == 0) {
                        numTwo.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            numFour.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (numFour.getText().toString().length() == 0) {
                        numFive.requestFocus();
                    }

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (numFour.getText().toString().length() == 0) {
                        numThree.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            numFive.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (numFive.getText().toString().length() == 0) {
                        numSix.requestFocus();
                    }

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (numFive.getText().toString().length() == 0) {
                        numFour.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            numSix.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (numFive.getText().toString().length() == 0) {
                        numFour.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            final AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);

            alert.setView(alertLayout);
            alert.setCancelable(false);

            dialogs = alert.create();
            dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogs.show();
            dialogs.setCanceledOnTouchOutside(false);


            continue_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    dialogs.dismiss();
                    // call varification function here......
                    String otpData = ""+numOne.getText().toString()+
                            numTwo.getText().toString()+
                            numThree.getText().toString()+
                            numFour.getText().toString()+
                            numFive.getText().toString()+
                            numSix.getText().toString();

                    Log.e("test_sam_otpdata",otpData+"ok");

                    email_otp_verification_api(otpData);

                }
            });
        }

    private void email_otp_verification_api(String otpData) {

            CustomProgressbar.showProgressBar(this, false);
            APIService service = ApiClientNoToken.getClient().create(APIService.class);
            retrofit2.Call<SignupModel> call = service.verify_email_otp(otpData);
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<SignupModel> call, @NonNull retrofit2.Response<SignupModel> response) {
                    CustomProgressbar.hideProgressBar();

                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success=response.body().getStatus();

                            if (success.equalsIgnoreCase("True")) {
                                Toast.makeText(SignupActivity.this,message, Toast.LENGTH_SHORT).show();
                                // sendVerificationCode(newPhoneNumber);
                                verifiedData = edt_email.getText().toString();
                                verify_button.setText(getResources().getString(R.string.verified));
                                verify_button.setTextColor(getResources().getColor(R.color.green));

                            } else {
                                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG).show();

                            }

                        } else {
                            try {
                                CustomProgressbar.hideProgressBar();
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(SignupActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(SignupActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(SignupActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(SignupActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(SignupActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(SignupActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(SignupActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(SignupActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                public void onFailure(@NonNull Call<SignupModel> call, Throwable t) {
                    CustomProgressbar.hideProgressBar();
                    if (t instanceof IOException) {
                        Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(SignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
}