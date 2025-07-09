package model.structure;

import model.kingdom.Kingdom;
import model.block.Block;
import model.unit.Unit;
import model.unit.UnitRank;


public class Tower extends Structure {

    private static final int BASE_HEALTH = 50;
    private static final int BASE_MAINTENANCE_COST = 5;
    private static final int MAX_LEVEL_TOWER = 3;
    private static final int BASE_BUILDING_COST = 5;
    private static final int BASE_LEVEL_UP_COST = 5;

    private static final int HEALTH_PER_LEVEL = 20;
    private static final int LEVEL_UP_COST_INCREMENT = 5;


    private UnitRank maxUnitRankRestricted;


    public Tower(Kingdom ownerKingdom, Block locationBlock) {
        super(BASE_HEALTH, MAX_LEVEL_TOWER, BASE_MAINTENANCE_COST,
                BASE_BUILDING_COST, BASE_LEVEL_UP_COST,
                ownerKingdom, locationBlock);
        this.structureType = StructureType.TOWER;
        initializeRestrictionRank();
    }


    private void initializeRestrictionRank() {
        UnitRank[] ranks = UnitRank.values();
        int rankIndex = Math.min(this.level - 1, ranks.length - 1);
        this.maxUnitRankRestricted = ranks[rankIndex];
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
        this.maxHealth += HEALTH_PER_LEVEL;
        this.currentHealth = this.maxHealth;
        this.levelUpCost += LEVEL_UP_COST_INCREMENT;
        initializeRestrictionRank();
    }


    public boolean restrictsUnit(Unit unit) {
        if (unit == null) return false;
        return unit.getUnitRank().ordinal() <= this.maxUnitRankRestricted.ordinal();
    }


    public UnitRank getMaxUnitRankRestricted() {
        return maxUnitRankRestricted;
    }
}