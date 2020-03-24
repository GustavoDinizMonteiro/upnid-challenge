package challenge.upnid.models;

import challenge.upnid.constants.RejectCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VeredictResponse {
	private Boolean accept;
	private RejectCode rejectCode;
}
