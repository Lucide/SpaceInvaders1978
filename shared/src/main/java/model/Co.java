package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Co{
	public static final short COMMAND_SIZE=1;
	public static final String CLIENT_SEPARATOR="\7", FIELD_SEPARATOR="\30";

	public static int[] getIntArray(byte src[]){
		ByteArrayInputStream ais=new ByteArrayInputStream(src);
		DataInputStream dis=new DataInputStream(ais);
		int v[]=null;
		try{
			v=new int[dis.readInt()];
			for(int i=0; i<v.length; i++){
				v[i]=dis.readInt();
			}
		}catch(IOException ex){
			Co.error("getIntArray: fail");
		}
		return v;
	}

	public static byte[] getByteArray(int src[]){
		ByteArrayOutputStream aos=new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(aos);
		try{
			dos.writeInt(src.length);
			for(int i=0; i<src.length; i++) dos.writeInt(src[i]);
		}catch(IOException ex){
			Co.error("getByteArray: fail");
		}
		return aos.toByteArray();
	}

	public static int atoi(String integer){
		return Integer.parseInt(integer);
	}

	public static void error(String s){
		System.err.println(s);
		System.exit(1);
	}
}
