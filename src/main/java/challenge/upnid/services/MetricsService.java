package challenge.upnid.services;

import org.springframework.stereotype.Service;

@Service
public class MetricsService {
	private static final long REAL = 100;

	public Long getAveragePurchageValueForNewUser() {
		return (long) (200 * REAL);
	}
	
	public Integer getAvaragePurchaseNumberForNewUser() {
		return 3;
	}
	
	public Integer getAveragePurchaseNumberPerWeek() {
		return 1;
	}
}
