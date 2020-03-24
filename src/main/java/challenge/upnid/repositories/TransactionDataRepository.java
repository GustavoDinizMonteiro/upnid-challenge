package challenge.upnid.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import challenge.upnid.models.TransactionData;

public interface TransactionDataRepository extends JpaRepository<TransactionData, Long> {
	Long countByCustomerId(Long id);
	Collection<TransactionData> findByCustomerId(Long id);
}
