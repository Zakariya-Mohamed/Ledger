package com.example.ledger;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests Ledger
 * 
 * @author Zakariya Mohamed
 */
public class LedgerTest {
  /**
   * Instance of Ledger for testing.
   */
  private Ledger ledger;

  /**
   * Sets up the test environment before each test.
   */
  @BeforeEach
  public void setUp() {
    ledger = new Ledger(30);
  }

  /**
   * Tests the no-parameter constructor.
   */
  @Test
  public void testConstructorNoParameters() {
    // checks that no entries and default values
    assertEquals(0, ledger.getNumEntries());
    assertEquals(0, ledger.getBalance());
    assertEquals("Date,Description,Amount,Balance\n", ledger.toString());
  }

  /**
   * Student test for one-parameter constructor.
   */
  @Test
  public void testConstructorOneParameterStudent() {
    // Create a Ledger with a specific size and one parameter
    int customSize = 50;
    Ledger customLedger = new Ledger(customSize);
    assertEquals(0, customLedger.getNumEntries());
    assertEquals(customSize, customLedger.getCapacity());
    assertEquals(0, customLedger.getBalance());
    assertEquals("Date,Description,Amount,Balance\n", customLedger.toString());
  }

  /**
   * Tests the equals method.
   */
  @Test
  public void testEquals() {
    Ledger b = new Ledger(30);
    // Different size so c and ledger will never be equal
    Ledger c = new Ledger(10);
    assertTrue(ledger.equals(b));
    assertFalse(ledger.equals(c));
    assertTrue(b.equals(ledger));
    assertTrue(ledger.equals(ledger));
    assertFalse(ledger.equals(c));

    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    assertFalse(ledger.equals(b));
    assertFalse(ledger.equals(c));

    b.addEntry(3, 3, 2033, "Dinner out", -100);
    c.addEntry(3, 3, 2033, "Dinner out", -100);
    assertTrue(ledger.equals(b));
    assertFalse(ledger.equals(c));

    assertFalse(ledger.equals(null));

    assertFalse(ledger.equals("CSC"));
  }

  /**
   * Student test for equals method with multiple entries.
   */
  @Test
  public void testEqualsMultipleEntriesStudent() {
    Ledger ledger1 = new Ledger(30);
    Ledger ledger2 = new Ledger(30);
    Ledger ledger3 = new Ledger(30);

    ledger1.addEntry(1, 1, 2022, "Groceries", -50);
    ledger1.addEntry(2, 15, 2022, "Salary", 1000);

    ledger2.addEntry(1, 1, 2022, "Groceries", -50);
    ledger2.addEntry(2, 15, 2022, "Salary", 1000);

    ledger3.addEntry(1, 1, 2022, "Groceries", -50);
    ledger3.addEntry(2, 15, 2022, "Salary", 1000);
    ledger3.addEntry(3, 10, 2022, "Rent", -800);

    assertTrue(ledger1.equals(ledger2));
    assertFalse(ledger1.equals(ledger3));
    assertFalse(ledger2.equals(ledger3));
    assertTrue(ledger1.equals(ledger1));

  }

  /**
   * Tests adding two entries to the ledger.
   */
  @Test
  public void testAddEntryTwoEntries() {
    // add first entry
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    assertEquals(1, ledger.getNumEntries());
    assertEquals(-100, ledger.getBalance());
    assertEquals("Date,Description,Amount,Balance\n"
        + "20330303,Dinner out,-100,-100\n", ledger.toString());

    // add second entry that would be sorted after first
    ledger.addEntry(3, 3, 2033, "Dinner out - friend's portion", 30);
    assertEquals(2, ledger.getNumEntries());
    assertEquals(30, ledger.getCapacity());
    assertEquals(-70, ledger.getBalance());
    assertEquals(
        "Date,Description,Amount,Balance\n"
            + "20330303,Dinner out,-100,-100\n"
            + "20330303,Dinner out - friend's portion,30,-70\n",
        ledger.toString());
  }

