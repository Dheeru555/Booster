import java.io.*;

public class SerialProduct
{
public static void main(String[] args)
{		
Product[] p = new Product[5];	
for(int k=0; k<5; k++)
{
p[k] = new Product();
}
p[0].pid=1001;
p[0].name="Sony";
p[0].qty=1;
p[0].price=700;

p[1].pid=1002;
p[1].name="Skull";
p[1].qty=2;
p[1].price=150;

p[2].pid=1003;
p[2].name="Samsung";
p[2].qty=1;
p[2].price=500;

p[3].pid=1004;
p[3].name="Iphone";
p[3].qty=2;
p[3].price=900;

p[4].pid=1005;
p[4].name="MI";
p[4].qty=1;
p[4].price=800;

try
{
FileOutputStream fileOut = new FileOutputStream("products.ser");
ObjectOutputStream out = new ObjectOutputStream(fileOut);
for(int k=0; k<5; k++)
{
out.writeObject(p[k]);
}
out.close();
fileOut.close();
System.out.printf("Serialized data is saved in /Products.ser");
}
catch(IOException i)
{
i.printStackTrace();
}
}
}

