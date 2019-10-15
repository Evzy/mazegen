package mazeGenerator;

import maze.Cell;
import maze.Maze;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;



public class HuntAndKillGenerator implements MazeGenerator {

// list of visited cells
ArrayList<Cell> visited = new ArrayList<Cell>();
boolean running;

	@Override
	public void generateMaze(Maze maze) {

		//used for randomizing directions
		Random random = new Random();

		running = true;

			//starts our walk in a random position in the maze.
			initWalk(random.nextInt(maze.sizeR), random.nextInt(maze.sizeC), maze);

			//while running (our boolean checking if hunt couldn't find a free position)
			//we will keep hunting for the next unvisited space neighboring a visited space
				while (running){
					hunt(maze);
				}

	} // end of generateMaze()


	//the in and row here is the location of where we will start our walk,
	//this is recursive, the maze is a reference to the maze to use.
	public void initWalk(int row, int col, Maze maze){

		boolean moved = false;
		visited.add(maze.map[row][col]);

		//implement tunnel, if cell has a tunnel, and it isnt visited, travel through,
		//(if it was visited, the cell is the end of the tunnel)

		if (maze.map[row][col].tunnelTo != null ){

			if(!visited.contains(maze.map[row][col].tunnelTo)){

				initWalk(maze.map[row][col].tunnelTo.r,maze.map[row][col].tunnelTo.c,maze);
				moved = true;

			}
		}

		//creates an array of directions and shuffles them to give a random directions
		 List<Integer> randomDirectionList = Arrays.asList(maze.EAST, maze.NORTH, maze.WEST, maze.SOUTH);
		 Collections.shuffle(randomDirectionList);

		 //this loop goes to a random neighbor cell, checks if its valid to go there,
		 //and if so, it will go there, otherwise it will check the next direcion.
		 for (int i = 0; i <4; i++){

			 if (moved == false){

				 if (maze.map[row][col].neigh[randomDirectionList.get(i)]!= null && !visited.contains(maze.map[row][col].neigh[randomDirectionList.get(i)])){

					 maze.map[row][col].wall[randomDirectionList.get(i)].present = false;
					 initWalk(row+maze.deltaR[randomDirectionList.get(i)], col+maze.deltaC[randomDirectionList.get(i)],maze);
					 moved = true;
					 //this is the break to the loop, once the position moves, there's no
					 //need to continue.
				 }
			 }
		}
	}

	public void hunt(Maze maze){

		boolean stop = false;
		//until we find a space thats valid
			while (!stop){

			//same as our random direction from before.
			List<Integer> randomDirectionHunt = Arrays.asList(maze.EAST, maze.NORTH, maze.WEST, maze.SOUTH);
			Collections.shuffle(randomDirectionHunt);
			//this loops through the maze from the bottom left, rightwards then up.
				for(int i = 0; i < maze.sizeR; i++){
			          for(int j = 0; j < maze.sizeC; j++){
									//checks if spot isnt in the visited list, then checks a random direction, and sees if it is in the list,
									// it will break the wall to the visited space, then start a new "walk" from this cell.
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
												//used to break the loop if it found a valid spot
												break;

											}
											//if no location is neighbors visited it continues to the next
									}
			          }
							// if it has found a direction that was valid and ran, it will break the loop
							// again, because it was nested.
							if(stop)
							break;
			 }
			 // if we visited every position and we didn't stop, theres no valid position
			 // and the maze is complete and 100% visited, so we stop the hunt, and change the
			 //running boolean, ending our initial while loop
			 if(!stop)
			 this.running = false;
			 stop = true;
		}
	}
} // end of class HuntAndKillGenerator
