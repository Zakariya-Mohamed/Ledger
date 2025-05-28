package com.example.ledger;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests LedgerEntry
 * 
 * @author Zakariya Mohamed
 */
public class LedgerEntryTest {

  /**
   * Tests the constructor and getter methods of LedgerEntry
   */
  @Test
  public void testConstructorAndGetters() {
    LedgerEntry entry = new LedgerEntry(1, 2, 2040, "Birthday", 100);
    LedgerDate date = new LedgerDate(1, 2, 2040);
    assertEquals(date, entry.getDate());
    assertEquals("Birthday", entry.getDescription());
    assertEquals(100, entry.getAmount());
  }

  /**
   * Tests the constructor and getter methods with student values
   */
  @Test
  public void testConstructorAndGettersStudent() {
    LedgerEntry entry = new LedgerEntry(7, 7, 2040, "Gas", -55);
    LedgerDate date = new LedgerDate(7, 7, 2040);
    assertEquals(date, entry.getDate());
    assertEquals("Gas", entry.getDescription());
    assertEquals(-55, entry.getAmount());
  }

  /**
   * Tests the toString method
   */
  @Test
  public void testToString() {
    LedgerEntry entry = new LedgerEntry(1, 2, 2040, "Birthday", 100);
    assertEquals("20400102,Birthday,100", entry.toString());
  }

  /**
   * Tests the toString method with student values
   */
  @Test
  public void testToStringStudent() {
    LedgerEntry entry = new LedgerEntry(7, 7, 2040, "Gas", -55);
    assertEquals("20400707,Gas,-55", entry.toString());
  }

  /**
   * Tests exceptions thrown by the constructor
   */
  @Test
  public void testConstructorExceptions() {

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new LedgerEntry(1, 2, 2000, "Birthday", 100),
        "Constructor - invalid date");
    assertEquals("Invalid date", exception.getMessage(),
        "Testing LedgerEntry constructor exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> new LedgerEntry(1, 2, 2040, null, 100),
        "Constructor - invalid description");
    assertEquals("Null description", exception.getMessage(),
        "Testing LedgerEntry constructor exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> new LedgerEntry(1, 2, 2040, "", 100),
        "Constructor - invalid description");
    assertEquals("Empty or all whitespace description",
        exception.getMessage(),
        "Testing LedgerEntry constructor exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> new LedgerEntry(1, 2, 2040, " \t", 100),
        "Constructor - invalid description");
    assertEquals("Empty or all whitespace description",
        exception.getMessage(),
        "Testing LedgerEntry constructor exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> new LedgerEntry(1, 2, 2040, "Birthday", 0),
        "Constructor - invalid amount");
    assertEquals("Amount is zero", exception.getMessage(),
        "Testing LedgerEntry constructor exception message");
  }

  /**
   * Tests equals and compareTo methods
   */
  @Test
  public void testEqualsAndCompareTo() {
    LedgerEntry entry = new LedgerEntry(1, 2, 2040, "Birthday", 100);
    LedgerEntry entry2 = new LedgerEntry(1, 2, 2040, "Birthday", 100);
    assertTrue(entry.equals(entry));
    assertTrue(entry.equals(entry2));
    assertTrue(entry2.equals(entry));
    assertEquals(0, entry.compareTo(entry2));
    assertEquals(0, entry2.compareTo(entry));

    LedgerEntry entry3 = new LedgerEntry(1, 2, 2040, "Birthday", 200);
    assertFalse(entry.equals(entry3));
    assertFalse(entry3.equals(entry));
    assertTrue(entry.compareTo(entry3) < 0);
    assertTrue(entry3.compareTo(entry) > 0);

    entry3 = new LedgerEntry(1, 2, 2040, "My Birthday", 100);
    assertFalse(entry.equals(entry3));
    assertFalse(entry3.equals(entry));
    assertTrue(entry.compareTo(entry3) < 0);
    assertTrue(entry3.compareTo(entry) > 0);

    entry3 = new LedgerEntry(1, 12, 2040, "Birthday", 100);
    assertFalse(entry.equals(entry3));
    assertFalse(entry3.equals(entry));
    assertTrue(entry.compareTo(entry3) < 0);
    assertTrue(entry3.compareTo(entry) > 0);

    assertFalse(entry.equals("Hello"));
    assertFalse(entry.equals(null));
  }
}
