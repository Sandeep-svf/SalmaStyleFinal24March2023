package com.barcode.salmaStyle.login;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.RetroifitErrorResponse.ApiClientNoToken;
import com.barcode.salmaStyle.response.CountryCodeResponse;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.utol.Originator;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.model.CountryCodeMOdel;
import com.barcode.salmaStyle.model.VarifyModel;
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

import retrofit2.Call;
import retrofit2.Callback;

public class ForgetPasswordActivity extends Originator {

    EditText edt_phone;
    String str_email="";
    CardView forgot;
    private FirebaseAuth mAuth;
    TextView verify_layout;
    EditText numOne, numTwo, numThree, numFour, numFive, numSix;
    String countr_code="";
    String mobno="";
    private String verificationId;
    ViewFlipper viewFlipper;
    Context context;
    Spinner sp_country;
    List<String> countryList = new ArrayList<>();
    List<CountryCodeResponse> countryCodeResponseList = new ArrayList<>();
    String phone_countrycode="";
    ImageView salmaLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        edt_phone=findViewById(R.id.edt_phone_forgot);
        forgot=findViewById(R.id.btn_signin_forgot);
        viewFlipper=findViewById(R.id.viewflipper_forget);
        verify_layout = findViewById(R.id.Verify_layout);
        sp_country=findViewById(R.id.spinner_country);
        salmaLogo=findViewById(R.id.img_salma);
        context=ForgetPasswordActivity.this;
        mAuth = FirebaseAuth.getInstance();
        Glide.with(this).load(R.drawable.logosalma)
                .thumbnail(0.5f)
                .into(salmaLogo);
        countryApi();
        forgotListener();
        codenumber();
        initt();
        verify_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCodes(v);
            }
        });


       // String[] proviance_array = {"+1", "+98","+91","+971","+973","+965"};

    }

    private void countryApi() {


    //    CustomProgressbar.showProgressBar(this, false);

        APIService service = ApiClientNoToken.getClient().create(APIService.class);
        retrofit2.Call<CountryCodeMOdel> call = service.countryList();
        call.enqueue(new Callback<CountryCodeMOdel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CountryCodeMOdel> call, @NonNull retrofit2.Response<CountryCodeMOdel> response) {
            //  CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                           // Toast.makeText(ForgetPasswordActivity.this,R.string.user_login_successful, Toast.LENGTH_SHORT).show();
                            countryCodeResponseList=response.body().getCountryCodeResponseList();

                           for(int i=0;i<countryCodeResponseList.size();i++){
                               countryList.add(countryCodeResponseList.get(i).getCountryCode());
                           }

                            sp_country.setAdapter(new ArrayAdapter<String>(ForgetPasswordActivity.this, R.layout.simple_spinner_row, R.id.rowTextView, countryList));
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
                            Toast.makeText(ForgetPasswordActivity.this,R.string.failed_error, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        try {
                        //    CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ForgetPasswordActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ForgetPasswordActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ForgetPasswordActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ForgetPasswordActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ForgetPasswordActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ForgetPasswordActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ForgetPasswordActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ForgetPasswordActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
             //   CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<CountryCodeMOdel> call, Throwable t) {
              //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(ForgetPasswordActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ForgetPasswordActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initt() {




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

    private void forgotListener() {

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                mobno=edt_phone.getText().toString();
                 phone_countrycode=countr_code+""+mobno;
               // Log.e("mob_no","       "+fbno);
                varifyphoneApi(phone_countrycode);
            }
        });
    }

    private void varifyphoneApi(String phone_countrycode) {

        CustomProgressbar.showProgressBar(this, false);

        APIService service = ApiClientNoToken.getClient().create(APIService.class);
        retrofit2.Call<VarifyModel> call = service.varify(phone_countrycode);
        call.enqueue(new Callback<VarifyModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<VarifyModel> call, @NonNull retrofit2.Response<VarifyModel> response) {
              CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                           // Toast.makeText(ForgetPasswordActivity.this,R.string.user_login_successful, Toast.LENGTH_SHORT).show();
                            next_button(viewFlipper);
                        } else {
                            // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            Toast.makeText(ForgetPasswordActivity.this,R.string.phone_not_exist, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ForgetPasswordActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ForgetPasswordActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ForgetPasswordActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ForgetPasswordActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ForgetPasswordActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ForgetPasswordActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ForgetPasswordActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ForgetPasswordActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ForgetPasswordActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ForgetPasswordActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void next_button(ViewFlipper viewFlipper) {
        String fbno=countr_code+""+mobno;
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
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(ForgetPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            //   Toast.makeText(SignUpActivity.this,"Please enter correct no", Toast.LENGTH_LONG).show();

        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Intent intent = new Intent(ForgetPasswordActivity.this, ForgetChangePasswordActivity.class);
                            //intent.putExtra("login_key", "login_value");
                            intent.putExtra("mob_no_country",phone_countrycode);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"OTP verification successfull",Toast.LENGTH_SHORT).show();

                            //  progressDialog.dismiss();

                        } else {
                            // progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Wrong OTP !", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}