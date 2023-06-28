package org.mozc.android.inputmethod.japanese.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

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

        Paint paint = this.paint;
        float r = (canvasRect.height()) * 0.01f;

        canvas.drawColor(bgColor);
        paint.reset();
        paint.setColor(fgColor);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        canvas.drawRoundRect(left + r, top + r, right - r, bottom - r, r * 2, r * 2, paint);
        canvas.drawLine(width / 2, top + r, width / 2, bottom - r, paint);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(height * 0.1f);
        float ofs = height * 0.05f;
        canvas.drawText("a", left + ofs, bottom - ofs, paint);
        canvas.drawText("1", right - (ofs * 2), bottom - ofs, paint);
    }

}
