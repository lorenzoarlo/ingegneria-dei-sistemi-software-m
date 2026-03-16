package conway26appl.caller;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.net.http.HttpClient;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
import java.net.http.WebSocket;
 
/*
 * PREMESSA: lanciare SistemaSJavalApplMsgsQueued
 *  
 * Componente che usa un WebSocketClient
 * per inviare messaggi su una WebSocket
 * e per elaborare i messaggi inviati dal server
 * Usa un CountDownLatch per terminare
 */

public class CallerServerWs  {  
	private IApplMessage reqmsg  = CommUtils.buildRequest("clientjava", "eval", "CELL", "server"  );
	private IApplMessage setctrl = CommUtils.buildRequest("clientjava", "eval", "setcontroller", "server"  );
	// Un latch per evitare che il programma termini prima di ricevere la risposta
	protected CountDownLatch latch = new CountDownLatch(1); //Inizializzo a 1 perché aspetto UNA risposta dal server
    protected HttpClient client = HttpClient.newHttpClient(); //WebSocketClient client;
    protected String name;
    
    public CallerServerWs( ) throws Exception {
//    	this.name = name;
    	setUp();
        //doJob();    	
    }

    protected void setUp( ) throws InterruptedException {
    	HttpClient client = HttpClient.newHttpClient();
        
        WebSocket webSocket = client.newWebSocketBuilder()
            .buildAsync(URI.create("ws://localhost:8080/chat"), new WebSocketListener(latch))
            .join();
        
        latch.await();
    }
    
    private static class WebSocketListener implements WebSocket.Listener {
        private final CountDownLatch latch;

        public WebSocketListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("--- Connessione aperta ---");
            WebSocket.Listener.super.onOpen(webSocket);
        }

        /*
         * I metodi del listener restituiscono un CompletionStage. 
         * Questo permette di gestire messaggi molto grandi o operazioni lente in modo non bloccante. 
         * Di default, richiamare super.onText è sufficiente.
         * 
         */
        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        	System.out.println(data);
            var msg = new ApplMessage(data.toString());
            System.out.println(msg);
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.println("--- Connessione chiusa: " + reason + " ---");
            latch.countDown();
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.err.println("Errore: " + error.getMessage());
            latch.countDown();
        }
    }

    
//    protected void sendToServer() {
//    	client.send("0.0");
//    }
    
//    public void doJob() {
//        try {
//            System.out.println("connect: "  );
//            client.connect();
//            while( ! client.isOpen() ) {
//            	CommUtils.outblue("waiting connections ...");
//            	CommUtils.delay(500);
//            }
//            sendToServer( );   //reqmsg.toJsonString()        
//            latch.await();
//            client.close();        
//            //System.exit(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }   	
//    }
  
    
    public static void main(String[] args) throws Exception {
    	System.out.println("Java.version="+ System.getProperty("java.version"));
    	CallerServerWs client = new CallerServerWs();
//				URI.create("ws://localhost:8080/eval"));
     }
}

