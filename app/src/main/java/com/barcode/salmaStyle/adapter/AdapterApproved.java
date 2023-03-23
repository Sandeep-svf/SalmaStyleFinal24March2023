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
import com.barcode.salmaStyle.response.ProductApprovedResponse;
import com.barcode.salmaStyle.utol.SharedPrefClass;

import java.util.List;

public class AdapterApproved extends RecyclerView.Adapter<AdapterApproved.VH> {

    View view = null;
    Context context;
    String img1 = "", img2 = "", img3 = "";
    String title_farsi = "", review_farsi = "";
    SharedPrefClass sharedPrefClass;
    List<ProductApprovedResponse> productApprovedResponseList;
    String approved_status = "";

    public AdapterApproved(Context context, List<ProductApprovedResponse> productApprovedResponseList) {
        this.productApprovedResponseList = productApprovedResponseList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterApproved.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_approved, parent, false);
        return new AdapterApproved.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterApproved.VH holder, int position) {

        sharedPrefClass = new SharedPrefClass(context);

        //   holder.status.setText(productApprovedResponseList.get(position).getStatus());

        img1 = String.valueOf(productApprovedResponseList.get(position).getProduct_image());
        img2 = String.valueOf(productApprovedResponseList.get(position).getIngredients_image());
        img3 = String.valueOf(productApprovedResponseList.get(position).getNutrition_facts_image());

        approved_status = productApprovedResponseList.get(position).getStatus();
        title_farsi = String.valueOf(productApprovedResponseList.get(position).getعنوان());


        holder.date.setText(productApprovedResponseList.get(position).getCreated_at());

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            holder.name.setText(title_farsi);
            Log.e("valuecheck", "spanish    " + sharedPrefClass.getString(SharedPrefClass.LANGUAGE));
        } else {
            holder.name.setText(productApprovedResponseList.get(position).getTitle());
        }


        String image_url = "http://69.49.235.253:8000" + img1;

        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.prod_img);


        if (approved_status.equals("Approved")) {
            holder.status.setText(R.string.salma_approved);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else if (approved_status.equals("Rejected")) {
            holder.status.setText(R.string.salma_not_approved);
            holder.status.setTextColor(Color.parseColor("#ff0000"));
        } else if (approved_status.equals("Rejected While Dieting")) {
            holder.status.setText(R.string.salma_on_diet);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        } else if (approved_status.equals("Occasionally")) {
            holder.status.setText(R.string.salma_occ_appr);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else {
            holder.status.setText(R.string.pending);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img1 = String.valueOf(productApprovedResponseList.get(position).getProduct_image());
                img2 = String.valueOf(productApprovedResponseList.get(position).getIngredients_image());
                img3 = String.valueOf(productApprovedResponseList.get(position).getNutrition_facts_image());

                Intent intent = new Intent(context, ScanProductDetailActivity.class);
                intent.putExtra("image1", img1);
                intent.putExtra("image2", img2);
                intent.putExtra("image3", img3);
                intent.putExtra("status", productApprovedResponseList.get(position).getStatus());
                intent.putExtra("comment_english", productApprovedResponseList.get(position).getReview());
                intent.putExtra("comment_farsi", productApprovedResponseList.get(position).getمرور());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productApprovedResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        CardView card_approved;
        TextView status, date, name;
        ImageView prod_img;

        public VH(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.txt_approved_status);
            date = itemView.findViewById(R.id.txt_approved_date);
            name = itemView.findViewById(R.id.txt_approved_prod_name);
            card_approved = itemView.findViewById(R.id.card_approved);
            prod_img = itemView.findViewById(R.id.prod_img);
        }
    }
}
