package data;

/**
 *  store all the info needed related to the method
 * 
 *
 */
public class Method {

	private String name;
	private Class c;
	
	// empty constructor
	public Method(){
		c = new Class();
	}
	
	/**
	 *  new method with only its name
	 * @param newName name of the method
	 */
	public Method(String newName){
		this.name = newName;
	}
	
	/**
	 *  new method
	 * @param newName name of the method
	 * @param newClass class that contains this method
	 */
	public Method(String newName, Class newClass){
		this.name = newName;
		this.c = newClass;
	}
	
	/**
	 *  get the name of the method
	 * @return name of the method
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 *  get the class that the method is in
	 * @return the class that contains this method
	 */
	public Class getTheClass(){
		return this.c;
	}
	
	/**
	 *  set the class that the method is in
	 * @param newClass class that contains this method
	 */
	public void setClass(Class newClass){
		this.c = newClass;
	}
	
	/**
	 *  set the name of the method
	 * @param newName method name
	 */
	public void setName(String newName){
		this.name = newName;
	}
	
	/**
	 *  print info for debug
	 */
	public void print(){
		System.out.println("  " +this.name);
	}
	
	/**
	 *  print class name for debug
	 */
	public void printClassName(){
		System.out.println(this.c.getName());
	}
	
}
