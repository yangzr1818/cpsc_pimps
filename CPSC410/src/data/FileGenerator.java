package data;

import japa.parser.ast.body.Parameter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileGenerator {
	
	private List<Class> loc = new ArrayList<Class>();
	
	public FileGenerator(List<Class> loc){
		this.loc.addAll(loc);
	}
	
	/**
	 * generate the .dot file for graphviz to draw the class diagram
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void fileGenerator() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("base.dot", "UTF-8");
		writer.println("digraph G {\n"+
				"fontname = \"Bitstream Vera Sans\"\n"+
				"fontsize = 10\n"+
				"node [\n"+
				"fontname = \"Bitstream Vera Sans\"\n"+
				"fontsize = 10\n"+
				"shape = \"record\"\n"+
				"]\n"+
				"edge [\n"+
				"fontname = \"Bitstream Vera Sans\"\n"+
				"fontsize = 10\n"+
				"]");
		for(Class n: loc){
			List<Field> f = n.getFields();
			List<Method> m = n.getMethods();
			String fields="<tr><td></td></tr>", methods = "<tr><td></td></tr>";
			for(Field field: f){
				String fieldName = field.getName();
				String fieldType = field.getType();
				fieldType = fieldType.replaceAll("<", "&lt;");
				fieldType = fieldType.replaceAll(">", "&gt;");
				fields = fields+"<tr><td>- "+fieldName+" : "+fieldType+"</td></tr>";
			}
			for(Method method: m){
				String methodName = method.getName();
				String parameter = "";
				if(method.getParameters() != null){
					for(Parameter p : method.getParameters()){
						if(parameter == "")
							parameter = parameter+p.getId().toString()+" : "+p.getType().toString();
						else parameter = parameter+", "+p.getId().toString()+" : "+p.getType().toString();
					}
				}
				String returnType = method.getReturnType().replaceAll("<", "&lt;");
				returnType = returnType.replaceAll(">", "&gt;");
				methods = methods+"<tr><td>+ "+methodName+"("+parameter+") : "+returnType+"</td></tr>";
			}
			writer.println(n.getName()+"[\nlabel = <<table border=\"0\" cellborder=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>"+n.getName()+"</td></tr>"+fields+methods+"</table>>\n]");

			ArrayList<String> parentClasses = (ArrayList<String>) n.getParentClass();
			for(String p:parentClasses){
				String a = n.getName()+"->"+p;
				if(findClass(p)==null){
					writer.println(p+"[\nlable = \"{"+p+"}\"\n]");
				}
				writer.println( "edge [\narrowhead = \"empty\" \nheadlabel=\"\"\n]\n"+a+"\n");

			}
			for(Class c : n.getToOneRelationship()){
				String relation = n.getName()+"->"+c.getName();
				writer.println("edge[\narrowhead = \"none\" \nheadlabel = \"0,1\"\n]\n"+relation);
			}
			for(Class c : n.getToManyRelationship()){
				String relation = n.getName()+"->"+c.getName();
				writer.println("edge[\narrowhead = \"none\" \nheadlabel = \"0...*\"\n]\n"+relation);
			}

		}
		writer.println("}");
		writer.close();
	}

	/**
	 * find the class given its name
	 * @param name
	 * @return the class found or null if not exist
	 */
	public Class findClass(String name){
		for(Class c : loc){
			if(c.getName() == name)
				return c;
		}
		return null;
	}

}
