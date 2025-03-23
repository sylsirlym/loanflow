package org.skills.loanflow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.skills.loanflow.configs.LoanFlowConfigs;
import org.skills.loanflow.dto.loan.request.ConsolidateRepaymentRequestDTO;
import org.skills.loanflow.dto.loan.request.LoanRequestDTO;
import org.skills.loanflow.dto.loan.response.LoanResponseDTO;
import org.skills.loanflow.entity.customer.LoanOfferEntity;
import org.skills.loanflow.entity.loan.LoanEntity;
import org.skills.loanflow.entity.product.ProductEntity;
import org.skills.loanflow.enums.BillingCycle;
import org.skills.loanflow.enums.LoanState;
import org.skills.loanflow.eventmanagement.LoanEventPublisher;
import org.skills.loanflow.exception.LoanException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 24/03/2025
 * Time: 01:41
 */


@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private StorageService storageService;

    @Mock
    private LoanFlowConfigs configs;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LoanEventPublisher loanEventPublisher;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
//        when(configs.getCreatedEventType()).thenReturn("LOAN_CREATED");
    }

    @Test
    @DisplayName("Should successfully request a loan")
    void requestLoan_Success() {
        LoanRequestDTO requestDTO = new LoanRequestDTO();
        requestDTO.setRequestedAmount(new BigDecimal("5000"));
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setLoanState(LoanState.OPEN);

        LoanOfferEntity loanOffer = mock(LoanOfferEntity.class);
        ProductEntity productEntity = mock(ProductEntity.class);
        when(productEntity.getDisbursementType()).thenReturn("TRANCHE_BASED");
        when(loanOffer.getProduct()).thenReturn(productEntity);
        when(loanOffer.getLoanLimit()).thenReturn(new BigDecimal("10000"));
        when(storageService.findLoanOfferByID(anyLong())).thenReturn(loanOffer);
        when(storageService.saveLoan(any())).thenReturn(loanEntity);
        when(modelMapper.map(any(), eq(LoanResponseDTO.class))).thenReturn(new LoanResponseDTO());

        LoanResponseDTO response = loanService.requestLoan(1L, requestDTO);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Should throw exception when requested amount exceeds loan limit")
    void requestLoan_ThrowsException_WhenAmountExceedsLimit() {
        LoanRequestDTO requestDTO = new LoanRequestDTO();
        requestDTO.setRequestedAmount(new BigDecimal("15000"));

        LoanOfferEntity loanOffer = mock(LoanOfferEntity.class);
        when(loanOffer.getLoanLimit()).thenReturn(new BigDecimal("10000"));
        when(storageService.findLoanOfferByID(anyLong())).thenReturn(loanOffer);

        assertThrows(LoanException.class, () -> loanService.requestLoan(1L, requestDTO));
    }

    @Test
    @DisplayName("Should fetch loans successfully")
    void fetchLoans_Success() {
        when(storageService.findAllLoansPerCustomer(anyString())).thenReturn(List.of(new LoanEntity()));
        when(modelMapper.map(any(), eq(LoanResponseDTO.class))).thenReturn(new LoanResponseDTO());

        List<LoanResponseDTO> loans = loanService.fetchLoans("1234567890", null);
        assertFalse(loans.isEmpty());
    }

    @Test
    @DisplayName("Should cancel a loan if it is not fully disbursed")
    void cancelLoan_Success() {
        LoanEntity loan = new LoanEntity();
        loan.setFullyDisbursed(false);
        when(storageService.findLoanById(anyLong())).thenReturn(loan);

        loanService.cancelLoan(1L);
        assertEquals(LoanState.CANCELLED, loan.getLoanState());
    }

    @Test
    @DisplayName("Should consolidate repayment dates successfully")
    void consolidateRepaymentDate_Success() {
        LoanEntity loan = new LoanEntity();
        loan.setLoanOffer(mock(LoanOfferEntity.class));
        loan.setDueDate(LocalDate.now());
        when(loan.getLoanOffer().getProduct()).thenReturn(mock(ProductEntity.class));
        when(storageService.findLoansByIds(anyList())).thenReturn(List.of(loan));
        when(loan.getLoanOffer().getProduct().getBillingCycle()).thenReturn(BillingCycle.MONTHLY);

        ConsolidateRepaymentRequestDTO request = new ConsolidateRepaymentRequestDTO();
        request.setLoanIds(List.of(1L));
        request.setNewRepaymentDay(15);

        assertDoesNotThrow(() -> loanService.consolidateRepaymentDate(request.getLoanIds(), request.getNewRepaymentDay()));
    }
}
