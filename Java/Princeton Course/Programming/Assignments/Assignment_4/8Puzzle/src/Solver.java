import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;

public class Solver {
    private SearchNode goal;
    private boolean canBeSolved;

    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
      if (initial == null)
        throw new java.lang.IllegalArgumentException();

      Board twin = initial.twin();

      MinPQ<SearchNode> minPQ = new MinPQ<>();
      MinPQ<SearchNode> minTwinPQ = new MinPQ<>();

      SearchNode root = new SearchNode(initial,null,0);
      SearchNode twinRoot = new SearchNode(twin,null,0);

      minPQ.insert(root);
      minTwinPQ.insert(twinRoot);

      SearchNode min = minPQ.delMin();
      SearchNode minTwin = minTwinPQ.delMin();

      while (!min.board.isGoal() && !minTwin.board.isGoal()) {
        for (Board neighbour : min.board.neighbors()) {
          if (min.last == null || !neighbour.equals(min.last.board)){
            SearchNode neighbourNode = new SearchNode(neighbour, min, min.moves + 1);
            minPQ.insert(neighbourNode);
          }
        }

        for (Board neighbour : minTwin.board.neighbors()) {
          if (minTwin.last == null || !neighbour.equals(minTwin.last.board)){
            SearchNode neighbourNode = new SearchNode(neighbour, minTwin, minTwin.moves + 1);
            minTwinPQ.insert(neighbourNode);
          }
        }

        min = minPQ.delMin();
        minTwin = minTwinPQ.delMin();
      }
      if (min.board.isGoal()){
        canBeSolved = true;
        goal = min;
      }
    }

    public boolean isSolvable() {            // is the initial board solvable?
      return canBeSolved;
    }

    public int moves(){                     // min number of moves to solve initial board; -1 if unsolvable
      if (!isSolvable())
        return -1;
      return goal.moves;
    }

    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
      if (!isSolvable())
        return null;
      ArrayList<Board> sequence = new ArrayList<>();
      SearchNode traverse = goal;
      while(traverse != null){
        sequence.add(traverse.board);
        traverse = traverse.last;
      }

      int length = sequence.size();
      ArrayList<Board> result = new ArrayList<>(length);

      for (int i = length - 1; i >= 0; i--) {
        result.add(sequence.get(i));
      }
      return result;
    }

    private class SearchNode implements Comparable<SearchNode> {
      private final int manhattan;
      Board board;
      SearchNode last;
      int moves;

      public SearchNode(Board board, SearchNode last, int moves) {
        this.board = board;
        this.last = last;
        this.moves = moves;
        this.manhattan = board.manhattan();
      }

      @Override
      public int compareTo(SearchNode that) {
          return Integer.compare(this.moves + this.manhattan, this.moves + that.manhattan);

      }
    }

}