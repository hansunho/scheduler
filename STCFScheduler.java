import java.io.*;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Comparator;
/** STCF scheduling framework
 *  @author Sam Knepper
 *  @version 10/19/2016
 */

class Simulator {

     private static PriorityQueue <Job> jobSet
     = new PriorityQueue <Job>(100, new Comparator<Job>(){
         @Override
         public int compare(Job j1, Job j2) {
                if( j1.dynamicArrivalTime != j2.dynamicArrivalTime ) {
                  return j1.dynamicArrivalTime - j2.dynamicArrivalTime ;
                }else{
                  return j1.timeToCompletion - j2.timeToCompletion ;
                }
              }
      }
     );

     static void schedule( Job j ) {
       jobSet.add( j ) ;
     }

     static LinkedList<Integer> run() {
          LinkedList <Integer> turnaroundTimes
          		= new LinkedList <>();
          int time = 0 ;
          while (!jobSet.isEmpty()) {
                Job currentJob = jobSet.poll() ;
                if( time < currentJob.arrivalTime ){
                  time = currentJob.arrivalTime ; 
                }
                if( currentJob.firstTimeScheduled < 0 ){ //first time currentJob has been scheduled
                    currentJob.firstTimeScheduled = time ;
                }
                time++ ;
                currentJob.timeToCompletion =
                        currentJob.timeToCompletion - 1 ;

                if( currentJob.timeToCompletion < 1 ){ //currentJob is complete
                  turnaroundTimes.add(time - currentJob.arrivalTime) ;
                  /*if( !jobSet.isEmpty() ){
                      time = jobSet.peek().arrivalTime ;
                  }*/
                }else if( !jobSet.isEmpty() ){
                    if( (jobSet.peek().arrivalTime == time) &&
                             (jobSet.peek().timeToCompletion <
                              currentJob.timeToCompletion) ){
                                  Job j = currentJob ;
                                  j.dynamicArrivalTime = time ;
                                  jobSet.add( j ) ;
                    }else{ //currentJob is still the job to be scheduled
                      jobSet.add( currentJob ) ;
                    }
                }else{
                  jobSet.add( currentJob ) ;
                }
          }
          return turnaroundTimes ;
      }
}

class Errors {
   	private static int errorCount = 0;

   	static void fatal( String message ) {
   		System.err.println( "Fatal error: " + message );
   		System.exit( 1 );
   	}
   	static void warning( String message ) {
   		System.err.println( "Error: " + message );
   		errorCount = errorCount + 1;
   	}
   	static boolean anyErrors() {
   		return errorCount > 0;
   	}
}

class ScanCheck {

  public interface ErrorMessage {
  		abstract String myString();
  }

  static int nextInt( Scanner sc, ErrorMessage message ) {
  		if (sc.hasNextInt()) {
  			return sc.nextInt();
  		} else {
  			Errors.warning(
  				message.myString() +
  				" -- expected an integer"
  			);
  			return 99;
  		}
  	}

}

class Job {

    public int arrivalTime;
    public int dynamicArrivalTime;
    public int timeToCompletion;
    public int firstTimeScheduled = -1 ;

    public Job( Scanner sc ) {
      arrivalTime = ScanCheck.nextInt(
  			            sc,
  			            () -> Job.this.toString()
      );
      timeToCompletion = ScanCheck.nextInt(
  			            sc,
  			            () -> Job.this.toString()
      );
      dynamicArrivalTime = arrivalTime; //we need to a dynamic arrival time for priority queue
      this.entryJob();
    }

    void entryJob() {
  		Simulator.schedule( this );
  	}
}


public class STCFScheduler {

    static int numberOfJobs = 0 ;
    static LinkedList <Job> jobs
    		= new LinkedList <> ();

    static void startScheduling( Scanner sc ){

      if( sc.hasNext() )
        numberOfJobs = sc.nextInt(); // first N in file

      while( sc.hasNext() ) {
        jobs.add( new Job(sc) );
      }
    }

    public static void main(String [] args) {

        // The name of the file to open.
        String fileName = "job.info.stcf";
        double averageTurnaroundTime = 0 ;
        double averageResponseTime = 0 ;

        try {
            startScheduling( new Scanner(new File(fileName)) );
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" +
                fileName + "'");
        }

        LinkedList<Integer> turnaroundTimes = Simulator.run();

        for( int tTime : turnaroundTimes ){
            averageTurnaroundTime += (double)tTime ;
        }
        averageTurnaroundTime = averageTurnaroundTime / numberOfJobs ;
        System.out.println( String.format( "%.5f", averageTurnaroundTime) + "\n" ) ;

        for( Job j : jobs ){
            averageResponseTime += (double)(j.firstTimeScheduled - j.arrivalTime) ;
        }
        averageResponseTime = averageResponseTime / (double)numberOfJobs ;
        System.out.println( String.format( "%.5f", averageResponseTime) + "\n" ) ;
    }

}
