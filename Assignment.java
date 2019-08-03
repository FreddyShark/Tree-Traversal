import java.util.HashMap;

import textbook.LinkedBinaryTree;
import textbook.LinkedQueue;
import textbook.Position;

public class Assignment 
{
	/**
	 * Convert an arithmetic expression (in prefix notation), to a binary tree
	 * 
	 * Binary operators are +, -, * (i.e. addition, subtraction, multiplication)
	 * Anything else is assumed to be a variable or numeric value
	 * 
	 * Example: "+ 2 15" will be a tree with root "+", left child "2" and right
	 * child "15" i.e. + 2 15
	 * 
	 * Example: "+ 2 - 4 5" will be a tree with root "+", left child "2", right
	 * child a subtree representing "- 4 5" i.e. + 2 - 4 5
	 * 
	 * This method runs in O(n) time
	 * 
	 * @param expression
	 *            - an arithmetic expression in prefix notation
	 * @return BinaryTree representing an expression expressed in prefix
	 *         notation
	 * @throws IllegalArgumentException
	 *             if expression was not a valid expression
	 */
	public static LinkedBinaryTree<String> prefix2tree(String expression) throws IllegalArgumentException 
	{
		if (expression == null) 
		{
			throw new IllegalArgumentException("Expression string was null");
		}
		// break up the expression string using spaces, into a queue
		LinkedQueue<String> tokens = new LinkedQueue<String>();
		for (String token : expression.split(" ")) {
			tokens.enqueue(token);
		}
		// recursively build the tree
		return prefix2tree(tokens);
	}
	
	/**
	 * Recursive helper method to build an tree representing an arithmetic
	 * expression in prefix notation, where the expression has already been
	 * broken up into a queue of tokens
	 * 
	 * @param tokens
	 * @return
	 * @throws IllegalArgumentException
	 *             if expression was not a valid expression
	 */
	private static LinkedBinaryTree<String> prefix2tree(LinkedQueue<String> tokens) throws IllegalArgumentException 
	{
		LinkedBinaryTree<String> tree = new LinkedBinaryTree<String>();

		// use the next element of the queue to build the root
		if (tokens.isEmpty()) 
		{
			throw new IllegalArgumentException("String was not a valid arithmetic expression in prefix notation");
		}
		String element = tokens.dequeue();
		tree.addRoot(element);

		// if the element is a binary operation, we need to build the left and
		// right subtrees
		if (element.equals("+") || element.equals("-") || element.equals("*")) 
		{
			LinkedBinaryTree<String> left = prefix2tree(tokens);
			LinkedBinaryTree<String> right = prefix2tree(tokens);
			tree.attach(tree.root(), left, right);
		}
		// otherwise, assume it's a variable or a value, so it's a leaf (i.e.
		// nothing more to do)

		return tree;
	}
	
	/**
	 * Test to see if two trees are identical (every position in the tree stores the same value)
	 * 
	 * e.g. two trees representing "+ 1 2" are identical to each other, but not to a tree representing "+ 2 1"
	 * @param a
	 * @param b
	 * @return true if the trees have the same structure and values, false otherwise
	 */
	public static boolean equals(LinkedBinaryTree<String> a, LinkedBinaryTree<String> b) 
	{
		return equals(a, b, a.root(), b.root());
	}

