package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rv.readData.StockChecker;
import com.rv.readData.yahooApi.StockCheckerYahooApi;
import com.rv.sendMail.SendMail;

@Component
public class Starter {


	@Autowired
	MessageSender messageSender;

	public void start(String name, String logic) throws IOException {
		messageSender.sendMessage("starting...");
		StockChecker checker = new StockCheckerYahooApi(messageSender);
		List<String> papers=readFile2(name);
		checker.compareStocks(papers,logic);
	}

	private List<String> readFile2(String name) throws IOException {
    String file;
    switch(name) {
    case ("nyse"): {
      file = "/symbols/nyse.txt";
      break;
    }
		case ("nasdaq"): {
			file = "/symbols/NASDAQ.txt";
			break;
		}
		case ("europe"): {
			file = "/symbols/europe.txt";
			break;
		}
		case ("champs"): {
			file = "/symbols/champs.txt";
			break;
		}
    default: {
      throw new RuntimeException("no such symbol file");
    }
    }
		/*Path path = FileSystems.getDefault().getPath(".",file);
		InputStream in = Files.newInputStream(path);*/
		InputStream in = getClass().getResourceAsStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		         
		List<String> lines = new ArrayList<>();
		String line = null;
		while ( (line = reader.readLine()) != null )
		    lines.add(line.split("	")[0]);
		return lines;
	}
	
	@Scheduled(cron = "0 0 3 * * *")
	public void doCheckAndSend() throws IOException {
		System.out.println("sendmail starts");
		MessageCollectorSender mss= new MessageCollectorSender();
		StockChecker checker = new StockCheckerYahooApi(mss);
		List<String> papers=readFile2("nyse");
		boolean found = checker.compareStocks(papers,"willfail");
		papers=readFile2("nasdaq");
		found = checker.compareStocks(papers,"willfail") || found;
		papers=readFile2("europe");
		found = checker.compareStocks(papers,"willfail") || found;
		
		if(found){
		SendMail.send(mss.getMessages(), "Talalat van");
		}
	    System.out.println("megvan!!!!");
	}
}