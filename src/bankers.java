import java.io.*;
import java.util.*;

public class bankers {

	static int[] resource_units;
	static int no_of_resources;
	
	
	public static void main(String[] args)throws IOException {
		// TODO Auto-generated method stub
		
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter input file name");
		String file_name=sc.nextLine();
		sc.close();
		
		//String file_name="input.txt";
		File file=new File(file_name);
		
		ArrayList<ArrayList<String>> task_inputs=load_processes(file);
		fifo.FIFO(task_inputs);
		banker.BANKER(task_inputs);
	}

	//------------------------------------------------------reading input file and loading data---------------------------------------------
	static ArrayList<ArrayList<String>> load_processes(File file)throws IOException
	{
		Scanner sc=new Scanner(file);
		
		int no_of_tasks=sc.nextInt();
		no_of_resources=sc.nextInt();
		resource_units=new int[no_of_resources];
		
		ArrayList<ArrayList<String>> task_inputs=new ArrayList<ArrayList<String>>();
		
		for(int i=0;i<no_of_resources;i++)
		{
			resource_units[i]=sc.nextInt();
		}
	
		for(int i=0;i<no_of_tasks;i++)
		{
			task_inputs.add(new ArrayList<String>());
		}
		
		int count=1;;
		ArrayList<String> temp=new ArrayList<String>();
		while(sc.hasNext())
		{
		
			String line="";
			line+=sc.next()+" ";		
			String curr_task=sc.next();
			line+=curr_task+" "; 			
			line+=sc.next()+" ";
			line+=sc.next()+" ";
			line+=sc.next()+" ";
			task_inputs.get(Integer.parseInt(curr_task)-1).add(line);
			//System.out.println(line);
			//temp.add(line);
			
			
		}
		//task_inputs.add(temp);
		
		/*for(int j=0;j<task_inputs.size();j++)
		{
			System.out.println("taskt "+j);
			for(int i=0;i<task_inputs.get(j).size();i++)
			{
				System.out.println(task_inputs.get(j).get(i));
			}
			System.out.println();
		}*/
		return task_inputs;
	}
	
}
