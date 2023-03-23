package com.barcode.salmaStyle.fragment;

import android.app.AlertDialog;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.model.NotificationModel;
import com.barcode.salmaStyle.response.NotificationCount;
import com.barcode.salmaStyle.response.NotificationResponse;
import com.barcode.salmaStyle.uploadimagescreen.ProductImageFragment;
import com.barcode.salmaStyle.utol.NewVamoQrScanner;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ScanProductFragment extends Fragment {

    ImageView barcode_scan;
    AlertDialog dialogs;
    NotificationCount notificationCount;
    List<NotificationResponse> notificationResponseList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan_product, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        barcode_scan = view.findViewById(R.id.card_barcode_scan);
        barcodescanListener();
        notificationApi();
        return view;

    }

    private void notificationApi() {

        //   CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<NotificationModel> call = service.get_notification();
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<NotificationModel> call, @NonNull retrofit2.Response<NotificationModel> response) {
                //  CustomProgressbar.hideProgressBar();

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
                // ((MainActivity) getActivity()).txtNotification.setText("0");
                //   CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<NotificationModel> call, Throwable t) {
                //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (getActivity() != null) {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


    private void barcodescanListener() {

        barcode_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  alert_dialogue();
                boolean ischecked = true;

                Intent intent = new Intent(getActivity(), NewVamoQrScanner.class);
                intent.putExtra("ischecked", String.valueOf(ischecked));
                intent.putExtra("send_value", "scanproduct_fragment");
                startActivity(intent);

               /* Intent intent=new Intent(getActivity(),ProductImageFragment.class);
                startActivity(intent);*/
            }
        });

    }

    private void alert_dialogue() {

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dialog, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnYes = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btnNo = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        //  alert.setTitle(NewVamoQrScanner.this.getString(R.string.confirm_logout));
        tvMessage.setText(getActivity().getString(R.string.want_send_photos));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btnYes.setOnClickListener(v -> {
            dialogs.dismiss();
            Intent intent = new Intent(getActivity(), ProductImageFragment.class);
            startActivity(intent);
        });

        btnNo.setOnClickListener(v -> {
            dialogs.dismiss();

        });

        dialogs = alert.create();
        dialogs.show();

    }


}
