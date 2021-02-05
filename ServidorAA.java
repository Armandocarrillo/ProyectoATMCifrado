// se ejecuta en terminal asi: java ServidorAA 8080
import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import java.util.Scanner;

public class ServidorAA
{
public static void main(String a[]) throws Exception
{ 
 ServerSocket serverSocket = null;
 Socket socket = null;
 String peticion = null;
 String peticionInicial = null;
 String respuesta = null;
 Integer contador = 0;
String bienvenida = "Bievenido a ATM\n\tPresiona una opcion:\nCONSULTAR\nDEPOSITAR\nRETIRAR\n";



if (a.length > 0){ //inicio if 1
	String valorPuerto = a[0];

File f = new File("llave.ser");
	if(!f.exists()) { 
	
		System.out.println( "Generando la llave..." );
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(56);
		Key newKey = keyGen.generateKey();
		System.out.println( "llave=" + newKey );
		System.out.println( "Llave generada!" );

		ObjectOutput out = new ObjectOutputStream(new FileOutputStream("llave.ser"));
		out.writeObject( newKey );
		out.close();
	}
	
     ObjectInput in = new ObjectInputStream(new FileInputStream("llave.ser"));
	 Key llave = (Key)in.readObject();
	 System.out.println( "llave=" + llave );
	 in.close();



	try
	{// inicio try 1
	 System.out.println("Escuchando por el puerto" + valorPuerto);
 	int cambioStringAInt = Integer.valueOf(valorPuerto);
	serverSocket = new ServerSocket(cambioStringAInt);
	}// fin try 1
	catch(IOException e)
	{
	 System.out.println("java.io.IOException generada");
	 e.printStackTrace();
	}

	System.out.println("Esperando a que los clientes se conecten...");
while(true)
	{ //inicio  while 1
	 try
	 { //inicio 2 try
		socket = serverSocket.accept();
		System.out.println("Se conecto un cliente: " + socket.getInetAddress().getHostName());
		DataInputStream dis = new DataInputStream( socket.getInputStream() );
		DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
		
		///
		peticionInicial = bienvenida;
		 System.out.println( "Mi peticion es: " + peticionInicial );
		 System.out.println( "Ahora encriptamos la peticion..." );
		 byte[] arrayPeticionInicial = peticionInicial.getBytes();
		 Cipher cifrarInicial = Cipher.getInstance("DES");
		 cifrarInicial.init(Cipher.ENCRYPT_MODE, llave);
		 byte[] cipherTextInicial = cifrarInicial.doFinal( arrayPeticionInicial );
		 bytesToBits( cipherTextInicial );
		 
		 dos.write( cipherTextInicial, 0, cipherTextInicial.length );
		


		 while(true){//inicio while 2

		 byte arreglo[] = new byte[10000];
	 	 BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
		 System.out.println("\n\nEscribe un mensaje para el Cliente");
		 peticion = "depositame";//br.readLine();
		 System.out.println( "Mi peticion es: " + peticion );
		 System.out.println( "Ahora encriptamos la peticion..." );
		 byte[] arrayPeticion = peticion.getBytes();
		 Cipher cifrar = Cipher.getInstance("DES");
		 cifrar.init(Cipher.ENCRYPT_MODE, llave);
		 byte[] cipherText = cifrar.doFinal( arrayPeticion );
		 bytesToBits( cipherText );
		 
		 dos.write( cipherText, 0, cipherText.length );
		 
		 if (peticion.equals("SALIR")){
		 	break;
		 }
		 
		int bytesLeidos = dis.read(arreglo);
		
		byte arreglo2[]	= new byte[bytesLeidos];
		for(int i=0; i < bytesLeidos; i++ ){
			arreglo2[i] = arreglo[i];
		}
		
		Cipher decifrar = Cipher.getInstance("DES");
		decifrar.init(Cipher.DECRYPT_MODE, llave);
		bytesToBits( arreglo2 );
		byte[] newPlainText = decifrar.doFinal(arreglo2);
		System.out.println( "\n\nMensaje recibido del cliente :" );
		String messageReceivedInPlainText = new String(newPlainText, "UTF8") ;
		System.out.println( new String(newPlainText, "UTF8"));
		
		if (messageReceivedInPlainText.equals("SALIR")){
			System.out.println("\n\n El servidor ha cerrado la conexion");
		 	break;
		} else if (messageReceivedInPlainText.equals("DEPOSITAR")){
			System.out.println("\n\n El cliente esta depositando");

		 	continue;
		} else if (messageReceivedInPlainText.equals("CONSULTAR")){
			System.out.println("\n\n El cliente esta consultando");
		 	continue;
		} else if (messageReceivedInPlainText.equals("RETIRAR")){
			System.out.println("\n\n El cliente esta retirando");
		 	continue;
		}

		 	
	 	
	 }// fin while 2

		dos.close();
		dis.close();
		socket.close();
	 } //fin 2 try
	 catch(IOException e)
	 {
		System.out.println("java.io.IOException generada");
		e.printStackTrace();
	 }
	}// fin while 1

 


} else {  // cierre del if 1 e inicio del else 1
	System.out.println("No ha pasado el puerto");
} // cierre else 1


} //cierre main

public static void bytesToBits( byte[] texto )
	{ // static bytesToBits
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
	} // Fin static bytesToBits


} // cierre class