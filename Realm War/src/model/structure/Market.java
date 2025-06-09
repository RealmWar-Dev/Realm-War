// src/model/structure/Market.java
package model.structure;

import model.kingdom.Kingdom;
import model.block.Block;


public class Market extends Structure {

    private static final int BASE_HEALTH = 50;
    private static final int BASE_MAINTENANCE_COST = 5;
    private static final int MAX_LEVEL_MARKET = 3;
    private static final int BASE_GOLD_PRODUCTION = 5;
    private static final int BASE_BUILDING_COST = 5;
    private static final int BASE_LEVEL_UP_COST = 5;

    private static final int GOLD_PER_LEVEL = 3;
    private static final int HEALTH_PER_LEVEL = 20;
    private static final int LEVEL_UP_COST_INCREMENT = 5;

    private int currentGoldProduction;


    public Market(Kingdom ownerKingdom, Block locationBlock) {
        super(BASE_HEALTH, MAX_LEVEL_MARKET, BASE_MAINTENANCE_COST,
                BASE_BUILDING_COST, BASE_LEVEL_UP_COST,
                ownerKingdom, locationBlock);
        this.structureType = StructureType.MARKET;
        this.currentGoldProduction = BASE_GOLD_PRODUCTION;
    }


    @Override
    public int calculateMaintenanceCost() {
        return baseMaintenanceCost;
    }


    @Override
    public void applyTurnEffects() {
        if (ownerKingdom != null && !isDestroyed()) {
            ownerKingdom.addGold(currentGoldProduction);
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
        this.currentGoldProduction += GOLD_PER_LEVEL;
        this.maxHealth += HEALTH_PER_LEVEL;
        this.currentHealth = this.maxHealth;
        this.levelUpCost += LEVEL_UP_COST_INCREMENT;
    }


    public int getCurrentGoldProduction() {
        return currentGoldProduction;
    }
}