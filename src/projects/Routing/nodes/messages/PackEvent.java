package projects.Routing.nodes.messages;

import sinalgo.nodes.messages.Message;

public class PackEvent extends Message implements Pack {

	/**
	 * The nextHop of the Message: an integer.
	 */
	public int nextHop = 0;

	/**
	 * The ID of the sender message.
	 */
	public int idSender = 0;

	/**
	 * The time that fired mensage
	 */
	public double timeFired = 0;

	/**
	 * The payload of the Message: an integer.
	 */
	public int value = 0;

	/**
	 * The constructor for the IntMessage class.
	 * 
	 * @param i
	 *            The integer the payload has to be set to.
	 */
	public PackEvent(int idSender, int nextHop, double timeFire, int value) {
		this.value = value;
		this.nextHop = nextHop;
		this.idSender = idSender;
		this.timeFired = timeFire;
	}

	@Override
	public float getMetric() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMetric(float metric) {
		// TODO Auto-generated method stub

	}

	public int getNextHop() {
		return nextHop;
	}

	public void setNextHop(int nextHop) {
		this.nextHop = nextHop;
	}

	public int getIdSender() {
		return idSender;
	}

	public void setIdSender(int idSender) {
		this.idSender = idSender;
	}

	public double getTimeFired() {
		return timeFired;
	}

	public void setTimeFired(double timeFired) {
		this.timeFired = timeFired;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new PackEvent(idSender, nextHop, timeFired, value);
	}

}
