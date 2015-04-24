package org.provebit.proof;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Shishir Kanodia, Noah Malmed
 *
 */
public class Proof {

	private String type = "Document Proof";
	private final String version = "0.1";
	private Timestamp idealTime;
	private Timestamp provenTime;
	private byte[] transactionPath;
	private byte[] transactionID;
	private byte[] merkleRoot;
	private byte[] blockID;
	private byte[] fileHash;
	
	public Proof(Timestamp iTime, Timestamp pTime, byte[] path, byte[] transID, byte[] root, byte[] bID, byte[] fileHash){
		idealTime = iTime;
		provenTime = pTime;
		transactionPath = path;
		transactionID = transID;
		merkleRoot = root;
		blockID = bID;
		this.fileHash = fileHash;
	}
	
	public Proof(File yamlFile) throws FileNotFoundException{
		InputStream yamlInput = new FileInputStream(yamlFile);
		Yaml parser = new Yaml();		
		Object parsedYaml = parser.load(yamlInput);
		if(parsedYaml instanceof Map<?, ?>){
			Map<String,String> yamlMap = (Map<String,String>)parsedYaml;
			idealTime = Timestamp.valueOf(yamlMap.get("Ideal Time"));
			provenTime = Timestamp.valueOf(yamlMap.get("Proven Time"));
			try {
				transactionPath = Hex.decodeHex(yamlMap.get("Transaction Path").toCharArray());
				transactionID = Hex.decodeHex(yamlMap.get("Transaction ID").toCharArray());
				merkleRoot = Hex.decodeHex(yamlMap.get("Merkle Root").toCharArray());
				blockID = Hex.decodeHex(yamlMap.get("Block ID").toCharArray());
				fileHash = Hex.decodeHex(yamlMap.get("File Hash").toCharArray());
			} catch (DecoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the transaction_path
	 */
	public byte[] getTransaction_path() {
		return transactionPath;
	}
	/**
	 * @param transaction_path the transaction_path to set
	 */
	public void setTransaction_path(byte[] transaction_path) {
		this.transactionPath = transaction_path;
	}
	/**
	 * @return the transactionID
	 */
	public byte[] getTransactionID() {
		return transactionID;
	}
	/**
	 * @param transactionID the transactionID to set
	 */
	public void setTransactionID(byte[] transactionID) {
		this.transactionID = transactionID;
	}
	/**
	 * @return the merkleroot
	 */
	public byte[] getMerkleroot() {
		return merkleRoot;
	}
	/**
	 * @param merkleroot the merkleroot to set
	 */
	public void setMerkleroot(byte[] merkleroot) {
		merkleRoot = merkleroot;
	}
	/**
	 * @return the blockId
	 */
	public byte[] getBlockId() {
		return blockID;
	}
	/**
	 * @param blockId the blockId to set
	 */
	public void setBlockId(byte[] blockId) {
		blockID = blockId;
	}
	/**
	 * @return the fileHash
	 */
	public byte[] getFileHash() {
		return fileHash;
	}
	/**
	 * @param fileHash the fileHash to set
	 */
	public void setFileHash(byte[] fileHash) {
		this.fileHash = fileHash;
	}
	
	public Timestamp getIdealTime() {
		return idealTime;
	}

	public void setIdealTime(Timestamp idealTime) {
		this.idealTime = idealTime;
	}

	public Timestamp getProvenTime() {
		return provenTime;
	}

	public void setProvenTime(Timestamp provenTime) {
		this.provenTime = provenTime;
	}
	
	public void writeProofToFile(String file){

		Map<String, String> mapRep = new HashMap<String, String>();
		mapRep.put("Type", type);
		mapRep.put("Version", version);
		mapRep.put("Ideal Time", idealTime.toString());
		mapRep.put("Proven Time", provenTime.toString());
		mapRep.put("Transaction Path", Hex.encodeHexString(transactionPath));
		mapRep.put("Transaction ID", Hex.encodeHexString(transactionID));
		mapRep.put("Merkle Root", Hex.encodeHexString(merkleRoot));
		mapRep.put("Block ID", Hex.encodeHexString(blockID));
		mapRep.put("File Hash", Hex.encodeHexString(fileHash));
		

		DumperOptions options = new DumperOptions();
		options.setPrettyFlow(true);
		
	    Yaml yaml = new Yaml(options);
	    FileWriter fw = null;
		try {
			fw = new FileWriter(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    yaml.dump(mapRep, fw);
	}

}
