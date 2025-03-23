package org.skills.loanflow.controller;

import lombok.RequiredArgsConstructor;
import org.skills.loanflow.dto.loan.request.ConsolidateRepaymentRequestDTO;
import org.skills.loanflow.dto.loan.request.LoanRequestDTO;
import org.skills.loanflow.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 16:47
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/loan")
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/{offerID}/request")
    public ResponseEntity<Object> requestLoan(@PathVariable("offerID") Long offerID, @RequestBody LoanRequestDTO loanRequestDTO) {
        var loan = loanService.requestLoan(offerID, loanRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @GetMapping("/{msisdn}")
    public ResponseEntity<Object> getLoansPerCustomer(@PathVariable("msisdn") String msisdn, @RequestParam("state") String state) {
        var loan = loanService.fetchLoans(msisdn, state);
        return ResponseEntity.ok(loan);
    }

    @PostMapping("/consolidate-repayment")
    public ResponseEntity<Object> consolidateRepayments(@RequestBody ConsolidateRepaymentRequestDTO request) {
        loanService.consolidateRepaymentDate(request.getLoanIds(), request.getNewRepaymentDay());
        return ResponseEntity.ok("Repayments consolidated successfully!");
    }

    @PutMapping("/loans/{loanId}/cancel")
    public ResponseEntity<String> cancelLoan(@PathVariable Long loanId) {
        loanService.cancelLoan(loanId);
        return ResponseEntity.ok("Loan cancelled successfully.");
    }

}
