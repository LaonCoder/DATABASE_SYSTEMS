import java.util.*;

/**
 * B+Ʈ���� ���(Node) Ŭ����
 */
public class Node {

	private int maxNumOfKey;          // ��尡 ���� �� �ִ� �ִ� Ű�� ���� : b - 1
	private int minNumOfKey;          // ��尡 ���� �� �ִ� �ּ� Ű�� ���� : Math.ceil(b/2) - 1
	private int currentNumOfKey;      // ���� ��忡 ����� Ű�� ���� : m
	private boolean isLeaf;           // ���� ��尡 Leaf ��������� ���� ����
	private DataPair[] keyPairArray;  // Leaf ����� ��� (key, value) pair��, Non-Leaf ����� ��� (key, leftChild) pair�� �����ϴ� �迭. (m + 1���� pair�� ����ȴ�.)
	private Node parent;              // ���� ����� �θ� ��� (�θ� ��尡 null�̸� ��Ʈ ���)
	private Node rightSibling;        // ���� ����� ������ ���� ���
	private Node rightmostChild;      // ���� ����� ���� ������ �ڽ� ��� 
	
	
	public Node(int maxNumOfKey) {
		this.maxNumOfKey = maxNumOfKey;
		this.minNumOfKey = (int)Math.ceil((double)(maxNumOfKey + 1) / 2) - 1;
		this.currentNumOfKey = 0;                       
		this.isLeaf = true;                                                            
		this.keyPairArray = new DataPair[maxNumOfKey + 1];  // '�ִ� Ű�� ���� + 1' ��ŭ�� DataPair�� ����� �� �ִ�. (��Ȱ�� split�� ����)
		this.parent = null;                                
		this.rightSibling = null;                           
		this.rightmostChild = null;                       
	}

	
	/**
	 * Node�� �־��� (key, value)���� ���� DataPair�� �����ϴ� �޼ҵ�
	 * 
	 * @param key 
	 * 		  ��忡 ���Ե� Ű ��
	 * @param value
	 * 		  ��忡 ���Ե� ���� ������ ��
	 * @param fromChild
	 *        �ڽ� ���κ��� ���� DataPair
	 * @return ���� ���� ���� ������ B+Ʈ���� ��Ʈ ���
	 */
	public Node push(int key, int value, DataPair fromChild) {
		
		if (isLeaf) {
			DataPair newDataPair = new DataPair(key, value);
			keyPairArray[currentNumOfKey++] = newDataPair;
			Arrays.sort(keyPairArray, Comparator.nullsLast(Comparator.naturalOrder()));

			// ��尡 ���� �� ���
			if (currentNumOfKey > maxNumOfKey) {
				
				if (parent == null) {
					Node parentNode = new Node(maxNumOfKey);
					Node rightSiblingNode = new Node(maxNumOfKey);
					
					// Ȧ��, ¦���� ���� ���� �ٸ� pivot �ε����� ����
					int i = (maxNumOfKey % 2 != 0) ? Math.round(maxNumOfKey / 2) + 1 : Math.round(maxNumOfKey / 2);
					
					parentNode.keyPairArray[0] = new DataPair(keyPairArray[i].getKey(), keyPairArray[i].getValue());
					parentNode.keyPairArray[0].setLeftChild(this);
					parentNode.rightmostChild = rightSiblingNode;
					parentNode.isLeaf = false;                      
					this.isLeaf = true;                             
					parentNode.addCurrentNumOfKey(1);
					
					// rightSiblingNode�� pivot �����͸� ������ ������ ���ݸ�ŭ�� ������ ����(Shallow copy)
					for (; i <= maxNumOfKey; i++) {
						rightSiblingNode.keyPairArray[rightSiblingNode.getCurrentNumOfKey()] = keyPairArray[i];
						rightSiblingNode.addCurrentNumOfKey(1);
						
						// ���� ����� pair �迭������ rightSiblingNode�� �߰��� ������ ����
						keyPairArray[i] = null;
						this.addCurrentNumOfKey(-1);
					}
					
					rightSiblingNode.rightSibling = this.rightSibling;
					rightSibling = rightSiblingNode;
					
					parent = parentNode;
					rightSiblingNode.parent = parentNode;

				}
				// �θ� ��尡 �ִ� ���
				else {
					Node rightSiblingNode = new Node(maxNumOfKey);
					
					int i = (maxNumOfKey % 2 != 0) ? Math.round(maxNumOfKey / 2) + 1 : Math.round(maxNumOfKey / 2);
					
					// �θ� ��忡 push�� DataPair ����
					DataPair parentDataPair = new DataPair(keyPairArray[i].getKey(), keyPairArray[i].getValue());
					
					for (; i <= maxNumOfKey; i++) {
						rightSiblingNode.keyPairArray[rightSiblingNode.getCurrentNumOfKey()] = keyPairArray[i];
						rightSiblingNode.addCurrentNumOfKey(1);
						
						keyPairArray[i] = null;
						this.addCurrentNumOfKey(-1);
					}
					
					rightSiblingNode.rightSibling = this.rightSibling;
					rightSibling = rightSiblingNode;
					
					rightSiblingNode.parent = parent;
					
					parentDataPair.setLeftChild(this);

					parent.push(key, value, parentDataPair);
				} 
			}
		}
		// Non-leaf ����� ���
		else {
			keyPairArray[currentNumOfKey++] = fromChild;  // �ڽ� ��忡�� ���� DataPair�� �迭�� ����
			Arrays.sort(keyPairArray, Comparator.nullsLast(Comparator.naturalOrder()));
			
			Node leftmostChild = keyPairArray[0].getLeftChild();
			
			// Non-Leaf ��忡 ����� �ڽ� ��� ������ �缳��
			for (int i = 0; i < currentNumOfKey; i++) {
				keyPairArray[i].setLeftChild(leftmostChild);
				leftmostChild = leftmostChild.rightSibling;
			}
			
			// ���� �������� ��ġ�� �ڽ� ��带 rightmostChild�� ����
			rightmostChild = leftmostChild;
			
			// ��尡 ���� �� ���
			if (currentNumOfKey > maxNumOfKey) {

				if (parent == null) {
					Node parentNode = new Node(maxNumOfKey);
					Node rightSiblingNode = new Node(maxNumOfKey);
					
					int i = (maxNumOfKey % 2 != 0) ? Math.round(maxNumOfKey / 2) + 1 : Math.round(maxNumOfKey / 2);
					
					parentNode.keyPairArray[0] = new DataPair(keyPairArray[i].getKey(), keyPairArray[i].getValue());

					parentNode.keyPairArray[0].setLeftChild(this);
					parentNode.rightmostChild = rightSiblingNode;
					parentNode.isLeaf = false;                      
					rightSiblingNode.isLeaf = false;
					parentNode.addCurrentNumOfKey(1);                
					
					rightSiblingNode.rightSibling = this.rightSibling;
					rightSibling = rightSiblingNode;
					
					this.rightmostChild.parent = rightSiblingNode;
					
					rightSibling.rightmostChild = this.rightmostChild;
					this.rightmostChild = keyPairArray[i].getLeftChild();
					
					// ���� ��忡 �ִ� pivot ������ ����
					this.keyPairArray[i++] = null;
					this.addCurrentNumOfKey(-1);
					
					// rightSiblingNode�� pivot ������ ������ ������ ���ݸ�ŭ�� ������ ����(Shallow copy)
					for (; i <= maxNumOfKey; i++) {
						keyPairArray[i].getLeftChild().parent = rightSibling;
						rightSiblingNode.keyPairArray[rightSiblingNode.getCurrentNumOfKey()] = keyPairArray[i];
						rightSiblingNode.addCurrentNumOfKey(1);
						
						keyPairArray[i] = null;
						this.addCurrentNumOfKey(-1);
					}
					
					parent = parentNode;
					rightSiblingNode.parent = parentNode;
				}
				// �θ� ��尡 �ִ� ���
				else {
					Node rightSiblingNode = new Node(maxNumOfKey);
					rightSiblingNode.isLeaf = false;
					
					int i = (maxNumOfKey % 2 != 0) ? Math.round(maxNumOfKey / 2) + 1 : Math.round(maxNumOfKey / 2);
					
					DataPair parentDataPair = new DataPair(keyPairArray[i].getKey(), keyPairArray[i].getValue());
					
					rightSiblingNode.rightSibling = this.rightSibling;
					rightSibling = rightSiblingNode;
					
					this.rightmostChild.parent = rightSiblingNode;
					
					rightSibling.rightmostChild = this.rightmostChild;
					this.rightmostChild = keyPairArray[i].getLeftChild();
					
					this.keyPairArray[i++] = null;
					this.addCurrentNumOfKey(-1);
					
					for (; i <= maxNumOfKey; i++) {
						keyPairArray[i].getLeftChild().parent = rightSibling;
						rightSiblingNode.keyPairArray[rightSiblingNode.getCurrentNumOfKey()] = keyPairArray[i];
						rightSiblingNode.addCurrentNumOfKey(1);
						
						keyPairArray[i] = null;
						this.addCurrentNumOfKey(-1);
					}
					
					rightSiblingNode.parent = parent;
					
					parentDataPair.setLeftChild(this);
					
					parent.push(key, value, parentDataPair);
				}
			}
		}
		// ��Ʈ ��带 ã�� ��ȯ
		Node rootNode = this;
		
		while (rootNode.parent != null)
			rootNode = rootNode.parent;
	
		return rootNode;
	}
	
