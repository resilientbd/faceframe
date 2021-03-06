package com.faisal.faceframe.util.base;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class FaceBaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected ViewDataBinding mViewDataBinding;

    /**
     * This class automatically set's itself the root views click listener. Most of the time this is beneficial.
     * If you face any problem with this default behavior you should call the overriden constructor
     * @param viewDataBinding
     */
    public FaceBaseViewHolder(ViewDataBinding viewDataBinding) {
        this(viewDataBinding, false);
    }

    /**
     * Use this constructor to reset default click listener.
     * @param viewDataBinding
     * @param isResetDefaultListener
     */
    public FaceBaseViewHolder(ViewDataBinding viewDataBinding, boolean isResetDefaultListener) {
        super(viewDataBinding.getRoot());

        mViewDataBinding = viewDataBinding;
        if(!isResetDefaultListener) {
            mViewDataBinding.getRoot().setOnClickListener(this);
        }
    }

    public ViewDataBinding getViewDataBinding() {
        return mViewDataBinding;
    }

    /**
     * Child class have to implement this method.
     **/
    public abstract void bind(T item);

    /**
     * To set click listener on any view, You can pass multiple view at a time
     *
     * @param views View as params
     */
    protected void setClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }
}