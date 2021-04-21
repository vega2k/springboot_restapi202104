package myboot.vega2k.restapi.events;

import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

	// @Autowired EventRepository eventRepository와 같은 의미
	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	private final EventValidator eventValidator;

	public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
		this.eventRepository = eventRepository;
		this.modelMapper = modelMapper;
		this.eventValidator = eventValidator;
	}

	@PostMapping
	public ResponseEntity<?> createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
		// Validation API에서 제공하는 어노테이션을 사용해서 입력항목 검증
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}
		// 사용자정의 EventValidator를 사용해서 입력항목 biz logic 검증
		eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}

		Event event = modelMapper.map(eventDto, Event.class);

		Event addEvent = eventRepository.save(event);

		// http://localhost:8087/api/events/10
		WebMvcLinkBuilder selfLinkBuilder = WebMvcLinkBuilder.linkTo(EventController.class).slash(addEvent.getId());
		URI createUri = selfLinkBuilder.toUri();

		return ResponseEntity.created(createUri).body(addEvent);
	}
}
