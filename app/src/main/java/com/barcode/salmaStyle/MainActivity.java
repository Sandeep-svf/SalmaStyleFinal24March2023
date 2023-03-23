package com.barcode.salmaStyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barcode.salmaStyle.fragment.AccountFragment;
import com.barcode.salmaStyle.fragment.DashboardFragment;
import com.barcode.salmaStyle.fragment.NotificationFragment;
import com.barcode.salmaStyle.fragment.ScanProductFragment;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.model.NotificationModel;
import com.barcode.salmaStyle.response.NotificationCount;
import com.barcode.salmaStyle.uploadimagescreen.ProductImageFragment;
import com.barcode.salmaStyle.utol.Originator;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public  class MainActivity extends Originator {

    ImageView btnMenuImage;
    public static int navItemIndex = 0;
    private static final String TAG_DASHBOARD = "services";
    public static String CURRENT_TAG = TAG_DASHBOARD;
    ImageView qr_code, home, notification, profile;
    RelativeLayout rel_dashboard, rel_scan_products, rel_aboutus, rel_setting, rel_term_condition, notification_lay, relative_logout;
    public TextView txtNotification;
    SharedPreferences sharedPreferences;
    public static String count = "";
    String refresh = "";
    public static String token = "";
    AlertDialog dialogs;
    TextView frag_tool_text;
    boolean doubleBackToExitPressedOnce = false;
    String frag_not_key = "";
    public static NotificationCount notificationCount;

    // ImageView toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // setBehindContentView(R.layout.activity_menu);

        //  btnMenuImage = (ImageView) findViewById(R.id.toolbar);
        qr_code = (ImageView) findViewById(R.id.bottom_nav_qr);
        home = (ImageView) findViewById(R.id.bottom_nav_home);
        notification = (ImageView) findViewById(R.id.nav_bottom_notification);
        profile = (ImageView) findViewById(R.id.profile_bottom_lay);
        rel_dashboard = (RelativeLayout) findViewById(R.id.relative_dashboard);
        rel_scan_products = (RelativeLayout) findViewById(R.id.relative_my_services);
        rel_term_condition = (RelativeLayout) findViewById(R.id.relative_notification);
        rel_aboutus = (RelativeLayout) findViewById(R.id.relative_order_history);
        rel_setting = (RelativeLayout) findViewById(R.id.relative_my_transaction);
        notification_lay = (RelativeLayout) findViewById(R.id.bellLay);
        relative_logout = (RelativeLayout) findViewById(R.id.relative_logout);
        frag_tool_text = (TextView) findViewById(R.id.frag_tool_text);
        txtNotification = findViewById(R.id.text_notification);
        // toolbar=findViewById(R.id.toolbar);
        sharedPreferences = MainActivity.this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        refresh = sharedPreferences.getString("refresh", "");
        token = sharedPreferences.getString("token", "");
        setupLeftNavigationView();
        NotificationApi();
    }

    private void NotificationApi() {
        //  CustomProgressbar.showProgressBar(this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<NotificationModel> call = service.get_notification();
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<NotificationModel> call, @NonNull retrofit2.Response<NotificationModel> response) {
                //   CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getStatus();
                        if (success.equalsIgnoreCase("True")) {
                            notificationCount = response.body().getNotificationCount();
                            Log.e("count_text_main", "     " + notificationCount.getMessageCount());
                            txtNotification.setText("" + notificationCount.getMessageCount());
                        } else {
                            txtNotification.setText("0");
                            // Toast.makeText(getActivity(), R.string.failed_error, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            //   CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(MainActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(MainActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(MainActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(MainActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(MainActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(MainActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(MainActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<NotificationModel> call, Throwable t) {
                //   CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(MainActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupLeftNavigationView() {
        String terms_key = getIntent().getStringExtra("login_key");
        if (terms_key.equals("login_value")) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            frag_tool_text.setText(R.string.dashboard);
            //  toolbar.setVisibility(View.INVISIBLE);
            qr_code.setColorFilter(ContextCompat.getColor(this, R.color.green));
            home.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            notification.setColorFilter(ContextCompat.getColor(this, R.color.green));
            profile.setColorFilter(ContextCompat.getColor(this, R.color.green));
            DashboardFragment dashBoardFragment = new DashboardFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            // ((Fragment) findViewById(R.id.nav_host_fragment)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, dashBoardFragment, CURRENT_TAG);
            fragmentTransaction.commit();
        }

        if (terms_key.equals("login_value_fragment")) {
            qr_code.setColorFilter(ContextCompat.getColor(this, R.color.green));
            home.setColorFilter(ContextCompat.getColor(this, R.color.green));
            notification.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            profile.setColorFilter(ContextCompat.getColor(this, R.color.green));
            frag_tool_text.setText(getString(R.string.notifications));
            //appTitleTxt.setText(getString(R.string.smart_farms));
            NotificationFragment notificationFragment = new NotificationFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            // ((Fragment) findViewById(R.id.nav_host_fragment)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, notificationFragment, CURRENT_TAG);
            fragmentTransaction.commit();
        }

        if (terms_key.equals("productimage_fragment")) {
            Intent intent = new Intent(MainActivity.this, ProductImageFragment.class);
            startActivity(intent);
        }


        qr_code.setOnClickListener(view -> {
            qr_code.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            home.setColorFilter(ContextCompat.getColor(this, R.color.green));
            notification.setColorFilter(ContextCompat.getColor(this, R.color.green));
            profile.setColorFilter(ContextCompat.getColor(this, R.color.green));
            frag_tool_text.setText(getString(R.string.scan_prod));
            //  finger_identification.setBackgroundColor(Color.parseColor("#EA3270"));
            ScanProductFragment scanProductFragment = new ScanProductFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            //  ((RelativeLayout) findViewById(R.id.fragment_contaner)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.nav_host_fragment, scanProductFragment, CURRENT_TAG);
            fragmentTransaction.commit();
        });


        home.setOnClickListener(view -> {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            qr_code.setColorFilter(ContextCompat.getColor(this, R.color.green));
            home.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            notification.setColorFilter(ContextCompat.getColor(this, R.color.green));
            profile.setColorFilter(ContextCompat.getColor(this, R.color.green));
            // toolbar.setVisibility(View.INVISIBLE);
            frag_tool_text.setText(R.string.dashboard);
            //appTitleTxt.setText(getString(R.string.smart_farms));
            DashboardFragment dashBoardFragment = new DashboardFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            // ((Fragment) findViewById(R.id.nav_host_fragment)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, dashBoardFragment, CURRENT_TAG);
            fragmentTransaction.commit();

        });


        notification.setOnClickListener(view -> {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            qr_code.setColorFilter(ContextCompat.getColor(this, R.color.green));
            home.setColorFilter(ContextCompat.getColor(this, R.color.green));
            notification.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            profile.setColorFilter(ContextCompat.getColor(this, R.color.green));
            frag_tool_text.setText(getString(R.string.notifications));
            //appTitleTxt.setText(getString(R.string.smart_farms));
            NotificationFragment notificationFragment = new NotificationFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            // ((Fragment) findViewById(R.id.nav_host_fragment)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, notificationFragment, CURRENT_TAG);
            fragmentTransaction.commit();
        });

        notification_lay.setOnClickListener(view -> {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            frag_tool_text.setText(getString(R.string.notifications));
            qr_code.setColorFilter(ContextCompat.getColor(this, R.color.green));
            home.setColorFilter(ContextCompat.getColor(this, R.color.green));
            notification.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            profile.setColorFilter(ContextCompat.getColor(this, R.color.green));
            NotificationFragment notificationFragment = new NotificationFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            // ((Fragment) findViewById(R.id.nav_host_fragment)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, notificationFragment, CURRENT_TAG);
            fragmentTransaction.commit();
        });


        profile.setOnClickListener(view -> {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            qr_code.setColorFilter(ContextCompat.getColor(this, R.color.green));
            home.setColorFilter(ContextCompat.getColor(this, R.color.green));
            notification.setColorFilter(ContextCompat.getColor(this, R.color.green));
            profile.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            //  toolbar.setVisibility(View.VISIBLE);
            frag_tool_text.setText(getString(R.string.my_account));
            //appTitleTxt.setText(getString(R.string.smart_farms));
            AccountFragment accountFragment = new AccountFragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            // ((Fragment) findViewById(R.id.nav_host_fragment)).removeAllViews();
            androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, accountFragment, CURRENT_TAG);
            fragmentTransaction.commit();
        });

    }

    @Override
    public void onBackPressed() {
        boolean shouldLoadHomeFragOnBackPress = true;
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_DASHBOARD;
                loadHomeFragment();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
                return;
            }
        }
    }

    private void loadHomeFragment() {
        navItemIndex = 0;
        CURRENT_TAG = TAG_DASHBOARD;
        qr_code.setColorFilter(ContextCompat.getColor(this, R.color.green));
        home.setColorFilter(ContextCompat.getColor(this, R.color.orange));
        notification.setColorFilter(ContextCompat.getColor(this, R.color.green));
        profile.setColorFilter(ContextCompat.getColor(this, R.color.green));
        // toolbar.setVisibility(View.INVISIBLE);
        frag_tool_text.setText(R.string.dashboard);
        //appTitleTxt.setText(getString(R.string.smart_farms));
        DashboardFragment dashBoardFragment = new DashboardFragment();
        androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
        // ((Fragment) findViewById(R.id.nav_host_fragment)).removeAllViews();
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, dashBoardFragment, CURRENT_TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        // NotificationApi();
        super.onStop();
    }

}