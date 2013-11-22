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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.perfspy.aspect.AbstractPerfSpyAspect;

// reinforced implementation
public class Main {

	private static FileParser fileParser;
	private static FileGenerator fileGenerator;
	private static String savedpath = "";

	public static void main(String[] args) throws Exception {
		// creates an input stream for the file to be parsed
		JFrame f = new JFrame();  


		//file chooser 
		final JFileChooser fc = new JFileChooser();

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		f.getContentPane().setBackground(Color.LIGHT_GRAY);

		((JComponent) f.getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		f.setBounds(100, 100, 450, 300);




		//text field for selected project directory
		final JTextField jf = new JTextField();
		jf.setForeground(Color.BLACK);
		jf.setBounds(16, 112, 255, 43);
		jf.setColumns(10);

		//add to content pane
		f.getContentPane().add(jf);

		//text field for current directory 
		JTextField jff = new JTextField();
		jff.setForeground(new Color(102, 51, 51));
		jff.setBackground(Color.LIGHT_GRAY);
		jff.setText("Current Directory");
		jff.setBounds(16, 89, 125, 28);
		jff.setColumns(10);
		jff.setEditable(false);
		jff.setBorder(null);
		//add to content pane
		f.getContentPane().add(jff);

		//create&add confirm button to content pane
		JButton btnNewButton_1 = new JButton("Confirm");

		//mouth click listener
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				savedpath = fc.getCurrentDirectory().getPath();
				if(savedpath == "")
					JOptionPane.showMessageDialog(null, 
							"The directory does not exist.","Error Occurred!",JOptionPane.ERROR_MESSAGE);
				else searchJava(savedpath);
				try {
					fileParser.parseFile();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, 
							"The directory does not exist.","Error Occurred!",JOptionPane.ERROR_MESSAGE);
				}
				try {
					fileGenerator.fileGenerator();
					JOptionPane.showMessageDialog(null, 
							"The .dot files have been generated succesfully.",
							"Message", JOptionPane.INFORMATION_MESSAGE );
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, 
							"File cannot be generated.","Error Occurred!",JOptionPane.ERROR_MESSAGE);
				} 
				PerfSpyDemoAsepct p = new PerfSpyDemoAsepct();
				p.withinCflowOps();
				p.cflowOps();
				
			}
		});


		btnNewButton_1.setBounds(283, 181, 116, 29);
		f.getContentPane().add(btnNewButton_1);


		//create & add cancel button to content pane
		JButton btnNewButton_2 = new JButton("Cancel");
		
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
                     System.exit(0);

			}
		});
		btnNewButton_2.setBounds(105, 181, 116, 29);
		f.getContentPane().add(btnNewButton_2);


		//button for select directory and corresponding file chooser
		final JButton btnNewButton = new JButton("Select directory");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		btnNewButton.setBounds(283, 115, 127, 38);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				fc.showOpenDialog(btnNewButton);

				String tempath = fc.getCurrentDirectory().getPath();
				jf.setText(tempath);

			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		//add to content pane
		f.getContentPane().add(btnNewButton); 
		f.getContentPane().setLayout(null);

		f.setSize(400,400);  
		f.setLocation(200,200);  
		f.setVisible(true); 
		// path may vary



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
				if(file.listFiles()==null){
					JOptionPane.showMessageDialog(null, 
							"The directory is empty.","Error Occurred!",JOptionPane.ERROR_MESSAGE);
				}
				else {for(File f: file.listFiles())
					if(f.isDirectory()){
						subDirectories.add(f);
					}
					else if (f.getName().endsWith((".java"))) {
						loc.add(new Class(eliminateDotJava(f.getName()),f.getPath()));
					}
				}
			}
			directories.clear();
			directories.addAll(subDirectories);
		}
		if(loc.isEmpty()){
			JOptionPane.showMessageDialog(null, 
					"The directory does not contain any java file.","Error Occurred!",JOptionPane.ERROR_MESSAGE);
		}
		else{
			fileParser = new FileParser(loc);
			fileGenerator = new FileGenerator(loc);
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
