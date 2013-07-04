package model.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.controller.nn.ParameterProvider;


/**
 * @author peter
 * 
 * Verwaltungsklasse, die ein Menge an Klassifikatoren verwaltet und Methoden bereitstellt mit diesen zu arbeiten
 */
/**
 * @author peter
 *
 */
/**
 * @author peter
 *
 */
/**
 * @author peter
 *
 */
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
		if(ParameterProvider.DEBUG) {
			System.out.println("Creating new ClassifierContainer!");
		}
		
		this.classifiers = new ArrayList<Classifier>();
		
		addDefaultClassifier();
		
		for (int i=0; i<ParameterProvider.CLASSIFIER_COUNT; i++) {
			classifiers.add(getRandomClassifier());
		}
		
		if(ParameterProvider.DEBUG) {
			System.out.println("classifiers final size: "+this.classifiers.size());
		}
	}

	
	/**
	 * Gibt das ActionSet zu einem Umgebungszustand zurück.
	 * Wird von außerhalb aufgerufen.
	 * Intern wird erst das MatchSet berechnet
	 */
	public List<Classifier> getActionSet(List<Integer> state) {
		
		if(ParameterProvider.DEBUG) {
			
			System.out.println("- Getting ActionSet - ");
			System.out.println("");
			System.out.print(String.format("State(%d):    ",state.size()));
			for(int i=0; i<state.size() - 1; i++) {
				System.out.print(String.format("%3d ",state.get(i)));
			}
			if(state.size() > 1) {
				System.out.println(String.format("%3d ",state.get(state.size() - 1)));
			}
			
			for(int j=0; j<classifiers.size(); j++) {
				System.out.println(classifiers.get(j).toString());			
			}
		
		}
		
		List<Classifier> matchSet = getMatchSet(state);
		
		if(ParameterProvider.DEBUG) {
		
			System.out.println(String.format("MatchSet-Size: %d",matchSet.size()));
			System.out.println("");
			
		}
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
		if(choosen != null) {
			int choosenAction = choosen.getAction();
			for(Classifier c : matchSet) {
				if(c.getAction() == choosenAction) {
					actionSet.add(c);
				}
			}
		}
		
		return actionSet;
	}
	
	/**
	 * Gibt das MatchSet zurück
	 */
	private List<Classifier> getMatchSet(List<Integer> state) {
		List<Classifier> results = new ArrayList<Classifier>();
		for(Classifier c : this.classifiers) {
			if(c.match(state)) {
				results.add(c);
			}
		}
		return results;
	}
	
	/**
	 * Fügt einen Standard-Klassifizierer hinzu, der erstmal
	 * alles ignoriert und annimmt, dass man in der untersten Etage ist.
	 * Die Aktion die gewählt wird ist dann zufällig
	 */
	private void addDefaultClassifier() {
		Random rand = new Random();
		List<Integer> classifier = new ArrayList<Integer>();
		// Passagiere warten an etagen
		for (int i2=0; i2<10; i2++) {
			classifier.add(Classifier.WAYNE);
		}
		// Positionen der AufzÃ¼ge
		for (int i2=0; i2<4; i2++) {
			classifier.add(0);
			// Richtungen der Aufzüge
			classifier.add(Classifier.WAYNE);
		}
		this.classifiers.add(new Classifier(classifier, 20, rand.nextInt(46)));
	}

	
	/**
	 * Gibt einen zufälligen Klassifizierer zurück.
	 * Dieser beinhaltet immer zu einer sehr hohen Wahrscheinlichkeit WAYNE,
	 * sodass er möglichst oft zuschlägt.
	 */
	private Classifier getRandomClassifier() {
		Random rand = new Random();
		List<Integer> classifier = new ArrayList<Integer>();
		// Passagiere warten an Etagen
		for (int waitingPassengerIndex=0; waitingPassengerIndex < 10; waitingPassengerIndex++) {
			
			float drawn = rand.nextFloat();
			int current = -1;
			if(drawn >= 0f && drawn <= 0.6f) {
				// 60% Wahrscheinlichkeit, dass man WAYNE setzt
				current = Classifier.WAYNE;
			} else if(drawn > 0.6f && drawn <= 0.8f) {
				// 20% Wahrscheinlichkeit, dass man 0 setzt
				current = 0;
			} else if(drawn > 0.8f && drawn <= 1.0f) {
				// 20% Wahrscheinlichkeit, dass man 1 setzt
				current = 1;
			}
			classifier.add(current);
		}
		
		
		for (int elevatorPositionIndex=0; elevatorPositionIndex < 4; elevatorPositionIndex++) {
			int current = -1;
			
			// Positionen der Aufzüge
			float drawn = rand.nextFloat();
			
			if(drawn >= 0f && drawn <= 0.6f) {
				// 60% Wahrscheinlichkeit, dass man WAYNE setzt
				current = Classifier.WAYNE;
			} else if(drawn > 0.6f && drawn <= 0.8f) {
				// 20% Wahrscheinlichkeit, dass man 0 setzt
				current = 0;
			} else if(drawn > 0.8f && drawn <= 1.0f) {
				// 20% Wahrscheinlichkeit, dass man einen festen Wert setzt
				current = 1;//rand.nextInt(9) + 1;
			}
			classifier.add(current);
			
			
			// Richtungen der Aufzüge
			drawn = rand.nextFloat();
			
			if(drawn >= 0f && drawn <= 0.75f) {
				// 75% Wahrscheinlichkeit, dass man WAYNE setzt
				current = Classifier.WAYNE;
			} else if(drawn > 0.75f && drawn <= 1.0f) {
				// 25% Wahrscheinlichkeit, dass man einen Wert setzt
				current = rand.nextInt(3);
			}
			classifier.add(current);
		}
		return new Classifier(classifier, 20, rand.nextInt(46));
	}
		
	
	/**
	 * Mutiert die Menge an Klassifizieren
	 * Entfernt die zwei schlechtesten Klassifizierer
	 * Fügt einen zufälligen hinzu.
	 * Und nimmt eine Kopie des besten, verändert diesen zu 50% an 3 Stellen und fügt diesen hinzu.
	 */
	public void mutate() {
		Classifier mutation = null;
		mutation = getMutatedBestClassifier(mutation);
		if(mutation != null) {
			this.classifiers.add(mutation);
			if(ParameterProvider.PRINT) {
				System.out.println("Mutierten Classifier mit Aktion:" + mutation.getAction() + " und Fitness: " + mutation.getFitness() + " hinzugefügt");
			}
			removeWorstClassifier();
		}
		
		Classifier newClassifier = getRandomClassifier();
		this.classifiers.add(newClassifier);
		if(ParameterProvider.PRINT) {
			System.out.println("Zufälligen Classifier mit Aktion:" + newClassifier.getAction() + " und Fitness: " + newClassifier.getFitness() + " hinzugefügt");
		}
		
		removeWorstClassifier();
	}

	
	/**
	 * Gibt den besten Klassifizierer zurück (mit der höchsten Fitness)
	 */
	private Classifier getMutatedBestClassifier(Classifier better) {
		if(this.classifiers.size() > 0) {
			// Mutate and copy best
			int toCopy = 0;
			int currentBestFitness = this.classifiers.get(0).getFitness();
			for(int i=1; i<this.classifiers.size(); i++) {
				if(classifiers.get(i).getFitness() > currentBestFitness) {
					currentBestFitness = classifiers.get(i).getFitness();
					toCopy = i;
				}
			}
			better = this.classifiers.get(toCopy).clone();
			mutateBits(better,3);
		}
		return better;
	}

	
	/**
	 * Entfernt den schlechtesten Klassifizierer (mit der niedrigsten Fitness).
	 */
	private void removeWorstClassifier() {
		if(this.classifiers.size() > 0) {
			// Remove Worst
			int toRemove = 0;
			int currentWorstFitness = classifiers.get(0).getFitness();
			for(int i=1; i<this.classifiers.size(); i++) {
				if(classifiers.get(i).getFitness() < currentWorstFitness) {
					currentWorstFitness = classifiers.get(i).getFitness();
					toRemove = i;
				}
			}
			this.classifiers.remove(toRemove);
		}
	}
	
	/**
	 * Mutiert countOfBits Bits mit einer Wahrscheinlichkeit von 50% in dem
	 * übergebenen Klassifizierer
	 */
	private void mutateBits(Classifier classifier, int countOfBits) {
		Random rand = new Random();
		Classifier reference = getRandomClassifier();
		List<Integer> referenceClassifier = reference.getClassifier();
		
		List<Integer> bitList = classifier.getClassifier();
		for(int i=0; i<countOfBits; i++) {
			if(rand.nextFloat() >= 0.5) {
				int indexToChange = rand.nextInt(bitList.size());
				bitList.set(indexToChange, referenceClassifier.get(indexToChange));
			}
		}
		classifier.setClassifier(bitList);
	}

	public void debugClassifiers() {
		System.out.println("Classifier:");
		for(Classifier c : classifiers) {
			System.out.println(c);
		}
	}
	
	
}
