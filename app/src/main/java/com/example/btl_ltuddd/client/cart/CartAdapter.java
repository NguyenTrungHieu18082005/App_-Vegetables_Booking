package com.example.btl_ltuddd.client.cart;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.model.Product;

import java.util.List;

public class CartAdapter
        extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;

    private List<Product> list;

    public CartAdapter(
            Context context,
            List<Product> list
    ) {

        this.context = context;

        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(context)
                        .inflate(
                                R.layout.item_cart,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Product product =
                list.get(position);

        holder.txtCartName.setText(
                product.getName()
        );

        holder.txtCartPrice.setText(
                String.format(
                        "%,.0fđ",
                        product.getPrice()
                )
        );

        holder.txtCartUnit.setText(
                product.getUnit()
        );

        if (
                product.getImageUrl() != null
                        &&
                        !product.getImageUrl().isEmpty()
        ) {

            holder.imgCartProduct.setImageURI(
                    Uri.parse(
                            product.getImageUrl()
                    )
            );
        }

        final int[] quantity = {1};

        holder.txtQuantity.setText(
                String.valueOf(
                        quantity[0]
                )
        );

        holder.btnPlus.setOnClickListener(v -> {

            quantity[0]++;

            holder.txtQuantity.setText(
                    String.valueOf(
                            quantity[0]
                    )
            );
        });

        holder.btnMinus.setOnClickListener(v -> {

            if (quantity[0] > 1) {

                quantity[0]--;

                holder.txtQuantity.setText(
                        String.valueOf(
                                quantity[0]
                        )
                );
            }
        });

        holder.btnDelete.setOnClickListener(v -> {

            int current =
                    holder.getAdapterPosition();

            if (
                    current
                            !=
                            RecyclerView.NO_POSITION
            ) {

                list.remove(current);

                notifyItemRemoved(
                        current
                );

                notifyItemRangeChanged(
                        current,
                        list.size()
                );
            }

        });

    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgCartProduct;

        ImageView btnDelete;

        TextView txtCartName;

        TextView txtCartUnit;

        TextView txtCartPrice;

        TextView txtQuantity;

        TextView btnPlus;

        TextView btnMinus;

        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgCartProduct =
                    itemView.findViewById(
                            R.id.imgCartProduct
                    );

            txtCartName =
                    itemView.findViewById(
                            R.id.txtCartName
                    );

            txtCartUnit =
                    itemView.findViewById(
                            R.id.txtCartUnit
                    );

            txtCartPrice =
                    itemView.findViewById(
                            R.id.txtCartPrice
                    );

            txtQuantity =
                    itemView.findViewById(
                            R.id.txtQuantity
                    );

            btnPlus =
                    itemView.findViewById(
                            R.id.btnPlus
                    );

            btnMinus =
                    itemView.findViewById(
                            R.id.btnMinus
                    );

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete
                    );
        }
    }
}