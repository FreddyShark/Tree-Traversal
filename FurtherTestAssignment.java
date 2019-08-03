import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import textbook.LinkedBinaryTree;

public class FurtherTestAssignment 
{
	// Set up JUnit to be able to check for expected exceptions (taken from provided test cases)
		@Rule
		public ExpectedException thrown = ExpectedException.none();
		
		@Test (timeout = 100)
		public void testIsArithmeticExpression()
		{
			LinkedBinaryTree<String> tree = new LinkedBinaryTree<String>();
			
			// small examples
			// single variable example
			tree.addRoot("x");
			assertTrue(Assignment.isArithmeticExpression(tree));
			// single integer
			tree.set(tree.root(), "7");
			assertTrue(Assignment.isArithmeticExpression(tree));
			// single negative integer
			tree.set(tree.root(), "-7");
			assertTrue(Assignment.isArithmeticExpression(tree));
			// single operator
			tree.set(tree.root(), "*");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			// integer * variable
			tree.addLeft(tree.root(),"3");
			tree.addRight(tree.root(), "x");
			assertTrue(Assignment.isArithmeticExpression(tree));
			// variable * integer
			tree.set(tree.left(tree.root()), "x");
			tree.set(tree.right(tree.root()), "3");
			assertTrue(Assignment.isArithmeticExpression(tree));
			// other operators
			tree.set(tree.root(), "-");
			assertTrue(Assignment.isArithmeticExpression(tree));
			tree.set(tree.root(), "+");
			assertTrue(Assignment.isArithmeticExpression(tree));
			// operator in leaf position
			tree.set(tree.left(tree.root()), "*");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			// position with only 1 child
			tree.remove(tree.left(tree.root()));
			assertTrue(!Assignment.isArithmeticExpression(tree));
			tree.addLeft(tree.root(), "x");  //add position again with valid value
			// internal position not operator
			tree.set(tree.root(), "0");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			tree.set(tree.root(), "*");
			tree.set(tree.left(tree.root()), "*");
			
			// larger examples
			// unbalanced tree  (y*7)*x)+3)
			tree.addLeft(tree.left(tree.root()), "*");
			tree.addRight(tree.left(tree.root()), "x");
			tree.addLeft(tree.left(tree.left(tree.root())), "y");
			tree.addRight(tree.left(tree.left(tree.root())), "7");
			assertTrue(Assignment.isArithmeticExpression(tree));
			// root's right child leaf as operator  (y*7)*x)+-)
			tree.set(tree.right(tree.root()), "-");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			tree.set(tree.right(tree.root()), "3"); // reset to integer
			// maximum depth leaf as operator  (+*7)*x)+3
			tree.set(tree.left(tree.left(tree.left(tree.root()))), "+");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			tree.set(tree.left(tree.left(tree.left(tree.root()))), "y"); // reset to variable
			// variable in internal position (y*7)zx)+3)
			tree.set(tree.left(tree.root()), "z");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			// integer in internal position (y*7)5x)+3)
			tree.set(tree.left(tree.root()), "5");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			tree.set(tree.left(tree.root()), "*");  // reset to operator
			
			// balance tree
			tree.set(tree.right(tree.root()), "+");
			tree.addLeft(tree.right(tree.root()), "-1");	// add negative value
			tree.addRight(tree.right(tree.root()), "k");
			// ((y*7)*x + (-1+k))
			assertTrue(Assignment.isArithmeticExpression(tree));
			// leaf as operator ((y*7)*x + (-1+*))
			tree.set(tree.right(tree.right(tree.root())), "*");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			tree.set(tree.right(tree.right(tree.root())), "k");  // reset to variable
			// internal position as variable ((y*7)*x + (-1gk))
			tree.set(tree.right(tree.root()), "g");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			// internal position as integer ((y*7)*x + (-1 0 k))
			tree.set(tree.right(tree.root()), "0");
			assertTrue(!Assignment.isArithmeticExpression(tree));
			tree.set(tree.right(tree.root()), "+");  // reset to operator
			// position with only 1 child ((y*7)*x + (-1+)
			tree.remove(tree.right(tree.right(tree.root())));
			assertTrue(!Assignment.isArithmeticExpression(tree));		
		}		
	@Test(timeout = 100)
	public void testTree2prefix()
	{
		String expression;
		String result;
		LinkedBinaryTree<String> tree;
		
		// single position trees
		// single integer
		tree = Assignment.prefix2tree("6");
		expression = "6";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// single variable
		tree = Assignment.prefix2tree("a");
		expression = "a";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		
		// general test
		tree = Assignment.prefix2tree("- - z z * z 0");
		expression = "- - z z * z 0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// tree with one sibling as leaf and the other internal
		tree = Assignment.prefix2tree("+ * b y z");
		expression = "+ * b y z";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// large complete and balanced tree
		tree = Assignment.prefix2tree("- + * * 22 7 + x x * + a 1 - 9 1 + - * 5 0 * z x * + 9 4 * 3 x");
		expression = "- + * * 22 7 + x x * + a 1 - 9 1 + - * 5 0 * z x * + 9 4 * 3 x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// unbalanced tree
		tree = Assignment.prefix2tree("- * + * 1 y z 3 y");
		expression = "- * + * 1 y z 3 y";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
	
	}
	
