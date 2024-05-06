import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ConcurrentHttpClient {
    public static void main(String[] args) {
        String[] urls = {
            "https://www.python.org/",
            "https://www.wikipedia.org/",
            "https://www.example.com/",
        };

        // Create an ExecutorService with a fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(urls.length);

        // Create an array to store the results of the requests
        @SuppressWarnings("unchecked")
        Future<String>[] futures = (Future<String>[]) new Future[urls.length];
        
        // Make HTTP requests in parallel
        for (int i = 0; i < urls.length; i++) {
            final int index = i;
            Callable<String> task = new Callable<String>() {
                public String call() throws Exception {
                    try {
                        URL url = new URL(urls[index]);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");

                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();

                        return urls[index] + " tiene " + content.toString().length() + " bytes";
                    } catch (IOException e) {
                        return "Error al acceder a " + urls[index] + ": " + e.getMessage();
                    }
                }
            };
            futures[i] = executor.submit(task);
        }

        // Get the results of the requests
        for (Future<String> future : futures) {
            try {
                String result = future.get();
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Shut down the ExecutorService
        executor.shutdown();
    }
}
