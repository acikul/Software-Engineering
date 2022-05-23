package hr.fer.fringilla.fringillasport;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.tokens.FlowEntryToken;


import hr.fer.fringilla.fringillasport.domain.Administrator;
import hr.fer.fringilla.fringillasport.domain.Athlete;
import hr.fer.fringilla.fringillasport.domain.Coach;
import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.Documentation.DocumentationType;
import hr.fer.fringilla.fringillasport.domain.Gender;
import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.Opening;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.PaidSportEvent;
import hr.fer.fringilla.fringillasport.domain.Renter;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.domain.SportEvent;
import hr.fer.fringilla.fringillasport.domain.Sport.SportType;
import hr.fer.fringilla.fringillasport.repository.AdministratorRepository;
import hr.fer.fringilla.fringillasport.repository.AthleteRepository;
import hr.fer.fringilla.fringillasport.repository.CoachRepository;
import hr.fer.fringilla.fringillasport.repository.DocumentationRepository;
import hr.fer.fringilla.fringillasport.repository.LocationRepository;
import hr.fer.fringilla.fringillasport.repository.OpeningRepository;
import hr.fer.fringilla.fringillasport.repository.PaidLocationRepository;
import hr.fer.fringilla.fringillasport.repository.PaidSportEventRepository;
import hr.fer.fringilla.fringillasport.repository.RenterRepository;
import hr.fer.fringilla.fringillasport.repository.SportEventRepository;
import hr.fer.fringilla.fringillasport.repository.SportRepository;

@Component
public class DataLoader implements ApplicationRunner{
	@Autowired
	PasswordEncoder encoder;
	
	// USERS
	
	@Autowired
	AdministratorRepository administratorRepository;
	
	@Autowired
	AthleteRepository athleteRepository;
	
	@Autowired
	CoachRepository coachRepository;
	
	@Autowired
	RenterRepository renterRepository;
	
	// DOCUMENTATION
	
	@Autowired
	DocumentationRepository documentationRepository;
	
	// SPORT EVENTS
	
	@Autowired
	SportEventRepository sportEventRepository;
	
	@Autowired
	PaidSportEventRepository paidSportEventRepository;
	
	// LOCATIONS
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	PaidLocationRepository paidLocationRepository;
	
	@Autowired
	OpeningRepository openingRepository;
	
	// SPORTS
	@Autowired
	SportRepository sportRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
	
		Sport sportSwimming = new Sport();
		sportSwimming.setName("swimming");
		sportSwimming.setType(Sport.SportType.INDOOR);
		sportSwimming.setIconBWUri("img/sports/swimming-bw.png");
		sportSwimming.setIconColorUri("img/sports/swimming.png");
		sportRepository.save(sportSwimming);
		
		Sport sportBasketball = new Sport();
		sportBasketball.setName("basketball");
		sportBasketball.setType(Sport.SportType.OUTDOOR);
		sportBasketball.setIconBWUri("img/sports/basketball-bw.png");
		sportBasketball.setIconColorUri("img/sports/basketball.png");
		sportRepository.save(sportBasketball);
		
		Sport sportFootball = new Sport();
		sportFootball.setName("football");
		sportFootball.setType(Sport.SportType.OUTDOOR);
		sportFootball.setIconBWUri("img/sports/football-bw.png");
		sportFootball.setIconColorUri("img/sports/football.png");
		sportRepository.save(sportFootball);
		
		Sport sportAthletics = new Sport();
		sportAthletics.setName("athletics");
		sportAthletics.setType(Sport.SportType.INDOOR);
		sportAthletics.setIconBWUri("img/sports/athletics-bw.png");	
		sportAthletics.setIconColorUri("img/sports/athletics.png");  
		sportRepository.save(sportAthletics);

		
		Sport sportShooting = new Sport();
		sportShooting.setName("shooting");
		sportShooting.setType(Sport.SportType.INDOOR);
		sportShooting.setIconBWUri("img/sports/shooting-bw.png");  
		sportShooting.setIconColorUri("img/sports/shooting.png");  
		sportRepository.save(sportShooting);

