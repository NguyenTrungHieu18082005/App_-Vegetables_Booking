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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<Product> list;

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = list.get(position);

        holder.imgProduct.setImageResource(product.getImage());
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(product.getPrice());
        holder.txtUnit.setText(product.getUnit());
        holder.txtTag.setText(product.getTag());

        if(product.getTag().isEmpty()){
            holder.txtTag.setVisibility(View.GONE);
        }

        // =======================================================
        // XỬ LÝ SỰ KIỆN CLICK NÚT THÊM VÀO GIỎ HÀNG (DẤU CỘNG XANH)
        // =======================================================
        holder.btnAdd.setOnClickListener(v -> {
            // Lấy tên và giá của sản phẩm tại vị trí được bấm
            String productName = product.getName();
            String productPrice = product.getPrice();

            // Hiển thị thông báo kiểm tra (Bạn có thể đổi đoạn này thành logic lưu SQLite/SharePrefs sau)
            Toast.makeText(context, "Đã thêm " + productName + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        // Click vào card → sang ProductDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("unit", product.getUnit());
            intent.putExtra("image", product.getImage());
            intent.putExtra("tag", product.getTag());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView txtName, txtPrice, txtUnit, txtTag;
        ImageButton btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtUnit = itemView.findViewById(R.id.txtUnit);
            txtTag = itemView.findViewById(R.id.txtTag);
            btnAdd = itemView.findViewById(R.id.btnAdd); // Đã ánh xạ chính xác nút bấm của bạn
        }
    }
}