package model.block;

public class GrassBlock extends Block {

    public GrassBlock(int x, int y) {
        super(x, y);
        this.canBuildStructure = true;
        this.canMoveThrough = true;
        this.canBeAbsorbed = true;
        this.resourceType = ResourceType.GOLD;
    }

    @Override
    public String getBlockType() {
        return "GrassBlock";
    }
}

