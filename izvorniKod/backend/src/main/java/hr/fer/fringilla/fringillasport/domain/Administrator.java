package hr.fer.fringilla.fringillasport.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_administrator")
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Administrator extends User {
	public Administrator(String username, String passwdHash) {
		super(username, passwdHash);
	}

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	@OneToMany(mappedBy = "approvedBy", orphanRemoval = false)
	private List<Documentation> approvedDocumentations;

	@PreRemove
	public void removeApprovalFromDocuments() {
		for (Documentation documentation : approvedDocumentations)
			documentation.setApprovedBy(null);
	}
}
