package model.block;

public class SpawnBlock extends Block {

    public SpawnBlock(int x, int y) {
        super(x, y);
        this.canBuildStructure = false;
        this.canMoveThrough = true;
        this.canBeAbsorbed = true;
        this.resourceType = ResourceType.NONE;
    }

    @Override
    public String getBlockType() {
        return "SpawnBlock";
    }
}

