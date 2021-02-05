
// se ejecuta en terminal asi; java ClienteAA localhost 8080

import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;


public class ClienteAA
{
public static void main(String a[]) throws Exception
{
 Socket socket = null;
 String peticion = null;
 String respuesta = null;

 byte arreglo[] = new byte[10000];

if (a.length > 0){
	String valorPuerto = a[1];

	try
	{// inicio try 1
	 										
	 ObjectInput in = new ObjectInputStream(new FileInputStream("llave.ser"));
	 Key llave = (Key)in.readObject();
	 System.out.println( "llave=" + llave );
	 in.close();
	 System.out.println("Me conecto al puerto del servidor "+ valorPuerto);
 	 int cambioStringAInt = Integer.valueOf(valorPuerto);

 	socket = new Socket(a[0],cambioStringAInt);
	 DataInputStream dis = new DataInputStream( socket.getInputStream() );
	 DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );



	 //while(true){ //inicio while 2

		
			int bytesLeidos = dis.read(arreglo);
			byte arreglo2[]	= new byte[bytesLeidos];
			for(int i=0; i < bytesLeidos; i++ )
			{
			 arreglo2[i] = arreglo[i];
			}

			Cipher decifrar = Cipher.getInstance("DES");
			decifrar.init(Cipher.DECRYPT_MODE, llave);
			bytesToBits( arreglo2 );
			byte[] newPlainText = decifrar.doFinal(arreglo2);
			System.out.println( "\n\nMensaje recibido del servidor :" );
			String plainTextReceived = new String(newPlainText, "UTF8");
			System.out.println( new String(newPlainText, "UTF8") );
			if (plainTextReceived.equals("SALIR")){
				System.out.println("\n\n Cerrando Conexión");
		 		break;
			 }
			System.out.println( "\n\n Escribe un mensaje para el servidor:" );


			BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
			peticion = br.readLine();
			 System.out.println( "Ahora encriptamos el mensaje para el servidor..." );
			 byte[] arrayPeticion = peticion.getBytes();
			 Cipher cifrar = Cipher.getInstance("DES");
			 cifrar.init(Cipher.ENCRYPT_MODE, llave);
			 byte[] cipherText = cifrar.doFinal( arrayPeticion );
			 bytesToBits( cipherText );
			 dos.write( cipherText, 0, cipherText.length );
			 if (peticion.equals("SALIR")){
			 	System.out.println("\n\nCerrando conexion con el servidor");
		 		break;
			 }

		//} //cierre while 2

	 
	 dos.close();
	 dis.close();
	 socket.close();
	}
	catch(IOException e)
	{
		
	 System.out.println("java.io.IOException generada");
	 e.printStackTrace();
	}





} else {//cierre if puerto
	System.out.println("No ha pasado el puerto");

} //cierre else
 
} //cierre main

public static void bytesToBits( byte[] texto )
	{ // inicio bytes
		StringBuilder stringToBits = new StringBuilder();
		for( int i=0; i < texto.length; i++ )
		{
			StringBuilder binary = new StringBuilder();
			byte b = texto[i];
			int val = b;
			for( int j = 0; j < 8; j++ )
			{
				binary.append( (val & 128) == 0 ? 0 : 1 );
				val <<= 1;
			}
			System.out.println( (char)b + " \t " + b + " \t " + binary );
			stringToBits.append( binary );
		}
		System.out.println( "El mensaje completo en bits es:" + stringToBits );
	} //cierre bytes
	
}// cierre class