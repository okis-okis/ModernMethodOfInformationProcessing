package okis.mmpi.lb1;

/**
 * Класс для определения треугольного числа
 */
public class Triangular {
	
	public float start, center, finish;
	
	public Triangular(float start, float center, float finish) {
		this.start = start;
		this.finish = finish;
		this.center = center;
	}
	
	/**
	 * Определение, является ли число треугольным
	 * @param num анализируемое число
	 * @return true - является, в противном случае false
	 */
    public boolean isTriangular(int num)
    {
        if (num < 0)
            return false;
     
        int c = (-2 * num);
        int b = 1, a = 1;
        int d = (b * b) - (4 * a * c);
     
        if (d < 0)
            return false;
     
        // Определение корней уравнения
        float root1 = ( -b + 
           (float)Math.sqrt(d)) / (2 * a);
            
        float root2 = ( -b - 
           (float)Math.sqrt(d)) / (2 * a);

        if (root1 > 0 && Math.floor(root1)
                                  == root1)
            return true;
     
        if (root2 > 0 && Math.floor(root2)
                                  == root2)
            return true;
     
        return false;
    }
}
