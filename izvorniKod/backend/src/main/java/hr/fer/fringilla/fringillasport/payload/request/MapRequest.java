package hr.fer.fringilla.fringillasport.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapRequest {

	private Double topRightLatitude;
	private Double topRightLongitude;

	private Double bottomLeftLatitude;
	private Double bottomLeftLongitude;

}
