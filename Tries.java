import java.util.Arrays;

import java.util.*;

public class Tries {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        /*for (int i = 0; i < 50; i++) tree.insert(i, true);
        Random random = new Random();
        int nextInt;
        int cnt = 0;
        while (cnt != 50) {
            nextInt = random.nextInt();
            if (nextInt >= 1 && tree.search(nextInt) == null) {
                tree.insert(nextInt, true);
                cnt++;
            }
        }*/
        tree.insert(3,true);
        System.out.println(tree.successor(tree.getRoot()));
//        tree.insert(15, false);
//        tree.insert(10,true);
//        tree.insert(22,true);
//        tree.insert(4,true);
//        tree.insert(11,true);
//        tree.insert(20,true);
//        tree.insert(24,false);
//        tree.insert(2,true);
//        tree.insert(7,true);
//        tree.insert(12,true);
//        tree.insert(18,true);
//        tree.insert(1,true);
//        tree.insert(6,true);
//        tree.insert(8,true);
//        System.out.println(tree.delete(15));
//
//        System.out.println(tree.insert(5,true));

/*

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
        System.out.println(Arrays.equals(prefarray2,prefarray1));
        System.out.println(tree.prefixXor3(10));

        //int[] arr = {15,4,8,10,22,11,20,24,2,7,12,18,1,6};
        int[] arr = {15,4,8,10,22};
        int cnt = 1;
        while (cnt != 0) {
            String[] res = RandomInsert(arr,tree);
            if(!PrefixTest(tree)) System.out.println("problem");
            for (int j : arr) {
                tree.delete(j);
            }
            cnt--;
        }
    }

    public static boolean PrefixTest(AVLTree tree) {
        int[] array = tree.keysToArray();
        boolean[] prefarray1 = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            prefarray1[i] = tree.succPrefixXor(array[i]);
        }
        boolean[] prefarray2 = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            prefarray2[i] = tree.prefixXor(array[i]);
        }
        for (int i = 0; i < array.length; i++) {
            if (prefarray1[i] != prefarray2[i]) return false;
        }
        return true;
    }

    public static String[] RandomInsert(int[] keys, AVLTree tree) {
        int n = keys.length;
        int cnt = n;
        Random num = new Random();
        String[] resArray = new String[n];
        int resIndex = 0;
        while (cnt != 0) {
            int Index = num.nextInt(n);
            int boolint = num.nextInt(2);
            boolean bool = false;
            if (boolint == 1) {
                bool = true;
            }
            int k = keys[Index];
            if (keys[Index] != -1) {
                tree.insert(k, bool);
                System.out.println(k + "," + bool);
                keys[Index] = -1;
                resArray[resIndex] = k + "," + bool;
                resIndex++;
                cnt--;
            }
        }
        return resArray;*/


    }

}
