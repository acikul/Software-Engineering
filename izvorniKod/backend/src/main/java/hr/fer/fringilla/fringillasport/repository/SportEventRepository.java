package hr.fer.fringilla.fringillasport.repository;

import org.springframework.transaction.annotation.Transactional;

import hr.fer.fringilla.fringillasport.domain.SportEvent;

@Transactional
public interface SportEventRepository extends SportEventBaseRepository<SportEvent> {

}
