import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

  private int trials;
  private double[] thresholds;

  public PercolationStats(int n, int trials) {
    // perform trials independent experiments on an n-by-n grid

    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException(Integer.toString(n) + Integer.toString(trials));
    }

    this.trials = trials;
    this.thresholds = new double[trials];

    int gridNodes = n * n;

    for(int trial = 0; trial < trials; trial++) {
      Percolation percolation = new Percolation(n);
      Random[] randoms = getRandomNumbers(n);

      for (int i = 0; i < gridNodes; i++) {
        Random random = randoms[i];

        percolation.open(random.row, random.column);
        if (percolation.percolates()) {
          double threshold = (double) percolation.numberOfOpenSites() / gridNodes;
          thresholds[trial] = threshold;
          break;
        }
      }
    }
  }

  public  double mean() {
    // sample sum of percolation threshold
    return StdStats.mean(thresholds);
  }
  public  double stddev()                   {
    // sample standard deviation of percolation threshold
    return StdStats.stddev(thresholds);
  }
  public  double confidenceLo()  {
    // low  endpoint of 95% confidence interval
    return mean() - (1.96 * stddev())/Math.sqrt(trials);
  }
  public  double confidenceHi()                 {
    // high endpoint of 95% confidence interval
    return mean() + (1.96 * stddev())/Math.sqrt(trials);
  }

  private Random[] getRandomNumbers(int n) {
    Random[] randoms = new Random[n*n];
    for (int i = 0; i < n; i++){
      for(int j=0;j<n;j++){
        randoms[i*n+j] = new Random(i+1,j+1);
      }
    }
    StdRandom.shuffle(randoms);
    return randoms;
  }

  public static void main(String[] args) {
    // test client (described below)
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);

    PercolationStats percolationStats = new PercolationStats(n, trials);

    StdOut.println("mean                     = "+percolationStats.mean());
    StdOut.println("stddev                   = "+percolationStats.stddev());
    StdOut.println("95% confidence interval  = "+String.format("[%f, %f, %f]",percolationStats.confidenceLo(),percolationStats.confidenceHi(),percolationStats.confidenceLo()));
  }

}

class Random {
  int row;
  int column;

  Random(int row, int col){
    this.row = row;
    this.column = col;
  }

}

