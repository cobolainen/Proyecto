package blockChain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class OutputTransaccion implements Serializable{
	public String id;
	public PublicKey receptor; //nuevo dueño de las monedas, o de los datos que se quieran enviar.
	public float valor; //cantidad de monedas que posee
	public Transaccion transaccionPadre; //id de la transaccion en la que este output ha sido creado
	public long timestamp;
	public long getTimestamp() {
		return timestamp;
	}
	
	//Constructor
	public OutputTransaccion(PublicKey receptor, float valor, Transaccion transaccionPadre) {
		this.receptor = receptor;
		this.valor = valor;
		this.transaccionPadre = transaccionPadre;
		this.id = StringUtil.aplicarSha256(StringUtil.getStringDeclave(receptor)+Float.toString(valor)+transaccionPadre);
		timestamp = new Date().getTime();
	}
	
	//Comprueba si la moneda o los datos te pertenecen
	public boolean mePertenece(PublicKey clavePublica) {
		return (clavePublica.equals(receptor));
	}
	private void writeObject(ObjectOutputStream stream)
            throws IOException {
		stream.writeObject(id);
		stream.writeObject(receptor.getEncoded());
		stream.writeFloat(valor);
		stream.writeObject(transaccionPadre);
	}
	public OutputTransaccion(String id, PublicKey receptor, float valor, Transaccion idTransaccionPadre) {
		super();
		this.id = id;
		this.receptor = receptor;
		this.valor = valor;
		this.transaccionPadre = idTransaccionPadre;
	}

	private void readObject(java.io.ObjectInputStream stream) throws ClassNotFoundException, IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
		id = (String) stream.readObject();
		receptor = fact.generatePublic(new X509EncodedKeySpec((byte[]) stream.readObject()));
		valor = stream.readFloat();
		transaccionPadre = (Transaccion) stream.readObject();
	}
	
}
