package data;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileParser {

	private List<Class> loc = new ArrayList<Class>();  // list of Class
	private List<Method> lom = new ArrayList<Method>();// list of Method
	private List<Field> lof = new ArrayList<Field>();  // list of Field
	private List<String> loe = new ArrayList<String>();// list of extends
	private List<String> loi = new ArrayList<String>();// list of interfaces
	

	public FileParser(List<Class> loc){
		this.loc.addAll(loc);
	}
	
	/**
	 * For each class, get its required information, etc. methods, fields
	 * @throws FileNotFoundException
	 * @throws ParseException
	 * @throws IOException
	 */
	public void parseFile() throws FileNotFoundException,
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
	
	/**
	 * 
	 * @param c
	 * @param cu
	 */
	private void parseFields(Class c, CompilationUnit cu) {
		// visit the fields
		new FieldVisitor().visit(cu, null);
		for(Field f: lof)
			f.setClass(c);
		c.addAllFields(lof);
		lof.clear();
	}

	/**
	 * 
	 * @param c
	 * @param cu
	 */
	private void parseMethods(Class c, CompilationUnit cu) {
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
	private class MethodVisitor extends VoidVisitorAdapter {

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

	/**
	 *  visit FieldDeclaration nodes
	 *
	 */
	private class FieldVisitor extends VoidVisitorAdapter{

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

	/**
	 *  visit FieldDeclaration nodes
	 *
	 */
	private class ClassOrInterfaceVisitor extends VoidVisitorAdapter{

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
	
	/**
     * Find the relationship between classes, i.e. one-to-one, one-to-many
     */
    public void findRelation(){
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
      * remove the '<' and '>' symbols from a string
      * @param s string
      * @return the string with '<' and '>' removed
      */
   
    public String eliminateBracket(String s){
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
