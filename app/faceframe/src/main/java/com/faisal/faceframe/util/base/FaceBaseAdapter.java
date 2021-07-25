package com.faisal.faceframe.util.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class FaceBaseAdapter<T> extends RecyclerView.Adapter<FaceBaseViewHolder<T>> {
    private final List<T> mItemList;
    protected FaceItemClickListener<T> mFaceItemClickListener;
    protected FaceItemLongClickListener<T> mFaceItemLongClickListener;
    private final int NO_ITEM = 0;
    private final int HAS_ITEM = 1;


    private RecyclerView mRecyclerView;

    public FaceBaseAdapter() {
        mItemList = new ArrayList<>();
        this.registerAdapterDataObserver(adapterDataObserver);

    }


    public abstract boolean isEqual(T left, T right);

    /**
     * @param parent
     * @param viewType
     * @return
     * @deprecated Would replace this method with {@link FaceBaseAdapterViewHolder}
     */

    public abstract FaceBaseViewHolder<T> newViewHolder(ViewGroup parent, int viewType);

    /**
     * Commit child fragment of BaseFragment on a frameLayout
     *
     * @param faceItemLongClickListener ItemLongClickListener object
     */
    public void setItemLongClickListener(FaceItemLongClickListener<T> faceItemLongClickListener) {
        this.mFaceItemLongClickListener = faceItemLongClickListener;
    }

    /**
     * Commit child fragment of BaseFragment on a frameLayout
     *
     * @param faceItemClickListener ItemClickListener object
     */
    public void setItemClickListener(FaceItemClickListener<T> faceItemClickListener) {
        this.mFaceItemClickListener = faceItemClickListener;
    }



    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = (RecyclerView) recyclerView;

    }

    @Override
    public FaceBaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return newViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(FaceBaseViewHolder<T> holder, int position) {
        T itemData = getItem(position);

        holder.bind(itemData);
    }

    /**
     * Attached recycler view
     *
     * @return: RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Clear current item list and update UI
     */
    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    /**
     * @return current item list
     */
    public List<T> getItems() {
        return mItemList;
    }

    public FaceItemClickListener<T> getmItemClickListener() {
        return mFaceItemClickListener;
    }
    /**
     * Remove a Item from list and update UI
     *
     * @param t T type object
     */
    public void removeItem(T t) {
        int toIndex = mItemList.indexOf(t);
        if (toIndex < 0 || toIndex >= mItemList.size()) return;
        mItemList.remove(toIndex);
        notifyDataSetChanged();
    }

    /**
     * @param position int value
     * @return get current Item based on Position
     */
    public T getItem(int position) {
        if (position < 0 || position >= mItemList.size()) return null;
        return mItemList.get(position);
    }

   /* public void addItem(T item) {

        int position = 0;
        addItem(item, position);

    }*/

    /**
     * @param item T type object
     * @return int value: current item inserted position in list
     */
    public int addItem(T item) {
        T tItem = findItem(item);

        if (tItem == null) {
            mItemList.add(item);
            notifyItemInserted(mItemList.size() - 1);
            return mItemList.size() - 1;
        }
        return updateItem(item, tItem);
    }

    /**
     * @param items adapter item list
     */
    public void addItem(List<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    /**
     * @param item     T type object
     * @param position int value of position where value will be inserted
     */
    public void addItemToPosition(T item, int position) {
        mItemList.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * @param item     T type object
     * @param position int value of position where value will be inserted
     */
    public void addAllItemToPosition(List<T> item, int position) {
        mItemList.addAll(position, item);
        notifyItemInserted(position);
    }


    /**
     * @param item T type object
     * @return if found then item from list otherwise null
     */
    public T findItem(T item) {
        for (T tItem : mItemList) {
            if (isEqual(item, tItem)) {
                return tItem;
            }
        }
        return null;
    }

    /**
     * @param items List type object list
     */
    public void addItems(List<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    /**
     * @param oldItem T type object
     * @param newItem T type object
     * @return int value: newItem position in list
     */
    public int updateItem(T oldItem, T newItem) {
        int toIndex = mItemList.indexOf(newItem);
        mItemList.set(toIndex, oldItem);
        notifyItemChanged(toIndex);
        return toIndex;
    }

    @Override
    public int getItemCount() {
        int count = mItemList.size();
        return count;
    }



    public ViewDataBinding inflate(ViewGroup viewGroup, int item_layout) {
        return DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), item_layout, viewGroup, false);
    }

    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();

        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);

        }

    };



    /**
     * Abstract class. All common ViewHolder related task happens here. Works coupled with {@link FaceBaseAdapter}
     *
     * @param <T>
     */
    public abstract class FaceBaseAdapterViewHolder<T> extends FaceBaseViewHolder<T> {

        /**
         * This class automatically set's itself the root views click listener. Most of the time this is beneficial.
         * If you face any problem with this default behavior you should call the overriden constructor
         *
         * @param viewDataBinding
         */
        public FaceBaseAdapterViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
        }

        /**
         * Extended from {@link FaceBaseViewHolder#FaceBaseViewHolder(ViewDataBinding, boolean)}
         *
         * @param viewDataBinding
         * @param isResetDefaultListener
         */
        public FaceBaseAdapterViewHolder(ViewDataBinding viewDataBinding, boolean isResetDefaultListener) {
            super(viewDataBinding, isResetDefaultListener);
        }

        @Override
        public void onClick(View view) {

            if (mFaceItemClickListener != null) {
                mFaceItemClickListener.onItemClick(view, getItem(getAdapterPosition()));
                mFaceItemClickListener.onItemClick(view, getItem(getAdapterPosition()), getAdapterPosition());
            }
        }
    }


}