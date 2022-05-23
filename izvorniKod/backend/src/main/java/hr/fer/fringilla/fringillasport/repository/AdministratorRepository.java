package hr.fer.fringilla.fringillasport.repository;

import javax.transaction.Transactional;

import hr.fer.fringilla.fringillasport.domain.Administrator;

@Transactional
public interface AdministratorRepository extends UserBaseRepository<Administrator> {

}
