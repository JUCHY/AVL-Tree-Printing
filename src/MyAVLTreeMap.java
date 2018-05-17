
import net.datastructures.*;
import java.util.Comparator;


public class MyAVLTreeMap<K,V> extends TreeMap<K,V> {
	
  /** Constructs an empty map using the natural ordering of keys. */
  public MyAVLTreeMap() { super(); }

  /**
   * Constructs an empty map using the given comparator to order keys.
   * @param comp comparator defining the order of keys in the map
   */
  public MyAVLTreeMap(Comparator<K> comp) { super(comp); }

  /** Returns the height of the given tree position. */
  protected int height(Position<Entry<K,V>> p) {
    return tree.getAux(p);
  }

  /** Recomputes the height of the given position based on its children's heights. */
  protected void recomputeHeight(Position<Entry<K,V>> p) {
    tree.setAux(p, 1 + Math.max(height(left(p)), height(right(p))));
  }

  /** Returns whether a position has balance factor between -1 and 1 inclusive. */
  protected boolean isBalanced(Position<Entry<K,V>> p) {
    return Math.abs(height(left(p)) - height(right(p))) <= 1;
  }

  /** Returns a child of p with height no smaller than that of the other child. */
  protected Position<Entry<K,V>> tallerChild(Position<Entry<K,V>> p) {
    if (height(left(p)) > height(right(p))) return left(p);     // clear winner
    if (height(left(p)) < height(right(p))) return right(p);    // clear winner
    // equal height children; break tie while matching parent's orientation
    if (isRoot(p)) return left(p);                 // choice is irrelevant
    if (p == left(parent(p))) return left(p);      // return aligned child
    else return right(p);
  }

  /**
   * Utility used to rebalance after an insert or removal operation. This traverses the
   * path upward from p, performing a trinode restructuring when imbalance is found,
   * continuing until balance is restored.
   */
  protected void rebalance(Position<Entry<K,V>> p) {
    int oldHeight, newHeight;
    do {
      oldHeight = height(p);                       // not yet recalculated if internal
      if (!isBalanced(p)) {                        // imbalance detected
        // perform trinode restructuring, setting p to resulting root,
        // and recompute new local heights after the restructuring
        p = restructure(tallerChild(tallerChild(p)));
        recomputeHeight(left(p));
        recomputeHeight(right(p));
      }
      recomputeHeight(p);
      newHeight = height(p);
      p = parent(p);
    } while (oldHeight != newHeight && p != null);
  }

  /** Overrides the TreeMap rebalancing hook that is called after an insertion. */
  @Override
  protected void rebalanceInsert(Position<Entry<K,V>> p) {
    rebalance(p);
  }

  /** Overrides the TreeMap rebalancing hook that is called after a deletion. */
  @Override
  protected void rebalanceDelete(Position<Entry<K,V>> p) {
    if (!isRoot(p))
      rebalance(parent(p));
  }

  /** Ensure that current tree structure is valid AVL (for debug use only). */
  private boolean sanityCheck() {
    for (Position<Entry<K,V>> p : tree.positions()) {
      if (isInternal(p)) {
        if (p.getElement() == null)
          System.out.println("VIOLATION: Internal node has null entry");
        else if (height(p) != 1 + Math.max(height(left(p)), height(right(p)))) {
          System.out.println("VIOLATION: AVL unbalanced node with key " + p.getElement().getKey());
          dump();
          return false;
        }
      }
    }
    return true;
  }
  //exponent function for determing space between trees
  //you wil need 2^(remainingheight) more space in between the next nodes, to prevent overlapping nodes
 static int exponent(int num,int exp) {
	 	if(exp <0) return 1;
	    if(exp == 0) { return 1;}
	    if (exp%2 == 0) {
	        int y = exponent(num,exp/2);
	        return y*y;
	    }
	    else {
	        int y = exponent(num, (exp-1)/2);
	        return num*y*y;
	    }
 }
String[][] preorder(Position<Entry<K,V>> p, String[][] tree, int mid,int treeheight,int height) {
	int i = height;
	int space = exponent(2,treeheight);
	space = space/2;
	if(p!= null) {
		
		  if(p.getElement()!=null) {
			 //System.out.println("Value: " + p.getElement()+ "@: " + i + mid);
				 if(i<tree.length) {
					  tree[i][mid] = (String) p.getElement().getKey();
				  
		  }
	}
		  preorder(left(p),tree,mid -space, treeheight -1,  i +2,0);
		  preorder(right(p), tree,mid +space,treeheight-1, i+2,1);
	}
	return tree;
	  
  }
//type 0 = left tree, type 1 = right tree
String[][] preorder(Position<Entry<K,V>> p, String[][] tree, int mid,int treeheight, int height, int type) {
	int i = height;
	int space = exponent(2,treeheight);
	space = space/2;
	
	if(p!= null) {
		
		  if(p.getElement()!=null) {
			 //System.out.println("Value: " + p.getElement()+ "@: " + i + " , "+ mid);
			  if(i<tree.length) {
				  tree[i][mid] = (String) p.getElement().getKey();
				 if(type == 0) {
				  		 tree[i-1][mid+1] = "/";
				  		// System.out.println("Value: " + "/"+ "@: " + i + " , " + mid);
				  	}
				  	else {
				  		 tree[i-1][mid-1] = "\\";
				  		//System.out.println("Value: " + "\\"+ "@: " + i +" , "+ mid);
				  	}
			  }		 	
				 
	}
		  preorder(left(p),tree,mid -space,treeheight-1,  i +2,0);
		  preorder(right(p), tree,mid +space,treeheight -1, i+2,1);
	}
	return tree;
	  
  }
  public void printTree() {
	
	 int height = super.tree.height(super.tree.root());
	 int arraylength = exponent(2,height+1);
	 String[][] arraytree = new String[height*2][arraylength];
	 System.out.println("Tree height is: "+ super.tree.height(super.tree.root()));
	 Position<Entry<K,V>> p= super.tree.root();
	 System.out.println("Root is: " + p.getElement());
	 String[][] tree = preorder(p, arraytree, arraylength/2,height, 0);
	 printarray(tree);
	 
	 
	  // Put your code to print AVL tree here
  }
  static void printarray(String[][] aray) {
	  for(int i = 0; i < aray.length; i++) {
		  printsinglearray(aray[i]);
		  System.out.println();
	  }
	  
  }
  static void printsinglearray(String[] aray) {
	  for(int i = 0; i < aray.length; i ++) {
		  if(aray[i] != null) {
			  System.out.print(aray[i]);
		  }
		  else {
			  System.out.print(" ");
		  }
		  
	  }
	  
  }
}
