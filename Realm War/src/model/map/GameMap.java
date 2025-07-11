package model.map;

import model.block.Block;
import model.block.EmptyBlock;
import model.block.ForestBlock;
import model.block.VoidBlock;
import model.structure.Structure;
import model.unit.Unit;

import java.util.*;

public class GameMap {
    private final int rows;
    private final int cols;
    private final Block[][] blocks;

    private final Structure[][] structures;
    private final Unit[][] units;

    public GameMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.blocks = new Block[rows][cols];
        this.structures = new Structure[rows][cols];
        this.units = new Unit[rows][cols];
        initializeMap();
    }

    private void initializeMap() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (row == 0 || col == 0 || row == rows - 1 || col == cols - 1) {
                    blocks[row][col] = new VoidBlock(row, col);
                } else {
                    blocks[row][col] = new EmptyBlock(row, col);
                }
                structures[row][col] = null;
                units[row][col] = null;
            }
        }
    }

    /**
     * دریافت بلاک در موقعیت مشخص
     */
    public Block getBlockAt(int row, int col) {
        validatePosition(row, col);
        return blocks[row][col];
    }

    /**
     * جایگذاری بلاک جدید در موقعیت مشخص
     */
    public void setBlockAt(int row, int col, Block block) {
        validatePosition(row, col);
        blocks[row][col] = block;
    }

    /**
     * جایگذاری تصادفی بلاک‌ها (مانند VOID یا FOREST)
     */
    public void placeRandomBlocks(Block.BlockType type, int count) {
        List<Block> candidates = new ArrayList<>();

        for (int row = 1; row < rows - 1; row++) {
            for (int col = 1; col < cols - 1; col++) {
                Block current = blocks[row][col];

                boolean canReplace = !current.isAbsorbed() &&
                        current.getStructure() == null &&
                        current.getUnit() == null;

                if (type == Block.BlockType.VOID && current instanceof EmptyBlock && canReplace) {
                    candidates.add(current);
                } else if (type != Block.BlockType.VOID && current instanceof EmptyBlock && canReplace) {
                    candidates.add(current);
                }
            }
        }

        if (count > candidates.size()) {
            count = candidates.size();
        }

        Collections.shuffle(candidates);

        for (int i = 0; i < count; i++) {
            Block b = candidates.get(i);
            int row = b.getPositionX();
            int col = b.getPositionY();

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
                default:
                    throw new IllegalArgumentException("Unsupported block type: " + type);
            }
        }
    }

    // متدهای مرتبط با ساختارها (ساختمان‌ها)

    public Structure getStructureAt(int row, int col) {
        validatePosition(row, col);
        return structures[row][col];
    }

    public boolean hasStructureAt(int row, int col) {
        validatePosition(row, col);
        return structures[row][col] != null;
    }

    public void placeStructure(int row, int col, Structure structure) {
        validatePosition(row, col);
        structures[row][col] = structure;
    }

    public void removeStructure(int row, int col) {
        validatePosition(row, col);
        structures[row][col] = null;
    }

    // متدهای مرتبط با یونیت‌ها

    public Unit getUnitAt(int row, int col) {
        validatePosition(row, col);
        return units[row][col];
    }

    public boolean hasUnitAt(int row, int col) {
        validatePosition(row, col);
        return units[row][col] != null;
    }

    public void placeUnit(int row, int col, Unit unit) {
        validatePosition(row, col);
        units[row][col] = unit;
    }

    public void removeUnit(int row, int col) {
        validatePosition(row, col);
        units[row][col] = null;
    }

    // بررسی معتبر بودن مختصات
    private void validatePosition(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid map position: (" + row + ", " + col + ")");
        }
    }

    // Getter ها برای ابعاد
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
