package model.structure;

import model.kingdom.Kingdom;
import model.block.Block;


public class Barrack extends Structure {

    private static final int BASE_HEALTH = 50;
    private static final int BASE_MAINTENANCE_COST = 5;
    private static final int MAX_LEVEL_BARRACK = 3;
    private static final int BASE_UNIT_SPACE = 5;
    private static final int BASE_BUILDING_COST = 5;
    private static final int BASE_LEVEL_UP_COST = 5;

    private static final int UNIT_SPACE_PER_LEVEL = 3;
    private static final int HEALTH_PER_LEVEL = 20;
    private static final int LEVEL_UP_COST_INCREMENT = 5;

    private int currentUnitSpaceProvided;


    public Barrack(Kingdom ownerKingdom, Block locationBlock) {
        super(BASE_HEALTH, MAX_LEVEL_BARRACK, BASE_MAINTENANCE_COST,
                BASE_BUILDING_COST, BASE_LEVEL_UP_COST,
                ownerKingdom, locationBlock);
        this.structureType = StructureType.BARRACK;
        this.currentUnitSpaceProvided = BASE_UNIT_SPACE;
    }


    @Override
    public int calculateMaintenanceCost() {
        return baseMaintenanceCost;
    }


    @Override
    public void applyTurnEffects() {

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
        this.currentUnitSpaceProvided += UNIT_SPACE_PER_LEVEL;
        this.maxHealth += HEALTH_PER_LEVEL;
        this.currentHealth = this.maxHealth;
        this.levelUpCost += LEVEL_UP_COST_INCREMENT;
    }


    public int getCurrentUnitSpaceProvided() {
        return currentUnitSpaceProvided;
    }
}