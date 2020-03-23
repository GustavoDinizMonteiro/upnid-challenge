package challenge.upnid.services;

import org.springframework.stereotype.Service;

import challenge.upnid.models.TransactionData;

@Service
public class TransactionApprovalService {
	public boolean isAlloewed(TransactionData transactionData) {
		return false;
	}
}
