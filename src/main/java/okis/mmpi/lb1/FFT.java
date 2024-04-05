package okis.mmpi.lb1;

/**
 * Класс для работы с рядом Фурье.
 * Класс был взят из open source.
 */
public class FFT {

    /**
     * Вычисление ряда Фурье.
     * @param x Комплексные числа ряда, гармоники которого необходимо найти. 
     * Важно, чтобы длина массива была равна возведению 2 в степень n.
     * @return
     */
    public static Complex[] fft(Complex[] x) {
        int n = x.length;

        if (n == 1) return new Complex[] { x[0] };

        // Если не равно 2 в степени n
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        // Вычисление гармоник
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] evenFFT = fft(even);

        Complex[] odd  = even;
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] oddFFT = fft(odd);

        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = evenFFT[k].plus (wk.times(oddFFT[k]));
            y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));
        }
        return y;
    }


    /**
     * Обратное преобразование Фурье
     * @param x Комплексные значение гармоник, 
     * необходимых для обратного преобразования
     * @return Массив комплексных чисел, которые
     * являются результатом обратного преобразования
     */
    public static Complex[] ifft(Complex[] x) {
        int n = x.length;
        Complex[] y = new Complex[n];

        // сопряжение
        for (int i = 0; i < n; i++) {
            y[i] = x[i].conjugate();
        }

        // Вычисление ряда Фурье
        y = fft(y);

        // Деление на n
        for (int i = 0; i < n; i++) {
            y[i] = y[i].scale(1.0 / n);
        }

        return y;

    }

    /**
     * Вычисление значения DFT для x[] с помощью brute force (n ^ 2 раза)
     * @param x Комплексные числа, которые будут подвергнуты анализу
     * @return Гармоники, вычесленные при помощи метода DFT.
     */
    public static Complex[] dft(Complex[] x) {
        int n = x.length;
        Complex ZERO = new Complex(0, 0);
        Complex[] y = new Complex[n];
        for (int k = 0; k < n; k++) {
            y[k] = ZERO;
            for (int j = 0; j < n; j++) {
                int power = (k * j) % n;
                double kth = -2 * power *  Math.PI / n;
                Complex wkj = new Complex(Math.cos(kth), Math.sin(kth));
                y[k] = y[k].plus(x[j].times(wkj));
            }
        }
        return y;
    }
}
