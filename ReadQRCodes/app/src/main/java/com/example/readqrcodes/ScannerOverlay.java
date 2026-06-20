package com.example.readqrcodes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.ViewfinderView;

/**
 * ScannerOverlay - Memberikan efek visual (masking, framing, dan animasi scan line)
 * yang modern dan profesional di atas tampilan kamera.
 */
public class ScannerOverlay extends ViewfinderView {
    private final Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF frameRect = new RectF();

    private float scanLinePos = 0f;
    private boolean goingDown = true;
    private LinearGradient lineShader;

    private static final float SCAN_LINE_SPEED = 8f;
    private static final float CORNER_LENGTH = 80f;
    private static final float CORNER_WIDTH = 14f;

    public ScannerOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        maskPaint.setColor(ContextCompat.getColor(getContext(), R.color.scanner_overlay));

        framePaint.setColor(ContextCompat.getColor(getContext(), R.color.primary));
        framePaint.setStrokeWidth(CORNER_WIDTH);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeCap(Paint.Cap.ROUND);

        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        linePaint.setStrokeWidth(8f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float size = Math.min(w, h) * 0.72f;
        float left = (w - size) / 2;
        float top = (h - size) / 2 - 100;
        frameRect.set(left, top, left + size, top + size);
        scanLinePos = frameRect.top;

        // Pre-allocate shader to avoid object allocation in onDraw
        lineShader = new LinearGradient(
                frameRect.left, 0, frameRect.right, 0,
                new int[]{0x00FFFFFF, ContextCompat.getColor(getContext(), R.color.primary), 0x00FFFFFF},
                null, Shader.TileMode.CLAMP);
        linePaint.setShader(lineShader);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (frameRect.isEmpty()) return;

        // 1. Gambar overlay latar belakang
        canvas.drawRect(0, 0, getWidth(), getHeight(), maskPaint);

        // 2. Efek lubang transparan di tengah
        canvas.drawRoundRect(frameRect, 40, 40, clearPaint);

        // 3. Gambar sudut bingkai
        drawCorners(canvas);

        // 4. Gambar garis pindai (Scan Line)
        canvas.drawLine(frameRect.left + 40, scanLinePos, frameRect.right - 40, scanLinePos, linePaint);

        updateScanLine();
        postInvalidateOnAnimation();
    }

    private void drawCorners(Canvas canvas) {
        float l = frameRect.left;
        float t = frameRect.top;
        float r = frameRect.right;
        float b = frameRect.bottom;

        // Top Left
        canvas.drawLine(l, t, l + CORNER_LENGTH, t, framePaint);
        canvas.drawLine(l, t, l, t + CORNER_LENGTH, framePaint);

        // Top Right
        canvas.drawLine(r - CORNER_LENGTH, t, r, t, framePaint);
        canvas.drawLine(r, t, r, t + CORNER_LENGTH, framePaint);

        // Bottom Left
        canvas.drawLine(l, b, l + CORNER_LENGTH, b, framePaint);
        canvas.drawLine(l, b, l, b - CORNER_LENGTH, framePaint);

        // Bottom Right
        canvas.drawLine(r - CORNER_LENGTH, b, r, b, framePaint);
        canvas.drawLine(r, b, r, b - CORNER_LENGTH, framePaint);
    }

    private void updateScanLine() {
        if (goingDown) {
            scanLinePos += SCAN_LINE_SPEED;
            if (scanLinePos >= frameRect.bottom - 10) goingDown = false;
        } else {
            scanLinePos -= SCAN_LINE_SPEED;
            if (scanLinePos <= frameRect.top + 10) goingDown = true;
        }
    }
}