		Sport sportCycling = new Sport();
		sportCycling.setName("cycling");
		sportCycling.setType(Sport.SportType.OUTDOOR);
		sportCycling.setIconBWUri("img/sports/cycling-bw.png");  
		sportCycling.setIconColorUri("img/sports/cycling.png");  
		sportRepository.save(sportCycling);
		
		Sport sportGymnastics = new Sport();
		sportGymnastics.setName("gymnastics");
		sportGymnastics.setType(Sport.SportType.INDOOR);
		sportGymnastics.setIconBWUri("img/sports/gymnastics-bw.png");	
		sportGymnastics.setIconColorUri("img/sports/gymnastics.png");	
		sportRepository.save(sportGymnastics);

		Sport sportFencing = new Sport();
		sportFencing.setName("fencing");
		sportFencing.setType(Sport.SportType.INDOOR);
		sportFencing.setIconBWUri("img/sports/fencing-bw.png");  
		sportFencing.setIconColorUri("img/sports/fencing.png");  
		sportRepository.save(sportFencing);
		
		Sport sportRowing = new Sport();
		sportRowing.setName("rowing");
		sportRowing.setType(Sport.SportType.OUTDOOR);
		sportRowing.setIconBWUri("img/sports/rowing-bw.png");	
		sportRowing.setIconColorUri("img/sports/rowing.png");	
		sportRepository.save(sportRowing);
		
		Sport sportCrossCountrySkiing = new Sport();
		sportCrossCountrySkiing.setName("cross country");
		sportCrossCountrySkiing.setType(Sport.SportType.OUTDOOR);
		sportCrossCountrySkiing.setIconBWUri("img/sports/cross-country-bw.png");
		sportCrossCountrySkiing.setIconColorUri("img/sports/cross-country.png");	
		sportRepository.save(sportCrossCountrySkiing);

		Sport sportAlpineSkiing = new Sport();
		sportAlpineSkiing.setName("alpine skiing");
		sportAlpineSkiing.setType(Sport.SportType.OUTDOOR);
		sportAlpineSkiing.setIconBWUri("img/sports/alpine-skiing-bw.png");
		sportAlpineSkiing.setIconColorUri("img/sports/alpine-skiing.png");
		sportRepository.save(sportAlpineSkiing);

		Sport sportWrestling = new Sport();
		sportWrestling.setName("wrestling");
		sportWrestling.setType(Sport.SportType.INDOOR);
		sportWrestling.setIconBWUri("img/sports/wrestling-bw.png");
		sportWrestling.setIconColorUri("img/sports/wrestling.png");
		sportRepository.save(sportWrestling);
		
		Sport sportHandball = new Sport();
		sportHandball.setName("handball");
		sportHandball.setType(Sport.SportType.INDOOR);
		sportHandball.setIconBWUri("img/sports/handball-bw.png");
		sportHandball.setIconColorUri("img/sports/handball.png");
		sportRepository.save(sportHandball);
		
		Sport sportBowling = new Sport();
		sportBowling.setName("bowling");
		sportBowling.setType(Sport.SportType.INDOOR);
		sportBowling.setIconBWUri("img/sports/bowling-bw.png");
		sportBowling.setIconColorUri("img/sports/bowling.png");
		sportRepository.save(sportBowling);
		
		Sport sportBadminton = new Sport();
		sportBadminton.setName("badminton");
		sportBadminton.setType(Sport.SportType.INDOOR);
		sportBadminton.setIconBWUri("img/sports/badminton-bw.png");
		sportBadminton.setIconColorUri("img/sports/badminton.png");
		sportRepository.save(sportBadminton);

		Sport sportTennis = new Sport();
		sportTennis.setName("tennis");
		sportTennis.setType(Sport.SportType.OUTDOOR);
		sportTennis.setIconBWUri("img/sports/tennis-bw.png");
		sportTennis.setIconColorUri("img/sports/tennis.png");
		sportRepository.save(sportTennis);

