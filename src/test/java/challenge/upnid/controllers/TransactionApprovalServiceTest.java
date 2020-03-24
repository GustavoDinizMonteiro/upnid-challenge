package challenge.upnid.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import challenge.upnid.constants.RejectCode;
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
            new Customer(null, "opa@gmail.com", "00000000000",  "99999999999", new Date(2020, 3, 20, 8, 42)), 
            new Card(null, "0000-0000-0000-0000", "Gerald od Rivia", "123", null),
            new Shipping(null, "Rua das ruas", "Brazil", "123456-789", "1", "Em frente a um prédio",
                        (long) 1000),
            new TransactionDetails(null, (long) 1000 * REAL, "177.37.155.124"),
            new Date(2020, 3, 20, 7, 40)
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
        var res = approvalService.isAlloewed(transaction);
        assertFalse(res.getAccept());
        assertEquals(RejectCode.NEW_USER_BIG_TRANSACTION, res.getRejectCode());
    }
    
    @Test
    public void newUserWithManyTransactions() {
    	setNormalValueToTransaction();
    	setCustomerId();
    	when(dataRepository.countByCustomerId(transaction.getCustomer().getId()))
    		.thenReturn((long) 10);
    	
    	var res = approvalService.isAlloewed(transaction);
    	assertFalse(res.getAccept());
    	assertEquals(RejectCode.NEW_USER_TOO_MANY_TRANSACTIONS, res.getRejectCode());
    }
    
    @Test 
    public void internationalPurchases() {
    	setNormalValueToTransaction();
    	setCustomerId();
    	when(dataRepository.findByCustomerId(transaction.getCustomer().getId()))
    		.thenReturn(getCustomerTransactionList());
    	
    	setInternationalAddress();
    	var res = approvalService.isAlloewed(transaction);
    	assertFalse(res.getAccept());
    	assertEquals(RejectCode.SUSPECT_INTERNATIONAL_SHIṔPING, res.getRejectCode());
    }
    
    @Test
    public void manyTransactionsWithExistingUser() {
      	setNormalValueToTransaction();
    	setCustomerId();
    	when(dataRepository.findByCustomerId(transaction.getCustomer().getId()))
    		.thenReturn(getCustomerTransactionSuspectList());
    	
    	var res = approvalService.isAlloewed(transaction);
    	assertFalse(res.getAccept());
    	assertEquals(RejectCode.MANY_TRANSACTIONS_IN_SHORT_TIME, res.getRejectCode());
    }
    
    @Test 
    public void manyTransactionsSameAdressDiferentCard() {
     	setNormalValueToTransaction();
    	setCustomerId();
    	when(dataRepository.findAll())
    		.thenReturn(getTransactionWithDiferentCardList());
    	
    	var res = approvalService.isAlloewed(transaction);
    	assertFalse(res.getAccept());
    	assertEquals(RejectCode.TOO_MANY_CARDS_TO_SAME_ADDRRES, res.getRejectCode());
    }
    
    private List<TransactionData> getTransactionWithDiferentCardList() {
    	Card[] cards = {
    		new Card(null, "1111-1111-1111-1111", "Gerald od Rivia", "123", null),
    		new Card(null, "2222-2222-2222-2222", "Gerald od Rivia", "123", null),
    		new Card(null, "3333-3333-3333-3333", "Gerald od Rivia", "123", null),
    		new Card(null, "4444-3333-4444-4444", "Gerald od Rivia", "123", null),
    		new Card(null, "5555-5555-5555-5555", "Gerald od Rivia", "123", null)
    	};
    	
     	var list = new ArrayList<TransactionData>();
    	for (var i = 0; i < cards.length; i++) {
    		transaction = new TransactionData(
        			null,
        			transaction.getCustomer(),
        			cards[i],
        			transaction.getShipping(),
        			transaction.getDetails(),
        			transaction.getCreatedAt()	
        	);
    		list.add(transaction);
    	}
    	return list;
    }
    
    private List<TransactionData> getCustomerTransactionSuspectList() {
    	var list = new ArrayList<TransactionData>();
    	for (var i = 0; i < 30; i++) {    		
    		list.add(transaction);
    	}
    	return list;
    }
    
    private List<TransactionData> getCustomerTransactionList() {
    	var list = new ArrayList<TransactionData>();
    	list.add(transaction);
    	return list;
    }
    
    private void setCustomerId() {
    	long custumerId = 1;
    	this.transaction.getCustomer().setId(custumerId);
    }
    
    private void setInternationalAddress() {
    	transaction = new TransactionData(
    			null,
    			transaction.getCustomer(),
    			transaction.getCard(),
    			new Shipping(null, "Esa calle", "Argentina", "123456-789", "2", "luego ahí",
    					(long) transaction.getDetails().getTotalValue() * 10),
    			transaction.getDetails(),
    			transaction.getCreatedAt()	
    	);
    }
    
    private void setNormalValueToTransaction() {
    	var normalTransactionValue = 100 * REAL;    	
    	this.transaction.getDetails().setTotalValue(normalTransactionValue);
    }
}