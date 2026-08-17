
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;

public class TestJava {

	public static void main(String[] args) {
	
	CompletableFuture.supplyAsync(() -> {
        simulateSlowNetworkCall();
        return "Raw Data from API";
    })
    // 2. Transform the data once it arrives (Non-blocking)
    .thenApply(data -> data + " -> Processed by System")
    
    // 3. Gracefully handle errors if anything breaks in the background
    .exceptionally(throwable -> "Fallback Data (API was down)")
    
    // 4. Consume the final result
    .thenAccept(finalResult -> System.out.println("Result: " + finalResult));

    // The main thread keeps moving instantly while the work happens in the background!
    System.out.println("Main thread is completely free to serve other traffic...");
    
    // Keep JVM alive just for this small example to let background thread finish
    try { Thread.sleep(3000); } catch (InterruptedException e) {}
}
	private static void simulateSlowNetworkCall() {
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

}
