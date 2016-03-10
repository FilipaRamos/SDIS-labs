import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ServerFile {
	// the id of the server to which the original copy of the file belongs to
	public int homeServer;
	// the name of the file
	public String name;
	// the identifier of the file
	public String identifier;
	// Name of the owner of the file
	public String owner;
	// When the file was modified
	public String date;
	// size of the file
	public int size;
	// number of chunks
	public int chunksNo;
	// to save the chunks
	public ArrayList<Chunk> chunks = new ArrayList<Chunk>();


	// constructor
	public ServerFile(int homeServer, String name, int size, String owner) throws NoSuchAlgorithmException, FileNotFoundException {

		this.homeServer = homeServer;
		this.name = name;
		this.size = size;
		this.owner = owner;
		this.chunksNo = this.size / 64000 + 1;

		File f = new File(name);
		this.date = Long.toString(f.lastModified());
		
	//	BufferedReader reader = new BufferedReader(new FileReader(f));
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		String id = name + date + owner;
		md.update(id.getBytes());
		byte[] digest = md.digest();
		this.identifier = String.format("%064x", new java.math.BigInteger(1, digest));
		
		System.out.println("File belongs to server: " + this.homeServer);
		System.out.println("Filename: " + this.name);
		System.out.println("File Identifier: " + this.identifier);
		System.out.println("File size: " + this.size);
		System.out.println("Owner of the file: " + this.owner);
		System.out.println("Number of chunks to be used: " + this.chunksNo);
		
		int missingSize = this.size;
		int filledSize = 0;
		
		for(int i = 0; i < this.chunksNo; i++){
			if(missingSize >= 64000){
				
				filledSize = 64000;
				missingSize = missingSize - filledSize;
				
				System.out.println("Chunk Size --> " + filledSize);
				Chunk chunk = new Chunk(1, i, filledSize);
				chunks.add(chunk);
				continue;
				
			}
			else if(missingSize < 64000){
				
				filledSize = missingSize;
				
				System.out.println("Chunk Size --> " + filledSize);
				Chunk chunk = new Chunk(1, i, filledSize);
				chunks.add(chunk);
				continue;
				
			}
		}

	}

}