  /**
   * Student test for adding three entries to the ledger.
   */
  @Test
  public void testAddEntryThreeEntriesStudent() {
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    assertEquals(1, ledger.getNumEntries());
    assertEquals(30, ledger.getCapacity());
    assertEquals(-100, ledger.getBalance());
    assertEquals("Date,Description,Amount,Balance\n"
        + "20330303,Dinner out,-100,-100\n", ledger.toString());

    ledger.addEntry(3, 3, 2033, "Dinner out - friend's portion", 30);
    assertEquals(2, ledger.getNumEntries());
    assertEquals(30, ledger.getCapacity());
    assertEquals(-70, ledger.getBalance());
    assertEquals(
        "Date,Description,Amount,Balance\n"
            + "20330303,Dinner out,-100,-100\n"
            + "20330303,Dinner out - friend's portion,30,-70\n",
        ledger.toString());

    ledger.addEntry(1, 1, 2023, "Gift", 50);
    assertEquals(3, ledger.getNumEntries());
    assertEquals(30, ledger.getCapacity());
    assertEquals(-20, ledger.getBalance());
    assertEquals(
        "Date,Description,Amount,Balance\n"
            + "20230101,Gift,50,50\n"
            + "20330303,Dinner out,-100,-50\n"
            + "20330303,Dinner out - friend's portion,30,-20\n",
        ledger.toString());
  }

  /**
   * Tests get methods with two entries in the ledger.
   */
  @Test
  public void testGetAtMethodsWithTwoEntries() {
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    ledger.addEntry(3, 1, 2033, "Dinner out - friend's portion", 30);

    assertEquals(new LedgerDate(3, 1, 2033), ledger.getDateAt(0),
        "Get date at index 0");
    assertEquals("Dinner out - friend's portion",
        ledger.getDescriptionAt(0), "Get description at index 0");
    assertEquals(30, ledger.getAmountAt(0), "Get amount at index 0");
    assertEquals(30, ledger.getBalanceAt(0), "Get balance at index 0");
  }

  /**
   * Student test for get methods with three entries in the ledger.
   */
  @Test
  public void testGetAtMethodsWithThreeEntriesStudent() {
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    ledger.addEntry(3, 1, 2033, "Dinner out - friend's portion", 30);
    ledger.addEntry(2, 28, 2033, "Paycheck", 2000);

    assertEquals(new LedgerDate(2, 28, 2033), ledger.getDateAt(0), "Get date at index 0");
    assertEquals("Paycheck", ledger.getDescriptionAt(0), "Get description at index 0");
    assertEquals(2000, ledger.getAmountAt(0), "Get amount at index 0");
    assertEquals(2000, ledger.getBalanceAt(0), "Get balance at index 0");

    assertEquals(new LedgerDate(3, 1, 2033), ledger.getDateAt(1), "Get date at index 1");
    assertEquals("Dinner out - friend's portion", ledger.getDescriptionAt(1), "Get description at index 1");
    assertEquals(30, ledger.getAmountAt(1), "Get amount at index 1");
    assertEquals(2030, ledger.getBalanceAt(1), "Get balance at index 1");

    assertEquals(new LedgerDate(3, 3, 2033), ledger.getDateAt(2), "Get date at index 2");
    assertEquals("Dinner out", ledger.getDescriptionAt(2), "Get description at index 2");
    assertEquals(-100, ledger.getAmountAt(2), "Get amount at index 2");
    assertEquals(1930, ledger.getBalanceAt(2), "Get balance at index 2");
  }

  /**
   * Tests getCredits method.
   */
  @Test
  public void testGetCredits() {
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    ledger.addEntry(3, 3, 2033, "Dinner out - friend's portion", 30);
    ledger.addEntry(1, 1, 2023, "Gift", 50);
    assertEquals(
        "Date,Description,Amount,Balance\n" + "20230101,Gift,50,50\n"
            + "20330303,Dinner out,-100,-50\n"
            + "20330303,Dinner out - friend's portion,30,-20\n",
        ledger.toString());

    Ledger credits = ledger.getCredits();
    assertEquals(2, credits.getNumEntries());
    assertEquals(30, credits.getCapacity());
    assertEquals(80, credits.getBalance());
    assertEquals(
        "Date,Description,Amount,Balance\n" + "20230101,Gift,50,50\n"
            + "20330303,Dinner out - friend's portion,30,80\n",
        credits.toString());
  }

  /**
   * Tests getDebits method.
   */
  @Test
  public void testGetDebits() {
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    ledger.addEntry(3, 3, 2033, "Dinner out - friend's portion", 30);
    ledger.addEntry(1, 1, 2023, "Gift", 50);
    assertEquals(
        "Date,Description,Amount,Balance\n" + "20230101,Gift,50,50\n"
            + "20330303,Dinner out,-100,-50\n"
            + "20330303,Dinner out - friend's portion,30,-20\n",
        ledger.toString());

    Ledger debits = ledger.getDebits();
    assertEquals(1, debits.getNumEntries());
    assertEquals(30, debits.getCapacity());
    assertEquals(-100, debits.getBalance());
    assertEquals("Date,Description,Amount,Balance\n"
        + "20330303,Dinner out,-100,-100\n", debits.toString());
  }

