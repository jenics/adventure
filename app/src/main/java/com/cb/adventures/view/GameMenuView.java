package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.utils.ImageLoader;

import java.util.ArrayList;

/**
 * Created by jenics on 2015/12/20.
 */
public class GameMenuView extends BaseView implements IControl {

    private String[] items = new String[] {
            "menu/inventory.png",
            "menu/sugar.png",
    };
    private ArrayList<MenuTtemView> menuItems;
    private static final float RATIO = 0.05f;
    private static final float INTERVAL_RATIO = 0.02f;

    public static final int MENU_ITEM_INVENTORY = 0;

    public static class MenuTtemView {
        public Bitmap bitmap;
        public RectF rectF;
    }

    public interface OnMenuItemClickListener {
        /**
         * @param itemIndex 菜单项索引
         */
        void onItemClick(int itemIndex);
    }

    private OnMenuItemClickListener listener;

    public void init() {
        menuItems = new ArrayList<>();
        float interval = GameConstants.sGameWidth * INTERVAL_RATIO;
        float width = GameConstants.sGameWidth*RATIO;
        float height = GameConstants.sGameWidth*RATIO;

        int i = 0;
        for (String item : items) {
            MenuTtemView menuTtemView = new MenuTtemView();
            menuTtemView.bitmap = ImageLoader.getmInstance().loadBitmap(item);
            menuTtemView.rectF = new RectF(
                    GameConstants.sGameWidth - (i+1)*width - interval*(i+1),
                    0,
                    GameConstants.sGameWidth-i*width-interval*(i+1),
                    height);
            ++i;
            menuItems.add(menuTtemView);
        }
    }

    public OnMenuItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onMouseDown(int x, int y) {

    }

    @Override
    public void onMouseMove(int x, int y) {

    }

    @Override
    public void onMouseUp(int x, int y) {
        ///在这里进行相应
        int index = ptInRegion(x,y);

        if (listener != null && index != -1) {
            listener.onItemClick(index);
        }
    }

    public int ptInRegion(int x, int y) {
        int regionTouch = -1;
        for(int i=0; i<menuItems.size(); ++i) {
            RectF rectf = menuItems.get(i).rectF;
            if (x >= rectf.left && x <= rectf.right && y >= rectf.top && y <= rectf.bottom) {
                regionTouch = i;
                break;
            }
        }
        return regionTouch;
    }


    @Override
    public void draw(Canvas canvas) {
        for (MenuTtemView menuTtemView : menuItems) {
            canvas.drawBitmap(menuTtemView.bitmap,
                    new Rect(   ///src rect
                            0,
                            0,
                            menuTtemView.bitmap.getWidth(),
                            menuTtemView.bitmap.getHeight()),
                    menuTtemView.rectF, null);
        }
    }
}
