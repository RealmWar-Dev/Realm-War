package model.block;

import model.kingdom.Kingdom;
import model.structure.Structure;
import model.unit.Unit;


public abstract class Block {

    protected int positionX;
    protected int positionY;
    protected boolean isAbsorbed;
    protected Kingdom kingdom;
    protected Structure structure;
    protected Unit unit;
    protected BlockType blockType;


    public Block(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.isAbsorbed = false;
        this.kingdom = null;
        this.structure = null;
        this.unit = null;
    }



    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public boolean isAbsorbed() {
        return isAbsorbed;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public Structure getStructure() {
        return structure;
    }

    public Unit getUnit() {
        return unit;
    }

    public BlockType getBlockType() {
        return blockType;
    }


    public void setAbsorbed(boolean absorbed) {
        isAbsorbed = absorbed;
    }


    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
        this.isAbsorbed = (kingdom != null);
    }


    public void setStructure(Structure structure) {
        if (this.structure != null) {
            throw new IllegalStateException("A structure already exists on this block.");
        }
        if (this.unit != null) {
            throw new IllegalStateException("Cannot place a structure on a block with a unit.");
        }
        if (!canPlaceStructure()) {
            throw new IllegalStateException("Cannot place a structure on this type of block: " + this.blockType);
        }
        this.structure = structure;
    }



    public void removeStructure() {
        this.structure = null;
    }


    public void setUnit(Unit unit) {
        if (this.unit != null) {
            throw new IllegalStateException("A unit already exists on this block.");
        }
        if (this.structure != null) {
            throw new IllegalStateException("Cannot place a unit on a block with a structure.");
        }
        this.unit = unit;
    }



    public void removeUnit() {
        this.unit = null;
    }




    public abstract boolean canPlaceStructure();


    public abstract void onAbsorb();


    public abstract void applyTurnEffects();



    public enum BlockType {
        VOID,
        EMPTY,
        FOREST,
        CASTLE,
        GOLD_MINE,
        MOUNTAIN,
        SPAWN,
        WATER
    }
}