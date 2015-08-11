package net.schastny.myspringtemplate;

import javax.swing.JOptionPane;

import org.springframework.web.client.RestTemplate;
import fileSystem.Folder;


public class client {
	
	public static void main(String[] args){
		RestTemplate restTemplate = new RestTemplate();
		Folder f;
		try{
		f =(Folder) restTemplate.getForObject("http://localhost:8080/root", Folder.class);
		System.out.println(f.getName());
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e, "ERROR!", 0);
		}
	}

}
