import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class kdtreeTest {
  KdTree kdtree;
  private Point2D point2D1;
  private Point2D point2D2;
  private Point2D point2D3;

  @BeforeEach
  void setUp() {

    kdtree = new KdTree();
    point2D1 = new Point2D(.1,.1);
    point2D2 = new Point2D(.2,.2);
    point2D3 = new Point2D(.3, .3);
    Point2D point2D4 = new Point2D(.4, .4);
    Point2D point2D5 = new Point2D(.5, .5);
    kdtree.insert(point2D2);
    kdtree.insert(point2D3);
    kdtree.insert(point2D4);
    kdtree.insert(point2D1);
    kdtree.insert(point2D5);
  }

  @Test
  public void whenNoPointsAreInsertedIsEmptyIsTrue() {
    kdtree = new KdTree();
    assertTrue(kdtree.isEmpty());
  }

  @Test
  public void whenOnePointIsInsertedIsEmptyIsNotTrue() {
    kdtree = new KdTree();
    kdtree.insert(point2D1);
    assertFalse(kdtree.isEmpty());
  }

  @Test
  public void whenPointIsInsertedContainsReturnsTrue() {
    kdtree = new KdTree();
    kdtree.insert(point2D1);
    assertTrue(kdtree.contains(point2D1));
  }

  @Test
  public void whenNodesAreInsertedSizeChangesAccordingly() {
    kdtree = new KdTree();
    kdtree.insert(point2D1);
    assertEquals(1,kdtree.size());

    kdtree.insert(point2D2);
    assertEquals(2,kdtree.size());
  }

  @Test
  void whenRectHVHasOnePointsWithinItNonEmptyIteratorIsReturned() {
    RectHV rectHV = new RectHV(.15,.15,.25,.25);
    Iterable<Point2D> iterable = kdtree.range(rectHV);
    assertTrue(iterable.iterator().hasNext());
    assertEquals(1,iterable.spliterator().getExactSizeIfKnown());

    rectHV = new RectHV(.35,.39,.45,.41);
    iterable = kdtree.range(rectHV);
    assertTrue(iterable.iterator().hasNext());
    assertEquals(1,iterable.spliterator().getExactSizeIfKnown());
  }

  @Test
  void whenRectHVHasNoPointsWithinItAnEmptyIteratorIsReturned() {
    RectHV rectHV = new RectHV(.6,.6,.8,.8);
    Iterable<Point2D> iterable = kdtree.range(rectHV);
    assertFalse(iterable.iterator().hasNext());
  }

  @Test
  void whenRectHasMultiplePointsWithinItNonEmptyIteratorWithCorrectSizeIsReturned() {
    RectHV rectHV = new RectHV(.25,.25,.8,.8);
    Iterable<Point2D> iterable = kdtree.range(rectHV);
    assertTrue(iterable.iterator().hasNext());
    assertEquals(3,iterable.spliterator().getExactSizeIfKnown());
  }

  @Test
  void whenReactHasPointOnItsEdgesNonEmptyIteratorWithCorrectSizeIsReturned() {
    kdtree = new KdTree();
    Point2D p = new Point2D(.15, .15);
    kdtree.insert(p);
    RectHV rectHV = new RectHV(.15,.15,.25,.25);
    Iterable<Point2D> iterable = kdtree.range(rectHV);
    assertTrue(iterable.iterator().hasNext());
    assertEquals(1,iterable.spliterator().getExactSizeIfKnown());

    kdtree = new KdTree();
    p = new Point2D(.25, .25);
    kdtree.insert(p);
    Point2D p1 = new Point2D(.15, .25);
    kdtree.insert(p1);
    rectHV = new RectHV(.15,.15,.25,.25);
    iterable = kdtree.range(rectHV);
    assertTrue(iterable.iterator().hasNext());
    assertEquals(2,iterable.spliterator().getExactSizeIfKnown());
  }

  @Test
  void nearestReturnsGivenPointsClosestNeighbouringPoint() {
    Point2D point =  new Point2D(.31,.29);
    Point2D nearest = kdtree.nearest(point);

    assertNotNull(nearest);
    assertEquals(point2D3, nearest);

    point = new Point2D(0,0);
    nearest = kdtree.nearest(point);
    assertNotNull(nearest);
    assertEquals(point2D1, nearest);
  }

  @Test
  void testNearest() {
    /*Point2D p1 = new Point2D(0.372, 0.497);
    Point2D p2 = new Point2D(0.564, 0.413);
    Point2D p3 = new Point2D(0.226, 0.577);
    Point2D p4 = new Point2D(0.144, 0.179);
    Point2D p5 = new Point2D(0.083, 0.51);*/
//    Point2D p6 = new Point2D(0.32 ,0.708);
//    Point2D p7 = new Point2D(0.417 ,0.362);
//    Point2D p8 = new Point2D(0.862 ,0.825);
//    Point2D p9 = new Point2D(0.785, 0.725);
//    Point2D p10 = new Point2D(0.499, 0.208);

    Point2D A = new Point2D(0.7, 0.2);
    Point2D B = new Point2D(0.5, 0.4);
    Point2D C = new Point2D(0.2, 0.3);
    Point2D D = new Point2D(0.4 ,0.7);
    Point2D E = new Point2D(0.9, 0.6);

    kdtree = new KdTree();
    kdtree.insert(A);
    kdtree.insert(B);
    kdtree.insert(C);
    kdtree.insert(D);
    kdtree.insert(E);
//    kdtree.insert(p6);
//    kdtree.insert(p7);
//    kdtree.insert(p8);
//    kdtree.insert(p9);
//    kdtree.insert(p10);

//    Point2D nearest = kdtree.nearest(new Point2D(0.159, 0.456));
    Point2D nearest = kdtree.nearest(new Point2D(0.77, 0.09));
    assertNotNull(nearest);
//    assertEquals(p5, nearest);
        assertEquals(A, nearest);

  }

  @Test
  void testRange() {
    Point2D A  = new Point2D(0.372 , 0.497);
    Point2D B  = new Point2D(0.564 ,0.413);
    Point2D C  = new Point2D(0.226 ,0.577);
    Point2D D  = new Point2D(0.144 ,0.179);
    Point2D E  = new Point2D(0.083 ,0.51);
    Point2D F  = new Point2D(0.32 ,0.708);
    Point2D G  = new Point2D(0.417 ,0.362);
    Point2D H  = new Point2D(0.862 ,0.825);
    Point2D I  = new Point2D(0.785 ,0.725);
    Point2D J  = new Point2D(0.499 ,0.208);

    RectHV rect = new RectHV(0.07, 0.17,0.38, 0.47);

    kdtree = new KdTree();

    kdtree.insert(A);
    kdtree.insert(B);
    kdtree.insert(C);
    kdtree.insert(D);
    kdtree.insert(E);
    kdtree.insert(F);
    kdtree.insert(G);
    kdtree.insert(H);
    kdtree.insert(I);
    kdtree.insert(J);

    Iterable<Point2D> it = kdtree.range(rect);
  }
}