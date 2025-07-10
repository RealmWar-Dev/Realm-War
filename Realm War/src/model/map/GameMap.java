package model.map;

import model.block.Block;
import model.block.EmptyBlock;
import model.block.ForestBlock;
import model.block.VoidBlock;

import java.util.*;

public class GameMap {
    private final int rows;
    private final int cols;
    private final Block[][] blocks;

    public GameMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.blocks = new Block[rows][cols];

        initializeMap();
    }

    private void initializeMap() {
        // نمونه ساده: همه خونه‌ها رو EmptyBlock بساز، به جز کناره‌ها که Void باشن
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (row == 0 || col == 0 || row == rows - 1 || col == cols - 1) {
                    blocks[row][col] = new VoidBlock(row, col);
                } else {
                    blocks[row][col] = new EmptyBlock(row, col);
                }
            }
        }
    }


    public void placeRandomBlocks(Block.BlockType type, int count) {
        List<Block> candidates = new ArrayList<>();

        // فقط ردیف‌ها و ستون‌های داخلی بررسی می‌شن (نه لبه‌ها)
        for (int row = 1; row < rows - 1; row++) {
            for (int col = 1; col < cols - 1; col++) {
                Block current = blocks[row][col];

                // فقط جاهایی که absorbed نیستن و ساختار یا یونیت ندارن
                boolean canReplace = !current.isAbsorbed() &&
                        current.getStructure() == null &&
                        current.getUnit() == null;

                // اگر بخوایم VOID بریزیم، فقط رو Empty ها بریز
                if (type == Block.BlockType.VOID && current instanceof EmptyBlock && canReplace) {
                    candidates.add(current);
                }

                // اگر بخوایم نوع دیگه بریزیم، فقط رو Empty بریز
                else if (type != Block.BlockType.VOID && current instanceof EmptyBlock && canReplace) {
                    candidates.add(current);
                }
            }
        }

        // محدود کردن به حداکثر موجود
        if (count > candidates.size()) {
            count = candidates.size();
        }

        // درهم‌ریزی لیست برای تصادفی شدن
        Collections.shuffle(candidates);

        // جایگزینی بلاک‌ها
        for (int i = 0; i < count; i++) {
            Block b = candidates.get(i);
            int row = b.getPositionX();
            int col = b.getPositionY();

            // جایگزین کردن با نوع مورد نظر
            switch (type) {
                case VOID:
                    blocks[row][col] = new VoidBlock(row, col);
                    break;
                case FOREST:
                    blocks[row][col] = new ForestBlock(row, col);
                    break;
                case EMPTY:
                    blocks[row][col] = new EmptyBlock(row, col);
                    break;
                // اگر خواستی نوع دیگه اضافه شه:
                // case GOLD_MINE:
                //     blocks[row][col] = new GoldMineBlock(row, col);
                //     break;
                default:
                    throw new IllegalArgumentException("Unsupported block type: " + type);
            }
        }
    }


    public Block getBlockAt(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid block position: (" + row + ", " + col + ")");
        }
        return blocks[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Block[][] getAllBlocks() {
        return blocks;
    }
}
