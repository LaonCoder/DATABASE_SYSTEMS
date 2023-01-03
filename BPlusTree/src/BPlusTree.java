/**
 * B+Ʈ�� Ŭ����
 */
public class BPlusTree {
	
	private Node root;          // B+Ʈ���� ��Ʈ ���
	private int maxNumOfChild;  // B+Ʈ���� ��尡 ���� �� �ִ� �ִ� �ڽ� ����� ����
	
	
	public BPlusTree(int maxNumOfChild) {
		this.root = new Node(maxNumOfChild - 1);
		this.maxNumOfChild = maxNumOfChild;
	}


	/**
	 * B+Ʈ���� (key, value) �����͸� �����Ѵ�. (Insertion)
	 * 
	 * @param key 
	 * 		  B+Ʈ���� ������ Ű ��
	 * @param value
	 * 		  B+Ʈ���� ������ ���� ������ ��
	 * @param fromChild
	 */
	public void insert(int key, int value) {
		Node currentNode = this.root;
		Node newRoot;
		
		while (!currentNode.isLeafNode()) {  
			boolean isSmallerKey = false;    // �Է� ���� Ű�� keyPairArray�� ����Ǿ� �ִ� DataPair���� �������� ��Ÿ���� ����
			
			DataPair[] currentKeyPairArray = currentNode.getKeyPairArray();

			for (int i = 0; i < currentNode.getCurrentNumOfKey(); i++) {
				// �Է� ���� key ���� �� ���� ���, �ش� DataPair�� leftChild ���� �̵�
				if (currentKeyPairArray[i].getKey() > key) {
					currentNode = currentKeyPairArray[i].getLeftChild();
					isSmallerKey = true;
					break;
				}
			}
			if (!isSmallerKey)  // ��� DataPair�� Ű ������ �Է� ���� key ���� Ŭ ���, ���� ����� rightmostChild ���� �̵�
				currentNode = currentNode.getRightmostChild();
		}
		
		newRoot = currentNode.push(key, value, null);
		this.root = newRoot;
	}
	
	
	/**
	 * B+Ʈ������ �־��� Ű ���� ���� �����Ͱ� ������ ���, �̸� ����Ѵ�. (Single Search)
	 * 
	 * @param key 
	 * 		  B+Ʈ������ Ž���� Ű ��
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
		
		// Leaf ������ Ž���ϸ鼭 �湮�� ������ Ű ������ ��� (�湮�� ��尡 ���� ��� ��� X)
		if (prevKeys.length() != 0)
			System.out.println(prevKeys.trim().replace(" ", ","));
		
		int i = 0;
		DataPair leafData = currentNode.getKeyPairArray()[i];
		
		if (leafData == null) {
			System.out.println("NO DATA IN TREE");
			return;
		}
		
		// ���� ����� Ű ����� �Է� ���� key �� ��
		while (leafData != null && leafData.getKey() != key)
			leafData = currentNode.getKeyPairArray()[++i];
		
		// ���� ��忡 �ش� key ���� ���� DataPair�� �����ϸ�, �ش� Ű�� �� �̷�� ���� ������ �� ��� (���� ��� "NOT FOUND" ���)
		System.out.println((leafData != null) ? leafData.getValue() : "NOT FOUND");
	}
	
	
	/**
	 * B+Ʈ������ �־��� ���� ���� Ű ���� ���� �����Ͱ� ������ ���, �̸� ��� ����Ѵ�. (Range Search)
	 * 
	 * @param startKey
	 * 		  B+Ʈ�� Ž�� ������ ����(����)
	 * @param endKey
	 * 		  B+Ʈ�� Ž�� ������ ����(����)
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
	 * B+Ʈ������ �ش� Ű ���� ���� �����Ͱ� ������ ���, �̸� �����Ѵ�. (Deletion)
	 * 
	 * @param startKey
	 * 		  B+Ʈ������ ������ �������� Ű ��
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
	
	
	/* B+Ʈ���� ��尡 ���� �� �ִ� �ִ� Ű ������ ��ȯ�Ѵ�. */
	public int getMaxNumOfChild() {
		return this.maxNumOfChild;
	}
	
	
	/* B+Ʈ������ ���� ���ʿ� �ִ� Leaf ��带 ��ȯ�Ѵ�. */
	public Node getLeftmostNode() {
		Node currentNode = this.root;
		
		while (!currentNode.isLeafNode()) {
			currentNode = currentNode.getKeyPairArray()[0].getLeftChild();
		}
		
		return currentNode;
	}

	
	/* B+Ʈ���� ����ִ����� ���� ������ ��ȯ�Ѵ�. */
	public boolean isEmpty() {
		return (this.root.getCurrentNumOfKey() == 0) ? true : false; 
	}
}
