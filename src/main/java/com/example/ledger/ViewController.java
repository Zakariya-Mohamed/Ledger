package com.example.ledger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

  @GetMapping("/")
  public String home() {
    return "upload"; // default to upload.html
  }

  @GetMapping("/upload")
  public String showUploadPage() {
    return "upload";
  }

  @GetMapping("/validate")
  public String showValidatePage() {
    return "validate";
  }

  @GetMapping("/import")
  public String showImportPage() {
    return "import";
  }

  @GetMapping("/export")
  public String showExportPage() {
    return "export";
  }

  @GetMapping("/")
  public String showHomePage() {
    return "index";
  }

}
