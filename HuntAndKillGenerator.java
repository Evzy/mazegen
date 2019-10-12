package mazeGenerator;

import maze.Cell;
import maze.Maze;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;



public class HuntAndKillGenerator implements MazeGenerator {

//ArrayList<Cell> unvisited = new ArrayList<Cell>();
ArrayList<Cell> visited = new ArrayList<Cell>();
boolean running;

	@Override
	public void generateMaze(Maze maze) {

		Random random = new Random();

		System.out.println(maze.sizeR);
		System.out.println(maze.sizeC);


		// for(int i = 0; i < maze.sizeR; i++){
	  //        for(int j = 1; j < maze.sizeC; j++){
	  //           unvisited.add(maze.map[i][j]);
	  //        }
	  //     }

		running = true;

		while(running){

			initWalk(random.nextInt(maze.sizeR), random.nextInt(maze.sizeC), maze);
			hunt(running);

		}

		//INSERT CODE InitWalk(random between r max & min, and c max & min)

		// these work, this is the row an columns

		// TODO Auto-generated method stub

	} // end of generateMaze()

	//the in and row here is the location of where we will start our walk, this is recursive

	public void initWalk(int row, int col, Maze maze){

		boolean moved = false;

		System.out.println(row + "&" + col);

		visited.add(maze.map[row][col]);

		//creates an array of directions and shuffles them to give a random directions
		 List<Integer> randomDirectionList = Arrays.asList(maze.EAST, maze.NORTH, maze.WEST, maze.SOUTH);
		 Collections.shuffle(randomDirectionList);

		 for (int i = 0; i <4; i++){

			 if (moved == false){

				 if (maze.map[row][col].neigh[randomDirectionList.get(i)]!= null && !visited.contains(maze.map[row][col].neigh[randomDirectionList.get(i)])){

					 maze.map[row][col].wall[randomDirectionList.get(i)].present = false;
					 maze.map[row][col].neigh[randomDirectionList.get(i)] = null;
					 maze.map[row+maze.deltaR[randomDirectionList.get(i)]][col+maze.deltaC[randomDirectionList.get(i)]].neigh[maze.oppoDir[randomDirectionList.get(i)]] = null;
					 initWalk(row+maze.deltaR[randomDirectionList.get(i)], col+maze.deltaC[randomDirectionList.get(i)],maze);
					 moved = true;
				 }

			 }

		}

	}

	public void hunt(boolean running){

		for(int i = 0; i < maze.sizeR; i++){
	          for(int j = 1; j < maze.sizeC; j++){
							if (visited.contains(maze.map[i][j]){

							}
	          }
	       }

		this.running = false;
		//if it ends with every space visited



	}

} // end of class HuntAndKillGenerator
