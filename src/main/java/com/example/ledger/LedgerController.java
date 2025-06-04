package com.example.ledger;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ledger")
public class LedgerController {

  @GetMapping
  public List<Ledger> getLedger() {
    // Create sample ledgers with actual data
    Ledger ledger1 = new Ledger(5);
    ledger1.addEntry(1, 15, 2024, "Salary", 3000);
    ledger1.addEntry(1, 16, 2024, "Groceries", -150);
    ledger1.addEntry(1, 20, 2024, "Utilities", -200);

    Ledger ledger2 = new Ledger(3);
    ledger2.addEntry(2, 1, 2024, "Freelance", 500);
    ledger2.addEntry(2, 5, 2024, "Gas", -60);

    return List.of(ledger1, ledger2);
  }

  @GetMapping("/credits")
  public List<Ledger> getCreditLedgers() {
    // Create a ledger and return its credits version
    Ledger mainLedger = new Ledger(10);
    mainLedger.addEntry(1, 15, 2024, "Salary", 3000);
    mainLedger.addEntry(1, 16, 2024, "Groceries", -150);
    mainLedger.addEntry(1, 18, 2024, "Bonus", 500);
    mainLedger.addEntry(1, 20, 2024, "Rent", -1200);

    Ledger creditsOnly = mainLedger.getCredits();

    return List.of(creditsOnly);
  }

  @GetMapping("/debits")
  public List<Ledger> getDebitLedgers() {
    // Create a ledger and return its debits version
    Ledger mainLedger = new Ledger(10);
    mainLedger.addEntry(1, 15, 2024, "Salary", 3000);
    mainLedger.addEntry(1, 16, 2024, "Groceries", -150);
    mainLedger.addEntry(1, 18, 2024, "Bonus", 500);
    mainLedger.addEntry(1, 20, 2024, "Rent", -1200);

    Ledger debitsOnly = mainLedger.getDebits();

    return List.of(debitsOnly);
  }
}
