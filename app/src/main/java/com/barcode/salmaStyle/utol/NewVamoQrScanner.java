package com.barcode.salmaStyle.utol;


import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission_group.CAMERA;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.ScanProductDetailActivity;
import com.barcode.salmaStyle.response.ProductScanResponse;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.model.ProductScanModel;
import com.barcode.salmaStyle.uploadimagescreen.ProductImageFragment;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class NewVamoQrScanner  extends AppCompatActivity {
    ProductScanResponse productScanResponse;
    boolean ischecked;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private ScannerLiveView camera;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    NewVamoQrScanner context;
    private TextView scannedTV;
    private String qrCode;
    String vamo_key="",user_id="";
   public static String bar_code="";
    String barcode="";
    SharedPreferences sharedPreferences;
    AlertDialog dialogs;
    String s="";
    String img1="",img2="",img3="",name="",status="",date="";
    String get_value="";
    TextView toolbar_text;
    ConstraintLayout back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        camera = findViewById(R.id.activity_main_previewView);
        scannedTV = findViewById(R.id.idTVscanned);
        toolbar_text=findViewById(R.id.toolbar_text);
        back=findViewById(R.id.cons_tool_img);
        context = NewVamoQrScanner.this;
        //vamo_key=getIntent().getStringExtra("vamo");
        toolbar_text.setVisibility(View.GONE);
        ischecked = Boolean.parseBoolean(getIntent().getStringExtra("ischecked"));
        get_value=getIntent().getStringExtra("send_value");
        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_ID", "");
        barcode = "436000291455";

        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                // method is called when scanner is started
                // Toast.makeText(ActivityQrcodeScanner.this, "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                // method is called when scanner is stopped.
                //   Toast.makeText(ActivityQrcodeScanner.this, "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                // method is called when scanner gives some error.
                //  Toast.makeText(ActivityQrcodeScanner.this, "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {
                // method is called when camera scans the.
                // qr code and the data from qr code is
                Log.e("ischeck_value", "           " + data);

                // stored in data in string format.
                camera.setPlaySound(false);
                camera.stopScanner();

                bar_code = data;
                Log.e("data_check", "           " + data);
                scanproductApi();

            }
        });

        scannedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), qrCode, Toast.LENGTH_SHORT).show();

            }
        });
        backListener();
    }

    private void backListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void alert_dialogue() {

        final LayoutInflater inflater = NewVamoQrScanner.this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dialog, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnYes = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btnNo = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(NewVamoQrScanner.this.getString(R.string.product_unavailable));
        tvMessage.setText(NewVamoQrScanner.this.getString(R.string.want_send_photos));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btnYes.setOnClickListener(v -> {
            dialogs.dismiss();
            Intent intent=new Intent(this,ProductImageFragment.class);
            startActivity(intent);
        });

        btnNo.setOnClickListener(v -> {
            dialogs.dismiss();
            finish();
        });

        dialogs = alert.create();
        dialogs.show();

    }

    private void scanproductApi() {

        CustomProgressbar.showProgressBar(this, false);

        Log.e("barcode", "      "+barcode);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ProductScanModel> call = service.productscan(bar_code);
        call.enqueue(new Callback<ProductScanModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProductScanModel> call, @NonNull retrofit2.Response<ProductScanModel> response) {
               CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {

                            if(message.equals("Please View Your Product Details") || message.equals("This Product has has not clear photos to review. Please wait for Review by Salma Style")) {
                                Log.e("product_detail", "      ");
                              //  Toast.makeText(getActivity(), R.string.view_profile_detal, Toast.LENGTH_SHORT).show();

                                productScanResponse = response.body().getProductScanResponse();
                                name = productScanResponse.getBrand();
                                status = productScanResponse.getStatus();
                                date = productScanResponse.getCreated_at();

                                img1 = String.valueOf(productScanResponse.getProduct_image());
                                img2 = String.valueOf(productScanResponse.getIngredients_image());
                                img3 = String.valueOf(productScanResponse.getNutrition_facts_image());


                                SharedPreferences.Editor editor = sharedPreferences.edit();
                               // Log.e("id", "    " + productScanResponse.getId());
                                editor.putString("id_pending", String.valueOf( productScanResponse.getId()));
                                editor.apply();

                                Intent intent = new Intent(NewVamoQrScanner.this, ScanProductDetailActivity.class);
                                intent.putExtra("image1", img1);
                                intent.putExtra("image2", img2);
                                intent.putExtra("image3", img3);
                                intent.putExtra("status", status);
                                intent.putExtra("comment_english", productScanResponse.getReview());
                                intent.putExtra("comment_farsi", productScanResponse.getمرور());
                                context.startActivity(intent);
                            }else if(message.equals("This Product is in Pending. Please wait for Review by Salma Style")){
                                Log.e("pending","      ");
                                Toast.makeText(NewVamoQrScanner.this, R.string.this_product_pending, Toast.LENGTH_LONG).show();
                        }
                        }
                        else {

                            if(message.equalsIgnoreCase("This Product Is Not In The Salma Style Currently. Do You Want To Add?")){
                                Log.e("new","      ");
                                Toast.makeText(NewVamoQrScanner.this, R.string.this_has_not_scaned, Toast.LENGTH_LONG).show();
                                alert_dialogue();

                            }else {
                                Log.e("else","      ");
                                Toast.makeText(NewVamoQrScanner.this, R.string.failed_error, Toast.LENGTH_LONG).show();
                            }


                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(NewVamoQrScanner.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(NewVamoQrScanner.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(NewVamoQrScanner.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(NewVamoQrScanner.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(NewVamoQrScanner.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(NewVamoQrScanner.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(NewVamoQrScanner.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(NewVamoQrScanner.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                Log.e("pending1","      ");
                CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<ProductScanModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(NewVamoQrScanner.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(NewVamoQrScanner.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private final ArrayList<String> requirePermissions = new ArrayList<>();
    private final ArrayList<String> permissionsToRequest = new ArrayList<>();
    private final ArrayList<String> permissionsRejected = new ArrayList<>();
    private void askPermissions() {
        requirePermissions.add(Manifest.permission.CAMERA);
        //   requirePermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requirePermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        requirePermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        //  requirePermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        requirePermissions.add(Manifest.permission.USE_BIOMETRIC);
        requirePermissions.add(Manifest.permission.USE_FINGERPRINT);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(NewVamoQrScanner.this,  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add( Manifest.permission.CAMERA);
            }
        }else {
            if (ContextCompat.checkSelfPermission(NewVamoQrScanner.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
// here, Permission is not granted
                ActivityCompat.requestPermissions(NewVamoQrScanner.this, new String[] {android.Manifest.permission.CAMERA}, 50);
            }
        }




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                //ActivityCompat.requestPermissions(context, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                ActivityCompat.requestPermissions(NewVamoQrScanner.this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                launchLocation();
            }
        } else {
            launchLocation();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        // 0.5 is the area where we have
        // to place red marker for scanning.
        decoder.setScanAreaPercent(0.8);
        // below method will set secoder to camera.
        camera.setDecoder(decoder);
        camera.startScanner();
        camera.setPlaySound(false);
    }

    @Override
    public void onPause() {
        // on app pause the
        // camera will stop scanning.
        camera.stopScanner();
        super.onPause();
    }

    private boolean checkPermission() {
        // here we are checking two permission that is vibrate
        // and camera which is granted by user and not.
        // if permission is granted then we are returning
        // true otherwise false.
        int camera_permission = ContextCompat.checkSelfPermission(NewVamoQrScanner.this.getApplicationContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(NewVamoQrScanner.this.getApplicationContext(), VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        // this method is to request
        // the runtime permission.
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(NewVamoQrScanner.this, new String[]{CAMERA, VIBRATE}, PERMISSION_REQUEST_CODE);
    }

    private void launchLocation() {
        GPSTracker gps = new GPSTracker(NewVamoQrScanner.this);
        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
        } else {

        }
    }

}