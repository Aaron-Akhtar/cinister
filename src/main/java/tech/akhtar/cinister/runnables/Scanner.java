package tech.akhtar.cinister.runnables;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static tech.akhtar.cinister.Cinister.*;
public class Scanner implements Runnable{
    private static List<String> tried = new ArrayList<>();

    private static String genUrl() throws MalformedURLException {
        Random random = new Random();
        return "http://" + random.nextInt(255) + "." + random.nextInt(255) + "."  + random.nextInt(255) + "." + random.nextInt(255) + "/phpmyadmin";
    }

    @Override
    public void run() {
        while(true){
            try{
                String r = genUrl();
                try(Socket socket = new Socket()){
                    socket.connect(new InetSocketAddress(r, 80), CONNECT_TIMEOUT);
                }
                URL url = new URL(r);
                if (tried.contains(url.getHost())) continue;
                tried.add(url.getHost());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try(AutoCloseable autoCloseable = () -> connection.disconnect()){
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 7.0; SM-G930VC Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/58.0.3029.83 Mobile Safari/537.36");
                    connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setRequestMethod("GET");
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String x;
                        int f =0;                                   // if the 'f' int is of a value of 2+ it will consider it a legitimate phpmyadmin login page as the
                                                                    // phpmyadmin login page contains at least 2 instances of the word 'phpmyadmin'
                        while ((x = reader.readLine()) != null) {
                            if (x.toLowerCase().contains("phpmyadmin"))f++;
                        }
                        if (f > 1) {
                            System.out.println(WHITE + "[----------------------------]");
                            System.out.println(YELLOW + "[Cinister] " + GREEN + "Potential phpMyAdmin installed on "+YELLOW+"["+ WHITE+url.toString()+ YELLOW +"]");
                            System.out.println(WHITE + "[----------------------------]");
                            save(url.toString());
                            continue;
                        }
                    }
                }

            }catch (Exception e){

            }
            try{
                Thread.sleep(100);
            }catch (Exception e){

            }
        }
    }
}
