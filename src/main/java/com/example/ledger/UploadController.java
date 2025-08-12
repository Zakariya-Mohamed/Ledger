package com.example.ledger;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UploadController {

  private final LedgerFileProcessing ledgerFileProcessing;

  @Autowired
  public UploadController(LedgerFileProcessing ledgerFileProcessing) {
    this.ledgerFileProcessing = ledgerFileProcessing;
  }

  @GetMapping("/upload")
  public String showUploadPage() {
    return "upload"; // expects upload.html in templates
  }

  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpSession session, Model model) {
    if (file.isEmpty()) {
      model.addAttribute("error", "No file selected");
      return "upload";
    }

    try {
      // Save MultipartFile to a temp file because your service expects a filepath
      var tempFile = java.io.File.createTempFile("ledger-upload-", ".csv");
      file.transferTo(tempFile);

      Ledger ledger = ledgerFileProcessing.readLedgerFromFile(tempFile.getAbsolutePath(), 1000);

      // Delete temp file ASAP
      if (!tempFile.delete()) {
        // Optional: log warning about temp file not deleted
      }

      session.setAttribute("ledger", ledger);
      model.addAttribute("message", "Upload successful");
      return "upload";
    } catch (Exception e) {
      model.addAttribute("error", "Failed to process file: " + e.getMessage());
      return "upload";
    }
  }

  @GetMapping("/download")
  public void downloadLedger(HttpSession session, HttpServletResponse response) throws IOException {
    Ledger ledger = (Ledger) session.getAttribute("ledger");
    if (ledger == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "No ledger uploaded");
      return;
    }

    response.setContentType("text/csv");
    response.setHeader("Content-Disposition", "attachment; filename=\"ledger.csv\"");

    ledgerFileProcessing.writeLedgerToOutputStream(response.getOutputStream(), ledger);
  }
}
