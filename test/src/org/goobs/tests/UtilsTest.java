package org.goobs.tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Random;

import org.goobs.util.Utils;
import org.junit.*;

import static org.goobs.util.Utils.*;

public class UtilsTest {
	
	private int[] randArray(int size){
		Random r = new Random();
		int[] rtn = new int[size];
		for (int i = 0; i < rtn.length; i++) {
			rtn[i] = r.nextInt(size);
		}
		return rtn;
	}
	
	private Integer[] toObj(int[] arr){
		Integer[] rtn = new Integer[arr.length];
		for(int i=0; i<arr.length; i++){
			rtn[i] = new Integer(arr[i]);
			assertTrue(rtn[i].intValue() == arr[i]);
		}
		return rtn;
	}
	
	private int[] toInt(Integer[] arr){
		int[] rtn = new int[arr.length];
		for(int i=0; i<arr.length; i++){
			rtn[i] = arr[i].intValue();
		}
		return rtn;
	}
	
	private HashMap<Integer,Integer> counts(int[] arr){
		HashMap<Integer,Integer> rtn = new HashMap<Integer,Integer>();
		for(int val : arr){
			if(!rtn.containsKey(val)){ rtn.put(val, 0); }
			rtn.put(val, rtn.get(val)+1);
		}
		return rtn;
	}
	
	private void assertSorted(int[] elems){
		int lastElem = Integer.MIN_VALUE;
		for(int e : elems){
			assertTrue(" last: " + lastElem + " this: " + e, lastElem <= e);
			lastElem = e;
		}
	}
	
	@Test
	public void testSortBasic(){
		//--Random
		int[] input = randArray(100);
		int len = input.length;
		sort(input);
		assertTrue(input.length == len);
		assertSorted(input);
		//--Contrived Cases
		input = new int[]{ 1, 2, 1, 1 };
		sort(input);
		assertSorted(input);
		input = new int[]{ 1, 2, 1, 1, 2, 2, 3, 4, 3, 1, 2, 1, 2, 2, 2, 3 };
		sort(input);
		assertSorted(input);
		input = new int[]{1,1,1,1,1};
		sort(input);
		assertSorted(input);
		input = new int[]{0, 2, 3, 4, 0, 3};
		sort(input);
		assertSorted(input);
		input = new int[]{0, 2, 3, 4, 0, 3};
		sort(input, toObj(input));
		assertSorted(input);
	}
	
	@Test
	public void testSortSpam(){
		for(int i=0; i<10; i++){
			int[] input = randArray(100000);
			int len = input.length;
			sort(input);
			assertTrue(input.length == len);
			assertSorted(input);
		}
	}
	
	@Test
	public void testSortObject(){
		//--Simple
		int[] input = randArray(100);
		Integer[] objA = toObj(input);
		sort(input, objA);
		assertSorted(toInt(toObj(input))); // check transformations
		assertSorted(input);
		assertSorted(toInt(objA));
		//--Spam
		for(int i=0; i<10; i++){
			input = randArray(100000);
			objA = toObj(input);
			Integer[] objB = toObj(input);
			int len = input.length;
			sort(input, objA, objB);
			assertTrue(input.length == len);
			assertSorted(input);
			assertSorted(toInt(objA));
			assertSorted(toInt(objB));
		}
	}
	
	@Test
	public void testSortIfDrops(){
		//--Simple
		int[] input = randArray(100);
		HashMap<Integer,Integer> preCounts = counts(input);
		sort(input);
		HashMap<Integer,Integer> postCounts = counts(input);
		for(Integer key : preCounts.keySet()){
			assertEquals(preCounts.get(key), postCounts.get(key));
		}
		//--Object
		input = randArray(100);
		Integer[] objA = toObj(input);
		preCounts = counts(toInt(objA));
		sort(input, objA);
		postCounts = counts(toInt(objA));
		for(Integer key : preCounts.keySet()){
			assertEquals("counts different for key " + key, preCounts.get(key), postCounts.get(key));
		}
		//--Spam
		for(int i=0; i<10; i++){
			input = randArray(100000);
			preCounts = counts(input);
			sort(input);
			postCounts = counts(input);
			for(Integer key : preCounts.keySet()){
				assertEquals(preCounts.get(key), postCounts.get(key));
			}
		}
		//--Contrived Cases
		//--Simple
		input = new int[]{0, 2, 3, 4, 0, 3};
		preCounts = counts(input);
		sort(input);
		postCounts = counts(input);
		for(Integer key : preCounts.keySet()){
			assertEquals(preCounts.get(key), postCounts.get(key));
		}
	}
	
	@Test
	public void testConcat(){
		String[] a = new String[]{ "a", "b", "c" };
		String[] b = new String[]{ "d", "e" };
		//(array concat)
		String[] rtn = Utils.concat(a, b);
		assertEquals(5, rtn.length);
		assertEquals("a", rtn[0]);
		assertEquals("b", rtn[1]);
		assertEquals("c", rtn[2]);
		assertEquals("d", rtn[3]);
		assertEquals("e", rtn[4]);
		//(array concat, type diff)
		Object[] c = new Integer[]{4,6};
		Object[] out = Utils.concat(a, c);
		assertEquals(5, out.length);
		assertEquals("a", out[0]);
		assertEquals("b", out[1]);
		assertEquals("c", out[2]);
		assertEquals(4, out[3]);
		assertEquals(6, out[4]);
		//(element concat)
		rtn = Utils.concat(a, "d");
		assertEquals(4, rtn.length);
		assertEquals("a", rtn[0]);
		assertEquals("b", rtn[1]);
		assertEquals("c", rtn[2]);
		assertEquals("d", rtn[3]);
		//(element concat, type diff)
		out = Utils.concat(a, 7);
		assertEquals(4, out.length);
		assertEquals("a", out[0]);
		assertEquals("b", out[1]);
		assertEquals("c", out[2]);
		assertEquals(7, out[3]);
	}
	

}
