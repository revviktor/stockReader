package hello;

public class RequestMessage {

	private String name;
	private String logic;
	
	public RequestMessage(String name, String logic) {
		super();
		this.name = name;
		this.logic = logic;
	}

	RequestMessage() {
	}

	public RequestMessage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}
}