package com.example.ledger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LedgerController {

  private final LedgerService ledgerService;

  public LedgerController(LedgerService ledgerService) {
    this.ledgerService = ledgerService;
  }

  @GetMapping("/")
  public String showLedger(Model model) {
    model.addAttribute("entries", ledgerService.getLedger());
    return "ledger";
  }

  @PostMapping("/add")
  public String addEntry(
      @RequestParam int month,
      @RequestParam int day,
      @RequestParam int year,
      @RequestParam String description,
      @RequestParam int amount) {
    ledgerService.addEntry(month, day, year, description, amount);
    return "redirect:/";
  }
}