		Sport sportHockey = new Sport();
		sportHockey.setName("hockey");
		sportHockey.setType(Sport.SportType.INDOOR);
		sportHockey.setIconBWUri("img/sports/hockey-bw.png");
		sportHockey.setIconColorUri("img/sports/hockey.png");
		sportRepository.save(sportHockey);
		
		
		
		
		
		//Administrator administrator = new Administrator();
		Administrator admin = new Administrator();
		admin.setUsername("admin");
		admin.setPasswdHash(encoder.encode("admin"));
		administratorRepository.save(admin);
		
		Athlete athlete = new Athlete();
		athlete.setUsername("athlete");
		athlete.setPasswdHash(encoder.encode("athlete"));
		athlete.setName("athlete-name");
		athlete.setSurname("athlete-surname");
		athlete.setEmail("athlete@athlete");
		athlete.setBirthdayDate(new Date(1));
		athlete.setGender(Gender.MALE);
		athlete.setHeight(130.0);
		athlete.setWeight(20.0);
		athleteRepository.save(athlete);
		
		Athlete athlete1 = new Athlete();
		athlete1.setUsername("athlete1");
		athlete1.setPasswdHash(encoder.encode("athlete1"));
		athlete1.setName("athlete1-name");
		athlete1.setSurname("athlete1-surname");
		athlete1.setEmail("athlete1@athlete1");
		athlete1.setBirthdayDate(new Date(1));
		athlete1.setGender(Gender.MALE);
		athlete1.setHeight(180.0);
		athlete1.setWeight(180.0);
		athleteRepository.save(athlete1);
		
		Athlete athlete2 = new Athlete();
		athlete2.setUsername("athlete2");
		athlete2.setPasswdHash(encoder.encode("athlete2"));
		athlete2.setName("athlete2-name");
		athlete2.setSurname("athlete2-surname");
		athlete2.setEmail("athlete2@athlete2");
		athlete2.setBirthdayDate(new Date(1));
		athlete2.setGender(Gender.MALE);
		athlete2.setHeight(180.0);
		athlete2.setWeight(80.0);
		athleteRepository.save(athlete2);
		
		Athlete athlete3 = new Athlete();
		athlete3.setUsername("athlete3");
		athlete3.setPasswdHash(encoder.encode("athlete3"));
		athlete3.setName("athlete3-name");
		athlete3.setSurname("athlete3-surname");
		athlete3.setEmail("athlete3@athlete3");
		athlete3.setBirthdayDate(new Date(1));
		athlete3.setGender(Gender.MALE);
		athlete3.setHeight(160.0);
		athlete3.setWeight(60.0);
		athleteRepository.save(athlete3);
		
		
		
		
		
		Documentation coachCertificate1 = new Documentation();
		coachCertificate1.setDocumentInternUri("documentation/gZ7XRwX2PQybJqQb46QBE17teuNdsRAYLvkktwvuV4YdU0w8cFTtXYWooa3Ho8xd.pdf");
		coachCertificate1.setSports(Arrays.asList(sportSwimming));
		coachCertificate1.setType(DocumentationType.CERTIFICATE);
		coachCertificate1.setApprovedBy(admin);
		//documentationRepository.save(coachCertificate1);
		
		Documentation coachCertificate2 = new Documentation();
		coachCertificate2.setDocumentInternUri("documentation/njJYaBCgxzYogY0dAusFBuTqvFP4tB76spq4cLLELgHVSAa6NFxtx6ZkaeIsONZ7.pdf");
		coachCertificate2.setSports(Arrays.asList(sportSwimming,sportFootball));
		coachCertificate2.setType(DocumentationType.CERTIFICATE);
		coachCertificate2.setApprovedBy(admin);
		
		Documentation coachCertificate3 = new Documentation();
		coachCertificate3.setDocumentInternUri("documentation/njJYaBCgxzYogY0dAusFBuTqvFP4tB76spq4cLLELgHVSAa6NFxtx6ZkaeIsONZ7.pdf");
		coachCertificate3.setSports(Arrays.asList(sportSwimming,sportFootball));
		coachCertificate3.setType(DocumentationType.CERTIFICATE);
		//documentationRepository.save(coachCertificate2);
		
		
		
		
		
