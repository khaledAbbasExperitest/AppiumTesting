package playGround;

import java.io.*;

public class playing {
    public static void main(String[] args) throws Exception {


        for (int i=0;i<1;i++) {
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "cd \"C:\\Users\\DELL\\Desktop\\features\" \nstart http-server -a 0.0.0.0 -p 8181");

            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                System.out.println(line);
            }
            Thread.sleep(10000);
        }
    }
}