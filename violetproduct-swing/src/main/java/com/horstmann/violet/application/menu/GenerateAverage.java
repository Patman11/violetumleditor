package com.horstmann.violet.application.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class GenerateAverage {
	private double avgOut, avgRec, avgSize, avgRel, avgCBO;
	private int numObj;
	private int flag;
	

	
	
	public GenerateAverage() {
		readFile("stats/classStats1.txt");
		createAverageWindow("stats/classStats1.txt");
	}
	
	public GenerateAverage(String path) {
		readFile(path);
		if (flag == 0)
		createAverageWindow(path);
	}
	
	private void readFile(String inFile) {
		BufferedReader br = null;
		
		JFrame frame = new JFrame();
		frame.setSize(200, 200);
		frame.getContentPane().setLayout(null);

		List<String> nameObjects = new ArrayList<String>();
		if (inFile.contains(".seq.")) {
			List<Integer> numOut = new ArrayList<Integer>();
			List<Integer> numRec = new ArrayList<Integer>();
		
			try {
				//File from sequence Diagram containing information
				int index = inFile.indexOf(".");
				String file = inFile.substring(0, index);
				br = new BufferedReader(new FileReader("stats/".concat(file.concat(".txt"))));
				
				String str;
				
				//read lines and put information into 3 different Lists
				while((str = br.readLine()) != null) {
					String tempName = str.substring(0, str.indexOf(":"));
				    nameObjects.add(tempName);
				    
				    String out = str.substring(str.indexOf(":"), str.indexOf(","));
				    String tempOut = out.substring(1);
				    int largeNum = Integer.parseInt(tempOut); 
				    	if (largeNum > 2) {
					JOptionPane.showMessageDialog(frame, "Large number of outgoing messages from an object ", "Error", JOptionPane.ERROR_MESSAGE);
					flag = 1;
				    }
				    
				    numOut.add(Integer.parseInt(tempOut));
				    
				    String rec = str.substring(str.indexOf(","));
				    String tempRec = rec.substring(1);
				    numRec.add(Integer.parseInt(tempRec));

				}
				// Total number of objects.
				numObj = nameObjects.size();
				
				// Get the average number of outgoing messages.
				avgOut = average(numOut, numObj);
				
				// Get the average number of receiving messages.
				avgRec = average(numRec, numObj);
			} catch (IOException e) {
	            e.printStackTrace();
			} finally {
				try {
					br.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (inFile.contains(".class.")) {
			List<Integer> numSize = new ArrayList<Integer>();
			List<Integer> numRel = new ArrayList<Integer>();
			List<Integer> numCBO = new ArrayList<Integer>();
			
			try {
				//File from class Diagram containing information
				int index = inFile.indexOf(".");
				String file = inFile.substring(0, index);
				br = new BufferedReader(new FileReader("stats/".concat(file.concat(".txt"))));
				
				String str;
				
				//read lines and put information into 4 different Lists
				while((str = br.readLine()) != null) {
					String tempName = str.substring(0, str.indexOf(":"));
				    nameObjects.add(tempName);
				    
				    String size = str.substring(str.indexOf(":"), str.indexOf(","));
				    String tempSize = size.substring(1);
				    numSize.add(Integer.parseInt(tempSize));
				    
				    String rel = str.substring(str.indexOf(","), str.indexOf(",")+2);
				    String tempRel = rel.substring(1);
				    numRel.add(Integer.parseInt(tempRel));
				    
				    String cbo = str.substring(str.lastIndexOf(","));
				    String tempCBO = cbo.substring(1);
				    int largeCBO = Integer.parseInt(tempCBO); 
			    	if (largeCBO > 5) {
			    		JOptionPane.showMessageDialog(frame, "High CBO", "Error", JOptionPane.ERROR_MESSAGE);
			    		flag = 1;}
				    numCBO.add(Integer.parseInt(tempCBO));
				    
				}
				// Get the number of classes.
				numObj = nameObjects.size();
				
				// Get the average number of functions.
				avgSize = average(numSize, numObj);
				
				// Get the average number of relationships.
				avgRel = average(numRel, numObj);
				
				// Get the average number of CBO.
				avgCBO = average(numCBO, numObj);
				
			} catch (IOException e) {
	            e.printStackTrace();
			} finally {
				try {
					br.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void createAverageWindow(String path) {
		
		JFrame frame;
		
		// Create the frame.
		if (path.contains(".class.")) {
			frame = new JFrame("Averages of the class diagram");
		} else if (path.contains(".seq.")) {
			frame = new JFrame("Averages of the sequence diagram");
		} else {
			frame = new JFrame();
		}

		Container c = frame.getContentPane();
		Dimension d = new Dimension(350, 200);
		
		DecimalFormat format = new DecimalFormat("0.0");
		
		JPanel panel = new JPanel();
		
		//if (avgCBO > 1) {
	//		JOptionPane.showMessageDialog(frame, "High CBO", "Error", JOptionPane.ERROR_MESSAGE);
		//}
		//
	//	if (avgOut > 1) {
	//		JOptionPane.showMessageDialog(frame, "â¦?	Large number of outgoing messages from an object ", "Error", JOptionPane.ERROR_MESSAGE);
	//	}
		
		
		//else {
		c.setPreferredSize(d);
		JLabel numObjs = new JLabel("Total number of objects: " + format.format(numObj));
		panel.add(numObjs, BorderLayout.CENTER);
		panel.add(Box.createRigidArea(new Dimension(0, 30)));
		
		if (path.contains(".seq.")) {
			
			JLabel avgOuts = new JLabel("Average number of outgoing messages per object: " + format.format(avgOut));
			panel.add(avgOuts, BorderLayout.CENTER);
			panel.add(Box.createRigidArea(new Dimension(0, 30)));
			
			JLabel avgRecs = new JLabel("Average number of receiving messages per object: " + format.format(avgRec));
			panel.add(avgRecs, BorderLayout.CENTER);
			panel.add(Box.createRigidArea(new Dimension(0, 30)));

		} else if (path.contains(".class.")) {
			
			JLabel avgFct = new JLabel("Average number of functions per class: " + format.format(avgSize));
			panel.add(avgFct, BorderLayout.CENTER);
			panel.add(Box.createRigidArea(new Dimension(0, 30)));
			
			JLabel avgRels = new JLabel("Average number of relationships per class: " + format.format(avgRel));
			panel.add(avgRels, BorderLayout.CENTER);
			panel.add(Box.createRigidArea(new Dimension(0, 30)));

			JLabel avgCBOs = new JLabel("Average number of CBO per class: " + format.format(avgCBO));
			panel.add(avgCBOs, BorderLayout.CENTER);
			panel.add(Box.createRigidArea(new Dimension(0, 30)));
		}
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		//}
	}
	
	private static double average(List<Integer> list, int size) {
		int total = 0;
		for (int i = 0; i < size; i++) {
			total += list.get(i);
		}
		return (double) total/size;
	}
	
	public double getAvgOut() {
		return avgOut;
	}
	
	public void setAvgOut(double avgOut) {
		this.avgOut = avgOut;
	}
	
	public double getAvgRec() {
		return avgRec;
	}
	
	public void setAvgRec(double avgRec) {
		this.avgRec = avgRec;
	}
	
	public double getAvgSize() {
		return avgSize;
	}
	
	public void setAvgSize(double size) {
		this.avgSize = size;
	}
	
	public double getAvgRel() {
		return avgRel;
	}
	
	public void setAvgRel(double relations) {
		this.avgRel = relations;
	}
	
	public double getAvgCBO() {
		return avgCBO;
	}
	
	public void setAvgCBO(double cbo) {
		this.avgCBO = cbo;
	}
	
	public int getNumObj() {
		return numObj;
	}
	
	public void setNumObj(int numObj) {
		this.numObj = numObj;
	}
}

//package com.horstmann.violet.application.menu;
//
//import java.awt.BorderLayout;
//import java.awt.Container;
//import java.awt.Dimension;
//import java.io.*;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.Box;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//
//public class GenerateAverage {
//	private double avgOut, avgRec, avgSize, avgRel, avgCBO;
//	private int numObj;
//	
//	public GenerateAverage() {
//		readFile("stats/classStats1.txt");
//		createAverageWindow("stats/classStats1.txt");
//	}
//	
//	public GenerateAverage(String path) {
//		readFile(path);
//		createAverageWindow(path);
//	}
//	
//	private void readFile(String inFile) {
//		BufferedReader br = null;
//
//		List<String> nameObjects = new ArrayList<String>();
//		if (inFile.contains(".seq.")) {
//			List<Integer> numOut = new ArrayList<Integer>();
//			List<Integer> numRec = new ArrayList<Integer>();
//		
//			try {
//				//File from sequence Diagram containing information
//				int index = inFile.indexOf(".");
//				String file = inFile.substring(0, index);
//				br = new BufferedReader(new FileReader("stats/".concat(file.concat(".txt"))));
//				
//				String str;
//				
//				//read lines and put information into 3 different Lists
//				while((str = br.readLine()) != null) {
//					String tempName = str.substring(0, str.indexOf(":"));
//				    nameObjects.add(tempName);
//				    
//				    String out = str.substring(str.indexOf(":"), str.indexOf(","));
//				    String tempOut = out.substring(1);
//				    numOut.add(Integer.parseInt(tempOut));
//				    
//				    String rec = str.substring(str.indexOf(","));
//				    String tempRec = rec.substring(1);
//				    numRec.add(Integer.parseInt(tempRec));
//				}
//				// Total number of objects.
//				numObj = nameObjects.size();
//				
//				// Get the average number of outgoing messages.
//				avgOut = average(numOut, numObj);
//				
//				// Get the average number of receiving messages.
//				avgRec = average(numRec, numObj);
//			} catch (IOException e) {
//	            e.printStackTrace();
//			} finally {
//				try {
//					br.close();
//				}
//				catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		} else if (inFile.contains(".class.")) {
//			List<Integer> numSize = new ArrayList<Integer>();
//			List<Integer> numRel = new ArrayList<Integer>();
//			List<Integer> numCBO = new ArrayList<Integer>();
//			
//			try {
//				//File from class Diagram containing information
//				int index = inFile.indexOf(".");
//				String file = inFile.substring(0, index);
//				br = new BufferedReader(new FileReader("stats/".concat(file.concat(".txt"))));
//				
//				String str;
//				
//				//read lines and put information into 4 different Lists
//				while((str = br.readLine()) != null) {
//					String tempName = str.substring(0, str.indexOf(":"));
//				    nameObjects.add(tempName);
//				    
//				    String size = str.substring(str.indexOf(":"), str.indexOf(","));
//				    String tempSize = size.substring(1);
//				    numSize.add(Integer.parseInt(tempSize));
//				    
//				    String rel = str.substring(str.indexOf(","), str.indexOf(",")+2);
//				    String tempRel = rel.substring(1);
//				    numRel.add(Integer.parseInt(tempRel));
//				    
//				    String cbo = str.substring(str.lastIndexOf(","));
//				    String tempCBO = cbo.substring(1);
//				    numCBO.add(Integer.parseInt(tempCBO));
//				    
//				}
//				// Get the number of classes.
//				numObj = nameObjects.size();
//				
//				// Get the average number of functions.
//				avgSize = average(numSize, numObj);
//				
//				// Get the average number of relationships.
//				avgRel = average(numRel, numObj);
//				
//				// Get the average number of CBO.
//				avgCBO = average(numCBO, numObj);
//				
//			} catch (IOException e) {
//	            e.printStackTrace();
//			} finally {
//				try {
//					br.close();
//				}
//				catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	
//	private void createAverageWindow(String path) {
//		
//		JFrame frame;
//		
//		// Create the frame.
//		if (path.contains(".class.")) {
//			frame = new JFrame("Averages of the class diagram");
//		} else if (path.contains(".seq.")) {
//			frame = new JFrame("Averages of the sequence diagram");
//		} else {
//			frame = new JFrame();
//		}
//
//		Container c = frame.getContentPane();
//		Dimension d = new Dimension(350, 200);
//		
//		DecimalFormat format = new DecimalFormat("0.0");
//		
//		JPanel panel = new JPanel();
//		
//		c.setPreferredSize(d);
//		JLabel numObjs = new JLabel("Total number of objects: " + format.format(numObj));
//		panel.add(numObjs, BorderLayout.CENTER);
//		panel.add(Box.createRigidArea(new Dimension(0, 30)));
//		
//		if (path.contains(".seq.")) {
//			JLabel avgOuts = new JLabel("Average number of outgoing messages per object: " + format.format(avgOut));
//			panel.add(avgOuts, BorderLayout.CENTER);
//			panel.add(Box.createRigidArea(new Dimension(0, 30)));
//			
//			JLabel avgRecs = new JLabel("Average number of receiving messages per object: " + format.format(avgRec));
//			panel.add(avgRecs, BorderLayout.CENTER);
//			panel.add(Box.createRigidArea(new Dimension(0, 30)));
//		} else if (path.contains(".class.")) {
//			JLabel avgFct = new JLabel("Average number of functions per class: " + format.format(avgSize));
//			panel.add(avgFct, BorderLayout.CENTER);
//			panel.add(Box.createRigidArea(new Dimension(0, 30)));
//			
//			JLabel avgRels = new JLabel("Average number of relationships per class: " + format.format(avgRel));
//			panel.add(avgRels, BorderLayout.CENTER);
//			panel.add(Box.createRigidArea(new Dimension(0, 30)));
//
//			JLabel avgCBOs = new JLabel("Average number of CBO per class: " + format.format(avgCBO));
//			panel.add(avgCBOs, BorderLayout.CENTER);
//			panel.add(Box.createRigidArea(new Dimension(0, 30)));
//		}
//		
//		frame.add(panel);
//		frame.pack();
//		frame.setVisible(true);
//	}
//	
//	private static double average(List<Integer> list, int size) {
//		int total = 0;
//		for (int i = 0; i < size; i++) {
//			total += list.get(i);
//		}
//		return (double) total/size;
//	}
//	
//	public double getAvgOut() {
//		return avgOut;
//	}
//	
//	public void setAvgOut(double avgOut) {
//		this.avgOut = avgOut;
//	}
//	
//	public double getAvgRec() {
//		return avgRec;
//	}
//	
//	public void setAvgRec(double avgRec) {
//		this.avgRec = avgRec;
//	}
//	
//	public double getAvgSize() {
//		return avgSize;
//	}
//	
//	public void setAvgSize(double size) {
//		this.avgSize = size;
//	}
//	
//	public double getAvgRel() {
//		return avgRel;
//	}
//	
//	public void setAvgRel(double relations) {
//		this.avgRel = relations;
//	}
//	
//	public double getAvgCBO() {
//		return avgCBO;
//	}
//	
//	public void setAvgCBO(double cbo) {
//		this.avgCBO = cbo;
//	}
//	
//	public int getNumObj() {
//		return numObj;
//	}
//	
//	public void setNumObj(int numObj) {
//		this.numObj = numObj;
//	}
//}
