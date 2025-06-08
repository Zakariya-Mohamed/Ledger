package com.example.ledger;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a ledger that tracks financial entries with dates, descriptions,
 * and amounts.
 * 
 * @author Zakariya Mohamed
 */
public class Ledger {

  /** Array storing the ledger entries. */
  private final List<LedgerEntry> entries;

  /** Array storing the balances. */
  private final List<Integer> balances;

  /** Current balance of the ledger. */
  private int balance;

  /** int representaion of the capacity of the ledger. */
  private final int capacity;

  /**
   * Creates a ledger with default maximum size.
   */
  public Ledger() {
    this.capacity = 100;
    this.balance = 0;
    this.entries = new ArrayList<>();
    this.balances = new ArrayList<>();
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
    this.capacity = size;
    this.balance = 0;
    this.entries = new ArrayList<>();
    this.balances = new ArrayList<>();
  }

  /**
   * Gets the capacity of the Ledger
   * 
   * @return capacity
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * Gets the current number of entries.
   *
   * @return number of entries
   */
  public int getNumEntries() {
    return entries.size();
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
    if (i < 0 || i >= entries.size()) {
      throw new IllegalArgumentException("Invalid index");
    }
    return entries.get(i).getDescription();
  }

  /**
   * Gets date at specified index.
   *
   * @param i the index
   * @return date at index
   * @throws IllegalArgumentException if index is invalid
   */
  public LedgerDate getDateAt(int i) {
    if (i < 0 || i >= entries.size()) {
      throw new IllegalArgumentException("Invalid index");
    }
    return entries.get(i).getDate();
  }

  /**
   * Gets amount at specified index.
   *
   * @param i the index
   * @return amount at index
   * @throws IllegalArgumentException if index is invalid
   */
  public int getAmountAt(int i) {
    if (i < 0 || i >= entries.size()) {
      throw new IllegalArgumentException("Invalid index");
    }
    return entries.get(i).getAmount();
  }

  /**
   * Gets balance at specified index.
   *
   * @param i the index
   * @return balance at index
   * @throws IllegalArgumentException if index is invalid
   */
  public int getBalanceAt(int i) {
    if (i < 0 || i >= entries.size()) {
      throw new IllegalArgumentException("Invalid index");
    }
    return balances.get(i);
  }

  /**
   * Sorts entries and updates balances.
   */
  private void sort() {
    entries.sort(entryCompWithNull);
    balances.clear();

    int runningBalance = 0;
    for (int i = 0; i < entries.size(); i++) {
      runningBalance += entries.get(i).getAmount();
      balances.add(runningBalance);
    }
  }

  /**
   * Adds an entry to the ledger.
   *
   * @param entry the entry to add
   * @throws IllegalArgumentException if entry is null
   */
  public void addEntry(LedgerEntry entry) {
    if (entry == null) {
      throw new IllegalArgumentException("Null entry");
    }

    entries.add(entry);
    sort();
    balance += entry.getAmount();
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

    LedgerEntry entry = new LedgerEntry(month, day, year, description, amount);
    entries.add(entry);
    balance += amount;
    sort();
  }

  /**
   * Gets a ledger containing only credit entries.
   *
   * @return ledger with credit entries
   */
  @JsonIgnore
  public Ledger getCredits() {
    Ledger creditsLedger = new Ledger();
    for (int i = 0; i < entries.size(); i++) {
      if (entries.get(i).getAmount() > 0) {
        creditsLedger.addEntry(entries.get(i));
      }
    }
    return creditsLedger;
  }

  @JsonIgnore
  public Ledger getDebits() {
    Ledger debitsLedger = new Ledger();
    for (LedgerEntry entry : entries) {
      if (entry.getAmount() < 0) {
        debitsLedger.addEntry(entry);
      }
    }
    return debitsLedger;
  }

  @JsonIgnore
  public Ledger getDateRange(LedgerDate start, LedgerDate end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Null date");
    }

    Ledger dateRangeLedger = new Ledger();
    for (LedgerEntry entry : entries) {
      LedgerDate entryDate = entry.getDate();
      if (entryDate.compareTo(start) >= 0 && entryDate.compareTo(end) <= 0) {
        dateRangeLedger.addEntry(entry);
      }
    }
    return dateRangeLedger;
  }

  public LedgerEntry getEntry(int index) {
    if (index < 0 || index >= entries.size()) {
      throw new IllegalArgumentException("Invalid index");
    }
    return entries.get(index);
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("Date,Description,Amount,Balance\n");
    for (int i = 0; i < entries.size(); i++) {
      LedgerEntry entry = entries.get(i);
      LedgerDate date = entry.getDate();

      String formattedDate = String.format("%04d%02d%02d", date.getYear(), date.getMonth(), date.getDay());
      result.append(formattedDate).append(",")
          .append(entry.getDescription()).append(",")
          .append(entry.getAmount()).append(",")
          .append(balances.get(i)).append("\n");
    }
    return result.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Ledger))
      return false;

    Ledger otherLedger = (Ledger) o;

    if (this.balance != otherLedger.balance)
      return false;
    if (!this.entries.equals(otherLedger.entries))
      return false;
    return this.balances.equals(otherLedger.balances);
  }

  private static final Comparator<LedgerEntry> entryCompWithNull = new Comparator<LedgerEntry>() {
    @Override
    public int compare(LedgerEntry o1, LedgerEntry o2) {
      if (o1 == null && o2 == null)
        return 0;
      if (o1 == null)
        return 1;
      if (o2 == null)
        return -1;
      return o1.compareTo(o2);
    }
  };
}
