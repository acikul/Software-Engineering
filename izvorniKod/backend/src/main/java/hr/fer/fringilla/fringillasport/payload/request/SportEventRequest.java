package hr.fer.fringilla.fringillasport.payload.request;

import java.time.LocalDateTime;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SportEventRequest {
	private Long sportId;

	private String sportEventType;

	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;

	private Integer maxNumberOfParticpents;

	private double cost;
}
