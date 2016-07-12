package me.selinali.tribbble.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import me.selinali.tribbble.R;

public class FontTextView extends TextView {

  private static final Map<Integer, String> FONT_MAP;

  private static final int CAPACITY = 4;
  private static final int CACHE_SIZE = CAPACITY * 250000;
  private static LruCache<String, Typeface> sFontCache;

  static {
    FONT_MAP = new HashMap<>(CAPACITY);
    sFontCache = new LruCache<>(CACHE_SIZE);

    FONT_MAP.put(0, "fonts/OpenSans-Light.ttf");
    FONT_MAP.put(1, "fonts/OpenSans-Regular.ttf");
    FONT_MAP.put(2, "fonts/OpenSans-Semibold.ttf");
    FONT_MAP.put(3, "fonts/OpenSans-Bold.ttf");
  }

  public FontTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.FontTextView,
        0, 0);

    String fontKey;
    try {
      fontKey = FONT_MAP.get(a.getInteger(R.styleable.FontTextView_weight, 1));
    } finally {
      a.recycle();
    }

    Typeface typeface;
    if (sFontCache.get(fontKey) == null) {
      typeface = Typeface.createFromAsset(getContext().getAssets(), fontKey);
      sFontCache.put(fontKey, typeface);
    } else {
      typeface = sFontCache.get(fontKey);
    }

    setTypeface(typeface);
  }
}
