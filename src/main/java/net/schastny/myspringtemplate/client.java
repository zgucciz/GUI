package net.schastny.myspringtemplate;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.*;
import org.springframework.web.client.RestTemplate;
import fileSystem.Folder;
import fileSystem.MyFile;


public class client {
	
	private static Folder f = null;
	private static String path = "http://localhost:8080/root?path=";
	private static Table table;
//	private static final int shellIcon = 0, iconClosedDrive = 1, iconClosedFolder = 2,
//		      iconFile = 3, iconOpenDrive = 4, iconOpenFolder = 5, cmdCopy = 6,
//		      cmdCut = 7, cmdDelete = 8, cmdParent = 9, cmdPaste = 10,
//		      cmdPrint = 11, cmdRefresh = 12, cmdRename = 13, cmdSearch = 14;
//
//	private static final String[] stockImageLocations = { "images/pin.png",
//		      "images/OPdatabase.png", "images/OpFolder.png", "images/File.png",
//		      "images/database.png", "images/Folder.png", "images/Copy.png",
//		      "images/cut.png", "images/Delete.png", "images/goback.png", "images/Paste.png",
//		      "images/Print.png", "images/refresh.png", "images/rename.png",
//		      "images/Search.png" };

	
	private static void createTable(final Shell parent) {
		
//	    table = new Table(parent,SWT.SINGLE);
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);

	    createTableColumn(SWT.CENTER,   "Name", 500);
	    createTableColumn(SWT.CENTER, "isHidden", 200);
	    
	    addTableContents();
	    parent.pack();

	    table.addMouseListener(new MouseListener(){

	    	public void mouseDown(MouseEvent e)
			{
//				System.out.println("Mouse Down.");
			}
			public void mouseUp(MouseEvent e)
			{
//				System.out.println("Mouse Up.");
			}
			public void mouseDoubleClick(MouseEvent e){
			if(table.getSelectionIndex() != -1)
				if(table.getItem(table.getSelectionIndex()).getData().getClass().equals(Folder.class)){
			    	RestTemplate restTemplate = new RestTemplate();
		
			    	path+="/";
			    	TableItem item = table.getItem(table.getSelectionIndex());
			        	path += item.getText();

					f =(Folder) restTemplate.getForObject(path, Folder.class);	
					
					System.out.println(f.getName());
					
					addTableContents();

				}
			}
            
	    });
	}
	protected static TableColumn createTableColumn
	(int style, String title, int width) {
	    TableColumn tc = new TableColumn(table, style);
	    tc.setText(title);
	    tc.setResizable(true);
	    tc.setWidth(width);
	    return tc;
	}
	protected static void addTableContents() {
		for(TableItem i: table.getItems())
			i.dispose();
		ArrayList<Folder> items = f.getSubfolders();
	    for (Folder i : items) {
	        TableItem ti = new TableItem(table, SWT.PUSH);
	        ti.setData(i);
	        ti.setText(0,i.getName());
	        ti.setText(1,Boolean.toString(i.getIsHidden()));
	        ti.setImage(new Image(null,new ImageData("images/Folder.png")));

	    }
	    
	    for (MyFile i : f.getSubfiles()) {
	        TableItem ti = new TableItem(table, SWT.NONE);
	        ti.setData(i);
	        ti.setText(0,i.getName());
	        ti.setText(1,Boolean.toString(i.getIsHidden()));
	        ti.setImage(new Image(null,new ImageData("images/File.png")));

	    }
	}
	  
	// примера использования
	protected static void initGui(Shell shell) {
		Composite pan = new Composite(shell,SWT.PUSH);
		table = new Table(shell,SWT.PUSH);
		pan.setSize(50, 50);
		
		
		Button but = new Button(pan, SWT.UP);
		but.setSize(55, 40);
		but.setImage(new Image(null,new ImageData("images/goBack2.png")));
		but.addSelectionListener(
	            new org.eclipse.swt.events.SelectionAdapter() {
	                public void widgetSelected(
	                        org.eclipse.swt.events.SelectionEvent e) {
	                	if(!path.equals("http://localhost:8080/root?path=")){
	                		path = path.substring(0,path.lastIndexOf('/'));
	                		System.out.println(path);
	                		RestTemplate restTemplate = new RestTemplate();
	                		f =(Folder) restTemplate.getForObject(path, Folder.class);
	                		addTableContents();
	                	}
	                }
	            });
					
	    createTable(shell);
	}
	
	
	public static void main(String[] args){

		RestTemplate restTemplate = new RestTemplate();

		try{
		f =(Folder) restTemplate.getForObject("http://localhost:8080/root", Folder.class);
		System.out.println(f.getName());
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e, "ERROR!", 0);
		}
		
		//эээээксперементыыы:
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Shell Title");
		shell.setImage(new Image(null,new ImageData("images/pic.png")));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);

		initGui(shell);

	    shell.open(); 
		// process all user input events
		while (!shell.isDisposed()) {
		      if (!display.readAndDispatch()) {
		        display.sleep();
		      }
		    }
		display.dispose();  // must always clean up
	}

}
