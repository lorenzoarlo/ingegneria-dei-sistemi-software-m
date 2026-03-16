package conway.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import main.java.conway.domain.Cell;
import main.java.conway.domain.GameController;
import main.java.conway.domain.Grid;
import main.java.conway.domain.IOutDev;
import main.java.conway.domain.Life;
import main.java.conway.domain.LifeController;
import main.java.conway.domain.LifeInterface;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class IoJavalin implements IOutDev {
	public IoJavalin() {
		life = new Life(20, 20);
		controller = new LifeController(life, this);
		
        var app = Javalin.create(config -> {
			config.staticFiles.add(staticFiles -> {
				staticFiles.directory = "/page";
				staticFiles.location = Location.CLASSPATH; // Cerca dentro il JAR/Classpath
				/*
				 * i file sono "impacchettati" con il codice, non cercati sul disco rigido esterno.
				 */
		    });
		}).start(8080);
        
        app.get("/", ctx -> {
    		Path path = Path.of("/conway26GuiHtml-1.0/lib/conway26GuiHtml-1.0.jar/page/ConwayInOutPage.html");   
		    if (Files.exists(path)) {
		        // Usiamo Files.newInputStream che è più moderno di FileInputStream
		        ctx.contentType("text/html").result(Files.newInputStream(path));
		    } else {
		        ctx.status(404).result("File non trovato nel file system");
		    }
		    //ctx.result("Hello from Java!"));  //la forma più semplice di risposta
        });
        
        app.ws("/chat", ws -> {
            ws.onConnect(ctx -> {
            	System.out.println("Client connected!");
            	context = ctx;
            });
            ws.onMessage(ctx -> {
                String message = ctx.message();
                var msg = new ApplMessage(message);
                System.out.println(msg.toString());
 
                if (msg.msgContent().equals("start")) {
                	controller.onStart();
                } else if (msg.msgContent().equals("stop")) {
                	controller.onStop();
                } else if (msg.msgContent().equals("clear")) {
                	controller.onClear();
                } else if (msg.msgContent().contains("cell")) {
	                Pattern p = Pattern.compile("cell\\((\\d+),\\s*(\\d+)\\)");
	                Matcher m = p.matcher(msg.msgContent());
	
	                if (m.find()) {
	                    int column = Integer.parseInt(m.group(1));
	                    int row = Integer.parseInt(m.group(2));
	                    
	                    System.out.println("Switched cell " + row + " " + column);
	                    controller.switchCellState(row, column);     
	                }
                }
            });
        });
	}
	
	public static void main(String[] args) {
		var resource = IoJavalin.class.getResource("/pages");
		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);
		new IoJavalin();
	}
	
	@Override
	public void display(String msg) {
		context.send(msg);
	}

	@Override
	public void displayCell(Cell cell, Grid grid) {
		System.out.println("Displaycell");
	}

	@Override
	public void close() {
		context.closeSession();
	}

	@Override
	public void displayGrid(Grid grid) {
		if (context == null) {
			return;
		}
	
		for (int row = 0; row < grid.getWidth(); row++) {
			for (int column = 0; column < grid.getHeight(); column++) {
				var cellstate = life.getCell(row, column).isAlive() ? "1" : "0";
				context.send("cell(" + row + "," + column + "," + cellstate + ")");
			}
		}
	}
	
	private WsContext context;
	private LifeInterface life;
	private GameController controller; 
}
