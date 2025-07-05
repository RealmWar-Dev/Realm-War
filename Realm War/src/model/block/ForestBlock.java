package model.block;

import model.kingdom.Kingdom;
import model.structure.Structure;


public class ForestBlock extends Block {

    private static final int FOOD_GENERATION_AMOUNT = 2;
    private static final double ATTACK_BONUS_MULTIPLIER = 1.2;
    private boolean isForestRemoved;


    public ForestBlock(int positionX, int positionY) {
        super(positionX, positionY);
        this.blockType = BlockType.FOREST;
        this.isForestRemoved = false;
    }


    @Override
    public boolean canPlaceStructure() {
        return this.isAbsorbed() && !this.isForestRemoved();
    }


    @Override
    public void setStructure(Structure structure) {
        super.setStructure(structure);
        this.isForestRemoved = true;
    }


    @Override
    public void onAbsorb() {

    }


    @Override
    public void applyTurnEffects() {
        if (isAbsorbed() && getKingdom() != null && !isForestRemoved()) {
            getKingdom().addFood(FOOD_GENERATION_AMOUNT);
        }
    }


    public double getAttackBonusMultiplier() {
        return isForestRemoved ? 1.0 : ATTACK_BONUS_MULTIPLIER;
    }


    public boolean isForestRemoved() {
        return isForestRemoved;
    }
}