package projects.RateBet.nodes.messages;

import sinalgo.nodes.messages.Message;

public class Teste extends Message {
	String data;
	
	
	
	/**
	 * @param data
	 */
	public Teste(String data) {
		super();
		this.data = data;
	}



	public String getData() {
		return data;
	}



	public void setData(String data) {
		this.data = data;
	}



	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new Teste(data);
	}

}
