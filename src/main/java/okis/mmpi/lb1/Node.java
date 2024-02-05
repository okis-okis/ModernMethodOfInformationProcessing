package okis.mmpi.lb1;

import java.text.DecimalFormat;

public class Node {
	public Double x, functionValue;
	public int id;
	
	public Node(int id, Double x, Double functionValue) {
		this.id = id;
		this.setX(x);
		this.setFunctionValue(functionValue);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = getTwoDecimal(x);
	}

	public Double getFunctionValue() {
		return functionValue;
	}

	public void setFunctionValue(Double functionValue) {
		this.functionValue = getTwoDecimal(functionValue);
	}
	
	public Double getTwoDecimal(Double d) {
		DecimalFormat df = new DecimalFormat("##.##");
		return Double.valueOf(df.format(d).replace(',', '.'));
	}
}
