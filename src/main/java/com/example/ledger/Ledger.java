package com.example.ledger;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Represents a ledger that tracks financial entries with dates, descriptions,
 * and amounts.
 * 
 * @author Zakariya Mohamed
 */
public class Ledger {

  /** Default maximum number of entries in the ledger. */
  public static final int DEFAULT_MAX_SIZE = 30;

  /** Maximum number of entries in the ledger. */
  private int size;

  /** Number of entries currently in the ledger. */
  private int numEntries;

  /** Array tracking balance after each entry. */
  private int[] balances;

  /** Current balance of the ledger. */
  private int balance;

  /** Array storing the ledger entries. */
  private LedgerEntry[] entries;

  /**
   * Creates a ledger with default maximum size.
   */
  public Ledger() {
    this.size = DEFAULT_MAX_SIZE;
    this.numEntries = 0;
    this.balance = 0;
    this.entries = new LedgerEntry[DEFAULT_MAX_SIZE];
    this.balances = new int[DEFAULT_MAX_SIZE];
  }

  /**
   * Creates a ledger with specified maximum size.
   *
   * @param size the maximum number of entries
   * @throws IllegalArgumentException if size is non-positive
   */
  public Ledger(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("Invalid size");
    }
    this.size = size;
    this.numEntries = 0;
    this.balance = 0;
    this.entries = new LedgerEntry[size];
    this.balances = new int[size];
  }

  /**
   * Checks if ledger is full.
   *
   * @return true if ledger is full, false otherwise
   */
  public boolean isFull() {
    return size == numEntries;
  }

  /**
   * Gets the maximum size of the ledger.
   *
   * @return the maximum number of entries
   */
  public int getSize() {
    return size;
  }

  /**
   * Gets the current number of entries.
   *
   * @return number of entries
   */
  public int getNumEntries() {
    return numEntries;
  }

  /**
   * Gets the current balance.
   *
   * @return current balance
   */
  public int getBalance() {
    return balance;
  }

  /**
   * Gets description at specified index.
   *
   * @param i the index
   * @return description at index
   * @throws IllegalArgumentException if index is invalid
   */
  public String getDescriptionAt(int i) {
    if (i < 0 || i >= numEntries) {
      throw new IllegalArgumentException("Invalid index");
    }
    return entries[i].getDescription();
  }

  /**
   * Gets date at specified index.
   *
   * @param i the index
   * @return date at index
   * @throws IllegalArgumentException if index is invalid
   */
  public LedgerDate getDateAt(int i) {
    if (i < 0 || i >= numEntries) {
      throw new IllegalArgumentException("Invalid index");
    }
    return entries[i].getDate();
  }

  /**
   * Gets amount at specified index.
   *
   * @param i the index
   * @return amount at index
   * @throws IllegalArgumentException if index is invalid
   */
  public int getAmountAt(int i) {
    if (i < 0 || i >= numEntries) {
      throw new IllegalArgumentException("Invalid index");
    }
    return entries[i].getAmount();
  }

  /**
   * Gets balance at specified index.
   *
   * @param i the index
   * @return balance at index
   * @throws IllegalArgumentException if index is invalid
   */
  public int getBalanceAt(int i) {
    if (i < 0 || i >= numEntries) {
      throw new IllegalArgumentException("Invalid index");
    }
    return balances[i];
  }

  /**
   * Sorts entries and updates balances.
   */
  private void sort() {
    Arrays.sort(entries, entryCompWithNull);

    int runningBalance = 0;
    for (int i = 0; i < numEntries; i++) {
      runningBalance += entries[i].getAmount();
      balances[i] = runningBalance;
    }
  }

  /**
   * Adds an entry to the ledger.
   *
   * @param entry the entry to add
   * @throws IllegalArgumentException if entry is null or ledger is full
   */
  public void addEntry(LedgerEntry entry) {
    if (entry == null) {
      throw new IllegalArgumentException("Null entry");
    }
    if (isFull()) {
      throw new IllegalArgumentException("Ledger is full");
    }

    entries[numEntries] = entry;
    numEntries++;
    sort();
    balance += entry.getAmount();
    balances[numEntries - 1] = balance;
  }

  /**
   * Creates and adds an entry to the ledger.
   *
   * @param month       the month
   * @param day         the day
   * @param year        the year
   * @param description the description
   * @param amount      the amount
   * @throws IllegalArgumentException for invalid parameters or full ledger
   */
  public void addEntry(int month, int day, int year, String description,
      int amount) {
    if (year < LedgerDate.MIN_YEAR || year > LedgerDate.MAX_YEAR) {
      throw new IllegalArgumentException("Invalid date");
    }
    if (description == null) {
      throw new IllegalArgumentException("Null description");
    }
    if (description.trim().isEmpty()) {
      throw new IllegalArgumentException("Empty or all whitespace description");
    }
    if (amount == 0) {
      throw new IllegalArgumentException("Amount is zero");
    }
    if (isFull()) {
      throw new IllegalArgumentException("Ledger is full");
    }

    LedgerEntry entry = new LedgerEntry(month, day, year, description, amount);
    entries[numEntries] = entry;
    numEntries++;
    balance += amount;
    sort();
    balances[numEntries - 1] = balance;
  }

  /**
   * Gets a ledger containing only credit entries.
   *
   * @return ledger with credit entries
   */
  public Ledger getCredits() {
    Ledger creditsLedger = new Ledger(getSize());
    for (int i = 0; i < numEntries; i++) {
      if (entries[i].getAmount() > 0) {
        creditsLedger.addEntry(entries[i]);
      }
    }
    return creditsLedger;
  }

  /**
   * Gets a ledger containing only debit entries.
   *
   * @return ledger with debit entries
   */
  public Ledger getDebits() {
    Ledger debitsLedger = new Ledger(getSize());
    for (int i = 0; i < numEntries; i++) {
      if (entries[i].getAmount() < 0) {
        debitsLedger.addEntry(entries[i]);
      }
    }
    return debitsLedger;
  }

  /**
   * Gets entries within a date range.
   *
   * @param start the start date
   * @param end   the end date
   * @return ledger with entries in range
   * @throws IllegalArgumentException if dates are null
   */
  public Ledger getDateRange(LedgerDate start, LedgerDate end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Null date");
    }

    Ledger dateRangeLedger = new Ledger(getSize());
    for (int i = 0; i < numEntries; i++) {
      LedgerDate entryDate = entries[i].getDate();
      if (entryDate.compareTo(start) >= 0 && entryDate.compareTo(end) <= 0) {
        dateRangeLedger.addEntry(entries[i]);
      }
    }
    return dateRangeLedger;
  }

  /**
   * Gets entry at specified index.
   *
   * @param index the index
   * @return the entry
   * @throws IllegalArgumentException if index is invalid
   */
  public LedgerEntry getEntry(int index) {
    if (index < 0 || index >= numEntries) {
      throw new IllegalArgumentException("Invalid index");
    }
    return entries[index];
  }

  /**
   * Returns string representation of ledger.
   *
   * @return string representation
   */
  public String toString() {
    String result = "Date,Description,Amount,Balance\n";

    for (int i = 0; i < numEntries; i++) {
      LedgerEntry entry = entries[i];
      int year = entry.getDate().getYear();
      int month = entry.getDate().getMonth();
      int day = entry.getDate().getDay();

      String formattedDate = String.format("%04d%02d%02d", year, month, day);
      result += formattedDate + ","
          + entry.getDescription() + ","
          + entry.getAmount() + ","
          + balances[i] + "\n";
    }
    return result;
  }

  /**
   * Checks equality with another ledger.
   *
   * @param o the object to compare
   * @return true if equal, false otherwise
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Ledger)) {
      return false;
    }

    Ledger otherLedger = (Ledger) o;
    if (this.size != otherLedger.size) {
      return false;
    }
    if (this.entries.length != otherLedger.entries.length) {
      return false;
    }

    for (int i = 0; i < this.entries.length; i++) {
      if ((this.entries[i] == null && otherLedger.entries[i] != null)
          || (this.entries[i] != null
              && !this.entries[i].equals(otherLedger.entries[i]))) {
        return false;
      }
    }

    if (this.balances.length != otherLedger.balances.length) {
      return false;
    }

    for (int i = 0; i < this.balances.length; i++) {
      if (this.balances[i] != otherLedger.balances[i]) {
        return false;
      }
    }
    return true;
  }

  /** Comparator for sorting LedgerEntry objects. */
  private static Comparator<LedgerEntry> entryCompWithNull = new Comparator<LedgerEntry>() {
    @Override
    public int compare(LedgerEntry o1, LedgerEntry o2) {
      if (o1 == null && o2 == null) {
        return 0;
      }
      if (o1 == null) {
        return 1;
      }
      if (o2 == null) {
        return -1;
      }
      return o1.compareTo(o2);
    }
  };
}
