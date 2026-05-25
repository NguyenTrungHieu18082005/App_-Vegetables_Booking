package com.example.btl_ltuddd.client.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // Thêm import này để hiển thị thông báo nhanh

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.R;

import java.util.List;
import android.content.Intent;
import com.example.btl_ltuddd.client.listproduct.detail.ProductDetailActivity;
import com.example.btl_ltuddd.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;

    private List<Product> productList;

    public ProductAdapter(
            Context context,
            List<Product> productList
    ) {

        this.context =
                context;

        this.productList =
                productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            ViewHolder holder,
            int position
    ) {

        Product product =
                productList.get(
                        position
                );

        holder.txtName.setText(
                product.getName()
        );

        holder.txtPrice.setText(
                String.format(
                        "%.0fđ",
                        product.getPrice()
                )
        );

        holder.txtUnit.setText(
                "/ " +
                        product.getUnit()
        );

//        holder.txtBadge.setText(
//                product.getCategory()
//        );

        if (
                product.getImageUrl() != null
        ) {

            holder.imgProduct.setImageURI(
                    android.net.Uri.parse(
                            product.getImageUrl()
                    )
            );
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgProduct;

        TextView txtName;

        TextView txtPrice;

        TextView txtUnit;

//        TextView txtBadge;

        ViewHolder(
                View itemView
        ) {

            super(itemView);

            imgProduct =
                    itemView.findViewById(
                            R.id.imgProduct
                    );

            txtName =
                    itemView.findViewById(
                            R.id.txtName
                    );

            txtPrice =
                    itemView.findViewById(
                            R.id.txtPrice
                    );

            txtUnit =
                    itemView.findViewById(
                            R.id.txtUnit
                    );

//            txtBadge =
//                    itemView.findViewById(
//                            R.id.txtBadge
//                    );

        }
    }
}