	@Test (timeout = 100)
	public void testTree2infix()
	{
		/* use same significant tests used for tree2Prefix */
		
		String expression;
		String result;
		LinkedBinaryTree<String> tree;
		
		// single integer
		tree = Assignment.prefix2tree("6");
		expression = "6";
		result = Assignment.tree2infix(tree);
		assertEquals(expression, result);
		// single variable
		tree = Assignment.prefix2tree("a");
		expression = "a";
		result = Assignment.tree2infix(tree);
		assertEquals(expression, result);
		//extra test for single operator
		tree = Assignment.prefix2tree("+ x x");
		expression = "(x+x)";
		result = Assignment.tree2infix(tree);
		assertEquals(expression, result);
		
		// general test
		tree = Assignment.prefix2tree("- - z z * z 0");
		expression = "((z-z)-(z*0))";
		result = Assignment.tree2infix(tree);
		assertEquals(expression, result);
		// tree with one sibling as leaf and the other internal
		tree = Assignment.prefix2tree("+ * b y z");
		expression = "((b*y)+z)";
		result = Assignment.tree2infix(tree);
		assertEquals(expression, result);
		// large complete tree
		tree = Assignment.prefix2tree("- + * * 22 7 + x x * + a 1 - 9 1 + - * 5 0 * z x * + 9 4 * 3 x");
		expression = "((((22*7)*(x+x))+((a+1)*(9-1)))-(((5*0)-(z*x))+((9+4)*(3*x))))";
		result = Assignment.tree2infix(tree);
		assertEquals(expression, result);
		// unbalanced tree
		tree = Assignment.prefix2tree("- * + * 1 y z 3 y");
		expression = "((((1*y)+z)*3)-y)";
		result = Assignment.tree2infix(tree);
		assertEquals(expression, result);
	}
	
