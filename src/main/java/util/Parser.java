package util;

import com.google.gson.Gson;
import entity.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {

    private static HashMap<String, Long> events = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    public static void parseLogs(String filename, DAO dao) throws IOException, SQLException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        Gson gson = new Gson();
        try {
            inputStream = new FileInputStream(filename);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                LogEvent event = gson.fromJson(sc.nextLine(), LogEvent.class);

                if (events.containsKey(event.getId())) {
                    long time1 = events.remove(event.getId());
                    long time2 = event.getTimestamp();
                    long duration = Math.abs(time1 - time2);
                    event.setDuration(duration);
                    event.setAlert(isAlert(duration));
                    dao.insertEvent(event);
                } else {
                    events.put(event.getId(), event.getTimestamp());
                }
            }
            // Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    private static boolean isAlert(long duration) {
        return duration > 4;
    }
}
