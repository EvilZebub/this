package anothor;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private Socket s;
    private Socket ts;
    private Socket ns;
    private BufferedReader br;
    private PrintWriter out;
    private boolean flag = true;
    private InputStream tIn = null;
    private OutputStream nOut = null;
    private String fileName = null;
    
    
    public static void main(String[] args) {
    	new Client().startUp();
    }
   
    private void startUp() {
		BufferedReader sbr = null;
		try {
//		    Scanner scanner = new Scanner(System.in);
		    sbr = new BufferedReader(new InputStreamReader(System.in));
		    System.out.print("Type your NAME: ");
		    String name = sbr.readLine();
		    System.out.println("----------------------------===Chat Room Started===---------------------------");
		    System.out.println("------------=====Type \"quit\" to disconnect from the Server=====-------------");
		    System.out.println("-=Type \"Get\" ,and the filename on the next line to start the download process=-");
		    System.out.println();
		    System.out.println();
		    
		    s = new Socket("127.0.0.1", 9999);
		    out = new PrintWriter(s.getOutputStream(), true);
	//	    nOut.flush();
		    br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		    out.println(name);
		    new Thread(new ClientThread()).start();
		    String str = null;
		    while(flag && (str=sbr.readLine()) != null) {
				if(!flag) break;
				if(str.trim().equalsIgnoreCase("Get")) {
					ts = new Socket("127.0.0.1", 8888);
					ns = new Socket("127.0.0.1", 7777);
					System.out.println("Type the file name you wannna download");
					tIn = ts.getInputStream();
					nOut = ns.getOutputStream();
					fileName = sbr.readLine().trim();
					nOut.write(fileName.getBytes());
					byte[] fileBytes = new byte[100];
					System.out.println("1");
					int len = 0;
					System.out.println("2");
					OutputStream fileOut = new FileOutputStream("/Users/dennislee/"+fileName);
					System.out.println("3");
					while((len=tIn.read(fileBytes)) != -1) {
						fileOut.write(fileBytes, 0, len);
					}
					System.out.println("finished");
				}
				out.println(str);
		    }
		} catch (UnknownHostException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		    	if(s != null) s.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		    try {
		    	if(sbr != null) s.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
    }

    private void receive() {
		try {
		    String rs = br.readLine();
		    if(rs.equalsIgnoreCase("disconnect")) {
		    	flag = false;
		    	System.out.println("PRESS <ENTER> TO EXIT");
		    }
		    System.out.println(rs);
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }

    private class ClientThread implements Runnable {
	
		@Override
		public void run() {
		    while(true) {
				if(!flag) break;
					receive();
		    }
		}
    }
}

