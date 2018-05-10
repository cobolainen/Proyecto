package blockChain;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import com.google.gson.GsonBuilder;
import java.util.List;

public class StringUtil {
	
	//Aplica Sha256 a un string. 
	public static String aplicarSha256(String input){
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        
			//Aplica sha256 a nuestro input, 
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
	        
			StringBuffer hexString = new StringBuffer(); // Esto contendra un hash en hexadecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//Aplica la firma ECDSA y devuelve el resultado en bytes.
	public static byte[] aplicarECDSASig(PrivateKey clavePrivada, String input) {
		Signature dsa;
		byte[] output = new byte[0];
		try {
			dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(clavePrivada);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}
	
	//Comprueba la firma
	public static boolean comprobarECDSASig(PublicKey clavePublica, String datos, byte[] firma) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(clavePublica);
			ecdsaVerify.update(datos.getBytes());
			return ecdsaVerify.verify(firma);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//COnversor de un Object a un JSON String
	public static String getJson(Object o) {
		return new GsonBuilder().setPrettyPrinting().create().toJson(o);
	}
	public static Object getBlockChain(String o) {
		return new GsonBuilder().setPrettyPrinting().create().fromJson(o, ArrayList.class);
	}
	
	//Devuleve la dificultad del String objetivo, para compararlo al hash. Dificultad 5 devolvera "00000"  
	public static String getStringDificultad(int dificultad) {
		return new String(new char[dificultad]).replace('\0', '0');
	}
	
	public static String getStringDeclave(Key clave) {
		return Base64.getEncoder().encodeToString(clave.getEncoded());
		
	}
	public static PublicKey getPublicKeyDeString (String str) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
		
		return fact.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(str)));
	}
	public static PrivateKey getPrivateKeyDeString (String str) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
		
		return fact.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(str)));
	}
	public static String getMerkleRaiz(ArrayList<Transaccion> transsacciones) {
		int cont = transsacciones.size();
		
		List<String> capaArbolAnterior = new ArrayList<String>();
		for(Transaccion transaccion : transsacciones) {
			capaArbolAnterior.add(transaccion.id);
		}
		List<String> capaArbol = capaArbolAnterior;
		
		while(cont > 1) {
			capaArbol = new ArrayList<String>();
			for(int i=1; i < capaArbolAnterior.size(); i+=2) {
				capaArbol.add(aplicarSha256(capaArbolAnterior.get(i-1) + capaArbolAnterior.get(i)));
			}
			cont = capaArbol.size();
			capaArbolAnterior = capaArbol;
		}
		
		String merkleRaiz = (capaArbol.size() == 1) ? capaArbol.get(0) : "";
		return merkleRaiz;
	}
}
