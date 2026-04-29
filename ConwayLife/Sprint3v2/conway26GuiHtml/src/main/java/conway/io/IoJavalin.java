package main.java.conway.io;

import java.nio.file.Files;
import java.nio.file.Path;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class IoJavalin {
	public IoJavalin() {
		var app = Javalin.create(config -> {
			config.staticFiles.add(staticFiles -> {
				staticFiles.directory = "/page";
				staticFiles.location = Location.CLASSPATH; // Cerca dentro il JAR/Classpath
				/*
				 * i file sono "impacchettati" con il codice, non cercati sul disco rigido
				 * esterno.
				 */
			});
		}).start(8080);

		app.get("/", ctx -> {
			Path path = Path.of("./src/main/resources/page/ConwayInOutPage.html");
			if (Files.exists(path)) {
				// Usiamo Files.newInputStream che è più moderno di FileInputStream
				ctx.contentType("text/html").result(Files.newInputStream(path));
			} else {
				ctx.status(404).result("File non trovato nel file system");
			}
			// ctx.result("Hello from Java!")); //la forma più semplice di risposta
		});

		app.ws("/eval", ws -> {
			ws.onConnect(ctx -> {
				System.out.println("Client connected!");
				context = ctx;
			});
			ws.onMessage(ctx -> {
				String message = ctx.message();
				var msg = new ApplMessage(message);
				System.out.println(msg.toString());

				switch (msg.msgContent()) {
				case "start":
					outDev.getController().onStart();
					break;
				case "stop":
					outDev.getController().onStop();
					break;
				case "clear":
					outDev.getController().onClear();
					break;
				case "cell":
					String content = msg.msgContent(); // Es: "cell(5, 10)"

					try {
						String values = content.substring(content.indexOf("(") + 1, content.lastIndexOf(")"));

						String[] parts = values.split(",");

						if (parts.length == 2) {
							int column = Integer.parseInt(parts[0].trim());
							int row = Integer.parseInt(parts[1].trim());

							System.out.println("Switched cell " + row + " " + column);
							outDev.getController().switchCellState(row, column);
						}
					} catch (Exception e) {
						System.err.println("Errore nel parsing del messaggio: " + content);
					}
				default:
					break;
				}

			});
		});
	}

	public static void main(String[] args) {
		var resource = IoJavalin.class.getResource("/pages");
		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);
		new IoJavalin();
	}

	public WsContext getContext() {
		return context;
	}

	public void setOutInWs(OutInWs ws) {
		outDev = ws;
	}

	private OutInWs outDev;
	private WsContext context;
}
