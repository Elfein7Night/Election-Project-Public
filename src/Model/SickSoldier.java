package Model;

public class SickSoldier extends Soldier implements Sickness {
    private boolean wearingProtectionSuit;
    private int numOfSicknessDays;

    //---------------Used For TableView---------------//
    @SuppressWarnings({"unused", "RedundantSuppression"})
    public int getNumOfSicknessDays() {
        return numOfSicknessDays;
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public boolean isWearingProtectionSuit() {
        return wearingProtectionSuit;
    }
    //------------------------------------------------//

    public SickSoldier() throws MyException {
        super();
        setSickParams(HelperUtilities.getHighChanceBoolean(), HelperUtilities.getRandomInt(MIN_DAYS, MAX_DAYS));
    }


    public SickSoldier(String _name, int _id, int _yearOfBirth, boolean _isWearingProtectionSuit,
                       int _numOfSicknessDays, boolean _carryWeapon) throws MyException {
        super(_name, _id, _yearOfBirth, _carryWeapon);
        setSickParams(_isWearingProtectionSuit, _numOfSicknessDays);
    }

    public void setSickParams(boolean _isWearingProtectionSuit, int _numOfSicknessDays) {
        setNumOfSicknessDays(_numOfSicknessDays);
        setWearingProtectionSuit(_isWearingProtectionSuit);
        //Has to wear protection suit, be in quarantine for at least 2 weeks, and not carry a weapon.
        setCanVote(wearingProtectionSuit && numOfSicknessDays >= MIN_QUARANTINE_DAYS_TO_VOTE && !carryWeapon);
    }

    public void setWearingProtectionSuit(boolean wearingProtectionSuit) {
        this.wearingProtectionSuit = wearingProtectionSuit;
    }

    public void setNumOfSicknessDays(int numOfSicknessDays) {
        this.numOfSicknessDays = numOfSicknessDays;
    }

//Implemented as required in project specifications but not actually used
// --Commented out by Inspection START (21/08/2020 11:02):
//    public boolean equals(SickSoldier other) {
//        return super.equals(other) && numOfSicknessDays == other.numOfSicknessDays &&
//                wearingProtectionSuit == other.wearingProtectionSuit;
//    }
// --Commented out by Inspection STOP (21/08/2020 11:02)

    public String toString() {
        return super.toString() + " | numOfSicknessDays: " + numOfSicknessDays +
                " | isWearingProtectionSuit: " + wearingProtectionSuit;
    }
}
