package model.structure;

import model.kingdom.Kingdom;
import model.block.Block;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Structure {

    private static final Logger LOGGER = Logger.getLogger(Structure.class.getName());

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

        LOGGER.log(Level.INFO, "{0} {1} at ({2},{3}) took {4} damage. Current health: {5}",
                new Object[]{ownerKingdom.getName(), structureType, locationBlock.getPositionX(),
                        locationBlock.getPositionY(), damage, currentHealth});

        if (isDestroyed()) {
            LOGGER.log(Level.INFO, "{0} {1} at ({2},{3}) has been destroyed!",
                    new Object[]{ownerKingdom.getName(), structureType, locationBlock.getPositionX(), locationBlock.getPositionY()});
            // Notify the kingdom to remove this structure
            if (ownerKingdom != null) {
                ownerKingdom.removeStructure(this);
            }
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
        LOGGER.log(Level.INFO, "{0} {1} at ({2},{3}) recovered {4} health. Current health: {5}",
                new Object[]{ownerKingdom.getName(), structureType, locationBlock.getPositionX(),
                        locationBlock.getPositionY(), amount, currentHealth});
    }

    /**
     * Attempts to level up the structure. Checks if max level is reached and
     * if the owning kingdom has enough gold.
     * @return true if leveled up successfully, false otherwise.
     */
    public boolean levelUp() {
        if (this.level >= this.maxLevel) {
            LOGGER.log(Level.INFO, "{0} {1} at ({2},{3}) cannot level up: Max level ({4}) already reached.",
                    new Object[]{ownerKingdom.getName(), structureType, locationBlock.getPositionX(), locationBlock.getPositionY(), maxLevel});
            return false;
        }

        // Check if ownerKingdom has enough gold for levelUpCost.
        if (ownerKingdom != null && !ownerKingdom.deductGold(this.levelUpCost)) {
            LOGGER.log(Level.INFO, "{0} cannot level up {1}: Insufficient gold (need {2}, have {3}).",
                    new Object[]{ownerKingdom.getName(), structureType, levelUpCost, ownerKingdom.getGold()});
            return false;
        }

        this.level++;
        applyLevelUpEffects(); // Apply specific effects for the subclass
        LOGGER.log(Level.INFO, "{0} {1} at ({2},{3}) leveled up to level {4}. Next level up cost: {5}",
                new Object[]{ownerKingdom.getName(), structureType, locationBlock.getPositionX(),
                        locationBlock.getPositionY(), level, levelUpCost});
        return true;
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