	@Test (timeout = 100)
	public void testSimplify()
	{
		String expression;
		String result;
		LinkedBinaryTree<String> tree;
		
		//small examples
		tree = Assignment.prefix2tree("0");
		tree = Assignment.simplify(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		tree = Assignment.prefix2tree("+ 1 1");
		tree = Assignment.simplify(tree);
		expression = "2";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		tree = Assignment.prefix2tree("* 2 -2");
		tree = Assignment.simplify(tree);
		expression = "-4";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		tree = Assignment.prefix2tree("- 3 4");
		tree = Assignment.simplify(tree);
		expression = "-1";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
	
		// general test 
		tree = Assignment.prefix2tree("+ * - 12 2 z + 1 9");
		tree = Assignment.simplify(tree);
		expression = "+ * 10 z 10";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// all integers
		tree = Assignment.prefix2tree("+ * - 12 2 5 + 1 9");
		tree = Assignment.simplify(tree);
		expression = "60";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// negative result
		tree = Assignment.prefix2tree("+ - 2 12 + 0 1");
		tree = Assignment.simplify(tree);
		expression = "-9";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// zero result
		tree = Assignment.prefix2tree("* - 12 2 * 1 0");
		tree = Assignment.simplify(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// large complete tree
		tree = Assignment.prefix2tree("- + * * 2 7 + x x * + a 1 - 9 1 + - * 5 0 * z x * + 9 4 * 3 x");
		tree = Assignment.simplify(tree);
		expression = "- + * 14 + x x * + a 1 8 + - 0 * z x * 13 * 3 x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// unbalanced tree (not simplifiable)
		tree = Assignment.prefix2tree("+ - + * y 3 3 3 3");
		tree = Assignment.simplify(tree);
		expression = "+ - + * y 3 3 3 3";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// unbalanced tree (simplifiable)"
		tree = Assignment.prefix2tree("+ - + * 3 3 3 3 y");
		tree = Assignment.simplify(tree);
		expression = "+ 9 y";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
	}
	
	@Test (timeout = 100)
	public void testSimplifyFancy()
	{
		String expression;
		String result;
		LinkedBinaryTree<String> tree;
		
		/* Apply tests from testSimplify() first to simplifyFancy() */
		
		//small examples
		tree = Assignment.prefix2tree("0");
		tree = Assignment.simplify(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		tree = Assignment.prefix2tree("+ 1 1");
		tree = Assignment.simplify(tree);
		expression = "2";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		tree = Assignment.prefix2tree("* 2 -2");
		tree = Assignment.simplify(tree);
		expression = "-4";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		tree = Assignment.prefix2tree("- 3 4");
		tree = Assignment.simplify(tree);
		expression = "-1";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
	
		// general test 
		tree = Assignment.prefix2tree("+ * - 12 2 z + 1 9");
		tree = Assignment.simplify(tree);
		expression = "+ * 10 z 10";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// all integers
		tree = Assignment.prefix2tree("+ * - 12 2 5 + 1 9");
		tree = Assignment.simplify(tree);
		expression = "60";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// negative result
		tree = Assignment.prefix2tree("+ - 2 12 + 0 1");
		tree = Assignment.simplify(tree);
		expression = "-9";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// zero result
		tree = Assignment.prefix2tree("* - 12 2 * 1 0");
		tree = Assignment.simplify(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// large complete tree
		tree = Assignment.prefix2tree("- + * * 2 7 + x x * + a 1 - 9 1 + - * 5 0 * z x * + 9 4 * 3 x");
		tree = Assignment.simplify(tree);
		expression = "- + * 14 + x x * + a 1 8 + - 0 * z x * 13 * 3 x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// unbalanced tree (not simplifiable)
		tree = Assignment.prefix2tree("+ - + * y 3 3 3 3");
		tree = Assignment.simplify(tree);
		expression = "+ - + * y 3 3 3 3";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// unbalanced tree (simplifiable)"
		tree = Assignment.prefix2tree("+ - + * 3 3 3 3 y");
		tree = Assignment.simplify(tree);
		expression = "+ 9 y";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		
		/* test small crucial cases */
		// single value 0
		tree = Assignment.prefix2tree("0");
		tree = Assignment.simplifyFancy(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// removal of 2 opposite sign variable under same parent
		tree = Assignment.prefix2tree("- x x");
		tree = Assignment.simplifyFancy(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// simplify - 0
		tree = Assignment.prefix2tree("- x 0");
		tree = Assignment.simplifyFancy(tree);
		expression = "x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// simplify + 0 (0 in right child)
		tree = Assignment.prefix2tree("+ x 0");
		tree = Assignment.simplifyFancy(tree);
		expression = "x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// simplify + 0 (0 in left child)
		tree = Assignment.prefix2tree("+ 0 x");
		tree = Assignment.simplifyFancy(tree);
		expression = "x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// simplify * 0 (0 in right child)
		tree = Assignment.prefix2tree("* x 0");
		tree = Assignment.simplifyFancy(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// simplify * 0 (0 in left child)
		tree = Assignment.prefix2tree("* 0 x");
		tree = Assignment.simplifyFancy(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// simplify  * 1 (1 in left child)
		tree = Assignment.prefix2tree("* 1 x");
		tree = Assignment.simplifyFancy(tree);
		expression = "x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// simplify *1 (1 in right child)
		tree = Assignment.prefix2tree("* x 1");
		tree = Assignment.simplifyFancy(tree);
		expression = "x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// multiple special cases in one expression (x - x) and (x * 0)
		tree = Assignment.prefix2tree("- - z z * z 0");
		tree =  Assignment.simplifyFancy(tree);
		LinkedBinaryTree<String> expected = Assignment.prefix2tree("0");
		assertTrue(Assignment.equals(tree, expected));
		
		// unbalanced tree multiplication by 0
		tree = Assignment.prefix2tree("* 0 * + x 1 + z z");
		tree = Assignment.simplifyFancy(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// subtraction of 2 identical subtrees
		tree = Assignment.prefix2tree("- - x z - x z");
		tree = Assignment.simplifyFancy(tree);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// subtraction of 2 identical subtrees with remaining elements
		tree = Assignment.prefix2tree("+ + 4 y - - x z - x z"); 
		tree = Assignment.simplifyFancy(tree);
		expression = "+ 4 y";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// subtraction of 2 identical subtrees with remaining elements in unbalanced tree
		tree = Assignment.prefix2tree("+ k - - x z - x z");	
		tree = Assignment.simplifyFancy(tree);
		expression = "k";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// bigger example thats unbalanced tree (4+4)+(((7+x)-(4+9)*x)-((7+x)-(4+9)*x))
		tree = Assignment.prefix2tree("+ + 4 4 - - + x 7 * + 4 9 x - + x 7 * + 4 9 x");
		tree = Assignment.simplifyFancy(tree);
		expression = "8";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// bigger example of (x * 0)  unbalanced tree
		tree = Assignment.prefix2tree("+ a * 0 - - + x 6 * + 4 9 x - + x 7 * + 4 9 x");
		tree = Assignment.simplifyFancy(tree);
		expression = "a";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// bigger example of (x * 1) unbalanced tree
		tree = Assignment.prefix2tree("+ a * 1 - - + x 6 * * 4 9 x - + x 7 * + 4 9 x");
		tree = Assignment.simplifyFancy(tree);
		expression = "+ a - - + x 6 * 36 x - + x 7 * 13 x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
	}
	
	@Test (timeout = 100)
	public void testSubstitute()
	{
		String expression;
		String result;
		LinkedBinaryTree<String> tree;
		
		// test Bad variable names
		// null variable
		tree = Assignment.prefix2tree("x");
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.substitute(tree, null, 0);
		// an integer name
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.substitute(tree, "42", 3);
		// operator names
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.substitute(tree, "*", 3);
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.substitute(tree, "+", 3);
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.substitute(tree, "-", 3);
		
		// test variable not in tree (should remain unchanged)
		tree = Assignment.substitute(tree, "z", 0);
		expression = "x";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// test tiny example
		tree = Assignment.substitute(tree, "x", 0);
		expression = "0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// test small example
		tree = Assignment.prefix2tree("+ x y");
		tree = Assignment.substitute(tree, "x", 0);
		expression = "+ 0 y";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// test significant small example
		tree = Assignment.prefix2tree("+  y");
		tree = Assignment.substitute(tree, "", 0);
		expression = "+ 0 y";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// larger tree example
		tree = Assignment.prefix2tree("+ a - - + x 6 * 36 y - + z 7 * 13 k");
		tree = Assignment.substitute(tree, "x", 2);
		expression = "+ a - - + 2 6 * 36 y - + z 7 * 13 k";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// multiple substitutions
		tree = Assignment.prefix2tree("+ x - - + x 6 * 36 x - + x 7 * 13 x");
		tree = Assignment.substitute(tree, "x", 0);
		expression = "+ 0 - - + 0 6 * 36 0 - + 0 7 * 13 0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// bigger example thats unbalanced tree (4+4)+(((7+x)-(4+9)*x)-((7+x)-(4+9)*x))
		tree = Assignment.prefix2tree("+ + 4 4 - - + x 7 * + 4 9 x - + x 7 * + 4 9 x");
		tree = Assignment.substitute(tree, "x", 3);
		expression = "+ + 4 4 - - + 3 7 * + 4 9 3 - + 3 7 * + 4 9 3";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// example with all operators
		tree = Assignment.prefix2tree("* + a a - a a");
		tree = Assignment.substitute(tree, "a", 0);
		expression = "* + 0 0 - 0 0";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// large integer value
		tree = Assignment.prefix2tree("* + a a - a a");
		tree = Assignment.substitute(tree, "a", 99999);
		expression = "* + 99999 99999 - 99999 99999";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// negative integer value
		tree = Assignment.prefix2tree("* + a a - a a");
		tree = Assignment.substitute(tree, "a", -3);
		expression = "* + -3 -3 - -3 -3";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
	}
	
	@Test (timeout = 100)
	public void testSubstituteMap()
	{
		HashMap<String, Integer> values = new HashMap<String, Integer>();
		values.put("x", 0);
		values.put("y", 1);
		values.put("z", -1);
		values.put("@", 99);
		LinkedBinaryTree<String> tree= new LinkedBinaryTree<String>();
		String expression;
		String result;
		
		// basic example
		tree = Assignment.prefix2tree("* + x y - z @");
		tree = Assignment.substitute(tree, values);
		expression = "* + 0 1 - -1 99";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// bad variable names
		// operator names
		values.put("*", 5);
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.substitute(tree, values);
		values.remove("*");
		values.put("+", 5);
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.substitute(tree, values);
		values.remove("+");
		values.put("-", 5);
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.substitute(tree, values);
		values.remove("-");
		// integer name
		values.put("5", 5);
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.substitute(tree, values);
		values.remove("5");
		
		// test null value
		thrown.expect(IllegalArgumentException.class);
		values.put("r", null);
		values.remove("r");
		
		// multiple substitutions per variable
		tree = Assignment.prefix2tree("- + @ y * + x y * - z @ * x z");
		tree = Assignment.substitute(tree, values);
		expression = "- + 99 1 * + 0 1 * - -1 99 * 0 -1";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// some variables not in tree
		tree = Assignment.prefix2tree("* - + z x z * x z");
		tree = Assignment.substitute(tree, values);
		expression = "* - + -1 0 -1 * 0 -1";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// all variables not in tree and integer value present
		tree = Assignment.prefix2tree("+ * a b 5");
		tree = Assignment.substitute(tree, values);
		expression = "+ * a b 5";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// unbalanced tree
		tree = Assignment.prefix2tree("* * x y * - * x z y @");
		tree = Assignment.substitute(tree, values);
		expression = "* * 0 1 * - * 0 -1 1 99";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		// bigger example thats unbalanced tree (4+4)+(((7+@)-(4+9)*y)-((7+x)-(4+9)*z))
		tree = Assignment.prefix2tree("+ + 4 4 - - + @ 7 * + 4 9 y - + x 7 * + 4 9 z");
		tree = Assignment.substitute(tree, values);
		expression = "+ + 4 4 - - + 99 7 * + 4 9 1 - + 0 7 * + 4 9 -1";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
		
		values.put("a", 5);
		values.put("b",  -5);
		values.put("c", 10);
		values.put("d", -10);
		values.put("e", 99999);
		values.put("f", -99999);
		// larger input example
		tree = Assignment.prefix2tree("* * + x y - z @ * + a b - c d * e f");
		tree = Assignment.substitute(tree, values);
		expression = "* * + 0 1 - -1 99 * + 5 -5 - 10 -10 * 99999 -99999";
		result = Assignment.tree2prefix(tree);
		assertEquals(expression, result);
	}

	//Testing helper method to print tree elements and double check errors
	public void printTree(LinkedBinaryTree<String> tree)
	{
		System.out.println(Assignment.tree2prefix(tree));
		System.out.println(Assignment.tree2infix(tree));
	}
}
