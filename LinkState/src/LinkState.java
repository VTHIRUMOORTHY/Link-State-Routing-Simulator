import java.io.*;
import java.util.*;

public class LinkState 
{
	static int source;             // Source router
	static int destination;        // Destination Router
    static int infinite_path = 99; // Minimum value
	
public static void main(String[] args) throws IOException // Main function
{
	
   System.out.println("Enter file path");
   int choice = 0;
   @SuppressWarnings("resource")
   Scanner in = new Scanner(System.in);  // Call scanner class
   String fileName = in.nextLine();      // Get the input file path
   int nodes = number_nodes(fileName);   // Get the number of nodes from the file
   int [][] matrix = readFile(fileName, nodes); // Give Read file function as input to the original matrix
   int[][] new_matrix = mod_matrix(matrix, nodes); // Give the modified matrix as input to the new matrix(matrix appended with 99)
   int[][] distance = calculate_distance(new_matrix, nodes); // Distance Matrix calculation
   System.out.println("Select any one command");
   
 
   while(choice < 6)
   {
	   System.out.println("1. Input original network topology matrix data file");
       System.out.println("2. Connection Table");
       System.out.println("3. Shortest Path");
       System.out.println("4. Select Router to be removed");
       System.out.println("5. Exit CS542 Project. Goodbye!");
       System.out.println("Master Command:"); 
       choice = in.nextInt();
   
       switch(choice)
       {
  	   case 1:
  		 printArray(matrix, nodes); // Print the matrix
  		 break;
  	   case 2:
  		 System.out.println("Select a Source Router");
		 System.out.println("Source Router:");
		 source = in.nextInt(); // Source router input
		 System.out.println("Connection Table for the selected router");
		 connection_table(distance,nodes,source); // Function to calculate the connection table
		 break;
  	   case 3:
  		 if (source==0)   // Check if no source router is given as input
  		 {
  			 System.out.println("There is no source router. So please select command 2"); // If yes print the error message
  		     break;
  		 }
		 System.out.println("Select a Destination Router");
		 System.out.println("Destination Router:");
		 destination = in.nextInt(); // Get the destination router value
		 shortest_path(distance, new_matrix,source,destination, nodes); // function for calculating the shortest path
		 break;
  	   case 4:
  		 if (source==0)   // Check if no source router is given as input
  		 {
  			 System.out.println("Please select command 1"); // If yes, print the error message
  		     break;
  		 }
  	     System.out.println("Select a router to be removed:");
  	     int remove_node;      // node to be removed
  	     remove_node = in.nextInt(); // get the node to be removed as input
  	     int[][] mod_mat = remove_router(new_matrix, remove_node, nodes); // function used to remove the router
  	     int[][] mod_dist = calculate_distance(mod_mat, nodes);// distance calculation with the help of modified matrix
  	     System.out.println("The modified matrix after removing the router"); 
  	     printArray(mod_dist, nodes); // print the new matrix after removing the node
  	     System.out.println("The Connection Table after removing the router");
  	     connection_table(mod_dist,nodes,source); // function to calculate the connection table
  	     System.out.println("The Shortest path after removing the router");
  	     shortest_path(mod_dist, mod_mat,source, destination, nodes); // function to calculate the shortest path after removing the router
  	     break;
  	   case 5:
  		 System.out.println("Exit CS542 project. Good Bye!"); // Exit the project
  		 choice=6; // break the while loop
  		 break;
  		 
       }
   }



}

public static int[][] mod_matrix(int[][] matrix, int nodes) // Modified matrix after removing the router
{
	int[][] new_matrix = new int[nodes][nodes]; // initialize the new matrix used after removing the router
    for (int i = 0; i < nodes;i++)             // for loop for array1
    {
    	for (int j = 0 ; j<nodes;j++ )         // for loop for array2
    	{
    		if (matrix[i][j] == -1)            // check for entries having value equal to -1
    		{
    			new_matrix[i][j] = infinite_path;  // new matrix with values appended with 99
    		}
    		else
    		{
    			new_matrix[i][j] = matrix[i][j];  // if there is no -1 values then return the original matrix
    		}
    	}
    }
    return new_matrix;   // return the new matrix
}

public static int number_nodes(String fileName) throws IOException 
{
	int lineCount = 0;
	// File handling classes
	FileInputStream inputStream = new FileInputStream(fileName);       
    DataInputStream in = new DataInputStream(inputStream);
    BufferedReader bf = new BufferedReader(new InputStreamReader(in));
    @SuppressWarnings("unused")
	String line;
    while ((line = bf.readLine()) != null)   // check if the lines of the files are not equal to null
    {
        lineCount++;                   // Increment line count to traverse line by line
    }
    bf.close();               // close the buffer reader handle 
	return lineCount;         // return the line count 
}

public static void connection_table(int[][] distance, int nodes, int source) 
{
	int src = source-1;
	System.out.println("\n Router "+ source + " Connection Table");
	System.out.println("Destination   Interface");
	System.out.println("=======================");
	for (int dest = 0 ; dest < nodes;dest++)  
	{
		System.out.print(dest + 1);
		if(src == dest)     // check if the source and destination routers are the same
		{
			System.out.println("\t \t" + "-");	// If yes print the value as '-'
		}
		else if (distance[src][dest] == 0)     // check if distance value between the source and the destination is zero
		{
			System.out.println("\t \t" + (dest+1)); // If yes print the destination value itself(because source and destination are same)
		}
		else 
		{
			System.out.println("\t \t" + distance[src][dest]); // if the above condition fails, print the distance value
		}
	}
	
}

public static int[][] calculate_distance(int[][] new_matrix, int nodes) 
{
    int src;
    int middle;
    int dest;
	int[][] temp_matrix;
	temp_matrix = new int[nodes][nodes];
	int[][]temp_matrix2;
	temp_matrix2 = new int[nodes][nodes];
	for (src = 0; src < nodes; src++)        // for loop for source router
    {
        for (dest = 0; dest < nodes; dest++)  // for loop for destination router
        {
            temp_matrix[src][dest] = new_matrix[src][dest];  // assign the temporary matrix to the new matrix
        }
    }
	for (middle = 0; middle < nodes; middle++) // for loop for intermediate router between source and destination router
    {
    	
        for (src = 0; src < nodes; src++)  // for loop for source router
        {
        	 
            for (dest = 0; dest < nodes; dest++)  // for loop for destination router
            {
            	
                if ((temp_matrix[src][middle] + temp_matrix[middle][dest]) < temp_matrix[src][dest]) // check if sum of matrix(source and middle)(middle and destination) routers is less than matrix with source and destination
                {
                    temp_matrix[src][dest] = temp_matrix[src][middle]  
                                     + temp_matrix[middle][dest];  // if so assign the sum of matrices to the matrix with source and destination
                    temp_matrix2[src][dest] = middle + 1; 
                    temp_matrix2[dest][src] = middle + 1;
                }
            }
        
        }
    }
	for (int i = 0;i<nodes;i++)
	{
    	for (int j = 0;j<nodes;j++)
    	{
    		if(temp_matrix2[i][j]!=0 && new_matrix[i][j] == infinite_path)
    		{
    			int temp = j;
    			while(temp_matrix2[i][temp] != 0)
    			{
    				int temp1 = temp_matrix2[i][temp]-1;
    				temp =temp1;        			
    				}
    			temp_matrix2[i][j] = temp+1;
    		}
    		}
	}
	return temp_matrix2;
	}

// Read File
        public static int[][] readFile(String fileName, int nodes) throws IOException
        {
            String line;            
            int[][] matrix = new int[nodes][nodes];
            // file handling classes
            FileInputStream inputStream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(inputStream);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));

            int lineCount = 0;
            String[] numbers;
            while ((line = bf.readLine()) != null) // check if the line in the file is not equal to null
            {
                numbers = line.split(" "); // Assign the split as delimiter
                for (int i = 0; i < nodes; i++)
                {
                matrix[lineCount][i] = (int) Double.parseDouble(numbers[i]);
                }

                lineCount++; // Increment the line count
            }
            bf.close(); // close the buffer reader 
            return matrix;
        }
        

 // Print Array      
        public static void printArray(int [][] array, int nodes)
        {
            System.out.println("The given matrix is:");

             for (int i = 0; i < nodes; i++)
                {
                    for(int j = 0; j < nodes; j++) 
                    {
                        System.out.print(array[i][j] + " "); // print the matrix 
                     }
                    System.out.println();
                }
             System.out.println();
         }


