import java.util.ArrayList;

public class BruteCollinearPoints {
    private int numberOfSegments;
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
      // finds all line segments containing 4 points
      if (points == null) throw new java.lang.IllegalArgumentException();

      for (Point point : points) {
          if (point==null)
            throw new java.lang.IllegalArgumentException();
      }

      for (Point point1 : points) {
        int count=0;
        for (Point point2 : points) {
          if (point1.compareTo(point2)==0)
            count++;
        }
        if (count>=2)
          throw new java.lang.IllegalArgumentException();
      }

      Point[] tempPoints = new Point[3];

      for (Point point : points) {
        for (Point point1 : points) {
          tempPoints[0] = point1;
          for (Point point2 : points) {
            tempPoints[1] = point2;
            if (areCollinear(point, tempPoints,1)) {
              for (Point point3 : points) {
                tempPoints[2] = point3;

                if (areCollinear(point, tempPoints,2) && isNaturalOrder(point, tempPoints)) {
                  LineSegment lineSegment = new LineSegment(point, tempPoints[2]);
                  lineSegments.add(lineSegment);
                  numberOfSegments++;
                }
              }
            }
          }
        }
      }
    }

  private boolean isNaturalOrder(Point point, Point[] points) {
    return point.compareTo(points[0])<0 && points[0].compareTo(points[1])<0 && points[1].compareTo(points[2])<0;
  }

  private boolean areCollinear(Point point, Point[] points, int end) {
    double prevSlope=0;
    double slope;
    for (int i=0; i<=end;i++) {
        Point p = points[i];
        slope = point.slopeTo(p);
        if (slope == Double.NEGATIVE_INFINITY) return false;
        if (i==0 || slope==prevSlope) {
          prevSlope=slope;
        }
        else
        {
          return false;
        }
      }
    return true;
  }

  public  int numberOfSegments() {
    // the number of line segments
    return numberOfSegments;
  }

  public LineSegment[] segments() {
    // the line segments
    return lineSegments.toArray(new LineSegment[0]);
  }

  /*public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
*/
}
