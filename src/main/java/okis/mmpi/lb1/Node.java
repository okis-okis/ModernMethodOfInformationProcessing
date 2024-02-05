package okis.mmpi.lb1;

public class Node {
	public Double x, functionValue;
	
	public Node(Double x, Double functionValue) {
		this.setX(x);
		this.setFunctionValue(functionValue);
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getFunctionValue() {
		return functionValue;
	}

	public void setFunctionValue(Double functionValue) {
		this.functionValue = functionValue;
	}
}
