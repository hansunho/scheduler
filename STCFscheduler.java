import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class STCFscheduler {

	 static int numberOfProcesses;//N
	 //processes array will have a 2D array size of [N][4] 
	 //[][0] for arrival time, [][1] for execution time, [][2] for first run time
	 //, and [][3] for completion time	
	 static int[][] processes; 
	 static int timeCount = 0;//keeps track of current time
	 static double averageTurnaroundTime = 0;
	 static double averageResponseTime = 0;		
	 static int numOfJobDone = 0;
	public static void readFile (String filePath) throws IOException{
		int i = 0;
		FileInputStream fis = new FileInputStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		
		numberOfProcesses = Integer.parseInt(br.readLine());
		processes = new int[numberOfProcesses][4];
		while ((line = br.readLine()) != null) {
			if(!line.trim().isEmpty()) {
				processes[i][0] = Integer.parseInt(line);
				line = br.readLine();
				processes[i][1] = Integer.parseInt(line);
				processes[i][2] = -1;
				i++;
			}
		}
		br.close();
	}

	//stable sorting algorithm from java library.
	public static void sortProcesses (){
		java.util.Arrays.sort(processes, new java.util.Comparator<int[]>() {
	    public int compare(int[] a, int[] b) {
	        return Integer.compare(a[0], b[0]);
	    }
	});
	}
		

	public static void calculateAvgTimes (){
	for (int i = 0; i < numberOfProcesses; i++){
		averageTurnaroundTime += processes[i][3] - processes[i][0];
		averageResponseTime += processes[i][2] - processes[i][0];
	}
	averageTurnaroundTime = averageTurnaroundTime/numberOfProcesses;
	averageResponseTime = averageResponseTime/numberOfProcesses;
	}

	
	
	
	//will run a process at processes[n][]
	public static void runAProcess (int n){
	
		//if currently there is no process to run, warp time
	//if(timeCount<processes[n][0]) timeCount=processes[n][0];
		
		//set the process' first run time
		if (processes[n][2] == -1) processes[n][2] = timeCount;
		
	
		timeCount++;
		processes[n][1]--;
		//System.out.println("Job " + n +" has " + processes[n][1] + "left");
		
		//if current process is finished
		if(processes[n][1] == 0){
			//set the process' completion time
			processes[n][3] = timeCount;
			//finished process will have execution time of -1
			processes[n][1] = -1;		
			//update the number of total jobs done
			numOfJobDone++;
		}
	
		
	}
	
	// processes where their arrivalTime<currentTime 
	public static int findNextProcess(){
		int shortestProcessIndex = -1;
		int shortestRemainingExceutionTime = Integer.MAX_VALUE;
		int i = 0;
		while(i < numberOfProcesses && processes[i][0] <= timeCount ){
			if (processes[i][1] != -1){
				//if two processes have the same execution time remaining, pick one with lower arrival time
				if(processes[i][1] == shortestRemainingExceutionTime && processes[i][0] < processes[shortestProcessIndex][0])
					shortestProcessIndex = i;
				
				if (processes[i][1] < shortestRemainingExceutionTime) {
					shortestProcessIndex = i;
					shortestRemainingExceutionTime = processes[i][1];
				}
			}
			i++;
		}
		
		//if no process to run at the moment, update the current time and call findNexprocess again
		if(shortestProcessIndex == -1){
			timeCount++;
			return findNextProcess();
		}
		
		return shortestProcessIndex;
	}
	
	public static void main(String [] args){
	
		try	{
			readFile("C:\\Users\\sun\\workspace\\OSHW3\\src\\jobinfo.txt");
			//readFile(args[1]);//when its running from command line arguments
		}
		catch (IOException ex){
	    System.err.println("Caught IOException: " + ex.getMessage());
		}  	
		
		
		sortProcesses();
	//	for(int z = 0; z < numberOfProcesses; z++){
	//		System.out.println(z + "is " + processes[z][0] + ":" + processes[z][1]);
	//	}
		
		
		while(numOfJobDone<numberOfProcesses){
			//n = index of next process to run
			int n = findNextProcess();		
			runAProcess(n);
		}
		calculateAvgTimes();
		System.out.printf("%.5f",averageTurnaroundTime);
		System.out.println();
		System.out.printf("%.5f",averageResponseTime);
		System.out.println();
	}

}