public static void shortest_path(int[][] matrix, int[][] dmatrix,int src, int dest, int nodes) 
{
    int[] path = new int[nodes];
    int temp = 0;
    int finish;
    finish = dest-1;
    int begin;
    begin = src -1;
    path[0] = src;
    int count = 1;
    while (matrix[begin][finish] != 0)
    	{
    	 temp = matrix[begin][finish]-1;
    	 if(temp!=-1)
    	 {
    	 path[count] = temp+1;
    	 count++;
    	 }
    	 begin = temp;
    	}
    	    
        path[count] = dest;
    	    
    	    int sum = 0;
    	    for (int i = 0 ; i < path.length; i++)
    	    {
    	    	if(path[i]!=0)
    	    	{
    	    		if(i!=0)
    	    		{
    	    			sum = sum + dmatrix[(path[i-1]-1)][(path[i]-1)];
    	    		}
    	    	}
    	    }
    	    if(sum >= infinite_path){
    	    	System.out.println("No path exists");
    	    }
    	    else{
    	    System.out.println("The Shortest Path is");
    	    for (int i = 0 ; i < path.length; i++)
    	    {
    	    	if(path[i]!=0)
    	    	{
    	    		if (path[i]==src)
    	    		{
    	    			System.out.print(path[i] + "->");
    	    		}
    	    		else if(path[i]==dest)
    	    		{
    	    			System.out.println(path[i]);
    	    		}
    	    		else
    	    		{
    	    			System.out.print(path[i]+"->");
    	    		}
    	    	}
    	 }
    System.out.println("The cost is " + sum);}
} 

public static int[][] remove_router(int[][] matrix, int remove_node, int nodes) 
{
	int[][] modified_matrix;
	modified_matrix = new int[nodes][nodes];
	for(int i = 0 ; i < nodes ; i++)
	{
		for(int j = 0 ; j < nodes; j++)
		{
			if(i == remove_node - 1)
			{
				modified_matrix[i][j] = infinite_path;
			}
			else if (j == remove_node - 1)
			{
				modified_matrix[i][j] = infinite_path;
			}
			else
			{
				modified_matrix[i][j] = matrix[i][j];
			}
		}
		
	}
	return modified_matrix;
  }
}
