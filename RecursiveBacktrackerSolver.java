package mazeSolver;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;

import maze.Cell;
import maze.Maze;

public class RecursiveBacktrackerSolver implements MazeSolver {

   int visitedCells = 0;
   int length = 0;
   boolean complete = false;
   ArrayList<Cell> visited = new ArrayList<Cell>();

	public void solveMaze(Maze maze) {

      recursion(maze.entrance.r, maze.entrance.c, 0, maze);
      System.out.println("Path length of the solution is " + length);
	} // end of solveMaze()

  public void recursion(int row, int col, int currentLength, Maze maze){

      visited.add(maze.map[row][col]);
      maze.drawFtPrt(maze.map[row][col]);
      visitedCells++;
      length = currentLength;
      currentLength++;
      List<Integer> randomDirectionList = Arrays.asList(maze.EAST, maze.NORTH, maze.WEST, maze.SOUTH);
      Collections.shuffle(randomDirectionList);
      if (row == maze.exit.r && col == maze.exit.c){
        complete = true;
      }

      if (maze.map[row][col].tunnelTo != null){

  			if(!visited.contains(maze.map[row][col].tunnelTo)){

          recursion(maze.map[row][col].tunnelTo.r,maze.map[row][col].tunnelTo.c, currentLength, maze);

  			}
  		}

      for (int i = 0; i <4; i++){

          if (!complete){

                if (maze.map[row][col].wall[randomDirectionList.get(i)].present == false && !visited.contains(maze.map[row][col].neigh[randomDirectionList.get(i)])){

                  recursion(maze.map[row][col].neigh[randomDirectionList.get(i)].r, maze.map[row][col].neigh[randomDirectionList.get(i)].c, currentLength, maze);

              }
          }
        }
  }


	@Override
	public boolean isSolved() {
		// The maze is solved when the solveMaze function stopped
		return true;
	} // end if isSolved()

	@Override
	public int cellsExplored() {
    return visitedCells;
	} // end of cellsExplored()

} // end of class RecursiveBackTrackerSolver
