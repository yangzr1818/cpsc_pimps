package data;

import java.util.ArrayList;
import java.util.List;

// stores all the data needed related to a class
// In order to avoid confusion, Class1 is used as the name of this class
public class Class {
	
	private String name;
	private String path;
	private List<Method> lom = new ArrayList<Method>();
	private String pack;
	private List<Field> lof = new ArrayList<Field>();
	
	// empty constructor
	public Class(){
	}
	
	// a new class
	public Class(String newName, String newPath){
		this.name = newName;
		this.path = newPath;
	}
	
	// create a new class
	public Class(String newName, String newPath, List<Method> newLom){
		this.name = newName;
		this.path = newPath;
		this.lom.addAll(newLom);
	}
	
	// get the class name
	public String getName(){
		return this.name;
	}
	
	// get the path of the file
	public String getPath(){
		return this.path;
	}
	
	// get the methods in this class
	public List<Method> getMethods(){
		return this.lom;
	}
	
	// get the package that the class is in
	public String getPackage(){
		return this.pack;
	}
	
	// get the fields in the class
	public List<Field> getFields(){
		return this.lof;
	}
	
	// set the name of the class
	public void setName(String name){
		this.name = name;
	}
	
	// set the path of the file
	public void setPath(String path){
		this.path = path;
	}
	
	// add the method to this class
	public void addMethod(Method m){
		this.lom.add(m);
	}
	
	/**
	 *  add all the methods to this class
	 * @param newLom
	 */
	public void addAllMethod(List<Method> newLom){
		this.lom.addAll(newLom);
	}
	
	/**
	 *  set the package
	 * @param p package
	 */
	public void setPackage(String p){
		this.pack = p;
	}
	
	/**
	 *  add a new field
	 * @param field
	 */
	public void addField(Field field){
		this.lof.add(field);
	}
	
	/**
	 *  add a list of field
	 * @param lof a list of fields
	 */
	public void addAllFields(List<Field> lof){
		this.lof.addAll(lof);
	}
	
	/**
	 *  print out all the information for debug
	 */
	public void print(){
		System.out.println("Class name: " +this.name);
		System.out.println("File path: "+this.path);
		System.out.println("Package: " + this.pack);
		System.out.println("Fields in this class: ");
		for(Field f : this.lof){
			f.print();
		}
		System.out.println("Methods in this class: ");
		for(Method m : this.lom){
			m.print();
		}
		
	}

}
