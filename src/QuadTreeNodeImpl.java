// CIS 121, QuadTree

public class QuadTreeNodeImpl implements QuadTreeNode {

    private int value;
    private QuadTreeNodeImpl[] tree;
    private boolean leaf;
    private final int hgt;
    private final int totalLength;


    private QuadTreeNodeImpl(int value, int hgt, int totalLength) {
        leaf = true;
        this.value = value;
        this.hgt = hgt;
        this.totalLength = totalLength;
    }

    private QuadTreeNodeImpl(QuadTreeNodeImpl[] tree, int hgt, int totalLength) {
        leaf = false;
        this.tree = tree;
        this.hgt = hgt;
        this.totalLength = totalLength;
    }

    private int getValue() {
        return value;
    }

    private static int[][] divideArray(int[][] array, int position) {
        int arrayHalf = array.length / 2;
        int[][] map = new int[arrayHalf][arrayHalf];
        for (int i = 0; i < arrayHalf; i++) {
            for (int j = 0; j < arrayHalf; j++) {
                if (position == 0) {
                    map[j][i] = array[j][i];
                } else if (position == 1) {
                    map[j][i] = array[j][i + arrayHalf];
                } else if (position == 2) {
                    map[j][i] = array[j + arrayHalf][i];
                } else {
                    map[j][i] = array[j + arrayHalf][i + arrayHalf];
                }
            }
        }
        return map;
    }

    private static QuadTreeNodeImpl recArray(int[][] imageSmall, int height) {
        int imageLength = ((int) Math.pow(2, height - 1)) * imageSmall.length;
        if (imageSmall.length == 1) {
            return new QuadTreeNodeImpl(imageSmall[0][0], height, imageLength);
        }

        QuadTreeNodeImpl[] map = new QuadTreeNodeImpl[4];

        for (int i = 0; i < 4; i++) {
            map[i] = recArray(divideArray(imageSmall, i), height + 1);
        }

        if (map[0].isLeaf() && map[1].isLeaf() && map[2].isLeaf() && map[3].isLeaf()) {

            if (map[0].getValue() == map[1].getValue()
                    && map[0].getValue() == map[2].getValue()
                    && map[0].getValue() == map[3].getValue()) {
                return new QuadTreeNodeImpl(imageSmall[0][0], height, imageLength);
            }
        }
        return new QuadTreeNodeImpl(map, height, imageLength);
    }

    /**
     * ! Do not delete this method !
     * Please implement your logic inside this method without modifying the signature
     * of this method, or else your code won't compile.
     * <p/>
     * As always, if you want to create another method, make sure it is not public.
     *
     * @param image image to put into the tree
     * @return the newly build QuadTreeNod e instance which stores the compressed image
     * @throws IllegalArgumentException if image is null
     * @throws IllegalArgumentException if image is empty
     * @throws IllegalArgumentException if image.length is not a power of 2
     * @throws IllegalArgumentException if image, the 2d-array, is not a perfect square
     */
    public static QuadTreeNodeImpl buildFromIntArray(int[][] image) {
        if (image == null) {
            throw new IllegalArgumentException();
        }
        if (image.length == 0) {
            throw new IllegalArgumentException();
        }

        int zeroRowLength = image[0].length;
        if (zeroRowLength != image.length) {
            throw new IllegalArgumentException();
        }
        for (int[] ints : image) {
            if (zeroRowLength != ints.length) {
                throw new IllegalArgumentException();
            }
        }

        if ((int) (Math.ceil(Math.log(image.length) / Math.log(2)))
                != (int) Math.floor((Math.log(image.length) / Math.log(2)))) {
            throw new IllegalArgumentException();
        }

        return recArray(image, 1);
    }

    @Override
    public int getColor(int x, int y) {
        int mapLength = (totalLength / (int) (Math.pow(2, hgt - 1)));
        if (x > mapLength - 1 || x < 0 || y > mapLength - 1 || y < 0) {
            throw new IllegalArgumentException();
        }

        int color;

        if (isLeaf()) {
            return value;
        } else {
            if (x < mapLength / 2 && y < mapLength / 2) {
                color = tree[0].getColor(x, y);
            } else if (x > mapLength / 2 - 1 && y < mapLength / 2) {
                color = tree[1].getColor(x - mapLength / 2, y);
            } else if (x < mapLength / 2 && y > mapLength / 2 - 1) {
                color = tree[2].getColor(x, y - mapLength / 2);
            } else {
                color = tree[3].getColor(x - mapLength / 2, y - mapLength / 2);
            }
        }

        return color;
    }

