package com.barcode.salmaStyle.fragment;

import static android.content.Context.UI_MODE_SERVICE;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.adapter.AdapterDashboard;
import com.barcode.salmaStyle.model.NotificationModel;
import com.barcode.salmaStyle.model.UserRecentProductModel;
import com.barcode.salmaStyle.response.NotificationCount;
import com.barcode.salmaStyle.response.NotificationResponse;
import com.barcode.salmaStyle.response.UserRecentProductResponse;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class DashboardFragment extends Fragment {

    RecyclerView dash_recyclerView;
    AdapterDashboard adapterDashboard;
    TextView more;
    ImageView barcode_scan;
    NotificationCount notificationCount;
    List<NotificationResponse> notificationResponseList = null;
    SharedPreferences sharedPreferences;

    private List<UserRecentProductResponse> userRecentProductResponseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        //  barcode_scan=view.findViewById(R.id.card_barcode_scan_dash);
        dash_recyclerView = view.findViewById(R.id.dashboard_recycle);
        more = view.findViewById(R.id.more);
        dash_recyclerView.setHasFixedSize(true);
        dash_recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int spanCount = 2;
                int spacing = 25;//spacing between views in grid

                if (position >= 0) {
                    int column = position % spanCount; // item column

                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });


        moreListener();
        notificationApi();
        recenproductApi();
        ui();
        return view;

    }

    private void ui() {
        UiModeManager uiModeManager = (UiModeManager) getActivity().getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
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
                // ((MainActivity) getActivity()).txtNotification.setText("0");
                //   CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<NotificationModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
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

    private void barcodeListener() {

    }

    private void recenproductApi() {

        //  CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<UserRecentProductModel> call = service.getrecentProduct();
        call.enqueue(new Callback<UserRecentProductModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<UserRecentProductModel> call, @NonNull retrofit2.Response<UserRecentProductModel> response) {
                //   CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            // sendVerificationCode(newPhoneNumber);
                            userRecentProductResponseList = response.body().getUserRecentProductResponseList();

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            adapterDashboard = new AdapterDashboard(getActivity(), userRecentProductResponseList);
                            dash_recyclerView.setAdapter(adapterDashboard);


                            for (int i = 0; i < userRecentProductResponseList.size(); i++) {
                                editor.putString("status", userRecentProductResponseList.get(i).getStatus());
                                //editor.putString("id", String.valueOf(userRecentProductResponseList.get(i).getId()));
                                editor.apply();
                            }

                        } else {
                            Toast.makeText(getActivity(), R.string.failed_error, Toast.LENGTH_LONG).show();

                        }

                    } else {
                        try {
                            //     CustomProgressbar.hideProgressBar();
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
            public void onFailure(@NonNull Call<UserRecentProductModel> call, Throwable t) {
                //   CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    //    Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void moreListener() {

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScanTabActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onResume() {
        notificationApi();
        recenproductApi();
        super.onResume();
    }
}