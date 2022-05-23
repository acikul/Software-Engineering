package hr.fer.fringilla.fringillasport;

import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.domain.Athlete;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import hr.fer.fringilla.fringillasport.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class FringillasportApplicationJUnitTests {

	@Autowired
	UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testFindGoodUsername() {
		Optional<User> user = userService.findByUsername("athlete");
		assertThat(user.isPresent());
	}

	@Test
	void testFindBadUsername() {
		Optional<User> user = userService.findByUsername("wrongUsername");
		assertThat(user.isEmpty());
	}

	@Test
	void testSaveAndFindUser() {
		User user = new Athlete("newUsername", "newPassword", "e@mail.com", "name", "surname");
		userService.save(user);
		Optional<User> user1 = userService.findByUsername("newUsername");
		assertThat(user1.isPresent());
	}

	@Test
	void testSporteventControllerFindByGoodId() throws Exception {
		this.mockMvc.perform(get("/api/v1/sportevents/2")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void testSporteventControllerFindByBadId() throws Exception {
		this.mockMvc.perform(get("/api/v1/sportevents/2000")).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	void testLocationControllerFindByGoodId() throws Exception {
		this.mockMvc.perform(get("/api/v1/locations/1")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void testLocationControllerFindByBadId() throws Exception {
		this.mockMvc.perform(get("/api/v1/locations/1000")).andDo(print()).andExpect(status().isBadRequest());
	}
}
