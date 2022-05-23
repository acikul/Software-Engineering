package hr.fer.fringilla.fringillasport.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//class View {
//    interface Summary {}
//}

@Entity
@Table(name = "t_sportevent")
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "sportevent_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("amateur")
public class SportEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private LocalDateTime startDateTime;

	@Column(nullable = false)
	private LocalDateTime endDateTime;

	private Integer maxNumberOfParticpents;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToMany(targetEntity = Person.class)
	private Set<Athlete> participents;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(targetEntity = Person.class)
	private Athlete organizer;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@OneToOne(targetEntity = Sport.class)
	private Sport sport;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@OneToOne(targetEntity = Location.class)
	private Location location;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToMany(targetEntity = PaidLocation.class)
	private Set<PaidLocation> locationReservationInquery;

	@Column(nullable = false)
	public String getSportEventType() {
		return "free";
	}

	@Column(nullable = false)
	public Boolean getEventApproved() {
		return this.getLocation() != null;
	}

	@PreRemove
	private void removeSportEventFromLocationReservationQueue() {
		for (PaidLocation paidLocation : locationReservationInquery) {
			paidLocation.getReservationQueue().remove(this);
		}
	}
}
