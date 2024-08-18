package com.potluck.buffet;

import com.potluck.buffet.domain.model.*;
import com.potluck.buffet.domain.repository.IEventRepository;
import com.potluck.buffet.domain.repository.IRoleRepository;
import com.potluck.buffet.domain.repository.IUserRepository;
import com.potluck.buffet.helpers.StaticData;
import com.potluck.buffet.helpers.TestValuesGenerator;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

	@Log4j2
	@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
	@EnableMongoRepositories(basePackages = {
			"com.potluck.buffet.domain.repository"
	})
	public class BuffetApplication {

		@Autowired
		private IUserRepository userRepository;

		@Autowired
		private IEventRepository eventRepository;

		@Autowired
		private IRoleRepository roleRepository;

		@Autowired
		private StaticData staticData;

		@Value("${spring.profiles.active:Unknown}")
		private String activeProfiles;

		private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		public static void main(String[] args) {
			SpringApplication.run(BuffetApplication.class, args);
		}

		private void createRoles() {
			Role adminRole = Role.builder()
					.role("ROLE_ADMIN")
					.build();
			Role regularRole = Role.builder()
					.role("ROLE_REGULAR")
					.build();
			roleRepository.save(adminRole);

			roleRepository.save(adminRole);
			roleRepository.save(regularRole);

		}
		private void createRegularUsers(int nb, int nbEventPerUser)
		{
			List<Role> roles = new ArrayList<Role>();
			roles.add(Role.builder().role("ROLE_REGULAR").build());

			for(int i=0; i<nb; i++) {
				String firstName = TestValuesGenerator.getRandomFirstName();
				String lastName = TestValuesGenerator.getRandomFirstName();
				User user = User.builder()
						.firstName(firstName)
						.lastName(lastName.toUpperCase())
						.email(firstName + "." + lastName + "@gmail.com")
						.password(bCryptPasswordEncoder.encode("toto"))
						.roles(roles)
						.build();
				userRepository.save(user);
				createEvent(user, nbEventPerUser);
			}
		}

		private void createEvent(User user, int nbEventPerUser) {
			for(int i=0; i<nbEventPerUser; i++) {
				if(TestValuesGenerator.getRandomNumber(0,20)%6 == 0) {
					Event event = Event.builder()
							.organizer(user)
							.startTime(Instant.now().plus(TestValuesGenerator.getRandomDayOffset(), ChronoUnit.DAYS))
							.status(Event.EventStatus.PENDING)
							.title(TestValuesGenerator.getRandomEventTitle())
							.build();
					eventRepository.save(event);
				}
			}
		}

		private void createAdminUser() {
			List<Role> roles = new ArrayList<>();
			Role.builder().role("ROLE_ADMIN").build();
			String firstName = "admin";
			String lastName = "admin";
			User user = User.builder()
					.firstName(firstName)
					.lastName(lastName.toUpperCase())
					.email(firstName + "." + lastName + "@gmail.com")
					.password(bCryptPasswordEncoder.encode("toto"))
					.roles(roles)
					.build();
			userRepository.save(user);

		}
		private void createEventsForUser(User user, int numberOfEvents) {
			for (int i = 0; i < numberOfEvents; i++) {
				if (TestValuesGenerator.getRandomNumber(0, 20) % 6 == 0) {
					Event event = Event.builder()
							.organizer(user)
							.startTime(Instant.now().plus(TestValuesGenerator.getRandomDayOffset(), ChronoUnit.DAYS))
							.status(Event.EventStatus.PENDING)
							.title(TestValuesGenerator.getRandomEventTitle())
							.build();
					eventRepository.save(event);
				}
			}
		}
		private void createParticipations(int nbAttendees){
			// Pour chaque event de chaque personne
			Iterable<Event> events = eventRepository.findAll();
			Iterable<User> users = userRepository.findAll();

			events.forEach(event -> { // pour chaque Event

				users.forEach(attendee -> { // on cree une participation
					if(attendee.getId() != event.getOrganizer().getId() && TestValuesGenerator.getRandomNumber(0,20)%6 == 0){

						Participation participationOfAttendee = Participation.builder()
								.id("1")
								.nbGuests(0)
								.participant(attendee)
								.status(Participation.ParticipationStatus.INVITED)
								.build();
						event.addParticipation(participationOfAttendee);

						// Adding Participation items
						int all = getItems().size();
						List<ParticipationItem> participationItems = new LinkedList<>();

						for(int i=0; i < TestValuesGenerator.getRandomNumber(0, all     -1); i++){
							Item test = getItems().get(TestValuesGenerator.getRandomNumber(0, getItems().size() -1));

							ParticipationItem participationItem = ParticipationItem.builder()
									.item(test)
									.qty(TestValuesGenerator.getRandomNumber(1,5))
									.build();
							participationOfAttendee.addItem(participationItem);
						}
					}
				});
				eventRepository.save(event);
			});

		}

		private List<Item> getItems() {
			return List.of(
					new Item("1", "PIZZA", Item.Category.MAIN_COURSE),
					new Item("2", "QUICHE", Item.Category.MAIN_COURSE),
					new Item("3", "BIERE", Item.Category.BREWERY),
					new Item("4", "EAU", Item.Category.BREWERY),
					new Item("5", "COCA", Item.Category.BREWERY)
			);
		}

		@PostConstruct
		private void initDb() {
		/*if ("DEV".equalsIgnoreCase(activeProfiles)) {
				flushDB();
				createRoles();
				createRegularUsers(10, 5);
				createAdminUser();
				createParticipations(7);
			}*/
		}

		private void flushDB() {
			roleRepository.deleteAll();
			userRepository.deleteAll();
			eventRepository.deleteAll();
		}
}
