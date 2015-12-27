package com.cb.adventures.utils;

import android.content.Context;
import android.graphics.Typeface;
import java.util.HashMap;


/**
 * Created by jenics on 2015/12/26.
 */
public class FontFace {

    public enum E_Font_Face {
        COMIXHEAVY
    }

    private static FontFace mInstance;
    private Context mContext;

    private HashMap<E_Font_Face , Typeface> mFaces;

    public void init(Context ctx) {
        mContext = ctx;
    }

    private FontFace() {
        mFaces = new HashMap<>();
    }


    public synchronized static FontFace getInstance() {
        if (mInstance == null) {
            mInstance = new FontFace();
        }
        return mInstance;
    }

    /**
     * Typeface
     * @param face 字体的枚举
     * @return typeface
     */
    public Typeface getFontFace(E_Font_Face face) {
        Typeface fontFace = mFaces.get(face);
        if (fontFace == null) {
            fontFace = createFontFace(face);
            if (fontFace == null) throw new IllegalStateException("undefinition font face");
        }
        return fontFace;
    }

    private Typeface createFontFace(E_Font_Face face) {
        Typeface typeface = null;
        switch (face) {
            case COMIXHEAVY:
                typeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/comixheavy.ttf");
            default:
                break;
        }
        mFaces.put(face,typeface);
        return typeface;
    }
}