    @Override
    public void setColor(int x, int y, int c) {
        int mapLength = (totalLength / (int) (Math.pow(2, hgt - 1)));
        if (x > mapLength - 1 || x < 0 || y > mapLength - 1 || y < 0) {
            throw new IllegalArgumentException();
        }
        if (mapLength == 1) {
            this.value = c;
            return;
        }

        if (isLeaf()) {
            this.leaf = false;
            tree = new QuadTreeNodeImpl[4];
            for (int i = 0; i < 4; i++) {
                tree[i] = new QuadTreeNodeImpl(value, this.hgt + 1, totalLength);
            }
        }

        if (x < mapLength / 2 && y < mapLength / 2) {
            tree[0].setColor(x, y, c);
        } else if (x > mapLength / 2 - 1 && y < mapLength / 2) {
            tree[1].setColor(x - mapLength / 2, y, c);
        } else if (x < mapLength / 2 && y > mapLength / 2 - 1) {
            tree[2].setColor(x, y - mapLength / 2, c);
        } else {
            tree[3].setColor(x - mapLength / 2, y - mapLength / 2, c);
        }

        if (tree[0].isLeaf() && tree[1].isLeaf() && tree[2].isLeaf() && tree[3].isLeaf()) {

            if (tree[0].getValue() == tree[1].getValue()
                    && tree[0].getValue() == tree[2].getValue()
                    && tree[0].getValue() == tree[3].getValue()) {
                this.leaf = true;
                this.value = tree[0].getValue();
                tree = null;
            }
        }
    }

    @Override
    public QuadTreeNode getQuadrant(QuadName quadrant) {
        if (quadrant == null) {
            return null;
        }
        if (isLeaf()) {
            return null;
        }
        switch (quadrant) {
            case TOP_LEFT:
                return tree[0];

            case TOP_RIGHT:
                return tree[1];

            case BOTTOM_LEFT:
                return tree[2];

            case BOTTOM_RIGHT:
                return tree[3];

            default:
                return null;
        }
    }

    @Override
    public int getDimension() {
        return totalLength / (int) (Math.pow(2, hgt - 1));
    }

    @Override
    public int getSize() {
        if (isLeaf()) {
            return 1;
        }

        int counter = 1;

        for (int i = 0; i < 4; i++) {
            counter += tree[i].getSize();
        }

        return counter;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public int[][] decompress() {
        int mapLength = (totalLength / (int) (Math.pow(2, hgt - 1)));
        int[][] map = new int[mapLength][mapLength];

        if (isLeaf()) {
            for (int x = 0; x < mapLength; x++) {
                for (int y = 0; y < mapLength; y++) {
                    map[y][x] = value;
                }
            }
        } else {
            int[][] subMap;

            subMap = tree[0].decompress();
            for (int x = 0; x < mapLength / 2; x++) {
                for (int y = 0; y < mapLength / 2; y++) {
                    map[y][x] = subMap[y][x];
                }
            }

            subMap = tree[1].decompress();
            for (int x = mapLength / 2; x < mapLength; x++) {
                for (int y = 0; y < mapLength / 2; y++) {
                    map[y][x] = subMap[y][x - mapLength / 2];
                }
            }

            subMap = tree[2].decompress();
            for (int x = 0; x < mapLength / 2; x++) {
                for (int y = mapLength / 2; y < mapLength; y++) {
                    map[y][x] = subMap[y - mapLength / 2][x];
                }
            }

            subMap = tree[3].decompress();
            for (int x = mapLength / 2; x < mapLength; x++) {
                for (int y = mapLength / 2; y < mapLength; y++) {
                    map[y][x] = subMap[y - mapLength / 2][x - mapLength / 2];
                }
            }
        }
        return map;
    }

    @Override
    public double getCompressionRatio() {
        return ((double) getSize()) / (Math.pow(getDimension(), 2));
    }
}