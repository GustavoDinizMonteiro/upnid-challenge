
package challenge.upnid.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import challenge.upnid.models.TransactionData;
import challenge.upnid.repositories.CustomerRepository;
import challenge.upnid.repositories.TransactionDataRepository;

@Service
public class TransactionApprovalService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TransactionDataRepository repository; 
	
	@Autowired
	private MetricsService metricsService;
	
	public boolean isAlloewed(TransactionData transactionData) {
		var customer = transactionData.getCustomer();
		var cpfExists = customerRepository.countByCpf(customer.getEmail()) > 0;
		// If a new user makes a purchase with a very high value,
		// that transaction is suspect.
		if (customer.getId() == null || !cpfExists) {
			var details = transactionData.getDetails();
			var limitValue = metricsService.getAveragePurchageValueForNewUser() * 3;
			if (details.getTotalValue() > limitValue) {
//				repository.save(transactionData);
				return false;
			}
		}
		
		// New users who make a much higher than average 
		// amount of transactions are suspect.
		var isNewUser = ChronoUnit.DAYS
				.between(
					customer.getCreatedAt().toInstant()
					.atZone(ZoneId.systemDefault()).toLocalDate(), 
					LocalDate.now()
				) < 3;
		if (isNewUser) {
			var avaragePurchaseNumber = metricsService.getAvaragePurchaseNumberForNewUser();
			var purchaseNumber = repository.countByCustomerId(customer.getId());
			if (purchaseNumber > avaragePurchaseNumber * 2) {
//				repository.save(transactionData);
				return false;
			}
		}
		
		
		// international purchases (when the address is not registered or 
		// the shipping cost is higher / much higher than the price of the product) 
		// the transaction becomes suspect.
		var adresses = repository.findByCustomerId(customer.getId()).stream()
				.map(record -> record.getShipping().getCountry())
				.distinct().collect(Collectors.toSet());
		var shipping = transactionData.getShipping();
		var details = transactionData.getDetails();
		if (!adresses.contains(shipping.getCountry())
			&& shipping.getPrice() > details.getTotalValue() * 3) {
//			repository.save(transactionData);
			return false;
		}
		
		// many transactions in a short period is suspect.
		var transactions = repository.findByCustomerId(customer.getId()).stream()
					.filter(record -> 
						ChronoUnit.MINUTES.between(
								record.getCustomer().getCreatedAt().toInstant() , LocalDate.now()
						) < 30).collect(Collectors.counting());
		if (transactions > metricsService.getAveragePurchaseNumberPerWeek()) {
//			repository.save(transactionData);
			return false;
		}
		
		// many transactions using different cards sending 
		// to same address is suspect.
		var card = transactionData.getCard();
		var sameAddressDiffCard = repository.findAll().stream()
				.filter(record -> record.getCard().equals(card) && 
						record.getShipping().equals(record.getShipping()))
				.collect(Collectors.counting());
		if (sameAddressDiffCard > 3) {
//			repository.save(transactionData);
			return false;
		}
		repository.save(transactionData);
		return true;
	}
}
