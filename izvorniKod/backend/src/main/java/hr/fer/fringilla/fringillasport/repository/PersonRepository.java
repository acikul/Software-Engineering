package hr.fer.fringilla.fringillasport.repository;

import javax.transaction.Transactional;

import hr.fer.fringilla.fringillasport.domain.Person;

@Transactional
public interface PersonRepository extends PersonBaseRepository<Person> {
}
