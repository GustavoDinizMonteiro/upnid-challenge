package challenge.upnid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import challenge.upnid.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Long countByCpf(String cpf);
	Long countByEmail(String email);
}
