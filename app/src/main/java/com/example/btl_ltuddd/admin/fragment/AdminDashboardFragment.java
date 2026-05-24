package com.example.btl_ltuddd.admin.fragment;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.example.btl_ltuddd.R;
import com.example.btl_ltuddd.admin.view.BarChartView;
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

        BarChartView chart = view.findViewById(R.id.bar_chart);
        chart.setValues(new float[]{3.2f, 4.1f, 2.8f, 5.2f, 0f, 0f}, 3);

        view.findViewById(R.id.btn_add_product).setOnClickListener(v -> {
            // TODO: mở AddProductActivity
        });

        view.findViewById(R.id.btn_view_orders).setOnClickListener(v -> {
            // TODO: switch tab Orders
        });
    }
}