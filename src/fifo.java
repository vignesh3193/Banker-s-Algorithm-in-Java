
import java.util.*;

public class fifo {

	public static void FIFO(ArrayList<ArrayList<String>> task_inputs)
	{
		int[] resource_units=new int[bankers.no_of_resources];
		int tofree[]=new int [bankers.no_of_resources];
		for(int i=0;i<resource_units.length;i++)
		{
			tofree[i]=0;
			resource_units[i]=bankers.resource_units[i];	
		}

		int N=task_inputs.size();
		int[][] resources_granted=new int [N][bankers.no_of_resources];

		ArrayList<Integer> blocked=new ArrayList<Integer>();
		int input_line[]=new int[N];
		int delay[]=new int[N];
		int isblocked[]=new int[N];
		int isterminated[]=new int[N];
		int start_time[]=new int[N];
		int finish_time[]=new int[N];
		int wait_time[]=new int[N];
		int total_time=0;
		int total_wait_time=0;
		int no_of_blocked_tasks=0;
		int no_of_active_tasks=0;
		int no_of_terminated_tasks=0;

		boolean delaybit[]=new boolean[N];
		boolean ran_this_cycle[]=new boolean[N];

		//--------------------------------------------------initialization loop----------------------------------------
		for(int i=0;i<N;i++)
		{
			isterminated[i]=0;
			delay[i]=0;
			isblocked[i]=0;
			wait_time[i]=0;
			start_time[i]=-1;
			finish_time[i]=-1;
			delaybit[i]=false;
			ran_this_cycle[i]=false;
		}


		int cycle=0;


		//----------------------------------------------------main loop-----------------------------------------------------------------------------
		while(no_of_terminated_tasks<N)
		{
			//System.out.println("------CYCLE "+cycle+"-----------");

			//System.out.println("respurces left "+resource_units[0]);


			
			//-----------------------------------handling blocked processes-----------------------------------------------------------------------------
			int count=0; int n=blocked.size();
			while(!blocked.isEmpty()&&count<blocked.size())
			{	//System.out.println("showing blocked process "+(blocked.get(y)+1) );
				int t=blocked.get(count);

				String[] blocked_line_array=task_inputs.get(t).get(input_line[t]).split(" ");
				//System.out.println("left: "+(resource_units[Integer.parseInt(blocked_line_array[3])-1])+"req: "+blocked_line_array[4]);
				if(resource_units[Integer.parseInt(blocked_line_array[3])-1]>=Integer.parseInt(blocked_line_array[4]))
				{
					//System.out.println("Task "+(t+1)+" completes its task");
					isblocked[t]=0;
					no_of_blocked_tasks--;
					blocked.remove((Object)t);
					ran_this_cycle[t]=true;
					resource_units[Integer.parseInt(blocked_line_array[3])-1]-=Integer.parseInt(blocked_line_array[4]);
					resources_granted[t][Integer.parseInt(blocked_line_array[3])-1]+=Integer.parseInt(blocked_line_array[4]);
				}
				else
				{
					//System.out.println("task "+(t+1)+" not granted");
					ran_this_cycle[t]=true;
					input_line[t]--;
					wait_time[t]++;
					count++;
				}

			}

			//----------------------------------------------for each process per cycle------------------------------------------------------
			for(int i=0;i<N;i++)
			{
				//System.out.println("resources       "+resource_units[0]);
				if(input_line[i]<task_inputs.get(i).size()&&isterminated[i]==0)
				{
					String[] line_array=task_inputs.get(i).get(input_line[i]).split(" ");
					String[] next_line={""};
					if(input_line[i]+1<task_inputs.get(i).size())
					{
						next_line=task_inputs.get(i).get(input_line[i]+1).split(" ");
					}

					//---------------------------------------------------setting delays--------------------------------------------------------------
					if(delaybit[i]==false)
					{
						delay[i]=Integer.parseInt(line_array[2]);
						delaybit[i]=true;
					}

					if(delay[i]==0)
					{

						input_line[i]++;
						if(ran_this_cycle[i]==false)
						{
							delaybit[i]=false;



							//-------------------------------------------task initiation----------------------------------------------------
							if(line_array[0].equals("initiate"))
							{
								if(Integer.parseInt(line_array[3])>bankers.no_of_resources)
								{
									System.out.println("error, resource doesnt exist");
								}
								else
								{
									if(start_time[i]==-1)
									{//System.out.println(line_array[1]+" initiated");
										start_time[i]=cycle;

									}
									no_of_active_tasks++;
								}
							}


							//-----------------------------------------handling resource requests by processes----------------------------
							if(line_array[0].equals("request"))
							{

								if(Integer.parseInt(line_array[3])<=bankers.no_of_resources)
								{
									if(resource_units[Integer.parseInt(line_array[3])-1]>=Integer.parseInt(line_array[4]))
									{	
										//System.out.println(line_array[1]+" request granted "+ Integer.parseInt(line_array[4]));
										resource_units[Integer.parseInt(line_array[3])-1]-=Integer.parseInt(line_array[4]);
										resources_granted[i][Integer.parseInt(line_array[3])-1]+=Integer.parseInt(line_array[4]);
									}
									else
									{
										//System.out.println(i+1+" not enought resources, process blocked "+Integer.parseInt(line_array[4]));

										blocked.add(i);
										no_of_blocked_tasks++;

										isblocked[i]=1;
										input_line[i]--;
										wait_time[i]++;
									}
								}
								else
								{
									System.out.println("invalid resource requested");
								}

							}


							//------------------------------------adding resources to tofree list----------------------------------------------------------
							if(line_array[0].equals("release"))
							{
								//System.out.println(line_array[1]+" successfully released " +Integer.parseInt(line_array[4]));
								tofree[Integer.parseInt(line_array[3])-1]+=Integer.parseInt(line_array[4]);
								resources_granted[i][Integer.parseInt(line_array[3])-1]-=Integer.parseInt(line_array[4]);

							}


							//-----------------------------------checking terminating tasks------------------------------------
							if(next_line[0].equals("terminate"))
							{
								if(Integer.parseInt(next_line[2])==0)
								{
									isterminated[i]=1;
									//System.out.println(i+1+" terminated");
									no_of_terminated_tasks++;
									finish_time[i]=cycle;

									for(int j=0;j<bankers.no_of_resources;j++)
									{
										tofree[j]+=resources_granted[i][j];
									}

								}
							}

						}
					}
					//---------------------------------------if process is delayed--------------------------------------------------------
					else
					{


						delay[i]--;
						if(line_array[0].equals("terminate")&&delay[i]==0)
						{

							isterminated[i]=1;
							//System.out.println(i+1+" is delayed and terminated");
							no_of_terminated_tasks++;
							finish_time[i]=cycle;

							for(int j=0;j<bankers.no_of_resources;j++)
							{
								tofree[j]+=resources_granted[i][j];
							}


						}
						else
						{
							//System.out.println(i+1+" delayed "+(delay[i]+1));
						}


					}
				}
			}

			//-------------------------returning freed resources for next cycle----------------------------------------------------------
			for(int i=0;i<bankers.no_of_resources;i++)
			{
				resource_units[i]+=tofree[i];
				tofree[i]=0;
			}

			//--------------------------------breaking deadlock-------------------------------------------------------------------------------			
			if(no_of_blocked_tasks==no_of_active_tasks)
			{
				boolean deadlock=true;
				while(deadlock==true)
				{
					for(int i=0;i<N;i++)
					{
						if(isblocked[i]==1&&isterminated[i]==0)
						{
							isterminated[i]=1;

							System.out.println("task "+(i+1)+" aborted (deadlock)");

							no_of_terminated_tasks++;
							no_of_active_tasks--;
							no_of_blocked_tasks--;
							for(int j=0;j<bankers.no_of_resources;j++)
							{
								resource_units[j]+=resources_granted[i][j];
								isblocked[i]=0;
								blocked.remove((Object)i);

								no_of_blocked_tasks--;
							}
							break;
						}
					}

					for(int i=0;i<N;i++)
					{
						if(isterminated[i]==0)
						{	
							String[] line_array=task_inputs.get(i).get(input_line[i]).split(" ");
							if(resource_units[Integer.parseInt(line_array[3])-1]>=Integer.parseInt(line_array[4]))
							{
								deadlock=false;
								isblocked[i]=0;
								blocked.remove((Object)i);
								no_of_blocked_tasks--;
							}
						}
					}
				}
			}

			no_of_active_tasks=N-no_of_terminated_tasks;
			for(int i=0;i<N;i++)
			{
				ran_this_cycle[i]=false;
			}

			//System.out.println("end of cycle");

			cycle++;
		}
		System.out.println("---------------- FIFO ----------------------------           ");
		for(int i=0;i<N;i++){

			if(finish_time[i]==-1)
			{
				System.out.println("TASK "+(i+1)+"   aborted");
			}
			else
			{
				System.out.print("TASK "+(i+1)+"   "+ (finish_time[i]-start_time[i]+1)+ "   "+ (wait_time[i])+"   "+(((double)wait_time[i]/(double)(finish_time[i]-start_time[i]+1)*100))+"%");
				System.out.println();
				total_wait_time+=(wait_time[i]);
				total_time+=(finish_time[i]-start_time[i]+1);
			}
		}

		System.out.print("TOTAL "+"    "+ (total_time)+ "   "+ (total_wait_time)+"   "+(((double)total_wait_time/(double)total_time)*100)+'%');
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();

	}

}