	/**
	 * Node�� �־��� Ű ���� ���� DataPair�� �����ϸ�, �ش� DataPair�� �����ϴ� �޼ҵ�
	 * 
	 * @param key 
	 * 		  ��忡�� ������ Ű ��
	 * @return ���� ���� ���� ������ B+Ʈ���� ��Ʈ ���
	 */
	public Node remove(int key) {
		
		int i;  // ������ DataPair�� �ε���
		DataPair delData = null;
		
		// ���� ��忡�� �Է� ���� Ű�� ���� DataPair�� �����ϴ��� Ȯ��
		for (i = 0; i < currentNumOfKey; i++) {
			if (keyPairArray[i].getKey() == key) {
				delData = keyPairArray[i];
				break;
			}
		}
		
		if (delData == null) {
			return this.findRoot();
		}
		
		// 1. Leaf ����� ���ÿ� ��Ʈ ����� ���
		if (isLeaf && this.parent == null) {
			pullForward(i);
			addCurrentNumOfKey(-1);
			
			return this;
		}
		// 2. Leaf ����� ���
		else if (isLeaf) {		
			Node newRoot = null;
			
			// 1) ������ ���� �Ŀ��� �ּ� Ű ������ �����Ǵ� ���
			if (currentNumOfKey - 1 >= minNumOfKey) {
				pullForward(i);
				addCurrentNumOfKey(-1);
				
				// ������ �������� Ű ���� �θ� ����� �ε����� ���Ǵ� ���
				if (parent != null && i == 0) {
					parent.updateParentKeys();
					findRoot().updateRootKeys();
				} 
			}
			// 2) ������ ���� �� ���� Ű�� ������ �ּ� Ű �������� ���� ���
			else {
				int parentDPIndex = findParentDataPairIndex();
				
				// �� ���� ��尡 �θ� ����� rightmostChild�� �ƴϰ�, ������ ���� ��忡 ����� Ű�� ������ ����� ���
				if (parentDPIndex != -1 && getRightSiblingNode().currentNumOfKey - 1 >= minNumOfKey) {
					redistribute(i, "R");
				}
				// �� ���� ��尡 �θ� ����� leftmostChild�� �ƴϰ�, ���� ���� ��忡 ����� Ű�� ������ ����� ���
				else if (parentDPIndex != 0 && getLeftSiblingNode().currentNumOfKey - 1 >= minNumOfKey) {
					redistribute(i, "L");
				}
				// �� ���� ���� ��� ��� Ű�� ������ ������� �ʰ�, ���� ��尡 rightmostChild�� �ƴ� ���
				else if (parentDPIndex != -1) {
					newRoot = merge(i, "R");
				}
				// �� ���� ���� ����� Ű�� ������ ������� ���� rightmostChild�� ���	
				else {
					newRoot = merge(i, "L");
				}

			}
			
			return (newRoot == null) ? this.findRoot() : newRoot;
		} 
		// 3. ��Ʈ ��尡 �ƴ� Non-Leaf ����� ���
		else if (parent != null) {
			
			if (currentNumOfKey - 1 >= minNumOfKey) {
				pullForward(i);
				addCurrentNumOfKey(-1);
			}
			else {
				int parentDPIndex = findParentDataPairIndex();
				
				if (parentDPIndex != -1 && getRightSiblingNode().currentNumOfKey - 1 >= minNumOfKey) {
					redistribute(i, "R");
				}
				else if (parentDPIndex != 0 && getLeftSiblingNode().currentNumOfKey - 1 >= minNumOfKey) {
					redistribute(i, "L");
				}
				else if (parentDPIndex != -1) {
					merge(i, "R");	
				}
				else {
					merge(i, "L");
				}
			}
		}
		// 4. Leaf ��尡 �ƴ� ��Ʈ ����� ���
		else {			
			Node getLeftmostChild = keyPairArray[0].getLeftChild();
			
			pullForward(i);
			addCurrentNumOfKey(-1);
				
			// ��Ʈ ��忡 �� �̻� DairPair�� ���� ���, ���� ���� �ڽ� ��带 ��Ʈ ���� �����.
			if (currentNumOfKey == 0) {
				getLeftmostChild.parent = null;
			}
		}
		
		return null;
	}
	
