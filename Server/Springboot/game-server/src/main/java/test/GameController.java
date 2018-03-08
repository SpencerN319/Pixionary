//create or start a game, we shall see. right now its just hello world
package test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	@RequestMapping("/hello")
	public String hello()
	{	
			return "Hello, World!";
	}
}
