package com.example.ledger;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ledgerentry")
public class LedgerEntryController {

  @GetMapping
  public List<LedgerEntry> getLedgerEntries() {
    return List.of(
        new LedgerEntry(1, 2, 2022, "food", 25),
        new LedgerEntry(3, 2, 2023, "rent", -1500));
  }
}
