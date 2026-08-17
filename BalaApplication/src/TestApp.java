import java.util.*;

public class TestApp {

    public static void main(String[] args) {
        TestApp app = new TestApp();
        app.solution("ababcbacadefegdehijhklij");
    }
	
	public List<Integer> solution(String s) {
        int n = s.length();
        HashMap<String, Integer> map = new HashMap<>();
        for (int i=0; i<n;i++) {
            String t = String.valueOf(s.charAt(i));
            if (t!= null) {
                map.put(t, i+1);
            }
        }
        // ababcbacadefegdehijhklij
        List<Integer> result = new ArrayList<>();
        int max = 0;
        String prev = null;
        String curr = null;
        int iCurr = 0; 
        int iPrev = 0;
        
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        
        while (iterator.hasNext()) {
        	Map.Entry<String, Integer> entry = iterator.next();
             iCurr = entry.getValue();
             if (iPrev == 0) {
            	 iPrev = iCurr;
            	 max = iCurr;
            	 continue;
             }
             if (iPrev < max && max < iCurr) {
            	 result.add(max);
             }
             if (!iterator.hasNext() ) {
            	result.add(Math.max(max, iCurr));
             }
             iPrev = iCurr;
             max = Math.max(max, iCurr);
           }
           
        System.out.println(map);
       printList(result);
        return result;
    }
	
	public void printList(List<Integer> result) {
		
		result.stream().forEach(item -> {
			System.out.println(item);
		});
	}

}