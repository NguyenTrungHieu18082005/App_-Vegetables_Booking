package com.example.btl_ltuddd.admin.product;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.model.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public static final int LOW_STOCK_THRESHOLD = 10;

    public interface OnProductActionListener {
        void onEdit(Product product);
        void onDelete(Product product);
    }

    private final Context context;
    private List<Product> products;
    private final OnProductActionListener listener;

    public ProductAdapter(Context context, List<Product> products,
                          OnProductActionListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    public void updateList(List<Product> newList) {
        this.products = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = products.get(position);
        boolean isLow = p.getStock() <= LOW_STOCK_THRESHOLD;

        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(String.format("$%.2f/kg", p.getPrice()));
        holder.tvStock.setText("Stock: " + p.getStock() + " kg");

        // Thêm phần load ảnh
        if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
            try {
                Uri imageUri = Uri.parse(p.getImageUrl());
                holder.ivProduct.setImageURI(imageUri);
                holder.ivProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (Exception e) {
                holder.ivProduct.setImageURI(null); // fallback về background xanh
            }
        } else {
            holder.ivProduct.setImageURI(null);
        }

        if (isLow) {
            holder.tvStock.setTextColor(ContextCompat.getColor(context, R.color.danger));
            holder.badgeLowStock.setVisibility(View.VISIBLE);
        } else {
            holder.tvStock.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            holder.badgeLowStock.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(p));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(p));
    }

    @Override
    public int getItemCount() { return products.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvStock, tvPrice, badgeLowStock;
        ImageButton btnEdit, btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct      = itemView.findViewById(R.id.iv_product);
            tvName         = itemView.findViewById(R.id.tv_product_name);
            tvStock        = itemView.findViewById(R.id.tv_stock);
            tvPrice        = itemView.findViewById(R.id.tv_price);
            badgeLowStock  = itemView.findViewById(R.id.badge_low_stock);
            btnEdit        = itemView.findViewById(R.id.btn_edit);
            btnDelete      = itemView.findViewById(R.id.btn_delete);
        }
    }
}