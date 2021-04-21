package myboot.vega2k.restapi.index;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import myboot.vega2k.restapi.events.EventController;

@RestController
public class IndexController {
	@GetMapping("/api")
	public RepresentationModel index() {
		var index = new RepresentationModel();
		index.add(linkTo(EventController.class).withRel("events"));
		return index;
	}
}
