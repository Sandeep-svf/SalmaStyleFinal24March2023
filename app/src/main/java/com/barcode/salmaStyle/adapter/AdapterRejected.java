package com.barcode.salmaStyle.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.ScanProductDetailActivity;
import com.barcode.salmaStyle.response.ProductRejectedResponse;
import com.barcode.salmaStyle.utol.SharedPrefClass;

import java.util.List;

public class AdapterRejected extends RecyclerView.Adapter<AdapterRejected.VH> {

    View view = null;
    Context context;
    List<ProductRejectedResponse> productRejectedResponseList;
    String img1 = "", img2 = "", img3 = "";
    String rej_status = "";
    String title_farsi = "", review_farsi = "";
    SharedPrefClass sharedPrefClass;

    public AdapterRejected(Context context, List<ProductRejectedResponse> productRejectedResponseList) {
        this.productRejectedResponseList = productRejectedResponseList;
        this.context = context;
    }


    @NonNull
    @Override
    public AdapterRejected.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_rejected, parent, false);
        return new AdapterRejected.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRejected.VH holder, int position) {

        sharedPrefClass = new SharedPrefClass(context);

        img1 = String.valueOf(productRejectedResponseList.get(position).getProduct_image());
        img2 = String.valueOf(productRejectedResponseList.get(position).getIngredients_image());
        img3 = String.valueOf(productRejectedResponseList.get(position).getNutrition_facts_image());

        rej_status = productRejectedResponseList.get(position).getStatus();
        title_farsi = String.valueOf(productRejectedResponseList.get(position).getعنوان());

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            holder.name.setText(title_farsi);
            Log.e("valuecheck", "spanish    " + sharedPrefClass.getString(SharedPrefClass.LANGUAGE));
        } else {
            holder.name.setText(productRejectedResponseList.get(position).getTitle());
        }

        if (rej_status.equals("Approved")) {
            holder.status.setText(R.string.salma_approved);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else if (rej_status.equals("Rejected")) {
            holder.status.setText(R.string.salma_not_approved);
            holder.status.setTextColor(Color.parseColor("#ff0000"));
        } else if (rej_status.equals("Rejected While Dieting")) {
            holder.status.setText(R.string.salma_on_diet);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        } else if (rej_status.equals("Occasionally")) {
            holder.status.setText(R.string.salma_occ_appr);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else {
            holder.status.setText(rej_status);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        }

        holder.date.setText(productRejectedResponseList.get(position).getCreated_at());

        String image_url = "http://69.49.235.253:8000" + img1;

        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.rej_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img1 = String.valueOf(productRejectedResponseList.get(position).getProduct_image());
                img2 = String.valueOf(productRejectedResponseList.get(position).getIngredients_image());
                img3 = String.valueOf(productRejectedResponseList.get(position).getNutrition_facts_image());

                Glide.with(context).load(img3)
                        .thumbnail(0.5f)
                        .into(holder.rej_img);


                Intent intent = new Intent(context, ScanProductDetailActivity.class);
                intent.putExtra("image1", img1);
                intent.putExtra("image2", img2);
                intent.putExtra("image3", img3);
                intent.putExtra("status", productRejectedResponseList.get(position).getStatus());
                intent.putExtra("comment_english", productRejectedResponseList.get(position).getReview());
                intent.putExtra("comment_farsi", productRejectedResponseList.get(position).getمرور());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productRejectedResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView name, date, status;
        CardView card_rejected;
        ImageView rej_img;

        public VH(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.txt_prod_rej_date);
            name = itemView.findViewById(R.id.txt_rejected_prod_name);
            status = itemView.findViewById(R.id.txt_rejected_prod_status);
            card_rejected = itemView.findViewById(R.id.card_rejected);
            rej_img = itemView.findViewById(R.id.rej_img);
        }
    }
}
