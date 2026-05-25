package com.example.btl_ltuddd.admin.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.model.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> list;

    public OrderAdapter(List<Order> list) { this.list = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order o = list.get(position);
        holder.tvCode.setText(o.getOrderCode());
        holder.tvDate.setText(o.getCreatedAt());
        holder.tvName.setText(o.getCustomerName());
        holder.tvTotal.setText(String.format("%,.0fđ", o.getTotalAmount()));

        // Lấy 2 chữ cái đầu của tên khách để làm avatar
        String name = o.getCustomerName();
        holder.tvAvatar.setText(name.length() >= 2 ? name.substring(0, 2).toUpperCase() : name);
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvDate, tvAvatar, tvName, tvTotal;
        ViewHolder(View v) {
            super(v);
            tvCode = v.findViewById(R.id.tvOrderCode);
            tvDate = v.findViewById(R.id.tvOrderDate);
            tvAvatar = v.findViewById(R.id.tvAvatar);
            tvName = v.findViewById(R.id.tvCustomerName);
            tvTotal = v.findViewById(R.id.tvTotalAmount);
        }
    }
}