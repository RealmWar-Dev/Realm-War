package model.block;

public class GoldMineBlock extends Block {

    public GoldMineBlock(int x, int y) {
        super(x, y);
        this.canBuildStructure = true;
        this.canMoveThrough = true;
        this.canBeAbsorbed = true;
        this.resourceType = ResourceType.GOLD;
    }

    @Override
    public String getBlockType() {
        return "GoldMineBlock";
    }
}

