package com.sadadsdk.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sufalamtech.sadad.sdk.R;

import java.util.ArrayList;
import java.util.List;

import com.sadadsdk.cardform.ExpirationDateDialogTheme;
import com.sadadsdk.utils.VibrationUtils;

public class ExpirationDateItemAdapter extends ArrayAdapter<String> {
    private ExpirationDateDialogTheme mTheme;
    private ShapeDrawable mSelectedItemBackground;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private int mSelectedPosition = -1;
    private List<Integer> mDisabledPositions = new ArrayList<>();

    public ExpirationDateItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ExpirationDateItemAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ExpirationDateItemAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public ExpirationDateItemAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ExpirationDateItemAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    public ExpirationDateItemAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ExpirationDateItemAdapter(Context context, ExpirationDateDialogTheme theme, List<String> objects) {
        super(context, R.layout.bt_expiration_date_item, objects);
        this.mTheme = theme;
        float radius = context.getResources().getDimension(R.dimen.bt_expiration_date_item_selected_background_radius);
        float[] radii = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        this.mSelectedItemBackground = new ShapeDrawable(new RoundRectShape(radii, null, null));
        this.mSelectedItemBackground.getPaint().setColor(this.mTheme.getSelectedItemBackground());
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setSelected(int position) {
        this.mSelectedPosition = position;
        this.notifyDataSetChanged();
    }

    public void setDisabled(List<Integer> disabledPositions) {
        this.mDisabledPositions = disabledPositions;
        this.notifyDataSetChanged();
    }

    @NonNull
    @SuppressLint("ResourceType")
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setEnabled(true);
        if (this.mSelectedPosition == position) {
            view.setBackgroundDrawable(this.mSelectedItemBackground);
            view.setTextColor(this.mTheme.getItemInvertedTextColor());
        } else {
            view.setBackgroundResource(17170445);
            if (this.mDisabledPositions.contains(position)) {
                view.setTextColor(this.mTheme.getItemDisabledTextColor());
                view.setEnabled(false);
            } else {
                view.setTextColor(this.mTheme.getItemTextColor());
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExpirationDateItemAdapter.this.mSelectedPosition = position;
                ExpirationDateItemAdapter.this.notifyDataSetChanged();
                VibrationUtils.vibrate(ExpirationDateItemAdapter.this.getContext(), 10);
                if (ExpirationDateItemAdapter.this.mOnItemClickListener != null) {
                    ExpirationDateItemAdapter.this.mOnItemClickListener.onItemClick(null, v, position, (long) position);
                }

            }
        });
        return view;
    }
}
