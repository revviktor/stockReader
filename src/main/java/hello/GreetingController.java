package hello;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import org.springframework.ui.Model;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

@Controller
public class GreetingController {

	@Autowired
	MessageSender messageSender;

  @Autowired
  Starter starter;

	/*@RequestMapping("/query")
	public String greeting(
			@RequestParam(value = "borse", required = true) String borse,
			@RequestParam(value = "logic", required = true) String logic,
			Model model) {
		model.addAttribute("borse", borse);
		model.addAttribute("logic", logic);
		model.addAttribute("result", "resultlogic");
		return "greeting";
	}*/
	@RequestMapping("/query")
  //@RequestMapping(method = GET, value = "test")
	public String greeting(	Model model) throws IOException {
		Stock tesla = YahooFinance.get("TSLA", true);

			model.addAttribute("borse", tesla.getName());
			return "greeting";
	}
			
	
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Response greeting(RequestMessage message) throws Exception {
		messageSender.sendMessage(message.getName()+"-"+message.getLogic());
		try {
			starter.start(message.getName(),message.getLogic());
		}catch (Exception e){
			e.printStackTrace();
			return new Response("Error:" + e.getMessage());
		}
		return new Response("Finished");
	}

}