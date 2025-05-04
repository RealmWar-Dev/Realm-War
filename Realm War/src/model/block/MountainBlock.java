package model.block;

public class MountainBlock extends Block {

    public MountainBlock(int x, int y) {
        super(x, y);
        this.canBuildStructure = false;
        this.canMoveThrough = false;
        this.canBeAbsorbed = false;
        this.resourceType = ResourceType.NONE;
    }

    @Override
    public String getBlockType() {
        return "MountainBlock";
    }
}

