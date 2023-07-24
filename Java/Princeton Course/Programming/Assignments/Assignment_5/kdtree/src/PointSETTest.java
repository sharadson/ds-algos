import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PointSETTest {
    private PointSET pointSET;
    private Point2D point2D1;
    private Point2D point2D2;

    @BeforeEach
    void setUp() {
        pointSET = new PointSET();
        point2D1 = new Point2D(.1,.1);
        point2D2 = new Point2D(.2,.2);
        Point2D point2D3 = new Point2D(.3, .3);
        Point2D point2D4 = new Point2D(.4, .4);
        Point2D point2D5 = new Point2D(.5, .5);
        pointSET.insert(point2D2);
        pointSET.insert(point2D3);
        pointSET.insert(point2D4);
        pointSET.insert(point2D1);
        pointSET.insert(point2D5);
    }

    @Test
    void whenNoElementsAreInsertedSizeReturnsZero() {
        PointSET pointSET = new PointSET();
        assertEquals(pointSET.size(),0);
        assertTrue(pointSET.isEmpty());
    }

    @Test
    void whenElementsAreEnteredTheyReflectInTypesSize() {
        assertEquals(5,pointSET.size());
    }

    /*@Test
    void whenPointsAreEnteredTheyCanBeDrawn() {
        pointSET.draw();
    }*/

    @Test
    void whenRectHVHasPointsWithinItNonEmptyIteratorIsReturned() {
        RectHV rectHV = new RectHV(.15,.15,.45,.45);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertTrue(iterable.iterator().hasNext());
    }

    @Test
    void whenRectHVHasNoPointsWithinItAnEmptyIteratorIsReturned() {
        RectHV rectHV = new RectHV(.6,.6,14,14);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    void whenRectHVHasNoFloorButHasCeilingAndPointsWithinItThenNonEmptyIteratorIsReturned() {
        RectHV rectHV = new RectHV(0.05,0.05,.45,.45);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertTrue(iterable.iterator().hasNext());
    }

    @Test
    void whenRectHVHasFloorAndPointsWithinItButHasNoCeilingThenNonEmptyIteratorIsReturned() {

        RectHV rectHV = new RectHV(.15,.15,.6,.6);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertTrue(iterable.iterator().hasNext());

    }

    @Test
    void whenRectHVHasBothFloorAndCeilingButNoPointsWithinItAnEmptyIteratorIsReturned() {

        pointSET = new PointSET();
        Point2D point2D1 = new Point2D(.1,.1);
        Point2D point2D5 = new Point2D(.5,.5);

        pointSET.insert(point2D1);
        pointSET.insert(point2D5);

        RectHV rectHV = new RectHV(.2,.2,.4,.4);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    void whenRectHVHasBothFloorAndCeilingAndPointsWithinThemButOutsideRectHVRangeThenAnEmptyIteratorIsReturned() {
        pointSET = new PointSET();
        Point2D point2D1 = new Point2D(.1,.1);
        Point2D point2D5 = new Point2D(.5,.5);
        Point2D point2DOutside1 = new Point2D(.25,.5);
        Point2D point2DOutside2 = new Point2D(.15,.35);

        pointSET.insert(point2D1);
        pointSET.insert(point2D5);
        pointSET.insert(point2DOutside1);
        pointSET.insert(point2DOutside2);

        RectHV rectHV = new RectHV(2,2,4,4);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    void whenRectHVHasPointsOnItsEdgesThenNonEmptyIteratorIsReturned() {
        pointSET = new PointSET();
        Point2D point2D1 = new Point2D(.25,.2);
        Point2D point2D5 = new Point2D(.4,.25);

        pointSET.insert(point2D1);
        pointSET.insert(point2D5);

        RectHV rectHV = new RectHV(.2,.2,.4,.4);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertTrue(iterable.iterator().hasNext());
    }

    @Test
    void whenRectHVFloorAndCeilingAreOnRectHVAreIncludedInPointsTheIteratorReturnedIsNonEmpty() {
        pointSET = new PointSET();
        Point2D point2D1 = new Point2D(.2,.2);
        Point2D point2D5 = new Point2D(.4,.4);

        pointSET.insert(point2D1);
        pointSET.insert(point2D5);

        RectHV rectHV = new RectHV(.2,.2,.4,.4);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertTrue(iterable.iterator().hasNext());
        assertEquals(2,iterable.spliterator().getExactSizeIfKnown());
    }

    @Test
    void whenRectHVFloorIsOnItAndHasNoCeilingTheIteratorIsNonEmptyWithFloorInIt() {
        pointSET = new PointSET();
        Point2D point2D1 = new Point2D(.2,.2);

        pointSET.insert(point2D1);

        RectHV rectHV = new RectHV(.2,.2,.4,.4);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertTrue(iterable.iterator().hasNext());
        assertEquals(1,iterable.spliterator().getExactSizeIfKnown());
    }

    @Test
    void whenRectHVCeilingIsOnItAndHasNoFloorTheIteratorIsNonEmptyWithCeilingInIt() {
        pointSET = new PointSET();
        Point2D point2D1 = new Point2D(.4,.4);

        pointSET.insert(point2D1);

        RectHV rectHV = new RectHV(.2,.2,.4,.4);
        Iterable<Point2D> iterable = pointSET.range(rectHV);
        assertTrue(iterable.iterator().hasNext());
        assertEquals(1,iterable.spliterator().getExactSizeIfKnown());
    }

    @Test
    void whenPointsAreAddedToSetNearestReturnsOneWhichIsCloseByOfAnyGivenPoint() {
        Point2D point = new Point2D(.05,.05);
        assertEquals(point2D1, pointSET.nearest(point));
        assertNotSame(point2D2, pointSET.nearest(point));
    }

    @Test
    void test() {
        RectHV rect;
        Point2D p1 = new Point2D(0.25, 0.5);
        Point2D p2 = new Point2D(0.5625, 0.8125);
        Point2D p3 = new Point2D(0.625, 0.875);

        pointSET = new PointSET();
        pointSET.insert(p1);
        pointSET.insert(p2);
        pointSET.insert(p3);


        rect = new RectHV(0.09375, 0, 0.15625, 0.03125);
        p1 = new Point2D(0.125, 0.0);
        p2 = new Point2D(0.8125, 0.0);
        p3 = new Point2D(0.875, 0.0);

        pointSET = new PointSET();
        pointSET.insert(p1);
        pointSET.insert(p2);
        pointSET.insert(p3);

        Iterable<Point2D> it = pointSET.range(rect);
        for (Point2D p :
                it) {
            System.out.println(p.toString());
        }

    }


}