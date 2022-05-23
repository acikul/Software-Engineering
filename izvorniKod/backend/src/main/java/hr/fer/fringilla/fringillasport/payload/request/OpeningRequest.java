package hr.fer.fringilla.fringillasport.payload.request;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpeningRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private LocalDate fromDate;

	private LocalDate toDate;

	private DayOfWeek weekday;

	private LocalTime startTime;

	private LocalTime endTime;

	private Double cost;
}
