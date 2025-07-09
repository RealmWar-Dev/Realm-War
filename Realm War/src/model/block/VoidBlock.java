package model.block;


public class VoidBlock extends Block {

    public VoidBlock(int positionX, int positionY) {
        super(positionX, positionY);
        this.blockType = BlockType.VOID;
        // Void blocks cannot be absorbed
        this.isAbsorbed = false;
        this.kingdom = null;
    }


    @Override
    public boolean canPlaceStructure() {
        return false;
    }


    @Override
    public void onAbsorb() {

    }

    /**
     * Void blocks generate no resources and have no per-turn effects.
     */
    @Override
    public void applyTurnEffects() {
        // No effects for Void blocks
    }

}