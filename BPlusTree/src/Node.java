import java.util.*;

/**
 * B+트리의 노드(Node) 클래스
 */
public class Node {

	private int maxNumOfKey;          // 노드가 가질 수 있는 최대 키의 개수 : b - 1
	private int minNumOfKey;          // 노드가 가질 수 있는 최소 키의 개수 : Math.ceil(b/2) - 1
	private int currentNumOfKey;      // 현재 노드에 저장된 키의 개수 : m
	private boolean isLeaf;           // 현재 노드가 Leaf 노드인지에 대한 논리값
	private DataPair[] keyPairArray;  // Leaf 노드의 경우 (key, value) pair를, Non-Leaf 노드의 경우 (key, leftChild) pair를 저장하는 배열. (m + 1개의 pair가 저장된다.)
	private Node parent;              // 현재 노드의 부모 노드 (부모 노드가 null이면 루트 노드)
	private Node rightSibling;        // 현재 노드의 오른쪽 형제 노드
	private Node rightmostChild;      // 현재 노드의 가장 오른쪽 자식 노드 
	
	
	public Node(int maxNumOfKey) {
		this.maxNumOfKey = maxNumOfKey;
		this.minNumOfKey = (int)Math.ceil((double)(maxNumOfKey + 1) / 2) - 1;
		this.currentNumOfKey = 0;                       
		this.isLeaf = true;                                                            
		this.keyPairArray = new DataPair[maxNumOfKey + 1];  // '최대 키의 개수 + 1' 만큼의 DataPair가 저장될 수 있다. (원활한 split을 위함)
		this.parent = null;                                
		this.rightSibling = null;                           
		this.rightmostChild = null;                       
	}

	
	/**
	 * Node에 주어진 (key, value)값을 갖는 DataPair를 삽입하는 메소드
	 * 
	 * @param key 
	 * 		  노드에 삽입될 키 값
	 * @param value
	 * 		  노드에 삽입될 실제 데이터 값
	 * @param fromChild
	 *        자식 노드로부터 받은 DataPair
	 * @return 삽입 이후 새로 구성된 B+트리의 루트 노드
	 */
	public Node push(int key, int value, DataPair fromChild) {
		
		if (isLeaf) {
			DataPair newDataPair = new DataPair(key, value);
			keyPairArray[currentNumOfKey++] = newDataPair;
			Arrays.sort(keyPairArray, Comparator.nullsLast(Comparator.naturalOrder()));

			// 노드가 가득 찬 경우
			if (currentNumOfKey > maxNumOfKey) {
				
				if (parent == null) {
					Node parentNode = new Node(maxNumOfKey);
					Node rightSiblingNode = new Node(maxNumOfKey);
					
					// 홀수, 짝수에 따라 서로 다른 pivot 인덱스를 설정
					int i = (maxNumOfKey % 2 != 0) ? Math.round(maxNumOfKey / 2) + 1 : Math.round(maxNumOfKey / 2);
					
					parentNode.keyPairArray[0] = new DataPair(keyPairArray[i].getKey(), keyPairArray[i].getValue());
					parentNode.keyPairArray[0].setLeftChild(this);
					parentNode.rightmostChild = rightSiblingNode;
					parentNode.isLeaf = false;                      
					this.isLeaf = true;                             
					parentNode.addCurrentNumOfKey(1);
					
					// rightSiblingNode에 pivot 데이터를 포함한 오른쪽 절반만큼의 데이터 복사(Shallow copy)
					for (; i <= maxNumOfKey; i++) {
						rightSiblingNode.keyPairArray[rightSiblingNode.getCurrentNumOfKey()] = keyPairArray[i];
						rightSiblingNode.addCurrentNumOfKey(1);
						
						// 현재 노드의 pair 배열에서는 rightSiblingNode에 추가한 데이터 삭제
						keyPairArray[i] = null;
						this.addCurrentNumOfKey(-1);
					}
					
					rightSiblingNode.rightSibling = this.rightSibling;
					rightSibling = rightSiblingNode;
					
					parent = parentNode;
					rightSiblingNode.parent = parentNode;

				}
				// 부모 노드가 있는 경우
				else {
					Node rightSiblingNode = new Node(maxNumOfKey);
					
					int i = (maxNumOfKey % 2 != 0) ? Math.round(maxNumOfKey / 2) + 1 : Math.round(maxNumOfKey / 2);
					
					// 부모 노드에 push될 DataPair 생성
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
		// Non-leaf 노드인 경우
		else {
			keyPairArray[currentNumOfKey++] = fromChild;  // 자식 노드에서 받은 DataPair를 배열에 삽입
			Arrays.sort(keyPairArray, Comparator.nullsLast(Comparator.naturalOrder()));
			
			Node leftmostChild = keyPairArray[0].getLeftChild();
			
			// Non-Leaf 노드에 연결된 자식 노드 포인터 재설정
			for (int i = 0; i < currentNumOfKey; i++) {
				keyPairArray[i].setLeftChild(leftmostChild);
				leftmostChild = leftmostChild.rightSibling;
			}
			
			// 가장 마지막에 위치한 자식 노드를 rightmostChild로 설정
			rightmostChild = leftmostChild;
			
			// 노드가 가득 찬 경우
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
					
					// 현재 노드에 있는 pivot 데이터 삭제
					this.keyPairArray[i++] = null;
					this.addCurrentNumOfKey(-1);
					
					// rightSiblingNode에 pivot 데이터 미포함 오른쪽 절반만큼의 데이터 복사(Shallow copy)
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
				// 부모 노드가 있는 경우
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
		// 루트 노드를 찾아 반환
		Node rootNode = this;
		
		while (rootNode.parent != null)
			rootNode = rootNode.parent;
	
		return rootNode;
	}
	
	/**
	 * Node에 주어진 키 값을 갖는 DataPair가 존재하면, 해당 DataPair를 삭제하는 메소드
	 * 
	 * @param key 
	 * 		  노드에서 삭제할 키 값
	 * @return 삭제 이후 새로 구성된 B+트리의 루트 노드
	 */
	public Node remove(int key) {
		
		int i;  // 삭제될 DataPair의 인덱스
		DataPair delData = null;
		
		// 현재 노드에서 입력 받은 키를 갖는 DataPair가 존재하는지 확인
		for (i = 0; i < currentNumOfKey; i++) {
			if (keyPairArray[i].getKey() == key) {
				delData = keyPairArray[i];
				break;
			}
		}
		
		if (delData == null) {
			return this.findRoot();
		}
		
		// 1. Leaf 노드인 동시에 루트 노드인 경우
		if (isLeaf && this.parent == null) {
			pullForward(i);
			addCurrentNumOfKey(-1);
			
			return this;
		}
		// 2. Leaf 노드인 경우
		else if (isLeaf) {		
			Node newRoot = null;
			
			// 1) 데이터 삭제 후에도 최소 키 개수가 유지되는 경우
			if (currentNumOfKey - 1 >= minNumOfKey) {
				pullForward(i);
				addCurrentNumOfKey(-1);
				
				// 삭제할 데이터의 키 값이 부모 노드의 인덱스로 사용되는 경우
				if (parent != null && i == 0) {
					parent.updateParentKeys();
					findRoot().updateRootKeys();
				} 
			}
			// 2) 데이터 삭제 후 현재 키의 개수가 최소 키 개수보다 작은 경우
			else {
				int parentDPIndex = findParentDataPairIndex();
				
				// ① 현재 노드가 부모 노드의 rightmostChild가 아니고, 오른쪽 형제 노드에 저장된 키의 개수가 충분한 경우
				if (parentDPIndex != -1 && getRightSiblingNode().currentNumOfKey - 1 >= minNumOfKey) {
					redistribute(i, "R");
				}
				// ② 현재 노드가 부모 노드의 leftmostChild가 아니고, 왼쪽 형제 노드에 저장된 키의 개수가 충분한 경우
				else if (parentDPIndex != 0 && getLeftSiblingNode().currentNumOfKey - 1 >= minNumOfKey) {
					redistribute(i, "L");
				}
				// ③ 양쪽 형제 노드 모두 키의 개수가 충분하지 않고, 현재 노드가 rightmostChild가 아닌 경우
				else if (parentDPIndex != -1) {
					newRoot = merge(i, "R");
				}
				// ④ 왼쪽 형제 노드의 키의 개수가 충분하지 않은 rightmostChild인 경우	
				else {
					newRoot = merge(i, "L");
				}

			}
			
			return (newRoot == null) ? this.findRoot() : newRoot;
		} 
		// 3. 루트 노드가 아닌 Non-Leaf 노드인 경우
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
		// 4. Leaf 노드가 아닌 루트 노드인 경우
		else {			
			Node getLeftmostChild = keyPairArray[0].getLeftChild();
			
			pullForward(i);
			addCurrentNumOfKey(-1);
				
			// 루트 노드에 더 이상 DairPair가 없는 경우, 가장 왼쪽 자식 노드를 루트 노드로 만든다.
			if (currentNumOfKey == 0) {
				getLeftmostChild.parent = null;
			}
		}
		
		return null;
	}
	
	/**
	 * 왼쪽 또는 오른쪽 형제 노드에서 남는 DataPair를 현재 노드로 가져온다.
	 * 
	 * @param deleteDataIndex
	 * 		  현재 노드에서 삭제할 DataPair의 index
	 * @param lr
	 * 		  왼쪽 또는 오른쪽 형제 노드를 가리키는 문자열 ("L" or "R")
	 */
	public void redistribute(int deleteDataIndex, String lr) {
		
		if (isLeaf) {
			this.pullForward(deleteDataIndex);
			this.addCurrentNumOfKey(-1);
			
			switch (lr) {
				case "L":
				{		
					// 왼쪽 형제 노드에서 가장 큰 키 값을 갖는 DataPair를 가져와 현재 노드에 추가
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
					// 오른쪽 형제 노드에서 가장 작은 키 값을 갖는 DataPair를 가져와 현재 노드에 추가
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
		// Non-Leaf 노드인 경우
		else {
			this.pullForward(deleteDataIndex);
			this.addCurrentNumOfKey(-1);
			
			switch (lr) {
				case "L":
				{		
					Node leftSiblingNode = getLeftSiblingNode();
					
					// 왼쪽 형제 노드의 rightmostChild와 Array의 맨 끝에 있는 DataPair의 leftChild가 가리키는 Node를 서로 교체
					Node tempNode = leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey - 1].getLeftChild();
					leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey - 1].setLeftChild(leftSiblingNode.rightmostChild);
					leftSiblingNode.rightmostChild = tempNode;
					
					pullBack(0);
					
					// 왼쪽 형제 노드의 Array의 맨 끝에 있는 DataPair를 가져와 현재 노드에 추가
					keyPairArray[0] = leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey - 1];
					keyPairArray[0].getLeftChild().parent = this;
					leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey - 1] = null;
					this.addCurrentNumOfKey(1);
					leftSiblingNode.addCurrentNumOfKey(-1);
					
					// leftSibling 노드에서 옮긴 DataPair의 (key, value)와 부모 노드의 (key, value)를 서로 교체
					swapData(keyPairArray[0], parent.getKeyPairArray()[leftSiblingNode.findParentDataPairIndex()]);
					break;
				}
				case "R":
				{	
					// 오른쪽 형제 노드에서 가장 작은 key값을 갖는 DataPair를 가져와 현재 노드에 추가
					Node rightSiblingNode = getRightSiblingNode();
					keyPairArray[currentNumOfKey] = rightSiblingNode.getKeyPairArray()[0];
					this.addCurrentNumOfKey(1);

					// rightSibling 노드에서 옮긴 DataPair의 leftChild가 가리키는 노드와 rightmostChild가 가리키는 노드를 서로 교체
					Node tempNode = keyPairArray[currentNumOfKey - 1].getLeftChild();
					keyPairArray[currentNumOfKey - 1].setLeftChild(rightmostChild);
					rightmostChild = tempNode;
					rightmostChild.parent = this;

					// rightSibling에서 옮긴 DataPair의 (key, value)와 부모 노드의 (key, value)를 서로 교체
					swapData(keyPairArray[currentNumOfKey - 1], parent.getKeyPairArray()[this.findParentDataPairIndex()]);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
					rightSiblingNode.pullForward(0);
					rightSiblingNode.addCurrentNumOfKey(-1);
					break;
				}
			}
		}
	}

	/**
	 * 왼쪽 또는 오른쪽 형제 노드와 현재 노드를 합쳐 하나의 노드로 만든다.
	 * 
	 * @param deleteDataIndex
	 * 		  현재 노드에서 삭제할 DataPair의 index
	 * @param lr
	 * 		  왼쪽 또는 오른쪽 형제 노드를 가리키는 문자열 ("L" or "R")
	 * @return 삭제 이후의 새로운 루트 노드
	 */
	public Node merge(int deleteDataIndex, String lr) {
		this.pullForward(deleteDataIndex);
		addCurrentNumOfKey(-1);
		
		if (isLeaf) {
			switch (lr) {
				case "L":
				{		
					Node leftSiblingNode = getLeftSiblingNode();
					
					// 현재 노드의 모든 DataPair를 왼쪽 형제 노드에 복사
					int leftSiblingCurrentNumOfKey = leftSiblingNode.currentNumOfKey;
					for (int i = 0; i < currentNumOfKey; i++) {
						leftSiblingNode.getKeyPairArray()[i + leftSiblingCurrentNumOfKey] = keyPairArray[i];
						leftSiblingNode.addCurrentNumOfKey(1);
					}
					
					leftSiblingNode.rightSibling = this.rightSibling;
					
					parent.rightmostChild = leftSiblingNode;
					
					// 부모 노드에서 leftSiblingNode를 leftChild로 갖는 DataPair 제거
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
					
					// 오른쪽 형제 노드의 모든 DataPair를 현재 노드에 복사
					int thisCurrentNumOfKey = currentNumOfKey;
					for (int i = 0; i < rightSiblingNode.currentNumOfKey; i++) {
						keyPairArray[i + thisCurrentNumOfKey] = rightSiblingNode.getKeyPairArray()[i];
						addCurrentNumOfKey(1);				
					}
					
					this.rightSibling = rightSiblingNode.rightSibling;
					
					int rightSiblingIndex = rightSiblingNode.findParentDataPairIndex();
					
					// 부모 노드에서 오른쪽 형제 노드가 leftChild로 저장된 DataPair의 leftChild를 현재 노드로 설정
					if (rightSiblingIndex == -1)
						parent.rightmostChild = this;
					else
 						parent.keyPairArray[rightSiblingIndex].setLeftChild(this);
					
					// 부모 노드에서 현재 노드를 leftChild로 갖는 DataPair를 제거
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
		// Non-leaf 노드인 경우
		else if (parent != null) {
			switch (lr) {
				case "L":
				{		
					Node leftSiblingNode = getLeftSiblingNode();
					
					// 부모 노드에서 왼쪽 형제 노드를 leftChild로 갖는 DataPair의 (key, value)를 갖는 새로운 DataPair를 왼쪽 형제 노드의 맨 뒤에 추가
					// (이때 새로운 DataPair가 가리키는 leftChild는 왼쪽 형제 노드의 rightmostChild)
					DataPair leftSiblingParentDP = parent.keyPairArray[leftSiblingNode.findParentDataPairIndex()];
					leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey] = new DataPair(leftSiblingParentDP.getKey(), leftSiblingParentDP.getValue());
					leftSiblingNode.keyPairArray[leftSiblingNode.currentNumOfKey].setLeftChild(leftSiblingNode.rightmostChild);
					leftSiblingNode.addCurrentNumOfKey(1);
					
					int leftSiblingCurrentNumOfKey = leftSiblingNode.currentNumOfKey;
					for (int i = 0; i < currentNumOfKey; i++) {
						leftSiblingNode.getKeyPairArray()[i + leftSiblingCurrentNumOfKey] = keyPairArray[i];
						leftSiblingNode.getKeyPairArray()[i + leftSiblingCurrentNumOfKey].getLeftChild().parent = leftSiblingNode;  // 옮겨진 DataPair의 leftChild가 가리키는 부모 노드 재설정
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
					
					// 부모 노드에서 현재 노드를 leftChild로 갖는 DataPair의 (key, value)를 갖는 새로운 DataPair를 현재 노드의 맨 뒤에 추가
					// (이때 새로운 DataPair가 가리키는 leftChild는 현재 노드의 rightmostChild)
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
	
	
	/* 현재 노드에 저장되어 있는 키의 개수를 반환한다. */
	public int getCurrentNumOfKey() {
		return this.currentNumOfKey;
	}
	
	
	/* 현재 노드가 리프 노드인지에 대한 논리값을 반환한다. */
	public boolean isLeafNode() {
		return this.isLeaf;
	}
	
	
	/* 현재 노드의 rightmostChild를 반환한다. */
	public Node getRightmostChild() {
		return this.rightmostChild;
	}

	
	/* 현재 노드의 rightSibling을 반환한다. */
	public Node getRightSibling() {
		return this.rightSibling;
	}
	
	
	/* 현재 노드의 keyPairArray를 반환한다. */
	public DataPair[] getKeyPairArray() {
		return this.keyPairArray;
	}
	
	
	/* 두 DataPair의 (key, value) 데이터를 서로 바꾼다. */
	private void swapData(DataPair nodeA, DataPair nodeB) {
		int tempData = nodeA.getKey();
		nodeA.setKey(nodeB.getKey());
		nodeB.setKey(tempData);
		
		tempData = nodeA.getValue();
		nodeA.setValue(nodeB.getValue());
		nodeB.setValue(tempData);
	}

	
	/* 현재 노드가 부모 노드에 저장된 몇 번째 DataPair의 자식 노드인지를 반환한다. */
	private int findParentDataPairIndex() {
		for (int i = 0; i < parent.currentNumOfKey; i++) {
			if (parent.keyPairArray[i].getLeftChild() == this) return i;
		}
		// 현재 노드가 RightmostChild 노드인 경우는 -1을 반환
		return -1;  
	}
	
	
	/* 현재 노드의 오른쪽 형제 노드를 가져온다. */
	private Node getRightSiblingNode() {
		int currentParentPDIndex = findParentDataPairIndex();
		
		// 현재 노드가 부모 노드의 rightmostChild인 경우
		if (currentParentPDIndex == -1)
			return null;
		// 현재 노드가 rightmostChild의 바로 이전 노드인 경우
		else if (parent.getKeyPairArray()[currentParentPDIndex + 1] == null)
			return parent.rightmostChild;
		else
			return parent.getKeyPairArray()[currentParentPDIndex + 1].getLeftChild();
	}
	

	/* 현재 노드의 왼쪽 형제 노드를 가져온다. */
	private Node getLeftSiblingNode() {
		int currentParentPDIndex = findParentDataPairIndex();
		
		// 현재 노드가 부모 노드의 leftmostChild인 경우
		if (currentParentPDIndex == 0)
			return null;
		// 현재 노드가 rightmostChild인 경우
		else if (findParentDataPairIndex() == -1)
			return parent.getKeyPairArray()[parent.getCurrentNumOfKey() - 1].getLeftChild();
		else
			return parent.getKeyPairArray()[currentParentPDIndex - 1].getLeftChild();
	}


	/* 현재 노드(Non-leaf)의 key와 value를 업데이트한다.(인덱스 업데이트) */
	private void updateParentKeys() {
		int i;
		for (i = 0; i < currentNumOfKey - 1; i++) {
			getKeyPairArray()[i].setKey(getKeyPairArray()[i + 1].getLeftChild().getKeyPairArray()[0].getKey());
			getKeyPairArray()[i].setValue(getKeyPairArray()[i + 1].getLeftChild().getKeyPairArray()[0].getValue());
		}
		// 가장 마지막 DataPair는 rightmostChild의 첫 번째 DataPair에 있는 key와 value를 복사
		getKeyPairArray()[i].setKey(rightmostChild.getKeyPairArray()[0].getKey());
		getKeyPairArray()[i].setValue(rightmostChild.getKeyPairArray()[0].getKey());
	}
	
	
	/* 루트 노드의(Non-leaf)의 key와 value를 업데이트한다.(인덱스 업데이트) */
	private void updateRootKeys() {
		int i;
		for (i = 0; i < currentNumOfKey - 1; i++) {
			getKeyPairArray()[i].setKey(getKeyPairArray()[i + 1].getLeftChild().getMinimumDataPair().getKey());
			getKeyPairArray()[i].setValue(getKeyPairArray()[i + 1].getLeftChild().getMinimumDataPair().getValue());
		}
		// 가장 마지막 DataPair는 rightmostChild의 서브 트리에서 가장 작은 키 값을 갖는 DataPair(Successor)에 있는 key와 value를 복사
		getKeyPairArray()[i].setKey(rightmostChild.getMinimumDataPair().getKey());
		getKeyPairArray()[i].setValue(rightmostChild.getMinimumDataPair().getValue());
	}
	
	
	/* 현재 노드를 루트로 하는 서브 트리의 리프 노드들 중에서 가장 작은 키 값을 갖는 DataPair를 가져온다. */
	private DataPair getMinimumDataPair() {
		Node currentNode = this;
		
		while (!currentNode.isLeafNode()) {
			currentNode = currentNode.getKeyPairArray()[0].getLeftChild();
		}
		
		return currentNode.keyPairArray[0];
	}
	
	
	/* 현재 노드를 기준으로 루트 노드를 찾는다. */
	private Node findRoot() {
		Node currentNode = this;
		
		while (currentNode.parent != null) {
			currentNode = currentNode.parent;
		}
		
		return currentNode;
	}
	
	
	/* 현재 노드의 DataPair 배열에서 i번째에 있는 삭제될 DataPair를 기준으로, 뒤에 있는 DataPair들을 한 칸씩 앞으로 옮긴다. (i번째 데이터는 삭제된다.)*/
	private void pullForward(int i) {
		for (; i < currentNumOfKey; i++) {
			keyPairArray[i] = keyPairArray[i + 1];
		}
	}
	
	
	/* 현재 노드의 DataPair 배열에서 i번째에 있는 DataPair를 포함하여, 뒤에 있는 모든 DataPair들을 한 칸씩 뒤로 옮긴다. */
	private void pullBack(int i) {
		int j;
		for (j = i; j < currentNumOfKey; j++) {
			keyPairArray[j + 1] = keyPairArray[j];
		}
		keyPairArray[i] = null;
	}
	
	
	/* increment만큼 현재 키의 개수를 더해준다. */
	private void addCurrentNumOfKey(int increment) {
		this.currentNumOfKey += increment;
	}
}
