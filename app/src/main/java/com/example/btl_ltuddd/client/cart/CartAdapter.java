package com.example.btl_ltuddd.client.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.database.DatabaseHelper;
import com.example.btl_ltuddd.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<Cart> list;
    private DatabaseHelper db;

    public interface CartListener {
        void onCartChanged();
    }

    private CartListener listener;

    public CartAdapter(Context context, List<Cart> list, CartListener listener) {
        this.context  = context;
        this.list     = list;
        this.listener = listener;
        db = DatabaseHelper.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart item = list.get(position);

        // FIX: hiện ảnh sản phẩm
        if (item.getProductImage() != null && !item.getProductImage().isEmpty()) {
            holder.imgCartProduct.setImageURI(
                    android.net.Uri.parse(item.getProductImage()));
        } else {
            holder.imgCartProduct.setImageResource(R.drawable.tomato); // placeholder
        }

        holder.txtCartName.setText(item.getProductName());
        holder.txtCartPrice.setText(String.format("%,.0fđ", item.getProductPrice()));
        holder.txtCartUnit.setText(item.getProductUnit());
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        // Tăng SL
        holder.btnPlus.setOnClickListener(v -> {
            int qty = item.getQuantity() + 1;
            item.setQuantity(qty);
            db.updateCartQuantity(item.getId(), qty);
            notifyItemChanged(position);
            listener.onCartChanged();
        });

        // Giảm SL
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int qty = item.getQuantity() - 1;
                item.setQuantity(qty);
                db.updateCartQuantity(item.getId(), qty);
                notifyItemChanged(position);
                listener.onCartChanged();
            }
        });

        // Xóa
        holder.btnDelete.setOnClickListener(v -> {
            db.deleteCart(item.getId());
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size()); // fix lệch index sau khi xóa
            listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCartProduct, btnDelete;
        TextView  txtCartName, txtCartUnit, txtCartPrice, txtQuantity, btnPlus, btnMinus;

        ViewHolder(View itemView) {
            super(itemView);
            imgCartProduct = itemView.findViewById(R.id.imgCartProduct);
            btnDelete      = itemView.findViewById(R.id.btnDelete);
            txtCartName    = itemView.findViewById(R.id.txtCartName);
            txtCartUnit    = itemView.findViewById(R.id.txtCartUnit);
            txtCartPrice   = itemView.findViewById(R.id.txtCartPrice);
            txtQuantity    = itemView.findViewById(R.id.txtQuantity);
            btnPlus        = itemView.findViewById(R.id.btnPlus);
            btnMinus       = itemView.findViewById(R.id.btnMinus);
        }
    }
}