package org.example;

import java.util.HashMap;
import java.util.Map;

public class ATM {

    private Map<Integer, Integer> availableNotes;

    public ATM() {
        // Initialize available banknote denominations and their quantities
        availableNotes = new HashMap<>();
        availableNotes.put(100, 10);
        availableNotes.put(50, 10);
        availableNotes.put(20, 20);
        availableNotes.put(10, 20);
        availableNotes.put(5, 50);
        availableNotes.put(1, 100);
    }
    public synchronized boolean withdrawCash(int amount) {
        Map<Integer, Integer> withdrawal = new HashMap<>();
        // Check if ATM has sufficient cash for withdrawal
        if (getTotalCash() < amount) {
            System.out.println("Sorry, the ATM does not have enough cash.");
            return false;
        }

        // Iterate through each denomination to determine the number of banknotes to dispense
        for (Map.Entry<Integer, Integer> entry : availableNotes.entrySet()) {
            int denomination = entry.getKey();
            int quantity = entry.getValue();

            // Calculate the number of banknotes of this denomination to dispense
            int numNotesToDispense = Math.min(amount / denomination, quantity);

            // Reduce the available quantity of this denomination
            availableNotes.put(denomination, quantity - numNotesToDispense);

            // Reduce the remaining amount by the value of the dispensed banknotes
            amount -= numNotesToDispense * denomination;

            // Add the dispensed banknotes to the withdrawal map
            if (numNotesToDispense > 0) {
                withdrawal.put(denomination, numNotesToDispense);
            }

            // If the remaining amount is zero, break the loop
            if (amount == 0) {
                break;
            }
        }

        // If there's still remaining amount, it means the ATM cannot dispense requested amount
        if (amount > 0) {
            System.out.println("Sorry, the ATM cannot dispense the requested amount.");
            // Rollback the changes
            rollbackWithdrawal(withdrawal);
            return false;
        } else {
            // Dispense cash
            System.out.println("Dispensing cash:");
            for (Map.Entry<Integer, Integer> entry : withdrawal.entrySet()) {
                System.out.println(entry.getValue() + " x $" + entry.getKey());
            }
            return true;
        }
    }

    private int getTotalCash() {
        int total = 0;
        for (Map.Entry<Integer, Integer> entry : availableNotes.entrySet()) {
            total += entry.getKey() * entry.getValue();
        }
        return total;
    }
    private void rollbackWithdrawal(Map<Integer, Integer> withdrawal) {
        for (Map.Entry<Integer, Integer> entry : withdrawal.entrySet()) {
            int denomination = entry.getKey();
            int quantity = entry.getValue();
            availableNotes.put(denomination, availableNotes.get(denomination) + quantity);
        }
    }
    public static void main(String[] args) {
            ATM atm = new ATM();
            // Example usage
            atm.withdrawCash(130);
    }
}

