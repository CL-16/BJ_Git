package payment;

public class Payment {

    private int totalAmount;      // Current bet
    private int availibleCoins;   // Player's total points

    public Payment(int startingCoins){
        this.availibleCoins = startingCoins;
        this.totalAmount = 0;
    }

    // Add chip value to bet
    public boolean addChip(Chip chip) {
        int chipValue = chip.getValue();

        // Prevent betting more than available points
        if (totalAmount + chipValue > availibleCoins) {
            System.out.println("Cannot bet more than total points!");
            return false;
        }

        totalAmount += chipValue;
        return true;
    }


    public int getTotalAmount(){
        return totalAmount;
    }

    public int getavailibleCoins(){
        return availibleCoins;
    }

    // Player wins -> gain bet * multiplier
    public void winBet(int multiplier){
        availibleCoins += totalAmount * multiplier;
    }

    // Player loses -> subtract bet from total points
    public void loseBet(){
        availibleCoins -= totalAmount;
    }

    // Push = no change
    public void pushBet(){
        // no action required
    }

    public void confirmBet(){
        System.out.println("Bet confirmed: " + totalAmount);
    }

    // Clamp bet so it never exceeds player's points
    public void clampBet() {
        if (totalAmount > availibleCoins) {
            totalAmount = availibleCoins;
        }
    }
}
