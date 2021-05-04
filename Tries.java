import java.util.Arrays;

public class Tries {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        tree.insert(15, false);
        tree.insert(8,true);
        tree.insert(22,true);
        tree.insert(4,true);
        tree.insert(11,true);
        tree.insert(20,true);
        tree.insert(24,false);
        tree.insert(2,true);
        tree.insert(9,true);
        tree.insert(12,true);
        tree.insert(18,true);
        tree.insert(13,true);
        //tree.delete(24);
        int[] array = tree.keysToArray();
        boolean[] prefarray1 = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            prefarray1[i] = tree.succPrefixXor(array[i]);
        }
        boolean[] prefarray2 = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            prefarray2[i] = tree.prefixXor3(array[i]);
        }
        System.out.println(Arrays.toString(array));
        System.out.println(Arrays.toString(prefarray1));
        System.out.println(Arrays.toString(prefarray2));
        System.out.println(tree.prefixXor3(15));
    }
}
