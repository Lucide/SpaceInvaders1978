package client;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import client.control.ControllerLobby;
import shared.model.Co;

public class Main{

	public static void main(String[] args){
		sapphire();

		@SuppressWarnings("unused")
		ControllerLobby controller=new ControllerLobby();
	}

	private static void sapphire(){
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch(UnsupportedLookAndFeelException|InstantiationException|IllegalAccessException|ClassNotFoundException ex){
			Co.error("Main: lookAndFeel error");
		}
	}
}
