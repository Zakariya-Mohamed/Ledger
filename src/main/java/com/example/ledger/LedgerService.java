package com.example.ledger;

import org.springframework.stereotype.Service;

@Service
public class LedgerService {
  private final Ledger ledger = new Ledger();

  public Ledger getLedger() {
    return ledger;
  }

  public void addEntry(int month, int day, int year, String description, int amount) {
    ledger.addEntry(month, day, year, description, amount);
  }
};
