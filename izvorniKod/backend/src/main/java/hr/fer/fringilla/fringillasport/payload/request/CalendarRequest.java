package hr.fer.fringilla.fringillasport.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarRequest {
	String dateFrom;
	String dateTo;
}