		Coach coach = new Coach();
		coach.setUsername("coach");
		coach.setPasswdHash(encoder.encode("coach"));
		coach.setName("coach-name");
		coach.setSurname("coach-surname");
		coach.setEmail("coach@coach");
		coach.setCoachCertificate(Set.of(coachCertificate1, coachCertificate2, coachCertificate3));
		coachRepository.save(coach);
		
		Renter renter = new Renter();
		renter.setUsername("renter");
		renter.setPasswdHash(encoder.encode("renter"));
		renter.setName("renter-name");
		renter.setSurname("renter-surname");
		renter.setEmail("renter@renter");
		renterRepository.save(renter);
		
		
		
		
		Documentation ownershipDocumentation1 = new Documentation();
		ownershipDocumentation1.setDocumentInternUri("documentation/O70nnNgdYKM9U35y9DbOTXJqQOrBfXA5E7Z9JN9Pf8ODCN6ysIc4pRcdVEnU15ZC.pdf");
		ownershipDocumentation1.setSports(Arrays.asList(sportFootball, sportBasketball));
		ownershipDocumentation1.setApprovedBy(admin);
		ownershipDocumentation1.setType(DocumentationType.OWNERSHIP);
		//documentationRepository.save(ownershipDocumentation1);
		
		Documentation ownershipDocumentation2 = new Documentation();
		ownershipDocumentation2.setDocumentInternUri("documentation/ck1gBDJG8XyrEBMPzKrFQcgMLMEZT28uNNkXKThpm6pHFQx6HoOGj11OhpKrqD9M.pdf");
		ownershipDocumentation2.setSports(Arrays.asList(sportSwimming));
		ownershipDocumentation2.setApprovedBy(admin);
		ownershipDocumentation2.setType(DocumentationType.OWNERSHIP);
		
		Documentation ownershipDocumentation3 = new Documentation();
		ownershipDocumentation3.setDocumentInternUri("documentation/ck1gBDJG8XyrEBMPzKrFQcgMLMEZT28uNNkXKThpm6pHFQx6HoOGj11OhpKrqD9M.pdf");
		ownershipDocumentation3.setSports(Arrays.asList(sportSwimming));
		ownershipDocumentation3.setApprovedBy(admin);
		ownershipDocumentation3.setType(DocumentationType.OWNERSHIP);
		
		Documentation ownershipDocumentation4 = new Documentation();
		ownershipDocumentation4.setDocumentInternUri("documentation/ck1gBDJG8XyrEBMPzKrFQcgMLMEZT28uNNkXKThpm6pHFQx6HoOGj11OhpKrqD9M.pdf");
		ownershipDocumentation4.setSports(Arrays.asList(sportWrestling));
		ownershipDocumentation4.setType(DocumentationType.OWNERSHIP);
		//documentationRepository.save(ownershipDocumentation2);
		
		
		Opening opening1 = new Opening();
		opening1.setCost(10.0);
		opening1.setFromDate(LocalDate.parse("2020-12-27"));
		opening1.setToDate(LocalDate.parse("2021-03-30"));
		opening1.setStartTime(LocalTime.parse("08:00:00"));
		opening1.setEndTime(LocalTime.parse("13:00:00"));
		opening1.setWeekday(DayOfWeek.MONDAY);
		//openingRepository.save(opening1);
		
		Opening opening2 = new Opening();
		opening2.setCost(11.0);
		opening2.setFromDate(LocalDate.parse("2020-12-27"));
		opening2.setToDate(LocalDate.parse("2021-03-10"));
		opening2.setStartTime(LocalTime.parse("09:00:00"));
		opening2.setEndTime(LocalTime.parse("10:00:00"));
		opening2.setWeekday(DayOfWeek.THURSDAY);
		//openingRepository.save(opening2);
		
		Opening opening3 = new Opening();
		opening3.setCost(12.0);
		opening3.setFromDate(LocalDate.parse("2020-12-27"));
		opening3.setToDate(LocalDate.parse("2021-03-19"));
		opening3.setStartTime(LocalTime.parse("11:00:00"));
		opening3.setEndTime(LocalTime.parse("15:00:00"));
		opening3.setWeekday(DayOfWeek.WEDNESDAY);
		//openingRepository.save(opening3);
		
