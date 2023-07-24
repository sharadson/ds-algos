import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PercolationTest {
    private Percolation percolation;

    @Test
    void testOpen() {
        percolation =  new Percolation(20);
        percolation.open(2,3);
        boolean isOpen = percolation.isOpen(2,3);
        assertTrue(isOpen);

        percolation.open(12,3);
        isOpen = percolation.isOpen(2,3);
        assertTrue(isOpen);
    }

   /* @Test
    void openConnectsObjectOnlyWithNeighbouringOpenObjects(){
        percolation =  new Percolation(4);
        Object1 o1 = new Object1(2,2,false,false);
        Object1 o2 = new Object1(2,1,false,false);
        Object1 o3 = new Object1(0,0,false,false);
        percolation.open(o1.getRow(),o1.getCol());
        percolation.open(o2.getRow(),o2.getCol());
        percolation.open(o3.getRow(),o3.getCol());

        boolean isConnected = percolation.isConnected(o1,o3);
        assertFalse(isConnected);

        isConnected = percolation.isConnected(o1,o2);
        assertTrue(isConnected);
    }*/

    @Test
    void objectsConnectedWithTopVirtualRootAreFull(){
        percolation =  new Percolation(4);

        percolation.open(1,3);
        percolation.open(1,2);

      assertTrue(percolation.isFull(1,3));
      assertTrue(percolation.isFull(1,2));
    }

    @Test
    void openMakesAllObjectsConnectedWithTopVirtualRootFull(){

        percolation =  new Percolation(4);

        percolation.open(1,2);
        percolation.open(1,3);

        assertTrue(percolation.isFull(1,2));
        assertTrue(percolation.isFull(1,3));
    }

    @Test
    void percolatesWhenThereIsTopToBottomPathWhichIsOpenAndFull(){
        percolation =  new Percolation(4);

        percolation.open(1,1);
        percolation.open(2,1);
        percolation.open(3,1);
        percolation.open(4,1);

        boolean percolates = percolation.percolates();

        assertTrue(percolates);
    }

    @Test
    void objectsOpenedAwayFromTopVirtualRootAreNotFull(){
        percolation =  new Percolation(4);

        percolation.open(4,3);
        percolation.open(3,3);

        assertFalse(percolation.isFull(4,3));
        assertFalse(percolation.isFull(3,3));

    }

    @Test
    void testIsOpen_NoObjectIsOpenAfterInitialization() {
        percolation =  new Percolation(20);
        boolean isOpen = percolation.isOpen(3,4);
        assertTrue(!isOpen);

        isOpen = percolation.isOpen(13,5);
        assertTrue(!isOpen);
    }


    @Test
    void testNumberOfOpenSites() {

        percolation =  new Percolation(20);
        percolation.open(3,4);
        int count = percolation.numberOfOpenSites();
      assertEquals(1, count);

        percolation.open(13,4);
        count = percolation.numberOfOpenSites();
      assertEquals(2, count);

    }

    @Test
    void percolatesRecursivelyToAllNodesOpenAndFull() {
        percolation =  new Percolation(4);

        percolation.open(1,3);//2
        percolation.open(3,3);//10
        percolation.open(3,4);//11
        percolation.open(3,2);//9
        percolation.open(3,1);//8
        percolation.open(2,3);//6
        percolation.open(4,1);//12

        boolean percolates = percolation.percolates();

        assertTrue(percolates);
    }

  @Test
  void percolatesRecursivelyToAllNodesOpenAndFull1() {
        percolation =  new Percolation(4);

        percolation.open(4,3);//2
        percolation.open(2,1);//10
        percolation.open(3,3);//11
        percolation.open(2,3);//9
        percolation.open(3,2);//8
        percolation.open(1,4);//6
        percolation.open(1,3);//12

        boolean percolates = percolation.percolates();

        assertTrue(percolates);
  }
}
