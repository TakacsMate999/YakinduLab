package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;
import org.yakindu.sct.model.sgraph.Reaction;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.lang.model.element.VariableElement;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	private static final String mainFunc1 = "package hu.bme.mit.yakindu.analysis.workhere;\r\n" + 
			"\r\n" + 
			"import java.io.BufferedReader;\r\n" + 
			"import java.io.IOException;\r\n" + 
			"import java.io.InputStreamReader;\r\n" + 
			"\r\n" + 
			"import hu.bme.mit.yakindu.analysis.RuntimeService;\r\n" + 
			"import hu.bme.mit.yakindu.analysis.TimerService;\r\n" + 
			"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\r\n" + 
			"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"public class generated {\r\n" + 
			"	\r\n" + 
			"	public static void main(String[] args) throws IOException {\r\n" + 
			"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
			"		s.setTimer(new TimerService());\r\n" + 
			"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
			"		s.init();\r\n" + 
			"		s.enter();\r\n" + 
			"		s.runCycle();\r\n" + 
			"		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));\r\n" + 
			"	    String line;\r\n" + 
			"		while((line = bf.readLine()) != null && !line.equalsIgnoreCase(\"exit\")) {";
	
	public static final String print1 = "		}\r\n" + 
			"		System.exit(0);\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"	public static void print(IExampleStatemachine s) {";
	
	public static final String end = "	}\r\n" + 
			"}";
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		int unnamed = 0;
		List<String> events = new ArrayList<>();
		List<String> variables = new ArrayList<>();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				List<Transition> tl  = state.getOutgoingTransitions();
				
			
				if(tl.isEmpty())
					System.out.println(state.getName()+" is a dead end!");
				if(state.getName() == null) {
					System.out.println("State with no name. Suggested name: State"+ unnamed);
					state.setName("State"+ unnamed++);
				}
				
			}
			if(content instanceof Transition) {
				Transition trans = (Transition) content;
				System.out.println(trans.getSource().getName() + " -> " +trans.getTarget().getName());
				
				
			}
			if(content instanceof VariableDefinition) {
				VariableDefinition ve = (VariableDefinition) content;
				if(ve.getName().equals("whiteTime"))
					System.out.println("W = "+ve.getName());
				else if(ve.getName().equals("blackTime"))
					System.out.println("B = "+ve.getName());
				variables.add(ve.getName());
			}
			
			if(content instanceof EventDefinition) {
				EventDefinition ve = (EventDefinition) content;
				System.out.println(ve.getName());
				events.add(ve.getName());
			}
		}
		StringBuilder codeBuilder = new StringBuilder();
		codeBuilder.append(mainFunc1+"\n");
		for (int i = 0; i < events.size(); i++) {
			String event = events.get(i);
			StringBuilder sb = new StringBuilder();
			/*
			 * 
			 * if(line.equals("start")) {
				s.raiseStart();
				s.runCycle();
				print(s);
			}
			 */
			sb.append("if(line.equals(\""+event+"\")) {\n");
			sb.append("\ts.raise"+event.substring(0,1).toUpperCase()+event.substring(1)+"();\n");
			sb.append("s.runCycle();\r\n" + 
					"				print(s);\n}\n");
			codeBuilder.append(sb.toString());
		}
		codeBuilder.append(Main.print1);
		for (int i = 0; i < variables.size(); i++) {
			String variable = variables.get(i);
			codeBuilder.append("System.out.println(\""+variable+" = \" + s.getSCInterface().get"+variable.substring(0,1).toUpperCase()+variable.substring(1)+"());\n");
		}
		codeBuilder.append(end);
		System.out.println(codeBuilder.toString());
		try {
			PrintWriter out = new PrintWriter("src/hu/bme/mit/yakindu/analysis/workhere/generated.java");
			out.println(codeBuilder.toString());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
