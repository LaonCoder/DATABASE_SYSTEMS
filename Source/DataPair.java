/**
 * B+Ʈ���� ��忡 ����Ǵ� (key, value) �����͸� ������ Ŭ����
 */
public class DataPair implements Comparable<DataPair> {
	
	private int key;        
	private int value;      
	private Node leftChild;  // ���� �ڽ� ��带 ����Ű�� ������ (Non-leaf ��忡�� �ش�)

	
	public DataPair(int key, int value) {
		this.key = key;
		this.value = value;
		this.leftChild = null;
	}
	
	
	/* ���� DataPair�� ����� Ű ���� ��ȯ�Ѵ�. */
	public int getKey() {
		return this.key;
	}
	
	
	/* ���� DataPair�� ����� ���� ������ ���� ��ȯ�Ѵ�. */
	public int getValue() {
		return this.value;
	}
	
	
	/* ���� DataPair�� ����� ���� �ڽ� ����� �����͸� ��ȯ�Ѵ�. */
	public Node getLeftChild() {
		return this.leftChild;
	}
	
	
	/* ���� DataPair�� Ű ���� �־��� key ������ �����Ѵ�. */
	public void setKey(int key) {
		this.key = key;
	}
	
	
	/* ���� DataPair�� ���� ������ ���� �־��� value ������ �����Ѵ�. */
	public void setValue(int value) {
		this.value = value;
	}
	
	
	/* ���� DataPair�� ���� �ڽ� ��带 �־��� leftChild ���� �����Ѵ�. */
	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}
	

	@Override
	public int compareTo(DataPair o) {
		return (this.key - o.key);
	}
}
