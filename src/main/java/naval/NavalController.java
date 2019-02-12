package naval;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;	
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@RestController
@RequestMapping("/naval")
public class NavalController {
	private static final Logger log = LoggerFactory.getLogger(NavalController.class);
	private State state;


	/*
		Width and height should not be magic set constants
	*/
	private int width = 10;
	private int height = 10;

	private Player p1;
	private Player p2;
	
	private Player currentPlayer;
	
	private Map<String, Player> players = new HashMap<>();


	private int[] parseMovement(String s){
		String mv = s.toUpperCase();
		int firstLetter = mv.charAt(0)-'A';
		int firstNumber = Integer.parseInt(s.substring(1)) - 1;
		
		log.info(String.valueOf(firstLetter) + ";" + String.valueOf(firstNumber));
		return new int[]{firstLetter, firstNumber};
	}

	private void parseInitialShips(String s, Player p){
		String[] usedCells = s.split(",");
		int x,y;
		for(String ship: usedCells){
			String[] tmp = ship.split("-");
			x = Integer.parseInt(tmp[0]);
			y = Integer.parseInt(tmp[1]);
			p.occupyCell(x,y);
		}
	}
	


    @RequestMapping(method=RequestMethod.PUT)
    public ResponseEntity<String> initPlayer(@RequestParam(value="moves") String positions) {
		String event = "Every player has logged\n";
		if (p1 == null){
			p1 = new Player(width, height);
			players.put(String.valueOf(1), p1);
			parseInitialShips(positions, p1);	
			event = "Player 1 logged in\n";
			state = new State("1");
		}
		else if (p2==null){
			p2 = new Player(width, height);
			p2.setNext(p1);
			p1.setNext(p2);
			players.put(String.valueOf(2), p2);
			parseInitialShips(positions, p2);
			currentPlayer = p1;
			event = "Player 2 logged in\n";
		}
		log.info(event);
		return new ResponseEntity<String>(event, HttpStatus.ACCEPTED);
    }

    @RequestMapping(method=RequestMethod.POST)
    public State makeMove(@RequestParam(value="player") String player, @RequestParam(value="move", defaultValue="none") String move) {
    	if (p1 == null || p2 == null){
			state = new State("NotEnoughPlayers");
			return state;
		}
		if (currentPlayer == players.get(player)){
		
			int[] play = parseMovement(move);	
			players.get(player).destroyCell(play[0], play[1]);
			currentPlayer = players.get(player).getNext();
			state = computeState();
		}
		return state;
    }

	private State computeState(){
		if (p1.isDefeated()){
			state.setNext("2");
			state.stop();
		}
		else if (p2.isDefeated()){
			state.setNext("1");
			state.stop();
		}
		else if (currentPlayer == p1){
			state.setNext("1");
		}
		else{
			state.setNext("2");
		}
		return state;
	}
    
    @RequestMapping(method=RequestMethod.GET)
    public State getState(){
		if (p1 == null || p2 == null){
			state = new State("NotEnoughPlayers");
			return state;
		}
 		return computeState();		   
    }
    
}
