package com.barcode.salmaStyle.productscan_tab;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.adapter.AdapterApproved;
import com.barcode.salmaStyle.model.ApprovedProductModel;
import com.barcode.salmaStyle.response.ProductApprovedResponse;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.R;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductApprovedFragment extends Fragment {

    RecyclerView approved_recyclerView;
    AdapterApproved adapterApproved;
    SharedPreferences sharedPreferences;
    String bar_code="";
    private List<ProductApprovedResponse> productApprovedResponseList ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.product_approved_fragment, container, false);
        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        approved_recyclerView=view.findViewById(R.id.recycle_product_approved);
        approved_recyclerView.setHasFixedSize(true);
        productapprovedApi();
        return view;
    }

    private void productapprovedApi() {
       // CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ApprovedProductModel> call = service.getapprovedProduct();
        call.enqueue(new Callback<ApprovedProductModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ApprovedProductModel> call, @NonNull retrofit2.Response<ApprovedProductModel> response) {
              //  CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                           // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            // sendVerificationCode(newPhoneNumber);
                           productApprovedResponseList=response.body().getProductApprovedResponseList();
                            adapterApproved = new AdapterApproved(getActivity(),productApprovedResponseList);
                            approved_recyclerView.setAdapter(adapterApproved);

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
            public void onFailure(@NonNull Call<ApprovedProductModel> call, Throwable t) {
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
