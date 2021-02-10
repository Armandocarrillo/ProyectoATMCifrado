// se ejecuta en terminal asi; java ClienteAA localhost 8080

 import java.net.*;
 import java.io.*;
 import java.security.*;
 import javax.crypto.*;
import java.util.Scanner;
/*se ingresaron las librerias*/
 public class ClienteAA /*Se crea la clase ClienteAA*/
 {
 public static void main(String a[]) throws Exception/*creamos la funcion principal*/
 {
 	BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
 //	Scanner sc = new Scanner(System.in);  
  Socket socket = null;/*Creamos un socket */
  String peticion = null;/*variable string*/
  String respuesta = null;/*variable de tipo cadena*/

  byte arreglo[] = new byte[10000];

 if (a.length > 0){/*Creamos un if para verificar que se hayan pasado los 
 	argumentos en ClienteAA si cumple con la condicion que se mayor 
 	realiza lo siguiente*/
 	String valorPuerto = a[1];/*Si este cumple se le asigna a valorPuerto el valor de a[1]*/

 	try
 	{// inicio try 1
 		/*Creamos un nuevo objeto y que guardara en un nuevo archivo llamado llave.ser*/
 	 ObjectInput in = new ObjectInputStream(new FileInputStream("llave.ser"));
 	 Key llave = (Key)in.readObject();
 	 System.out.println( "llave=" + llave );
 	 in.close();
 	 System.out.println("Me conecto al puerto del servidor "+ valorPuerto);
 	 /*se imprime en pantalla*/
  	 int cambioStringAInt = Integer.valueOf(valorPuerto);/*Se crea una variable cambioStringAInt
  	 se le cambia el valor por lo que contiene la var valorPuerto*/

  	socket = new Socket(a[0],cambioStringAInt);
 	 DataInputStream dis = new DataInputStream( socket.getInputStream() );
 	 DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );

int tDepo=0;
/*Se aplica la seccion de la llave para poder leer  */
 	 while(true){ //inicio while 2

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
 			/*aqui Se aplica la comparacion de palabra y la accion para el servidor */
 			if (plainTextReceived.equals("SALIR")){
 				System.out.println("\n\n Cerrando Conexión");
 		 		break;
 			 }
 			System.out.println( "\n\n Escribe un mensaje para el servidor:" );
			/*impresion en pantalla */

 			
 			peticion = br.readLine();
 			 System.out.println( "Ahora encriptamos el mensaje para el servidor..." );
 			 byte[] arrayPeticion = peticion.getBytes();/*codigo de envio*/
 			 Cipher cifrar = Cipher.getInstance("DES");
 			 cifrar.init(Cipher.ENCRYPT_MODE, llave);
 			 byte[] cipherText = cifrar.doFinal( arrayPeticion );
 			 bytesToBits( cipherText );
 			 dos.write( cipherText, 0, cipherText.length );/*codigo de envio*/
 			 /*en caso dado se aplican comparaciones de lo que hara el usuario*/
 			 if (peticion.equals("SALIR"))
 			 {
 			 	System.out.println("\n\nCerrando conexion con el servidor");
 		 		break;

 			 }
 			  
 			 if (peticion.equals("DEPOSITAR")) 
 			 {
 			 System.out.println("Cuanto es lo que deseas depositar");
 			///////////////////////////////////////////////////////////////////////
 			 String depositado = br.readLine();
 			
 			 byte[] arrayDepositado = depositado.getBytes();/*codigo de envio*/
 			 
 			 cifrar.init(Cipher.ENCRYPT_MODE, llave);
 			 byte[] cipherDepositado = cifrar.doFinal( arrayDepositado );
 			 bytesToBits( cipherDepositado );
 			 dos.write( cipherDepositado, 0, cipherDepositado.length );/*codigo de envio*/
 			 //int deposit=50;
 			//enviar al sertvi

 			  //System.out.println("Tienes : "+ tDepo);
 			 /// tDepo = (in);
 			 //  tDepo = tDepo + depositado;
 			
 			  // System.out.println("Tienes : "+ tDepo);
			///////////////////////////////////////////////////////////////////////
 			 }

 			  if (peticion.equals("CONSULTAR")) 
 			 {
 			 System.out.println("\n\tTu dinero total es:");
 			 ///////////////////////////////////////////////////////////////////////
 			 String consultar = br.readLine();
 			
 			 byte[] arrayConsultar = consultar.getBytes();/*codigo de envio*/
 			 
 			 cifrar.init(Cipher.ENCRYPT_MODE, llave);
 			 byte[] cipherConsultar = cifrar.doFinal( arrayConsultar );
 			 bytesToBits( cipherConsultar );
 			 dos.write( cipherConsultar, 0, cipherConsultar.length );/*codigo de envio*/
 			
 			//enviar al sertvi

 			 // System.out.println("Tienes : "+ tDepo);
 			 /// tDepo = (in);
 			 //  tDepo = tDepo + depositado;
 			
 			   //System.out.println("Tienes : "+ tDepo);
 			 ///////////////////////////////////////////////////////////////////////  
 			 }

 		
 			  if (peticion.equals("RETIRAR")) 
 			 {
 			 System.out.println("\n\tEXTRAYENDO SU DINERO... ... ...");
 			 //int guardado=0;

			 ///////////////////////////////////////////////////////////////////////
 			 String retirado = br.readLine();
 			
 			 byte[] arrayRetirado = retirado.getBytes();/*codigo de envio*/
 			 
 			 cifrar.init(Cipher.ENCRYPT_MODE, llave);
 			 byte[] cipherRetirado = cifrar.doFinal( arrayRetirado );
 			 bytesToBits( cipherRetirado );
 			 dos.write( cipherRetirado, 0, cipherRetirado.length );/*codigo de envio*/
 			 //int deposit=50;
 			//enviar al sertvi

 			  //System.out.println("Tenias : "+ tDepo);
 			
 			//codigo de regreso del servidor::


 			 // System.out.println("Tienes : "+ tDepo);
 			 ///////////////////////////////////////////////////////////////////////  
 			 
 			 // System.out.println("\n\tSU DINERO TOTAL ES  : "+ guardado);
 			   
 			 }
 			 /*tenemos varios ciclos para poder efectuar en la pantalla del cliente 
 			 todps estos son comparaciones con las palabras clave*/
 		} //cierre while 2

 	 dos.close();/*Cerramos variable escribir*/
 	 dis.close();/*Cerramos variable de lectura*/
 	 socket.close();/*Cerramos el socket fin*/
 	}
 	catch(IOException e)/*en caso de exista la excepcion capturamos en "e" el valor 
 	para que este se ejecute normalmente*/
 	{

 	 System.out.println("java.io.IOException generada");
 	 e.printStackTrace();/*impreision en pantalla del IOException*/
 	}

 } else {//cierre if puerto
 	System.out.println("No ha pasado el puerto");/*impresion en pantalla*/

 } //cierre else

 } //cierre main
/*lectura de los bits paso apaso */
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
 		/*muestra el mensaje completo*/
 	} //cierre bytes

 }// cierre class 