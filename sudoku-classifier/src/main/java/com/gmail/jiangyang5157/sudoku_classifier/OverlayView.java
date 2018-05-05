package com.gmail.jiangyang5157.sudoku_classifier;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.gmail.jiangyang5157.kotlin_kit.render.Renderable;

import java.util.LinkedList;
import java.util.List;

public class OverlayView extends View {

    private final List<Renderable<Canvas>> renderables = new LinkedList<>();

    public OverlayView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public void addRenderable(final Renderable<Canvas> callback) {
        renderables.add(callback);
    }

    @Override
    public synchronized void draw(final Canvas canvas) {
        super.draw(canvas);
        for (final Renderable<Canvas> renderable : renderables) {
            renderable.onRender(canvas);
        }
    }
}
