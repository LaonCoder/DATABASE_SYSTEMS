import java.io.*;

/**
 * 사용자로부터 명령어를 입력받아 데이터 베이스를 조작하는 클래스
 */
public class DBProgram {

	/**
	 * 사용자로부터 명령어를 입력받아 데이터 베이스를 조작하는 main 함수
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
    	
    	String command = args[0];
    	
    	switch(command) {
    		// 1. Data File Creation
	    	case "-c":
	    	{
	            try {
	                FileWriter fw = new FileWriter(args[1]);
	                fw.write(args[2]);
	                fw.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            
	            break;
	    	}
	    	// 2. Insertion
	    	case "-i":
	    	{
	            BPlusTree bpTree = readBPTree(args[1]);
	            
	            try {
	                BufferedReader buffer = new BufferedReader(new FileReader(args[2]));
	                String line;

	                while ((line = buffer.readLine()) != null) {
	                    String[] keyValueData = line.split(",");
	                    bpTree.insert(Integer.parseInt(keyValueData[0]), Integer.parseInt(keyValueData[1]));
	                }
	                
	                saveBPTree(args[1], bpTree);
	                buffer.close();

	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            
	            break;
	    	}
	    	// 3. Deletion
	    	case "-d":
	    	{
	            BPlusTree bpTree = readBPTree(args[1]);
	            
	            try {
	                BufferedReader buffer = new BufferedReader(new FileReader(args[2]));
	                String line;

	                while ((line = buffer.readLine()) != null) {
	                    bpTree.delete(Integer.parseInt(line));
	                }
	                saveBPTree(args[1], bpTree);
	                buffer.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            
	            break;
	    	}
	    	// 4. Single Key Search
	    	case "-s":
	    	{
	        	BPlusTree bpTree = readBPTree(args[1]);
				bpTree.singleSearch(Integer.parseInt(args[2]));
				
				break;
	    	}
	    	// 5. Ranged Search
	    	case "-r":
	    	{
                BPlusTree bpTree = readBPTree(args[1]);
                bpTree.rangeSearch(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                
                break;
	    	}
	    	default:
	    	{
	    		System.out.printf("Unknown Command '%s'", command);
	    	}
    	}
    }
    

	/**
	 * indexFile에 저장된 (key, value)쌍의 데이터를 모두 읽어들여 B+트리로 재구성한다.
	 * 
	 * @param indexFile
	 * 		  데이터를 읽어들일 파일
	 * @return 삽입 이후 새로 구성된 B+트리의 루트 노드
	 */
    public static BPlusTree readBPTree(String indexFile) {
        BPlusTree bpTree = null;
        int degree;
        String line;
        
        try {
			BufferedReader buffer = new BufferedReader(new FileReader(indexFile));
			
			degree = Integer.parseInt(buffer.readLine()); 
			
			bpTree = new BPlusTree(degree);
			
			// indexFile에 저장된 모든 (key, value) 데이터를 B+트리에 노드로 삽입
            while ((line = buffer.readLine()) != null) {
                String[] keyValueData = line.split(",");
                bpTree.insert(Integer.parseInt(keyValueData[0]), Integer.parseInt(keyValueData[1]));
            }
            
            buffer.close();
            return bpTree;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    
	/**
	 * B+트리에 저장된 모든 데이터를 indexFile에 저장한다.
	 * 
	 * @param indexFile
	 * 		  데이터를 저장할 파일
	 * @param bpTree
	 * 		  파일에 저장할 데이터가 저장되어 있는 B+트리
	 */
    public static void saveBPTree(String indexFile, BPlusTree bpTree) {
    	int degree = bpTree.getMaxNumOfChild();
    	
    	Node currentNode = bpTree.getLeftmostNode();
        
        try {
			BufferedWriter buffer = new BufferedWriter(new FileWriter(indexFile));
			
			if (bpTree.isEmpty()) {
				buffer.write(degree+"\n");
			}
			else {
				buffer.write(degree+"\n");
				
				for (int i = 0; i < currentNode.getCurrentNumOfKey(); i++) {
					buffer.write(currentNode.getKeyPairArray()[i].getKey() + "," + currentNode.getKeyPairArray()[i].getValue() + "\n");  // (key, value) 쌍 저장
				}
				
				// 현재 노드의 rightSibling 노드가 없을 때까지 이동하면서 (Key, Value)쌍 작성
				while (currentNode.getRightSibling() != null) {
					currentNode = currentNode.getRightSibling();
					
					for (int i = 0; i < currentNode.getCurrentNumOfKey(); i++) {
						buffer.write(currentNode.getKeyPairArray()[i].getKey() + "," + currentNode.getKeyPairArray()[i].getValue() + "\n");
					}
				}
			}
			buffer.flush();
			buffer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
    }
}