package hr.fer.fringilla.fringillasport.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_athlete")
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class Athlete extends Person {
	public Athlete(String username, String passwdHash, String email, String name, String surname) {
		super(username, passwdHash, email, name, surname);
		// TODO Auto-generated constructor stub
	}

	private Double height;

	private Double weight;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private Date birthdayDate;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToMany(mappedBy = "participents")
	private List<SportEvent> eventsParticipation;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SportEvent> eventsOrganizer;

	@PreRemove
	private void removePersonFromSportEvent() {
		for (SportEvent sportEvent : eventsParticipation) {
			sportEvent.getParticipents().remove(this);
		}
	}
}
