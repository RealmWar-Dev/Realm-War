package model.structure;

import model.kingdom.Kingdom;
import model.block.Block;


public abstract class Structure {

    protected int currentHealth;
    protected int maxHealth;
    protected int level;
    protected final int maxLevel;
    protected int baseMaintenanceCost;
    protected int buildingCost;
    protected int levelUpCost;
    protected Kingdom ownerKingdom;
    protected Block locationBlock;
    protected StructureType structureType;


    public Structure(int initialHealth, int maxLevel, int baseMaintenanceCost,
                     int buildingCost, int initialLevelUpCost,
                     Kingdom ownerKingdom, Block locationBlock) {
        if (initialHealth <= 0 || maxLevel <= 0 || baseMaintenanceCost < 0 || buildingCost < 0 || initialLevelUpCost < 0) {
            throw new IllegalArgumentException("Structure properties must be non-negative and positive where required.");
        }
        if (ownerKingdom == null || locationBlock == null) {
            throw new IllegalArgumentException("Owner kingdom and location block cannot be null.");
        }

        this.currentHealth = initialHealth;
        this.maxHealth = initialHealth;
        this.level = 1;
        this.maxLevel = maxLevel;
        this.baseMaintenanceCost = baseMaintenanceCost;
        this.buildingCost = buildingCost;
        this.levelUpCost = initialLevelUpCost;
        this.ownerKingdom = ownerKingdom;
        this.locationBlock = locationBlock;
        this.structureType = StructureType.GENERIC;
    }



    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getBuildingCost() {
        return buildingCost;
    }

    public int getLevelUpCost() {
        return levelUpCost;
    }

    public Kingdom getOwnerKingdom() {
        return ownerKingdom;
    }

    public Block getLocationBlock() {
        return locationBlock;
    }

    public StructureType getStructureType() {
        return structureType;
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
            // Handle destruction logic, perhaps notify Kingdom or remove from Block
            // This is where a game manager or Kingdom class would handle cleanup.
        }
    }


    public boolean isDestroyed() {
        return this.currentHealth <= 0;
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

    public boolean levelUp() {
        if (this.level < this.maxLevel) {
            // Check if ownerKingdom has enough gold for levelUpCost.
            // This logic will likely be managed by a BuildingManager or Kingdom class
            // which calls this method after verifying resources.
            this.level++;
            // Update properties based on new level (e.g., increased max health, changed maintenance)
            // This part is structure-specific and will be implemented in subclasses.
            return true;
        }
        return false;
    }




    public abstract int calculateMaintenanceCost();


    public abstract void applyTurnEffects();


    protected abstract void applyLevelUpEffects();



    public enum StructureType {
        GENERIC, // Default, should be overridden
        TOWN_HALL,
        BARRACK,
        FARM,
        MARKET,
        TOWER
    }
}