package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.NetworkInterface;

public class Co{
	public static NetworkInterface ni;

	public static int[] getIntArray(byte[] src){
		ByteArrayInputStream ais=new ByteArrayInputStream(src);
		DataInputStream in=new DataInputStream(ais);
		int v[]=null;
		try{
			v=new int[in.readInt()];
			for(int i=0; i<v.length; i++) v[i]=in.readInt();
		}catch(IOException e){
			System.out.println("getIntArray: fail");
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
			System.out.println("getByteArray: fail");
		}
		return aos.toByteArray();
	}
}