	/**
	 * ���� �Ǵ� ������ ���� ��忡�� ���� DataPair�� ���� ���� �����´�.
	 * 
	 * @param deleteDataIndex
	 * 		  ���� ��忡�� ������ DataPair�� index
	 * @param lr
	 * 		  ���� �Ǵ� ������ ���� ��带 ����Ű�� ���ڿ� ("L" or "R")
	 */
	public void redistribute(int deleteDataIndex, String lr) {
		
		if (isLeaf) {
			this.pullForward(deleteDataIndex);
			this.addCurrentNumOfKey(-1);
			
			switch (lr) {
				case "L":
				{		
					// ���� ���� ��忡�� ���� ū Ű ���� ���� DataPair�� ������ ���� ��忡 �߰�
					Node leftSiblingNode = getLeftSiblingNode();
					pullBack(0);
					keyPairArray[0] = leftSiblingNode.getKeyPairArray()[leftSiblingNode.getCurrentNumOfKey() - 1];
					leftSiblingNode.getKeyPairArray()[leftSiblingNode.getCurrentNumOfKey() - 1] = null;
					this.addCurrentNumOfKey(1);
					leftSiblingNode.addCurrentNumOfKey(-1);
					
					break;
				}
				case "R":
				{	
					// ������ ���� ��忡�� ���� ���� Ű ���� ���� DataPair�� ������ ���� ��忡 �߰�
					Node rightSiblingNode = getRightSiblingNode();
					keyPairArray[currentNumOfKey] = rightSiblingNode.getKeyPairArray()[0];
					rightSiblingNode.pullForward(0);
					this.addCurrentNumOfKey(1);
					rightSiblingNode.addCurrentNumOfKey(-1);
					break;
				}
			}
			parent.updateParentKeys();
			findRoot().updateRootKeys();
		}
		// Non-Leaf ����� ���
		else {
			this.pullForward(deleteDataIndex);
			this.addCurrentNumOfKey(-1);
			
			switch (lr) {
				case "L":
				{		
					Node leftSiblingNode = getLeftSiblingNode();
					
					// ���� ���� ����� rightmostChild�� Array�� �� ���� �ִ� DataPair�� leftChild�� ����Ű�� Node�� ���� ��ü
					Node tempNode = leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey - 1].getLeftChild();
					leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey - 1].setLeftChild(leftSiblingNode.rightmostChild);
					leftSiblingNode.rightmostChild = tempNode;
					
					pullBack(0);
					
					// ���� ���� ����� Array�� �� ���� �ִ� DataPair�� ������ ���� ��忡 �߰�
					keyPairArray[0] = leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey - 1];
					keyPairArray[0].getLeftChild().parent = this;
					leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey - 1] = null;
					this.addCurrentNumOfKey(1);
					leftSiblingNode.addCurrentNumOfKey(-1);
					
