//create or start a game, we shall see. right now its just hello world
package test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	@RequestMapping("/creategame")
	//TODO: pass ID value
	public String makeGame(String gameName, ConnectedClient c)
	{	
		Main.server.gamesList.createGame(c, gameName);
			return "Game has been started";
	}
	
	
	@RequestMapping("/joingame")
	//TODO: pass ID value of game
	public String joinGame(Game g, ConnectedClient c)
	{
			g.addMember(c);
			return "game has been joined";
			/* if (numplayers = maximum
			 g.startGame();
			 */
	}
	
	public String startGame(Game g)
	{
		g.startGame();
		return("game has been started");
	}
}
