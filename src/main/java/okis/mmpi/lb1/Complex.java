package okis.mmpi.lb1;

import java.util.Objects;

/**
 * Класс для работы с комплексными числами. 
 * Данный класс был взят из open source.
 */
public class Complex {
	/**
	 * Реальная часть
	 */
    private final double re;
    
    /**
     * Мнимая часть
     */
    private final double im;

    /**
     * Конструктор класса
     * @param real Реальная часть
     * @param imag Мнимая часть
     */
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    /**
     * Возвращает строковое представление вызывающего комплексное значение
     * @return Комплексное число преобразованное в строку
     */
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    /**
     * Модуль комплексного числа
     * @return Возвращает значение sqrt(x<sup>2</sup> +y<sup>2</sup>) 
     * без промежуточного переполнения или недопотока.
     * @see Meth.hypot()
     */
    public double abs() {
        return Math.hypot(re, im);
    }

    /**
     * @return Возвращает фазу в промежутке от -pi до pi
     * @see Math.atan2()
     */
    public double phase() {
        return Math.atan2(im, re);
    }

    /**
     * Суммирование комплексных значений
     * @param b Комплексное число, которое суммируется к текущему значению
     * @return новое комплексное число, значение которого равно (текущее + b)
     */
    public Complex plus(Complex b) {
        Complex a = this;
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    /**
     * Вычитание комплексных значений
     * @param b Комплексное число, которое будет вычтено от текущего значения
     * @return новое комплексное число, значение которого равно (текущее - b)
     */
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    /**
     * Операция умножения комплексных значений
     * @param b Комплексное число, которое будет умножено на текущее значение
     * @return новое комплексное число, значение которого равно (текущее * b)
     */
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    /**
     * Умножить комплексное значение на число alpha
     * @param alpha Значение, на которое будет умножена реальная и мнимая часть комплексного значения.
     * @return новое комплексное значение, которое равно (alpha * реальную часть, alpha * мнимую часть)
     */
    public Complex scale(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    /**
     * @return новое комплексное значение, которое сопряжено с текущим
     */
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    /**
     * @return новое комплексное число, значения которого обратны текущему комплексному числу
     */
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    /**
     * @return возвращает реальную часть комплексного числа
     */
    public double re() { return re; }
    
    /**
     * @return возвращает мнимаю часть комплексного числа
     */
    public double im() { return im; }

    /**
     * Деление комплексных чисел
     * @param b Комплексное число, которое будет поделено на текущее
     * @return новое комплексное число, которое делится на текущее (текущее / b)
     */
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    /**
     * @return возвращает новое комплексное значение, которое является комплексной 
     * экспонентой текущего значения
     */
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    /**
     * @return возвращает новое комплексное чило, которое являетя комплексной 
     * синусоидой текущего значения
     */
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    /**
     * @return возвращает новое комплексное чило, которое являетя комплексной 
     * косинусоидой текущего значения
     */
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    /**
     * @return возвращает новое комплексное чило, которое являетя комплексной 
     * тангенсоидой текущего значения
     */
    public Complex tan() {
        return sin().divides(cos());
    }

    //---------Статические функции------------

    /**
     * Статическая операция суммирования комплексных чисел
     * @param a Первое комплексное число
     * @param b Второе комплексное число
     * @return Новое комплексное число, которое является результатом
     * суммирования a и b
     */
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }

    /**
     * Статическая операция сравнения
     * @param x Объект, с которым будет происходить сравнение
     * @return  Логическое значение, 
     * где true - комплексные значения равны,
     * а false - комплексные значения не равны.
     * Если объект x равен null, то будет возвращено значение false.
     */
    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }

    /**
     * @return hash значение данного объекта
     */
    public int hashCode() {
        return Objects.hash(re, im);
    }
}