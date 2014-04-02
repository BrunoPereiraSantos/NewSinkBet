/*
 Copyright (c) 2007, Distributed Computing Group (DCG)
                    ETH Zurich
                    Switzerland
                    dcg.ethz.ch

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the
   distribution.

 - Neither the name 'Sinalgo' nor the names of its contributors may be
   used to endorse or promote products derived from this software
   without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package projects.defaultProject.nodes.messages;

import sinalgo.nodes.messages.Message;



/**
 * A standard message type consisting only of a integer as payload.
 */
public class EventMessage extends Message {
	
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
	public double firedTime = 0; 
	
	
	/**
	 * The payload of the Message: an integer.
	 */
	public int value = 0; 
	
	
	/**
	 * The constructor for the IntMessage class.
	 *
	 * @param i The integer the payload has to be set to.
	 */
	public EventMessage(int idSender, int nextHop, double timeFired, int value){
		this.value = value;
		this.nextHop = nextHop;
		this.idSender = idSender;
		this.firedTime = timeFired;
	}
	
	
	public Message clone(){
		return new EventMessage(this.idSender, this.nextHop, this.firedTime, this.value);
	}

	public int getNextHop() {
		return nextHop;
	}

	public void setNextHop(int nextHop) {
		this.nextHop = nextHop;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getIdSender() {
		return idSender;
	}

	public void setIdSender(int idSender) {
		this.idSender = idSender;
	}

	public double getFiredTime() {
		return firedTime;
	}

	public void setFiredTime(double firedTime) {
		this.firedTime = firedTime;
	}

	@Override
	public String toString() {
		return "EventMessage [nextHop=" + nextHop + ", idSender=" + idSender
				+ ", firedTime=" + firedTime + ", value=" + value + "]";
	}

	
}
