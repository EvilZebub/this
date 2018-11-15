package anothor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class downloadServer {
	public static void main(String[] args) throws IOException {
		ServerSocket ts = new ServerSocket(8888);
	    ServerSocket ns = new ServerSocket(7777);
	    while(true) {
			Socket t = ts.accept();
			Socket n=ns.accept();
			new Thread(new TransferServerThread(t, n)).start();
	    }
	}
	private static class TransferServerThread implements Runnable {
	    private Socket t = null;
	   	private Socket n = null;
	    private InputStream in = null;
	    private InputStream nIn = null;
	    private OutputStream out = null;
//	    private OutputStream nOut = null;
	    private String str =null;
	    	
	    public TransferServerThread(Socket socket,Socket nSocket) throws IOException {
	    	this.t = socket;
	    	this.n = nSocket;
	    		
		}
		@Override
		public void run() {
			try {
				in = t.getInputStream();
		   		out = t.getOutputStream();
		   		nIn = n.getInputStream();
//		   		nOut = nSocket.getOutputStream();
		   		byte[] bs = new byte[100];
		   		nIn.read(bs);
		   		str = new String(bs).trim();
				out.flush();
				File file = new File("/Users/dennislee/Downloads/software/"+str);
				InputStream fileIn = new FileInputStream(file);
				byte[] fileBytes = new byte[100];
				int len = 0;
				while((len=fileIn.read(fileBytes)) != -1) {
					out.write(fileBytes, 0, len);
					System.out.println(len);
				}
//				out.flush();
				fileIn.close();
				out.close();
				in.close();
				nIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


