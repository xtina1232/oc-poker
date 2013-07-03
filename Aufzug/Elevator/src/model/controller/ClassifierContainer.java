package model.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassifierContainer {
	
	private static ClassifierContainer INSTANCE;
	private List<Classifier> classifiers;
	
	public static ClassifierContainer getInstance() {
		if(ClassifierContainer.INSTANCE == null) {
			ClassifierContainer.INSTANCE = new ClassifierContainer();
		}
		return ClassifierContainer.INSTANCE;
	}
	
	private ClassifierContainer() {
		this.classifiers = new ArrayList<Classifier>();
		Random rand = new Random();
		
		for (int i=0; i<10; i++) {
			List<Integer> classifier = new ArrayList<Integer>();
			// Passagiere warten an etagen
			for (int i2=0; i2<10; i++) {
				classifier.add(rand.nextInt(2));
			}
			// Positionen der Aufzüge
			for (int i2=0; i2<4; i++) {
				classifier.add(rand.nextInt(10) + 1);
			}
			// Richtungen der Aufzüge
			for (int i2=0; i2<4; i++) {
				classifier.add(rand.nextInt(2));
			}
			// Passagiere im Aufzug
			for (int i2=0; i2<4; i++) {
				classifier.add(rand.nextInt(2));
			}
			this.classifiers.add(new Classifier(classifier, 20, rand.nextInt(46)));
		}
	}
		
	
	
	public List<Classifier> getActionSet(List<Integer> state) {
		
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
		
		int choosenAction = choosen.getAction();
		for(Classifier c : matchSet) {
			if(c.getAction() == choosenAction) {
				actionSet.add(c);
			}
		}
		
		return actionSet;
	}
	
	private List<Classifier> getMatchSet(List<Integer> state) {
		List<Classifier> results = new ArrayList<Classifier>();
		for(Classifier c : this.classifiers) {
			if(c.match(state)) {
				results.add(c);
			}
		}
		return results;
	}
	
	
}
