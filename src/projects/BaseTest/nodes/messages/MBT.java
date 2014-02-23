package projects.BaseTest.nodes.messages;

import sinalgo.nodes.messages.Message;

public class MBT extends Message {

	String a = "";
	
	
	/**
	 * @param a
	 */
	public MBT(String a) {
		super();
		this.a = a;
	}



	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new MBT(a);
	}



	public String getA() {
		return a;
	}



	public void setA(String a) {
		this.a = a;
	}
	
	

}
