package com.barcode.salmaStyle.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.barcode.salmaStyle.response.UserRecentProductResponse;

import java.util.List;

public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.VH> {

    View view = null;
    Context context;
    List<UserRecentProductResponse> userRecentProductResponseList;
    String img1 = "", img2 = "", img3 = "";
    SharedPreferences sharedPreferences;
    String dash_status = "";

    public AdapterDashboard(Context context, List<UserRecentProductResponse> userRecentProductResponseList) {

        this.context = context;
        this.userRecentProductResponseList = userRecentProductResponseList;
    }


    @NonNull
    @Override
    public AdapterDashboard.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dashboard, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDashboard.VH holder, int position) {
        sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        holder.prod_name.setText(userRecentProductResponseList.get(position).getTitle());

        img1 = userRecentProductResponseList.get(position).getProduct_image();
        img2 = userRecentProductResponseList.get(position).getIngredients_image();
        img3 = userRecentProductResponseList.get(position).getNutrition_facts_image();
        dash_status = userRecentProductResponseList.get(position).getStatus();

        if (dash_status.equals("Approved")) {
            Glide.with(context).load(R.drawable.doubleup)
                    .thumbnail(0.5f)
                    .into(holder.exp_img);
        } else if (dash_status.equals("Rejected")) {
            Glide.with(context).load(R.drawable.singledown)
                    .thumbnail(0.5f)
                    .into(holder.exp_img);
        } else if (dash_status.equals("Rejected While Dieting")) {
            Glide.with(context).load(R.drawable.rejacted_while_dieting)
                    .thumbnail(0.5f)
                    .into(holder.exp_img);
        } else if (dash_status.equals("Occasionally")) {
            Glide.with(context).load(R.drawable.singleup)
                    .thumbnail(0.5f)
                    .into(holder.exp_img);
        } else if (dash_status.equals("Pending")) {
            Glide.with(context).load(R.drawable.pendinglog)
                    .thumbnail(0.5f)
                    .into(holder.exp_img);
        } else if (dash_status.equals("Not Clear Photos")) {
            Glide.with(context).load(R.drawable.image_not_clear)
                    .thumbnail(0.5f)
                    .into(holder.exp_img);
        }


        String image_url = "http://69.49.235.253:8000" + img1;

        Log.e("img_url", "ok: " + dash_status);
        Log.e("img_url", "ok: " + image_url);
        Log.e("img_url", "ok: " + userRecentProductResponseList.get(position).getTitle());
        Glide.with(context).load(image_url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.rec_img);

        Log.e("image_check", "       " + img1 + "         " + img2 + "          " + img3);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.e("id", "    " + userRecentProductResponseList.get(position).getId());
                editor.putString("id_pending", String.valueOf(userRecentProductResponseList.get(position).getId()));
                editor.apply();

                Intent intent = new Intent(context, ScanProductDetailActivity.class);
                intent.putExtra("image1", userRecentProductResponseList.get(position).getProduct_image());
                intent.putExtra("image2", userRecentProductResponseList.get(position).getIngredients_image());
                intent.putExtra("image3", userRecentProductResponseList.get(position).getNutrition_facts_image());
                intent.putExtra("status", userRecentProductResponseList.get(position).getStatus());
                intent.putExtra("comment_english", userRecentProductResponseList.get(position).getReview());
                intent.putExtra("comment_farsi", userRecentProductResponseList.get(position).getمرور());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userRecentProductResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView prod_name, status, date;
        ImageView rec_img, exp_img;

        public VH(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_whole);
            prod_name = itemView.findViewById(R.id.rec_prod_name);
            // status=itemView.findViewById(R.id.rec_prod_status);
            //  date=itemView.findViewById(R.id.rec_prod_date);
            rec_img = itemView.findViewById(R.id.dash_img_logo1);
            exp_img = itemView.findViewById(R.id.exp_img);
        }
    }
}
