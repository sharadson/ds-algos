import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
  private int[][] blocks2d;
  private int[][] blocks3d;
  private Board board;

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
  public void whenBoardTypeIsInstantiatedDimensionIsSet(){
    board = new Board(blocks2d);
    assertEquals(2,board.dimension());
  }

  @Test
  public void whenNoBlocksAreOutOfPlaceHammingReturnsZero(){
    board = new Board(blocks2d);
    assertEquals(0,board.hamming());
  }

  @Test
  public void whenThereAreOutOfPlaceBlocksHammingReturnsTheirCount(){
    blocks2d[0][0] = 2;
    blocks2d[0][1] = 1;
    blocks2d[1][0] = 3;
    blocks2d[1][1] = 0;
    board = new Board(blocks2d);

    assertEquals(2,board.hamming());

    blocks3d[0][0] = 1;
    blocks3d[0][1] = 2;
    blocks3d[0][2] = 0;
    blocks3d[1][0] = 3;
    blocks3d[1][1] = 4;
    blocks3d[1][2] = 5;
    blocks3d[2][0] = 6;
    blocks3d[2][1] = 7;
    blocks3d[2][2] = 8;
    board = new Board(blocks3d);
    assertEquals(6,board.hamming());
  }

  @Test
  public void whenNoBlocksAreOutOfPlaceManhattanReturnsZero(){
    Board board = new Board(blocks2d);
    assertEquals(0,board.manhattan());
  }

  @Test
  public void whenBlocksAreOutOfPlaceManhattanReturnsValue(){
    blocks2d[0][0]=1;
    blocks2d[0][1]=0;
    blocks2d[1][0]=2;
    blocks2d[1][1]=3;

    Board board = new Board(blocks2d);
    assertEquals(3,board.manhattan());

    blocks3d[0][0] = 1;
    blocks3d[0][1] = 2;
    blocks3d[0][2] = 0;
    blocks3d[1][0] = 3;
    blocks3d[1][1] = 4;
    blocks3d[1][2] = 5;
    blocks3d[2][0] = 6;
    blocks3d[2][1] = 7;
    blocks3d[2][2] = 8;
    board = new Board(blocks3d);
    assertEquals(10,board.manhattan());

    blocks3d[0][0] = 8;
    blocks3d[0][1] = 1;
    blocks3d[0][2] = 3;
    blocks3d[1][0] = 4;
    blocks3d[1][1] = 0;
    blocks3d[1][2] = 2;
    blocks3d[2][0] = 7;
    blocks3d[2][1] = 6;
    blocks3d[2][2] = 5;
    assertEquals(10,board.manhattan());
  }

  @Test
  public void whenBlocksAreArrangedProperlyIsGoalReturnsTrueOtherwiseFalse(){
    blocks2d[0][0]=3;
    blocks2d[0][1]=1;
    blocks2d[1][0]=0;
    blocks2d[1][1]=2;
    Board board = new Board(blocks2d);
    assertFalse(board.isGoal());
  }

  @Test
  public void whenBoardIsNotGoalThenReturnNeighbours(){
    blocks2d[0][0] = 2;
    blocks2d[0][1] = 3;
    blocks2d[1][0] = 0;
    blocks2d[1][1] = 1;

    Board board = new Board(blocks2d);
    assertTrue(board.neighbors().iterator().hasNext());
    long size = board.neighbors().spliterator().getExactSizeIfKnown();
    assertEquals(2,size);

    blocks3d[0][0] = 1;
    blocks3d[0][1] = 2;
    blocks3d[0][2] = 3;
    blocks3d[1][0] = 4;
    blocks3d[1][1] = 0;
    blocks3d[1][2] = 5;
    blocks3d[2][0] = 6;
    blocks3d[2][1] = 7;
    blocks3d[2][2] = 8;

    board = new Board(blocks3d);
    size = board.neighbors().spliterator().getExactSizeIfKnown();
    assertEquals(4,size);
  }

  @Test
  public void whenBoardIsInitializedToStringIsPopulatedInGivenFormat(){
    Board board = new Board(blocks2d);
    String expected = "2\n 1 2\n 3 0";
    System.out.println(expected);
    assertEquals(expected,board.toString());

    board = new Board(blocks3d);
    expected = "3\n 1 2 3\n 4 5 6\n 7 8 0";
    System.out.println(expected);
    assertEquals(expected,board.toString());
  }

  @Test
  public void equalityForTwoBoardsWithSameBlocksIsTrue(){
    Board board = new Board(blocks2d);
    assertTrue(board.equals(board));
    int[][] newBlocks = new int[2][2];
    newBlocks[0][0]=1;
    newBlocks[0][1]=2;
    newBlocks[1][0]=3;
    newBlocks[1][1]=0;
    Board newBoard = new Board(newBlocks);
    assertTrue(board.equals(newBoard));
  }

  @Test
  public void equalityForTwoBoardsWithDifferentBlocksIsFalse(){
    Board board = new Board(blocks2d);
    assertTrue(board.equals(board));
    int[][] newBlocks = new int[2][2];
    newBlocks[0][0]=1;
    newBlocks[0][1]=3;
    newBlocks[1][0]=2;
    newBlocks[1][1]=0;
    Board newBoard = new Board(newBlocks);
    assertFalse(board.equals(newBoard));
  }

  @Test
  public void testNeighbours(){
    blocks2d[0][0]=1;
    blocks2d[0][1]=2;
    blocks2d[1][0]=3;
    blocks2d[1][1]=0;
    board = new Board(blocks2d);
    long size = board.neighbors().spliterator().getExactSizeIfKnown();
    assertEquals(2,size);
  }

  @Test
  public void whenBoardIsInitializedTwinReturnsADifferentBoard(){
    board = new Board(blocks2d);
    Board twin = board.twin();
    assertFalse(board.equals(twin));

    blocks2d[0][0]=0;
    blocks2d[0][1]=3;
    blocks2d[1][0]=1;
    blocks2d[1][1]=2;

    board = new Board(blocks2d);
    twin = board.twin();
    assertFalse(board.equals(twin));

    board = new Board(blocks3d);
    twin = board.twin();
    assertFalse(board.equals(twin));

    blocks3d[0][0]=0;
    blocks3d[0][1]=2;
    blocks3d[0][2]=3;
    blocks3d[1][0]=7;
    blocks3d[1][1]=8;
    blocks3d[1][2]=5;
    blocks3d[2][0]=4;
    blocks3d[2][1]=1;
    blocks3d[2][2]=6;

    board = new Board(blocks3d);
    twin = board.twin();
    assertFalse(board.equals(twin));

  }

  @Test
  public void whenBoardsInputBlocksChangeOutsideOfBoardTheBoardsFunctionsSuchAsHammingGoalNeighboursTwinStaySame(){
    board = new Board(blocks2d);
    int oldHamming = board.hamming();
    long oldNeighbourSize = board.neighbors().spliterator().getExactSizeIfKnown();
    boolean oldGoal = board.isGoal();
    Board oldTwin = board.twin();

    blocks2d[0][0]=2;
    blocks2d[0][1]=3;
    blocks2d[1][0]=1;
    blocks2d[1][1]=0;

    int newHamming = board.hamming();
    long newNeighbourSize = board.neighbors().spliterator().getExactSizeIfKnown();
    boolean newGoal = board.isGoal();
    Board newTwin = board.twin();
    assertTrue(oldHamming==newHamming);
    assertTrue(oldNeighbourSize==newNeighbourSize);
    assertTrue(oldGoal==newGoal);
    assertTrue(oldTwin.equals(newTwin));

  }



}