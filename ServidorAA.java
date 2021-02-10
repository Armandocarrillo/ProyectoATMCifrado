// se ejecuta en terminal asi: java ServidorAA 8080
import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import java.util.Scanner;
//se ingresaron las librerias que usaremos
public class ServidorAA /*Se crea la clase ServidorAA*/
{
public static void main(String a[]) throws Exception/*Iniciamos nuestra Funcion principal*/
{ /*Creamos nuestras variables, creamos el socker para el servidor*/
 ServerSocket serverSocket = null;
 Socket socket = null;
 String peticion = null;
 String peticionInicial = null;
 String respuesta = null;
 Integer contador = 0;
 Integer saldo = 0;
 Integer suma = 0;
 Integer cambioArreglodepositadoAInt = 0;
String bienvenida = "Bievenido a ATM\n\tPresiona una opcion:\nCONSULTAR\nDEPOSITAR\nRETIRAR\n";

if (a.length > 0){ //inicio if 1
	String valorPuerto = a[0];
/*Se genera la sweccion de nuestra llave */
File f = new File("llave.ser");
	if(!f.exists()) { 
	
		System.out.println( "Generando la llave..." );/*Impresion en pantalla de llave*/
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(56);
		Key newKey = keyGen.generateKey();/*Se le asigna a new key el valor de la llave generada*/
		System.out.println( "llave=" + newKey );/*Muestra nuestra llave*/
		System.out.println( "Llave generada!" );

		/*En los puntos siguientes se genera el archivo donde se guardara nuestra llave
		y este pueda ser compartido con el cliente*/
		ObjectOutput out = new ObjectOutputStream(new FileOutputStream("llave.ser"));
		out.writeObject( newKey );
		out.close();
	}
	
     ObjectInput in = new ObjectInputStream(new FileInputStream("llave.ser"));
	 Key llave = (Key)in.readObject();
	 System.out.println( "llave=" + llave );
	 in.close();



	try/*asinamos la seccion del puerto en espera*/
	{// inicio try 1
	 System.out.println("Escuchando por el puerto" + valorPuerto);
 	int cambioStringAInt = Integer.valueOf(valorPuerto);
	serverSocket = new ServerSocket(cambioStringAInt);
	}// fin try 1
	catch(IOException e)
	{/*Inicio de CATCH*/
	 System.out.println("java.io.IOException generada");/*Impresion en pantalla de la excepcion*/
	 e.printStackTrace();
	}/*Final de CATCH*/

	System.out.println("Esperando a que los clientes se conecten...");/*impresion en pantalla*/
while(true)
	{ //inicio  while ciclo
	 try
	 { //inicio 2 try
		socket = serverSocket.accept();/*Se acepta el socket creado se le asigna el valor a socket*/
		System.out.println("Se conecto un cliente: " + socket.getInetAddress().getHostName());
		DataInputStream dis = new DataInputStream( socket.getInputStream() );
		DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
		
		/*desplegamos en pantalla el menu guardado en bienvenida*/
		peticionInicial = bienvenida;
		 System.out.println( "Mi peticion es: " + peticionInicial );
		 System.out.println( "Ahora encriptamos la peticion..." );/*Impreison en pantalla*/
		 byte[] arrayPeticionInicial = peticionInicial.getBytes();
		 Cipher cifrarInicial = Cipher.getInstance("DES");
		 cifrarInicial.init(Cipher.ENCRYPT_MODE, llave);
		 byte[] cipherTextInicial = cifrarInicial.doFinal( arrayPeticionInicial );
		 bytesToBits( cipherTextInicial );
		 
		 dos.write( cipherTextInicial, 0, cipherTextInicial.length );
		
		 /*Seccion de encriptar el mensaje*/

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
		 
		 if (peticion.equals("SALIR")){/*se efectua si el cliente ingresa SALIR */
		 	break;
		 }
		 
		int bytesLeidos = dis.read(arreglo);
		
		byte arreglo2[]	= new byte[bytesLeidos];
		for(int i=0; i < bytesLeidos; i++ ){
			arreglo2[i] = arreglo[i];
		}
		/*Seccion de encriptado*/
		Cipher decifrar = Cipher.getInstance("DES");
		decifrar.init(Cipher.DECRYPT_MODE, llave);
		bytesToBits( arreglo2 );
		byte[] newPlainText = decifrar.doFinal(arreglo2);
		System.out.println( "\n\nMensaje recibido del cliente :" );
		String messageReceivedInPlainText = new String(newPlainText, "UTF8") ;
		System.out.println( new String(newPlainText, "UTF8"));
		
		/*Ciclo if en caso de que el cliente ejecute una accion en el servidor nos
		mostrara las siguientes lienas*/
		if (messageReceivedInPlainText.equals("SALIR")){
			System.out.println("\n\n El servidor ha cerrado la conexion");
		 	break;
		} else if (messageReceivedInPlainText.equals("DEPOSITAR")){
			System.out.println("\n\n El cliente esta depositando");

			bytesLeidos = dis.read(arreglo);

			byte arregloDepositado[]	= new byte[bytesLeidos];
		for(int i=0; i < bytesLeidos; i++ ){
			arregloDepositado[i] = arreglo[i];
		}
		/*Seccion de encriptado*/
		decifrar.init(Cipher.DECRYPT_MODE, llave);
		bytesToBits( arregloDepositado );
		byte[] newDeposit = decifrar.doFinal(arregloDepositado);
		System.out.println( "\n\nMensaje recibido del cliente :" );
		String messageReceivedInDeposit = new String(newDeposit, "UTF8") ;
		System.out.println( new String(newDeposit, "UTF8"));
		//
		//  arregloDepositado=Integer.paser   Recordar era para convertir
		cambioArreglodepositadoAInt = Integer.parseInt(messageReceivedInDeposit);


		suma = cambioArreglodepositadoAInt + suma;
		//arregloDepositado = Integer.parseInt();
		System.out.println( "\t Tu nuevo saldo es:" + suma );

		 	continue;
		} else if (messageReceivedInPlainText.equals("CONSULTAR")){
			System.out.println("\n\n El cliente esta consultando");
		 ///////////////////////////////////
		 	bytesLeidos = dis.read(arreglo);

			byte arregloConsultado[]	= new byte[bytesLeidos];
		for(int i=0; i < bytesLeidos; i++ ){
			arregloConsultado[i] = arreglo[i];
		}
		/*Seccion de encriptado*/
		decifrar.init(Cipher.DECRYPT_MODE, llave);
		bytesToBits( arregloConsultado );
		byte[] newConsult = decifrar.doFinal(arregloConsultado);
		System.out.println( "\n\nMensaje recibido del cliente :" );
		String messageReceivedInDeposit = new String(newConsult, "UTF8") ;
		System.out.println( new String(newConsult, "UTF8"));
		System.out.println( "\t Tu nuevo saldo es:" + suma );

		//
		//   arregloConsultado=Integer.paser
//////////////////////////////////////////////////////////////////////

		 	continue;
		} else if (messageReceivedInPlainText.equals("RETIRAR")){
			System.out.println("\n\n El cliente esta retirando");
		 	continue;
		} 	
	 	
	 }// fin while 2

		dos.close();/*Se cierra el modo de escritura*/
		dis.close();/*Se cierra el modo de lectura */
		socket.close();/*Cierre del socket se asigna null*/
	 } //fin 2 try
	 catch(IOException e)
	 {
		System.out.println("java.io.IOException generada");/*Impresio de excepcion*/
		e.printStackTrace();
	 }
	}// fin while 1

 


} else {  // cierre del if 1 e inicio del else 1
	System.out.println("No ha pasado el puerto");/*si es que no pasa el puerto*/
} // cierre else 1


} //cierre main

public static void bytesToBits( byte[] texto )
	{ // static bytesToBits
		StringBuilder stringToBits = new StringBuilder();
		for( int i=0; i < texto.length; i++ )
		{/*aplicacion del for */
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
		System.out.println( "El mensaje completo en bits es:" + stringToBits );/*mensaje terminado*/
	} // Fin static bytesToBits


} // cierre class