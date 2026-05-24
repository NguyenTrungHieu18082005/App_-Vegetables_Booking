package com.example.btl_ltuddd.admin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BarChartView extends View {

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] values = {3.2f, 4.1f, 2.8f, 5.2f, 0f, 0f};
    private int activeIndex = 3;

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        barPaint.setColor(0xFFA8D5A2);
        activePaint.setColor(0xFF1B4D2E);
    }

    public void setValues(float[] values, int activeIndex) {
        this.values = values;
        this.activeIndex = activeIndex;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth(), h = getHeight(), n = values.length;
        float max = 0;
        for (float v : values) if (v > max) max = v;
        if (max == 0) max = 1;

        float barW = w / (n * 2f);
        float gap = barW;

        for (int i = 0; i < n; i++) {
            float barH = (values[i] / max) * (h - 8);
            float left = gap / 2 + i * (barW + gap);
            RectF rect = new RectF(left, h - barH, left + barW, h);
            canvas.drawRoundRect(rect, 8, 8, i == activeIndex ? activePaint : barPaint);
        }
    }
}