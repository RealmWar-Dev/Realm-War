package model.kingdom;

import model.block.Block;
import model.block.EmptyBlock;
import model.block.ForestBlock;
import model.structure.*;
import model.unit.Unit;
import model.unit.UnitRank;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Kingdom {

    private static final Logger LOGGER = Logger.getLogger(Kingdom.class.getName());

    private String name;
    private String playerID;
    private int gold;
    private int food;

    private List<Block> ownedBlocks;
    private List<Structure> ownedStructures;
    private List<Unit> ownedUnits;

    private TownHall townHall;


    private int totalUnitCapacity;
    private int currentUnitOccupancy;

    private static final int MAX_BARRACKS = 5;
    private static final int MAX_FARMS = 5;
    private static final int MAX_MARKETS = 5;

    private int nextBarrackCost;
    private int nextFarmCost;
    private int nextMarketCost;

    private static final int BUILDING_COST_INCREMENT = 5;

    public Kingdom(String name, String playerID, int initialGold, int initialFood) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Kingdom name cannot be null or empty.");
        }
        if (playerID == null || playerID.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty.");
        }
        if (initialGold < 0 || initialFood < 0) {
            throw new IllegalArgumentException("Initial resources cannot be negative.");
        }

        this.name = name;
        this.playerID = playerID;
        this.gold = initialGold;
        this.food = initialFood;

        this.ownedBlocks = new ArrayList<>();
        this.ownedStructures = new ArrayList<>();
        this.ownedUnits = new ArrayList<>();

        this.totalUnitCapacity = 0;
        this.currentUnitOccupancy = 0;

        this.nextBarrackCost = 5;
        this.nextFarmCost = 5;
        this.nextMarketCost = 5;
    }

    public String getName() {
        return name;
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getGold() {
        return gold;
    }

    public int getFood() {
        return food;
    }

    public int getTotalUnitCapacity() {
        return totalUnitCapacity;
    }

    public int getCurrentUnitOccupancy() {
        return currentUnitOccupancy;
    }

    public List<Block> getOwnedBlocks() {
        return new ArrayList<>(ownedBlocks);
    }

    public List<Structure> getOwnedStructures() {
        return new ArrayList<>(ownedStructures);
    }

    public List<Unit> getOwnedUnits() {
        return new ArrayList<>(ownedUnits);
    }

    public TownHall getTownHall() {
        return townHall;
    }

    public int getNextBarrackCost() {
        return nextBarrackCost;
    }

    public int getNextFarmCost() {
        return nextFarmCost;
    }

    public int getNextMarketCost() {
        return nextMarketCost;
    }



    public void addGold(int amount) {
        if (amount < 0) {
            LOGGER.log(Level.WARNING, "Attempted to add negative gold amount: " + amount);
            return;
        }
        this.gold += amount;
        LOGGER.log(Level.INFO, "{0} added {1} gold. Current gold: {2}", new Object[]{name, amount, gold});
    }


    public boolean deductGold(int amount) {
        if (amount < 0) {
            LOGGER.log(Level.WARNING, "Attempted to deduct negative gold amount: " + amount);
            return false;
        }
        if (this.gold >= amount) {
            this.gold -= amount;
            LOGGER.log(Level.INFO, "{0} deducted {1} gold. Current gold: {2}", new Object[]{name, amount, gold});
            return true;
        }
        LOGGER.log(Level.WARNING, "{0} has insufficient gold to deduct {1}. Current gold: {2}", new Object[]{name, amount, gold});
        return false;
    }


    public void addFood(int amount) {
        if (amount < 0) {
            LOGGER.log(Level.WARNING, "Attempted to add negative food amount: " + amount);
            return;
        }
        this.food += amount;
        LOGGER.log(Level.INFO, "{0} added {1} food. Current food: {2}", new Object[]{name, amount, food});
    }


    public boolean deductFood(int amount) {
        if (amount < 0) {
            LOGGER.log(Level.WARNING, "Attempted to deduct negative food amount: " + amount);
            return false;
        }
        if (this.food >= amount) {
            this.food -= amount;
            LOGGER.log(Level.INFO, "{0} deducted {1} food. Current food: {2}", new Object[]{name, amount, food});
            return true;
        }
        LOGGER.log(Level.WARNING, "{0} has insufficient food to deduct {1}. Current food: {2}", new Object[]{name, amount, food});
        return false;
    }


    public boolean absorbBlock(Block block) {
        if (block == null) {
            LOGGER.log(Level.WARNING, "{0} tried to absorb a null block.", name);
            return false;
        }
        if (block.isAbsorbed()) {
            if (block.getKingdom() == this) {
                LOGGER.log(Level.INFO, "{0} already owns block at ({1},{2}).", new Object[]{name, block.getPositionX(), block.getPositionY()});
                return false;
            } else {
                LOGGER.log(Level.INFO, "{0} is attempting to conquer block at ({1},{2}) from {3}.",
                        new Object[]{name, block.getPositionX(), block.getPositionY(), block.getKingdom().getName()});
                return false;
            }
        }
        if (block.getBlockType() == Block.BlockType.VOID) {
            LOGGER.log(Level.WARNING, "{0} tried to absorb a Void block at ({1},{2}). Not possible.", new Object[]{name, block.getPositionX(), block.getPositionY()});
            return false;
        }

        ownedBlocks.add(block);
        block.setKingdom(this);
        block.onAbsorb();

        LOGGER.log(Level.INFO, "{0} absorbed block at ({1},{2}). Total blocks: {3}",
                new Object[]{name, block.getPositionX(), block.getPositionY(), ownedBlocks.size()});
        return true;
    }

    public void loseBlock(Block block) {
        if (block == null || !ownedBlocks.contains(block)) {
            LOGGER.log(Level.WARNING, "{0} attempted to lose a block it doesn't own or a null block.", name);
            return;
        }

        if (block.getStructure() != null) {
            removeStructure(block.getStructure());
            block.removeStructure();
        }
        if (block.getUnit() != null) {
            removeUnit(block.getUnit());
            block.removeUnit();
        }

        ownedBlocks.remove(block);
        block.setKingdom(null);
        block.setAbsorbed(false);

        if (block.equals(this.townHall.getLocationBlock())) {
            this.townHall = null;
            LOGGER.log(Level.SEVERE, "{0}'s Town Hall block at ({1},{2}) has been lost! {0} is defeated!",
                    new Object[]{name, block.getPositionX(), block.getPositionY()});
        }
        LOGGER.log(Level.INFO, "{0} lost block at ({1},{2}). Remaining blocks: {3}",
                new Object[]{name, block.getPositionX(), block.getPositionY(), ownedBlocks.size()});
    }


    public boolean addStructure(Structure structure) {
        if (structure == null || ownedStructures.contains(structure)) {
            LOGGER.log(Level.WARNING, "{0} attempted to add null or already owned structure.", name);
            return false;
        }

        if (structure instanceof Barrack && getStructureCount(Structure.StructureType.BARRACK) >= MAX_BARRACKS) {
            LOGGER.log(Level.WARNING, "{0} cannot build more Barracks. Max limit reached ({1}).", new Object[]{name, MAX_BARRACKS});
            return false;
        }
        if (structure instanceof Farm && getStructureCount(Structure.StructureType.FARM) >= MAX_FARMS) {
            LOGGER.log(Level.WARNING, "{0} cannot build more Farms. Max limit reached ({1}).", new Object[]{name, MAX_FARMS});
            return false;
        }
        if (structure instanceof Market && getStructureCount(Structure.StructureType.MARKET) >= MAX_MARKETS) {
            LOGGER.log(Level.WARNING, "{0} cannot build more Markets. Max limit reached ({1}).", new Object[]{name, MAX_MARKETS});
            return false;
        }

        try {
            structure.getLocationBlock().setStructure(structure);
        } catch (IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "{0} failed to place structure on block: {1}", new Object[]{name, e.getMessage()});
            return false;
        }


        ownedStructures.add(structure);
        LOGGER.log(Level.INFO, "{0} added a {1} at ({2},{3}). Total structures: {4}",
                new Object[]{name, structure.getStructureType(), structure.getLocationBlock().getPositionX(),
                        structure.getLocationBlock().getPositionY(), ownedStructures.size()});

        if (structure instanceof Barrack) {
            nextBarrackCost += BUILDING_COST_INCREMENT;
        } else if (structure instanceof TownHall) {
            this.townHall = (TownHall) structure;
        } else if (structure instanceof Farm) {
            nextFarmCost += BUILDING_COST_INCREMENT;
        } else if (structure instanceof Market) {
            nextMarketCost += BUILDING_COST_INCREMENT;
        }

        updateUnitCapacity();
        return true;
    }


    public void removeStructure(Structure structure) {
        if (structure == null || !ownedStructures.contains(structure)) {
            LOGGER.log(Level.WARNING, "{0} attempted to remove null or non-owned structure.", name);
            return;
        }
        ownedStructures.remove(structure);
        structure.getLocationBlock().removeStructure();

        if (structure instanceof TownHall && structure.equals(townHall)) {
            this.townHall = null;
            LOGGER.log(Level.SEVERE, "{0}'s Town Hall has been destroyed! {0} is defeated!", name);
        }

        LOGGER.log(Level.INFO, "{0} removed a {1}. Remaining structures: {2}",
                new Object[]{name, structure.getStructureType(), ownedStructures.size()});

        updateUnitCapacity();
    }


    public int getStructureCount(Structure.StructureType type) {
        return (int) ownedStructures.stream()
                .filter(s -> s.getStructureType() == type)
                .count();
    }


    public boolean canBuildStructure(Structure.StructureType type) {
        int cost = 0;
        int currentCount = getStructureCount(type);

        switch (type) {
            case TOWN_HALL:
                return (townHall == null);
            case BARRACK:
                cost = nextBarrackCost;
                if (currentCount >= MAX_BARRACKS) {
                    LOGGER.log(Level.INFO, "{0} cannot build Barrack: Max limit reached.", name);
                    return false;
                }
                break;
            case FARM:
                cost = nextFarmCost;
                if (currentCount >= MAX_FARMS) {
                    LOGGER.log(Level.INFO, "{0} cannot build Farm: Max limit reached.", name);
                    return false;
                }
                break;
            case MARKET:
                cost = nextMarketCost;
                if (currentCount >= MAX_MARKETS) {
                    LOGGER.log(Level.INFO, "{0} cannot build Market: Max limit reached.", name);
                    return false;
                }
                break;
            case TOWER:
                cost = 5;
                break;
            default:
                LOGGER.log(Level.WARNING, "Unknown structure type: " + type);
                return false;
        }

        if (gold < cost) {
            LOGGER.log(Level.INFO, "{0} cannot build {1}: Insufficient gold (need {2}, have {3}).",
                    new Object[]{name, type, cost, gold});
            return false;
        }
        return true;
    }


    public boolean addUnit(Unit unit) {
        if (unit == null || ownedUnits.contains(unit)) {
            LOGGER.log(Level.WARNING, "{0} attempted to add null or already owned unit.", name);
            return false;
        }
        if (currentUnitOccupancy + unit.getUnitSpaceOccupied() > totalUnitCapacity) {
            LOGGER.log(Level.WARNING, "{0} has insufficient unit capacity for {1}. Need {2}, have {3}/{4}.",
                    new Object[]{name, unit.getUnitRank(), unit.getUnitSpaceOccupied(), currentUnitOccupancy, totalUnitCapacity});
            return false;
        }

        ownedUnits.add(unit);
        currentUnitOccupancy += unit.getUnitSpaceOccupied();
        unit.getCurrentBlockLocation().setUnit(unit);

        LOGGER.log(Level.INFO, "{0} added a {1}. Current unit occupancy: {2}/{3}",
                new Object[]{name, unit.getUnitRank(), currentUnitOccupancy, totalUnitCapacity});
        return true;
    }

    public void removeUnit(Unit unit) {
        if (unit == null || !ownedUnits.contains(unit)) {
            LOGGER.log(Level.WARNING, "{0} attempted to remove null or non-owned unit.", name);
            return;
        }
        ownedUnits.remove(unit);
        currentUnitOccupancy -= unit.getUnitSpaceOccupied();
        unit.getCurrentBlockLocation().removeUnit();

        LOGGER.log(Level.INFO, "{0} removed a {1}. Current unit occupancy: {2}/{3}",
                new Object[]{name, unit.getUnitRank(), currentUnitOccupancy, totalUnitCapacity});
    }


    private void updateUnitCapacity() {
        int newCapacity = 0;
        for (Structure structure : ownedStructures) {
            if (structure instanceof TownHall) {
                newCapacity += ((TownHall) structure).getUnitSpaceProvided();
            } else if (structure instanceof Barrack) {
                newCapacity += ((Barrack) structure).getCurrentUnitSpaceProvided();
            }
        }
        this.totalUnitCapacity = newCapacity;
        LOGGER.log(Level.INFO, "{0}'s unit capacity updated to {1}.", new Object[]{name, totalUnitCapacity});
    }



    public void processTurn() {
        LOGGER.log(Level.INFO, "--- Processing Turn for {0} ---", name);

        for (Unit unit : ownedUnits) {
            unit.resetTurnActions();
        }

        for (Block block : ownedBlocks) {
            if (block.getKingdom() == this) {
                block.applyTurnEffects();
            }
        }

        int totalMaintenanceCostGold = 0;
        for (Structure structure : ownedStructures) {
            if (structure.getOwnerKingdom() == this) {
                structure.applyTurnEffects();
                totalMaintenanceCostGold += structure.calculateMaintenanceCost();
            }
        }

        int totalMaintenanceCostFood = 0;
        Iterator<Unit> unitIterator = ownedUnits.iterator();
        while (unitIterator.hasNext()) {
            Unit unit = unitIterator.next();
            totalMaintenanceCostFood += unit.getMaintenanceCostFood();
        }

        LOGGER.log(Level.INFO, "{0}'s Maintenance: Gold: {1}, Food: {2}", new Object[]{name, totalMaintenanceCostGold, totalMaintenanceCostFood});

        boolean canAffordGold = deductGold(totalMaintenanceCostGold);
        boolean canAffordFood = deductFood(totalMaintenanceCostFood);

        if (!canAffordFood) {
            LOGGER.log(Level.WARNING, "{0} ran out of food! Units may starve.", name);
            for (Unit unit : ownedUnits) {
                unit.takeDamage(10);
            }
            ownedUnits.removeIf(Unit::isDestroyed);
            updateCurrentUnitOccupancy();
        }
        if (!canAffordGold) {
            LOGGER.log(Level.WARNING, "{0} ran out of gold! Structures might degrade or stop functioning.", name);
            for (Structure structure : ownedStructures) {
                if (structure.calculateMaintenanceCost() > 0) {
                    structure.takeDamage(5);
                }
            }
            ownedStructures.removeIf(Structure::isDestroyed);
            updateUnitCapacity();
        }

        LOGGER.log(Level.INFO, "--- Turn for {0} Completed. Gold: {1}, Food: {2} ---", new Object[]{name, gold, food});
    }


    public void updateCurrentUnitOccupancy() {
        this.currentUnitOccupancy = ownedUnits.stream()
                .filter(unit -> !unit.isDestroyed())
                .mapToInt(Unit::getUnitSpaceOccupied)
                .sum();
        LOGGER.log(Level.FINE, "{0}'s current unit occupancy updated to {1}.", new Object[]{name, currentUnitOccupancy});
    }

   
    public boolean isDefeated() {
        return townHall == null || townHall.isDestroyed();
    }
}