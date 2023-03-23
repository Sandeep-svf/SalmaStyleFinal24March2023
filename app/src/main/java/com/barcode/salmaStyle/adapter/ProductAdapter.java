package com.barcode.salmaStyle.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barcode.salmaStyle.model.ProductModel;
import com.barcode.salmaStyle.uploadimagescreen.ProductImageFragment;
import com.barcode.salmaStyle.utol.CustomImageVIew;
import com.bumptech.glide.Glide;
import com.barcode.salmaStyle.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.My_View_holder> {
    Context context;
    View view = null;
    ItemClick itemClick;
    ArrayList<ProductModel> productimgList;
    public ProductAdapter(ProductImageFragment productImageFragment, ItemClick itemClick, ArrayList<ProductModel> productimgList) {
        this.context = productImageFragment;
        this.itemClick=itemClick;
        this.productimgList=productimgList;
    }

    @NonNull
    @Override
    public ProductAdapter.My_View_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_layout, parent, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new ProductAdapter.My_View_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull My_View_holder holder, int position) {
      holder.upload_image.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              itemClick.sendclickEvent(position);
          }
      });

      if(productimgList.size()>0){
          if(position==0){
              holder.upload_image.setText("Add Product");
          }else if(position==1){
              holder.upload_image.setText("Add Ingerident");
          }else if(position==2){
              holder.upload_image.setText("Add Nutrition Fact");
          }else if(position==3){
              holder.upload_image.setText("Add Barcode Image");
          }
          Glide.with(context).load(productimgList.get(position).getFile_url()).placeholder(R.drawable.rectangle_background).into(holder.prodImg);

      }

      holder.prodImg.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              zoomImageListener(productimgList.get(position).getFile_url());
          }
      });
    }

    private void zoomImageListener(String file_url) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_fullsize_popup);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        CustomImageVIew imag_full = dialog.findViewById(R.id.imag_full);

        ImageView img_cancel = dialog.findViewById(R.id.img_cancel);

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Glide.with(context).load(file_url).into(imag_full);

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return productimgList != null ? productimgList.size() : 0;
    }

    public void update(ArrayList<ProductModel> productimgList1) {
        productimgList.addAll(productimgList1);
        notifyDataSetChanged();
    }

    public class My_View_holder extends RecyclerView.ViewHolder {
        TextView upload_image;
        ImageView prodImg;
        public My_View_holder(@NonNull View itemView) {
            super(itemView);
            upload_image=itemView.findViewById(R.id.upload_image_button);
            prodImg=itemView.findViewById(R.id.img_prod);
        }
    }
}
