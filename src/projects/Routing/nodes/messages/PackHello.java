package projects.Routing.nodes.messages;

import sinalgo.nodes.messages.Message;

public class PackHello extends Message implements Pack{

	private float metric;
	private int hops;
	private int paths;
	private int sinkID;

	public PackHello() {
		super();
	}

	public PackHello(float metric, int hops, int paths, int sinkID) {
		super();
		this.metric = metric;
		this.hops = hops;
		this.sinkID = sinkID;
		this.paths = paths;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new PackHello(this.metric, this.hops, this.paths, this.sinkID);
	}

	public float getMetric() {
		return metric;
	}

	public void setMetric(float metric) {
		this.metric = metric;
	}

	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public int getSinkID() {
		return sinkID;
	}

	public void setSinkID(int sinkID) {
		this.sinkID = sinkID;
	}

	public int getPaths() {
		return paths;
	}

	public void setPaths(int paths) {
		this.paths = paths;
	}

	@Override
	public String toString() {
		return "PackageHello [metric=" + metric + ", hops=" + hops + ", paths="
				+ paths + ", sinkID=" + sinkID + "]";
	}

}
