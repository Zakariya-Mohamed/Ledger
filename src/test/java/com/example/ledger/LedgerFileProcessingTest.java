package com.example.ledger;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Tests LedgerFileProcessing
 * 
 * @author Zakariya Mohamed
 */
public class LedgerFileProcessingTest {

  /**
   * Tests readLedgerFromFile method with valid file
   */
  @Test
  public void testReadLedgerFromFile() {

    Ledger ledger = LedgerFileProcessing
        .readLedgerFromFile("test-files/Input-three-entries.csv", 15);
    assertEquals(3, ledger.getNumEntries());
    assertEquals(15, ledger.getSize());
    assertEquals(-20, ledger.getBalance());
    assertEquals(
        "Date,Description,Amount,Balance\n" + "20230101,Gift,50,50\n"
            + "20330303,Dinner out,-100,-50\n"
            + "20330303,Dinner out - friend's portion,30,-20\n",
        ledger.toString());
  }

  /**
   * Tests readLedgerFromFile method with size that is too small
   */
  @Test
  public void testReadLedgerFromFileSizeTooSmall() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.readLedgerFromFile(
            "test-files/Input-three-entries.csv", 2),
        "Ledger full");
    assertEquals("Ledger is full", exception.getMessage(),
        "Testing file input when size is too small exception message");

  }

  /**
   * Tests writeLedgerToFile with valid input
   */
  @Test
  public void testWriteLedgerToFile() {
    Ledger a = new Ledger();
    a.addEntry(3, 3, 2033, "Dinner out", -100);
    a.addEntry(3, 3, 2033, "Dinner out - friend's portion", 30);
    a.addEntry(1, 1, 2023, "Gift", 50);

    String filename = "test-files/Output-three-entries.csv";
    Path path = Path.of(filename);
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      // Nothing needs to be done
      e.printStackTrace();
    }

    LedgerFileProcessing.writeLedgerToFile(filename, a);

    String message = "Testing write to file";

    String expectedContents = "Date,Description,Amount,Balance\n"
        + "20230101,Gift,50,50\n" + "20330303,Dinner out,-100,-50\n"
        + "20330303,Dinner out - friend's portion,30,-20\n";

    try {
      Scanner expected = new Scanner(expectedContents);
      Scanner actual = new Scanner(new FileInputStream(filename));
      testFileContents(expected, actual, message);
      expected.close();
      actual.close();
    } catch (FileNotFoundException e) {
      fail("File does not exist");
    }
  }

  /**
   * Testing contents of scanner
   * 
   * @param expected
   *                 expected scanner
   * @param actual
   *                 actual scanner
   * @param message
   *                 message for test
   */
  public void testFileContents(Scanner expected, Scanner actual,
      String message) {
    int line = 0;
    while (expected.hasNextLine()) {
      line++;
      if (actual.hasNextLine()) {
        assertEquals(expected.nextLine(), actual.nextLine(),
            message + ": Testing line " + line);
      } else {
        fail(message + ": Too few lines: line " + line);
      }
    }
    if (actual.hasNextLine()) {
      fail(message + ": Too many lines");
    }
  }

  /**
   * Tests for Exceptions for writeLedgerToFile
   */
  @Test
  public void testForExceptionsWriteLedgerToFile() {
    String filename = "test-files/Output-exceptions.csv";
    Path path = Path.of(filename);
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      // Nothing needs to be done
      e.printStackTrace();
    }

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.writeLedgerToFile(filename, null),
        "null ledger when writing to file");
    assertEquals("null ledger", exception.getMessage(),
        "Testing exception message for null ledger when writing to file");

    Ledger ledger = new Ledger();
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    ledger.addEntry(3, 3, 2033, "Dinner out - friend's portion", 30);
    ledger.addEntry(1, 1, 2023, "Gift", 50);

    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.writeLedgerToFile(null, ledger),
        "null filepath when writing to file");
    assertEquals("Invalid filepath", exception.getMessage(),
        "Testing exception message for null filepath when writing to file");

    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.writeLedgerToFile(" \t ", ledger),
        "whitespace filepath when writing to file");
    assertEquals("Invalid filepath", exception.getMessage(),
        "Testing exception message for whitespace filepath when writing to file");

    path = Path.of(filename);
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      // Nothing needs to be done
      e.printStackTrace();
    }

    try {
      PrintWriter out = new PrintWriter(new FileOutputStream(filename));
      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.writeLedgerToFile(filename, ledger),
        "output file already exists");
    assertEquals("Output file already exists.", exception.getMessage(),
        "Testing exception message for output file already exists");

  }

  /**
   * Tests for Exceptions for readLedgerFromFile
   */
  @Test
  public void testForExceptionsReadLedgerFromFile() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.readLedgerFromFile(null, 10),
        "invalid filepath when reading from file");
    assertEquals("Invalid filepath", exception.getMessage(),
        "Testing exception message for invalid filepath when reading from file");
    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.readLedgerFromFile("\t\t\t", 10),
        "invalid filepath when reading from file");
    assertEquals("Invalid filepath", exception.getMessage(),
        "Testing exception message for invalid filepath when reading from file");
    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.readLedgerFromFile(
            "test-files/Input-three-entries.csv", 0),
        "non-positive ledger size");
    assertEquals("Invalid size", exception.getMessage(),
        "Testing exception message for non-positive ledger size");
    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing
            .readLedgerFromFile("test-files/not-here.csv", 10),
        "file not found");
    assertEquals("Input file not found.", exception.getMessage(),
        "Testing exception message for file not found");
    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing
            .readLedgerFromFile("test-files/empty.csv", 10),
        "file is empty");
    assertEquals("Input file is empty.", exception.getMessage(),
        "Testing exception message for file that is empty");
    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing
            .readLedgerFromFile("test-files/two-columns.csv", 10),
        "too few columns in header");
    assertEquals("Invalid file", exception.getMessage(),
        "Testing exception message for too few columns in header");
    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing
            .readLedgerFromFile("test-files/non-int-date.csv", 10),
        "entry with non-int date");
    assertEquals("Invalid file", exception.getMessage(),
        "Testing exception message for entry with non-int date");
    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.readLedgerFromFile(
            "test-files/non-int-amount.csv", 10),
        "entry with non-int amount");
    assertEquals("Invalid file", exception.getMessage(),
        "Testing exception message for entry with non-int amount");
    exception = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.readLedgerFromFile(
            "test-files/missing-description.csv", 10),
        "entry missing description");
    assertEquals("Invalid file", exception.getMessage(),
        "Testing exception message for entry missing description");

  }

}
