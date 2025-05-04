package model.block;

public class CastleBlock extends Block {

    public CastleBlock(int x, int y) {
        super(x, y);
        this.canBuildStructure = false;
        this.canMoveThrough = true;
        this.canBeAbsorbed = false;
        this.resourceType = ResourceType.NONE;
    }

    @Override
    public String getBlockType() {
        return "CastleBlock";
    }
}

