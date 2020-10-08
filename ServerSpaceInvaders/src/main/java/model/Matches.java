package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Matches{
	private final Listener listener;
	private final Map<Integer, Match> matches=new ConcurrentHashMap<>();

	public Matches(Listener listener){
		this.listener=listener;
	}

	public void add(Match match){
		matches.putIfAbsent(match.id,match);
		listener.msMatchesChanged();
	}

	private void remove(Match match){
		matches.remove(match.id);
		match.die();
	}

	synchronized public void purge(){
		for(Match match:matches.values()){
			if(!match.checkAlive()){
				remove(match);
				listener.log("Matches: purged "+match.id);
			}
		}
		listener.msMatchesChanged();
	}

	public void clear(){
		for(Match match:matches.values()){
			remove(match);
		}
		listener.msMatchesChanged();
	}

	public void route(int matchId, int pId, int command, int data){
		final Match match=matches.get(matchId);
		if(match==null){
			return;
		}
		match.received(pId,command,data);
	}

	public List<Match> list(){
		return new LinkedList<>(matches.values());
	}

	public interface Listener extends LogListener{
		void msMatchesChanged();
	}
}
