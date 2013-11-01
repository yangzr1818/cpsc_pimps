package data;

import java.util.ArrayList;
import java.util.List;

/**
 *  stores all the data needed related to a class
 * 
 *
 */
public class Class {
	
	private String name;
	private String path;
	private List<Method> lom = new ArrayList<Method>();
	private String pack;
	private List<Field> lof = new ArrayList<Field>();
	private List<Class> oneToOneRelation = new ArrayList<Class>();
	private List<Class> oneToManyRelation = new ArrayList<Class>();
	private List<String> extend = new ArrayList<String>();
	private List<String> implement = new ArrayList<String>();
	
	// empty constructor
	public Class(){
	}
	
	/**
	 *  a new class
	 * @param newName name of the class
	 * @param newPath file path
	 */
	public Class(String newName, String newPath){
		this.name = newName;
		this.path = newPath;
	}
	
	/**
	 *  create a new class
	 * @param newName name of the class
	 * @param newPath file path
	 * @param newLom a list of methods in this class
	 */
	public Class(String newName, String newPath, List<Method> newLom){
		this.name = newName;
		this.path = newPath;
		this.lom.addAll(newLom);
	}
	
	/**
	 *  get the class name
	 * @return the name of this class as a string
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 *  get the path of the file
	 * @return a string containing the file path
	 */
	public String getPath(){
		return this.path;
	}
	
	/**
	 *  get the methods in this class
	 * @return a list of method in this class
	 */
	public List<Method> getMethods(){
		return this.lom;
	}
	
	/**
	 *  get the package that the class is in
	 * @return a string containing the package of this class
	 */
	public String getPackage(){
		return this.pack;
	}
	
	/**
	 *  get the fields in the class
	 * @return a list of fields that the class contains
	 */
	public List<Field> getFields(){
		return this.lof;
	}
	
	/**
	 *  set the name of the class
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 *  set the path of the file
	 * @param path
	 */
	public void setPath(String path){
		this.path = path;
	}
	
	/**
	 *  add the method to this class
	 * @param m method
	 */
	public void addMethod(Method m){
		this.lom.add(m);
	}
	
	/**
	 *  add all the methods to this class
	 * @param newLom a list of methods
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
	 * add a new one-to-one relationship
	 */
	public void addOneToOneRelation(Class r){
		this.oneToOneRelation.add(r);
	}
	
	/**
	 * add a new one-to-many relationship
	 */
	public void addOneToManyRelation(Class r){
		this.oneToManyRelation.add(r);
	}
	/**
	 * 
	 * @param e
	 */
	public void addAllExtend(List<String> e){
		this.extend.addAll(e);
	}
	
	/**
	 * 
	 * @param i
	 */
	public void addAllImplement(List<String> i){
		this.implement.addAll(i);
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
		System.out.println("to-one relationships: ");
		for(Class c : this.oneToOneRelation)
			System.out.println("  "+c.getName());
		System.out.println("to-many relationships:");
		for(Class c : this.oneToManyRelation)
			System.out.println("  "+c.getName());
		System.out.println("extends: "+this.extend);
		System.out.println("implements: "+this.implement);
		
	}

}