		Opening opening4 = new Opening();
		opening4.setCost(13.0);
		opening4.setFromDate(LocalDate.parse("2020-12-27"));
		opening4.setToDate(LocalDate.parse("2021-03-13"));
		opening4.setStartTime(LocalTime.parse("18:00:00"));
		opening4.setEndTime(LocalTime.parse("22:00:00"));
		opening4.setWeekday(DayOfWeek.FRIDAY);
		//openingRepository.save(opening4);	
		
		Opening opening5 = new Opening();
		opening5.setCost(14.0);
		opening5.setFromDate(LocalDate.parse("2020-12-27"));
		opening5.setToDate(LocalDate.parse("2021-03-30"));
		opening5.setStartTime(LocalTime.parse("07:00:00"));
		opening5.setEndTime(LocalTime.parse("10:00:00"));
		opening5.setWeekday(DayOfWeek.MONDAY);
		//openingRepository.save(opening5);
		
		Opening opening6 = new Opening();
		opening6.setCost(10.0);
		opening6.setFromDate(LocalDate.parse("2020-12-27"));
		opening6.setToDate(LocalDate.parse("2021-03-30"));
		opening6.setStartTime(LocalTime.parse("11:00:00"));
		opening6.setEndTime(LocalTime.parse("15:00:00"));
		opening6.setWeekday(DayOfWeek.MONDAY);
		//openingRepository.save(opening6);
		
		Opening opening7 = new Opening();
		opening7.setCost(10.0);
		opening7.setFromDate(LocalDate.parse("2020-12-27"));
		opening7.setToDate(LocalDate.parse("2021-03-30"));
		opening7.setStartTime(LocalTime.parse("11:00:00"));
		opening7.setEndTime(LocalTime.parse("15:00:00"));
		opening7.setWeekday(DayOfWeek.MONDAY);
		
		
		
		
		
		Location location1 = new Location();
		location1.setCreator(coach);
		location1.setAvaliableSports(Arrays.asList(sportSwimming));
		location1.setGpxCoordinates("[[45.8014667,15.9935691]]");
		locationRepository.save(location1);
		
		Location location2 = new Location();
		location2.setCreator(athlete);
		location2.setAvaliableSports(Arrays.asList(sportFootball));
		location2.setGpxCoordinates("[[45.7889118,16.0004113],[45.7880754,16.00035],[45.7880455,16.0011889],[45.7888819,16.0012503],[45.7889118,16.0004113]]");
		locationRepository.save(location2);
		
		Location location3 = new Location();
		location3.setCreator(athlete2);
		location3.setAvaliableSports(Arrays.asList(sportRowing));
		location3.setGpxCoordinates("[[45.7861684,15.9122732],[45.7757830,15.9342458],[45.7769803,15.9356191],[45.7869166,15.9131744]]");
		locationRepository.save(location3);
		
		PaidLocation paidLocation1 = new PaidLocation();
		paidLocation1.setGpxCoordinates("[[45.8024192,15.9637387],[45.8024836,15.9637996],[45.8025471,15.9638208],[45.8026652,15.9638403],[45.8027472,15.9638228],[45.8027952,15.9637815],[45.8028421,15.9637148],[45.8028807,15.9635796],[45.8028795,15.9634591],[45.8028603,15.9633517],[45.8028375,15.9632702],[45.8028083,15.9632146],[45.8027782,15.9631695],[45.8027232,15.9631125],[45.8026522,15.9630555],[45.8025718,15.9630196],[45.8025102,15.9630103],[45.8024626,15.9630174],[45.8024067,15.9630346],[45.8023559,15.9630627],[45.8022889,15.9631216],[45.802256,15.9631682],[45.80223,15.963231],[45.8022241,15.9632988],[45.8022247,15.9633728],[45.802235,15.9634347],[45.8022576,15.963505],[45.8022911,15.9635804],[45.8023402,15.9636564],[45.8024192,15.9637387]]");
		paidLocation1.setCreator(renter);
		paidLocation1.setName("Košarkaški centar Dražen Petrović");
		paidLocation1.setOpenings(Arrays.asList(opening1,opening2,opening3));
		paidLocation1.setOwnershipCertificate(Arrays.asList(ownershipDocumentation2, ownershipDocumentation4));
		paidLocationRepository.save(paidLocation1);
		
