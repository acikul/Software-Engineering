package hr.fer.fringilla.fringillasport.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("professional")
public class PaidSportEvent extends SportEvent {
	private double cost;

	public String getSportEventType() {
		return "paid";
	}
}
