import edu.princeton.cs.algs4.WeightedQuickUnionUF;

  public class Percolation {

  private final int topRootUfIndex;
  private final int bottomRootUfIndex;
  private boolean[][] gridOpen;
  private WeightedQuickUnionUF weightedQuickUnionUF;
  private WeightedQuickUnionUF wQUUFNoBottomRoot;
  private int n;

  public Percolation(int n) {
    if (n < 1 ) throw new IllegalArgumentException("n is out of bounds");
        this.n = n;
        gridOpen = new boolean[n+1][n+1];

        for(int row=1;row<=n;row++){
            for(int col=1;col<=n;col++)
            {
                gridOpen[row][col] = false;
            }
        }

      int totalNodes = n * n + 2;
      weightedQuickUnionUF = new WeightedQuickUnionUF(totalNodes);
      wQUUFNoBottomRoot = new WeightedQuickUnionUF(totalNodes-1);

      topRootUfIndex = n*n;
      bottomRootUfIndex = (n*n) + 1;
    }

  private void processNeighbours(int currentRow, int currentCol, int currentUfIndex) {

    int neighbourCol = currentCol - 1;

    if(checkLowerBound(neighbourCol)) {
      int neighbourUfIndex = getUFIndex(currentRow,neighbourCol);
      if(gridOpen[currentRow][neighbourCol]) {
        connectOpen(currentUfIndex, neighbourUfIndex);
      }
    }

    neighbourCol = currentCol + 1;
    if(checkUpperBound(neighbourCol)) {
      int neighbourUfIndex = getUFIndex(currentRow,neighbourCol);
      if(gridOpen[currentRow][neighbourCol]) {
        connectOpen(currentUfIndex, neighbourUfIndex);
      }
    }

    int neighbourRow = currentRow - 1;
    if(checkLowerBound(neighbourRow)) {
      int neighbourUfIndex = getUFIndex(neighbourRow,currentCol);
      if(gridOpen[neighbourRow][currentCol]) {
        connectOpen(currentUfIndex, neighbourUfIndex);
      }
    }

    neighbourRow = currentRow + 1;
    if(checkUpperBound(neighbourRow)) {
      int neighbourUfIndex = getUFIndex(neighbourRow,currentCol);
      if(gridOpen[neighbourRow][currentCol]) {
        connectOpen(currentUfIndex, neighbourUfIndex);
      }
    }
  }

  private boolean checkUpperBound(int newRow) {
    return newRow <= n;
  }

  private boolean checkLowerBound(int newCol) {
    return newCol >= 1;
  }

  private void connectOpen(int currentNodeUfIndex, int neighbourUfIndex) {
        weightedQuickUnionUF.union(currentNodeUfIndex, neighbourUfIndex);
        wQUUFNoBottomRoot.union(currentNodeUfIndex, neighbourUfIndex);
  }

  private boolean isBottomVirtualRootNeighbour(int row) {
    int lastRowNumber = n;
    return row == lastRowNumber;
  }

  private boolean isTopVirtualRootNeighbour(int row) {
    int firstRowNumber = 1;
    return row == firstRowNumber;
  }

  public void open(int row, int col) {

    if (row < 1 || row >n  || col < 1 || col > n) throw new IllegalArgumentException("row index i out of bounds");

    int currentNodeUfIndex = getUFIndex(row,col);

    gridOpen[row][col] = true;

    if(isTopVirtualRootNeighbour(row)){
      weightedQuickUnionUF.union(topRootUfIndex, currentNodeUfIndex);
      wQUUFNoBottomRoot.union(topRootUfIndex, currentNodeUfIndex);
    }

    if(isBottomVirtualRootNeighbour(row)){
      weightedQuickUnionUF.union(bottomRootUfIndex, currentNodeUfIndex);
    }
    processNeighbours(row, col, currentNodeUfIndex);
  }

  private int getUFIndex(int row, int col){
    return ((row-1)*n + (col-1));
  }

  public boolean isFull(int row, int col) {
    if (row < 1 || row >n  || col < 1 || col > n) throw new IllegalArgumentException("row index i out of bounds");
    int ufIndex = getUFIndex(row,col);
    return weightedQuickUnionUF.connected(ufIndex, topRootUfIndex) && wQUUFNoBottomRoot.connected(ufIndex, topRootUfIndex);
  }

  public boolean isOpen(int row, int col) {
    if (row < 1 || row >n  || col < 1 || col > n) throw new IllegalArgumentException("row index i out of bounds");
    return gridOpen[row][col];
  }

  public int numberOfOpenSites() {
    int numberOfOpenSites = 0;
    for(int row=1;row<=n;row++){
      for(int col=1;col<=n;col++)
      {
        if(gridOpen[row][col])
          numberOfOpenSites+=1 ;
      }
    }
    return numberOfOpenSites;
    }

  public boolean percolates() {
      return weightedQuickUnionUF.connected(bottomRootUfIndex, topRootUfIndex);
  }

}