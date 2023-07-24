import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private int numberOfSegments;
  private ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
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

      Point[] sortedPoints = points.clone();
      for (Point current : points) {
        if (current==null)
          throw new java.lang.IllegalArgumentException();
        Point point;
        double currentSlope;
        double prevSlope = 0;
        int count=1;
        int length = sortedPoints.length;

        Arrays.sort(sortedPoints,current.slopeOrder());

        for (int i = 0; i< length; i++){
          point = sortedPoints[i];
          currentSlope = current.slopeTo(point);
          if (i>0 && currentSlope == prevSlope){
            count++;
            prevSlope=currentSlope;
            continue;
          }

          int start = i - count;
          int end = i - 1;
          if (count>=3 && isCurrentSmallest(current,sortedPoints, start, end)) {
            addLineSegment(current, sortedPoints, start, end);
          }
          count=1;
          prevSlope = currentSlope;
        }

        if (count>=3 && isCurrentSmallest(current,sortedPoints, length-count, length-1)) {
          addLineSegment(current, sortedPoints, length-count,length-1);
        }
      }
    }

  private boolean isCurrentSmallest(Point current, Point[] points, int start, int end) {
    for (int i = start; i <= end; i++) {
        if (current.compareTo(points[i]) > 0)
          return false;
      }
    return true;
  }

  private void addLineSegment(Point point, Point[] points, int first, int last) {
    Point end = point;
    for (int i=first;i<=last;i++) {
      Point temp = points[i];
      if (temp.compareTo(end) > 0) {
        end = temp;
      }
    }

    LineSegment lineSegment = new LineSegment(point,end);
    lineSegments.add(lineSegment);
    numberOfSegments++;
  }

  public int numberOfSegments()        // the number of line segments
  {
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }*/
}


