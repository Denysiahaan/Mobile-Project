package com.example.audiorecorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisualizerView extends View {

    private Paint paint;
    private List<Float> amplitudes;
    private int width, height;
    private static final int MAX_BARS = 40;
    private static final int BAR_WIDTH = 12;
    private static final int BAR_GAP = 6;

    public VisualizerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#6200EE")); // Primary color
        paint.setStrokeWidth(BAR_WIDTH);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        amplitudes = new ArrayList<>();
    }

    public void addAmplitude(float amplitude) {
        // Normalize amplitude (0 to ~32767)
        amplitudes.add(amplitude);
        if (amplitudes.size() > MAX_BARS) {
            amplitudes.remove(0);
        }
        invalidate();
    }

    public void clear() {
        amplitudes.clear();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (amplitudes.isEmpty()) return;

        float midY = height / 2f;
        float totalWidth = (amplitudes.size() * (BAR_WIDTH + BAR_GAP)) - BAR_GAP;
        float startX = (width - totalWidth) / 2f;

        for (int i = 0; i < amplitudes.size(); i++) {
            float amplitude = amplitudes.get(i);
            // Enhanced scaling for better visibility
            float barHeight = (float) (Math.sqrt(amplitude) / Math.sqrt(32767)) * (height * 0.9f);
            if (barHeight < 15) barHeight = 15; // Minimum height for style

            float x = startX + i * (BAR_WIDTH + BAR_GAP);
            canvas.drawLine(x, midY - barHeight / 2, x, midY + barHeight / 2, paint);
        }
    }

    public void setBarColor(int color) {
        paint.setColor(color);
        invalidate();
    }
}