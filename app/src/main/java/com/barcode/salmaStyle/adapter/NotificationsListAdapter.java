package com.barcode.salmaStyle.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.NotificationDelete;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.RetroifitApi.APIService;
import com.barcode.salmaStyle.RetroifitApi.ApiClient;
import com.barcode.salmaStyle.ScanProductDetailActivity;
import com.barcode.salmaStyle.model.NotificationDeleteModel;
import com.barcode.salmaStyle.model.ProductScanModel;
import com.barcode.salmaStyle.response.NotificationResponse;
import com.barcode.salmaStyle.response.ProductScanResponse;
import com.barcode.salmaStyle.utol.CustomProgressbar;
import com.barcode.salmaStyle.utol.RefreshInterface;
import com.barcode.salmaStyle.utol.SharedPrefClass;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {

    Context context;
    View view = null;
    List<NotificationResponse> notificationResponseList;
    AlertDialog dialogs;
    NotificationDeleteModel notificationDeleteModel;
    RefreshInterface refreshInterface;
    String farsi_title = "", farsi_message = "", barcode = "";
    SharedPrefClass sharedPrefClass;
    String img1 = "", img2 = "", img3 = "";
    String status = "", text_farsi = "", text_english = "";
    ProductScanResponse productScanResponse;
    NotificationDelete notificationDelete;

    public NotificationsListAdapter(Context context, List<NotificationResponse> notificationResponseList, NotificationDelete notificationDelete) {
        this.context = context;
        this.notificationResponseList = notificationResponseList;
        this.notificationDelete = notificationDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        sharedPrefClass = new SharedPrefClass(context);

        farsi_title = notificationResponseList.get(position).getعنوان();
        farsi_message = notificationResponseList.get(position).getپیام();

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            holder.title.setText(farsi_title);
            holder.notification_msg.setText(farsi_message);
            Log.e("valuecheck", "spanish    " + sharedPrefClass.getString(SharedPrefClass.LANGUAGE));
        } else {
            holder.title.setText(notificationResponseList.get(position).getTitle());
            holder.notification_msg.setText(notificationResponseList.get(position).getMessage());
        }

        holder.date.setText(notificationResponseList.get(position).getCreated_at());

        String prod_image = notificationResponseList.get(position).getProduct_image();
        String image_url = "http://69.49.235.253:8000" + prod_image;

        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.prod_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                barcode = notificationResponseList.get(position).getBarcode();

                scanproductApi();

            }
        });

        holder.notification_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = String.valueOf(notificationResponseList.get(position).getId());
                DeleteNotification_AlertDialog(position, holder, id);
            }
        });

    }

    private void scanproductApi() {


       // CustomProgressbar.showProgressBar(context, false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ProductScanModel> call = service.productscan(barcode);

        call.enqueue(new Callback<ProductScanModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProductScanModel> call, @NonNull retrofit2.Response<ProductScanModel> response) {
              //  CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            Log.e("message_check", "         " + message);

                            productScanResponse = response.body().getProductScanResponse();

                            Log.e("notification_img", "      " + productScanResponse.getProduct_image() + "    " + productScanResponse.getIngredients_image() + "      " + productScanResponse.getNutrition_facts_image() + "   " + productScanResponse.getStatus() + "      " + productScanResponse.getReview() + "        ");

                            img1 = String.valueOf(productScanResponse.getProduct_image());
                            img2 = String.valueOf(productScanResponse.getIngredients_image());
                            img3 = String.valueOf(productScanResponse.getNutrition_facts_image());
                            status = productScanResponse.status;
                            text_english = productScanResponse.getReview();
                            text_farsi = productScanResponse.getمرور();

                            Intent intent = new Intent(context, ScanProductDetailActivity.class);
                            intent.putExtra("image1", img1);
                            intent.putExtra("image2", img2);
                            intent.putExtra("image3", img3);

                            intent.putExtra("comment_english", text_english);
                            intent.putExtra("comment_farsi", text_farsi);
                            intent.putExtra("status", status);
                            intent.putExtra("notification_key", "not_value");
                            context.startActivity(intent);


                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        try {
                          //  CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(context, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(context, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(context, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(context, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(context, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(context, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(context, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ProductScanModel> call, Throwable t) {
             //   CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(context, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(context, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void DeleteNotification_AlertDialog(int position, ViewHolder holder, String id) {

        MainActivity mainActivity = (MainActivity) view.getContext();
        final LayoutInflater inflater = mainActivity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dialog, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnDelete = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btncancel = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.delete_notification));
        tvMessage.setText(context.getString(R.string.Are_you_sure_delete_all_notification));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btncancel.setOnClickListener(v -> {
            dialogs.dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            dialogs.dismiss();
            notificationDelete.onItemClick(position, id);
        });

        dialogs = alert.create();
        dialogs.show();

    }

    private void DeleteNotification_Api(int position, String id) {

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<NotificationDeleteModel> call = service.get_notificationdelete(id);
        call.enqueue(new Callback<NotificationDeleteModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<NotificationDeleteModel> call, @NonNull retrofit2.Response<NotificationDeleteModel> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getStatus();

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(context, R.string.notification_delete, Toast.LENGTH_LONG).show();
                            deletenotification(position);
                            notifyDataSetChanged();
                            refreshInterface.Refresh();
                            dialogs.dismiss();

                        } else {
                            Toast.makeText(context, R.string.failed_error, Toast.LENGTH_LONG).show();

                        }

                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(context, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(context, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(context, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(context, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(context, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(context, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(context, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<NotificationDeleteModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(context, R.string.network_failure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(context, R.string.network_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void deletenotification(int position) {

        try {
            notificationResponseList.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return notificationResponseList.size();
    }

    public void remove(String id, int position) {
        notificationResponseList.remove(position);
        notificationResponseList.notify();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notification_msg, date, title;
        ImageView notification_delete, prod_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            notification_msg = itemView.findViewById(R.id.notificationContentTxt);
            date = itemView.findViewById(R.id.notificationTimeTxt);
            title = itemView.findViewById(R.id.notificationTitleTxt);
            notification_delete = itemView.findViewById(R.id.notificationDelete_not);
            prod_image = itemView.findViewById(R.id.not_adp_img);
        }
    }
}
