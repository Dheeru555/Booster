import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class DevineniKirtiProductServer{
	

public static void main(String args[])
{
try
{
int serverPort = 7896;
ServerSocket listenSocket = new ServerSocket(serverPort);
while(true)
{
Socket clientSocket = listenSocket.accept();
Connection c = new Connection(clientSocket);
}
}catch(IOException e){System.out.println("Listen:"+e.getMessage());}
}

//For concurrency using thread class
public static class Connection extends Thread
{
String uname = "";
String pwd = "";
DataInputStream in;
DataOutputStream out;
String portin = "";

Socket clientSocket;
public Connection(Socket aClientSocket)
{
try
{
clientSocket = aClientSocket;
in = new DataInputStream(clientSocket.getInputStream());
out = new DataOutputStream(clientSocket.getOutputStream());
this.start();
}
catch(IOException e)
{
System.out.println("Connection:" + e.getMessage());}
}

public void run()
{
	
Product[] p = new Product[5];	
for(int k=0; k<5; k++)
{
p[k] = null;
}
try
{
FileInputStream fileIn = new FileInputStream("products.ser");//Deserializing from products class
ObjectInputStream in = new ObjectInputStream(fileIn);
for(int k=0; k<5; k++)
{
p[k] = (Product)in.readObject();
}
in.close();
fileIn.close();
}
catch(IOException i)
{
i.printStackTrace();
return;
}
catch(ClassNotFoundException c)
{
System.out.println("Product class not found");
c.printStackTrace();
return;
}	
	
	
String points="";
try
{
File file = new File("Impdet.txt");//Server file that maintains username and password
while(true)
{
uname = in.readUTF();
pwd = in.readUTF();

Scanner scanner = new Scanner(file);
			String unam="",psswd="";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();				
				String[] lineArray = line.split(" ");
			    if(lineArray[0].equals(uname) && lineArray[1].equals(pwd))
				{
					unam=lineArray[0];
					psswd=lineArray[1];
				}
				
            }
			
if (unam.equals("") && psswd.equals(""))//validation
			{
				String data = "Wrong credentials entered";
				out.writeUTF(data);
			}
			else
			{
				System.out.println("User name at server side "+uname+" " + ",Password at server side "+psswd);
				String data = "Credentials Accepted";
				out.writeUTF(data);
				break;
			}

}
points = in.readUTF();
System.out.println("The points received are"+points);
Double point = Double.parseDouble(points);

System.out.println("Please enter the port number to connect");
			Scanner ss = new Scanner(System.in);
			String take = ss.nextLine();
//int porttt = Integer.parseInt(take);		
portin = take;
//Based on received points redirects to specific server and sent to the client
if(point >=1.0 && point <500.0)
{
	out.writeUTF(take);
}
else if(point >=500.0 && point <2000.0)
{
	out.writeUTF(take);
}
else
{
	out.writeUTF(take);
}
}
catch (EOFException e){System.out.println("EOF"+e.getMessage());}
catch (IOException e){System.out.println("IO"+ e.getMessage());}
finally
{
try
{
clientSocket.close();}
catch(IOException e)
{}}

Double point = Double.parseDouble(points);
//Code for Silver server
if(point >=1.0 && point <500.0)
{
System.out.println("Reached here in silver server");
int serverPort1 = Integer.parseInt(portin);
double discountPrice=0.0;
double pointCal=0.0;
String pointOff=" ";
byte[] client = new byte[1024];
DatagramSocket SilverSocket=null;
try
{
SilverSocket = new DatagramSocket(serverPort1);
}
catch(IOException e){System.out.println("IO"+ e.getMessage());}

try
{
while(true)
{
DatagramPacket req1 = new DatagramPacket(client,client.length);
SilverSocket.receive(req1);
System.out.println("Reply from client to the silver server:"+new String(req1.getData()));

String[][] prodArray = new String[5][2];
for(int y=0;y<5;y++)
{
for(int z=0;z<2;z++)
{
prodArray[y][z]=" ";
}
}

InetAddress aHost1 = InetAddress.getByName(null);

//int k=0;
for(int k=0;k<5;k++)
{
byte[] v1 = String.valueOf(p[k].pid).getBytes();
byte[] v2 = (p[k].name).getBytes();
byte[] v3 = String.valueOf(p[k].qty).getBytes();
byte[] v4 = String.valueOf(p[k].price).getBytes();

DatagramPacket r1 = new DatagramPacket(v1,v1.length,req1.getAddress(),req1.getPort());
DatagramPacket r2 = new DatagramPacket(v2,v2.length,req1.getAddress(),req1.getPort());
DatagramPacket r3 = new DatagramPacket(v3,v3.length,req1.getAddress(),req1.getPort());
DatagramPacket r4 = new DatagramPacket(v4,v4.length,req1.getAddress(),req1.getPort());

//String pide = String.valueOf(p[k].pid);
//System.out.println(pide);
SilverSocket.send(r1);
prodArray[k][0]=String.valueOf(p[k].pid);
SilverSocket.send(r2);
SilverSocket.send(r3);
SilverSocket.send(r4);
prodArray[k][1]=String.valueOf(p[k].price);

}
byte[] cl = new byte[1024];
DatagramPacket prd = new DatagramPacket(cl,cl.length);
SilverSocket.receive(prd);
System.out.println("Selected product from the client to silver server is: "+new String(prd.getData()));

String prodsel = new String(prd.getData());

String pk = prodsel.trim();




//Points calculation part
for(int h=0;h<5;h++)
{
	
	if(pk.equals(prodArray[h][0]))
	{
		Double temp = Double.parseDouble(prodArray[h][1]);
		discountPrice = temp*0.9;
		
		pointCal = discountPrice/50;
		
		pointOff = String.valueOf(pointCal);
		
		System.out.println("Given price from client to silver server: "+ temp);
		System.out.println("Discounted pricefrom client to silver server: "+ discountPrice);
		System.out.println("Points gained from client to silver server: "+ pointCal);
		
		
	}
}

byte[] poin = pointOff.getBytes();
DatagramPacket pion = new DatagramPacket(poin,poin.length,req1.getAddress(),req1.getPort());
SilverSocket.send(pion);

//out.writeUTF(pointOff);
}
}
catch (SocketException e){System.out.println("Socket"+e.getMessage());}
catch (IOException e){System.out.println("IO"+ e.getMessage());}
catch(Exception e){System.out.println("IO"+ e.getMessage());}
finally
{
	if(SilverSocket!=null) SilverSocket.close();
}
}//Code for gold server
else if(point >=500.0 && point <=2000.0)
{	
System.out.println("Reached here in gold server");
int serverPort1 = Integer.parseInt(portin);
double discountPrice=0.0;
double pointCal=0.0;
String pointOff=" ";
byte[] client = new byte[1024];
DatagramSocket SilverSocket=null;
try
{
SilverSocket = new DatagramSocket(serverPort1);
}
catch(IOException e){System.out.println("IO"+ e.getMessage());}

try
{
while(true)
{
DatagramPacket req1 = new DatagramPacket(client,client.length);
SilverSocket.receive(req1);
System.out.println("Reply from client to gold server:"+new String(req1.getData()));

String[][] prodArray = new String[5][2];
for(int y=0;y<5;y++)
{
for(int z=0;z<2;z++)
{
prodArray[y][z]=" ";
}
}

InetAddress aHost1 = InetAddress.getByName(null);

//int k=0;
for(int k=0;k<5;k++)
{
byte[] v1 = String.valueOf(p[k].pid).getBytes();
byte[] v2 = (p[k].name).getBytes();
byte[] v3 = String.valueOf(p[k].qty).getBytes();
byte[] v4 = String.valueOf(p[k].price).getBytes();

DatagramPacket r1 = new DatagramPacket(v1,v1.length,req1.getAddress(),req1.getPort());
DatagramPacket r2 = new DatagramPacket(v2,v2.length,req1.getAddress(),req1.getPort());
DatagramPacket r3 = new DatagramPacket(v3,v3.length,req1.getAddress(),req1.getPort());
DatagramPacket r4 = new DatagramPacket(v4,v4.length,req1.getAddress(),req1.getPort());

//String pide = String.valueOf(p[k].pid);
//System.out.println(pide);
SilverSocket.send(r1);

prodArray[k][0]=String.valueOf(p[k].pid);
SilverSocket.send(r2);
SilverSocket.send(r3);
SilverSocket.send(r4);
prodArray[k][1]=String.valueOf(p[k].price);

}
byte[] cl = new byte[1024];
DatagramPacket prd = new DatagramPacket(cl,cl.length);
SilverSocket.receive(prd);

System.out.println("Selected product from the client to gold server is: "+new String(prd.getData()));

String prodsel = new String(prd.getData());


String pk = prodsel.trim();

for(int h=0;h<5;h++)
{
	
	if(pk.equals(prodArray[h][0]))
	{
		Double temp = Double.parseDouble(prodArray[h][1]);
		discountPrice = temp*0.85;
		
		pointCal = discountPrice/50;
		
		pointOff = String.valueOf(pointCal);
		
		System.out.println("Given price from client to gold server: "+ temp);
		System.out.println("Discounted pricefrom client to gold server: "+ discountPrice);
		System.out.println("Points gained from client to gold server: "+ pointCal);
		
	}
}

byte[] poin = pointOff.getBytes();
DatagramPacket pion = new DatagramPacket(poin,poin.length,req1.getAddress(),req1.getPort());
SilverSocket.send(pion);

//out.writeUTF(pointOff);
}
}
catch (SocketException e){System.out.println("Socket"+e.getMessage());}
catch (IOException e){System.out.println("IO"+ e.getMessage());}
catch(Exception e){System.out.println("IO"+ e.getMessage());}
finally
{
	if(SilverSocket!=null) SilverSocket.close();
}

}
else//Code for platinum server
{
System.out.println("Reached here in platinum server");
int serverPort1 = Integer.parseInt(portin);
double discountPrice=0.0;
double pointCal=0.0;
String pointOff=" ";
byte[] client = new byte[1024];
DatagramSocket SilverSocket=null;
try
{
SilverSocket = new DatagramSocket(serverPort1);
}
catch(IOException e){System.out.println("IO"+ e.getMessage());}

try
{
while(true)
{
DatagramPacket req1 = new DatagramPacket(client,client.length);
SilverSocket.receive(req1);
System.out.println("Reply from client to platinum server:"+new String(req1.getData()));

String[][] prodArray = new String[5][2];
for(int y=0;y<5;y++)
{
for(int z=0;z<2;z++)
{
prodArray[y][z]=" ";
}
}

InetAddress aHost1 = InetAddress.getByName(null);

//int k=0;
for(int k=0;k<5;k++)
{
byte[] v1 = String.valueOf(p[k].pid).getBytes();
byte[] v2 = (p[k].name).getBytes();
byte[] v3 = String.valueOf(p[k].qty).getBytes();
byte[] v4 = String.valueOf(p[k].price).getBytes();

DatagramPacket r1 = new DatagramPacket(v1,v1.length,req1.getAddress(),req1.getPort());
DatagramPacket r2 = new DatagramPacket(v2,v2.length,req1.getAddress(),req1.getPort());
DatagramPacket r3 = new DatagramPacket(v3,v3.length,req1.getAddress(),req1.getPort());
DatagramPacket r4 = new DatagramPacket(v4,v4.length,req1.getAddress(),req1.getPort());

//String pide = String.valueOf(p[k].pid);
//System.out.println(pide);
SilverSocket.send(r1);

prodArray[k][0]=String.valueOf(p[k].pid);
SilverSocket.send(r2);
SilverSocket.send(r3);
SilverSocket.send(r4);
prodArray[k][1]=String.valueOf(p[k].price);

}
byte[] cl = new byte[1024];
DatagramPacket prd = new DatagramPacket(cl,cl.length);
SilverSocket.receive(prd);
System.out.println("Selected product from the client to platinum server is: "+new String(prd.getData()));

String prodsel = new String(prd.getData());


String pk = prodsel.trim();
//Points calculation
for(int h=0;h<5;h++)
{
	
	if(pk.equals(prodArray[h][0]))
	{
		
		Double temp = Double.parseDouble(prodArray[h][1]);
		System.out.println("Given price from client to platinum server: "+ temp);
		discountPrice = temp*0.8;
		System.out.println("Discounted price from client to platinum server: "+ discountPrice);
		
		pointCal = discountPrice/50;
		System.out.println("Points gained from client to platinum server: "+ pointCal);
		
		
		pointOff = String.valueOf(pointCal);
		
	}
}

byte[] poin = pointOff.getBytes();
DatagramPacket pion = new DatagramPacket(poin,poin.length,req1.getAddress(),req1.getPort());
SilverSocket.send(pion);

//out.writeUTF(pointOff);
}
}
catch (SocketException e){System.out.println("Socket"+e.getMessage());}
catch (IOException e){System.out.println("IO"+ e.getMessage());}
catch(Exception e){System.out.println("IO"+ e.getMessage());}
finally
{
	if(SilverSocket!=null) SilverSocket.close();
}

}

}
}
}

