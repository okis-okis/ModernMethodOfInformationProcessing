package okis.mmpi.lb1;

/**
 * Класс для определения линейной регрессии.
 * Исходные данные были взяты из open source.
 */
public class LinearRegression {
	
	/**
	 * Значение постоянной составляющей &alpha
	 */
    private final double intercept;
    
    /**
	 * Значение коэффициента &beta
	 */
    private final double slope;
    
    /**
     * коэффициент детерминации <em>R</em><sup>2</sup>, 
     * который является вещественным числом от 0 до 1
     */
    private final double r2;
    
    /**
     * стандартная ошибка оценки для постоянной составляющей &alpha
     */
    private final double svar0;
    
    /**
     * стандартная ошибка оценки коэффициента &beta
     */
    private final double svar1;

   /**
     * Выполняет линейную регрессию по точкам данных {@code (y[i], x[i])}.
     *
     * @param  x значения предикторной переменной
     * @param  y значения результата выполнения F(x)
     * @throws IllegalArgumentException если длины двух массивов не равны
     */
    public LinearRegression(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("array lengths are not equal");
        }
        int n = x.length;

        // первый проход
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int i = 0; i < n; i++) {
            sumx  += x[i];
            sumx2 += x[i]*x[i];
            sumy  += y[i];
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        // второй этап: вычисление сводной статистики
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        slope  = xybar / xxbar;
        intercept = ybar - slope * xbar;

        // дополнительный статистический анализ
        double rss = 0.0;      // остаточная сумма квадратов
        double ssr = 0.0;      // регрессионная сумма квадратов
        for (int i = 0; i < n; i++) {
            double fit = slope*x[i] + intercept;
            rss += (fit - y[i]) * (fit - y[i]);
            ssr += (fit - ybar) * (fit - ybar);
        }

        int degreesOfFreedom = n-2;
        r2    = ssr / yybar;
        double svar  = rss / degreesOfFreedom;
        svar1 = svar / xxbar;
        svar0 = svar/n + xbar*xbar*svar1;
    }

   /**
     * Возвращает <em>y</em>-постаянную составляющую &alpha; наилучшей из наиболее подходящих строк <em>y</em> = &alpha; + &beta; <em>x</em>.
     * @return <em>y</em> -постаянная составляющая &alpha; наиболее подходящей линии <em>y = &alpha; + &beta; x</em>
     */
    public double intercept() {
        return intercept;
    }

   /**
     * Возвращает коэффициент &beta; наилучшей из наиболее подходящих линий <em>y</em> = &alpha; + &beta; <em>x</em>.
     * @return Значение коэффициента &beta; Используется в следующей формуле: <em>y</em> = &alpha; + &beta; <em>x</em>
     */
    public double slope() {
        return slope;
    }

   /**
     * Возвращает коэффициент детерминации <em>R</em><sup>2</sup>.
     *
     * @return коэффициент детерминации <em>R</em><sup>2</sup>, 
     * который является вещественным числом от 0 до 1
     */
    public double R2() {
        return r2;
    }

   /**
     * Возвращает стандартную ошибку оценки для перехвата.
     * @return стандартная ошибка оценки для постоянной составляющей &alpha
     */
    public double interceptStdErr() {
        return Math.sqrt(svar0);
    }

   /**
     * Возвращает стандартную ошибку оценки коэффициента &beta
     * @return стандартная ошибка оценки коэффициента &beta
     */
    public double slopeStdErr() {
        return Math.sqrt(svar1);
    }

   /**
     * Возвращает ожидаемый ответ {@code y}, 
     * заданный значением переменной-предиктора {@code x}.
     *
     * @param  x значение предикторной переменной
     * @return ожидаемый ответ {@code y}, 
     * заданный значением переменной-предиктора {@code x}
     */
    public double predict(double x) {
        return slope*x + intercept;
    }

   /**
     * Возвращает строковое представление простой модели линейной регрессии.
     *
     * @return строковое представление простой модели линейной регрессии, 
     * включающее наиболее подходящую линию и коэффициент д
     * етерминации <em>R</em><sup>2</sup>
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%.2f n + %.2f", slope(), intercept()));
        s.append("  (R^2 = " + String.format("%.3f", R2()) + ")");
        return s.toString();
    }

}
