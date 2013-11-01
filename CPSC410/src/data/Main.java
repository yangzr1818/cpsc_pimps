package data;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// reinforced implementation
public class Main {
	
	private static List<Class> loc = new ArrayList<Class>();
	private static List<Method> lom = new ArrayList<Method>();
	private static List<Field> lof = new ArrayList<Field>();
    
    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        
        // path may vary
        searchJava("D:\\assignment\\CPSC\\410\\Pacman");    
        
        parseFile();
        
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
            lom.add(new Method(n.getName()));
            //System.out.println();
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
}