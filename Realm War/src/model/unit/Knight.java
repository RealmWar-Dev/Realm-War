package model.unit;

import model.kingdom.Kingdom;
import model.block.Block;
import model.block.ForestBlock;


public class Knight extends Unit {

    private static final int BASE_HEALTH = 120;
    private static final int BASE_MOVEMENT_RANGE = 3;
    private static final int BASE_ATTACK_POWER = 25;
    private static final int BASE_ATTACK_RANGE = 1;
    private static final int BASE_PRODUCTION_COST_GOLD = 60;
    private static final int BASE_MAINTENANCE_COST_FOOD = 10;
    private static final int BASE_UNIT_SPACE_OCCUPIED = 2;


    public Knight(Kingdom ownerKingdom, Block currentBlockLocation) {
        super(BASE_HEALTH, BASE_MOVEMENT_RANGE, BASE_ATTACK_POWER, BASE_ATTACK_RANGE,
                BASE_PRODUCTION_COST_GOLD, BASE_MAINTENANCE_COST_FOOD, BASE_UNIT_SPACE_OCCUPIED,
                ownerKingdom, currentBlockLocation, UnitRank.KNIGHT);
    }


    @Override
    public boolean move(Block targetBlock) {
        if (hasMoved) {
            return false;
        }
        if (targetBlock == null || targetBlock.getBlockType() == Block.BlockType.VOID ||
                (targetBlock.getKingdom() != null && targetBlock.getKingdom() != ownerKingdom)) {
            return false;
        }

        int distance = calculateDistance(currentBlockLocation, targetBlock);
        if (distance > movementRange) {
            return false;
        }

        this.currentBlockLocation.removeUnit();
        this.currentBlockLocation = targetBlock;
        this.currentBlockLocation.setUnit(this);
        this.hasMoved = true;
        return true;
    }


    @Override
    public int attack(Unit targetUnit) {

        if (hasAttacked) {
            return 0;
        }
        if (targetUnit == null || targetUnit.getOwnerKingdom() == ownerKingdom) {
            return 0;
        }

        int distance = calculateDistance(currentBlockLocation, targetUnit.getCurrentBlockLocation());
        if (distance > attackRange) {
            return 0;
        }

        int effectiveAttackPower = this.attackPower;

        if (currentBlockLocation instanceof ForestBlock) {
            ForestBlock forestBlock = (ForestBlock) currentBlockLocation;
            if (!forestBlock.isForestRemoved()) {
                effectiveAttackPower = (int) (effectiveAttackPower * forestBlock.getAttackBonusMultiplier());
            }
        }

        if (targetUnit.getCurrentBlockLocation() instanceof ForestBlock) {
            ForestBlock targetForestBlock = (ForestBlock) targetUnit.getCurrentBlockLocation();
            if (!targetForestBlock.isForestRemoved()) {
                effectiveAttackPower = (int) (effectiveAttackPower / targetForestBlock.getAttackBonusMultiplier());
            }
        }

        targetUnit.takeDamage(effectiveAttackPower);
        this.hasAttacked = true;
        return effectiveAttackPower;
    }
}