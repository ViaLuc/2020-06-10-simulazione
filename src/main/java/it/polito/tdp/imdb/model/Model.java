package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private HashMap<Integer, Actor> idAttori;
	private List<Adiacenza> adiacenze;

	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public List<String> getGeneri(){
		return this.dao.listGeneri();
	}
	
	public int vertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int archi() {
		return this.grafo.edgeSet().size();
	} 
	
	public void creaGrafo(String genere) {
		this.grafo= new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.idAttori=new HashMap<Integer, Actor>();
		this.adiacenze=new ArrayList<Adiacenza>(dao.listEdge(genere));
		
		
		List<Actor> attori = new ArrayList<Actor>(dao.listAllActorsGenere(genere));
		
		for(Actor a : attori)
		{
			idAttori.put(a.id, a);
			this.grafo.addVertex(a.id);
		}
		
		for(Adiacenza e: adiacenze)
		{
			if(idAttori.containsKey(e.getId1()) || idAttori.containsKey(e.getId2()))
				Graphs.addEdgeWithVertices(this.grafo, e.getId1(), e.getId2(), e.getPeso());
		}
		
	}
	
	public List<Actor> attoriConnessi(Actor a){
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<Actor,DefaultWeightedEdge>(grafo);
		List<Actor> attori = new ArrayList<Actor>(ci.connectedSetOf(a));
		attori.remove(a);
		Collections.sort(attori, new Comparator<Actor>() {

			@Override
			public int compare(Actor o1, Actor o2) {
				return o1.lastName.compareTo(o2.lastName);
			}
			
		});
	}
	
	public List<String> attoriGrafo(){
		List<String> attori=new ArrayList<String>();
		
		for(Actor a: this.idAttori.values())
		{
			attori.add(a.toString());
		}
		
		Collections.sort(attori);
		return attori;
	}
	
	

}
