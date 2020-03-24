package challenge.upnid.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import challenge.upnid.models.Card;
import challenge.upnid.models.Customer;
import challenge.upnid.models.Shipping;
import challenge.upnid.models.TransactionData;
import challenge.upnid.models.TransactionDetails;
import challenge.upnid.repositories.CustomerRepository;
import challenge.upnid.repositories.TransactionDataRepository;
import challenge.upnid.services.MetricsService;
import challenge.upnid.services.TransactionApprovalService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TransactionApprovalServiceTest {
    private static final long REAL = 100;

	private TransactionData transaction;

    @MockBean
    private TransactionDataRepository dataRepository;

    @MockBean
    private CustomerRepository customerRepository;

    private TransactionApprovalService approvalService;

    @SuppressWarnings("deprecation")
	@BeforeEach
    void setUp() {
        this.approvalService = new TransactionApprovalService(customerRepository, dataRepository, new MetricsService());
        this.transaction = new TransactionData(null, 
            new Customer(null, "opa@gmail.com", "00000000000",  "99999999999", new Date(2020, 3, 20)), 
            new Card(null, "0000-0000-0000-0000", "Gerald od Rivia", "123", null),
            new Shipping(null, "Rua das ruas", "Brazil", "123456-789", "1", "Em frente a um prédio",
                        (long) 1000),
            new TransactionDetails(null, (long) 1000 * REAL, "177.37.155.124"),
            new Date(2020, 3, 20)
        );
    }
    
    @AfterEach
    void destroy() {
    	dataRepository.deleteAll();
    	customerRepository.deleteAll();
    }
    
    @Test
    public void newUserWithBigTransactionTest() {
        when(customerRepository.countByCpf("00000000000"))
            .thenReturn((long) 0);
        assertFalse(approvalService.isAlloewed(transaction));
    }
    
    @Test
    public void newUserWithManyTransactions() {
    	long custumerId = 1;
    	var normalTransactionValue = 100 * REAL;
    	when(dataRepository.countByCustomerId(custumerId))
    		.thenReturn((long) 10);
    	this.transaction.getCustomer().setId(custumerId);
    	this.transaction.getDetails().setTotalValue(normalTransactionValue);
    	
    	assertFalse(approvalService.isAlloewed(transaction));
    }
}