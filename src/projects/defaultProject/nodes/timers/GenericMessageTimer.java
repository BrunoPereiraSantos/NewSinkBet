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
package projects.defaultProject.nodes.timers;

import projects.Hop.nodes.nodeImplementations.NodeHop;
import projects.defaultProject.nodes.messages.EventMessage;
import Analises.InterfaceRequiredMethods;
import Analises.StatisticsNode;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.timers.Timer;

/**
 * A timer that sends a message at a given time.
 * The message may be unicast to a specific node or broadcast. 
 */
public class GenericMessageTimer extends Timer {
	private Node receiver; 
	private Message msg; 
	private boolean fwd;
	
	/**
	 * Creates a new MessageTimer object that unicasts a message to a given receiver when the timer fires.
	 * 
	 * Nothing happens when the message cannot be sent at the time when the timer fires.
	 *
	 * @param msg The message to be sent when this timer fires.
	 * @param receiver The receiver of the message.
	 */
	public GenericMessageTimer(Message msg, Node receiver, boolean fwd) {
		this.msg = msg;
		this.receiver = receiver;
		this.fwd = fwd;
	}
	
	/**
	 * Creates a MessageTimer object that broadcasts a message when the timer fires.
	 *
	 * @param msg The message to be sent when this timer fires.
	 * @param fwd se true o nodo deve somente encaminhar a mensagem,
	 * caso contrário o nodo deve atualizar as informacoes do pacote
	 */
	public GenericMessageTimer(Message msg, boolean fwd) {
		this.msg = msg;
		this.receiver = null; // indicates broadcasting
		this.fwd = fwd;
	}
	
	/**
	 * Creates a MessageTimer object that broadcasts a message when the timer fires.
	 *
	 * @param msg The message to be sent when this timer fires.
	 * @param fwd se true o nodo deve somente encaminhar a mensagem,
	 * caso contrário o nodo deve atualizar as informacoes do pacote
	 */
	public GenericMessageTimer(Message msg) {
		this.msg = msg;
		this.receiver = null; // indicates broadcasting
		this.fwd = false;
	}
	
	
	@Override
	public void fire() {
		
		if(receiver != null){
			((InterfaceRequiredMethods) this.node ).sendUnicastMsg(this.msg, this.receiver, fwd);
		}else{
			((InterfaceRequiredMethods) this.node).broadcastMsg(this.msg, fwd);
		}
		
		/*if(receiver != null) { // there's a receiver => unicast the message
			((InterfaceRequiredMethods) this.node ).sendUnicastMsg(this.msg, this.receiver);
		} else  if(this.msg instanceof EventMessage){ // there's no reciever => broadcast the message
			((InterfaceRequiredMethods) this.node).broadcastWithNack(this.msg);;
		}else{
			
		}*/
	}
}
