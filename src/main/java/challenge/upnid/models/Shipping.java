package challenge.upnid.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Shipping {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@Column
	@NotNull
	@NotBlank
	private String address;
	
	@Column
	@NotNull
	@NotBlank
	private String country;
	
	@Column
	@NotNull
	@NotBlank
	private String zip;
	
	@Column
	@NotNull
	@NotBlank
	private String number;
	
	@Column
	private String aditional;
	
	@Column
	@NotNull
	private Long price;
}