		PaidLocation paidLocation2 = new PaidLocation();
		paidLocation2.setGpxCoordinates("[[45.7827397,15.9454758],[45.7829547,15.9454695],[45.7829552,15.9454045],[45.7829846,15.9454041],[45.7829838,15.9452766],[45.7830223,15.9452766],[45.783017,15.9450009],[45.7831691,15.944991],[45.7831696,15.9448092],[45.7832006,15.9448067],[45.7831963,15.9444015],[45.783236,15.9443981],[45.7832647,15.9443611],[45.7832702,15.9443203],[45.7832594,15.9442805],[45.7832313,15.9442534],[45.7831944,15.9442507],[45.7831855,15.9438309],[45.7826035,15.9438512],[45.7826239,15.9450178],[45.7827324,15.9450138],[45.7827397,15.9454758]]");
		paidLocation2.setCreator(renter);
		paidLocation2.setName("Sportsko rekreativni centar Mladost");
		paidLocation2.setOpenings(Arrays.asList(opening4,opening5,opening6));
		paidLocation2.setOwnershipCertificate(Arrays.asList(ownershipDocumentation1));
		paidLocationRepository.save(paidLocation2);
		
		PaidLocation paidLocation3 = new PaidLocation();
		paidLocation3.setGpxCoordinates("[[45.774417,15.992248],[45.7744111,15.9918188],[45.7743751,15.9918188],[45.7743213,15.9916729],[45.7735849,15.9917073],[45.7735849,15.9921536],[45.7736089,15.9921879],[45.7736089,15.9922351],[45.7736089,15.9922737],[45.7739193,15.9922638],[45.7739221,15.9923787],[45.7740099,15.9923774],[45.7740091,15.992261],[45.774417,15.992248]]");
		paidLocation3.setCreator(renter);
		paidLocation3.setName("Bazen Utrine");
		paidLocation3.setOpenings(Arrays.asList(opening7));
		paidLocation3.setOwnershipCertificate(Arrays.asList(ownershipDocumentation3));
		paidLocationRepository.save(paidLocation3);
		
		
		
		SportEvent sportEvent1 = new SportEvent();
		sportEvent1.setStartDateTime(LocalDateTime.parse("2021-02-13T10:00:00"));
		sportEvent1.setEndDateTime(LocalDateTime.parse("2021-02-13T11:00:00"));
		sportEvent1.setLocation(location1);
		sportEvent1.setMaxNumberOfParticpents(10);
		sportEvent1.setOrganizer(coach);
		sportEvent1.setSport(sportBasketball);
		sportEventRepository.save(sportEvent1);
		
		SportEvent sportEvent2 = new SportEvent();
		sportEvent2.setStartDateTime(LocalDateTime.parse("2021-02-04T12:00:00"));
		sportEvent2.setEndDateTime(LocalDateTime.parse("2021-02-04T13:00:00"));
		//sportEvent2.setLocation(paidLocation3);
		sportEvent2.setMaxNumberOfParticpents(3);
		sportEvent2.setOrganizer(athlete);
		sportEvent2.setSport(sportSwimming);
		sportEvent2.setLocationReservationInquery(Set.of(paidLocation2, paidLocation3));
		sportEvent2.setParticipents(Set.of(athlete1,coach,athlete2));
		sportEventRepository.save(sportEvent2);
		
		SportEvent sportEvent3 = new SportEvent();
		sportEvent3.setStartDateTime(LocalDateTime.parse("2021-02-04T10:00:00"));
		sportEvent3.setEndDateTime(LocalDateTime.parse("2021-02-04T15:00:00"));
		sportEvent3.setLocation(location1);
		sportEvent3.setMaxNumberOfParticpents(15);
		sportEvent3.setOrganizer(athlete);
		sportEvent3.setSport(sportFootball);
		sportEvent3.setParticipents(Set.of(athlete2,athlete3));
		sportEventRepository.save(sportEvent3);
		
