package view.mapDetals.mapPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// پنل اصلی نقشه که گریدی از TileButtonها می‌سازه
public class MapPanel extends JPanel {
    private final int rows;                         // تعداد ردیف‌ها
    private final int cols;                         // تعداد ستون‌ها
    private final TileButton[][] tiles;             // ماتریس دکمه‌های خانه
    private TileButton selectedTile;                // خانه انتخاب‌شده فعلی

    // رابط کاربری برای گوش دادن به انتخاب خانه
    public interface TileSelectionListener {
        void onTileSelected(TileButton tile);
    }

    private TileSelectionListener selectionListener;

    // سازنده پنل نقشه
    public MapPanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new TileButton[rows][cols];

        setLayout(new GridLayout(rows, cols, 2, 2));  // چیدمان شبکه‌ای
        setBackground(Color.BLACK);
        initTiles();  // ساخت خانه‌ها
    }

    // ایجاد خانه‌ها و اضافه‌کردن به پنل
    private void initTiles() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TileButton tile = new TileButton(row, col, TileType.EMPTY);
                tile.addActionListener(this::onTileClicked);  // اضافه‌کردن رویداد کلیک
                tiles[row][col] = tile;
                add(tile);
            }
        }
    }

    // وقتی یک خانه کلیک شد
    private void onTileClicked(ActionEvent e) {
        TileButton clicked = (TileButton) e.getSource();

        if (selectedTile != null) {
            selectedTile.setSelected(false);   // خانه قبلی رو غیرفعال کن
        }

        selectedTile = clicked;
        selectedTile.setSelected(true);        // این خونه رو فعال کن

        if (selectionListener != null) {
            selectionListener.onTileSelected(selectedTile); // خبر بده به بیرون
        }
    }

    // تنظیم گوش‌دهنده‌ی انتخاب خانه (برای اتصال به پنل کناری)
    public void setTileSelectionListener(TileSelectionListener listener) {
        this.selectionListener = listener;
    }

    // دسترسی به یک خانه خاص از بیرون
    public TileButton getTile(int row, int col) {
        return tiles[row][col];
    }

    // دسترسی به خانه انتخاب‌شده فعلی
    public TileButton getSelectedTile() {
        return selectedTile;
    }
}
