package model.unit;


public enum UnitRank {
    PEASANT,
    SPEARMAN,
    SWORDMAN,
    KNIGHT;


    public UnitRank getNextRank() {
        if (this.ordinal() < UnitRank.values().length - 1) {
            return UnitRank.values()[this.ordinal() + 1];
        }
        return null; 
    }
}