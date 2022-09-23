import java.io.*;

/**
 * ����ڷκ��� ��ɾ �Է¹޾� ������ ���̽��� �����ϴ� Ŭ����
 */
public class DBProgram {

	/**
	 * ����ڷκ��� ��ɾ �Է¹޾� ������ ���̽��� �����ϴ� main �Լ�
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
	 * indexFile�� ����� (key, value)���� �����͸� ��� �о�鿩 B+Ʈ���� �籸���Ѵ�.
	 * 
	 * @param indexFile
	 * 		  �����͸� �о���� ����
	 * @return ���� ���� ���� ������ B+Ʈ���� ��Ʈ ���
	 */
    public static BPlusTree readBPTree(String indexFile) {
        BPlusTree bpTree = null;
        int degree;
        String line;
        
        try {
			BufferedReader buffer = new BufferedReader(new FileReader(indexFile));
			
			degree = Integer.parseInt(buffer.readLine()); 
			
			bpTree = new BPlusTree(degree);
			
			// indexFile�� ����� ��� (key, value) �����͸� B+Ʈ���� ���� ����
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
	 * B+Ʈ���� ����� ��� �����͸� indexFile�� �����Ѵ�.
	 * 
	 * @param indexFile
	 * 		  �����͸� ������ ����
	 * @param bpTree
	 * 		  ���Ͽ� ������ �����Ͱ� ����Ǿ� �ִ� B+Ʈ��
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
					buffer.write(currentNode.getKeyPairArray()[i].getKey() + "," + currentNode.getKeyPairArray()[i].getValue() + "\n");  // (key, value) �� ����
				}
				
				// ���� ����� rightSibling ��尡 ���� ������ �̵��ϸ鼭 (Key, Value)�� �ۼ�
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