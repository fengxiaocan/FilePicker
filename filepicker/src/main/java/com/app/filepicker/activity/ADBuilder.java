package com.app.filepicker.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

class ADBuilder extends AlertDialog.Builder {
    public ADBuilder(@NonNull Context context) {
        super(context);
    }

    public ADBuilder(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public ADBuilder setTitle(int titleId) {
        super.setTitle(titleId);
        return this;
    }

    @Override
    public ADBuilder setTitle(@Nullable CharSequence title) {
        super.setTitle(title);
        return this;
    }

    @Override
    public ADBuilder setCustomTitle(@Nullable View customTitleView) {
        super.setCustomTitle(customTitleView);
        return this;
    }

    @Override
    public ADBuilder setMessage(int messageId) {
        super.setMessage(messageId);
        return this;
    }

    @Override
    public ADBuilder setMessage(@Nullable CharSequence message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public ADBuilder setIcon(int iconId) {
        super.setIcon(iconId);
        return this;
    }

    @Override
    public ADBuilder setIcon(@Nullable Drawable icon) {
        super.setIcon(icon);
        return this;
    }

    @Override
    public ADBuilder setIconAttribute(int attrId) {
        super.setIconAttribute(attrId);
        return this;
    }

    @Override
    public ADBuilder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
        super.setPositiveButton(textId, listener);
        return this;
    }

    @Override
    public ADBuilder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
        super.setPositiveButton(text, listener);
        return this;
    }

    @Override
    public ADBuilder setPositiveButtonIcon(Drawable icon) {
        super.setPositiveButtonIcon(icon);
        return this;
    }

    @Override
    public ADBuilder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
        super.setNegativeButton(textId, listener);
        return this;
    }

    @Override
    public ADBuilder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
        super.setNegativeButton(text, listener);
        return this;
    }

    @Override
    public ADBuilder setNegativeButtonIcon(Drawable icon) {
        super.setNegativeButtonIcon(icon);
        return this;
    }

    @Override
    public ADBuilder setNeutralButton(int textId, DialogInterface.OnClickListener listener) {
        super.setNeutralButton(textId, listener);
        return this;
    }

    @Override
    public ADBuilder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
        super.setNeutralButton(text, listener);
        return this;
    }

    @Override
    public ADBuilder setNeutralButtonIcon(Drawable icon) {
        super.setNeutralButtonIcon(icon);
        return this;
    }

    @Override
    public ADBuilder setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        return this;
    }

    @Override
    public ADBuilder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        super.setOnCancelListener(onCancelListener);
        return this;
    }

    @Override
    public ADBuilder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        return this;
    }

    @Override
    public ADBuilder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        super.setOnKeyListener(onKeyListener);
        return this;
    }

    @Override
    public ADBuilder setItems(int itemsId, DialogInterface.OnClickListener listener) {
        super.setItems(itemsId, listener);
        return this;
    }

    @Override
    public ADBuilder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
        super.setItems(items, listener);
        return this;
    }

    @Override
    public ADBuilder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener) {
        super.setAdapter(adapter, listener);
        return this;
    }

    @Override
    public ADBuilder setCursor(Cursor cursor, DialogInterface.OnClickListener listener, String labelColumn) {
        super.setCursor(cursor, listener, labelColumn);
        return this;
    }

    @Override
    public ADBuilder setMultiChoiceItems(int itemsId,
                                         boolean[] checkedItems,
                                         DialogInterface.OnMultiChoiceClickListener listener) {
        super.setMultiChoiceItems(itemsId, checkedItems, listener);
        return this;
    }

    @Override
    public ADBuilder setMultiChoiceItems(CharSequence[] items,
                                         boolean[] checkedItems,
                                         DialogInterface.OnMultiChoiceClickListener listener) {
        super.setMultiChoiceItems(items, checkedItems, listener);
        return this;
    }

    @Override
    public ADBuilder setMultiChoiceItems(Cursor cursor,
                                         String isCheckedColumn,
                                         String labelColumn,
                                         DialogInterface.OnMultiChoiceClickListener listener) {
        super.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener);
        return this;
    }

    @Override
    public ADBuilder setSingleChoiceItems(int itemsId, int checkedItem, DialogInterface.OnClickListener listener) {
        super.setSingleChoiceItems(itemsId, checkedItem, listener);
        return this;
    }

    @Override
    public ADBuilder setSingleChoiceItems(Cursor cursor,
                                          int checkedItem,
                                          String labelColumn,
                                          DialogInterface.OnClickListener listener) {
        super.setSingleChoiceItems(cursor, checkedItem, labelColumn, listener);
        return this;
    }

    @Override
    public ADBuilder setSingleChoiceItems(CharSequence[] items,
                                          int checkedItem,
                                          DialogInterface.OnClickListener listener) {
        super.setSingleChoiceItems(items, checkedItem, listener);
        return this;
    }

    @Override
    public ADBuilder setSingleChoiceItems(ListAdapter adapter,
                                          int checkedItem,
                                          DialogInterface.OnClickListener listener) {
        super.setSingleChoiceItems(adapter, checkedItem, listener);
        return this;
    }

    @Override
    public ADBuilder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        super.setOnItemSelectedListener(listener);
        return this;
    }

    @Override
    public ADBuilder setView(int layoutResId) {
        super.setView(layoutResId);
        return this;
    }

    @Override
    public ADBuilder setView(View view) {
        super.setView(view);
        return this;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public ADBuilder setView(View view,
                             int viewSpacingLeft,
                             int viewSpacingTop,
                             int viewSpacingRight,
                             int viewSpacingBottom) {
        super.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
        return this;
    }

    @Override
    public ADBuilder setInverseBackgroundForced(boolean useInverseBackground) {
        super.setInverseBackgroundForced(useInverseBackground);
        return this;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public ADBuilder setRecycleOnMeasureEnabled(boolean enabled) {
        super.setRecycleOnMeasureEnabled(enabled);
        return this;
    }

    @Override
    public AlertDialog show() {
        return show(0.9f);
    }

    public AlertDialog show(int width) {
        AlertDialog dialog = super.show();
        //由于adapterutils方案导致alertDialog的宽度问题,所以在此处更改dialog的宽度
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public AlertDialog show(float percent) {
        AlertDialog dialog = super.show();
        //由于adapterutils方案导致alertDialog的宽度问题,所以在此处更改dialog的宽度
        final int widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
        dialog.getWindow().setLayout((int) (widthPixels * percent), ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }
}
