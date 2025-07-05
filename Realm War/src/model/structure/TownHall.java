package model.structure;

import model.kingdom.Kingdom;
import model.block.Block;

public class TownHall extends Structure {

    private static final int BASE_HEALTH = 50;
    private static final int MAX_LEVEL = 1;
    private static final int MAINTENANCE_COST = 0;
    private static final int BUILDING_COST = 0;
    private static final int INITIAL_LEVEL_UP_COST = 0;

    private static final int GOLD_PRODUCTION = 5;
    private static final int FOOD_PRODUCTION = 5;
    private static final int UNIT_SPACE_PROVIDED = 5;


    public TownHall(Kingdom ownerKingdom, Block locationBlock) {
        super(BASE_HEALTH, MAX_LEVEL, MAINTENANCE_COST, BUILDING_COST, INITIAL_LEVEL_UP_COST, ownerKingdom, locationBlock);
        this.structureType = StructureType.TOWN_HALL;
    }


    @Override
    public int calculateMaintenanceCost() {
        return MAINTENANCE_COST;
    }


    @Override
    public void applyTurnEffects() {
        if (ownerKingdom != null && !isDestroyed()) {
            ownerKingdom.addGold(GOLD_PRODUCTION);
            ownerKingdom.addFood(FOOD_PRODUCTION);
            // Unit space is typically a fixed value added to the kingdom's total capacity
            // and managed by the Kingdom itself or a UnitManager.
            // No direct unit production happens here, as it's typically player-initiated.
        }
    }


    @Override
    public boolean levelUp() {
        // System.out.println("Town Hall cannot be leveled up as its max level is 1.");
        return false;
    }


    @Override
    protected void applyLevelUpEffects() {
    }


    public int getUnitSpaceProvided() {
        return UNIT_SPACE_PROVIDED;
    }
}