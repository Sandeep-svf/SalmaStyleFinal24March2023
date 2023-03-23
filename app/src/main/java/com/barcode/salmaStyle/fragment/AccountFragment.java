package com.barcode.salmaStyle.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.barcode.salmaStyle.AppGuidanceActivity;
import com.barcode.salmaStyle.AboutusActivity;
import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.SettingActivity;
import com.barcode.salmaStyle.TermConditionActivity;
import com.barcode.salmaStyle.adapter.AdapterApproved;
import com.barcode.salmaStyle.login.LoginActivity;
import com.barcode.salmaStyle.model.ApprovedProductModel;
import com.barcode.salmaStyle.model.DeleteAccModel;
import com.barcode.salmaStyle.model.LogoutModel;
import com.barcode.salmaStyle.model.NotificationModel;
import com.barcode.salmaStyle.response.NotificationCount;
import com.barcode.salmaStyle.response.NotificationResponse;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AccountFragment extends Fragment {

    ConstraintLayout my_profile, my_setting, my_aboutus, my_term_condition, logout, app_guidance;
    AlertDialog dialogs;
    String refresh = "";
    SharedPreferences sharedPreferences;
    ImageView toolbar;
    TextView frag_tool_text;
    NotificationCount notificationCount;
    List<NotificationResponse> notificationResponseList = null;
    ConstraintLayout delete_account;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        my_profile = view.findViewById(R.id.cons_my_profile);
        my_setting = view.findViewById(R.id.cons_my_setting);
        my_aboutus = view.findViewById(R.id.cons_my_aboutus);
        my_term_condition = view.findViewById(R.id.cons_my_termcondition);
        app_guidance = view.findViewById(R.id.cons_app_guidance);
        logout = view.findViewById(R.id.cons_my_logout);
        delete_account = view.findViewById(R.id.cons_deletete_account);
        frag_tool_text = (TextView) view.findViewById(R.id.frag_tool_text);
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        refresh = sharedPreferences.getString("refresh", "");

        profileListener();
        settingListener();
        aboutusListener();
        termconditionListerner();
        notificationApi();
        appGuideListener();
        logoutListener();
        deleteListener();
        // backListener();
        return view;

    }

    private void deleteListener() {
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.custom_alert_dialog, null);
                final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
                final TextView btnYes = alertLayout.findViewById(R.id.btnd_delete);
                final TextView btnNo = alertLayout.findViewById(R.id.btn_cancel);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(getActivity().getString(R.string.delete_account));
                tvMessage.setText(getActivity().getString(R.string.are_you_sure_to_delete));
                alert.setView(alertLayout);
                alert.setCancelable(false);

                btnYes.setOnClickListener(v -> {
                    dialogs.dismiss();
                    //  relative_logout.setEnabled(true);

                    deleteApi();

                });

                btnNo.setOnClickListener(v -> {
                    dialogs.dismiss();
                    // relative_logout.setEnabled(true);
                });

                dialogs = alert.create();
                dialogs.show();

            }
        });
    }

    private void deleteApi() {
        // CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<DeleteAccModel> call = service.getdeleteAcc();
        call.enqueue(new Callback<DeleteAccModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<DeleteAccModel> call, @NonNull retrofit2.Response<DeleteAccModel> response) {
                //  CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(getActivity(), R.string.delete_account_successfully, Toast.LENGTH_SHORT).show();
                            // sendVerificationCode(newPhoneNumber);
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                            Toast.makeText(getActivity(), R.string.failed_error, Toast.LENGTH_LONG).show();

                        }

                    } else {
                        try {
                            //   CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<DeleteAccModel> call, Throwable t) {
                //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void appGuideListener() {
        app_guidance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppGuidanceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void notificationApi() {

        //   CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<NotificationModel> call = service.get_notification();
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<NotificationModel> call, @NonNull retrofit2.Response<NotificationModel> response) {
                //    CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getStatus();
                        // notificationResponseList=response.body().getNotificationResponseList();
                        if (success.equalsIgnoreCase("True")) {
                            //   Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            // sendVerificationCode(newPhoneNumber);

                            notificationCount = response.body().getNotificationCount();
                            ((MainActivity) getActivity()).txtNotification.setText("" + notificationCount.getMessageCount());
                            notificationResponseList = response.body().getNotificationResponseList();

                        } else {
                            if (notificationResponseList == null) {
                                ((MainActivity) getActivity()).txtNotification.setText("0");
                            } else {

                                ((MainActivity) getActivity()).txtNotification.setText("0");
                            }
                            ((MainActivity) getActivity()).txtNotification.setText("0");
                        }

                    } else {
                        try {
                            //  CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
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
                // ((MainActivity) getActivity()).txtNotification.setText("0");
                CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<NotificationModel> call, Throwable t) {
                //   CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void logoutListener() {

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout_AlertDialog();
            }
        });

    }

    private void Logout_AlertDialog() {

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dialog, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnYes = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btnNo = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getActivity().getString(R.string.confirm_logout));
        tvMessage.setText(getActivity().getString(R.string.are_you_sure_to_logout));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btnYes.setOnClickListener(v -> {
            dialogs.dismiss();
            //  relative_logout.setEnabled(true);

            logoutApi();

        });

        btnNo.setOnClickListener(v -> {
            dialogs.dismiss();
            // relative_logout.setEnabled(true);
        });

        dialogs = alert.create();
        dialogs.show();

    }

    private void logoutApi() {

        CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<LogoutModel> call = service.logout(refresh);
        call.enqueue(new Callback<LogoutModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<LogoutModel> call, @NonNull retrofit2.Response<LogoutModel> response) {
                CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(getActivity(), R.string.logout_success, Toast.LENGTH_SHORT).show();

                            SharedPreferences preferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
                            preferences.edit().remove("refresh").apply();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                            Toast.makeText(getActivity(), R.string.logout_failed, Toast.LENGTH_LONG).show();

                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<LogoutModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void termconditionListerner() {

        my_term_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TermConditionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void aboutusListener() {

        my_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutusActivity.class);
                startActivity(intent);
            }
        });

    }

    private void settingListener() {

        my_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

    }

    private void profileListener() {

        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

}
