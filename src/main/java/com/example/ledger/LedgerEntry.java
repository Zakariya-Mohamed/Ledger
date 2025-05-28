package com.example.ledger;

/**
 * Ledger Entry Class
 * 
 * @author Zakariya Mohamed
 */
public class LedgerEntry implements Comparable<LedgerEntry> {

  /**
   * object that represents the date of the entry
   */
  private LedgerDate date;

  /**
   * String that represents the description of the entry
   */
  private String description;

  /**
   * int that represents the non-zero amount of the entry
   */
  private int amount;

  /**
   * Constructor for the LedgerEntry class
   * 
   * @param month       the month
   * @param day         the day
   * @param year        the year
   * @param description the description
   * @param amount      the amount
   * @throws IllegalArgumentException if description is null or empty
   */
  public LedgerEntry(int month, int day, int year, String description, int amount) {

    this.date = new LedgerDate(month, day, year);

    if (description == null) {
      throw new IllegalArgumentException("Null description");
    }
    if (description.trim().isEmpty()) {
      throw new IllegalArgumentException("Empty or all whitespace description");
    }
    this.description = description.trim();

    if (amount == 0) {
      throw new IllegalArgumentException("Amount is zero");
    }
    this.amount = amount;
  }

  /**
   * Getter method for the date
   * 
   * @return the date
   */
  public LedgerDate getDate() {
    return date;
  }

  /**
   * Getter method for the description
   * 
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Getter method for the amount
   * 
   * @return the amount
   */
  public int getAmount() {
    return amount;
  }

  /**
   * To string returns the date in desired format
   * 
   * @return the date
   */
  public String toString() {
    return date.getDate() + "," + description + "," + amount;
  }

  /**
   * Checks if two Ledger Entry objects are equal
   * 
   * @param o the object to be compared
   * @return true or false depending on if they are equal or not
   */
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof LedgerEntry))
      return false;
    LedgerEntry other = (LedgerEntry) o;
    return this.date.equals(other.date) &&
        this.description.equals(other.description) &&
        this.amount == other.amount;
  }

  /**
   * This method is used for sorting LedgerEntry objects where earliest date
   * is first then description alphabetically then amount increasing
   * 
   * @param o
   *          The LedgerEntry object to which this LedgerEntry is being
   *          compared.
   * @return negative value if this LedgerEntry should be before the other
   *         LedgerEntry, positive value if this LedgerEntry should be after
   *         the other LedgerEntry.
   */
  @Override
  public int compareTo(LedgerEntry o) {
    if (this.equals(o)) {
      return 0;
    }
    if (!this.date.equals(o.date)) {
      return this.date.compareTo(o.date);
    }
    if (!this.description.equals(o.description)) {
      return this.description.compareTo(o.description);
    }
    return this.amount - o.amount;
  }
}
