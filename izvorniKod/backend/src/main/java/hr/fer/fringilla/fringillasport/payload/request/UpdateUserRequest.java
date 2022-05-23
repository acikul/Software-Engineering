package hr.fer.fringilla.fringillasport.payload.request;

import java.util.Date;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

import hr.fer.fringilla.fringillasport.domain.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

	private String password;

	private String email;

	private String firstName;

	private String lastName;

	private String accountType;

	private Double height;

	private Double weight;

	private Gender gender;

	private Date birthdayDate;
}
