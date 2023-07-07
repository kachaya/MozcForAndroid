package org.mozc.android.inputmethod.japanese.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class StrokeKeyDrawable extends BaseBackgroundDrawable {

    private final int fgColor;
    private final int bgColor;
    private final Paint paint = new Paint();

    public StrokeKeyDrawable(int leftPadding, int topPadding, int rightPadding, int bottomPadding, int fgColor, int bgColor) {
        super(leftPadding, topPadding, rightPadding, bottomPadding);
        this.fgColor = fgColor;
        this.bgColor = bgColor;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isCanvasRectEmpty()) {
            return;
        }
        Rect canvasRect = getCanvasRect();
        float left = canvasRect.left;
        float top = canvasRect.top;
        float right = canvasRect.right;
        float bottom = canvasRect.bottom;
        float width = canvasRect.width();
        float height = canvasRect.height();

        canvas.drawColor(bgColor);

        Paint paint = this.paint;
        paint.reset();
        paint.setColor(fgColor);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(1);

        paint.setStyle(Paint.Style.STROKE);
        float r = height * 0.01f;
        canvas.drawRoundRect(left + r, top + r, right - r, bottom - r, r * 2, r * 2, paint);
        canvas.drawLine(width / 2, top + r, width / 2, bottom - r, paint);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(height * 0.1f);
        paint.setTextAlign(Paint.Align.CENTER);
        float ofsX = height * 0.07f;
        float ofsY = height * 0.08f + ((paint.descent() + paint.ascent()) / 2);
        canvas.drawText("a", left + ofsX, bottom - ofsY, paint);
        canvas.drawText("1", right - ofsX, bottom - ofsY, paint);
    }

}
