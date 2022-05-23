package hr.fer.fringilla.fringillasport.payload.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {
	private String name;

	private String locationType;

	@NotBlank
	private String gpxCoordinates;
}
