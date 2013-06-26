package model.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassifierContainer {

	private static ClassifierContainer INSTANCE;
	
	private List<Classifier> classifier;
	
	private ClassifierContainer() {
		this.classifier = new ArrayList<Classifier>();
		
		// TODO: init Actions and Fitness
	}
		
	private List<Classifier> getMatchSet(List<Boolean> state) {
		List<Classifier> results = new ArrayList<Classifier>();
		for(Classifier c : classifier) {
			if(c.match(state)) {
				results.add(c);
			}
		}
		return results;
	}
	
	public List<Classifier> getActionSet(List<Boolean> state) {
		
		List<Classifier> matchSet = getMatchSet(state);
		
		List<Classifier> actionSet = new ArrayList<Classifier>();
		
		if(matchSet.size() == 0) {
			return actionSet;
		}
		
		if(matchSet.size() == 1){
			actionSet.add(matchSet.get(0));
			return actionSet;
		}
		
		Random rand = new Random();
		int wholeFitness = 0;		
		for(Classifier match : matchSet) {
			wholeFitness += match.getFitness();
		}
		int currentRandom = rand.nextInt(wholeFitness + 1);
		int current_fitness = 0;
		Classifier choosen = null;
		for(int i=0; i<matchSet.size()-1; i++) {
			current_fitness += matchSet.get(i).getFitness();
			int next_fitness = current_fitness + matchSet.get(i+1).getFitness();
			if(currentRandom >= current_fitness && currentRandom <= next_fitness) {
				choosen = matchSet.get(i);
				break;
			}
		}
		
		IAction choosenAction = choosen.getAction();
		for(Classifier c : matchSet) {
			if(c.getAction().equals(choosenAction)) {
				actionSet.add(c);
			}
		}
		
		return actionSet;
	}
	
	public static ClassifierContainer getInstance() {
		if(ClassifierContainer.INSTANCE == null) {
			ClassifierContainer.INSTANCE = new ClassifierContainer();
		}
		return ClassifierContainer.INSTANCE;
	}
	
}
