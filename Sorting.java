import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
    	// ADD YOUR CODE HERE
		ArrayList<K> sortedUrls = new ArrayList<K>();
		ArrayList<K> sortedList1;
		ArrayList<K> sortedList2;
		sortedUrls.addAll(results.keySet());
		if (sortedUrls.size()==1){
			return sortedUrls;
		}
		else {
			int mid =(results.size()-1)/2;
			HashMap<K,V> list1 = new HashMap<K,V>();
			HashMap<K,V> list2 = new HashMap<K,V>();
			for (int i =0; i<mid+1;i++){
				list1.put(sortedUrls.get(i),results.get(sortedUrls.get(i)));
			}
			for (int i = mid+1; i<results.size();i++){
				list2.put(sortedUrls.get(i),results.get(sortedUrls.get(i)));

			}
			sortedList1 = fastSort(list1);
			sortedList2 = fastSort(list2);
			return merge(sortedList1,sortedList2,results);
		}
    }

	public static <K,V extends Comparable> ArrayList<K> merge(ArrayList<K> list1, ArrayList<K> list2,HashMap<K,V> results) {
    	ArrayList<K> list = new ArrayList<K>();
    		while (!list1.isEmpty()&& !list2.isEmpty()) {
    			if (results.get(list1.get(0)).compareTo(results.get(list2.get(0)))<0){
    				list.add(list2.get(0));
    				list2.remove(0);
				}
    			else {
    				list.add(list1.get(0));
    				list1.remove(0);
				}

			}
    		while (!list1.isEmpty()){
    			list.add(list1.get(0));
    			list1.remove(0);
			}
		while (!list2.isEmpty()){
			list.add(list2.get(0));
			list2.remove(0);
		}
    	return list;
	}
    

}