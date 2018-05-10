package blockChain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class InputTransaccion implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -329735011625662501L;
	public String idOutput; //id del output
	public OutputTransaccion UTXO; //contiene la salida de una trnsaccion no utilizada
	public long timestamp;
	public long getTimestamp() {
		return timestamp;
	}
	
	public InputTransaccion(String idOutput) {
		this.idOutput = idOutput;
		timestamp = new Date().getTime();
	}
	private void writeObject(ObjectOutputStream stream)
            throws IOException {
		stream.writeObject(idOutput);
		stream.writeObject(UTXO);
	}
	public InputTransaccion(String idOutput, OutputTransaccion uTXO) {
		super();
		this.idOutput = idOutput;
		UTXO = uTXO;
	}
	private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
		idOutput = (String) stream.readObject();
		UTXO = (OutputTransaccion) stream.readObject();
	}
}
