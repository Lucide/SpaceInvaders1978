import java.awt.Color;
import java.awt.Insets;

import javax.swing.UIManager;

import control.ControllerLobby;

public class Main {

	public static void main(String[] args) {
		sapphire();
		
		@SuppressWarnings("unused")
		ControllerLobby controller=new ControllerLobby();
	}
	
	private static void sapphire(){
		UIManager.put("TabbedPane.focus", Color.DARK_GRAY);
		UIManager.put("TabbedPane.selectHighlight", Color.DARK_GRAY);
		UIManager.put("TabbedPane.borderHightlightColor", Color.DARK_GRAY);
		UIManager.put("TabbedPane.selected",Color.DARK_GRAY);
		UIManager.put("TabbedPane.darkShadow", Color.GRAY);
		UIManager.put("TabbedPane.light", Color.GRAY);
		UIManager.put("TabbedPane.unselectedBackground", Color.GRAY);
		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
	}

}
