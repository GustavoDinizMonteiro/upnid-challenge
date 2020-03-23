package challenge.upnid.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import challenge.upnid.models.TransactionData;
import challenge.upnid.services.TransactionApprovalService;

@RestController
@RequestMapping("approval")
public class TransactionApprovalController {
	@Autowired
	private TransactionApprovalService service;

	@PostMapping
	public boolean approval(@RequestBody TransactionData transactionData) {
		return service.isAlloewed(transactionData);
	}
}