		SportEvent sportEvent4 = new SportEvent();
		sportEvent4.setStartDateTime(LocalDateTime.parse("2021-02-16T10:00:00"));
		sportEvent4.setEndDateTime(LocalDateTime.parse("2021-02-16T12:00:00"));
		sportEvent4.setLocation(location3);
		sportEvent4.setMaxNumberOfParticpents(8);
		sportEvent4.setOrganizer(athlete2);
		sportEvent4.setSport(sportRowing);
		sportEvent4.setParticipents(Set.of(athlete2, athlete3, athlete1));
		sportEventRepository.save(sportEvent4);
		
		SportEvent sportEvent5 = new SportEvent();
		sportEvent5.setStartDateTime(LocalDateTime.parse("2021-02-05T12:00:00"));
		sportEvent5.setEndDateTime(LocalDateTime.parse("2021-02-05T13:00:00"));
		//sportEvent5.setLocation(paidLocation3);
		sportEvent5.setMaxNumberOfParticpents(3);
		sportEvent5.setOrganizer(athlete);
		sportEvent5.setSport(sportSwimming);
		sportEvent5.setLocationReservationInquery(Set.of(paidLocation2, paidLocation3));
		sportEvent5.setParticipents(Set.of(athlete1,coach,athlete2));
		sportEventRepository.save(sportEvent5);
		
		SportEvent sportEvent6 = new SportEvent();
		sportEvent6.setStartDateTime(LocalDateTime.parse("2021-02-17T12:00:00"));
		sportEvent6.setEndDateTime(LocalDateTime.parse("2021-02-17T13:00:00"));
		//sportEvent6.setLocation(paidLocation1);
		sportEvent6.setMaxNumberOfParticpents(4);
		sportEvent6.setOrganizer(coach);
		sportEvent6.setSport(sportWrestling);
		sportEvent6.setLocationReservationInquery(Set.of(paidLocation1));
		sportEvent6.setParticipents(Set.of(coach,athlete2));
		sportEventRepository.save(sportEvent6);
		
		SportEvent sportEvent7 = new SportEvent();
		sportEvent7.setStartDateTime(LocalDateTime.parse("2021-02-18T10:00:00"));
		sportEvent7.setEndDateTime(LocalDateTime.parse("2021-02-18T12:00:00"));
		sportEvent7.setLocation(location3);
		sportEvent7.setMaxNumberOfParticpents(8);
		sportEvent7.setOrganizer(athlete2);
		sportEvent7.setSport(sportRowing);
		sportEvent7.setParticipents(Set.of(athlete2, athlete3, athlete1));
		sportEventRepository.save(sportEvent7);

		SportEvent sportEvent8 = new SportEvent();
		sportEvent8.setStartDateTime(LocalDateTime.parse("2021-02-20T13:00:00"));
		sportEvent8.setEndDateTime(LocalDateTime.parse("2021-02-20T14:00:00"));
		sportEvent8.setLocation(location3);
		sportEvent8.setMaxNumberOfParticpents(2);
		sportEvent8.setOrganizer(athlete2);
		sportEvent8.setSport(sportRowing);
		sportEvent8.setParticipents(Set.of(athlete2, athlete3));
		sportEventRepository.save(sportEvent8);

		SportEvent sportEvent9 = new SportEvent();
		sportEvent9.setStartDateTime(LocalDateTime.parse("2021-02-17T13:00:00"));
		sportEvent9.setEndDateTime(LocalDateTime.parse("2021-02-17T14:00:00"));
		sportEvent9.setLocation(location2);
		sportEvent9.setMaxNumberOfParticpents(22);
		sportEvent9.setOrganizer(athlete2);
		sportEvent9.setSport(sportFootball);
		sportEvent9.setParticipents(Set.of(athlete2));
		sportEventRepository.save(sportEvent9);

