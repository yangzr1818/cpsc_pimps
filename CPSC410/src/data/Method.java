package data;

import japa.parser.ast.body.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 *  store all the info needed related to the method
 * 
 *
 */
public class Method {

	private String name;
	private Class c;
	private List<Parameter> parameters = new ArrayList<Parameter>();
	private String returnType;
	
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
	 *  get the return type of this method
	 */
	public String getReturnType(){
		return this.returnType;
	}
	
	/**
	 *  get the parameters of this method
	 */
	public List<Parameter> getParameters(){
		return this.parameters;
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
	 *  set the return type of this method
	 */
	public void setReturnType(String type){
		this.returnType = type;
	}
	
	/**
	 *  set the parameters of this method
	 */
	public void setParameters(List<Parameter> lop){
		this.parameters = lop;
	}
	
	/**
	 *  print info for debug
	 */
	public void print(){
		String parameter = "";
		if(this.parameters != null){
			for(Parameter p : this.parameters){
				if(parameter == "")
					parameter = parameter+p.getId().toString()+" : "+p.getType().toString();
				else parameter = parameter+", "+p.getId().toString()+" : "+p.getType().toString();
			}
		}
		System.out.println("  " +this.name+"("+parameter+") : "+this.returnType);
	}
	
	/**
	 *  print class name for debug
	 */
	public void printClassName(){
		System.out.println(this.c.getName());
	}
	
}
