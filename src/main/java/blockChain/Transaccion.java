package blockChain;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;

public class Transaccion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3805676056221830082L;
	public String id; //hash de la transaccion
	public PublicKey emisor; //clave publica/direccion del emisor.
	public PublicKey receptor; //clave publica/direccion del receptor.
	public long timestamp;
	public float valor; //Cantidad que se desea enviar al receptor
	public byte[] firma; //Tpara evitar que nadie mas que nosotros tenga acceso a nuestro moonedero.
	
	public ArrayList<InputTransaccion> inputs = new ArrayList<InputTransaccion>();
	public ArrayList<OutputTransaccion> outputs = new ArrayList<OutputTransaccion>();
	public boolean esRecompensa = false;
	
	private static int secuencia = 0; //cuenta de transacciones generadas
	
	public long getTimestamp() {
		return timestamp;
	}
	
	// Constructor: 
	public Transaccion(PublicKey de, PublicKey para, float valor,  ArrayList<InputTransaccion> inputs) {
		this.emisor = de;
		this.receptor = para;
		this.valor = valor;
		this.inputs = inputs;
		timestamp = new Date().getTime();
	}
	
	public boolean procesarTransaccion() {
		
		if(comprobarFirma() == false) {
			System.out.println("no se pudo comprobar la firma");
			return false;
		}
				
		//Comprueba que las transacciones no han sido "gastadas"):
		for(InputTransaccion i : inputs) {
			i.UTXO = BlockChainPrueba.UTXOs.get(i.idOutput);
		}

		//Comprueba si la transaccion es valida:
		if(getValorInput() < BlockChainPrueba.transaccionMinima) {
			System.out.println("Los imputs de la transaccion son muy pocos: " + getValorInput());
			System.out.println("introduzca un valor mayor que " + BlockChainPrueba.transaccionMinima);
			return false;
		}
		
		//Genera los outputs de la transaccion:
		float leftOver = getValorInput() - valor; //coge el valor delos inputs y le resta el valor:
		id = calularHash();
		outputs.add(new OutputTransaccion( this.receptor, valor,this)); //envia el valor al receptor
		outputs.add(new OutputTransaccion( this.emisor, leftOver,this)); 	
				
		//añade el output a la lista de transacciones sin gastar
		for(OutputTransaccion o : outputs) {
			BlockChainPrueba.UTXOs.put(o.id , o);
		}
		
		//quita la transaccion de la lista de inputs:
		for(InputTransaccion i : inputs) {
			if(i.UTXO == null) continue; 
			BlockChainPrueba.UTXOs.remove(i.UTXO.id);
		}
		
		return true;
	}
	
	public Transaccion(String id, PublicKey emisor, PublicKey receptor, float valor, byte[] firma,
			ArrayList<InputTransaccion> inputs, ArrayList<OutputTransaccion> outputs) {
		super();
		this.id = id;
		this.emisor = emisor;
		this.receptor = receptor;
		this.valor = valor;
		this.firma = firma;
		this.inputs = inputs;
		this.outputs = outputs;
	}

	public float getValorInput() {
		float total = 0;
		for(InputTransaccion i : inputs) {
			if(i.UTXO == null) continue;
			total += i.UTXO.valor;
		}
		return total;
	}
	
	public void generarFirma(PrivateKey clavePrivada) {
		String datos = StringUtil.getStringDeclave(emisor) + StringUtil.getStringDeclave(receptor) + Float.toString(valor)	;
		firma = StringUtil.aplicarECDSASig(clavePrivada,datos);		
	}
	
	public boolean comprobarFirma() {
		String datos = StringUtil.getStringDeclave(emisor) + StringUtil.getStringDeclave(receptor) + Float.toString(valor)	;
		return StringUtil.comprobarECDSASig(emisor, datos, firma);
	}
	
	public float getValorOutput() {
		float total = 0;
		for(OutputTransaccion o : outputs) {
			total += o.valor;
		}
		return total;
	}
	
	public String calularHash() {
		secuencia++; //incrementa la secuencia para evitar dos transacciones iguales con el mismo hash
		return StringUtil.aplicarSha256(
				StringUtil.getStringDeclave(emisor) +
				StringUtil.getStringDeclave(receptor) +
				Float.toString(valor) + secuencia
				);
	}private void writeObject(ObjectOutputStream stream)
            throws IOException {
		stream.writeObject(id);
		stream.writeObject(emisor.getEncoded());
		stream.writeObject(receptor.getEncoded());
		stream.writeFloat(valor);
		stream.writeObject(firma);
		stream.writeObject(inputs);
		stream.writeObject(outputs);
		stream.writeBoolean(esRecompensa);
		stream.writeLong(timestamp);;
	}
	private void readObject(java.io.ObjectInputStream stream) throws ClassNotFoundException, IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
		id = (String) stream.readObject();
		emisor = fact.generatePublic(new X509EncodedKeySpec((byte[]) stream.readObject()));
		receptor = fact.generatePublic(new X509EncodedKeySpec((byte[]) stream.readObject()));
		valor = stream.readFloat();
		firma = (byte[]) stream.readObject();
		inputs = (ArrayList<InputTransaccion>) stream.readObject();
		outputs = (ArrayList<OutputTransaccion>) stream.readObject();
		esRecompensa = stream.readBoolean();
		timestamp = stream.readLong();
	}
}
