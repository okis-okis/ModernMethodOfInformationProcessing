package okis.mmpi.lb1;

/**
* Author: OKis
* Group:  SCS-23m
* Organization: DSTU
* Language: Java
* Language version: 17 (maven project)
* Frameworks: Spring
* Date: 05.01.2024
* Revision:  04.04.2024 - добавление комментариев
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;

/**
 * Класс для обработки стастических данных
 */
public class Statistic {
	/**
	 * Минимальное значение в массиве данных X
	 */
	private double min; 
	
	/**
	 * Максимальное значение в массиве данных X
	 */
	private double max;
	
	/**
	 * Значение шага для массива X
	 */
	private double delta;
	
	/**
	 * Номер студента по списку (коэффициент для f(x))
	 */
	int N;
	
	/**
	 * Массив результатов выполнения функции f(x)
	 */
	List<Node> functionValue;
	
	/**
	 * Среднее значение результата выполнения функции f(x)
	 */
	double averageValue;
	
	/**
	 * Значение ошибки
	 */
	double standardError; 
	
	/**
	 * Значение дисперсии
	 */
	double variance;
	
	/**
	 * Значение медианы
	 */
	double median; 
	
	/**
	 * Минимальное значение результата выполнения функции f(x)
	 */
	double minimumValue; 
	
	/**
	 * Максимальное значение результата выполнения функции f(x)
	 */
	double maximumValue; 
	
	/**
	 * Сумма всех значений результата выполнения функции f(x)
	 */
	double amount; 
	
	/**
	 * Линия тренда (конечная точка)
	 */
	double trend;
	
	public double getTrend() {
		return trend;
	}

	public void setTrend(double trend) {
		this.trend = trend;
	}

	public double getAverageValue() {
		return averageValue;
	}

	public void setAverageValue(double averageValue) {
		this.averageValue = averageValue;
	}

	public double getStandardError() {
		return standardError;
	}

	public void setStandardError(double standardError) {
		this.standardError = standardError;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getMedian() {
		return median;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public double getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(double minimumValue) {
		this.minimumValue = minimumValue;
	}

	public double getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(double maximumValue) {
		this.maximumValue = maximumValue;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * Функция вычисления статистических данных
	 * @param min - Нижняя граница (минимальное значение) X<sup>i</sup>
	 * @param max - Верхняя граница (максимальное значение) X<sup>i</sup>
	 * @param delta - Значение шага X<sup>i</sup>
	 * @param N - Номер студента по списку (коэффициент)
	 */
	public Statistic(double min, double max, double delta, int N) {
		this.min = min;	
		this.max = max;
		this.delta = delta;		
		
		this.N = N; 
		
		functionValue = new ArrayList();
		
		for(double d=min;d<=max+delta;d+=delta)
			functionValue.add(new Node((int)(d/delta), d, func(d)));
	}
	
	public List<Node> getNodes() {
		return functionValue;
	}
	
	/**
	 * Вычислить значение функции f(x)
	 * @param x Параметр X
	 * @return Результат выполнения функции f(x)
	 */
	private double func(double x) {
		//return (N+4)*Math.sin(x/N)*(Math.random()%(N+4))+((N+2)/2)*Math.cos(x)*(Math.random()%(N+3))-(Math.random()%(N/2))+(N*x)/(N+2);
		return (N+4)*Math.sin(x/N) + (N+2)/2*Math.cos(N*x);
	}
	
	/**
	 * Получить гармоники для массива X
	 * @return массив комплексных чисел, являющихся гармониками массива X
	 */
	public Complex[] getHarmonicsComplex() {
		Complex[] x = new Complex[functionValue.size()-1];

        for (int i = 0; i < x.length; i++) {
            x[i] = new Complex(functionValue.get(i+1).getFunctionValue(), 0);
        }
        return FFT.dft(x);
	}
	
	/**
	 * Получить гармоники с фитром по нижней границе
	 * @param harmonicsArray Массив комплексных чисел гармоник
	 * @param filterValue Пароговое значение фильтра
	 * @return новый отфильтрованный массив комплексных чисел 
	 */
	public Complex[] getHarmonicsArrayFilter(Complex[] harmonicsArray, int filterValue) {		      
		Complex[] filter = new Complex[harmonicsArray.length];
        
		for(int i=0;i<harmonicsArray.length;i++)
        	filter[i] = harmonicsArray[i].abs()>filterValue?harmonicsArray[i]:new Complex(0, 0);
        
        return filter;
	}
	
	/**
	 * Преобразовать комплексные функции в массив dobule[]
	 * @param harmonicsArray Массив комплексных чисел
	 * @return восстановленный массив
	 */
	public double[] getRestoredArray(Complex[] harmonicsArray) {		                	
        Complex[] z = FFT.ifft(harmonicsArray);
        
        double[] result = new double[z.length];
        
        for (int i=0;i<result.length;i++)
        	result[i] = z[i].abs();
        	
        return result;
	}
	
	/**
	 * Конвертировать массив комплексных значений в массив типа double[]
	 * @param arr Массив комплексных чисел, которые будут конвертированы
	 * @return конвертированный массив типа double[]
	 */
	public double[] convertToDoubleArray(Complex[] arr) {
		double[] result = new double[arr.length];
		
		for(int i=0;i<arr.length;i++)
			result[i] = arr[i].abs();
		
		return result;
	}
	
	/**
	 * Сформировать Excel файл. Необходим для проведения расчётов.
	 * @return Пусть к созданному файлу. Если произошла ошибка - null.
	 */
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
									"SUM(D1:D257)",
									"TREND(D1:D257, C1:C257, "+(this.max+this.delta)+", TRUE)"
									};
			
			for(int i=0;i<functionExcel.length;i++) {
				XSSFFormulaEvaluator formulaEvaluator = 
						(XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
				XSSFCell formulaCell = (XSSFCell) sheet.getRow(i).createCell(5);
				formulaCell.setCellFormula(functionExcel[i]);
				formulaEvaluator.evaluateFormulaCell(formulaCell);
			}
			
			averageValue 	= getThreeDecimal(sheet.getRow(0).getCell(5).getNumericCellValue());
			standardError 	= getThreeDecimal(sheet.getRow(1).getCell(5).getNumericCellValue());
			variance 		= getThreeDecimal(sheet.getRow(2).getCell(5).getNumericCellValue());
			median 			= getThreeDecimal(sheet.getRow(3).getCell(5).getNumericCellValue());
			minimumValue 	= getThreeDecimal(sheet.getRow(4).getCell(5).getNumericCellValue());
			maximumValue 	= getThreeDecimal(sheet.getRow(5).getCell(5).getNumericCellValue());
			amount 			= getThreeDecimal(sheet.getRow(6).getCell(5).getNumericCellValue());
			trend 			= getThreeDecimal(sheet.getRow(7).getCell(5).getNumericCellValue());
			
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
	
	/**
	 * Округлить вещественное число до 3-х знаком после запятой
	 * @param d Значение Double, которое необходимо округлить
	 * @return Новое округлённое до 3-х знаков после запятой значение
	 */
	public Double getThreeDecimal(Double d) {
		DecimalFormat df = new DecimalFormat("##.###");
		return Double.valueOf(df.format(d).replace(',', '.'));
	}
}
