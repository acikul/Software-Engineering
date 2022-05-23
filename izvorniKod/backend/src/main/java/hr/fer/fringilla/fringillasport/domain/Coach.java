package hr.fer.fringilla.fringillasport.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_coach")
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class Coach extends Athlete {

	public Coach(String username, String passwdHash, String email, String name, String surname) {
		super(username, passwdHash, email, name, surname);
		// TODO Auto-generated constructor stub
	}

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	Set<Documentation> coachCertificate;

	public List<Sport> getCoachableSports() {
		Set<Sport> result = new HashSet<Sport>();
		for (Documentation documentation : coachCertificate) {
			if (documentation.getApprovedBy() != null) {
				result.addAll(documentation.getSports());
			}
		}
		return new ArrayList<Sport>(result);
	}
}
