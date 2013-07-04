package model.controller;

import java.util.ArrayList;
import java.util.List;


/**
 * @author peter
 * 
 * Representiert einen Klassifizierer
 * Dieser besteht nicht nur aus Binären Elementen, sondern aus Ganzzahlen und dem Wildcard *
 */
public class Classifier implements Cloneable {
	
	public static final int WAYNE = -1;
	
	private List<Integer> classifier;
	private int fitness;
	private int action;
	
	public Classifier(List<Integer> classifier, int fitness, int action) {
		this.classifier = classifier;
		this.fitness = fitness;
		this.action = action;
	}
	
	/**
	 * Überprüft ob ein übergebener Zustand die Prüfung durch diesen Klassifizierer übersteht
	 * 
	 * @param state Der Zustand
	 * @return true, wenn der Zustand auf den Classifier matched, sonst false
	 */
	public boolean match(List<Integer> state) {
		for(int i=0; i<classifier.size(); i++) {
			int wc = classifier.get(i);
			int current = state.get(i);
			if(wc != current && wc != Classifier.WAYNE) {
				return false;
			}
		}
		return true;
	}
	
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public int getAction() {
		return action;
	}

	public int getFitness() {
		return fitness;
	}
	
	public void setAction(int action) {
		this.action = action;
	}
	
	public List<Integer> getClassifier() {
		return classifier;
	}

	public void setClassifier(List<Integer> classifier) {
		this.classifier = classifier;
	}
	
	@Override
	public String toString() {
		
		// Fitness
		String out = String.format("F: %3d",fitness);
		// Action
		out += String.format(" A: %2d",action);

		// Classifier
		out += " C: ";
		for(int i=0; i<classifier.size() - 1; i++) {
			out += String.format("%3d ",classifier.get(i));
		}
		if(classifier.size() > 1) {
			out += String.format("%3d ",classifier.get(classifier.size() - 1));
		}
		
		
		return out;
	}

	@Override
	protected Classifier clone() {
		List<Integer> newList = new ArrayList<Integer>();
		for(int i=0; i<classifier.size(); i++) {
			newList.add(classifier.get(i));
		}
		Classifier c = new Classifier(newList, this.fitness, this.action);
		return c;
	}
}