		SportEvent sportEvent10 = new SportEvent();
		sportEvent10.setStartDateTime(LocalDateTime.parse("2021-03-17T13:00:00"));
		sportEvent10.setEndDateTime(LocalDateTime.parse("2021-03-17T14:00:00"));
		sportEvent10.setLocation(location2);
		sportEvent10.setMaxNumberOfParticpents(22);
		sportEvent10.setOrganizer(athlete2);
		sportEvent10.setSport(sportFootball);
		sportEvent10.setParticipents(Set.of(athlete1, athlete2, athlete3, athlete));
		sportEventRepository.save(sportEvent10);
		
		
		SportEvent sportEvent11 = new SportEvent();
		sportEvent11.setStartDateTime(LocalDateTime.parse("2021-02-21T13:00:00"));
		sportEvent11.setEndDateTime(LocalDateTime.parse("2021-02-21T14:00:00"));
		sportEvent11.setLocation(location3);
		sportEvent11.setMaxNumberOfParticpents(10);
		sportEvent11.setOrganizer(athlete2);
		sportEvent11.setSport(sportRowing);
		sportEvent11.setParticipents(Set.of(athlete1, athlete, athlete3));
		sportEventRepository.save(sportEvent11);
		
		
		PaidSportEvent paidSportEvent1 = new PaidSportEvent();
		paidSportEvent1.setStartDateTime(LocalDateTime.parse("2021-02-10T10:00:00"));
		paidSportEvent1.setEndDateTime(LocalDateTime.parse("2021-02-10T11:00:00"));
		paidSportEvent1.setLocation(paidLocation2);
		paidSportEvent1.setMaxNumberOfParticpents(10);
		paidSportEvent1.setOrganizer(coach);
		paidSportEvent1.setSport(sportSwimming);
		paidSportEvent1.setCost(80.00);
		paidSportEvent1.setParticipents(Set.of(athlete1));
		sportEventRepository.save(paidSportEvent1);
	
		
		PaidSportEvent paidSportEvent2 = new PaidSportEvent();
		paidSportEvent2.setStartDateTime(LocalDateTime.parse("2021-02-02T08:00:00"));
		paidSportEvent2.setEndDateTime(LocalDateTime.parse("2021-02-02T09:30:00"));
		paidSportEvent2.setLocation(location2);
		paidSportEvent2.setMaxNumberOfParticpents(12);
		paidSportEvent2.setOrganizer(coach);
		paidSportEvent2.setSport(sportFootball);
		paidSportEvent2.setCost(140.00);
		paidSportEvent2.setParticipents(Set.of(athlete1,athlete3));
		sportEventRepository.save(paidSportEvent2);
		
		
		PaidSportEvent paidSportEvent3 = new PaidSportEvent();
		paidSportEvent3.setStartDateTime(LocalDateTime.parse("2021-02-03T15:00:00"));
		paidSportEvent3.setEndDateTime(LocalDateTime.parse("2021-02-03T16:00:00"));
		paidSportEvent3.setLocation(paidLocation1);
		paidSportEvent3.setMaxNumberOfParticpents(14);
		paidSportEvent3.setOrganizer(coach);
		paidSportEvent3.setSport(sportBasketball);
		paidSportEvent3.setCost(200.00);
		paidSportEvent3.setParticipents(Set.of(athlete1,athlete2,athlete3));
		sportEventRepository.save(paidSportEvent3);


		PaidSportEvent paidSportEvent4 = new PaidSportEvent();
		paidSportEvent4.setStartDateTime(LocalDateTime.parse("2021-04-04T18:00:00"));
		paidSportEvent4.setEndDateTime(LocalDateTime.parse("2021-04-04T19:00:00"));
		paidSportEvent4.setLocation(paidLocation1);
		paidSportEvent4.setMaxNumberOfParticpents(14);
		paidSportEvent4.setOrganizer(coach);
		paidSportEvent4.setSport(sportBasketball);
		paidSportEvent4.setCost(200.00);
		paidSportEvent4.setParticipents(Set.of(athlete1,athlete2));
		sportEventRepository.save(paidSportEvent4);

	}
	
}
