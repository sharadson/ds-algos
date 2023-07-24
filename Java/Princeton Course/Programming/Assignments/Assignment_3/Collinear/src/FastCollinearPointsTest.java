import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FastCollinearPointsTest {
    @BeforeEach
    void setUp(){

    }

    @Test
    void whenSinglePointIsSentNoSegmentIsReturned(){
        Point[] points = new Point[1];
        points[0] = new Point(1,1);
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        assertEquals(0,fastCollinearPoints.numberOfSegments());
    }

    @Test
    public void whenNullPointIsSentIllegalArgumentExceptionIsThrown(){
        Point[] points = new Point[3];
        points[0] = new Point(1,1);
        points[1] = null;
        points[2] = new Point(2,2);
        assertThrows(java.lang.IllegalArgumentException.class, () -> new FastCollinearPoints(points));
    }

    @Test
    public void whenNullArrayIsPassedIllegalArgumentExceptionIsThrown(){
        Point[] points = null;
        assertThrows(java.lang.IllegalArgumentException.class, () -> new FastCollinearPoints(points));
    }

    @Test
    public void whenDuplicatePointIsPassedIllegalArgumentExceptionIsThrown(){
        Point[] points = new Point[4];
        points[0] = new Point(1,1);
        points[1] = new Point(2,1);
        points[2] = new Point(3,1);
        points[3] = new Point(1,1);

        assertThrows(java.lang.IllegalArgumentException.class, () -> new FastCollinearPoints(points));
    }

    @Test
    public void whenFourCollinearPointsAreSentOneSegmentIsReturned(){
        Point[] points = new Point[4];
        points[0] = new Point(1,1);
        points[1] = new Point(2,2);
        points[2] = new Point(3,3);
        points[3] = new Point(4,4);
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        LineSegment[] segments = fastCollinearPoints.segments();
        assertEquals(1, fastCollinearPoints.numberOfSegments());
        assertEquals(1, segments.length);
    }

    @Test
    public void whenFourCollinearPointsAreSentCorrectSegmentStartAndEndIsReturned(){
        Point[] points = new Point[4];
        points[0] = new Point(3,3);
        points[1] = new Point(2,2);
        points[2] = new Point(1,1);
        points[3] = new Point(4,4);

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        LineSegment[] segments = bruteCollinearPoints.segments();
        LineSegment segment = segments[0];
        assertEquals(points[2]+" -> "+points[3],segment.toString());
    }

    @Test
    public void checkIfFourPointsAreCollinear(){
        Point[] points = new Point[4];
        points[0] = new Point(3,2);
        points[1] = new Point(2,2);
        points[2] = new Point(1,1);
        points[3] = new Point(4,4);
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

        assertEquals(0,fastCollinearPoints.numberOfSegments());

        points[0] = new Point(3,3);
        points[1] = new Point(2,2);
        points[2] = new Point(1,1);
        points[3] = new Point(4,4);
        fastCollinearPoints = new FastCollinearPoints(points);

        assertEquals(1,fastCollinearPoints.numberOfSegments());
    }

    @Test
    public void checkIfThereAreFourCollinearPointsInALargerSetOfPoints(){
        Point[] points = new Point[6];
        points[0] = new Point(8,7);
        points[1] = new Point(2,2);
        points[2] = new Point(1,1);
        points[3] = new Point(4,4);
        points[4] = new Point(1,7);
        points[5] = new Point(3,3);

        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);

        assertEquals(1,fastCollinearPoints.numberOfSegments());
        LineSegment segment = fastCollinearPoints.segments()[0];
        assertEquals(points[2]+" -> "+points[3],segment.toString());


        points = new Point[8];
        points[0] = new Point(8,7);
        points[1] = new Point(2,2);
        points[2] = new Point(1,1);
        points[3] = new Point(11,18);
        points[4] = new Point(31,67);
        points[5] = new Point(4,4);
        points[6] = new Point(1,7);
        points[7] = new Point(1,77);

        fastCollinearPoints = new FastCollinearPoints(points);
        assertEquals(0,fastCollinearPoints.numberOfSegments());

        points = new Point[9];
        points[0] = new Point(8,7);
        points[1] = new Point(2,2);
        points[2] = new Point(1,1);
        points[3] = new Point(11,18);
        points[4] = new Point(31,67);
        points[5] = new Point(4,4);
        points[6] = new Point(1,7);
        points[7] = new Point(1,77);
        points[8] = new Point(3,3);

        fastCollinearPoints = new FastCollinearPoints(points);
        assertEquals(1,fastCollinearPoints.numberOfSegments());
    }

    @Test
    public void checkForTwoCollinearLineSegments(){
        Point[] points = new Point[13];
        points[0] = new Point(8,7);
        points[1] = new Point(2,2);
        points[2] = new Point(1,1);
        points[3] = new Point(11,18);
        points[4] = new Point(31,67);
        points[5] = new Point(4,4);
        points[6] = new Point(1,7);
        points[7] = new Point(3,77);
        points[8] = new Point(1,2);
        points[9] = new Point(2,3);
        points[10] = new Point(3,3);
        points[11] = new Point(3,4);
        points[12] = new Point(4,5);

        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        assertEquals(2,fastCollinearPoints.numberOfSegments());
    }

}
