package com.example.btl_ltuddd.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.admin.order.AdminOrderActivity;
import com.example.btl_ltuddd.admin.product.addeditproduct.AddEditProductActivity;
import com.example.btl_ltuddd.database.DatabaseHelper;

public class AdminDashboardFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_admin_dashboard_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DatabaseHelper db = DatabaseHelper.getInstance(requireContext());

        ((TextView) view.findViewById(R.id.tv_low_stock))
                .setText(String.valueOf(db.getLowStockCount(5)));

//
        view.findViewById(R.id.btn_add_product).setOnClickListener(v -> {
             startActivity(new Intent(requireContext(), AddEditProductActivity.class));
        });

        view.findViewById(R.id.btn_view_orders).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AdminOrderActivity.class));

        });
    }
}