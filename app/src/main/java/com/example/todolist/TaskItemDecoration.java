package com.example.todolist;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskItemDecoration extends RecyclerView.ItemDecoration {

    private int lineWidth;
    private final Paint paintIncomplete;
    private final Paint paintComplete;
    private final Paint circlePaint;
    private final Paint circleBorderPaint; // Paint for the circle border
    private int circleRadius;
    private final int circleBorderWidth; // Width of the circle border
    private final int lineOffset; // Offset to avoid overlapping the circle

    // Constructor
    public TaskItemDecoration(int lineWidth, int incompleteColor, int completedColor, int circleColor, int circleBorderColor, int circleRadius, int circleBorderWidth, int lineOffset) {
        this.lineWidth = lineWidth;
        this.circleRadius = circleRadius;
        this.circleBorderWidth = circleBorderWidth;
        this.lineOffset = lineOffset;

        this.paintIncomplete = new Paint();
        this.paintIncomplete.setStrokeWidth(lineWidth);
        this.paintIncomplete.setStyle(Paint.Style.STROKE);
        this.paintIncomplete.setColor(incompleteColor);
        this.paintIncomplete.setAntiAlias(true); // Smooth edges

        this.paintComplete = new Paint();
        this.paintComplete.setStrokeWidth(lineWidth);
        this.paintComplete.setStyle(Paint.Style.STROKE);
        this.paintComplete.setColor(completedColor);
        this.paintComplete.setAntiAlias(true); // Smooth edges

        this.circlePaint = new Paint();
        this.circlePaint.setStyle(Paint.Style.FILL);
        this.circlePaint.setAntiAlias(true); // Smooth edges

        this.circleBorderPaint = new Paint();
        this.circleBorderPaint.setStyle(Paint.Style.STROKE);
        this.circleBorderPaint.setStrokeWidth(circleBorderWidth); // Set circle border width
        this.circleBorderPaint.setColor(circleBorderColor);
        this.circleBorderPaint.setAntiAlias(true); // Smooth edges
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        if (childCount == 0) return;

        // Iterate through all children to draw lines and circles
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(child);

            if (viewHolder instanceof TaskAdapter.TaskViewHolder) {
                TaskAdapter.TaskViewHolder taskViewHolder = (TaskAdapter.TaskViewHolder) viewHolder;
                boolean isCompleted = taskViewHolder.checkBoxComplete.isChecked();
                Paint paint = isCompleted ? paintComplete : paintIncomplete;

                int left = child.getLeft() - lineWidth / 2; // Center line with the item
                int top = child.getTop();
                int bottom = child.getBottom();

                // Draw the vertical line from top to bottom of the current view
                c.drawLine(left, top, left, bottom, paint);

                // Draw the circle for the first item separately
                if (i == 0) {
                    circlePaint.setColor(isCompleted ? paintComplete.getColor() : paintIncomplete.getColor());
                    c.drawCircle(left, top - circleRadius, circleRadius, circlePaint);
                    c.drawCircle(left, top - circleRadius, circleRadius, circleBorderPaint); // Draw circle border
                }

                // Only draw circle and line for the next item if it's not the last item
                if (i < childCount - 1) {
                    View nextChild = parent.getChildAt(i + 1);
                    int nextTop = nextChild.getTop();

                    // Draw the line to the top of the next item, adjusted by the offset
                    int adjustedBottom = bottom + lineOffset;
                    c.drawLine(left, adjustedBottom + circleRadius, left, nextTop, paint);

                    // Draw the circle only in the middle of the connecting line
                    int circleY = (adjustedBottom + circleRadius + nextTop) / 2;
                    circlePaint.setColor(isCompleted ? paintComplete.getColor() : paintIncomplete.getColor());
                    c.drawCircle(left, circleY, circleRadius, circlePaint);
                    c.drawCircle(left, circleY, circleRadius, circleBorderPaint); // Draw circle border
                }
            }
        }

        // Draw circle at the end of the last item
        View lastChild = parent.getChildAt(childCount - 1);
        RecyclerView.ViewHolder lastViewHolder = parent.getChildViewHolder(lastChild);
        boolean isLastItemCompleted = lastViewHolder instanceof TaskAdapter.TaskViewHolder &&
                ((TaskAdapter.TaskViewHolder) lastViewHolder).checkBoxComplete.isChecked();
        circlePaint.setColor(isLastItemCompleted ? paintComplete.getColor() : paintIncomplete.getColor());
        int lastChildLeft = lastChild.getLeft() - lineWidth / 2;
        int lastChildBottom = lastChild.getBottom();
        c.drawCircle(lastChildLeft, lastChildBottom + circleRadius, circleRadius, circlePaint);
        c.drawCircle(lastChildLeft, lastChildBottom + circleRadius, circleRadius, circleBorderPaint); // Draw circle border
    }

    // Optional: Methods to update lineWidth and circleRadius dynamically
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        this.paintIncomplete.setStrokeWidth(lineWidth);
        this.paintComplete.setStrokeWidth(lineWidth);
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }
}
