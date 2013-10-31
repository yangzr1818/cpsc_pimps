package data;

// stores all the information required for a field
public class Field {

	private String name;
	private String type;
	private String init;
	private Class c;
	
	// empty constructor
	public Field(){
	}
	
	// new field 
	public Field(String name, String type, String init){
		this.name = name;
		this.type = type;
		this.init = init;
	}
	
	// get the name of the field
	public String getName(){
		return this.name;
	}
	
	// get the type of the field
	public String getType(){
		return this.type;
	}
	
	// get the initial value of the field
	public String getInitialValue(){
		return this.init;
	}
	
	// get the class
	public Class getTheClass(){
		return this.c;
	}
	
	// set the class
	public void setClass(Class c){
		this.c = c;
	}
	
	// print for debug
	public void print(){
		System.out.println("  " + this.type + " " +this.name+" = "+this.init);
	}
	
	// print the class
	public void printClass(){
		System.out.println(this.c.getName());
	}
	
}
