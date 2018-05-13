package blockChain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Bloque implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7771623379861003887L;
	public String hash;
	public String hashPrevio;
	public String arbolMerkle;
	public ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();
	public long timeStamp; // como numero en milisegundos.
	public int nonce;

	// constructor
	public Bloque(String hashPrevio) {
		this.hashPrevio = hashPrevio;
		this.timeStamp = new Date().getTime();

		this.hash = calcularHash();
	}

	// calcular un nuevo hash que depende del contenido del bloque
	public String calcularHash() {
		String hashCalculado = StringUtil
				.aplicarSha256(hashPrevio + Long.toString(timeStamp) + Integer.toString(nonce) + arbolMerkle);
		return hashCalculado;
	}

	// Incrementa el valor de nonce hasta que el hash objetivo es alcanzado.
	public void minarBloque(int dificultad) {
		arbolMerkle = StringUtil.getMerkleRaiz(transacciones);
		String objetivo = StringUtil.getStringDificultad(dificultad); // Crea un String de dificultad * "0"
		while (!hash.substring(0, dificultad).equals(objetivo)) {
			nonce++;
			hash = calcularHash();
		}
		System.out.println("Bloque minado!! : " + hash);
		System.out.println("Nonce: " + nonce);
	}

	// Añade transacciones al bloque
	public boolean anadirTransaccion(Transaccion transaccion) {
		// procesa la transaccion y comprueba si es valida, a no ser que sea el primer
		// bloque, en ese caso se ignora.
		if (transaccion == null)
			return false;
		if ((!"0".equals(hashPrevio))) {
			if ((transaccion.procesarTransaccion() != true)) {
				System.out.println("Fallo al procesar la transaccion");
				return false;
			}
		}

		transacciones.add(transaccion);
		System.out.println("Transaccion añadida correctamente al bloque");
		return true;
	}

	public boolean anadirRecompensa(Transaccion t) {
		transacciones.add(t);
		System.out.println("Transaccion añadida correctamente al bloque");
		return true;
	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.writeObject(hash);
		stream.writeObject(hashPrevio);
		stream.writeObject(arbolMerkle);
		stream.writeObject(transacciones);
		stream.writeLong(timeStamp);
		stream.writeInt(nonce);
	}

	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {

		hash = (String) stream.readObject();
		hashPrevio = (String) stream.readObject();
		arbolMerkle = (String) stream.readObject();
		transacciones = (ArrayList<Transaccion>) stream.readObject();
		timeStamp = stream.readLong();
		nonce = stream.readInt();
	}

	public Bloque(String hash, String hashPrevio, String arbolMerkle, ArrayList<Transaccion> transacciones,
			long timeStamp, int nonce) {
		super();
		this.hash = hash;
		this.hashPrevio = hashPrevio;
		this.arbolMerkle = arbolMerkle;
		this.transacciones = transacciones;
		this.timeStamp = timeStamp;
		this.nonce = nonce;
	}

}
