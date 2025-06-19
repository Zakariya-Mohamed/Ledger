package com.example.ledger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for handling ledger file operations.
 * Provides endpoints for uploading, importing, and exporting ledger files.
 * 
 * @author Zakariya Mohamed
 */
@RestController
@RequestMapping("/api/ledger/files")
public class LedgerFileProcessingController {

  private static final String UPLOAD_DIR = "uploads/";
  private static final String EXPORT_DIR = "exports/";
  private static final int DEFAULT_LEDGER_SIZE = 1000;

  /**
   * Uploads a ledger file and imports its contents.
   *
   * @param file       the CSV file to upload
   * @param sizeLedger optional maximum size for the ledger (defaults to 1000)
   * @return ResponseEntity with the imported Ledger or error message
   */
  @PostMapping("/upload")
  public ResponseEntity<Map<String, Object>> uploadLedgerFile(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "sizeLedger", defaultValue = "1000") int sizeLedger) {

    Map<String, Object> response = new HashMap<>();

    try {
      // Validate file
      if (file.isEmpty()) {
        response.put("error", "Please select a file to upload");
        return ResponseEntity.badRequest().body(response);
      }

      if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
        response.put("error", "Only CSV files are supported");
        return ResponseEntity.badRequest().body(response);
      }

      // Create upload directory if it doesn't exist
      Path uploadPath = Paths.get(UPLOAD_DIR);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      // Save uploaded file
      String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
      Path filePath = uploadPath.resolve(fileName);
      Files.copy(file.getInputStream(), filePath);

      // Import ledger from file
      Ledger ledger = LedgerFileProcessing.readLedgerFromFile(filePath.toString(), sizeLedger);

      // Clean up uploaded file
      Files.deleteIfExists(filePath);

      response.put("success", true);
      response.put("message", "File uploaded and processed successfully");
      response.put("ledger", ledger);
      response.put("entriesCount", ledger.getNumEntries());

      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      response.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    } catch (IOException e) {
      response.put("error", "Error processing file: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Imports a ledger from a file path on the server.
   *
   * @param filepath   the path to the file on the server
   * @param sizeLedger optional maximum size for the ledger
   * @return ResponseEntity with the imported Ledger or error message
   */
  @PostMapping("/import")
  public ResponseEntity<Map<String, Object>> importLedgerFromFile(
      @RequestParam("filepath") String filepath,
      @RequestParam(value = "sizeLedger", defaultValue = "1000") int sizeLedger) {

    Map<String, Object> response = new HashMap<>();

    try {
      Ledger ledger = LedgerFileProcessing.readLedgerFromFile(filepath, sizeLedger);

      response.put("success", true);
      response.put("message", "Ledger imported successfully");
      response.put("ledger", ledger);
      response.put("entriesCount", ledger.getNumEntries());

      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      response.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  /**
   * Exports a ledger to a CSV file.
   *
   * @param ledger   the Ledger object to export
   * @param filename optional filename (defaults to timestamp-based name)
   * @return ResponseEntity with export status and file path
   */
  @PostMapping("/export")
  public ResponseEntity<Map<String, Object>> exportLedgerToFile(
      @RequestBody Ledger ledger,
      @RequestParam(value = "filename", required = false) String filename) {

    Map<String, Object> response = new HashMap<>();

    try {
      // Create export directory if it doesn't exist
      Path exportPath = Paths.get(EXPORT_DIR);
      if (!Files.exists(exportPath)) {
        Files.createDirectories(exportPath);
      }

      // Generate filename if not provided
      if (filename == null || filename.trim().isEmpty()) {
        filename = "ledger_export_" + System.currentTimeMillis() + ".csv";
      } else if (!filename.toLowerCase().endsWith(".csv")) {
        filename += ".csv";
      }

      String fullPath = exportPath.resolve(filename).toString();

      LedgerFileProcessing.writeLedgerToFile(fullPath, ledger);

      response.put("success", true);
      response.put("message", "Ledger exported successfully");
      response.put("filepath", fullPath);
      response.put("filename", filename);
      response.put("entriesExported", ledger.getNumEntries());

      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      response.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    } catch (IOException e) {
      response.put("error", "Error creating export directory: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Validates a ledger file format without importing it.
   *
   * @param file the CSV file to validate
   * @return ResponseEntity with validation results
   */
  @PostMapping("/validate")
  public ResponseEntity<Map<String, Object>> validateLedgerFile(
      @RequestParam("file") MultipartFile file) {

    Map<String, Object> response = new HashMap<>();

    try {
      if (file.isEmpty()) {
        response.put("error", "Please select a file to validate");
        return ResponseEntity.badRequest().body(response);
      }

      if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
        response.put("error", "Only CSV files are supported");
        return ResponseEntity.badRequest().body(response);
      }

      // Create temporary file for validation
      Path tempPath = Files.createTempFile("validate_", ".csv");
      Files.copy(file.getInputStream(), tempPath,
          java.nio.file.StandardCopyOption.REPLACE_EXISTING);

      // Try to read the file (this will validate format)
      Ledger testLedger = LedgerFileProcessing.readLedgerFromFile(
          tempPath.toString(), DEFAULT_LEDGER_SIZE);

      // Clean up temp file
      Files.deleteIfExists(tempPath);

      response.put("valid", true);
      response.put("message", "File format is valid");
      response.put("entriesFound", testLedger.getNumEntries());

      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      response.put("valid", false);
      response.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    } catch (IOException e) {
      response.put("valid", false);
      response.put("error", "Error processing file: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Gets information about the expected file format.
   *
   * @return ResponseEntity with file format information
   */
  @GetMapping("/format-info")
  public ResponseEntity<Map<String, Object>> getFileFormatInfo() {
    Map<String, Object> response = new HashMap<>();

    response.put("fileType", "CSV");
    response.put("requiredHeader", "Date,Description,Amount");
    response.put("dateFormat", "YYYYMMDD (e.g., 20231215 for December 15, 2023)");
    response.put("amountFormat", "Integer (positive for income, negative for expenses)");
    response.put("example", "20231215,Grocery Shopping,-150");
    response.put("notes", "File must start with the header row and contain at least one data row");

    return ResponseEntity.ok(response);
  }
}
