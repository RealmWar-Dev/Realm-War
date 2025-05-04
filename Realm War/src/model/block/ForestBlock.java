package model.block;

public class ForestBlock extends Block {

    private boolean forestIntact;

    public ForestBlock(int x, int y) {
        super(x, y);
        this.canBuildStructure = true; // but loses forest
        this.canMoveThrough = true;
        this.canBeAbsorbed = true;
        this.resourceType = ResourceType.FOOD;
        this.forestIntact = true;
    }

    public boolean isForestIntact() {
        return forestIntact;
    }

    public void buildStructure() {
        this.forestIntact = false;
        this.resourceType = ResourceType.NONE;
    }

    @Override
    public String getBlockType() {
        return "ForestBlock";
    }
}

