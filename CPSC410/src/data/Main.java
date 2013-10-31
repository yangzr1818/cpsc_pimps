package data;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// reinforced implementation
public class Main {
	
	private static List<Class> loc = new ArrayList<Class>();
	private static List<Method> lom = new ArrayList<Method>();
//	private static List<String> filePaths = new ArrayList<String>();
//	private static List<String>	classes = new ArrayList<String>();
//	
//	private static Map<String, List<String>> m = new HashMap<String, List<String>>();
//	private static List<HashMap> classesAndMethods = new ArrayList<HashMap>();
//	private static List<String> methods = new ArrayList<String>();
	
	
    
    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        
        // path may vary
        searchJava("D:\\assignment\\CPSC\\410\\Pacman");    
        
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
            
            // visit and print the methods names
            new MethodVisitor().visit(cu, null);
            
            
            Map<String, List<String>> tempM = new HashMap<String, List<String>>();
            List<String> tempMethods = new ArrayList<String>();
            for(Method m: lom){
            	m.setClass(c);
            }
            c.addAllMethod(lom);
//            
//            tempMethods.addAll(methods);
//            tempM.put(classes.get(i), tempMethods);
//            classesAndMethods.add((HashMap<String, List<String>>) tempM);
//            
//            methods.clear();
            lom.clear();
        }
        
        for(Class c: loc){
        	c.print();
        }
//        System.out.println(classesAndMethods.get(0).keySet());
//        System.out.println(classesAndMethods.get(0).get("Board"));
        
        
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
//            methods.add(n.getName());
            lom.add(new Method(n.getName()));
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
//                        filePaths.add(f.getPath());
//                        classes.add(EliminateDotJava(f.getName()));
                        loc.add(new Class(EliminateDotJava(f.getName()),f.getPath()));
                    }
            }
            directories.clear();
            directories.addAll(subDirectories);
        }
        
    }
    
    public static String EliminateDotJava(String s){
        int dotIndex=0;
        for(int i=0; i<s.length();i++){
            if(s.charAt(i) == '.'){
                dotIndex=i;
            }
            
        }
        return s.substring(0, dotIndex);
    }
    
}