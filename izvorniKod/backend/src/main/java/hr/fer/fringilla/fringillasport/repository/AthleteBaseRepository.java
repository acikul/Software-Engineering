package hr.fer.fringilla.fringillasport.repository;

import org.springframework.data.repository.NoRepositoryBean;

import hr.fer.fringilla.fringillasport.domain.Athlete;

@NoRepositoryBean
public interface AthleteBaseRepository<T extends Athlete> extends PersonBaseRepository<T> {

}
