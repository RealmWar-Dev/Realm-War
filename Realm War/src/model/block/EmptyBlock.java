package model.block;

import model.kingdom.Kingdom;
import model.structure.Structure;

public class EmptyBlock extends Block {

    private static final int GOLD_GENERATION_AMOUNT = 1; // Example: 1 gold per turn


    public EmptyBlock(int positionX, int positionY) {
        super(positionX, positionY);
        this.blockType = BlockType.EMPTY;
    }


    @Override
    public boolean canPlaceStructure() {
        return this.isAbsorbed();
    }


    @Override
    public void onAbsorb() {

    }


    @Override
    public void applyTurnEffects() {
        if (isAbsorbed() && getKingdom() != null) {
            getKingdom().addGold(GOLD_GENERATION_AMOUNT);
        }
    }
}