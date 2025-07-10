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
        return new ArrayList<>(ownedBlocks); // Return a copy to prevent external modification
    }

    public List<Structure> getOwnedStructures() {
        return new ArrayList<>(ownedStructures); // Return a copy
    }

    public List<Unit> getOwnedUnits() {
        return new ArrayList<>(ownedUnits); // Return a copy
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
        // LOGGER.log(Level.INFO, "{0} added {1} gold. Current gold: {2}", new Object[]{name, amount, gold});
    }


    public boolean deductGold(int amount) {
        if (amount < 0) {
            LOGGER.log(Level.WARNING, "Attempted to deduct negative gold amount: " + amount);
            return false;
        }
        if (this.gold >= amount) {
            this.gold -= amount;
            // LOGGER.log(Level.INFO, "{0} deducted {1} gold. Current gold: {2}", new Object[]{name, amount, gold});
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
        // LOGGER.log(Level.INFO, "{0} added {1} food. Current food: {2}", new Object[]{name, amount, food});
    }


    public boolean deductFood(int amount) {
        if (amount < 0) {
            LOGGER.log(Level.WARNING, "Attempted to deduct negative food amount: " + amount);
            return false;
        }
        if (this.food >= amount) {
            this.food -= amount;
            // LOGGER.log(Level.INFO, "{0} deducted {1} food. Current food: {2}", new Object[]{name, amount, food});
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
                LOGGER.log(Level.INFO, "{0} is attempting to conquer block at ({1},{2}) from {3}. This requires combat, not just absorption.",
                        new Object[]{name, block.getPositionX(), block.getPositionY(), block.getKingdom().getName()});
                // This scenario would typically involve combat logic, not just absorption.
                return false;
            }
        }
        if (block.getBlockType() == Block.BlockType.VOID) {
            LOGGER.log(Level.WARNING, "{0} tried to absorb a Void block at ({1},{2}). Not possible.", new Object[]{name, block.getPositionX(), block.getPositionY()});
            return false;
        }

        ownedBlocks.add(block);
        block.setKingdom(this);
        block.onAbsorb(); // Trigger any block-specific on-absorb effects

        LOGGER.log(Level.INFO, "{0} absorbed block at ({1},{2}). Total blocks: {3}",
                new Object[]{name, block.getPositionX(), block.getPositionY(), ownedBlocks.size()});
        return true;
    }

    public void loseBlock(Block block) {
        if (block == null || !ownedBlocks.contains(block)) {
            LOGGER.log(Level.WARNING, "{0} attempted to lose a block it doesn't own or a null block.", name);
            return;
        }

        // If a structure is on the block, it's also lost/destroyed with the block
        if (block.getStructure() != null) {
            // This will also remove it from ownedStructures and clean its block reference
            block.getStructure().takeDamage(block.getStructure().getCurrentHealth()); // Destroy the structure
        }
        // If a unit is on the block, it's also lost/destroyed with the block
        if (block.getUnit() != null) {
            // This will also remove it from ownedUnits and clean its block reference
            block.getUnit().takeDamage(block.getUnit().getCurrentHealth()); // Destroy the unit
        }

        ownedBlocks.remove(block);
        block.setKingdom(null);
        block.setAbsorbed(false); // Make it available for other kingdoms

        // Check if the lost block was the Town Hall's location
        if (this.townHall != null && block.equals(this.townHall.getLocationBlock())) {
            // The Town Hall structure itself will handle its destruction via takeDamage/removeStructure.
            // This check is mainly for logging if the *block* of the Town Hall is lost
            // and the Town Hall structure is also implicitly removed/destroyed.
            LOGGER.log(Level.SEVERE, "{0}'s Town Hall block at ({1},{2}) has been lost! {0} might be defeated!",
                    new Object[]{name, block.getPositionX(), block.getPositionY()});
            // The isDefeated() method will verify if townHall is null or destroyed.
        }
        LOGGER.log(Level.INFO, "{0} lost block at ({1},{2}). Remaining blocks: {3}",
                new Object[]{name, block.getPositionX(), block.getPositionY(), ownedBlocks.size()});
    }


    /**
     * Adds a structure to the kingdom and places it on its designated block.
     * This method also handles deducting the building cost.
     * @param structure The structure to add.
     * @return true if the structure was successfully added, false otherwise.
     */
    public boolean addStructure(Structure structure) {
        if (structure == null || ownedStructures.contains(structure)) {
            LOGGER.log(Level.WARNING, "{0} attempted to add null or already owned structure.", name);
            return false;
        }

        // Check for max limits (already done in canBuildStructure, but good to double check)
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

        // Deduct building cost
        if (!deductGold(structure.getBuildingCost())) {
            LOGGER.log(Level.WARNING, "{0} could not build {1}: Insufficient gold (needed {2}).",
                    new Object[]{name, structure.getStructureType(), structure.getBuildingCost()});
            return false;
        }

        try {
            structure.getLocationBlock().setStructure(structure);
            ownedStructures.add(structure); // Add to kingdom's list only after successful placement on block
            LOGGER.log(Level.INFO, "{0} built a {1} at ({2},{3}). Total structures: {4}",
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

        } catch (IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "{0} failed to place structure on block at ({1},{2}): {3}. Refunding gold.",
                    new Object[]{name, structure.getLocationBlock().getPositionX(), structure.getLocationBlock().getPositionY(), e.getMessage()});
            addGold(structure.getBuildingCost()); // Refund gold if placement fails
            return false;
        }
    }


    /**
     * Removes a structure from the kingdom and its associated block.
     * This method is typically called when a structure is destroyed or abandoned.
     * @param structure The structure to remove.
     */
    public void removeStructure(Structure structure) {
        if (structure == null || !ownedStructures.contains(structure)) {
            LOGGER.log(Level.WARNING, "{0} attempted to remove null or non-owned structure.", name);
            return;
        }
        ownedStructures.remove(structure);
        // Ensure the block's reference to this structure is cleared
        if (structure.getLocationBlock() != null && structure.getLocationBlock().getStructure() == structure) {
            structure.getLocationBlock().removeStructure();
        }

        if (structure instanceof TownHall && structure.equals(townHall)) {
            this.townHall = null; // Town Hall is destroyed, kingdom is likely defeated
            LOGGER.log(Level.SEVERE, "{0}'s Town Hall at ({1},{2}) has been destroyed! {0} is defeated!",
                    new Object[]{name, structure.getLocationBlock().getPositionX(), structure.getLocationBlock().getPositionY()});
        }

        LOGGER.log(Level.INFO, "{0} removed a {1}. Remaining structures: {2}",
                new Object[]{name, structure.getStructureType(), ownedStructures.size()});

        updateUnitCapacity(); // Recalculate capacity as a barrack/townhall might be removed
    }


    public int getStructureCount(Structure.StructureType type) {
        return (int) ownedStructures.stream()
                .filter(s -> s.getStructureType() == type)
                .count();
    }


    /**
     * Checks if the kingdom has enough resources and capacity to build a specific structure type.
     * This method does NOT deduct resources.
     * @param type The type of structure to check.
     * @return true if the structure can be built, false otherwise.
     */
    public boolean canBuildStructure(Structure.StructureType type) {
        int cost = 0;
        int currentCount = getStructureCount(type);

        switch (type) {
            case TOWN_HALL:
                // Only one Town Hall allowed
                if (townHall != null) {
                    LOGGER.log(Level.INFO, "{0} cannot build Town Hall: One already exists.", name);
                    return false;
                }
                cost = 0; // Town Hall usually has no building cost, as it's the starting point
                break;
            case BARRACK:
                cost = nextBarrackCost;
                if (currentCount >= MAX_BARRACKS) {
                    LOGGER.log(Level.INFO, "{0} cannot build Barrack: Max limit reached ({1}).", new Object[]{name, MAX_BARRACKS});
                    return false;
                }
                break;
            case FARM:
                cost = nextFarmCost;
                if (currentCount >= MAX_FARMS) {
                    LOGGER.log(Level.INFO, "{0} cannot build Farm: Max limit reached ({1}).", new Object[]{name, MAX_FARMS});
                    return false;
                }
                break;
            case MARKET:
                cost = nextMarketCost;
                if (currentCount >= MAX_MARKETS) {
                    LOGGER.log(Level.INFO, "{0} cannot build Market: Max limit reached ({1}).", new Object[]{name, MAX_MARKETS});
                    return false;
                }
                break;
            case TOWER:
                cost = 5; // Towers currently have a fixed cost
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


    /**
     * Adds a unit to the kingdom and places it on its designated block.
     * This method also handles deducting the production cost and checking unit capacity.
     * @param unit The unit to add.
     * @return true if the unit was successfully added, false otherwise.
     */
    public boolean addUnit(Unit unit) {
        if (unit == null || ownedUnits.contains(unit)) {
            LOGGER.log(Level.WARNING, "{0} attempted to add null or already owned unit.", name);
            return false;
        }

        // Deduct production cost
        if (!deductGold(unit.getProductionCostGold())) {
            LOGGER.log(Level.WARNING, "{0} could not train {1}: Insufficient gold (needed {2}).",
                    new Object[]{name, unit.getUnitRank(), unit.getProductionCostGold()});
            return false;
        }
        if (!deductFood(unit.getMaintenanceCostFood())) { // Assuming some initial food cost for production
            LOGGER.log(Level.WARNING, "{0} could not train {1}: Insufficient food (needed {2}).",
                    new Object[]{name, unit.getUnitRank(), unit.getMaintenanceCostFood()});
            addGold(unit.getProductionCostGold()); // Refund gold if food is insufficient
            return false;
        }


        // Check unit capacity
        if (currentUnitOccupancy + unit.getUnitSpaceOccupied() > totalUnitCapacity) {
            LOGGER.log(Level.WARNING, "{0} has insufficient unit capacity for {1}. Need {2}, have {3}/{4}.",
                    new Object[]{name, unit.getUnitRank(), unit.getUnitSpaceOccupied(), currentUnitOccupancy, totalUnitCapacity});
            addGold(unit.getProductionCostGold()); // Refund gold if capacity is insufficient
            addFood(unit.getMaintenanceCostFood()); // Refund food
            return false;
        }

        try {
            unit.getCurrentBlockLocation().setUnit(unit); // Place unit on block
            ownedUnits.add(unit);
            currentUnitOccupancy += unit.getUnitSpaceOccupied();

            LOGGER.log(Level.INFO, "{0} trained a {1}. Current unit occupancy: {2}/{3}",
                    new Object[]{name, unit.getUnitRank(), currentUnitOccupancy, totalUnitCapacity});
            return true;
        } catch (IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "{0} failed to place unit on block at ({1},{2}): {3}. Refunding resources.",
                    new Object[]{name, unit.getCurrentBlockLocation().getPositionX(), unit.getCurrentBlockLocation().getPositionY(), e.getMessage()});
            addGold(unit.getProductionCostGold()); // Refund gold if placement fails
            addFood(unit.getMaintenanceCostFood()); // Refund food
            return false;
        }
    }

    /**
     * Removes a unit from the kingdom and its associated block.
     * This method is typically called when a unit is destroyed.
     * @param unit The unit to remove.
     */
    public void removeUnit(Unit unit) {
        if (unit == null || !ownedUnits.contains(unit)) {
            LOGGER.log(Level.WARNING, "{0} attempted to remove null or non-owned unit.", name);
            return;
        }
        ownedUnits.remove(unit);
        // Ensure the block's reference to this unit is cleared
        if (unit.getCurrentBlockLocation() != null && unit.getCurrentBlockLocation().getUnit() == unit) {
            unit.getCurrentBlockLocation().removeUnit();
        }
        currentUnitOccupancy -= unit.getUnitSpaceOccupied();

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


    /**
     * Processes all turn-based effects for the kingdom, including resource generation,
     * maintenance costs, and unit/structure state updates.
     */
    public void processTurn() {
        LOGGER.log(Level.INFO, "--- Processing Turn for {0} ---", name);

        // Reset unit actions (move, attack)
        for (Unit unit : ownedUnits) {
            unit.resetTurnActions();
        }

        // Apply block effects (resource generation from absorbed empty/forest blocks)
        // Iterate over a copy to avoid ConcurrentModificationException if blocks are lost/conquered
        List<Block> blocksCopy = new ArrayList<>(ownedBlocks);
        for (Block block : blocksCopy) {
            if (block.getKingdom() == this) { // Double check ownership in case of recent changes
                block.applyTurnEffects();
            }
        }

        // Apply structure effects (production, maintenance)
        int totalMaintenanceCostGold = 0;
        // Use an iterator for safe removal during iteration if structures are destroyed
        Iterator<Structure> structureIterator = ownedStructures.iterator();
        while (structureIterator.hasNext()) {
            Structure structure = structureIterator.next();
            if (structure.getOwnerKingdom() == this) { // Double check ownership
                structure.applyTurnEffects(); // Apply production (Farm, Market, TownHall)
                totalMaintenanceCostGold += structure.calculateMaintenanceCost();
            }
        }

        // Deduct structure maintenance
        LOGGER.log(Level.INFO, "{0}'s Structure Maintenance: Gold: {1}", new Object[]{name, totalMaintenanceCostGold});
        boolean canAffordGoldMaintenance = deductGold(totalMaintenanceCostGold);
        if (!canAffordGoldMaintenance) {
            LOGGER.log(Level.WARNING, "{0} ran out of gold for structure maintenance! Structures taking damage.", name);
            // Structures take damage if maintenance cannot be paid
            // Iterate over a copy or use iterator for safe removal
            List<Structure> structuresToDamage = new ArrayList<>(ownedStructures);
            for (Structure structure : structuresToDamage) {
                // Only structures with maintenance cost take damage
                if (structure.calculateMaintenanceCost() > 0) {
                    structure.takeDamage(5); // Example damage for unpaid maintenance
                }
            }
            // Structures will remove themselves from ownedStructures via takeDamage -> removeStructure()
        }

        // Deduct unit maintenance (food)
        int totalMaintenanceCostFood = 0;
        for (Unit unit : ownedUnits) { // Iterate to sum up maintenance, actual deduction will be done once
            totalMaintenanceCostFood += unit.getMaintenanceCostFood();
        }
        LOGGER.log(Level.INFO, "{0}'s Unit Maintenance: Food: {1}", new Object[]{name, totalMaintenanceCostFood});
        boolean canAffordFoodMaintenance = deductFood(totalMaintenanceCostFood);
        if (!canAffordFoodMaintenance) {
            LOGGER.log(Level.WARNING, "{0} ran out of food for unit maintenance! Units taking damage.", name);
            // Units take damage if maintenance cannot be paid
            // Use an iterator for safe removal during iteration if units are destroyed
            Iterator<Unit> unitMaintenanceIterator = ownedUnits.iterator();
            while (unitMaintenanceIterator.hasNext()) {
                Unit unit = unitMaintenanceIterator.next();
                unit.takeDamage(10); // Example damage for starving units
                // Units will remove themselves from ownedUnits via takeDamage -> removeUnit()
            }
        }

        // After all destructions, update occupancy
        updateCurrentUnitOccupancy();
        updateUnitCapacity(); // In case a TownHall/Barrack was destroyed due to maintenance

        LOGGER.log(Level.INFO, "--- Turn for {0} Completed. Gold: {1}, Food: {2} ---", new Object[]{name, gold, food});
    }


    public void updateCurrentUnitOccupancy() {
        this.currentUnitOccupancy = ownedUnits.stream()
                .filter(unit -> !unit.isDestroyed()) // Only count units that are not yet destroyed
                .mapToInt(Unit::getUnitSpaceOccupied)
                .sum();
        LOGGER.log(Level.FINE, "{0}'s current unit occupancy updated to {1}.", new Object[]{name, currentUnitOccupancy});
    }

    /**
     * Determines if the kingdom has been defeated (i.e., its Town Hall is destroyed or lost).
     * @return true if the kingdom is defeated, false otherwise.
     */
    public boolean isDefeated() {
        // A kingdom is defeated if it has no Town Hall, or if its Town Hall is destroyed.
        // The townHall field is set to null in removeStructure when the Town Hall is lost/destroyed.
        return townHall == null || townHall.isDestroyed();
    }
}