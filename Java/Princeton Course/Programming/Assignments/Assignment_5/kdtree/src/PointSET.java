import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
  private final TreeSet<Point2D>  points = new TreeSet<>();

  public PointSET() {                         // construct an empty set of points
  }

  public boolean isEmpty() {                  // is the set empty?
    return points.isEmpty();
  }

  public int size() {                         // number of points in the set
    return points.size();
  }

  public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
    if (p == null) throw new IllegalArgumentException();
    points.add(p);
  }

  public boolean contains(Point2D p) {          // does the set contain point p?
    if (p == null) throw new IllegalArgumentException();
    return points.contains(p);
  }

  public void draw() {                          // draw all points to standard draw
    throw new UnsupportedOperationException();
  }

  public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
    if (rect == null) throw new IllegalArgumentException();

    ArrayList<Point2D> pointsInRage = new ArrayList<>();

    Point2D rectMaxPoint = new Point2D(rect.xmax(), rect.ymax());
    Point2D ceiling = points.ceiling(rectMaxPoint);
    Point2D rectMinPoint = new Point2D(rect.xmin(), rect.ymin());
    Point2D floor = points.floor(rectMinPoint);

    if (floor != null && ceiling == null) {
      Iterable<Point2D> iterable = points.tailSet(floor);
      filterPoints(rect, pointsInRage, iterable);
    }

    if (floor == null && ceiling != null) {
      Iterable<Point2D> iterable = points.headSet(ceiling);
      filterPoints(rect, pointsInRage, iterable);
    }

    if (floor != null && ceiling != null) {
      Iterable<Point2D> iterable = points.subSet(floor, ceiling);
      filterPoints(rect, pointsInRage, iterable);
    }

    if (floor == null && ceiling == null) {
      filterPoints(rect, pointsInRage, points);
    }

    if (ceiling != null && ceiling.equals(rectMaxPoint))
      pointsInRage.add(ceiling);

    return pointsInRage;
  }

  private void filterPoints(RectHV rect, ArrayList<Point2D> pointsInRage, Iterable<Point2D> iterable) {
    for (Point2D point : iterable) {
      if (point.x() >= rect.xmin() && point.x() <= rect.xmax() && point.y() <= rect.ymax() && point.y() >= rect.ymin()) {
        pointsInRage.add(point);
      }
    }
  }

  public Point2D nearest(Point2D p) {           // a nearest neighbor in the set to point p; null if the set is empty
    if (p == null) throw new IllegalArgumentException();

    Point2D min = null;
    double distance = Double.POSITIVE_INFINITY;
    if (points.isEmpty()) return null;
    for (Point2D point : points) {
      double d = p.distanceSquaredTo(point);
      if (d < distance) {
        min = point;
        distance = d;
      }
    }
    return min;
  }

}
