package com.example.ledger;

import java.io.*;
import java.util.*;

/**
 * Handles reading and writing ledger data to/from files.
 * 
 * @author Zakariya Mohamed
 */
public class LedgerFileProcessing {

  /**
   * integer representing the index the year ends at and the month starts at
   */
  public static final int YEAR_END_AND_MONTH_START = 4;

  /**
   * integer representing the index the month ends at and the day starts at
   */
  public static final int MONTH_END_AND_DAY_START = 6;

  /**
   * integer representing the index the day ends at
   */
  public static final int DAY_END = 8;

  /**
   * Reads ledger entries from a file and creates a Ledger object.
   *
   * @param filepath   the path to the input file
   * @param sizeLedger the maximum size of the ledger
   * @return the populated Ledger object
   * @throws IllegalArgumentException if any input is invalid
   */
  public static Ledger readLedgerFromFile(String filepath, int sizeLedger) {
    if (filepath == null || filepath.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid filepath");
    }
    if (sizeLedger <= 0) {
      throw new IllegalArgumentException("Invalid size");
    }

    FileInputStream fileInput = null;
    Scanner scanner = null;
    Ledger ledger = new Ledger(sizeLedger);

    try {
      fileInput = new FileInputStream(filepath);
      if (fileInput.available() == 0) {
        throw new IllegalArgumentException("Input file is empty.");
      }

      scanner = new Scanner(fileInput);

      if (scanner.hasNextLine()) {
        String header = scanner.nextLine();
        if (!header.startsWith("Date,Description,Amount")) {
          throw new IllegalArgumentException("Invalid file");
        }
      }

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
          continue;
        }

        Scanner lineScanner = null;
        try {
          lineScanner = new Scanner(line);
          lineScanner.useDelimiter(",");

          if (!lineScanner.hasNext()) {
            throw new IllegalArgumentException("Invalid file");
          }
          String dateStr = lineScanner.next().trim();

          if (!lineScanner.hasNext()) {
            throw new IllegalArgumentException("Invalid file");
          }
          String description = lineScanner.next().trim();

          if (!lineScanner.hasNext()) {
            throw new IllegalArgumentException("Invalid file");
          }
          String amountStr = lineScanner.next().trim();

          int year;
          int month;
          int day;
          try {
            year = Integer.parseInt(dateStr.substring(0, YEAR_END_AND_MONTH_START));
            month = Integer.parseInt(dateStr.substring(YEAR_END_AND_MONTH_START,
                MONTH_END_AND_DAY_START));
            day = Integer.parseInt(dateStr.substring(MONTH_END_AND_DAY_START, DAY_END));
          } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid file");
          }

          int amount;
          try {
            amount = Integer.parseInt(amountStr);
          } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid file");
          }

          LedgerEntry entry = new LedgerEntry(month, day, year, description, amount);
          ledger.addEntry(entry);
        } finally {
          if (lineScanner != null) {
            lineScanner.close();
          }
        }
      }
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Input file not found.");
    } catch (IOException e) {
      throw new IllegalArgumentException("Error reading file.");
    } finally {
      if (scanner != null) {
        scanner.close();
      }
      if (fileInput != null) {
        try {
          fileInput.close();
        } catch (IOException e) {
          /// fix checkstyle error
        }
      }
    }

    return ledger;
  }

  /**
   * Writes ledger entries to a file.
   *
   * @param filepath the path to the output file
   * @param ledger   the Ledger object to write
   * @throws IllegalArgumentException if any input is invalid
   */
  public static void writeLedgerToFile(String filepath, Ledger ledger) {
    if (filepath == null || filepath.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid filepath");
    }
    if (ledger == null) {
      throw new IllegalArgumentException("null ledger");
    }

    FileOutputStream fileOutputStream = null;
    OutputStreamWriter outputStreamWriter = null;
    try {
      try {
        FileInputStream testExists = new FileInputStream(filepath);
        testExists.close();
        throw new IllegalArgumentException("Output file already exists.");
      } catch (FileNotFoundException e) {
        /// checkstyle
      }

      fileOutputStream = new FileOutputStream(filepath);
      outputStreamWriter = new OutputStreamWriter(fileOutputStream);

      outputStreamWriter.write("Date,Description,Amount,Balance\n");

      int balance = 0;
      for (int i = 0; i < ledger.getNumEntries(); i++) {
        LedgerEntry entry = ledger.getEntry(i);
        balance += entry.getAmount();

        int year = entry.getDate().getYear();
        int month = entry.getDate().getMonth();
        int day = entry.getDate().getDay();

        String dateStr = String.format("%04d%02d%02d", year, month, day);

        String line = dateStr + ","
            + entry.getDescription() + ","
            + entry.getAmount() + ","
            + balance + "\n";

        outputStreamWriter.write(line);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Error writing to file");
    } finally {
      if (outputStreamWriter != null) {
        try {
          outputStreamWriter.close();
        } catch (IOException e) {
          /// checkstyle
        }
      }
      if (fileOutputStream != null) {
        try {
          fileOutputStream.close();
        } catch (IOException e) {
          /// checkstyle
        }
      }
    }
  }
}