  /**
   * Tests getDateRange method.
   */
  @Test
  public void testGetRange() {
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    ledger.addEntry(3, 3, 2033, "Dinner out - friend's portion", 30);
    ledger.addEntry(1, 1, 2023, "Gift", 50);
    assertEquals(
        "Date,Description,Amount,Balance\n" + "20230101,Gift,50,50\n"
            + "20330303,Dinner out,-100,-50\n"
            + "20330303,Dinner out - friend's portion,30,-20\n",
        ledger.toString());

    Ledger range = ledger.getDateRange(new LedgerDate(12, 10, 2022),
        new LedgerDate(3, 4, 2033));
    assertEquals(3, range.getNumEntries());
    assertEquals(30, range.getCapacity());
    assertEquals(-20, range.getBalance());
    assertEquals(
        "Date,Description,Amount,Balance\n" + "20230101,Gift,50,50\n"
            + "20330303,Dinner out,-100,-50\n"
            + "20330303,Dinner out - friend's portion,30,-20\n",
        range.toString());

    range = ledger.getDateRange(new LedgerDate(2, 28, 2033),
        new LedgerDate(3, 3, 2033));
    assertEquals(2, range.getNumEntries());
    assertEquals(30, range.getCapacity());
    assertEquals(-70, range.getBalance());
    assertEquals(
        "Date,Description,Amount,Balance\n"
            + "20330303,Dinner out,-100,-100\n"
            + "20330303,Dinner out - friend's portion,30,-70\n",
        range.toString());
  }

  @Test
  public void testForExceptions() {

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Ledger(-116),
        "constructor with non-positive constructor");
    assertEquals("Invalid size", exception.getMessage(),
        "Testing Ledger constructor exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> new Ledger(0),
        "constructor with non-positive constructor");
    assertEquals("Invalid size", exception.getMessage(),
        "Testing Ledger constructor exception message");

    ledger = new Ledger(1);
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getDescriptionAt(-1), "Checking index");
    assertEquals("Invalid index", exception.getMessage(),
        "Testing index exception message");
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getDescriptionAt(1), "Checking index");
    assertEquals("Invalid index", exception.getMessage(),
        "Testing index exception message");
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getDateAt(-1), "Checking index");
    assertEquals("Invalid index", exception.getMessage(),
        "Testing index exception message");
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getDateAt(1), "Checking index");
    assertEquals("Invalid index", exception.getMessage(),
        "Testing index exception message");
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getAmountAt(-1), "Checking index");
    assertEquals("Invalid index", exception.getMessage(),
        "Testing index exception message");
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getAmountAt(1), "Checking index");
    assertEquals("Invalid index", exception.getMessage(),
        "Testing index exception message");
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getBalanceAt(-1), "Checking index");
    assertEquals("Invalid index", exception.getMessage(),
        "Testing index exception message");
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getBalanceAt(1), "Checking index");
    assertEquals("Invalid index", exception.getMessage(),
        "Testing index exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.addEntry(null), "Checking null entry");
    assertEquals("Null entry", exception.getMessage(),
        "Testing null entry exception message");

    ledger = new Ledger(20);
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.addEntry(1, 2, 2000, "Birthday", 100),
        "addEntry invalid date");
    assertEquals("Invalid date", exception.getMessage(),
        "Testing addEntry exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.addEntry(1, 2, 2040, null, 100),
        "addEntry invalid description");
    assertEquals("Null description", exception.getMessage(),
        "Testing addEntry exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.addEntry(1, 2, 2040, "", 100),
        "addEntry invalid description");
    assertEquals("Empty or all whitespace description",
        exception.getMessage(), "Testing addEntry exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.addEntry(1, 2, 2040, " \t", 100),
        "addEntry invalid description");
    assertEquals("Empty or all whitespace description",
        exception.getMessage(), "Testing addEntry exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.addEntry(1, 2, 2040, "Birthday", 0),
        "addEntry invalid amount");
    assertEquals("Amount is zero", exception.getMessage(),
        "Testing addEntry exception message");

    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getDateRange(null, new LedgerDate(1, 2, 2025)),
        "null date in range");
    assertEquals("Null date", exception.getMessage(),
        "Testing getDateRange exception message");
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getDateRange(new LedgerDate(1, 2, 2025), null),
        "null date in range");
    assertEquals("Null date", exception.getMessage(),
        "Testing getDateRange exception message");
    exception = assertThrows(IllegalArgumentException.class,
        () -> ledger.getDateRange(null, null), "null date in range");
    assertEquals("Null date", exception.getMessage(),
        "Testing getDateRange exception message");

  }
}
