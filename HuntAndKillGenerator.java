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

		running = true;

			initWalk(random.nextInt(maze.sizeR), random.nextInt(maze.sizeC), maze);
				while (running){

					hunt(maze);
					System.out.println("STILL RUNNING");
				}

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
					 initWalk(row+maze.deltaR[randomDirectionList.get(i)], col+maze.deltaC[randomDirectionList.get(i)],maze);
					 moved = true;
				 }
			 }
		}
	}

	public void hunt(Maze maze){

		boolean stop = false;
			while (!stop){

			List<Integer> randomDirectionHunt = Arrays.asList(maze.EAST, maze.NORTH, maze.WEST, maze.SOUTH);
			Collections.shuffle(randomDirectionHunt);

				for(int i = 0; i < maze.sizeR; i++){
			          for(int j = 0; j < maze.sizeC; j++){
									if (!visited.contains(maze.map[i][j])){

											if(visited.contains(maze.map[i][j].neigh[randomDirectionHunt.get(0)]) && !visited.contains(maze.map[i][j])){
												maze.map[i][j].wall[randomDirectionHunt.get(0)].present = false;
												initWalk(i,j,maze);
												stop = true;
												break;

											}

											if(visited.contains(maze.map[i][j].neigh[randomDirectionHunt.get(1)])&& !visited.contains(maze.map[i][j])){
												maze.map[i][j].wall[randomDirectionHunt.get(1)].present = false;
												initWalk(i,j,maze);
												stop = true;
												break;

											}

											if(visited.contains(maze.map[i][j].neigh[randomDirectionHunt.get(2)]) && !visited.contains(maze.map[i][j])){
												maze.map[i][j].wall[randomDirectionHunt.get(2)].present = false;
												initWalk(i,j,maze);
												stop = true;
												break;

											}

											if(visited.contains(maze.map[i][j].neigh[randomDirectionHunt.get(3)])&& !visited.contains(maze.map[i][j])){
												maze.map[i][j].wall[randomDirectionHunt.get(3)].present = false;
												initWalk(i,j,maze);
												stop = true;
												break;

											}
											//if it ends with every space visited
									}
			          }
							if(stop)
							break;
			 }//if it ends with every space visited

			 if(!stop)
			 this.running = false;
			 stop = true;
		}
	}
} // end of class HuntAndKillGenerator
