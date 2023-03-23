package com.barcode.salmaStyle.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.barcode.salmaStyle.MainActivity;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.ScanProductDetailActivity;
import com.barcode.salmaStyle.response.PendingProductResponse;
import com.barcode.salmaStyle.utol.SharedPrefClass;

import java.util.List;

public class AdapterPending extends RecyclerView.Adapter<AdapterPending.VH> {

    View view = null;
    Context context;
    String img1 = "", img2 = "", img3 = "";
    String pend_status = "";
    String title_farsi = "", review_farsi = "";
    SharedPrefClass sharedPrefClass;
    List<PendingProductResponse> pendingProductResponseList;
    SharedPreferences sharedPreferences;
    public static String upload_status = "";

    public AdapterPending(Context context, List<PendingProductResponse> pendingProductResponseList) {
        this.pendingProductResponseList = pendingProductResponseList;
        this.context = context;

    }

    @NonNull
    @Override
    public AdapterPending.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pending, parent, false);
        return new AdapterPending.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPending.VH holder, int position) {


        sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        sharedPrefClass = new SharedPrefClass(context);

        img1 = String.valueOf(pendingProductResponseList.get(position).getProduct_image());
        img2 = String.valueOf(pendingProductResponseList.get(position).getIngredients_image());
        img3 = String.valueOf(pendingProductResponseList.get(position).getNutrition_facts_image());
        title_farsi = String.valueOf(pendingProductResponseList.get(position).getعنوان());
        pend_status = pendingProductResponseList.get(position).getStatus();

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            holder.name.setText(title_farsi);
            Log.e("valuecheck", "spanish    " + sharedPrefClass.getString(SharedPrefClass.LANGUAGE));
        } else {
            holder.name.setText(pendingProductResponseList.get(position).getTitle());
        }

        holder.date.setText(pendingProductResponseList.get(position).getCreated_at());


        if (pend_status.equals("Approved")) {
            holder.status.setText(R.string.salma_approved);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else if (pend_status.equals("Rejected")) {
            holder.status.setText(R.string.salma_not_approved);
            holder.status.setTextColor(Color.parseColor("#ff0000"));
        } else if (pend_status.equals("Rejected While Dieting")) {
            holder.status.setText(R.string.salma_on_diet);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        } else if (pend_status.equals("Occasionally")) {
            holder.status.setText(R.string.salma_occ_appr);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else if (pend_status.equals("Not Clear Photos")) {
            holder.status.setText(R.string.photo_not_clear);
            holder.upload_image.setVisibility(View.VISIBLE);
            holder.status.setTextColor(Color.parseColor("#ff0000"));
            upload_status = "not_clear_photo_adapter";
        } else {
            holder.status.setText(R.string.pending);
            holder.upload_image.setVisibility(View.VISIBLE);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        }

        String image_url = "http://69.49.235.253:8000" + img1;

        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.pen_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.e("id", "    " + pendingProductResponseList.get(position).getId());
                editor.putString("id_pending", String.valueOf(pendingProductResponseList.get(position).getId()));
                editor.apply();


                img1 = String.valueOf(pendingProductResponseList.get(position).getProduct_image());
                img2 = String.valueOf(pendingProductResponseList.get(position).getIngredients_image());
                img3 = String.valueOf(pendingProductResponseList.get(position).getNutrition_facts_image());

                pend_status = pendingProductResponseList.get(position).getStatus();

                Intent intent = new Intent(context, ScanProductDetailActivity.class);
                intent.putExtra("image1", img1);
                intent.putExtra("image2", img2);
                intent.putExtra("image3", img3);
                intent.putExtra("status", pendingProductResponseList.get(position).getStatus());
                intent.putExtra("comment_english", pendingProductResponseList.get(position).getReview());
                intent.putExtra("comment_farsi", pendingProductResponseList.get(position).getمرور());
                context.startActivity(intent);
            }
        });

        holder.upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.e("id", "    " + pendingProductResponseList.get(position).getId());
                editor.putString("id_pending", String.valueOf(pendingProductResponseList.get(position).getId()));
                editor.apply();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("login_key", "productimage_fragment");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pendingProductResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView name, date, status;
        CardView card_pending;
        ImageView pen_img;
        Button upload_image;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_pending_prod_name);
            date = itemView.findViewById(R.id.txt_prod_pending_date);
            status = itemView.findViewById(R.id.txt_pending_prod_status);
            card_pending = itemView.findViewById(R.id.card_pending);
            pen_img = itemView.findViewById(R.id.pend_img);
            upload_image = itemView.findViewById(R.id.btn_upload_img);
        }
    }
}
