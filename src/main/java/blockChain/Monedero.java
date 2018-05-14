package blockChain;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

public class Monedero implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1990676464777995451L;
	public transient PrivateKey clavePrivada;
	public transient PublicKey clavePublica;
	public String name;
	
	public HashMap<String,OutputTransaccion> UTXOs = new HashMap<String,OutputTransaccion>();
	
	public Monedero(String name) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Proveedor de seguridad
		this.name = name;
		generarKeyPair();
	}
	public Monedero() {
		generarKeyPair();
	}
		
	public void generarKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom aleatorio = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Inicializa el generador de claves y genera un KeyPair
			keyGen.initialize(ecSpec, aleatorio); //256 
	        KeyPair keyPair = keyGen.generateKeyPair();
	        // se asignan la clave publica y privada del KeyPair
	        clavePrivada = keyPair.getPrivate();
	        clavePublica = keyPair.getPublic();
	       
	        
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Monedero(PrivateKey clavePrivada, PublicKey clavePublica, HashMap<String, OutputTransaccion> uTXOs) {
		super();
		this.clavePrivada = clavePrivada;
		this.clavePublica = clavePublica;
		UTXOs = uTXOs;
	}


	public float getBalance(DefaultListModel<Transaccion> listModel) {
		float total = 0;	
        for (Map.Entry<String, OutputTransaccion> item: BlockChainPrueba.UTXOs.entrySet()){
        	OutputTransaccion UTXO = item.getValue();
            if(UTXO.mePertenece(clavePublica)) { //si el output me pertenece ( si la moneda me pertenece )
            	UTXOs.put(UTXO.id,UTXO); //se añade a la lista de transacciones no gastadas.
            	total += UTXO.valor ; 
            	
            }
        }  
		return total;
	}
	public float getBalance() {
		float total = 0;	
        for (Map.Entry<String, OutputTransaccion> item: BlockChainPrueba.UTXOs.entrySet()){
        	OutputTransaccion UTXO = item.getValue();
            if(UTXO.mePertenece(clavePublica)) { //si el output me pertenece ( si la moneda me pertenece )
            	UTXOs.put(UTXO.id,UTXO); //se añade a la lista de transacciones no gastadas.
            	total += UTXO.valor ; 
            }
        }  
		return total;
	}
	
	public Transaccion enviarFondos(PublicKey _receptor,float cantidad ) {
		if(getBalance() < cantidad) {
			System.out.println("no hay suficientes fondos para realizar la transaccion.");
			return null;
		}
		ArrayList<InputTransaccion> inputs = new ArrayList<InputTransaccion>();
		
		float total = 0;
		for (Map.Entry<String, OutputTransaccion> item: UTXOs.entrySet()){
			OutputTransaccion UTXO = item.getValue();
			total += UTXO.valor;
			inputs.add(new InputTransaccion(UTXO.id));
			if(total > cantidad) break;
		}
		
		Transaccion nuevaTransaccion = new Transaccion(clavePublica, _receptor , cantidad, inputs);
		nuevaTransaccion.generarFirma(clavePrivada);
		
		for(InputTransaccion input: inputs){
			UTXOs.remove(input.idOutput);
		}
		
		return nuevaTransaccion;
	}
	private void writeObject(ObjectOutputStream stream)
            throws IOException {
		stream.writeObject(clavePrivada.getEncoded());
		stream.writeObject(clavePublica.getEncoded());
		stream.writeObject(UTXOs);
		stream.writeObject(name);
		
	}
	private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
		clavePrivada = fact.generatePrivate(new PKCS8EncodedKeySpec((byte[]) stream.readObject()));
		clavePublica  = fact.generatePublic(new X509EncodedKeySpec((byte[]) stream.readObject()));
		UTXOs = (HashMap<String, OutputTransaccion>) stream.readObject();
		name = (String) stream.readObject();
		
		
	}
	
}


