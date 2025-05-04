package model.block;

import player.Player;

public abstract class Block {
    protected int x, y;
    protected boolean isOccupied;
    protected Player owner;
    protected boolean canBuildStructure;
    protected boolean canMoveThrough;
    protected boolean canBeAbsorbed;
    protected ResourceType resourceType;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.isOccupied = false;
        this.owner = null;
    }

    public abstract String getBlockType();

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { this.isOccupied = occupied; }

    public Player getOwner() { return owner; }
    public void setOwner(Player owner) { this.owner = owner; }

    public boolean canBuildStructure() { return canBuildStructure; }
    public boolean canMoveThrough() { return canMoveThrough; }
    public boolean canBeAbsorbed() { return canBeAbsorbed; }

    public ResourceType getResourceType() { return resourceType; }

    @Override
    public String toString() {
        return getBlockType() + " at (" + x + "," + y + ")";
    }

    public enum ResourceType {
        GOLD,
        FOOD,
        NONE
    }

}

