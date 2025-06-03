package com.example.ledger;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/Ledger")
public class LedgerController {

  public List<Ledger> getLedger() {
    return List.of(
      new Ledger(

      )
    )
  }
}
