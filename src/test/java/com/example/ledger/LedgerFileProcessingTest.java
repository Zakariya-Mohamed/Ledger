package com.example.ledger;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Tests LedgerFileProcessing
 *
 * @author Jessica Young Schmidt
 */
public class LedgerFileProcessingTest {

  /** Resolve a test CSV that lives in src/test/resources/test-files/ */
  private String res(String filename) {
    URL url = getClass().getClassLoader()
        .getResource("test-files/" + filename);
    assertNotNull(url, "Test resource not found: " + filename);
    return Path.of(url.getPath()).toString();
  }

  /** Return a path in target/test-output (created if necessary) */
  private Path tempOut(String filename) {
    try {
      Path dir = Path.of("target", "test-output");
      Files.createDirectories(dir);
      return dir.resolve(filename);
    } catch (IOException e) {
      throw new RuntimeException(e); // fail fast if we cannot create it
    }
  }

  /** Tests readLedgerFromFile with valid file */
  @Test
  public void testReadLedgerFromFile() {
    Ledger ledger = LedgerFileProcessing.readLedgerFromFile(
        res("Input-three-entries.csv"), 15);

    assertEquals(3, ledger.getNumEntries());
    assertEquals(15, ledger.getSize());
    assertEquals(-20, ledger.getBalance());
    assertEquals(
        "Date,Description,Amount,Balance\n" +
            "20230101,Gift,50,50\n" +
            "20330303,Dinner out,-100,-50\n" +
            "20330303,Dinner out - friend's portion,30,-20\n",
        ledger.toString());
  }

  /** Tests readLedgerFromFile when size is too small */
  @Test
  public void testReadLedgerFromFileSizeTooSmall() {
    Exception ex = assertThrows(IllegalArgumentException.class,
        () -> LedgerFileProcessing.readLedgerFromFile(
            res("Input-three-entries.csv"), 2));
    assertEquals("Ledger is full", ex.getMessage());
  }

  /** Tests writeLedgerToFile with valid input */
  @Test
  public void testWriteLedgerToFile() throws IOException {
    Ledger ledger = new Ledger();
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    ledger.addEntry(3, 3, 2033, "Dinner out - friend's portion", 30);
    ledger.addEntry(1, 1, 2023, "Gift", 50);

    Path out = tempOut("Output-three-entries.csv");
    Files.deleteIfExists(out);

    LedgerFileProcessing.writeLedgerToFile(out.toString(), ledger);

    String expected = "Date,Description,Amount,Balance\n" +
        "20230101,Gift,50,50\n" +
        "20330303,Dinner out,-100,-50\n" +
        "20330303,Dinner out - friend's portion,30,-20\n";

    try (Scanner exp = new Scanner(expected);
        Scanner act = new Scanner(new FileInputStream(out.toFile()))) {
      compareScanners(exp, act, "writeLedgerToFile");
    }
  }

  /** Compare two scanners line-by-line */
  private void compareScanners(Scanner expected, Scanner actual,
      String context) {
    int line = 0;
    while (expected.hasNextLine()) {
      line++;
      if (actual.hasNextLine()) {
        assertEquals(expected.nextLine(), actual.nextLine(),
            context + " – line " + line);
      } else {
        fail(context + " – too few lines (at " + line + ')');
      }
    }
    if (actual.hasNextLine()) {
      fail(context + " – too many lines");
    }
  }

  /** Tests for exception cases in writeLedgerToFile */
  @Test
  public void testExceptionsWriteLedgerToFile() throws IOException {
    Path out = tempOut("Output-exceptions.csv");
    Files.deleteIfExists(out);

    Ledger ledger = new Ledger();
    ledger.addEntry(3, 3, 2033, "Dinner out", -100);
    ledger.addEntry(3, 3, 2033, "Dinner out - friend's portion", 30);
    ledger.addEntry(1, 1, 2023, "Gift", 50);

    assertEquals("null ledger",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.writeLedgerToFile(out.toString(), null))
            .getMessage());

    assertEquals("Invalid filepath",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.writeLedgerToFile(null, ledger))
            .getMessage());

    assertEquals("Invalid filepath",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.writeLedgerToFile(" \t ", ledger))
            .getMessage());

    // create an empty file so "already exists" path is triggered
    Files.createFile(out);

    assertEquals("Output file already exists.",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.writeLedgerToFile(out.toString(), ledger))
            .getMessage());
  }

  /** Tests for exception cases in readLedgerFromFile */
  @Test
  public void testExceptionsReadLedgerFromFile() {
    assertEquals("Invalid filepath",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.readLedgerFromFile(null, 10))
            .getMessage());

    assertEquals("Invalid filepath",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.readLedgerFromFile("\t\t\t", 10))
            .getMessage());

    assertEquals("Invalid size",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.readLedgerFromFile(
                res("Input-three-entries.csv"), 0))
            .getMessage());

    /* path that really does NOT exist */
    assertEquals("Input file not found.",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.readLedgerFromFile(
                tempOut("not-here.csv").toString(), 10))
            .getMessage());

    assertEquals("Input file is empty.",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.readLedgerFromFile(
                res("empty.csv"), 10))
            .getMessage());

    assertEquals("Invalid file",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.readLedgerFromFile(
                res("two-columns.csv"), 10))
            .getMessage());

    assertEquals("Invalid file",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.readLedgerFromFile(
                res("non-int-date.csv"), 10))
            .getMessage());

    assertEquals("Invalid file",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.readLedgerFromFile(
                res("non-int-amount.csv"), 10))
            .getMessage());

    assertEquals("Invalid file",
        assertThrows(IllegalArgumentException.class,
            () -> LedgerFileProcessing.readLedgerFromFile(
                res("missing-description.csv"), 10))
            .getMessage());
  }
}
