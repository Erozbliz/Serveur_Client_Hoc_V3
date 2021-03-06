package tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Classe qui contient des outils pour sÚrialisation et desÚrialisation
 *
 */
public final class Serialisation {


	
	  public static Object deserialize2(byte[] bytes) throws IOException, ClassNotFoundException {
	        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
	            try(ObjectInputStream o = new ObjectInputStream(b)){
	                return o.readObject();
	            }
	        }
	  }
	    

	   public static byte[] serialize2(Object obj) throws IOException {
	        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
	            try(ObjectOutputStream o = new ObjectOutputStream(b)){
	                o.writeObject(obj);
	            }
	            return b.toByteArray();
	        }
	    }
}
