package okis.mmpi.lb1;

/**
* Author: OKis
* Group:  SCS-23m
* Organization: DSTU
* Language: Java
* Language version: 17 (maven project)
* Frameworks: Spring
* Date: 05.01.2024
* Revision: 10.01.2024 - добавление функции getTwoDecimal() 
* 			04.04.2024 - добавление комментариев в код
*/

import java.text.DecimalFormat;

/**
 * Класс используется представления данных о выполнения
 * функции в одном объекте. Удобен при отправке данных на frontend
 */
public class Node {
	/**
	 * Значение, которое является параметром в функции f(x)
	 */
	public Double x;
	/**
	 * Результат выполнения функции f(x)
	 */
	public Double functionValue;
	/**
	 * Идентификатор записи (необходим для визуализации списка)
	 */
	public int id;
	
	/**
	 * Класс используется представления данных о выполнения
	 * функции в одном объекте. Удобен при отправке данных на frontend
	 * @param id идентификатор записи 
	 * @param x Значение, которое является параметром в функции f(x)
	 * @param functionValue Результат выполнения функции f(x)
	 */
	public Node(int id, Double x, Double functionValue) {
		this.id = id;
		this.setX(x);
		this.setFunctionValue(functionValue);
	}
	
	/**
	 * Получение идентификатора записи
	 * @return возвращает идентификатор записи
	 */
	public int getId() {
		return id;
	}

	/**
	 * Установить идентификатор записи
	 * @param id новый идентификатор записи
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Получить значение, которое является параметром в функции f(x)
	 * @return значение, которое является параметром в функции f(x)
	 */
	public Double getX() {
		return x;
	}

	/**
	 * Установить новое значение, которое является параметром в функции f(x)
	 * @param x новое значение, которое является параметро в функции f(x)
	 */
	public void setX(Double x) {
		this.x = getTwoDecimal(x);
	}

	/**
	 * Получение значение, которое является результатом выполнения функции f(x)
	 * @return результат выполнения функции f(x)
	 */
	public Double getFunctionValue() {
		return functionValue;
	}

	/**
	 * Установить новое значение результата выполнения функции f(x)
	 * @param functionValue новое значение результата выполнения функции f(x)
	 */
	public void setFunctionValue(Double functionValue) {
		this.functionValue = getTwoDecimal(functionValue);
	}
	
	/**
	 * Функция округления значения до 2-х знаков после запятой
	 * @param d Значение, которое необходимо округлить
	 * @return Округлённое до двух знаков после запятой значение
	 */
	public Double getTwoDecimal(Double d) {
		DecimalFormat df = new DecimalFormat("##.##");
		return Double.valueOf(df.format(d).replace(',', '.'));
	}
}
