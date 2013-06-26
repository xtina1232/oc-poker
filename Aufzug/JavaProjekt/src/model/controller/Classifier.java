package model.controller;

import java.util.List;

public class Classifier {
	private List<Wildcard> classifier;
	private int fitness;
	private IAction action;
	
	public Classifier(List<Wildcard> classifier, int fitness, IAction action) {
		this.classifier = classifier;
		this.fitness = fitness;
		this.action = action;
	}
	
	public List<Wildcard> getClassifier() {
		return classifier;
	}

	public void setClassifier(List<Wildcard> classifier) {
		this.classifier = classifier;
	}

	public boolean match(List<Boolean> state) {
		for(int i=0; i<classifier.size(); i++) {
			Wildcard wc = classifier.get(i);
			Boolean current = state.get(i);
			if (current && wc != Wildcard.set) {
				return false;
			} else if (!current && wc != Wildcard.unset) {
				return false;
			}
		}
		return true;
	}
	
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public IAction getAction() {
		return action;
	}

	public int getFitness() {
		return fitness;
	}
	
	public void setAction(IAction action) {
		this.action = action;
	}

}
