import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Random;

public class testGenerator {

	public static void main (String [] args){
		int numJobs = -1;
		System.out.print("number of test files to generate: ");
		
		 Scanner sc = new Scanner(System.in);
		 numJobs = sc.nextInt();
		 sc.close();
		  while(numJobs > 0){
			  generateTestFile(numJobs);
			  numJobs--;
		  }
		   
		
	}
	
	private static void generateTestFile(int fileNum){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("Test-"+fileNum+".txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Random random = new Random();
		int max = 100;
		int min = 1;
		int arrivalTime,executionTime;
		int numProcesses=random.nextInt(max - min + 1) + min;
		
		writer.println(numProcesses);
		writer.println("\n");
		
		
		while(numProcesses>0){

			arrivalTime=random.nextInt(101);//pick 0~100 (inclusive)
			executionTime=random.nextInt(100) + 1;//pick 1~100 (inclusive)
			writer.println(arrivalTime);
			writer.println(executionTime);
			writer.println("\n");			
			numProcesses--;
		}
		
		writer.close();
	}
	
}
