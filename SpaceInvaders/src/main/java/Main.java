import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import control.ControllerLobby;
import model.Co;

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
