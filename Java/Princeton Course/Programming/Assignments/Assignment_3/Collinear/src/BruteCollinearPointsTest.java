import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BruteCollinearPointsTest {
  @BeforeEach
  public void setUp(){

  }

  @Test
  public void whenSinglePointIsSentNoSegmentIsReturned(){
    Point[] points = new Point[1];
    points[0] = new Point(1,1);
    BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
    assertEquals(0,bruteCollinearPoints.numberOfSegments());
  }

  @Test
  public void whenNullPointIsSentIllegalArgumentExceptionIsThrown(){
    Point[] points = new Point[3];
    points[0] = new Point(1,1);
    points[1] = null;
    points[2] = new Point(2,2);
    assertThrows(java.lang.IllegalArgumentException.class, () -> new BruteCollinearPoints(points));
  }

  @Test
  public void whenDuplicatePointIsPassedIllegalArgumentExceptionIsThrown(){
    Point[] points = new Point[4];
    points[0] = new Point(1,1);
    points[1] = new Point(2,1);
    points[2] = new Point(3,1);
    points[3] = new Point(1,1);

    assertThrows(java.lang.IllegalArgumentException.class, () -> new BruteCollinearPoints(points));
  }

  @Test
  public void whenNullArrayIsPassedIllegalArgumentExceptionIsThrown(){
    Point[] points = null;
    assertThrows(java.lang.IllegalArgumentException.class, () -> new BruteCollinearPoints(points));
  }

  @Test
  public void whenFourCollinearPointsAreSentOneSegmentIsReturned(){
    Point[] points = new Point[4];
    points[0] = new Point(1,1);
    points[1] = new Point(2,2);
    points[2] = new Point(3,3);
    points[3] = new Point(4,4);
    BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
    LineSegment[] segments = bruteCollinearPoints.segments();
    assertEquals(1, bruteCollinearPoints.numberOfSegments());
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
    BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);

    assertEquals(0,bruteCollinearPoints.numberOfSegments());

    points[0] = new Point(3,3);
    points[1] = new Point(2,2);
    points[2] = new Point(1,1);
    points[3] = new Point(4,4);
    bruteCollinearPoints = new BruteCollinearPoints(points);

    assertEquals(1,bruteCollinearPoints.numberOfSegments());
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

    BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);

    LineSegment[] segments = bruteCollinearPoints.segments();
    LineSegment segment = segments[0];
    assertEquals(points[2]+" -> "+points[3],segment.toString());
    assertEquals(1,bruteCollinearPoints.numberOfSegments());

    points = new Point[8];
    points[0] = new Point(8,7);
    points[1] = new Point(2,2);
    points[2] = new Point(1,1);
    points[3] = new Point(11,18);
    points[4] = new Point(31,67);
    points[5] = new Point(4,4);
    points[6] = new Point(1,7);
    points[7] = new Point(1,77);

    bruteCollinearPoints = new BruteCollinearPoints(points);
    assertEquals(0,bruteCollinearPoints.numberOfSegments());

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

    bruteCollinearPoints = new BruteCollinearPoints(points);
    assertEquals(1,bruteCollinearPoints.numberOfSegments());
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

    BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
    assertEquals(2,bruteCollinearPoints.numberOfSegments());
  }
}
