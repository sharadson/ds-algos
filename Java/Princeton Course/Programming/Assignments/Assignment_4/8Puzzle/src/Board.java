import java.util.ArrayList;

public class Board {
  private int[][] blocks;
  private int dimension;
  public Board(int[][] inputBlocks) {           // construct a board from an n-by-n array of blocks2d
    blocks = inputBlocks;
    dimension = blocks.length;
    blocks = cloneBlocks();
  }

  public int dimension() {                 // board dimension n
    return dimension;
  }

  public int hamming() {                   // number of blocks out of place
    int count = 1;
    int outOfPlace=0;

    for(int i=0;i<dimension;i++) {
      for(int j=0;j<dimension;j++) {
        int block = blocks[i][j];
        if(block != count++ && block>0) {
          outOfPlace++;
        }
      }
    }
    return outOfPlace;
  }

  public int manhattan() {                 // sum of Manhattan distances between blocks2d and goal
    int count = 1;
    int distance = 0;
    for(int row=0; row < dimension ; row++){
      for(int column = 0; column < dimension; column++){
        int block = blocks[row][column];
        if(block != count++ && block > 0) {
          int division = block / dimension;
          int remainder = block % dimension;
          int destRow;
          int destColumn;
          if (remainder == 0) {
            destRow = division-1;
            destColumn = dimension-1;
          }
          else {
            destRow = division;
            destColumn = remainder-1;
          }
          distance = distance + Math.abs(destRow - row) + Math.abs(destColumn - column);
        }
      }
    }
    return distance;
  }

  public boolean isGoal() {                // is this board the goal board?
    int count = 1;
    for (int row = 0; row < dimension; row++) {
      for (int column = 0; column < dimension ; column++) {
        int block = blocks[row][column];
        if (block != count++ && block > 0) {
          return false;
        }
      }
    }
    return true;
  }


  public Board twin() {                    // a board that is obtained by exchanging any pair of blocks
    int[][] cloneBlocks = cloneBlocks();

    for (int i=0;i<dimension-1;i++){
      for (int j=0;j<dimension;j++){
        if (cloneBlocks[i][j]!=0 && cloneBlocks[i+1][j]!=0){
          int swap = cloneBlocks[i][j];
          cloneBlocks[i][j] = cloneBlocks[i+1][j];
          cloneBlocks[i+1][j] = swap;
          return new Board(cloneBlocks);
        }
      }
    }

    for (int i=0;i<dimension;i++){
      for (int j=0;j<dimension-1;j++){
        if (cloneBlocks[i][j]!=0 && cloneBlocks[i][j+1]!=0){
          int swap = cloneBlocks[i][j];
          cloneBlocks[i][j] = cloneBlocks[i][j+1];
          cloneBlocks[i][j+1] = swap;
          return new Board(cloneBlocks);
        }
      }
    }

    return null;
  }


  public boolean equals(Object other) {        // does this board equal y?
    if (this==other) return true;
    if (other==null) return false;
    if (this.getClass()!=other.getClass()) return false;
    Board that = (Board) other;
    if (this.dimension!=that.dimension) return false;
    for (int row=0;row<dimension;row++){
      for (int column=0;column<dimension;column++){
        if (blocks[row][column]!=that.blocks[row][column]) return false;
      }
    }
    return true;
  }

  public Iterable<Board> neighbors() {     // all neighboring boards
    ArrayList<Board> neighbours = new ArrayList<>();

    for (int i=0;i<dimension;i++){
      for (int j=0;j<dimension;j++){
        int block = blocks[i][j];
        if (block == 0){
          if (i-1>=0){
            addNeighbour(neighbours, i,j,i-1, j);
          }
          if (i+1<dimension){
            addNeighbour(neighbours, i,j,i+1, j);
          }
          if (j-1>=0){
            addNeighbour(neighbours, i,j,i, j-1);
          }
          if (j+1<dimension){
            addNeighbour(neighbours, i,j,i, j+1);
          }
        }
      }
    }
    return neighbours;
  }

  private void addNeighbour(ArrayList<Board> neighbours, int emptyRow, int emptyColumn, int row, int column) {
    int[][] myBlocks = cloneBlocks();

    int block = myBlocks[row][column];
    myBlocks[emptyRow][emptyColumn] = block;
    myBlocks[row][column] = 0;
    Board board = new Board(myBlocks);
    neighbours.add(board);
  }

  private int[][] cloneBlocks() {
    int [][] myBlocks = new int[dimension][];
    for(int i = 0; i < dimension; i++)
      myBlocks[i] = blocks[i].clone();
    return myBlocks;
  }

  public String toString() {               // string representation of this board (in the output format specified below)
    String output=""+dimension;
    for(int row=0;row<dimension;row++){
      output=output.concat("\n");
      for (int column=0;column<dimension;column++){
        output=output.concat(" "+blocks[row][column]);
      }
    }
    return output;
  }

}

