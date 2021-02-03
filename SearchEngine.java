import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here
		ArrayList<String> myHyperlinks = null;
		ArrayList<String> myContent = null;
		String myParent = null;
		if (this.internet.getVisited(url)==false) {
			if (myParent==null) {
				this.internet.addVertex(url);
			}
			this.internet.setVisited(url,true);
			myHyperlinks = this.parser.getLinks(url);
			//TODO : myContent is all the words on a url. You haven't used it for something yet!
			myContent = this.parser.getContent(url);
			ArrayList<String> myValues =new ArrayList<>();
			myValues.add(url);
			for (int i =0;i<myContent.size();i++) {
				if (!(this.wordIndex.containsKey(myContent.get(i)))){
					this.wordIndex.put(myContent.get(i),myValues);
				}
				else {
				//	System.out.println(myValues+"old values");
				//	System.out.println(this.wordIndex+"old word index websites");
					myValues = this.wordIndex.get(myContent.get(i));
					myValues.add(url);
				//	System.out.println(myValues+"new values");
					this.wordIndex.replace(myContent.get(i),myValues);
				//	System.out.println(this.wordIndex+"new word index websites");
				}
			}

			for (int i =0; i<myHyperlinks.size();i++){
				this.internet.addVertex(myHyperlinks.get(i));
				this.internet.addEdge(url,myHyperlinks.get(i));
				myParent = url;
			}
		}
		if(myHyperlinks!=null) {
			for (int i = 0; i < myHyperlinks.size();i++) {
				crawlAndIndex(myHyperlinks.get(i));
			}
		}
	}

	
	
	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here
		ArrayList<Double> previousRanks = new ArrayList<Double>();
		ArrayList<Double> currentRanks = new ArrayList<Double>();
		for (int i = 0; i < this.internet.getVertices().size(); i++) {
			this.internet.setPageRank(this.internet.getVertices().get(i), 1);
			previousRanks.add(this.internet.getPageRank(this.internet.getVertices().get(i)));
			currentRanks.add(this.internet.getPageRank(this.internet.getVertices().get(i)));
		}
		for (int j = 0; j < this.internet.getVertices().size(); j++) {
			if (!((currentRanks.get(j) - previousRanks.get(j)) < epsilon)) {
				previousRanks = currentRanks;
				currentRanks = new ArrayList<Double>();
				currentRanks = computeRanks(this.internet.getVertices());
				for (int i = 0; i < this.internet.getVertices().size(); i++){
					this.internet.setPageRank(this.internet.getVertices().get(i),currentRanks.get(i));
				}
				j = 0;
			}
		}
	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here
		ArrayList<Double> ranks = new ArrayList<Double>();
		for (int i =0; i<vertices.size();i++) {
			ArrayList<String> myFollowers;
			myFollowers = this.internet.getEdgesInto(vertices.get(i));
			double d = 0.5;
			double hyperlinksRank = 0;
			for (int j = 0; j<myFollowers.size();j++){
				double myOutDegree = this.internet.getOutDegree(myFollowers.get(j));
				if (myOutDegree ==0){
					myOutDegree = 1;
				}
				hyperlinksRank = hyperlinksRank + (this.internet.getPageRank(myFollowers.get(j))/myOutDegree);
			}
			Double myRank = (1-d)+(d*(hyperlinksRank));
			ranks.add(myRank);
		}
		return ranks;
	}

	
	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 */
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		ArrayList<String> searchResults = new ArrayList<String>();
		searchResults = this.wordIndex.get(query);
		HashMap<String,Double> urlRanks = new HashMap<>();
		for (int i = 0; i<searchResults.size();i++){
		    urlRanks.put(searchResults.get(i),this.internet.getPageRank(searchResults.get(i)));
        }
		// TODO : implement fastSort and return orderedResults instead of searchResults
        ArrayList<String> orderedResults = Sorting.fastSort(urlRanks);
		return orderedResults;
	}
}
