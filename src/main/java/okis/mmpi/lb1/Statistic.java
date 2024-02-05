package okis.mmpi.lb1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;

public class Statistic {
	double min, max, delta;
	int N;
	List<Node> functionValue;
	
	public Statistic(double min, double max, double delta, int N) {
		this.min = min;	
		this.max = max;
		this.delta = delta;		
		
		this.N = N; 
		
		functionValue = new ArrayList();
		
		for(double d=min;d<=max+delta;d+=delta)
			functionValue.add(new Node(d, func(d)));
	}
	
	public List<Node> getNodes() {
		return functionValue;
	}
	
	private double func(double x) {
		return (N+4)*Math.sin(x/N)*(Math.random()%(N+4))+((N+2)/2)*Math.cos(x)*(Math.random()%(N+3))-(Math.random()%(N/2))+(N*x)/(N+2);
	}
	
	public String toExcelFile() {
		try {
			File templateFile = ResourceUtils.getFile("classpath:static/template.xlsx");
			System.out.println("Template file path: "+templateFile.getAbsolutePath());
			
			FileInputStream file = new FileInputStream(templateFile);
			
			Workbook workbook = new XSSFWorkbook(file);
			
			Sheet sheet = workbook.getSheet("Culc");
			
			sheet.getRow(0).getCell(1).setCellValue(this.min);
			sheet.getRow(1).getCell(1).setCellValue(this.max);
			sheet.getRow(2).getCell(1).setCellValue(this.delta);
			sheet.getRow(3).getCell(1).setCellValue(this.N);
			
			for(int i=0;i<functionValue.size();i++) {
				sheet.getRow(i).getCell(3).setCellValue((Double)functionValue.get(i).getFunctionValue());
				sheet.getRow(i).getCell(2).setCellValue((Double)functionValue.get(i).getX());
			}
			
			String[] functionExcel = {"AVERAGE(D1:D257)",
									"STDEV(D1:D257)/SQRT(COUNT(D1:D257))", 
									"VAR(D1:D257)", 
									"MEDIAN(D1:D257)", 
									"MIN(D1:D257)", 
									"MAX(D1:D257)", 
									"SUM(D1:D257)"
									};
			
			for(int i=0;i<functionExcel.length;i++) {
				XSSFFormulaEvaluator formulaEvaluator = 
						(XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
				XSSFCell formulaCell = (XSSFCell) sheet.getRow(i).createCell(5);
				formulaCell.setCellFormula(functionExcel[i]);
				formulaEvaluator.evaluateFormulaCell(formulaCell);
			}
			
			File resultFile = new File("result.xlsx");
			String saveFileLocation = resultFile.getPath();
			System.out.println("File path: "+saveFileLocation);
			
			FileOutputStream outputStream = new FileOutputStream(saveFileLocation);
			workbook.write(outputStream);
			workbook.close();
			file.close();
			
			return saveFileLocation;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
}
