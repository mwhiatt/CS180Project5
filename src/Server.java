import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 * Project 5 - Learning Management Quiz Tool - Server
 * Server class to receive connections from multiple users and store information. 
 * <p>
 * 
 * @author Matt Hiatt, Aryan Mathur, Aniket Mohanty, and Nathan Lo
 * @version 11/15/2021
 */
public class Server implements Runnable {
	Socket socket;
	public Server(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			Scanner input = new Scanner(socket.getInputStream());
			//handling
			
			pw.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(4343);
		
		while (true) {
			Socket socket = serverSocket.accept();
			Server server = new Server(socket);
			new Thread(server).start();
		}
		

	}

}


/*
 * first try, saving in case better way is too hard
public class Server {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(4242);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true) {
			Socket socket = null;
			try {
				String message = "";
				socket = serverSocket.accept();
				BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw = new PrintWriter(socket.getOutputStream());
				String line = bfr.readLine();
				while (line != null) {
					message += line;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
 */