					// leftSibling ��忡�� �ű� DataPair�� (key, value)�� �θ� ����� (key, value)�� ���� ��ü
					swapData(keyPairArray[0], parent.getKeyPairArray()[leftSiblingNode.findParentDataPairIndex()]);
					break;
				}
				case "R":
				{	
					// ������ ���� ��忡�� ���� ���� key���� ���� DataPair�� ������ ���� ��忡 �߰�
					Node rightSiblingNode = getRightSiblingNode();
					keyPairArray[currentNumOfKey] = rightSiblingNode.getKeyPairArray()[0];
					this.addCurrentNumOfKey(1);

					// rightSibling ��忡�� �ű� DataPair�� leftChild�� ����Ű�� ���� rightmostChild�� ����Ű�� ��带 ���� ��ü
					Node tempNode = keyPairArray[currentNumOfKey - 1].getLeftChild();
					keyPairArray[currentNumOfKey - 1].setLeftChild(rightmostChild);
					rightmostChild = tempNode;
					rightmostChild.parent = this;

					// rightSibling���� �ű� DataPair�� (key, value)�� �θ� ����� (key, value)�� ���� ��ü
					swapData(keyPairArray[currentNumOfKey - 1], parent.getKeyPairArray()[this.findParentDataPairIndex()]);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
					rightSiblingNode.pullForward(0);
					rightSiblingNode.addCurrentNumOfKey(-1);
					break;
				}
			}
		}
	}

	/**
	 * ���� �Ǵ� ������ ���� ���� ���� ��带 ���� �ϳ��� ���� �����.
	 * 
	 * @param deleteDataIndex
	 * 		  ���� ��忡�� ������ DataPair�� index
	 * @param lr
	 * 		  ���� �Ǵ� ������ ���� ��带 ����Ű�� ���ڿ� ("L" or "R")
	 * @return ���� ������ ���ο� ��Ʈ ���
	 */
	public Node merge(int deleteDataIndex, String lr) {
		this.pullForward(deleteDataIndex);
		addCurrentNumOfKey(-1);
		
		if (isLeaf) {
			switch (lr) {
				case "L":
				{		
					Node leftSiblingNode = getLeftSiblingNode();
					
					// ���� ����� ��� DataPair�� ���� ���� ��忡 ����
					int leftSiblingCurrentNumOfKey = leftSiblingNode.currentNumOfKey;
					for (int i = 0; i < currentNumOfKey; i++) {
						leftSiblingNode.getKeyPairArray()[i + leftSiblingCurrentNumOfKey] = keyPairArray[i];
						leftSiblingNode.addCurrentNumOfKey(1);
					}
					
					leftSiblingNode.rightSibling = this.rightSibling;
					
					parent.rightmostChild = leftSiblingNode;
					
					// �θ� ��忡�� leftSiblingNode�� leftChild�� ���� DataPair ����
					parent.remove(parent.keyPairArray[leftSiblingNode.findParentDataPairIndex()].getKey());
					
					this.parent = null;
				
					if (leftSiblingNode.parent != null) {
						leftSiblingNode.parent.updateParentKeys();
						leftSiblingNode.findRoot().updateRootKeys();
					}
					
					return leftSiblingNode.findRoot();
				}
				case "R":
				{	
					Node rightSiblingNode = getRightSiblingNode();
					
					// ������ ���� ����� ��� DataPair�� ���� ��忡 ����
					int thisCurrentNumOfKey = currentNumOfKey;
					for (int i = 0; i < rightSiblingNode.currentNumOfKey; i++) {
						keyPairArray[i + thisCurrentNumOfKey] = rightSiblingNode.getKeyPairArray()[i];
						addCurrentNumOfKey(1);				
					}
					
					this.rightSibling = rightSiblingNode.rightSibling;
					
					int rightSiblingIndex = rightSiblingNode.findParentDataPairIndex();
					
					// �θ� ��忡�� ������ ���� ��尡 leftChild�� ����� DataPair�� leftChild�� ���� ���� ����
					if (rightSiblingIndex == -1)
						parent.rightmostChild = this;
					else
 						parent.keyPairArray[rightSiblingIndex].setLeftChild(this);
					
					// �θ� ��忡�� ���� ��带 leftChild�� ���� DataPair�� ����
					parent.remove(parent.keyPairArray[findParentDataPairIndex()].getKey());
					
					rightSiblingNode.parent = null;
					
					if (parent != null) {
						parent.updateParentKeys();
						findRoot().updateRootKeys();
					}
					
					return this.findRoot();
				}
			}
		}
		// Non-leaf ����� ���
		else if (parent != null) {
			switch (lr) {
				case "L":
				{		
					Node leftSiblingNode = getLeftSiblingNode();
					
					// �θ� ��忡�� ���� ���� ��带 leftChild�� ���� DataPair�� (key, value)�� ���� ���ο� DataPair�� ���� ���� ����� �� �ڿ� �߰�
					// (�̶� ���ο� DataPair�� ����Ű�� leftChild�� ���� ���� ����� rightmostChild)
					DataPair leftSiblingParentDP = parent.keyPairArray[leftSiblingNode.findParentDataPairIndex()];
					leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey] = new DataPair(leftSiblingParentDP.getKey(), leftSiblingParentDP.getValue());
					leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey].setLeftChild(leftSiblingNode.rightmostChild);
					leftSiblingNode.addCurrentNumOfKey(1);
					
					int leftSiblingCurrentNumOfKey = leftSiblingNode.currentNumOfKey;
					for (int i = 0; i < currentNumOfKey; i++) {
						leftSiblingNode.getKeyPairArray()[i + leftSiblingCurrentNumOfKey] = keyPairArray[i];
						leftSiblingNode.getKeyPairArray()[i + leftSiblingCurrentNumOfKey].getLeftChild().parent = leftSiblingNode;  // �Ű��� DataPair�� leftChild�� ����Ű�� �θ� ��� �缳��
						leftSiblingNode.addCurrentNumOfKey(1);
					}
					
					leftSiblingNode.rightmostChild = this.rightmostChild;
					leftSiblingNode.rightmostChild.parent = leftSiblingNode;
					
					leftSiblingNode.rightSibling = this.rightSibling;
					
					parent.rightmostChild = leftSiblingNode;
					
					parent.remove(parent.keyPairArray[leftSiblingNode.findParentDataPairIndex()].getKey());
					
					break;
				}
				case "R":
				{	
					Node rightSiblingNode = getRightSiblingNode();
					
					// �θ� ��忡�� ���� ��带 leftChild�� ���� DataPair�� (key, value)�� ���� ���ο� DataPair�� ���� ����� �� �ڿ� �߰�
					// (�̶� ���ο� DataPair�� ����Ű�� leftChild�� ���� ����� rightmostChild)
					DataPair parentDP = parent.keyPairArray[this.findParentDataPairIndex()];
					keyPairArray[currentNumOfKey] = new DataPair(parentDP.getKey(), parentDP.getValue());
					keyPairArray[currentNumOfKey].setLeftChild(rightmostChild);
					this.addCurrentNumOfKey(1);
					
					int thisCurrentNumOfKey = currentNumOfKey;
					for (int i = 0; i < rightSiblingNode.currentNumOfKey; i++) {
						keyPairArray[i + thisCurrentNumOfKey] = rightSiblingNode.getKeyPairArray()[i];
						keyPairArray[i + thisCurrentNumOfKey].getLeftChild().parent = this;
						addCurrentNumOfKey(1);				
					}
					
					this.rightmostChild = rightSiblingNode.rightmostChild;
					this.rightmostChild.parent = this;
					
					this.rightSibling = rightSiblingNode.rightSibling;
					
					int rightSiblingIndex = rightSiblingNode.findParentDataPairIndex();
					
					if (rightSiblingIndex == -1)
						parent.rightmostChild = this;
					else
						parent.keyPairArray[rightSiblingIndex].setLeftChild(this);
					
					parent.remove(parent.keyPairArray[findParentDataPairIndex()].getKey());
					
					break;
				}
			}	
		}
		return null;
	}
	
	
	/* ���� ��忡 ����Ǿ� �ִ� Ű�� ������ ��ȯ�Ѵ�. */
	public int getCurrentNumOfKey() {
		return this.currentNumOfKey;
	}
	
	
	/* ���� ��尡 ���� ��������� ���� ������ ��ȯ�Ѵ�. */
	public boolean isLeafNode() {
		return this.isLeaf;
	}
	
	
	/* ���� ����� rightmostChild�� ��ȯ�Ѵ�. */
	public Node getRightmostChild() {
		return this.rightmostChild;
	}

	
	/* ���� ����� rightSibling�� ��ȯ�Ѵ�. */
	public Node getRightSibling() {
		return this.rightSibling;
	}
	
	
	/* ���� ����� keyPairArray�� ��ȯ�Ѵ�. */
	public DataPair[] getKeyPairArray() {
		return this.keyPairArray;
	}
	
	
	/* �� DataPair�� (key, value) �����͸� ���� �ٲ۴�. */
	private void swapData(DataPair nodeA, DataPair nodeB) {
		int tempData = nodeA.getKey();
		nodeA.setKey(nodeB.getKey());
		nodeB.setKey(tempData);
		
		tempData = nodeA.getValue();
		nodeA.setValue(nodeB.getValue());
		nodeB.setValue(tempData);
	}

	
	/* ���� ��尡 �θ� ��忡 ����� �� ��° DataPair�� �ڽ� ��������� ��ȯ�Ѵ�. */
	private int findParentDataPairIndex() {
		for (int i = 0; i < parent.currentNumOfKey; i++) {
			if (parent.keyPairArray[i].getLeftChild() == this) return i;
		}
		// ���� ��尡 RightmostChild ����� ���� -1�� ��ȯ
		return -1;  
	}
	
	
	/* ���� ����� ������ ���� ��带 �����´�. */
	private Node getRightSiblingNode() {
		int currentParentPDIndex = findParentDataPairIndex();
		
		// ���� ��尡 �θ� ����� rightmostChild�� ���
		if (currentParentPDIndex == -1)
			return null;
		// ���� ��尡 rightmostChild�� �ٷ� ���� ����� ���
		else if (parent.getKeyPairArray()[currentParentPDIndex + 1] == null)
			return parent.rightmostChild;
		else
			return parent.getKeyPairArray()[currentParentPDIndex + 1].getLeftChild();
	}
	

	/* ���� ����� ���� ���� ��带 �����´�. */
	private Node getLeftSiblingNode() {
		int currentParentPDIndex = findParentDataPairIndex();
		
		// ���� ��尡 �θ� ����� leftmostChild�� ���
		if (currentParentPDIndex == 0)
			return null;
		// ���� ��尡 rightmostChild�� ���
		else if (findParentDataPairIndex() == -1)
			return parent.getKeyPairArray()[parent.getCurrentNumOfKey() - 1].getLeftChild();
		else
			return parent.getKeyPairArray()[currentParentPDIndex - 1].getLeftChild();
	}


	/* ���� ���(Non-leaf)�� key�� value�� ������Ʈ�Ѵ�.(�ε��� ������Ʈ) */
	private void updateParentKeys() {
		int i;
		for (i = 0; i < currentNumOfKey - 1; i++) {
			getKeyPairArray()[i].setKey(getKeyPairArray()[i + 1].getLeftChild().getKeyPairArray()[0].getKey());
			getKeyPairArray()[i].setValue(getKeyPairArray()[i + 1].getLeftChild().getKeyPairArray()[0].getValue());
		}
		// ���� ������ DataPair�� rightmostChild�� ù ��° DataPair�� �ִ� key�� value�� ����
		getKeyPairArray()[i].setKey(rightmostChild.getKeyPairArray()[0].getKey());
		getKeyPairArray()[i].setValue(rightmostChild.getKeyPairArray()[0].getKey());
	}
	
	
	/* ��Ʈ �����(Non-leaf)�� key�� value�� ������Ʈ�Ѵ�.(�ε��� ������Ʈ) */
	private void updateRootKeys() {
		int i;
		for (i = 0; i < currentNumOfKey - 1; i++) {
			getKeyPairArray()[i].setKey(getKeyPairArray()[i + 1].getLeftChild().getMinimumDataPair().getKey());
			getKeyPairArray()[i].setValue(getKeyPairArray()[i + 1].getLeftChild().getMinimumDataPair().getValue());
		}
		// ���� ������ DataPair�� rightmostChild�� ���� Ʈ������ ���� ���� Ű ���� ���� DataPair(Successor)�� �ִ� key�� value�� ����
		getKeyPairArray()[i].setKey(rightmostChild.getMinimumDataPair().getKey());
		getKeyPairArray()[i].setValue(rightmostChild.getMinimumDataPair().getValue());
	}
	
	
	/* ���� ��带 ��Ʈ�� �ϴ� ���� Ʈ���� ���� ���� �߿��� ���� ���� Ű ���� ���� DataPair�� �����´�. */
	private DataPair getMinimumDataPair() {
		Node currentNode = this;
		
		while (!currentNode.isLeafNode()) {
			currentNode = currentNode.getKeyPairArray()[0].getLeftChild();
		}
		
		return currentNode.keyPairArray[0];
	}
	
	
	/* ���� ��带 �������� ��Ʈ ��带 ã�´�. */
	private Node findRoot() {
		Node currentNode = this;
		
		while (currentNode.parent != null) {
			currentNode = currentNode.parent;
		}
		
		return currentNode;
	}
	
	
	/* ���� ����� DataPair �迭���� i��°�� �ִ� ������ DataPair�� ��������, �ڿ� �ִ� DataPair���� �� ĭ�� ������ �ű��. (i��° �����ʹ� �����ȴ�.)*/
	private void pullForward(int i) {
		for (; i < currentNumOfKey; i++) {
			keyPairArray[i] = keyPairArray[i + 1];
		}
	}
	
	
	/* ���� ����� DataPair �迭���� i��°�� �ִ� DataPair�� �����Ͽ�, �ڿ� �ִ� ��� DataPair���� �� ĭ�� �ڷ� �ű��. */
	private void pullBack(int i) {
		int j;
		for (j = i; j < currentNumOfKey; j++) {
			keyPairArray[j + 1] = keyPairArray[j];
		}
		keyPairArray[i] = null;
	}
	
	
	/* increment��ŭ ���� Ű�� ������ �����ش�. */
	private void addCurrentNumOfKey(int increment) {
		this.currentNumOfKey += increment;
	}
}
