import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuadTreeNodeImplTest {
    private int[][] smallImage;
    private int[][] bigImage;
    private int[][] singlePixelImage;
    private int[][] notTwoSquaredImage;
    private int[][] emptyImage;
    private int[][] nullImage;
    private int[][] uniColorImage;
    private int[][] rectangleImage;
    private int[][] jaggedImage;

    @Before
    public void setupTestMazes() {
        smallImage = new int[][]{
                {1, 0, 0, 3},
                {1, 1, 0, 0},
                {0, 0, 2, 2},
                {0, 0, 2, 2}
        };

        bigImage = new int[][]{
                {1, 1, 1, 0, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 1},
        };

        singlePixelImage = new int[][]{{1}};

        notTwoSquaredImage = new int[][]{
                {1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0},
                {1, 1, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
        };

        emptyImage = new int[][]{{}};

        nullImage = null;

        uniColorImage = new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        rectangleImage = new int[][]{
                {0, 0, 0, 0}
        };

        jaggedImage = new int[][]{
                {0, 0, 0, 0},
                {0, 0}
        };
    }

    //buildFromIntArray()
    @Test(expected = IllegalArgumentException.class)
    public void testReturnsNotTwoSquaredImage() {
        QuadTreeNodeImpl.buildFromIntArray(notTwoSquaredImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnsEmptyImage() {
        QuadTreeNodeImpl.buildFromIntArray(emptyImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnsNullImage() {
        QuadTreeNodeImpl.buildFromIntArray(nullImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnsRectangleImage() {
        QuadTreeNodeImpl.buildFromIntArray(rectangleImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnsJaggedImage() {
        QuadTreeNodeImpl.buildFromIntArray(jaggedImage);
    }

    //getSize()
    @Test
    public void testReturnsNodeSizeSmallImage() {
        assertEquals(13, QuadTreeNodeImpl.buildFromIntArray(smallImage)
                .getSize());
    }

    @Test
    public void testReturnsNodeSizeSinglePixelImage() {
        assertEquals(1, QuadTreeNodeImpl.buildFromIntArray(singlePixelImage)
                .getSize());
    }

    @Test
    public void testReturnsNodeSizeUniColorImage() {
        assertEquals(1, QuadTreeNodeImpl.buildFromIntArray(uniColorImage)
                .getSize());
    }

    //getQuadrant()
    @Test
    public void testReturnsNodeSizeSmallImageQuadrant() {
        assertEquals(5, QuadTreeNodeImpl.buildFromIntArray(smallImage)
                .getQuadrant(QuadTreeNode.QuadName.TOP_LEFT).getSize());
    }


    //getDimension()
    @Test
    public void testReturnsNodeDimensionSmallImageQuadrant() {
        assertEquals(2, QuadTreeNodeImpl.buildFromIntArray(smallImage)
                .getQuadrant(QuadTreeNode.QuadName.TOP_LEFT).getDimension());
    }

    //getCompressionRatio()
    @Test
    public void testReturnsCompressionRatioSmallImage() {
        assertEquals((double) 13 / (double) 16, QuadTreeNodeImpl.buildFromIntArray(smallImage)
                .getCompressionRatio(), 0.0);
    }

    //getColor()
    @Test
    public void testReturnsGetColorSmallImage() {
        assertEquals(2, QuadTreeNodeImpl.buildFromIntArray(smallImage)
                .getColor(3, 3));
    }

    @Test
    public void testReturnsGetColorSmallImageQuadrant() {
        assertEquals(3, QuadTreeNodeImpl.buildFromIntArray(smallImage)
                .getQuadrant(QuadTreeNode.QuadName.TOP_RIGHT).getColor(1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnsGetColorOOBSmallImage() {
        QuadTreeNodeImpl.buildFromIntArray(smallImage)
                .getQuadrant(QuadTreeNode.QuadName.TOP_RIGHT).getColor(4, 4);
    }

    //setColor()
    @Test
    public void testReturnsSetColorWithoutChangeSmallImage() {
        QuadTreeNodeImpl test = QuadTreeNodeImpl.buildFromIntArray(smallImage);
        test.setColor(3, 0, 2);
        assertEquals(13, test.getSize());
        assertEquals(2, test.getColor(3, 0));
    }

    @Test
    public void testReturnsSetColorMergeSmallImage() {
        QuadTreeNodeImpl test = QuadTreeNodeImpl.buildFromIntArray(smallImage);
        test.setColor(1, 0, 1);
        assertEquals(9, test.getSize());
        assertEquals(1, test.getColor(1, 0));
    }

    @Test
    public void testReturnsSetColorDivideSmallImage() {
        QuadTreeNodeImpl test = QuadTreeNodeImpl.buildFromIntArray(smallImage);
        test.setColor(0, 3, 1);
        assertEquals(17, test.getSize());
        assertEquals(1, test.getColor(0, 3));
    }

    //decompress()
    @Test
    public void testReturnsDecompressSmallImage() {
        QuadTreeNodeImpl test = QuadTreeNodeImpl.buildFromIntArray(smallImage);
        int[][] testArr = test.decompress();
        assertArrayEquals(smallImage, testArr);
    }

    @Test
    public void testReturnsDecompressBigImage() {
        QuadTreeNodeImpl test = QuadTreeNodeImpl.buildFromIntArray(bigImage);
        int[][] testArr = test.decompress();
        assertArrayEquals(bigImage, testArr);
    }

    @Test
    public void testReturnsSetColorDecompressWithoutChangeSmallImage() {
        int[][] solutionImage = {
                {1, 0, 0, 2},
                {1, 1, 0, 0},
                {0, 0, 2, 2},
                {0, 0, 2, 2}
        };
        QuadTreeNodeImpl test = QuadTreeNodeImpl.buildFromIntArray(smallImage);
        test.setColor(3, 0, 2);
        int[][] testArr = test.decompress();
        assertArrayEquals(solutionImage, testArr);
    }

    @Test
    public void testReturnsSetColorMergeDecompressSmallImage() {
        int[][] solutionImage = {
                {1, 1, 0, 3},
                {1, 1, 0, 0},
                {0, 0, 2, 2},
                {0, 0, 2, 2}
        };
        QuadTreeNodeImpl test = QuadTreeNodeImpl.buildFromIntArray(smallImage);
        test.setColor(1, 0, 1);
        int[][] testArr = test.decompress();
        assertArrayEquals(solutionImage, testArr);
    }

    @Test
    public void testReturnsSetColorDivideDecompressSmallImage() {
        int[][] solutionImage = {
                {1, 0, 0, 3},
                {1, 1, 0, 0},
                {0, 0, 2, 2},
                {1, 0, 2, 2}
        };
        QuadTreeNodeImpl test = QuadTreeNodeImpl.buildFromIntArray(smallImage);
        test.setColor(0, 3, 1);
        int[][] testArr = test.decompress();
        assertArrayEquals(solutionImage, testArr);
    }
}