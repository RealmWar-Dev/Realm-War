package model.structure;

import model.kingdom.Kingdom;
import model.block.Block;


public class Farm extends Structure {

    private static final int BASE_HEALTH = 50;
    private static final int BASE_MAINTENANCE_COST = 5;
    private static final int MAX_LEVEL_FARM = 3;
    private static final int BASE_FOOD_PRODUCTION = 5;
    private static final int BASE_BUILDING_COST = 5;
    private static final int BASE_LEVEL_UP_COST = 5; // For level 1 to 2

    private static final int FOOD_PER_LEVEL = 3;
    private static final int HEALTH_PER_LEVEL = 20;
    private static final int LEVEL_UP_COST_INCREMENT = 5;

    private int currentFoodProduction;

    public Farm(Kingdom ownerKingdom, Block locationBlock) {
        super(BASE_HEALTH, MAX_LEVEL_FARM, BASE_MAINTENANCE_COST,
                BASE_BUILDING_COST, BASE_LEVEL_UP_COST,
                ownerKingdom, locationBlock);
        this.structureType = StructureType.FARM;
        this.currentFoodProduction = BASE_FOOD_PRODUCTION;
    }


    @Override
    public int calculateMaintenanceCost() {
        return baseMaintenanceCost;
    }


    @Override
    public void applyTurnEffects() {
        if (ownerKingdom != null && !isDestroyed()) {
            ownerKingdom.addFood(currentFoodProduction);
        }
    }


    @Override
    public boolean levelUp() {
        if (super.levelUp()) {
            applyLevelUpEffects();
            return true;
        }
        return false;
    }


    @Override
    protected void applyLevelUpEffects() {
        this.currentFoodProduction += FOOD_PER_LEVEL;
        this.maxHealth += HEALTH_PER_LEVEL;
        this.currentHealth = this.maxHealth;
        this.levelUpCost += LEVEL_UP_COST_INCREMENT;
    }


    public int getCurrentFoodProduction() {
        return currentFoodProduction;
    }
}