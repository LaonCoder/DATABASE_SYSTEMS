/**
 * B+트리의 노드에 저장되는 (key, value) 데이터를 구현한 클래스
 */
public class DataPair implements Comparable<DataPair> {
	
	private int key;        
	private int value;      
	private Node leftChild;  // 왼쪽 자식 노드를 가리키는 포인터 (Non-leaf 노드에만 해당)

	
	public DataPair(int key, int value) {
		this.key = key;
		this.value = value;
		this.leftChild = null;
	}
	
	
	/* 현재 DataPair에 저장된 키 값을 반환한다. */
	public int getKey() {
		return this.key;
	}
	
	
	/* 현재 DataPair에 저장된 실제 데이터 값을 반환한다. */
	public int getValue() {
		return this.value;
	}
	
	
	/* 현재 DataPair에 저장된 왼쪽 자식 노드의 포인터를 반환한다. */
	public Node getLeftChild() {
		return this.leftChild;
	}
	
	
	/* 현재 DataPair의 키 값을 주어진 key 값으로 설정한다. */
	public void setKey(int key) {
		this.key = key;
	}
	
	
	/* 현재 DataPair의 실제 데이터 값을 주어진 value 값으로 설정한다. */
	public void setValue(int value) {
		this.value = value;
	}
	
	
	/* 현재 DataPair의 왼쪽 자식 노드를 주어진 leftChild 노드로 설정한다. */
	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}
	

	@Override
	public int compareTo(DataPair o) {
		return (this.key - o.key);
	}
}
