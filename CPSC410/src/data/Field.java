package data;

/**
 *  stores all the information required for a field
 *
 *
 */
public class Field {

	private String name;
	private String type;
	private String init;
	private Class c;
	
	// empty constructor
	public Field(){
	}
	
	/**
	 *  new field 
	 * @param name name of this field
	 * @param type type of this field
	 * @param init initial value of this field
	 */
	public Field(String name, String type, String init){
		this.name = name;
		this.type = type;
		this.init = init;
	}
	
	/**
	 *  get the name of the field
	 * @return the name of this field as a string 
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 *  get the type of the field
	 * @return the type of this field as a string
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 *  get the initial value of the field
	 * @return the initial value of the field as a string
	 */
	public String getInitialValue(){
		return this.init;
	}
	
	/**
	 *  get the class
	 * @return the class that contains this field
	 */
	public Class getTheClass(){
		return this.c;
	}
	
	/**
	 *  set the class
	 * @param c the class which has this field
	 */
	public void setClass(Class c){
		this.c = c;
	}
	
	/**
	 *  print for debug
	 */
	public void print(){
		System.out.println("  " + this.type + " " +this.name+" = "+this.init);
	}
	
	/**
	 *  print the class name
	 */
	public void printClass(){
		System.out.println(this.c.getName());
	}
	
}
