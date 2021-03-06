package mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import model.LTS;
import model.State;
import model.Transition;

/* this class save a .dot file in a LTS */
public class LTSmapper {

	public static LTS mapping(File file) {
		LTS lts = new LTS();
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			while (!line.equals("}")){
				if (line.contains("shape=circle")) { /* it is the case if generated by COnfECt or ASSESS */
					String statename = line.substring(0, line.indexOf("["));
					State state = new State(statename);
					lts.addState(state);
				}
				else if (line.contains("->") & !line.contains("S00")) {
					String keySource = line.substring(0, line.indexOf(" ->"));
					String keyTarget = line.substring(line.indexOf(" ->")+4, line.indexOf("["));
					State source = lts.getState(keySource);
					State target = lts.getState(keyTarget);
					String trname = line.substring(line.indexOf("label =")+8, line.indexOf("]")-1); 
					Transition transition = new Transition(source, trname, target);
					lts.addTransition(transition);
					source.addSuccesseur(transition);
					target.addPredecesseur(transition);
				}
				else if (line.contains("->") & line.contains("S00")) {
					String init = line.substring(line.indexOf(" ->")+4);
					lts.setInitialState(lts.getState(init));
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("file " + file + " not found: " + e);
		} catch (IOException e){
			System.out.println("error " + e);
		}
		return lts;
	}

}
