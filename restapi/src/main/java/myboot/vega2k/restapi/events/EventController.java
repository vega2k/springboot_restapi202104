package myboot.vega2k.restapi.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import myboot.vega2k.restapi.common.ErrorsResource;

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

	@GetMapping
	public ResponseEntity<?> queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		Page<Event> page = this.eventRepository.findAll(pageable);
		//PagedModel<EntityModel<Event>> pagedResources = assembler.toModel(page);
		/*
		  1. toModel(Page<T> page, org.springframework.hateoas.server.RepresentationModelAssembler<T,R> assembler)
		  2. RepresentationModelAssembler 는 함수형 인터페이스이다.
		  3. event -> new EventResource(event)  
		      => RepresentationModelAssembler 의 D toModel(T) 메서드를 재정의 하는것을 람다식으로 표현한 것이다
		*/
		PagedModel<RepresentationModel<EventResource>> pagedResources =
				assembler.toModel(page, event -> new EventResource(event));
		
		return ResponseEntity.ok(pagedResources);
		
		//return ResponseEntity.ok(this.eventRepository.findAll(pageable));
	}

	@PostMapping
	public ResponseEntity<?> createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
		// Validation API에서 제공하는 어노테이션을 사용해서 입력항목 검증
		if (errors.hasErrors()) {
			return badRequest(errors);
		}
		// 사용자정의 EventValidator를 사용해서 입력항목 biz logic 검증
		eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		Event event = modelMapper.map(eventDto, Event.class);

		// 등록하기 전에 free와 offline 값을 set 한다
		event.update();

		Event addEvent = eventRepository.save(event);

		// http://localhost:8087/api/events/10
		WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(addEvent.getId());
		URI createUri = selfLinkBuilder.toUri();
		/*
		 * "_links": { "query-events": { "href": "http://localhost:8087/api/events" },
		 * "self": { "href": "http://localhost:8087/api/events/11" }, "update-event": {
		 * "href": "http://localhost:8087/api/events/11" } }
		 */
		EventResource eventResource = new EventResource(addEvent);
		eventResource.add(linkTo(EventController.class).withRel("query-events"));
		eventResource.add(selfLinkBuilder.withRel("update-event"));

		return ResponseEntity.created(createUri).body(eventResource);
	}

	private ResponseEntity<?> badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
}
