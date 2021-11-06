import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DAO;
import util.Parser;

public class LogHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogHandler.class);


    public static void main(String[] args) {
        if(args.length != 1) {
            logger.error("Please provide filename as argument");
            System.exit(1);
        } else {
            String filename = args[0];
            try {
                DAO dao = new DAO();
                dao.createTable();

                Parser.parseLogs(filename, dao);
                logger.info("Successfully parsed the input file");

                dao.selectAll();
//                Clearing the table to prepare it for next run
                dao.clearTable();
                dao.close();
            } catch (Exception e) {
                logger.error("Error parsing file: ", e);
            }
        }
    }
}
