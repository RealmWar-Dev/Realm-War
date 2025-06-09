package model.unit;

import model.kingdom.Kingdom;
import model.block.Block;


public abstract class Unit {

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
        if (isDestroyed()) {
            // Logic for unit destruction (e.g., remove from block, remove from kingdom's unit list)
            // This will be handled by a game manager or the Kingdom class.
            // LOGGER.info(unitRank + " at (" + currentBlockLocation.getPositionX() + "," + currentBlockLocation.getPositionY() + ") has been defeated!");
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
        this.currentBlockLocation = newBlock;
        this.hasMoved = true;
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
        return this.getUnitRank().getNextRank() != null;
    }


    public UnitRank getMergedUnitRank() {
        return this.getUnitRank().getNextRank();
    }

    public abstract boolean move(Block targetBlock);
    public abstract int attack(Unit targetUnit);
}