import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {
  private int[][] blocks2d;
  private int[][] blocks3d;

  @BeforeEach
  void setUp() {
    blocks2d = new int[2][2];
    blocks2d[0][0] = 1;
    blocks2d[0][1] = 2;
    blocks2d[1][0] = 3;
    blocks2d[1][1] = 0;

    blocks3d = new int[3][3];
    blocks3d[0][0] = 1;
    blocks3d[0][1] = 2;
    blocks3d[0][2] = 3;
    blocks3d[1][0] = 4;
    blocks3d[1][1] = 5;
    blocks3d[1][2] = 6;
    blocks3d[2][0] = 7;
    blocks3d[2][1] = 8;
    blocks3d[2][2] = 0;
  }

  @Test
  public void whenSolverIsInitializedWithFinalGoalBoardMovesReturnsZero(){
    Board board = new Board(blocks2d);
    Solver solver = new Solver(board);
    assertEquals(0, solver.moves());
  }

  @Test
  public void whenSolverIsInitializedWithBoardOneStepAwayFromTheGoalMovesReturnsOne(){
    blocks2d[0][0] = 1;
    blocks2d[0][1] = 2;
    blocks2d[1][0] = 0;
    blocks2d[1][1] = 3;
    Board board = new Board(blocks2d);
    Solver solver = new Solver(board);
    assertEquals(1,solver.moves());
  }

  @Test
  public void whenSolverIsInitializedWithBoardWithOutOfPlaceBlocksTheMovesAreAtLeastHammingOrManhattanPriority(){

    blocks2d[0][0] = 1;
    blocks2d[0][1] = 0;
    blocks2d[1][0] = 3;
    blocks2d[1][1] = 2;

    Board board = new Board(blocks2d);
    Solver solver = new Solver(board);
    assertTrue(solver.moves()>=board.hamming());
    assertTrue(solver.moves()>=board.manhattan());

    blocks2d[0][0] = 0;
    blocks2d[0][1] = 3;
    blocks2d[1][0] = 2;
    blocks2d[1][1] = 1;
    board = new Board(blocks2d);
    solver = new Solver(board);
    assertTrue(solver.moves()>=board.hamming());
    assertTrue(solver.moves()>=board.manhattan());

  }

  @Test
  public void whenSolverIsInitializedWithUnresolvablePuzzleIsSolvableReturnsFalse(){

    Solver solver = new Solver(new Board(blocks2d));
    assertTrue(solver.isSolvable());

    blocks2d[0][0]=1;
    blocks2d[0][1]=0;
    blocks2d[1][0]=2;
    blocks2d[1][1]=3;

    solver = new Solver(new Board(blocks2d));
    assertFalse(solver.isSolvable());

  }

}