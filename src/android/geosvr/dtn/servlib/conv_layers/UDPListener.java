/*
 *	  This file is part of the Bytewalla Project
 *    More information can be found at "http://www.tslab.ssvl.kth.se/csd/projects/092106/".
 *    
 *    Copyright 2009 Telecommunication Systems Laboratory (TSLab), Royal Institute of Technology, Sweden.
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *    
 */

package android.geosvr.dtn.servlib.conv_layers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;

import android.geosvr.dtn.DTNService;
import android.geosvr.dtn.servlib.conv_layers.TCPConvergenceLayer.TCPLinkParams;
import android.geosvr.dtn.servlib.conv_layers.UDPConvergenceLayer.UDPLinkParams;
import android.geosvr.dtn.servlib.discovery.Discovery;
import android.geosvr.dtn.servlib.discovery.DiscoveryTable;
import android.geosvr.dtn.systemlib.util.BufferHelper;
import android.geosvr.dtn.systemlib.util.IByteBuffer;
import android.util.Log;

/**
 * "Helper class (and thread) that listens on a registered interface for new
 * connections" [DTN2].
 * 
 * @author Mar�a Jos� Peroza Marval (mjpm@kth.se)
 */

public class UDPListener extends CLInfo implements Runnable {

	/**
	 * Internal Thread
	 */
	private Thread thread_;

	/**
	 * ServerSocket instance
	 */
	private DatagramSocket server_socket_;

	/**
	 * TAG for Android Logging mechanism
	 */
	private static final String TAG = "UDPListener";

	/**
	 * Unique identifier according to Java Serializable specification
	 */
	private static final long serialVersionUID = -6949424199702356461L;

	/**
	 * Constructor
	 */
	public UDPListener(ConvergenceLayer convergenceLayer, int port) {

		cl_ = (UDPConvergenceLayer) convergenceLayer;

		try {

			server_socket_ = new DatagramSocket(port);
			server_socket_.setSoTimeout(50);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "IOException " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Check is the socket is bound
	 */
	public boolean isBound() {
		return server_socket_.isBound();
	}

	/**
	 * Start the UDPListener thread
	 */
	public void start() {
		thread_ = new Thread(this);
		listening_ = true;
		thread_.start();

	}

	/**
	 * Stop the UDPListener thread
	 */
	public void stop() {
		listening_ = false;
		thread_ = null;
		try {
			server_socket_.close();
		} catch (Exception e) {
			Log.d(TAG, "Exception stopping server_socket: " + e.getMessage());
		}
		server_socket_ = null;
	}

	UDPConvergenceLayer cl_; // / The UDPCL instance
//	DatagramSocket socket; // / The socket

	private boolean listening_ = false; // / Listening flag

	
	/**
	 * Main loop
	 */
	public void run() {
		UDPConnection udpconnection;

		try {
			UDPLinkParams tlp = cl_.new UDPLinkParams(true);
//			tlp.remote_addr_ = socket.getInetAddress();
			udpconnection = new UDPConnection(cl_, tlp);
			udpconnection.set_socket(server_socket_);
			udpconnection.recive_data();

		} catch (OutOfMemoryError e) {
			Log.d(TAG, "Not enough resources");
		}
//		DiscoveryTable disTable = DiscoveryTable.getInstance();
//		Iterator<Discovery> iter = disTable.getDiscoveryList().iterator();
//		Discovery disc = disTable.find("ipdisc0", iter);
		

//		while (!(disc.discoveries().isEmpty())) {
//		while (!(Discovery.discoveries().isEmpty())) {
//			int timeout = 10;
//			handle_poll_activity(timeout);
//			UDPConnection udpconnection;
//
//			try {
//				UDPLinkParams tlp = cl_.new UDPLinkParams(true);
//				//tlp.remote_addr_ = socket.getInetAddress();
//				udpconnection = new UDPConnection(cl_, tlp);
//				udpconnection.set_socket(socket);
//				udpconnection.start();
//
//			} catch (OutOfMemoryError e) {
//				Log.d(TAG, "Not enough resources");
//			}
//
//		}//end while
	}//end run()
	

}