	/**
	 * Recursive helper method to compare two trees
	 * @param aTree one of the trees to compare
	 * @param bTree the other tree to compare
	 * @param aRoot a position in the first tree
	 * @param bRoot a position in the second tree (corresponding to a position in the first)
	 * @return true if the subtrees rooted at the given positions are identical
	 */
	private static boolean equals(LinkedBinaryTree<String> aTree, LinkedBinaryTree<String> bTree, 
			Position<String> aRoot, Position<String> bRoot) 
	{
		//if either of the positions is null, then they are the same only if they are both null
		if(aRoot == null || bRoot == null) {
			return (aRoot == null) && (bRoot == null);
		}
		//first check that the elements stored in the current positions are the same
		String a = aRoot.getElement();
		String b = bRoot.getElement();
		if((a==null && b==null) || a.equals(b)) {
			//then recursively check if the left subtrees are the same...
			boolean left = equals(aTree, bTree, aTree.left(aRoot), bTree.left(bRoot));
			//...and if the right subtrees are the same
			boolean right = equals(aTree, bTree, aTree.right(aRoot), bTree.right(bRoot));
			//return true if they both matched
			return left && right;
		}
		else {
			return false;
		}
	}

	
	/**
	 * Given a tree, this method should output a string for the corresponding
	 * arithmetic expression in prefix notation, without (parenthesis) (also
	 * known as Polish notation)
	 * 
	 * Example: A tree with root "+", left child "2" and right child "15" would
	 * be "+ 2 15" Example: A tree with root "-", left child a subtree
	 * representing "(2+15)" and right child "4" would be "- + 2 15 4"
	 * 
	 * Ideally, this method should run in O(n) time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return prefix notation expression of the tree
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static String tree2prefix(LinkedBinaryTree<String> tree) throws IllegalArgumentException 
	{
		if (!isArithmeticExpression(tree))
			throw new IllegalArgumentException("Tree does not represent arithmetic expression");
		
		StringBuilder expression = new StringBuilder();
		
		return tree2prefix(tree, tree.root(), expression).toString().substring(1);		//.substring(1) to remove first space
	}
	
	/**
	 * Helper method used by: 
	 * 		- tree2prefix(LinkedBinaryTree<String> tree) 
	 * 
	 * Performs preorder traversal on tree to create desired expression.
	 * 
	 * @param tree - a tree representing an arithmetic expression
	 * @param treeRoot - the root position of the tree
	 * @param expression - a StringBuilder object to hold the String 
	 *  
	 * @return the expression
	 */
	private static StringBuilder tree2prefix(LinkedBinaryTree<String> tree, Position<String> treeRoot, StringBuilder expression)
	{
		// pseudocode used to guide code from https://en.wikipedia.org/wiki/Binary_expression_tree#Prefix_traversal
		// while unvisited positions remain in the tree peform preorder traversal
		if (treeRoot != null)
		{
			expression.append(" " + treeRoot.getElement());
			tree2prefix(tree, tree.left(treeRoot), expression);
			tree2prefix(tree, tree.right(treeRoot), expression);
		}
		return expression;
	}

	/**
	 * Given a tree, this method should output a string for the corresponding
	 * arithmetic expression in infix notation with parenthesis (i.e. the most
	 * commonly used notation).
	 * 
	 * Example: A tree with root "+", left child "2" and right child "15" would
	 * be "(2+15)"
	 * 
	 * Example: A tree with root "-", left child a subtree representing "(2+15)"
	 * and right child "4" would be "((2+15)-4)"
	 * 
	 * Optionally, you may leave out the outermost parenthesis, but it's fine to
	 * leave them on. (i.e. "2+15" and "(2+15)-4" would also be acceptable
	 * output for the examples above)
	 * 
	 * Ideally, this method should run in O(n) time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return infix notation expression of the tree
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static String tree2infix(LinkedBinaryTree<String> tree) throws IllegalArgumentException 
	{
		if (!isArithmeticExpression(tree))
			throw new IllegalArgumentException("Tree does not represent arithmetic expression");
		
		StringBuilder expression = new StringBuilder();
		
		return tree2infix(tree, tree.root(), expression).toString();
	}
	
	/**
	 * Helper method used by:
	 * 		 - tree2Infix(LinkedBinaryTree<String> tree) 
	 * 
	 * Performs inorder traversal on tree to create desired expression.
	 * 
	 * @param tree - a tree representing an arithmetic expression
	 * @param subtreeRoot - a position that is the root of the current tree
	 * @param expression - the arithmetic expression being built
	 * 
	 * @return the String form of the arithmetic expression
	 */
	private static StringBuilder tree2infix(LinkedBinaryTree<String> tree, Position<String> subtreeRoot, StringBuilder expression)
	{
		/* pseodocode for inorder order traversal used as guide, from pg 36 of The University of Sydney INFO1105 (Goodrich, Tamassia, Goldwasser) lecture notes , week 4
		 https://elearning.sydney.edu.au/bbcswebdav/pid-4874052-dt-content-rid-20620515_1/courses/2017_S2C_INFO1105_ND/INFO1105_Lecture4_2017.pdf */
		// while unvisited positions remain in the tree, perform inorder traversal
		if (subtreeRoot != null)
		{
			if (tree.left(subtreeRoot) != null)
			{
				expression.append("(");
				tree2infix(tree, tree.left(subtreeRoot), expression);	
			}
			expression.append(subtreeRoot.getElement());
			
			if (tree.right(subtreeRoot) != null)
			{
				tree2infix(tree, tree.right(subtreeRoot), expression);
				expression.append(")" );
			}
		}
		return expression;
	}

