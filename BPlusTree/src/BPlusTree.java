/**
 * B+트리 클래스
 */
public class BPlusTree {
	
	private Node root;          // B+트리의 루트 노드
	private int maxNumOfChild;  // B+트리의 노드가 가질 수 있는 최대 자식 노드의 개수
	
	
	public BPlusTree(int maxNumOfChild) {
		this.root = new Node(maxNumOfChild - 1);
		this.maxNumOfChild = maxNumOfChild;
	}


	/**
	 * B+트리에 (key, value) 데이터를 삽입한다. (Insertion)
	 * 
	 * @param key 
	 * 		  B+트리에 삽입할 키 값
	 * @param value
	 * 		  B+트리에 삽입할 실제 데이터 값
	 * @param fromChild
	 */
	public void insert(int key, int value) {
		Node currentNode = this.root;
		Node newRoot;
		
		while (!currentNode.isLeafNode()) {  
			boolean isSmallerKey = false;    // 입력 받은 키가 keyPairArray에 저장되어 있는 DataPair보다 작은지를 나타내는 논리값
			
			DataPair[] currentKeyPairArray = currentNode.getKeyPairArray();

			for (int i = 0; i < currentNode.getCurrentNumOfKey(); i++) {
				// 입력 받은 key 값이 더 작은 경우, 해당 DataPair의 leftChild 노드로 이동
				if (currentKeyPairArray[i].getKey() > key) {
					currentNode = currentKeyPairArray[i].getLeftChild();
					isSmallerKey = true;
					break;
				}
			}
			if (!isSmallerKey)  // 모든 DataPair의 키 값보다 입력 받은 key 값이 클 경우, 현재 노드의 rightmostChild 노드로 이동
				currentNode = currentNode.getRightmostChild();
		}
		
		newRoot = currentNode.push(key, value, null);
		this.root = newRoot;
	}
	
	
	/**
	 * B+트리에서 주어진 키 값을 갖는 데이터가 존재할 경우, 이를 출력한다. (Single Search)
	 * 
	 * @param key 
	 * 		  B+트리에서 탐색할 키 값
	 */
	public void singleSearch(int key) {
		Node currentNode = this.root;

		String prevKeys = "";
		
		while (!currentNode.isLeafNode()) { 
			boolean isSmallerKey = false;    
			
			DataPair[] currentKeyPairArray = currentNode.getKeyPairArray();

			for (int i = 0; i < currentNode.getCurrentNumOfKey(); i++) {
				if (currentKeyPairArray[i].getKey() > key) {
					prevKeys += String.valueOf(currentKeyPairArray[i].getKey()) + " ";
					currentNode = currentKeyPairArray[i].getLeftChild();
					isSmallerKey = true;
					break;
				}
			}
			if (!isSmallerKey) {  
				prevKeys += String.valueOf(currentKeyPairArray[currentNode.getCurrentNumOfKey() - 1].getKey()) + " ";
				currentNode = currentNode.getRightmostChild();
			}
		}
		
		// Leaf 노드까지 탐색하면서 방문한 노드들의 키 값들을 출력 (방문한 노드가 없는 경우 출력 X)
		if (prevKeys.length() != 0)
			System.out.println(prevKeys.trim().replace(" ", ","));
		
		int i = 0;
		DataPair leafData = currentNode.getKeyPairArray()[i];
		
		if (leafData == null) {
			System.out.println("NO DATA IN TREE");
			return;
		}
		
		// 리프 노드의 키 값들과 입력 받은 key 값 비교
		while (leafData != null && leafData.getKey() != key)
			leafData = currentNode.getKeyPairArray()[++i];
		
		// 리프 노드에 해당 key 값을 갖는 DataPair가 존재하면, 해당 키와 페어를 이루는 실제 데이터 값 출력 (없을 경우 "NOT FOUND" 출력)
		System.out.println((leafData != null) ? leafData.getValue() : "NOT FOUND");
	}
	
	
	/**
	 * B+트리에서 주어진 범위 내의 키 값을 갖는 데이터가 존재할 경우, 이를 모두 출력한다. (Range Search)
	 * 
	 * @param startKey
	 * 		  B+트리 탐색 범위의 하한(포함)
	 * @param endKey
	 * 		  B+트리 탐색 범위의 상한(포함)
	 */
	public void rangeSearch(int startKey, int endKey) {
		Node currentNode = this.root;
		
		while (!currentNode.isLeafNode()) { 
			boolean isSmallerKey = false;
			
			DataPair[] currentKeyPairArray = currentNode.getKeyPairArray();

			for (int i = 0; i < currentNode.getCurrentNumOfKey(); i++) {
				if (currentKeyPairArray[i].getKey() > startKey) {
					currentNode = currentKeyPairArray[i].getLeftChild();
					isSmallerKey = true;
					break;
				}
			}
			if (!isSmallerKey) {
				currentNode = currentNode.getRightmostChild();
			}
		}

		int i = 0;
		DataPair leafData = currentNode.getKeyPairArray()[i];
		DataPair targetData = null;
		
		if (leafData == null) {
			System.out.println("NO DATA IN TREE");
			return;
		}
		
		while (leafData.getKey() < startKey) {
			leafData = currentNode.getKeyPairArray()[++i];
			
			if (leafData == null) {
				if (currentNode.getRightSibling() != null) {
					i = 0;
					currentNode = currentNode.getRightSibling();
					leafData = currentNode.getKeyPairArray()[i];
				}
				else {
					break;
				}
			}
		}
		
		if (leafData == null) {
			System.out.println("NOT FOUND");
			return;
		}

		while (leafData.getKey() <= endKey) {
			targetData = leafData;
			System.out.println(targetData.getKey() + "," + targetData.getValue());
			
			leafData = currentNode.getKeyPairArray()[++i];
			
			if (leafData == null) {
				if (currentNode.getRightSibling() != null) {
					i = 0;
					currentNode = currentNode.getRightSibling();
					leafData = currentNode.getKeyPairArray()[i];
				}
				else {
					break;
				}
			}
		}
		
		if (targetData == null) {
			System.out.println("NOT FOUND");
		}
	}
	

