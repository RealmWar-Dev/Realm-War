package model.unit;

import model.kingdom.Kingdom;
import model.block.Block;
import model.block.ForestBlock;
import model.structure.Tower; // Import Tower for restriction check
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Unit {

    private static final Logger LOGGER = Logger.getLogger(Unit.class.getName());

    protected int currentHealth;
    protected int maxHealth;
    protected int movementRange;
    protected int attackPower;
    protected int attackRange;
    protected int productionCostGold;
    protected int maintenanceCostFood;
    protected int unitSpaceOccupied;
    protected Kingdom ownerKingdom;
    protected Block currentBlockLocation;
    protected UnitRank unitRank;

    protected boolean hasMoved;
    protected boolean hasAttacked;

    public Unit(int initialHealth, int movementRange, int attackPower, int attackRange,
                int productionCostGold, int maintenanceCostFood, int unitSpaceOccupied,
                Kingdom ownerKingdom, Block currentBlockLocation, UnitRank unitRank) {
        if (initialHealth <= 0 || movementRange < 0 || attackPower < 0 || attackRange < 0 ||
                productionCostGold < 0 || maintenanceCostFood < 0 || unitSpaceOccupied <= 0) {
            throw new IllegalArgumentException("Unit properties must be non-negative and positive where required.");
        }
        if (ownerKingdom == null || currentBlockLocation == null || unitRank == null) {
            throw new IllegalArgumentException("Owner kingdom, current block, and unit rank cannot be null.");
        }

        this.currentHealth = initialHealth;
        this.maxHealth = initialHealth;
        this.movementRange = movementRange;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.productionCostGold = productionCostGold;
        this.maintenanceCostFood = maintenanceCostFood;
        this.unitSpaceOccupied = unitSpaceOccupied;
        this.ownerKingdom = ownerKingdom;
        this.currentBlockLocation = currentBlockLocation;
        this.unitRank = unitRank;
        this.hasMoved = false;
        this.hasAttacked = false;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getProductionCostGold() {
        return productionCostGold;
    }

    public int getMaintenanceCostFood() {
        return maintenanceCostFood;
    }

    public int getUnitSpaceOccupied() {
        return unitSpaceOccupied;
    }

    public Kingdom getOwnerKingdom() {
        return ownerKingdom;
    }

    public Block getCurrentBlockLocation() {
        return currentBlockLocation;
    }

    public UnitRank getUnitRank() {
        return unitRank;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        this.currentHealth -= damage;
        if (this.currentHealth < 0) {
            this.currentHealth = 0;
        }

        LOGGER.log(Level.INFO, "{0} {1} at ({2},{3}) took {4} damage. Current health: {5}",
                new Object[]{ownerKingdom.getName(), unitRank, currentBlockLocation.getPositionX(),
                        currentBlockLocation.getPositionY(), damage, currentHealth});

        if (isDestroyed()) {
            LOGGER.log(Level.INFO, "{0} {1} at ({2},{3}) has been defeated!",
                    new Object[]{ownerKingdom.getName(), unitRank, currentBlockLocation.getPositionX(), currentBlockLocation.getPositionY()});
            // Notify the kingdom to remove this unit
            if (ownerKingdom != null) {
                ownerKingdom.removeUnit(this);
            }
        }
    }

    public void recoverHealth(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Recovery amount cannot be negative.");
        }
        this.currentHealth += amount;
        if (this.currentHealth > maxHealth) {
            this.currentHealth = maxHealth;
        }
        LOGGER.log(Level.INFO, "{0} {1} at ({2},{3}) recovered {4} health. Current health: {5}",
                new Object[]{ownerKingdom.getName(), unitRank, currentBlockLocation.getPositionX(),
                        currentBlockLocation.getPositionY(), amount, currentHealth});
    }

    public boolean isDestroyed() {
        return this.currentHealth <= 0;
    }

    public void resetTurnActions() {
        this.hasMoved = false;
        this.hasAttacked = false;
    }

    public void setCurrentBlockLocation(Block newBlock) {
        if (newBlock == null) {
            throw new IllegalArgumentException("New block location cannot be null.");
        }
        // This method is primarily for internal use after successful move.
        // It's not the 'move' action itself.
        this.currentBlockLocation = newBlock;
    }

    public void markAttacked() {
        this.hasAttacked = true;
    }

    public boolean canMergeWith(Unit otherUnit) {
        if (otherUnit == null) {
            return false;
        }
        if (this.getUnitRank() != otherUnit.getUnitRank()) {
            return false;
        }
        return this.getUnitRank().getNextRank() != null; // Can only merge if there's a higher rank
    }

    public UnitRank getMergedUnitRank() {
        return this.getUnitRank().getNextRank();
    }

    /**
     * Calculates the Manhattan distance between two blocks.
     * @param block1 The first block.
     * @param block2 The second block.
     * @return The Manhattan distance.
     */
    protected int calculateDistance(Block block1, Block block2) {
        return Math.abs(block1.getPositionX() - block2.getPositionX()) +
                Math.abs(block1.getPositionY() - block2.getPositionY());
    }

    /**
     * Attempts to move the unit to a target block.
     * This is a common implementation that can be overridden by subclasses for specific movement rules.
     * @param targetBlock The block to move to.
     * @return true if the move was successful, false otherwise.
     */
    public boolean move(Block targetBlock) {
        if (hasMoved) {
            LOGGER.log(Level.INFO, "{0} {1} at ({2},{3}) cannot move: Already moved this turn.",
                    new Object[]{ownerKingdom.getName(), unitRank, currentBlockLocation.getPositionX(), currentBlockLocation.getPositionY()});
            return false;
        }
        if (targetBlock == null) {
            LOGGER.log(Level.WARNING, "{0} {1} tried to move to a null block.", new Object[]{ownerKingdom.getName(), unitRank});
            return false;
        }
        if (targetBlock.getBlockType() == Block.BlockType.VOID) {
            LOGGER.log(Level.INFO, "{0} {1} cannot move to Void block at ({2},{3}).",
                    new Object[]{ownerKingdom.getName(), unitRank, targetBlock.getPositionX(), targetBlock.getPositionY()});
            return false;
        }
        // Cannot move to a block already occupied by another unit (friendly or enemy)
        if (targetBlock.getUnit() != null) {
            LOGGER.log(Level.INFO, "{0} {1} cannot move to occupied block at ({2},{3}).",
                    new Object[]{ownerKingdom.getName(), unitRank, targetBlock.getPositionX(), targetBlock.getPositionY()});
            return false;
        }
        // Cannot move to a block owned by another kingdom unless it's a conquest/attack
        // For simple 'move', it implies moving within friendly territory or to unowned territory.
        if (targetBlock.isAbsorbed() && targetBlock.getKingdom() != this.ownerKingdom) {
            LOGGER.log(Level.INFO, "{0} {1} cannot move to enemy-controlled block at ({2},{3}) for simple movement.",
                    new Object[]{ownerKingdom.getName(), unitRank, targetBlock.getPositionX(), targetBlock.getPositionY()});
            return false; // This typically implies an attack/conquer move, not a simple move
        }

        int distance = calculateDistance(currentBlockLocation, targetBlock);
        if (distance > movementRange) {
            LOGGER.log(Level.INFO, "{0} {1} cannot move to ({2},{3}): Target out of range. Distance {4}, Range {5}.",
                    new Object[]{ownerKingdom.getName(), unitRank, targetBlock.getPositionX(), targetBlock.getPositionY(), distance, movementRange});
            return false;
        }

        // Check for Tower blockade: if moving into an adjacent block to an enemy tower
        // or through a block with an enemy tower
        // This is a simplified check. A more robust implementation might check the path.
        // For now, let's assume a unit cannot *enter* a block directly adjacent to an enemy tower
        // if the tower restricts its rank.
        // This requires accessing the game board, which Unit doesn't have directly.
        // A GameManager or Map class would ideally perform this check.
        // For a simpler approach, we'll assume the target block cannot have a restricting enemy tower.
        if (targetBlock.getStructure() instanceof Tower && targetBlock.getKingdom() != ownerKingdom) {
            Tower enemyTower = (Tower) targetBlock.getStructure();
            if (enemyTower.restrictsUnit(this)) {
                LOGGER.log(Level.INFO, "{0} {1} cannot move to ({2},{3}): Blocked by enemy Tower (level {4} restricts {5}).",
                        new Object[]{ownerKingdom.getName(), unitRank, targetBlock.getPositionX(), targetBlock.getPositionY(), enemyTower.getLevel(), unitRank});
                return false;
            }
        }
        // Also check if any *neighboring* block of the target has an enemy tower that restricts.
        // This would require iterating through neighbors of targetBlock and checking if they belong to enemy and have restricting towers.
        // This is complex for a simple 'move' method in Unit.
        // A simpler rule: If a unit tries to move *through* an enemy-controlled block (even if empty of units)
        // that is adjacent to a tower, that's where the restriction applies.
        // For now, we'll stick to the simpler rule: cannot move into a block with an enemy tower if restricted.

        // Remove unit from current block
        this.currentBlockLocation.removeUnit();
        // Set unit to new block
        this.currentBlockLocation = targetBlock;
        this.currentBlockLocation.setUnit(this);
        this.hasMoved = true;
        LOGGER.log(Level.INFO, "{0} {1} moved to ({2},{3}).",
                new Object[]{ownerKingdom.getName(), unitRank, targetBlock.getPositionX(), targetBlock.getPositionY()});
        return true;
    }

    /**
     * Abstract method for attacking a target unit.
     * @param targetUnit The unit to attack.
     * @return The amount of damage dealt.
     */
    public abstract int attack(Unit targetUnit);
}