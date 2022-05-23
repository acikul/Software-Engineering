package hr.fer.fringilla.fringillasport.payload.request;

import hr.fer.fringilla.fringillasport.domain.Administrator;
import hr.fer.fringilla.fringillasport.domain.Sport;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class DocumentationRequest {

	@NotBlank
	private List<Long> sportsIds;
}
