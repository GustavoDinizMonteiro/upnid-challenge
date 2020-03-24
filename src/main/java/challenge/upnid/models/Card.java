package challenge.upnid.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false, unique = true)
	@EqualsAndHashCode.Exclude
	private Long id;
	
	@Column
	@NotNull
	@NotBlank
	private String number;
	
	@Column
	@NotNull
	@NotBlank
	private String holder;
	
	@Column
	@NotNull
	@NotBlank
	private String cvc;
	
	@OneToOne
	@JoinColumn(name = "id", referencedColumnName = "customerId")
	private Customer customer;
}
