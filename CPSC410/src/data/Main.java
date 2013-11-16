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

// reinforced implementation
public class Main {
	
	private static List<Class> loc = new ArrayList<Class>();
	private static List<Method> lom = new ArrayList<Method>();
	private static List<Field> lof = new ArrayList<Field>();
	private static List<String> loe = new ArrayList<String>();
	private static List<String> loi = new ArrayList<String>();
    
    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        
        // path may vary
        searchJava("D:\\assignment\\CPSC\\410\\PacmanLab4");    
        
        parseFile();
        fileGenerator();
    }

	private static void parseFile() throws FileNotFoundException,
			ParseException, IOException {
		for(int i = 0; i<loc.size();i++){
            Class c = loc.get(i);
            FileInputStream in = new FileInputStream(c.getPath());
            CompilationUnit cu;
            try {
                // parse the file
                cu = JavaParser.parse(in);
            } finally {
                in.close();
            }
            c.setPackage(cu.getPackage().getName().toString());
            parseMethods(c, cu);
            parseFields(c, cu);
            new ClassOrInterfaceVisitor().visit(cu, null);
            c.addAllExtend(loe);
            loe.clear();
            c.addAllImplement(loi);
            loi.clear();
        }
		findRelation();
		
        // print for debug
        for(Class c: loc){
        	c.print();
        }
	}

	private static void parseFields(Class c, CompilationUnit cu) {
		// visit the fields
		new FieldVisitor().visit(cu, null);
		for(Field f: lof)
			f.setClass(c);
		c.addAllFields(lof);
		lof.clear();
	}

	private static void parseMethods(Class c, CompilationUnit cu) {
		// visit the methods
		new MethodVisitor().visit(cu, null);
		for(Method m: lom){
			m.setClass(c);
		}
		c.addAllMethod(lom);
		lom.clear();
	}
    
    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitor extends VoidVisitorAdapter {
        
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this
            // CompilationUnit, including inner class methods
        	Method m = new Method(n.getName());
            
            if(n.getParameters()!=null){
            	List<Parameter> para = n.getParameters();
            	m.setParameters(para);
            }
            m.setReturnType(n.getType().toString());
            lom.add(m);
        }
    }
    
    // visit FieldDeclaration nodes
    private static class FieldVisitor extends VoidVisitorAdapter{
    	
    	public void visit(FieldDeclaration n, Object arg){
    		List<VariableDeclarator> vd = n.getVariables();
    		if(vd.get(0).getInit() != null){
    			lof.add(new Field(vd.get(0).getId().toString(), n.getType().toString(),
    				vd.get(0).getInit().toString()));
    		}
    		else lof.add(new Field(vd.get(0).getId().toString(), n.getType().toString(),
    				"null"));
    	}
    }
    
 // visit FieldDeclaration nodes
    private static class ClassOrInterfaceVisitor extends VoidVisitorAdapter{
    	
    	public void visit(ClassOrInterfaceDeclaration n, Object arg){
	    		if(n.getExtends()!=null)
		    		for(ClassOrInterfaceType t :n.getExtends()){
		    			loe.add(t.getName());
		    		}
	    		if(n.getImplements()!=null)
		    		for(ClassOrInterfaceType t : n.getImplements()){
		    			loi.add(t.getName());
		    		}
    	}
    }
    
    // given a root directory of a project, produce the file paths of all the java file
    public static void searchJava(String path){
        
        File dir = new File(path);
        List<File> directories = new ArrayList<File>();
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
    
    public static void fileGenerator() throws FileNotFoundException, UnsupportedEncodingException{
    	PrintWriter writer = new PrintWriter("base.dot", "UTF-8");
    	writer.println("digraph G {\n"+
    					"fontname = \"Bitstream Vera Sans\"\n"+
    					"fontsize = 8\n"+
    					"node [\n"+
    					"fontname = \"Bitstream Vera Sans\"\n"+
    					"fontsize = 8\n"+
    					"shape = \"record\"\n"+
    					"]\n"+
    					"edge [\n"+
    					"fontname = \"Bitstream Vera Sans\"\n"+
    					"fontsize = 8\n"+
        	"]");
    	for(Class n: loc){
	    	List<Field> f = n.getFields();
	    	List<Method> m = n.getMethods();
	    	String fields="|", methods = "|";
	    	for(Field field: f){
	    		String fieldName = field.getName();
	    		String fieldType = field.getType();
	    		fieldType = fieldType.replaceAll("<", "&lt;");
	    		fieldType = fieldType.replaceAll(">", "&gt;");
	    		fields = fields+"- "+fieldName+" : "+fieldType+"\\l";
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
	    		methods = methods+"+ "+methodName+"("+parameter+") : "+returnType+"\\l";
	    	}
	    	writer.println(n.getName()+"[\nlabel = \"{"+n.getName()+fields+methods+"}\"\n]");
	    	
	    	ArrayList<String> parentClasses = (ArrayList<String>) n.getParentClass();
	    	for(String p:parentClasses){
	    		String a = n.getName()+"->"+p;
	    		if(findClass(p)==null){
	    			writer.println(p+"[\nlable = \"{"+p+"}\"\n]");
	    		}
	    		writer.println( "edge [\narrowhead = \"empty\" \nheadlabel=\"\"\n5]\n"+a+"\n");
	    		
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
     * 
     */
    public static Class findClass(String name){
    	for(Class c : loc){
    		if(c.getName() == name)
    			return c;
    	}
    	return null;
    }
    
}
