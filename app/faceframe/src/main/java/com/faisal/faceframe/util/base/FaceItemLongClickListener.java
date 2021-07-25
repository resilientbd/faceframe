package com.faisal.faceframe.util.base;

import android.view.View;

/**
 * Works with {@link FaceBaseAdapter} normally
 */
public interface FaceItemLongClickListener<T> {
    /**
     * Called when a item has been long clicked.
     *
     * @param view The view that was clicked.
     * @param item The T type object that was clicked.
     */
    void onItemLongClick(View view, T item);
}