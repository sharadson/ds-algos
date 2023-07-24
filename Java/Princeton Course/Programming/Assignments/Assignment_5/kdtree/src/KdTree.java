import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {
  private Node root;
  private int count = 0;
  private boolean alternate = true;

  private enum Side {
    ROOT, LEFT_BOTTOM, RIGHT_TOP
  }

  private static class Node {
    private Point2D point;      // the point
    private RectHV rect;
    private Node lb = null;        // the left/bottom subtree
    private Node rt = null;        // the right/top subtree
  }

  public KdTree() {                               // construct an empty set of points
  }

  public boolean isEmpty() {                      // is the set empty?
    return count == 0;
  }

  public int size() {                         // number of points in the set
    return count;
  }

  public void insert(Point2D p) {              // add the point to the set (if it is not already in the set)
    if (p == null) throw new IllegalArgumentException();
    if (contains(p)) return;
    alternate = true;
    root = insertNode(root, p, root, Side.ROOT);
    count++;
  }

  private Node insertNode(Node node, Point2D p, Node parent, Side side) {
      if (node == null) {
        Node newNode = new Node();
        newNode.rect = getNodeRect(parent, side);
        newNode.point = p;
        return newNode;
      }
      if (alternate) {
        insertTraverse(node, p, p.x(), node.point.x());
      }
      else {
        insertTraverse(node, p, p.y(), node.point.y());
      }
      return node;
  }

  private void insertTraverse(Node node, Point2D p, double pointCoordinate, double nodeCoordinate) {
    alternate = !alternate;

    if (pointCoordinate < nodeCoordinate)
      node.lb = insertNode(node.lb, p, node, Side.LEFT_BOTTOM);
    else
      node.rt = insertNode(node.rt, p, node, Side.RIGHT_TOP);
  }

  private RectHV getNodeRect(Node parent, Side side) {
    RectHV rect;
    if (parent == null) {
      return new RectHV(0, 0, 1, 1);
    }
    if (side == Side.LEFT_BOTTOM) {
      if (alternate)
        rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.point.y());
      else
        rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.point.x(), parent.rect.ymax());
    }
    else {
      if (alternate)
        rect = new RectHV(parent.rect.xmin(), parent.point.y(), parent.rect.xmax(), parent.rect.ymax());
      else
        rect = new RectHV(parent.point.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
    }
    return rect;
  }

  public boolean contains(Point2D p) {            // does the set contain point p?
    if (p == null) throw new IllegalArgumentException();
    if (isEmpty()) return false;

    alternate = true;
    return containsNode(root, p);
  }

  private boolean containsNode(Node node, Point2D p) {
    if (node == null) return false;
    if (p.equals(node.point)) return true;
    if (alternate) {
      alternate = false;
      return p.x() < node.point.x() ? containsNode(node.lb, p) : containsNode(node.rt, p);
    }
    else {
      alternate = true;
      return p.y() < node.point.y() ? containsNode(node.lb, p) : containsNode(node.rt, p);
    }
  }

  public void draw() {                         // draw all points to standard draw

  throw new UnsupportedOperationException();
  }

  public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle (or on the boundary)
    if (rect == null) throw new IllegalArgumentException();
    ArrayList<Point2D> pointsInRange = new ArrayList<>();
    rectRange(root, rect, pointsInRange);
    return pointsInRange;
  }

  private void rectRange(Node node, RectHV rect, ArrayList<Point2D> rangePoints) {
    if (node == null) return;
    if (node.rect.xmax() < rect.xmin()
            || node.rect.xmin() > rect.xmax()
            || node.rect.ymin() > rect.ymax()
            || node.rect.ymax() < rect.ymin())
      return;

    if (isNodeInRange(node, rect))
      rangePoints.add(node.point);

    rectRange(node.lb, rect, rangePoints);
    rectRange(node.rt, rect, rangePoints);
  }

  private boolean isNodeInRange(Node node, RectHV rect) {
    return node.point.x() >= rect.xmin()
            && node.point.x() <= rect.xmax()
            && node.point.y() >= rect.ymin()
            && node.point.y() <= rect.ymax();
  }

  public Point2D nearest(Point2D p) {             // a nearest neighbor in the set to point p; null if the set is empty
    if (p == null) throw new IllegalArgumentException();
    if (isEmpty()) return null;

    Node node = root;
    return nearestNode(node, p, root.point, true);

  }

  private Point2D nearestNode(Node node, Point2D p, Point2D champion, boolean alt) {
    if (node == null) return champion;

    if (alt) {
      return nearestTraverse(node, p, champion, p.x(), node.point.x(), alt);
    }

    else {
      return nearestTraverse(node, p, champion, p.y(), node.point.y(), alt);
    }
  }

  private Point2D nearestTraverse(Node node, Point2D p, Point2D champion, double pointCoordinate, double nodeCoordinate, boolean alt) {
    Point2D champion1;
    Point2D champion2;
    alt = !alt;
    Node node1;
    Node node2;
    if (pointCoordinate > nodeCoordinate) {
      node1 = node.rt;
      node2 = node.lb;
    } else {
      node1 = node.lb;
      node2 = node.rt;
    }

    champion = updateChampion(p, champion, node.point);

    if (node1 != null) {
      champion1 = nearestNode(node1, p, champion, alt);
      champion = updateChampion(p, champion, champion1);
    }

    if (node2 != null && node2.rect.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
      champion2 = nearestNode(node2, p, champion, alt);
      champion = updateChampion(p, champion, champion2);
    }

    return champion;
  }

  private Point2D updateChampion(Point2D p, Point2D champion1, Point2D champion2) {
    return champion1.distanceSquaredTo(p) > champion2.distanceSquaredTo(p) ? champion2 : champion1;
  }

}
