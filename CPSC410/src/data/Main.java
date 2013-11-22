package data;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.perfspy.aspect.AbstractPerfSpyAspect;

// reinforced implementation
public class Main {
	
	private static List<Class> loc = new ArrayList<Class>();
	private static FileParser fileParser;
    
    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        
        // path may vary
        searchJava("D:\\assignment\\CPSC\\410\\PacmanLab4");    
        
        fileParser.parseFile();
        fileGenerator();
        PerfSpyDemoAsepct p = new PerfSpyDemoAsepct();
        p.withinCflowOps();
        p.cflowOps();
    }

    
    /**
     *  given a root directory of a project, produce the file paths of all the java file
     * @param path
     */
    public static void searchJava(String path){
        
        File dir = new File(path);
        List<File> directories = new ArrayList<File>();
        List<Class> loc = new ArrayList<Class>();
        directories.add(dir);
        while (!directories.isEmpty())
        {
            List<File> subDirectories = new ArrayList<File>();
            for (File file : directories) {
                for(File f: file.listFiles())
                    if(f.isDirectory()){
                        subDirectories.add(f);
                    }
                    else if (f.getName().endsWith((".java"))) {
                        loc.add(new Class(eliminateDotJava(f.getName()),f.getPath()));
                    }
            }
            directories.clear();
            directories.addAll(subDirectories);
        }
        fileParser = new FileParser(loc);
    }
    /**
     * Remove the characters after the '.'
     * @param s file name
     * @return the file name without the characters after the dot '.'
     */
    public static String eliminateDotJava(String s){
        int dotIndex=0;
        // iterate from the last character to the first one, because the filename extension is always
        // at the end of the file name
        for(int i=s.length()-1; i>0;i--){
            if(s.charAt(i) == '.'){
                dotIndex=i;
                // stop the iteration if dot is found
                break;
            }
            
        }
        return s.substring(0, dotIndex);
    }
    
    /**
     * Find the relationship between classes, i.e. one-to-one, one-to-many
     */
    public static void findRelation(){
    	for(Class c: loc){
    		String className = c.getName();
    		for(Class c2: loc){
    			if(c2 != c){
		    		for(Field f: c2.getFields()){
		    			if(f.getType().equals(className))
		    				c2.addOneToOneRelation(c);
		    			else if (eliminateBracket(f.getType()).equals(className))
		    				c2.addOneToManyRelation(c);
		    				
		    		}
    			}
    		}
    	}
    }
    
    /**
     * 
     */
    public static String eliminateBracket(String s){
    	int startIndex = 0,endIndex = 1;
        // iterate from the last character to the first one
        for(int i=s.length()-1; i>=1;i--){
            if(s.charAt(i) == '>'){
                endIndex = i;
            }
            if(s.charAt(i) == '<'){
                startIndex = i;
            }
            
        }
        return s.substring(startIndex+1, endIndex);
    }
    
    /**
     * generate the .dot file for graphviz to draw the class diagram
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void fileGenerator() throws FileNotFoundException, UnsupportedEncodingException{
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
    public static Class findClass(String name){
    	for(Class c : loc){
    		if(c.getName() == name)
    			return c;
    	}
    	return null;
    }
    
    @Aspect
    private static class PerfSpyDemoAsepct extends AbstractPerfSpyAspect {
           
            @Pointcut("execution(*)")
            public void withinCflowOps() {
            	
            }

           @Pointcut("cflow(execution(*)")
            public void cflowOps() {
            }

    }
    
}
