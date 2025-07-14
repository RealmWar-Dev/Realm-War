package controller;

import model.*;
import model.block.Block;
import model.kingdom.Kingdom;
import model.map.GameMap;
import model.structure.TownHall;

import java.awt.*;

public class GameController {
    private static GameController instance;

    private GameStatus gameStatus;
    private GameMap gameMap;
    private Kingdom kingdom1;
    private Kingdom kingdom2;
    private Kingdom activePlayer; // This is the field MapPanel needs to access

    private int kingdom1TurnCount;
    private int kingdom2TurnCount;

    private GameController() {
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void startNewGame(User p1, User p2, int rows, int cols) {
        // ساخت نقشه
        gameMap = new GameMap(rows, cols);

        // ساخت پادشاهی‌ها
        createPrimaryKingdom(p1, p2);

        int playable = (rows - 2) * (cols - 2);  // ناحیه قابل بازی، چون لبه‌ها Void هستن
        gameMap.placeRandomBlocks(Block.BlockType.FOREST, (int) (playable * 0.15));
        gameMap.placeRandomBlocks(Block.BlockType.VOID, (int) (playable * 0.10));

        this.activePlayer = kingdom1;

        this.kingdom1TurnCount = 1;
        this.kingdom2TurnCount = 0;

        kingdom1.setColor(Color.RED);
        kingdom2.setColor(Color.BLUE);

        // ساخت وضعیت بازی با بازیکن فعال و زمان نوبت 30 ثانیه
        this.gameStatus = new GameStatus(kingdom1, 30);
    }

    private void createPrimaryKingdom(User p1, User p2) {
        kingdom1 = new Kingdom(p1.getUsername(), Color.RED, 20, 10);
        kingdom2 = new Kingdom(p2.getUsername(), Color.BLUE, 20, 10);

        int margin = Math.max(1, Math.min(gameMap.getRows(), gameMap.getCols()) / 6);

        Block startBlock1 = gameMap.getBlockAt(margin, margin);
        Block startBlock2 = gameMap.getBlockAt(gameMap.getRows() - margin - 1, gameMap.getCols() - margin - 1);

        kingdom1.absorbBlock(startBlock1);
        kingdom2.absorbBlock(startBlock2);

        TownHall townHall1 = new TownHall(kingdom1, startBlock1);
        TownHall townHall2 = new TownHall(kingdom2, startBlock2);

        kingdom1.addStructure(townHall1);
        kingdom2.addStructure(townHall2);

        // مهم: اضافه کردن ساختار به نقشه
        gameMap.placeStructure(margin, margin, townHall1);
        gameMap.placeStructure(gameMap.getRows() - margin - 1, gameMap.getCols() - margin - 1, townHall2);
    }


    public void nextTurn() {
        gameStatus.nextTurn();

        if (activePlayer.equals(kingdom1)) {
            activePlayer = kingdom2;
            kingdom2TurnCount++;
        } else {
            activePlayer = kingdom1;
            kingdom1TurnCount++;
        }
        gameStatus.setActiveKingdom(activePlayer);
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getPlayer1TurnCount() {
        return kingdom1TurnCount;
    }

    public int getKingdom2TurnCount() {
        return kingdom2TurnCount;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    // ⭐ ADDED THIS METHOD TO ALLOW MapPanel TO ACCESS THE ACTIVE KINGDOM ⭐
    public Kingdom getActiveKingdom() {
        return activePlayer;
    }

    public void buildUnitAt(int row, int col) {
        System.out.println("✅ Building unit at: " + row + "," + col);
        // بعداً اینجا منطق ساخت نیرو رو اضافه کن
    }

    public void upgradeAt(int row, int col) {
        System.out.println("🔼 Upgrading at: " + row + "," + col);
        // بعداً اینجا منطق ارتقاء نیرو یا ساختمان رو اضافه کن
    }
}