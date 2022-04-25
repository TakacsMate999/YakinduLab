package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class generated {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
	    String line;
		while((line = bf.readLine()) != null && !line.equalsIgnoreCase("exit")) {
if(line.equals("start")) {
	s.raiseStart();
s.runCycle();
				print(s);
}
if(line.equals("white")) {
	s.raiseWhite();
s.runCycle();
				print(s);
}
if(line.equals("black")) {
	s.raiseBlack();
s.runCycle();
				print(s);
}
if(line.equals("cica")) {
	s.raiseCica();
s.runCycle();
				print(s);
}
		}
		System.exit(0);
	}

	public static void print(IExampleStatemachine s) {System.out.println("whiteTime = " + s.getSCInterface().getWhiteTime());
System.out.println("blackTime = " + s.getSCInterface().getBlackTime());
System.out.println("cicaTimer = " + s.getSCInterface().getCicaTimer());
	}
}
