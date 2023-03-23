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
import com.barcode.salmaStyle.response.OccassionResponse;
import com.barcode.salmaStyle.utol.SharedPrefClass;

import java.util.List;

public class AdapterOccassion extends RecyclerView.Adapter<AdapterOccassion.VH> {

    View view = null;
    Context context;
    String img1 = "", img2 = "", img3 = "";
    String status = "";
    List<OccassionResponse> occassionResponseList;
    String occ_status = "";
    String title_farsi = "", review_farsi = "";
    SharedPrefClass sharedPrefClass;

    public AdapterOccassion(Context context, List<OccassionResponse> occassionResponseList) {

        this.context = context;
        this.occassionResponseList = occassionResponseList;

    }

    @NonNull
    @Override
    public AdapterOccassion.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_occassion, parent, false);
        return new AdapterOccassion.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOccassion.VH holder, int position) {
        sharedPrefClass = new SharedPrefClass(context);
        status = occassionResponseList.get(position).getStatus();

        holder.name.setText(occassionResponseList.get(position).getBrand());
        holder.date.setText(occassionResponseList.get(position).getCreated_at());
        occ_status = occassionResponseList.get(position).getStatus();
        title_farsi = String.valueOf(occassionResponseList.get(position).getعنوان());

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            holder.name.setText(title_farsi);
            Log.e("valuecheck", "spanish    " + sharedPrefClass.getString(SharedPrefClass.LANGUAGE));
        } else {
            holder.name.setText(occassionResponseList.get(position).getTitle());
        }


        if (occ_status.equals("Approved")) {
            holder.status.setText(R.string.salma_approved);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else if (occ_status.equals("Rejected")) {
            holder.status.setText(R.string.salma_not_approved);
            holder.status.setTextColor(Color.parseColor("#ff0000"));
        } else if (occ_status.equals("Rejected While Dieting")) {
            holder.status.setText(R.string.salma_on_diet);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        } else if (occ_status.equals("Occasionally")) {
            holder.status.setText(R.string.salma_occ_appr);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else {
            holder.status.setText(occ_status);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        }


        img1 = String.valueOf(occassionResponseList.get(position).getProduct_image());
        img2 = String.valueOf(occassionResponseList.get(position).getIngredients_image());
        img3 = String.valueOf(occassionResponseList.get(position).getNutrition_facts_image());

        String image_url = "http://69.49.235.253:8000" + img1;

        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.occ_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status = occassionResponseList.get(position).getStatus();
                img1 = String.valueOf(occassionResponseList.get(position).getProduct_image());
                img2 = String.valueOf(occassionResponseList.get(position).getIngredients_image());
                img3 = String.valueOf(occassionResponseList.get(position).getNutrition_facts_image());

                Intent intent = new Intent(context, ScanProductDetailActivity.class);
                intent.putExtra("image1", img1);
                intent.putExtra("image2", img2);
                intent.putExtra("image3", img3);
                intent.putExtra("status", status);
                intent.putExtra("comment_english", occassionResponseList.get(position).getReview());
                intent.putExtra("comment_farsi", occassionResponseList.get(position).getمرور());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return occassionResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        CardView card_occassion;
        TextView status, date, name;
        ImageView occ_img;

        public VH(@NonNull View itemView) {
            super(itemView);


            status = itemView.findViewById(R.id.txt_occassion_status);
            date = itemView.findViewById(R.id.txt_occassion_date);
            name = itemView.findViewById(R.id.txt_occassion_prod_name);
            card_occassion = itemView.findViewById(R.id.card_occassion);
            occ_img = itemView.findViewById(R.id.occ_img);
        }
    }
}
