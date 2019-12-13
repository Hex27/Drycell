package org.drycell.command;

public class InvalidArgumentException extends Exception {
	
	private String problem;
	public InvalidArgumentException(String problem){
		this.problem = problem;
	}
	
	public String getProblem(){
		return problem;
	}

}