	/**
	 * Given a tree, this method should simplify any subtrees which can be
	 * evaluated to a single integer value.
	 * 
	 * Ideally, this method should run in O(n) time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return resulting binary tree after evaluating as many of the subtrees as
	 *         possible
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static LinkedBinaryTree<String> simplify(LinkedBinaryTree<String> tree) throws IllegalArgumentException 
	{
		if (!isArithmeticExpression(tree))
			throw new IllegalArgumentException("Tree does not represent arithmetic expression");
				
		return simplify(tree, tree.root());
	}
	
	/**
	 * Helper method used by:
	 * 		 - simplify(LinkedBinaryTree<String> tree)
	 * 
	 * Performs inorder traversal to calculate Integer arithmetic expressions
	 * and simplify subtrees with result. 
	 * 
	 * uses helper methods:
	 * 		 - modifyPosition(LinkedBinaryTree<String> tree, Position<String> p, String element)
	 * 		 - IsInteger(Position<String> p)
	 * 
	 * @param tree - a tree representing an arithmetic expression that is being simplified
	 * @param treeRoot - a position that is the root of the current tree
	 * 
	 * @return a simplified tree
	 */
	private static LinkedBinaryTree<String> simplify(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	{
		/* pseodocode for postorder traversal taken from pg 23 of The University of Sydney INFO1105 (Goodrich, Tamassia, Goldwasser) lecture notes , week 4
		 https://elearning.sydney.edu.au/bbcswebdav/pid-4874052-dt-content-rid-20620515_1/courses/2017_S2C_INFO1105_ND/INFO1105_Lecture4_2017.pdf */
		//while unvisited positions with children remain in the tree, perform postorder traversal
		if (tree.left(treeRoot) != null && tree.right(treeRoot) != null)
		{
			simplify(tree, tree.left(treeRoot));
			simplify(tree, tree.right(treeRoot));	
		
			Position<String> leftChild = tree.left(treeRoot);	
			Position<String> rightChild = tree.right(treeRoot);		
			Integer leftValue = 0;					// initialize with random value 0
			Integer rightValue = 0;					// initialize with random value 0
			Integer calculatedValue;

			//check if left and right children hold Integer values
			if (isInteger(leftChild) && isInteger(rightChild))
			{
				leftValue = Integer.parseInt(leftChild.getElement());	
				rightValue = Integer.parseInt(rightChild.getElement());
		
				// for case when parent contains addition operator
				if (treeRoot.getElement().equals("+"))
				{	
					calculatedValue = leftValue + rightValue;
					modifyPosition(tree, treeRoot, calculatedValue.toString());
				}
				// for case when parent contains negative operator
				if (treeRoot.getElement().equals("-"))
				{
					calculatedValue = leftValue - rightValue;
					modifyPosition(tree, treeRoot, calculatedValue.toString());
				}
				//for case when parent contains multiplying operator
				if (treeRoot.getElement().equals("*"))
				{
					calculatedValue = leftValue * rightValue;
					modifyPosition(tree, treeRoot, calculatedValue.toString());
				}
			}
		}
		return tree;
	}

	/**
	 * This should do everything the simplify method does AND also apply the following rules:
	 *  * 1 x == x  i.e.  (1*x)==x
	 *  * x 1 == x        (x*1)==x
	 *  * 0 x == 0        (0*x)==0
	 *  * x 0 == 0        (x*0)==0
	 *  + 0 x == x        (0+x)==x
	 *  + x 0 == 0        (x+0)==x
	 *  - x 0 == x        (x-0)==x
	 *  - x x == 0        (x-x)==0
	 *  
	 *  Example: - * 1 x x == 0, in infix notation: ((1*x)-x) = (x-x) = 0
	 *  
	 * Ideally, this method should run in O(n) time (harder to achieve than for other methods!)
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return resulting binary tree after applying the simplifications
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static LinkedBinaryTree<String> simplifyFancy(LinkedBinaryTree<String> tree) throws IllegalArgumentException
	{
		if (!isArithmeticExpression(tree))
			throw new IllegalArgumentException("Tree does not represent arithmetic expression");
				
		return simplifyFancy(tree, tree.root());
	}
	
	/**
	 * Helper method used by:
	 * 		- simplifyFancy(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 
	 * Takes a binary tree representing an arithmetic expression and a position that is its root.
	 * Uses a postorder tree traversal to visit positions in the tree and perform all simplifications
	 * specified by simplifyFancy.
	 * 
	 * uses helper methods:
	 * 		- modifyPosition(LinkedBinaryTree<String> tree, Position<String> p, String element)
	 * 		- modifyPosition(LinkedBinaryTree<String> tree, Position<String> p, Position<String> q)
	 * 		- IsInteger(Position<String> p)
	 * 		- removeSubtree(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 
	 * @param tree - binary tree being simplified
	 * @param treeRoot - position being visited within tree (root of subtrees)
	 * 
	 * @return simplified tree
	 */
	private static LinkedBinaryTree<String> simplifyFancy(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	{
		/* pseodocode for postorder traversal taken from pg 23 of The University of Sydney INFO1105 (Goodrich, Tamassia, Goldwasser) lecture notes , week 4
		 https://elearning.sydney.edu.au/bbcswebdav/pid-4874052-dt-content-rid-20620515_1/courses/2017_S2C_INFO1105_ND/INFO1105_Lecture4_2017.pdf */
		// while unvisited positions with children remain in the tree, perform postorder traversal
		if (tree.left(treeRoot) != null && tree.right(treeRoot) != null)
		{
			simplifyFancy(tree, tree.left(treeRoot));
			simplifyFancy(tree, tree.right(treeRoot));
	
			// initialize with random values that are not 1 or 0 to not interfere with if statement comparison
			Integer leftValue = -1111;				
			Integer rightValue = -1111;
			Integer calculatedValue;
			Position<String> leftChild = tree.left(treeRoot);		
			Position<String> rightChild = tree.right(treeRoot);		

			// check if left child holds an Integer value and store it in leftValue if it does
			if (isInteger(leftChild))
				leftValue = Integer.parseInt(leftChild.getElement());	
			// check if right child holds an Integer value and store it in rightValue if it does
			if (isInteger(rightChild))
				rightValue = Integer.parseInt(rightChild.getElement());
		
			// for cases where position contains addition operator
			if (treeRoot.getElement().equals("+"))
			{	
				// for case where both left and right children are integer values
				if (isInteger(leftChild) && isInteger(rightChild))
				{
					calculatedValue = leftValue + rightValue;	
					modifyPosition(tree, treeRoot, calculatedValue.toString());
				}
				// for (0 + x) case
				else if (leftValue.equals(0))
				{
					modifyPosition(tree, treeRoot, leftChild);
				}
				// for (x + 0) case
				else if (rightValue.equals(0))
				{
					modifyPosition(tree, treeRoot, rightChild);
				}			
			}
			// for cases where position contains multiplication operator
			else if (treeRoot.getElement().equals("*"))
			{
				// for case where both children are integer values
				if (isInteger(leftChild) && isInteger(rightChild))
				{
					calculatedValue = leftValue * rightValue;
					modifyPosition(tree, treeRoot, calculatedValue.toString());
				}			
				// for (x * 1) case
				else if (rightValue.equals(1))
				{
					modifyPosition(tree, treeRoot, rightChild);
				}	
				// for (1 * x) case
				else if (leftValue.equals(1))
				{
					modifyPosition(tree, treeRoot, leftChild);
				}	
				// for  (x * 0) case
				else if (rightValue.equals(0))
				{
					tree.set(treeRoot, "0");
					tree.remove(rightChild);
					removeSubtree(tree, leftChild);
				}	
				// for  (0 * x) case
				else if (leftValue.equals(0))
				{
					tree.set(treeRoot, "0");
					tree.remove(leftChild);
					removeSubtree(tree, rightChild);
				}	
			}
			// for cases where position contains negative operator
			else if (treeRoot.getElement().equals("-"))
			{
				// for case where both children are integer values
				if (isInteger(leftChild) && isInteger(rightChild))
				{
					calculatedValue = leftValue - rightValue;
					modifyPosition(tree, treeRoot, calculatedValue.toString());
				}
				// for (x - 0) case
				else if (rightValue.equals(0))
				{
					modifyPosition(tree, treeRoot, rightChild);
				}
				// for (x - x) case
				else if (!isInteger(rightChild) && !isInteger(leftChild))
				{
					StringBuilder treeElements1 = new StringBuilder();
					StringBuilder treeElements2 = new StringBuilder();
					String leftsubTree = getTreeElements(tree, leftChild, treeElements1).toString();
					String rightsubTree = getTreeElements(tree, rightChild, treeElements2).toString();

					if (leftsubTree.equals(rightsubTree))
					{
						//evaluate position to 0
						tree.set(treeRoot, "0");
						// remove left and right subtrees
						removeSubtree(tree, leftChild);
						removeSubtree(tree, rightChild);	
					}	
				}
			}
		}
		return tree;
	}
			
	/**
	 * Given a tree, a variable label and a value, this should replace all
	 * instances of that variable in the tree with the given value
	 * 
	 * Ideally, this method should run in O(n) time (quite hard! O(n^2) is easier.)
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @param variable
	 *            - a variable label that might exist in the tree
	 * @param value
	 *            - an integer value that the variable represents
	 * @return Tree after replacing all instances of the specified variable with
	 *         it's numeric value
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression, or either of the other
	 *             arguments are null
	 */
	public static LinkedBinaryTree<String> substitute(LinkedBinaryTree<String> tree, String variable, int value)
			throws IllegalArgumentException 
	{
		//check if tree is holding arithmetic expression
		if (!isArithmeticExpression(tree))
			throw new IllegalArgumentException("Tree does not represent arithmetic expression");
		
		//check that variable name given is valid
		if (variable == null || !(variable instanceof String) || variable.equals("+") 
				|| variable.equals("-") || variable.equals("*") || (variable.matches("[0-9]+")))	//variable.matches taken from https://stackoverflow.com/questions/10575624/java-string-see-if-a-string-contains-only-numbers-and-not-letters
			throw new IllegalArgumentException("invalid variable: must not be an operator, only numeric or null");
		
		return substitute(tree, tree.root(), variable, ((Integer) value).toString());
	}
	
	/**
	 * Helper method used by:
	 * 		- substitute(LinkedBinaryTree<String> tree, String variable, int value)
	 * 
	 * Takes a tree representing an arithmetic expression, a Position that is the root of a subtree,
	 * a String variable and an integer value to substitute into all instances of that variable
	 * in tree positions.
	 * 
	 * @param tree - a binary tree representing an arithmetic expression
	 * @param subtreeRoot - a Position which is the root of the current tree
	 * @param variable - a variable name
	 * @param value - an integer value in String format
	 * 
	 * @return a binary tree with values substituted into positions
	 * that previously carried the given variable
	 */
	private static LinkedBinaryTree<String> substitute(LinkedBinaryTree<String> tree, Position<String> treeRoot,
			String variable, String value) throws IllegalArgumentException
	{
		/* pseodocode for preorder traversal taken from pg 21 of The University of Sydney INFO1105 (Goodrich, Tamassia, Goldwasser) lecture notes , week 4
		 https://elearning.sydney.edu.au/bbcswebdav/pid-4874052-dt-content-rid-20620515_1/courses/2017_S2C_INFO1105_ND/INFO1105_Lecture4_2017.pdf */
		// while unvisited positions are in the tree, perform preorder traversal
		if (treeRoot != null)
		{
			// if element = given variable, replace with value
			if (treeRoot.getElement().equals(variable))
			{
				tree.set(treeRoot, value); 
			}
			substitute(tree, tree.left(treeRoot), variable, value);
			substitute(tree, tree.right(treeRoot), variable, value);
		}
		return tree;
	}

	/**
	 * Given a tree and a a map of variable labels to values, this should
	 * replace all instances of those variables in the tree with the
	 * corresponding given values
	 * 
	 * Ideally, this method should run in O(n) expected time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @param map
	 *            - a map of variable labels to integer values
	 * @return Tree after replacing all instances of variables which are keys in
	 *         the map, with their numeric values
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression, or map is null, or tries
	 *             to substitute a null into the tree
	 */
	public static LinkedBinaryTree<String> substitute(LinkedBinaryTree<String> tree, HashMap<String, Integer> map)
			throws IllegalArgumentException 
	{
		if (!isArithmeticExpression(tree))
			throw new IllegalArgumentException("Tree does not represent arithmetic expression");
		
		if (map == null)
			throw new IllegalArgumentException("invalid map: map cannot be null");
		
		//check exception for null variables in map
		for (String key: map.keySet())
		{

			if (key == null || !(key instanceof String) || key.equals("+") 
					|| key.equals("-") || key.equals("*") || key.matches("[0-9]+"))	//variable.matches taken from https://stackoverflow.com/questions/10575624/java-string-see-if-a-string-contains-only-numbers-and-not-letters
				throw new IllegalArgumentException("invalid variable in map: must not be an operator, only numeric or null");
		}
		return substitute(tree, tree.root(), map);
	}
	
	/**
	 * Helper method used by: 
	 * 		- substitute(LinkedBinaryTree<String> tree, HashMap<String, Integer> map)
	 * 
	 * Performs preorder traversal to replace a collection of variables
	 * found within a binary tree, with desired integer values.
	 * 
	 * @param tree - a binary tree representing an arithmetic expression
	 * @param subtreeRoot - a Position that is the root of the given tree
	 * @param mappedVariables - a map (key = String variable, value = integer value to replace variable in tree)
	 * 
	 * @return a tree with all variables in map, substituted with desired values.
	 */
	private static LinkedBinaryTree<String> substitute(LinkedBinaryTree<String> tree, Position<String> treeRoot,
			HashMap<String, Integer> mappedVariables) throws IllegalArgumentException
	{
		/* pseodocode for preorder traversal taken from pg 21 of The University of Sydney INFO1105 (Goodrich, Tamassia, Goldwasser) lecture notes , week 4
		 https://elearning.sydney.edu.au/bbcswebdav/pid-4874052-dt-content-rid-20620515_1/courses/2017_S2C_INFO1105_ND/INFO1105_Lecture4_2017.pdf */
		//while unvisited Positions are still in tree, perform preorder traversal
		if (treeRoot != null)
		{
			//ensure map contains the variable as a key because hashMap.get() also returns null if key does not exist in map
			if (mappedVariables.containsKey(treeRoot.getElement()))
			{
				Integer value = mappedVariables.get(treeRoot.getElement());
				
				// check exception for null values in map
				if (value == null)
					throw new IllegalArgumentException("invalid value in map: values cannot be null");
				// substitute the corresponding value into the position
				else
				{
					String valueAsString = ((Integer) value).toString();
					tree.set(treeRoot, valueAsString);
				}
			}
			substitute(tree, tree.left(treeRoot), mappedVariables);
			substitute(tree, tree.right(treeRoot), mappedVariables);
		}
		return tree;
	}

	/**
	 * Given a tree, identify if that tree represents a valid arithmetic
	 * expression (possibly with variables)
	 * 
	 * Ideally, this method should run in O(n) expected time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return true if the tree is not null and it obeys the structure of an
	 *              arithmetic expression. Otherwise, it returns false
	 */
	public static boolean isArithmeticExpression(LinkedBinaryTree<String> tree) 
	{
		if (tree == null || tree.root() == null)
			return false;
		
		return isArithmeticExpression(tree, tree.root());
	}
	
	/**
	 * Helper method used by:
	 * 		- isArithmeticExpression(LinkedBinaryTree<String> tree) 
	 * 
	 * Takes a binary tree and a Position that is a root of the given tree performs a 
	 * preorder traversal to check if the binary tree represents a valid arithmetic expression
	 * 
	 * @param tree - any binary tree
	 * @param subtreeRoot - the root position of the current subtree
	 * 
	 * @return true if tree represents valid arithmetic expression, false otherwise
	 */
	private static boolean isArithmeticExpression(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	{
		/* pseodocode for preorder traversal taken from pg 21 of The University of Sydney INFO1105 (Goodrich, Tamassia, Goldwasser) lecture notes , week 4
		 https://elearning.sydney.edu.au/bbcswebdav/pid-4874052-dt-content-rid-20620515_1/courses/2017_S2C_INFO1105_ND/INFO1105_Lecture4_2017.pdf */
		Boolean leftSubtreeFlag = null;
		Boolean rightSubtreeFlag = null;
		
		//return false if tree is single position with operator
		if ((tree.size() == 1) 
				&& (treeRoot.getElement().equals("+") || treeRoot.getElement().equals("-") || treeRoot.getElement().equals("*")))
			return false;
		
		// return false if position is leaf and contains operand
		if ((tree.left(treeRoot) == null && tree.right(treeRoot) == null)
				&& (treeRoot.getElement().equals("+") || treeRoot.getElement().equals("-") || treeRoot.getElement().equals("*")))
			return false;
		
		// return false if position only has 1 child (must be proper tree)
		if ((tree.left(treeRoot) == null && tree.right(treeRoot) != null)
			|| (tree.left(treeRoot) != null && tree.right(treeRoot) == null))
			return false;
		
		// return false if position is internal and contains operand
		//else return result of recursive method call on left and right subtrees for tree traversal
		if (tree.left(treeRoot) != null && tree.right(treeRoot) != null)
		{
			if (!treeRoot.getElement().equals("+") && !treeRoot.getElement().equals("-") && !treeRoot.getElement().equals("*"))
				return false;
	
			leftSubtreeFlag = isArithmeticExpression(tree, tree.left(treeRoot));
			rightSubtreeFlag = isArithmeticExpression(tree, tree.right(treeRoot));
			
			return leftSubtreeFlag & rightSubtreeFlag;
		}
		//return true if all tests pass
		return true;
	}	

 /* 
  ==============================================================================================================================================================
  =================================================  Extra helper methods added to this class ====================================================================
  ===============================================================================================================================================================
 */
	
	/**
	 * Helper method used by:
	 * 			- simplify(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 			- simplifyFancy(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 
	 * checks if a position carries an element which can be converted to an Integer type
	 * 
	 * @param p - a position in a tree
	 * 
	 * @return true if element can be converted to an Integer, false otherwise
	 */
	private static boolean isInteger(Position<String> p)
	{
		try
		{
			Integer.parseInt(p.getElement());
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	/**
	 * Overloaded Helper method used by: 
	 * 			- simplify(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 			- simplifyFancy(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 
	 * This version inserts a desired String value into a given Position and 
	 * removes the Position's children from the tree
	 * 
	 * @param tree - any tree of the LinkedBinaryTree class
	 * @param p	- any position within the tree
	 * @param element - a String to become the element held in the position
	 */
	private static void modifyPosition(LinkedBinaryTree<String> tree, Position<String> p, String element)
	{
		tree.set(p, element);
		tree.remove(tree.left(p));
		tree.remove(tree.right(p));
	}
	
	/**
	 * Overloaded Helper method used by: 
	 * 			- simplify(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 			- simplifyFancy(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 
	 * This version removes any 2 given positions from a tree
	 * 
	 * @param tree - any tree of the LinkedBinaryTree class
	 * @param p	- any position within the tree
	 */
	private static void modifyPosition(LinkedBinaryTree<String> tree, Position<String> p, Position<String> q)
	{
		tree.remove(q);
		tree.remove(p);
	}
	
	/**
	 * Helper method used by:
	 * 		- simplifyFancy(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 
	 * Takes a tree and removes all subtree positions from the given position
	 * 
	 * @param tree - any binary tree
	 * @param subtreeRoot - a Position in the tree
	 * 
	 * @return a modified tree with all positions underneath the given position, removed.
	 */
	private static LinkedBinaryTree<String> removeSubtree(LinkedBinaryTree<String> tree, Position<String> subtreeRoot)
	{
		/* pseodocode for post order traversal taken from pg 23 of The University of Sydney INFO1105 (Goodrich, Tamassia, Goldwasser) lecture notes , week 4
		 https://elearning.sydney.edu.au/bbcswebdav/pid-4874052-dt-content-rid-20620515_1/courses/2017_S2C_INFO1105_ND/INFO1105_Lecture4_2017.pdf */
		// while unvisited positions with children remain in the tree, perform postorder traversal
		if (subtreeRoot != null)
		{
			removeSubtree(tree, tree.left(subtreeRoot));
			removeSubtree(tree, tree.right(subtreeRoot));
			tree.remove(subtreeRoot);
		}
		return tree; 
	}
	
	/**
	 * Helper method used by:
	 * 		- simplifyFancy(LinkedBinaryTree<String> tree, Position<String> treeRoot)
	 * 
	 * Takes a tree rooted at treeRoot and returns 
	 * a concatenation of the elements in prefix order
	 * 
	 * @param tree - any binary tree
	 * @param treeRoot - a Position in the tree
	 * @param elements - A StringBuilder object to create the return String
	 * 
	 * @return a String of concatenated elements in prefix order
	 */
	private static StringBuilder getTreeElements(LinkedBinaryTree<String> tree, Position<String> treeRoot, StringBuilder elements)
	{
		// pseudocode used to guide code from https://en.wikipedia.org/wiki/Binary_expression_tree#Prefix_traversal
		// while unvisited positions remain in the tree peform preorder traversal
		if (treeRoot != null)
		{
			elements.append(treeRoot.getElement());
			getTreeElements(tree, tree.left(treeRoot), elements);
			getTreeElements(tree, tree.right(treeRoot), elements);
		}
		return elements;
	}
}