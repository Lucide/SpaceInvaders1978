package view.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GTabbedPane extends JTabbedPane implements ChangeListener{
	public GTabbedPane(int layout){
		super(layout);
		addChangeListener(this);
	}

	@Override
	public void addTab(String title, Icon icon, Component component, String tip){
		super.addTab(title,icon,component,tip);
		stateChanged(null);
	}

	@Override
	public void stateChanged(ChangeEvent e){
		int i;

		for(i=getTabCount()-1; i>-1; i--){
			setForegroundAt(i,Color.LIGHT_GRAY);
		}
		i=getSelectedIndex();
		if(i!=-1){
			setForegroundAt(i,Color.WHITE);
		}
	}

}
