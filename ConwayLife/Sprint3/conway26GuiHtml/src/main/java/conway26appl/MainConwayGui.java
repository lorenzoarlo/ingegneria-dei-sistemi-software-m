package main.java.conway26appl;

import main.java.conway.io.OutInWs;
import main.java.conway.domain.*;
import unibo.basicomm23.utils.CommUtils;

public class MainConwayGui {
	private OutInWs ws = new OutInWs();
	private LifeInterface life = new Life(20, 20);
	private GameController controller = new LifeController(life, ws);

	public MainConwayGui() {
		ws.setController(controller);
	}

	public static void main(String[] args) {
		System.out.println("MainConway | STARTS ");

		var resource = MainConwayGui.class.getResource("/");
		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);

		MainConwayGui app = new MainConwayGui();
 
		// app.configureTheSystemWithHtmlWs(true);
		System.out.println("MainConway | ENDS ");
	}
}