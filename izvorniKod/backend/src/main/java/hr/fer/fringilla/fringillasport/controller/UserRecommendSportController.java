package hr.fer.fringilla.fringillasport.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.fringilla.fringillasport.domain.Athlete;
import hr.fer.fringilla.fringillasport.domain.Gender;
import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.service.SportService;
import hr.fer.fringilla.fringillasport.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/{user_id}/recommend_sport")
public class UserRecommendSportController {

	private static String tempOdg = "dezo";

	private static final Logger logger = LoggerFactory.getLogger(UserSportEventParticipateController.class);

	@Autowired
	SportService sportService;

	@Autowired
	UserService userService;

	@GetMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> getUserSportRecommendation(@PathVariable Long user_id) throws IOException {

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User id" + user_id + "is not existed");
			return ResponseEntity.badRequest().build();
		}
		User user = userService.findById(user_id).get();

		if (user instanceof Athlete) {
			Athlete sportas = (Athlete) user;
			Double height = sportas.getHeight();
			Double weight = sportas.getWeight();
			Gender gender = sportas.getGender();
			Date rodz = sportas.getBirthdayDate();

			LocalDate today = LocalDate.now();

			if (height == null || weight == null || gender == null || rodz == null) {

				return ResponseEntity.badRequest().build();
			}

			LocalDate dob = rodz.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Integer age = Period.between(LocalDate.now(), dob).getYears();

			Integer year = today.getYear();

			URL url = new URL("http://127.0.0.1:1234");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Age", age.toString());
			con.setRequestProperty("Height", height.toString());
			con.setRequestProperty("Weight", weight.toString());
			con.setRequestProperty("Year", year.toString());
			con.setRequestProperty("Sex", gender.toString());

			con.setDoOutput(true);
			String jsonInputString = "{\"body\": \"test\"}";

			try (OutputStream os = con.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}

				tempOdg = response.toString();

			}

		} else {
			return ResponseEntity.badRequest().build();
		}

		List<Sport> sportovi = sportService.findAll();
		for (Sport sport : sportovi) {
			if (sport.getName().toLowerCase().equals(tempOdg.toLowerCase())) {
				return ResponseEntity.ok(sport);
			}
		}
		return ResponseEntity.ok(tempOdg);

	}
}
