package com.example.ledger;

/**
 * Represents a date in the format YYYYMMDD with validation.
 * Implements Comparable for sorting.
 * 
 * @author Zakariya Mohamed
 */

public class LedgerDate implements Comparable<LedgerDate> {

  /** Minimum valid year. */
  public static final int MIN_YEAR = 2020;

  /** Maximum valid year. */
  public static final int MAX_YEAR = 2050;

  /** Minimum days in a month. */
  public static final int MIN_DAYS_IN_MONTH = 1;

  /** Maximum days in a month. */
  public static final int MAX_DAYS_IN_MONTH = 31;

  /** Number of months in a year. */
  public static final int NUM_OF_MONTHS = 12;

  /** Days in February during a leap year. */
  public static final int LEAP_YEAR_DAYS = 29;

  /** Days in each month of a non-leap year. */
  private static final int[] DAYS_IN_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

  /**
   * The number four used in leap year calculations
   */
  public static final int FOUR = 4;

  /**
   * The number four hundred used in leap year calculations
   */
  public static final int FOUR_HUNDRED = 400;

  /**
   * The number 10000 used in date calculations
   */
  public static final int TEN_THOUSAND = 10000;

  /** Date represented as an integer in YYYYMMDD format. */
  private int date;

  /**
   * Constructs a LedgerDate with the given month, day, and year.
   *
   * @param month the month (1-12)
   * @param day   the day (1-31)
   * @param year  the year (2020-2050)
   * @throws IllegalArgumentException if the date is invalid
   */
  public LedgerDate(int month, int day, int year) {
    if (year < MIN_YEAR || year > MAX_YEAR || month < 1 ||
        month > NUM_OF_MONTHS || day < 1 || day > MAX_DAYS_IN_MONTH) {
      throw new IllegalArgumentException("Invalid date");
    }

    int maxDays = DAYS_IN_MONTH[month - 1];
    if ((year % FOUR == 0 && year % 100 != 0) || (year % FOUR_HUNDRED == 0)) {
      if (month == 2) {
        maxDays = LEAP_YEAR_DAYS;
      }
    }

    if (day > maxDays) {
      throw new IllegalArgumentException("Invalid date");
    }
    this.date = year * TEN_THOUSAND + month * 100 + day;
  }

  /**
   * Gets the date as an integer in YYYYMMDD format.
   *
   * @return the date
   */
  public int getDate() {
    return date;
  }

  /**
   * Gets the month from the date.
   *
   * @return the month
   */
  public int getMonth() {
    return (date / 100) % 100;
  }

  /**
   * Gets the day from the date.
   *
   * @return the day
   */
  public int getDay() {
    return date % 100;
  }

  /**
   * Gets the year from the date.
   *
   * @return the year
   */
  public int getYear() {
    return date / TEN_THOUSAND;
  }

  /**
   * Returns the date as a formatted string MM/DD/YYYY.
   *
   * @return the formatted date string
   */
  @Override
  public String toString() {
    return String.format("%02d/%02d/%04d", getMonth(), getDay(), getYear());
  }

  /**
   * Checks if two LedgerDate objects are equal.
   *
   * @param obj the object to compare
   * @return true if equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(obj instanceof LedgerDate)) {
      return false;
    }
    LedgerDate other = (LedgerDate) obj;
    return date == other.date;
  }

  /**
   * Compares this LedgerDate with another for sorting.
   *
   * @param other the LedgerDate to compare
   * @return negative if this is earlier, positive if later, 0 if equal
   */
  @Override
  public int compareTo(LedgerDate other) {
    return this.date - other.date;
  }
}
