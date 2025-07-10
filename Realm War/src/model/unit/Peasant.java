package model.unit;

import model.kingdom.Kingdom;
import model.block.Block;
import model.block.ForestBlock;


public class Peasant extends Unit {

    private static final int BASE_HEALTH = 20;
    private static final int BASE_MOVEMENT_RANGE = 2;
    private static final int BASE_ATTACK_POWER = 5;
    private static final int BASE_ATTACK_RANGE = 1; // Melee unit
    private static final int BASE_PRODUCTION_COST_GOLD = 5;
    private static final int BASE_MAINTENANCE_COST_FOOD = 1;
    private static final int BASE_UNIT_SPACE_OCCUPIED = 1;

    public Peasant(Kingdom ownerKingdom, Block currentBlockLocation) {
        super(BASE_HEALTH, BASE_MOVEMENT_RANGE, BASE_ATTACK_POWER, BASE_ATTACK_RANGE,
                BASE_PRODUCTION_COST_GOLD, BASE_MAINTENANCE_COST_FOOD, BASE_UNIT_SPACE_OCCUPIED,
                ownerKingdom, currentBlockLocation, UnitRank.PEASANT);
    }

    @Override
    public boolean move(Block targetBlock) {
        // Use the common move logic from the Unit abstract class
        return super.move(targetBlock);
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

        // Apply Forest Block attack advantage
        if (currentBlockLocation instanceof ForestBlock) {
            ForestBlock forestBlock = (ForestBlock) currentBlockLocation;
            if (forestBlock.isForestRemoved()) {
                effectiveAttackPower = (int) (effectiveAttackPower * forestBlock.getAttackBonusMultiplier());
            }
        }

        // Apply Forest Block defense advantage to target
        if (targetUnit.getCurrentBlockLocation() instanceof ForestBlock) {
            ForestBlock targetForestBlock = (ForestBlock) targetUnit.getCurrentBlockLocation();
            if (targetForestBlock.isForestRemoved()) {
                effectiveAttackPower = (int) (effectiveAttackPower / targetForestBlock.getAttackBonusMultiplier());
            }
        }

        targetUnit.takeDamage(effectiveAttackPower);
        this.hasAttacked = true;
        return effectiveAttackPower;
    }
}