package com.kynguyen.shop_3hkt;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class FontAwesome extends androidx.appcompat.widget.AppCompatTextView {
    public static final String FONTAWESOME ="fasolid.ttf";

    public FontAwesome(Context context) {
        super(context);
       init();
    }

    public FontAwesome(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAwesome(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                FONTAWESOME);
        setTypeface(tf);
    }

  public void setTextColor(String s) {
  }
}
