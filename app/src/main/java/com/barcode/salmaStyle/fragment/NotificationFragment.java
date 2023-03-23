package com.barcode.salmaStyle.fragment;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.barcode.salmaStyle.NotificationDelete;
import com.barcode.salmaStyle.adapter.NotificationsListAdapter;
import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.model.ClearNotificationModel;
import com.barcode.salmaStyle.model.NotificationDeleteModel;
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

public class NotificationFragment extends Fragment implements NotificationDelete {

    NotificationsListAdapter notificationsAdapter;
    RecyclerView notificationRecyclerView;
    AlertDialog dialogs;
    List<NotificationResponse> notificationResponseList = null;
    RelativeLayout clear_all;
    NotificationCount notificationCount;
    NotificationDelete notificationDelete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        notificationRecyclerView=view.findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        clear_all=view.findViewById(R.id.Clear_btn_layout);
        notificationDelete=NotificationFragment.this;
        notificationRecyclerView.setHasFixedSize(true);
        clear_all.setVisibility(View.GONE);
        clearNotificationListener();
        notificationApi();
        return view;

    }

    private void clearNotificationListener() {

        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeleteAllNotification_AlertDialog();

            }
        });
    }

    private void DeleteAllNotification_AlertDialog() {

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dialog, null);

        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnDelete = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btncancel = alertLayout.findViewById(R.id.btn_cancel);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.delete_notification));
        tvMessage.setText(getString(R.string.Are_You_sure_delete_all_notification));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btncancel.setOnClickListener(v -> {
            dialogs.dismiss();
        });
        btnDelete.setOnClickListener(v -> {
            clearallApi();
            dialogs.dismiss();

        });

        dialogs = alert.create();
        dialogs.show();

    }

    private void clearallApi() {

      //  CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ClearNotificationModel> call = service.clearNotifications("1");

        call.enqueue(new Callback<ClearNotificationModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ClearNotificationModel> call, @NonNull retrofit2.Response<ClearNotificationModel> response) {
             //  CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(getActivity(), R.string.notification_clear, Toast.LENGTH_SHORT).show();
                            notificationResponseList.removeAll(notificationResponseList);
                            // notify();
                            notificationApi();

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
             //   CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<ClearNotificationModel> call, Throwable t) {
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

    private void notificationApi() {

     //   CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<NotificationModel> call = service.get_notification();

        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<NotificationModel> call, @NonNull retrofit2.Response<NotificationModel> response) {
            //   CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            //   Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            // sendVerificationCode(newPhoneNumber);
                            notificationResponseList=response.body().getNotificationResponseList();
                            notificationCount=response.body().getNotificationCount();
                            Log.e("notification_list_size","   "+notificationCount.getMessageCount());
                            ((MainActivity) getActivity()).txtNotification.setText(""+notificationCount.getMessageCount());
                            notificationsAdapter = new NotificationsListAdapter(getActivity(),notificationResponseList,notificationDelete);
                            notificationRecyclerView.setAdapter(notificationsAdapter);
                            clear_all.setVisibility(View.VISIBLE);
                        } else {
                            if (notificationResponseList.size() == 0) {
                                ((MainActivity) getActivity()).txtNotification.setText("0");
                                Log.e("notifiction_list_null","   ");
                                notificationRecyclerView.setVisibility(View.GONE);
                                clear_all.setVisibility(View.GONE);
                            } else {
                                Log.e("notifiction_not_null","   ");
                                ((MainActivity) getActivity()).txtNotification.setText("0");
                                notificationRecyclerView.setVisibility(View.VISIBLE);
                                clear_all.setVisibility(View.VISIBLE);
                            }
                            // Toast.makeText(getActivity(), R.string.failed_error, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        try {
                        //    CustomProgressbar.hideProgressBar();
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
             //   CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<NotificationModel> call, Throwable t) {
            //    CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    if(getActivity()!=null){
                        Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(getActivity()!=null){
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    @Override
    public void onItemClick(int position, String id) {

        deleteApi(position,id);
    }

    private void deleteApi(int position, String id) {

       // CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<NotificationDeleteModel> call = service.get_notificationdelete(id);
        call.enqueue(new Callback<NotificationDeleteModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<NotificationDeleteModel> call, @NonNull retrofit2.Response<NotificationDeleteModel> response) {
             // CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(getActivity(), R.string.notification_delete,Toast.LENGTH_LONG).show();
                            notificationApi();
                            notificationsAdapter.remove(id,position);
                            dialogs.dismiss();
                        } else {
                            Toast.makeText(getActivity(), R.string.failed_error, Toast.LENGTH_LONG).show();

                        }

                    } else {
                        try {
                        //    CustomProgressbar.hideProgressBar();
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
              //  CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<NotificationDeleteModel> call, Throwable t) {
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
}
