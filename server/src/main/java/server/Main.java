package server;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import server.control.Controller;
import shared.model.Co;

public class Main{

	public Main(){
	}

	public static void main(String[] args){
		sapphire();
		@SuppressWarnings("unused")
		Controller controller=new Controller();
	}

	private static void sapphire(){
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch(UnsupportedLookAndFeelException|InstantiationException|IllegalAccessException|ClassNotFoundException ex){
			Co.error("Main: lookAndFeel error");
		}
		UIManager.put("ProgressBar.background",Color.DARK_GRAY);
		UIManager.put("ProgressBar.foreground",Color.GRAY);
		UIManager.put("ProgressBar.selectionBackground",Color.WHITE);
		UIManager.put("ProgressBar.selectionForeground",Color.WHITE);
		UIManager.put("ComboBox.background",Color.GRAY);
	}
}
