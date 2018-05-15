package blockChain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Constantes.Constantes;

public class BlockChainPrueba implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3409930309299685635L;
	public static ArrayList<Bloque> blockchain = new ArrayList<Bloque>();
	public static HashMap<String, OutputTransaccion> UTXOs = new HashMap<String, OutputTransaccion>();
	public static Queue<Transaccion> transaccionesSinMinar = new ConcurrentLinkedQueue<Transaccion>();
	public static int dificultad = 9;
	public static float transaccionMinima = 0.1f;
	public static transient ObjectOutputStream oos;
	public static Monedero coinbase = new Monedero();
	public static boolean minando;

	

	public static Boolean cadenaEsValida() {
		Bloque bloqueActual;
		Bloque bloqueAnterior;
		String hashObjetivo = new String(new char[dificultad]).replace('\0', '0');
		HashMap<String, OutputTransaccion> tempUTXOs = new HashMap<String, OutputTransaccion>(); // una lista temporal
																									// de transacciones
																									// sin gastar en un
																									// bloque

		// recorre el blockchain para comprobar los hashes:
		for (int i = 1; i < blockchain.size(); i++) {

			bloqueActual = (Bloque) blockchain.get(i);
			bloqueAnterior = blockchain.get(i - 1);
			// compara el hash registrado y el calculado:
			if (!bloqueActual.hash.equals(bloqueActual.calcularHash())) {
				System.out.println("Los hashes actuales no coinciden");
				return false;
			}
			// compara el hash previo y el hash previo registrado
			if (!bloqueAnterior.hash.equals(bloqueActual.hashPrevio)) {
				System.out.println("Los hash previos no coinciden");
				return false;
			}
			// comprueba si se ha solucionado el hash
			if (!bloqueActual.hash.substring(0, dificultad).equals(hashObjetivo)) {
				System.out.println("El bloque no ha sido minado");
				return false;
			}

			// se recorren las transacciones del blockchain:
			OutputTransaccion tempOutput;
			for (int t = 0; t < bloqueActual.transacciones.size(); t++) {
				Transaccion transaccionActual = bloqueActual.transacciones.get(t);

				if (!transaccionActual.comprobarFirma()) {
					System.out.println("La firma de la transaccion(" + t + ") es valida");
					return false;
				}
				if (transaccionActual.getValorInput() != transaccionActual.getValorOutput()) {
					System.out.println("Los inputs no son iguales a los outputs de la transaccion(" + t + ")");
					return false;
				}

				for (InputTransaccion input : transaccionActual.inputs) {
					tempOutput = tempUTXOs.get(input.idOutput);

					if (tempOutput == null) {
						System.out.println("Este input no esta en la transaccion(" + t + ")");
						return false;
					}

					if (input.UTXO.valor != tempOutput.valor) {
						System.out.println("El valor del input(" + t + ") no es valido");
						return false;
					}

					tempUTXOs.remove(input.idOutput);
				}

				for (OutputTransaccion output : transaccionActual.outputs) {
					tempUTXOs.put(output.id, output);
				}

				if (transaccionActual.outputs.get(0).receptor != transaccionActual.receptor) {
					System.out.println("El receptor de la transaccion(" + t + ") no es el que deberia ser");
					return false;
				}
				if (transaccionActual.outputs.get(1).receptor != transaccionActual.emisor) {
					System.out.println(
							"El receptor no ha sido quien ha cambiado el output de la transaccion (" + t + ").");
					return false;
				}

			}

		}
		System.out.println("Blockchain valida");
		return true;
	}

	public static void anadirBloque(Bloque nuevoBloque) throws FileNotFoundException, IOException {
		nuevoBloque.minarBloque(dificultad);
		blockchain.add(nuevoBloque);
		for ( Transaccion t : nuevoBloque.transacciones) {
			if (t.id.equals("0")) {
					BlockChainPrueba.UTXOs.put(t.outputs.get(0).id, t.outputs.get(0));
			}
			
		}
		if (oos == null) {
			Properties prop = new Properties();
			FileInputStream input = new FileInputStream(Constantes.props);
			prop.load(input);
			oos = new ObjectOutputStream(new FileOutputStream(prop.getProperty("blockchain"), false));
		}
		oos.writeObject(new BlockChainPrueba());

	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.writeObject(blockchain);
		stream.writeObject(UTXOs);
		stream.writeObject(transaccionesSinMinar);
		stream.writeInt(dificultad);
		stream.writeFloat(transaccionMinima);
		stream.writeObject(coinbase);
	}

	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		blockchain = (ArrayList<Bloque>) stream.readObject();
		UTXOs = (HashMap<String, OutputTransaccion>) stream.readObject();
		transaccionesSinMinar = (ConcurrentLinkedQueue<Transaccion>) stream.readObject();
		dificultad = stream.readInt();
		transaccionMinima = stream.readFloat();
		coinbase = (Monedero) stream.readObject();
	}


	public static void darRecompensa(String clave)
			throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		Transaccion t = new Transaccion(coinbase.clavePublica, StringUtil.getPublicKeyDeString(clave), 12f, null);
		t.generarFirma(coinbase.clavePrivada); // se firma la transaccion
		t.id = "0"; // de le da el id 0
		t.outputs.add(new OutputTransaccion(t.receptor, t.valor, t)); 																									
		t.esRecompensa = true;
		transaccionesSinMinar.add(t);
		try {
			Properties prop = new Properties();
			FileInputStream input = new FileInputStream(Constantes.props);
			prop.load(input);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(prop.getProperty("blockchain"),false));
			oos.writeObject(new BlockChainPrueba());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
