import java.net.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class DevineniKirtiProductClient{
public static void main(String args[])
{
Socket s = null;
String portdata1="";
String uname="",pwd="",points="";
try
{
InetAddress address = InetAddress.getByName(null);
int serverPort = 7896;//Connecting to fixed port
s=new Socket(address,serverPort);

DataInputStream in = new DataInputStream(s.getInputStream());
DataOutputStream out = new DataOutputStream(s.getOutputStream());



File file = new File("Samp.txt");
//Username validation part will be done here from client side
        while(true)
		{
			System.out.println("Please enter your user name");
			Scanner ss = new Scanner(System.in);
			String take = ss.nextLine();
		try {
            //
            // Create a new Scanner object which will read the data from the 
            // file passed in. To check if there are more line to read from it
            // we check by calling the scanner.hasNextLine() method. We then
            // read line one by one till all line is read.
            //
            Scanner scanner = new Scanner(file);
			
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();				
				String[] lineArray = line.split(" ");
			    if(lineArray[0].equals(take))
				{
					uname=lineArray[0];
					pwd=lineArray[1];
					points=lineArray[2];
				}
				
            }
			if (uname.equals(""))
			{
				System.out.println("Please enter correct uname");
			}
			else
			{
				System.out.println("User name "+uname+" " + "Password is "+pwd);
				break;
			}
			
			
			
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		}

out.writeUTF(uname);
out.writeUTF(pwd);

//Username validation part will be done here from client side after getting authentication error from the server side
while(true)
{
String data = in.readUTF();
if(data.equals("Wrong credentials entered"))
{
System.out.println("Received from server"+ data);
 while(true)
		{
			System.out.println("Please enter your user name");
			Scanner ss = new Scanner(System.in);
			String take = ss.nextLine();
		try {
            //
            // Create a new Scanner object which will read the data from the 
            // file passed in. To check if there are more line to read from it
            // we check by calling the scanner.hasNextLine() method. We then
            // read line one by one till all line is read.
            //
            Scanner scanner = new Scanner(file);
			
				while (scanner.hasNextLine()) {
                String line = scanner.nextLine();				
				String[] lineArray = line.split(" ");
			    if(lineArray[0].equals(take))
				{
					uname=lineArray[0];
					pwd=lineArray[1];
					points=lineArray[2];
				}
				
            }
			if (uname.equals(""))
			{
				System.out.println("Please enter correct password");
			}
			else
			{
				System.out.println("User name "+uname+" " + "Password is "+pwd);
				break;
			}
			
			
			
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		}

out.writeUTF(uname);
out.writeUTF(pwd);

}	
else
{
	System.out.println("Received:"+data + ", pls turn to the server window and provide the port number");
	break;
}
}

out.writeUTF(points);
portdata1 = in.readUTF();
System.out.println("Received port data is: "+ portdata1);

Double point = Double.parseDouble(points);
if(point >=1.0 && point <500.0)
{
	System.out.println("Connected to silver server");
}
else if(point >=500.0 && point <2000.0)
{
	System.out.println("Connected to gold server");
}
else
{
	System.out.println("Connected to platinum server");
}


}
catch (UnknownHostException e)
{
System.out.println("Sock"+e.getMessage());
}
catch (EOFException e){System.out.println("EOF"+e.getMessage());}
catch(IOException e){System.out.println("IO"+ e.getMessage());}
finally
{
if(s != null)
{ try {
	s.close();}
catch(IOException e){System.out.println("IO"+ e.getMessage());}
}
}


//Creating the products bytes
DatagramSocket normal = null;
byte[] p1 = new byte[1024];
byte[] p2 = new byte[1024];
byte[] p3 = new byte[1024];
byte[] p4 = new byte[1024];
try{
normal = new DatagramSocket();
byte[] test = "To the new server".getBytes();
InetAddress aHost = InetAddress.getByName(null);

DatagramPacket req1 = new DatagramPacket(test,test.length,aHost,Integer.parseInt(portdata1));
normal.send(req1);

System.out.println("Products available that are sent from server:");

String pidee[] = new String[5];
//Sending the five instances
for(int k=0;k<5;k++) 
{
DatagramPacket rr1 = new DatagramPacket(p1,p1.length);
normal.receive(rr1);
DatagramPacket rr2 = new DatagramPacket(p2,p2.length);
normal.receive(rr2);
DatagramPacket rr3 = new DatagramPacket(p3,p3.length);
normal.receive(rr3);
DatagramPacket rr4 = new DatagramPacket(p4,p4.length);
normal.receive(rr4);

System.out.print("ID "+new String(rr1.getData()).trim());
System.out.print(" Name "+new String(rr2.getData()).trim());
System.out.print(" Qty "+new String(rr3.getData()).trim());
System.out.println(" Price "+new String(rr4.getData()).trim());
pidee[k]=new String(rr1.getData()).trim();

}



String taker;
System.out.println("Please enter the product id from the above list");
int temper = 1;
//Checking if the entered id is valid or not
while(true)
{
	
Scanner tk = new Scanner(System.in);
taker = tk.nextLine();

	for(int d=0;d<5;d++)
	{
		if(pidee[d].equals(taker))
		{
			temper = 0;
		}
	}
	
	if(temper == 0)
	{
		break;
	}
	else
	{
	System.out.println("Please enter the correct product id");	
	}
}

byte[] t11 = taker.getBytes();
//InetAddress aHost = InetAddress.getByName(null);
//Sending the product id that client wants to buy to server
DatagramPacket rpl = new DatagramPacket(t11,t11.length,aHost,Integer.parseInt(portdata1));
normal.send(rpl);


//out.writeUTF(taker);

byte[] rec = new byte[1024];
DatagramPacket rece = new DatagramPacket(rec,rec.length);
normal.receive(rece);
System.out.println("Received points are:"+ new String(rece.getData()));

String forCal = new String(rece.getData()).trim();

//////////File updation is doing here
        File fileToBeModified = new File("Samp.txt");
         
        String oldContent = "";
		
		Double temp = Double.parseDouble(forCal) + Double.parseDouble(points);
		String newString = String.valueOf(temp);
         
        BufferedReader reader = null;
         
        FileWriter writer = null;
         
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
             
            //Reading all the lines of input text file into oldContent
             
            String line = reader.readLine();
             
            while (line != null) 
            {
                String[] lineArray1 = line.split(" ");
				if(lineArray1[0].equals(uname))
				{
					System.out.println("File update is under processing and updated");
				}
				else
				{
				oldContent = oldContent + line + System.lineSeparator();
				}
                line = reader.readLine();
            }
             
            //Replacing oldString with newString in the oldContent
             
            String newContent = oldContent + uname + " " + pwd + " "+ newString;
             
            //Rewriting the input text file with newContent
             
            writer = new FileWriter(fileToBeModified);
             
            writer.write(newContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources
                 
                reader.close();
                 
                writer.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }







//String PointOff = in.readUTF();
//System.out.println("Received points are:"+ PointOff);


//clientSocket1.close();
}
catch (SocketException e){System.out.println("Socket"+e.getMessage());}
catch (IOException e){System.out.println("IO"+ e.getMessage());}
finally
{
	if(normal!=null) normal.close();
}
}	
}
