package challenge.upnid.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TransactionData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "customerId", referencedColumnName = "id")
	private Customer customer;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "cardId", referencedColumnName = "id")
	private Card card;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "shippingId", referencedColumnName = "id")
	private Shipping shipping;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "transactionDetailsId", referencedColumnName = "id")
	private TransactionDetails details;
}
