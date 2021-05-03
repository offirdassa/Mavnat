
public class Tries {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        tree.insert(15, false);
        tree.insert(22,true);
        tree.insert(10,true);
        tree.insert(24,true);
        tree.insert(20,true);
        tree.insert(4,true);
        tree.insert(11,true);
        tree.insert(18,true);
        tree.insert(12,true);
        tree.insert(2,true);
        tree.insert(7,true);
        tree.insert(1,true);
        tree.insert(6,true);
        tree.insert(8,true);
        System.out.println(tree.insert(5,true));
        System.out.println('a');
    }
}
