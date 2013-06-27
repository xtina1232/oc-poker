package model.controller;

import java.util.List;

public class Classifier {
	public static final int WAYNE = -1;
	
	private List<Integer> classifier;
	private int fitness;
	private int action;
	
	public Classifier(List<Integer> classifier, int fitness, int action) {
		this.classifier = classifier;
		this.fitness = fitness;
		this.action = action;
	}
	
	public List<Integer> getClassifier() {
		return classifier;
	}

	public void setClassifier(List<Integer> classifier) {
		this.classifier = classifier;
	}

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

}
