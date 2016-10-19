package tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Classe qui contient des outils pour sérialisation et desérialisation
 *
 */
public final class Serialisation {

	public static Serializable deserialisation(byte[] arrayByte) throws IOException, ClassNotFoundException {
		ByteArrayInputStream intputStream = new ByteArrayInputStream(arrayByte);
		BufferedInputStream bufferStream = new BufferedInputStream(intputStream);
		ObjectInputStream objectStream = new ObjectInputStream(bufferStream);
		Serializable deserialisation = null;

		deserialisation = (Serializable) objectStream.readObject();

		objectStream.close();
		bufferStream.close();
		intputStream.close();
		return deserialisation;
	}

	public static byte[] serialisation(Serializable serialisation) throws IOException {
		byte[] arrayByte;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferStream = new BufferedOutputStream(outputStream);
		ObjectOutputStream objectStream = new ObjectOutputStream(bufferStream);
		objectStream.writeObject(serialisation);
		objectStream.close();
		bufferStream.close();
		outputStream.close();
		arrayByte = outputStream.toByteArray();
		return arrayByte;
	}
}
