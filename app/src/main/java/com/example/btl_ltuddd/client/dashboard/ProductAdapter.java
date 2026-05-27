package com.example.btl_ltuddd.client.dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.client.listproduct.detail.ProductDetailActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;       // danh sách đang hiển thị
    private List<Product> productListFull;   // danh sách gốc để filter

    public ProductAdapter(Context context, List<Product> productList) {
        this.context          = context;
        this.productList      = new ArrayList<>(productList);
        this.productListFull  = new ArrayList<>(productList);
    }

    /** Lọc danh sách theo từ khoá, gọi sau mỗi lần người dùng nhập */
    public void filter(String query) {
        productList.clear();
        if (query == null || query.trim().isEmpty()) {
            productList.addAll(productListFull);
        } else {
            String lower = query.trim().toLowerCase();
            for (Product p : productListFull) {
                if (p.getName().toLowerCase().contains(lower)) {
                    productList.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    /** Cập nhật toàn bộ dữ liệu mới (gọi từ onResume / loadProducts) */
    public void updateData(List<Product> newList) {
        productListFull = new ArrayList<>(newList);
        productList     = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(String.format("%,.0fđ", product.getPrice()));
        holder.txtUnit.setText("/ " + product.getUnit());

        if (product.getImageUrl() != null) {
            holder.imgProduct.setImageURI(
                    android.net.Uri.parse(product.getImageUrl()));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
        });

        holder.btnAdd.setOnClickListener(v -> {
            long userId = context
                    .getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .getLong("userId", -1);
            if (userId == -1) {
                Toast.makeText(context, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseHelper.getInstance(context)
                    .insertCart((int) userId, product.getId());
            Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() { return productList.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView   imgProduct;
        ImageButton btnAdd;
        TextView    txtName, txtPrice, txtUnit;

        ViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName    = itemView.findViewById(R.id.txtName);
            txtPrice   = itemView.findViewById(R.id.txtPrice);
            txtUnit    = itemView.findViewById(R.id.txtUnit);
            btnAdd     = itemView.findViewById(R.id.btnAdd);
        }
    }
}