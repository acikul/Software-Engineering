package hr.fer.fringilla.fringillasport.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Setter;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("paid")
public class PaidLocation extends Location {
	private String name;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@OneToMany(targetEntity = Documentation.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Documentation> ownershipCertificate;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@OneToMany(targetEntity = Opening.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Opening> openings;

	@ManyToMany(targetEntity = SportEvent.class, mappedBy = "locationReservationInquery")
	private Set<SportEvent> reservationQueue;

	public String getLocationStringType() {
		return "paid";
	}

	public List<Sport> getAvaliableSports() {
		List<Sport> result = new ArrayList();
		for (Documentation documentation : ownershipCertificate) {
			if (documentation.getApprovedBy() != null) {
				result.addAll(documentation.getSports());
			}
		}
		return result;
	}

	@PreRemove
	private void removeLocationFromSportEventReservationInquery() {
		for (SportEvent sportEvent : reservationQueue) {
			sportEvent.getLocationReservationInquery().remove(this);
		}
	}
}
