package hr.fer.fringilla.fringillasport.repository;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.User;

@Transactional
public interface LocationRepository extends LocationBaseRepository<Location> {

}