	/**
	 * B+트리에서 해당 키 값을 갖는 데이터가 존재할 경우, 이를 삭제한다. (Deletion)
	 * 
	 * @param startKey
	 * 		  B+트리에서 삭제할 데이터의 키 값
	 */
	public void delete(int key) {
		Node currentNode = this.root;
		Node newRoot;
		
		while (!currentNode.isLeafNode()) {
			boolean isSmallerKey = false;
			
			DataPair[] currentKeyPairArray = currentNode.getKeyPairArray();

			for (int i = 0; i < currentNode.getCurrentNumOfKey(); i++) {
				if (currentKeyPairArray[i].getKey() > key) {
					currentNode = currentKeyPairArray[i].getLeftChild();
					isSmallerKey = true;
					break;
				}
			}
			if (!isSmallerKey)
				currentNode = currentNode.getRightmostChild();
		}
		
		newRoot = currentNode.remove(key);
		this.root = newRoot;
	}
	
	
	/* B+트리의 노드가 가질 수 있는 최대 키 개수를 반환한다. */
	public int getMaxNumOfChild() {
		return this.maxNumOfChild;
	}
	
	
	/* B+트리에서 가장 왼쪽에 있는 Leaf 노드를 반환한다. */
	public Node getLeftmostNode() {
		Node currentNode = this.root;
		
		while (!currentNode.isLeafNode()) {
			currentNode = currentNode.getKeyPairArray()[0].getLeftChild();
		}
		
		return currentNode;
	}

	
	/* B+트리가 비어있는지에 대한 논리값을 반환한다. */
	public boolean isEmpty() {
		return (this.root.getCurrentNumOfKey() == 0) ? true : false; 
	}
}
