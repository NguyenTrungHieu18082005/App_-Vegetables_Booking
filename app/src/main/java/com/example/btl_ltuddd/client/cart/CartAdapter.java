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
import com.example.btl_ltuddd.client.dashboard.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<Product> list;

    public CartAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
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
        Product product = list.get(position);

        holder.imgCartProduct.setImageResource(product.getImage());
        holder.txtCartName.setText(product.getName());
        holder.txtCartPrice.setText(product.getPrice());
        holder.txtCartUnit.setText(product.getUnit());

        // Tăng giảm số lượng
        final int[] qty = {1};
        holder.txtQuantity.setText(String.valueOf(qty[0]));

        holder.btnPlus.setOnClickListener(v -> {
            qty[0]++;
            holder.txtQuantity.setText(String.valueOf(qty[0]));
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (qty[0] > 1) {
                qty[0]--;
                holder.txtQuantity.setText(String.valueOf(qty[0]));
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCartProduct, btnDelete;
        TextView txtCartName, txtCartUnit, txtCartPrice, txtQuantity, btnPlus, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCartProduct = itemView.findViewById(R.id.imgCartProduct);
            txtCartName = itemView.findViewById(R.id.txtCartName);
            txtCartUnit = itemView.findViewById(R.id.txtCartUnit);
            txtCartPrice = itemView.findViewById(R.id.txtCartPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}