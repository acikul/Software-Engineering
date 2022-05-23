package hr.fer.fringilla.fringillasport.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_renter")
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Renter extends Person {
	public Renter(String username, String passwdHash, String email, String name, String surname) {
		super(username, passwdHash, email, name, surname);
		// TODO Auto-generated constructor stub
	}

}
