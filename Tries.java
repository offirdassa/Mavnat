
public class Tries {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        tree.insert(15, false);
        tree.insert(8,true);
        tree.insert(22,true);
        tree.insert(4,true);
        tree.insert(11,true);
        tree.insert(20,true);
        tree.insert(24,true);
        tree.insert(2,true);
        tree.insert(9,true);
        tree.insert(12,true);
        tree.insert(18,true);
        tree.insert(13,true);
        //tree.delete(24);
        System.out.println(tree.prefixXor2(15));
    }
}
