package anothor;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private List<ServerThread> clients = null;
    
    public static void main(String[] args) {
    	new Server().startUp();
    }

    private void startUp() {
		ServerSocket ss = null;
		Socket s = null;
		try {
		    ss = new ServerSocket(9999);
		    clients = new ArrayList<ServerThread>();
		    while(true) {
		    	s = ss.accept();
		    	ServerThread st = new ServerThread(s);
		    		new Thread(st).start();
		    	}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
			    if(ss != null)
				ss.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
    }

    private class ServerThread implements Runnable {
		private Socket s = null;
		private BufferedReader br;
		private PrintWriter out;
		private String name;
		private boolean flag = true;
	
		public ServerThread(Socket socket) throws IOException {
		    Date date = new Date();
		    this.s = socket;
		    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    out = new PrintWriter(socket.getOutputStream(), true);
		    String str = br.readLine();
		    name = str+" :["+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"]";
		    clients.add(this);
		    send(name+"is ONLINE! now");
		    System.out.println(name+"Login on "+date);
		}
	
		private void send(String msg) {
			Date date = new Date();
		    for(ServerThread st : clients)
			st.out.println(msg+"  ("+date+").");
		}
	
		private void receive() throws IOException {
		    String str = null;
		    while((str=br.readLine()) != null) {
				if(str.equalsIgnoreCase("quit")) {
				    stop();
				    out.println("disconnect");
				    break;
				}
			send(name+": "+str+"wWw");
			System.out.println(name+": "+str);
		    }
		}
	
		private void stop() {
		    Date date = new Date();
		    clients.remove(this);
		    flag = false;
		    send(name+"OFFLINE on"+ date);
		    System.out.println(name+"OFFLINE on" + date);
		}
	
		@Override
		public void run() {
		    try {
				while(true) {
				    if(!flag) break;
				    receive();
				}
		    } catch (SocketException e) {
		    	stop();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    } finally {
				try {
				    if(s != null)
					s.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		}
    }
}
//    private class TransferServerThread implements Runnable {
//    	private Socket t = null;
//    	private Socket n = null;
//    	private InputStream in = null;
//    	private InputStream nIn = null;
//    	private OutputStream out = null;
//    	//private OutputStream nOut = null;
//    	private String str =null;
//    	
//    	public TransferServerThread(Socket socket,Socket nSocket) throws IOException {
//    		this.t = socket;
//    		this.n = nSocket;
//    		in = socket.getInputStream();
//    		out = socket.getOutputStream();
//    		nIn = nSocket.getInputStream();
//    		//nOut = nSocket.getOutputStream();
//    		byte[] bs = new byte[100];
//    		nIn.read(bs);
//    		str = new String(bs).trim();
//		}
//		@Override
//		public void run() {
//			try {
//				out.flush();
//				File file = new File("/Users/dennislee/Downloads/software/"+str);
//				InputStream fileIn = new FileInputStream(file);
//				byte[] fileBytes = new byte[1024];
//				int len = -1;
//				while((len=fileIn.read(fileBytes)) != -1) {
//					out.write(fileBytes, 0, len);
//				}
//				out.flush();
//				out.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//		}
//	
//    }
//}

