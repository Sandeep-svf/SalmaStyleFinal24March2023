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
import com.barcode.salmaStyle.response.DietResponse;
import com.barcode.salmaStyle.utol.SharedPrefClass;

import java.util.List;

public class AdapterOnDiet extends RecyclerView.Adapter<AdapterOnDiet.VH> {

    View view = null;
    Context context;
    String img1 = "", img2 = "", img3 = "";
    List<DietResponse> dietResponseList;
    String diet_status = "";
    String title_farsi = "", review_farsi = "";
    SharedPrefClass sharedPrefClass;

    public AdapterOnDiet(Context context, List<DietResponse> dietResponseList) {

        this.context = context;
        this.dietResponseList = dietResponseList;
    }

    @NonNull
    @Override
    public AdapterOnDiet.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ondiet, parent, false);
        return new AdapterOnDiet.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOnDiet.VH holder, int position) {


        sharedPrefClass = new SharedPrefClass(context);
        diet_status = dietResponseList.get(position).getStatus();
        holder.date.setText(dietResponseList.get(position).getCreated_at());
        title_farsi = String.valueOf(dietResponseList.get(position).getعنوان());
        img1 = String.valueOf(dietResponseList.get(position).getProduct_image());
        img2 = String.valueOf(dietResponseList.get(position).getIngredients_image());
        img3 = String.valueOf(dietResponseList.get(position).getNutrition_facts_image());

        String image_url = "http://69.49.235.253:8000" + img1;

        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.ondiet_img);


        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            holder.name.setText(title_farsi);
            Log.e("valuecheck", "spanish    " + sharedPrefClass.getString(SharedPrefClass.LANGUAGE));
        } else {
            holder.name.setText(dietResponseList.get(position).getTitle());

        }

        if (diet_status.equals("Approved")) {
            holder.status.setText(R.string.salma_approved);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else if (diet_status.equals("Rejected")) {
            holder.status.setText(R.string.salma_not_approved);
            holder.status.setTextColor(Color.parseColor("#ff0000"));
        } else if (diet_status.equals("Rejected While Dieting")) {
            holder.status.setText(R.string.salma_on_diet);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        } else if (diet_status.equals("Occasionally")) {
            holder.status.setText(R.string.salma_occ_appr);
            holder.status.setTextColor(Color.parseColor("#097F04"));
        } else {
            holder.status.setText(diet_status);
            holder.status.setTextColor(Color.parseColor("#f26531"));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                img1 = String.valueOf(dietResponseList.get(position).getProduct_image());
                img2 = String.valueOf(dietResponseList.get(position).getIngredients_image());
                img3 = String.valueOf(dietResponseList.get(position).getNutrition_facts_image());


                Intent intent = new Intent(context, ScanProductDetailActivity.class);
                intent.putExtra("image1", img1);
                intent.putExtra("image2", img2);
                intent.putExtra("image3", img3);
                intent.putExtra("status", dietResponseList.get(position).getStatus());
                if(position==0){
                    Log.e("test_sam",dietResponseList.get(position).getStatus()+"ok");
                }
                intent.putExtra("comment_english", dietResponseList.get(position).getReview());
                intent.putExtra("comment_farsi", dietResponseList.get(position).getمرور());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dietResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        CardView card_diet;
        TextView status, date, name;
        ImageView ondiet_img;

        public VH(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.txt_diet_status);
            date = itemView.findViewById(R.id.txt_diet_date);
            name = itemView.findViewById(R.id.txt_diet_prod_name);
            card_diet = itemView.findViewById(R.id.card_diet);
            ondiet_img = itemView.findViewById(R.id.diet_img);

        }
    }
}
