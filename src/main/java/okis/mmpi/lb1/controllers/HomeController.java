package okis.mmpi.lb1.controllers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: OKis
 * Group:  SCS-23m
 * Organization: DSTU
 * Language: Java
 * Language version: 17 (maven project)
 * Frameworks: Spring
 * Date: 30.01.2024
 * Revision: (1) - 04.01.2024
 * 			 (2) - 05.01.2024
 * 			 (3) - 06.01.2024
 */

import java.util.List;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.jenetics.DoubleChromosome;
import io.jenetics.DoubleGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;
import jwave.Transform;
import jwave.transforms.DiscreteFourierTransform;
import jwave.transforms.FastWaveletTransform;
import jwave.transforms.wavelets.haar.Haar1;
import okis.mmpi.lb1.Complex;
import okis.mmpi.lb1.LinearRegression;
import okis.mmpi.lb1.Node;
import okis.mmpi.lb1.Statistic;

@Controller
@RequestMapping("/")
public class HomeController {
	
	Double[] gaApproximationY;
	
	Statistic stat;
	
	@GetMapping
	public ModelAndView getHomePage() {
		
		//Установить стандартные значения исходных переменных
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		
		mv.addObject("min", 0.0);   
		mv.addObject("max", 51.2); 
		mv.addObject("delta", 0.2); 
		mv.addObject("n", 2); 
		mv.addObject("filterFourier", -10); 
		mv.addObject("averageValue", 0);
		mv.addObject("triangularStart", 5.2);
		mv.addObject("triangularCenter", 13.2);
		mv.addObject("triangularFinish", 20.0);
		
		mv.addObject("secondTriangularStart", 25.2);
		mv.addObject("secondTriangularCenter", 33.2);
		mv.addObject("secondTriangularFinish", 40.0);
		
		return mv;
	}
	
	@PostMapping
	public ModelAndView getCulcPage(
			@RequestParam(name = "min") Double min,
			@RequestParam(name = "max") Double max,
			@RequestParam(name = "delta") Double delta,
			@RequestParam(name = "n") int n,
			@RequestParam(name = "filterFourier") int filterFourier,
			@RequestParam(name = "triangularStart") float triangleStart,
			@RequestParam(name = "triangularCenter") float triangleCenter,
			@RequestParam(name = "triangularFinish") float triangleFinish,
			@RequestParam(name = "secondTriangularStart") float secondTriangleStart,
			@RequestParam(name = "secondTriangularCenter") float secondTriangleCenter,
			@RequestParam(name = "secondTriangularFinish") float secondTriangleFinish) {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		
		mv.addObject("min", min);   
		mv.addObject("max", max); 
		mv.addObject("delta", delta); 
		mv.addObject("n", n);
		mv.addObject("filterFourier", filterFourier); 
		
		mv.addObject("triangularStart", triangleStart);
		mv.addObject("triangularCenter", triangleCenter);
		mv.addObject("triangularFinish", triangleFinish);
		
		mv.addObject("secondTriangularStart", secondTriangleStart);
		mv.addObject("secondTriangularCenter", secondTriangleCenter);
		mv.addObject("secondTriangularFinish", secondTriangleFinish);
		
		stat = new Statistic(min, max, delta, n);		
		
		setStatisticalData(mv);
		
		setHarmonicsData(mv);
		
		setLinearData(mv);
		
		setTriangularData(mv);
		
		setGeneticAlgorithmData(mv);
		
		setNeuralNetworkData(mv);
		
		return mv;
	}
	
	/**
	 * Установление статистических данных (лабораторная работа №1)
	 * @param mv Класс Model и View для MVC используется для занесения данных 
	 * (для отображения на странице)
	 */
	private ModelAndView setStatisticalData(ModelAndView mv) {
		String excelFile = stat.toExcelFile();
		Complex[] harmonics = stat.getHarmonicsComplex();
		Complex[] harmonicsFilter = stat.getHarmonicsArrayFilter(harmonics, (int)mv.getModel().get("filterFourier"));
		double[] restoredData = stat.getRestoredArray(harmonicsFilter);
		double[] restoredDataCorrect = new double[restoredData.length+1];
		
		mv.addObject("averageValue", stat.getAverageValue());
		mv.addObject("standardError", stat.getStandardError());
		mv.addObject("variance", stat.getVariance());
		mv.addObject("median", stat.getMedian());
		mv.addObject("minValue", stat.getMinimumValue());
		mv.addObject("maxValue", stat.getMaximumValue());
		mv.addObject("amount", stat.getAmount());
		mv.addObject("trend", stat.getTrend());
		
		List<Node> nodes = stat.getNodes();
		
		Double[] xVar = new Double[nodes.size()];
		double[] yVar = new double[nodes.size()];
		
		double[] trendVar = new double[nodes.size()];
		
		for(int i=0;i<xVar.length;i++) {
			xVar[i] = nodes.get(i).getX();
			yVar[i] = nodes.get(i).getFunctionValue();
			trendVar[i] = stat.getTrend()/xVar.length*i;
		}
		
		mv.addObject("nodes", nodes);
		mv.addObject("xArr", xVar);
		mv.addObject("yArr", yVar);
		mv.addObject("trendArr", trendVar);
		
		restoredDataCorrect[0] = ((Double)yVar[0]).intValue();
		
		for(int i=1;i<restoredData.length;i++)
			restoredDataCorrect[i] = restoredData[i-1];
		
		mv.addObject("excelFilePath", "files/"+excelFile); 
		
		return mv;
	}
	
	/**
	 * Установить данные и треугольных числах
	 * @param mv Класс Model и View для MVC используется для занесения данных 
	 * (для отображения на странице)
	 */
	private ModelAndView setTriangularData(ModelAndView mv) {
		List<Float> triangular 		= new ArrayList<Float>(),
				secondTriangular 	= new ArrayList<Float>();
		
		double entropy = triangularInit(triangular, 
				(float)mv.getModel().get("triangularStart"),
				(float)mv.getModel().get("triangularCenter"), 
				(float)mv.getModel().get("triangularFinish"), 
				(Double[])mv.getModel().get("xArr"));
		
		mv.addObject("triangular", triangular);
		mv.addObject("entropy", entropy);
		
		double secondEntropy = triangularInit(secondTriangular, 
				(float)mv.getModel().get("secondTriangularStart"),
				(float)mv.getModel().get("secondTriangularCenter"), 
				(float)mv.getModel().get("secondTriangularFinish"), 
				(Double[])mv.getModel().get("xArr"));
		
		mv.addObject("triangular", triangular);
		mv.addObject("entropy", entropy);
		
		mv.addObject("secondTriangular", secondTriangular);
		mv.addObject("secondEntropy", secondEntropy);
		
		List<Float> summTriangular = new ArrayList<Float>(),
					subTriangular = new ArrayList<Float>(),
					mulTriangular = new ArrayList<Float>(),
					divTriangular = new ArrayList<Float>();
		
		for(int i=0;i<triangular.size();i++) {
			summTriangular.add(triangular.get(i)+secondTriangular.get(i));
			subTriangular.add((float) (triangular.get(i)-secondTriangular.get(i)<0.0?0.0:triangular.get(i)-secondTriangular.get(i)));
			mulTriangular.add(triangular.get(i)*secondTriangular.get(i));
			divTriangular.add(triangular.get(i)/secondTriangular.get(i));
		}
		
		mv.addObject("summTriangular", summTriangular);
		mv.addObject("subTriangular", subTriangular);
		mv.addObject("mulTriangular", mulTriangular);
		mv.addObject("divTriangular", divTriangular);
		
		return mv;
	}
	
	/**
	 * Установить данные о гармониках (Ряд Фурье и Вейвлеты)
	 * @param mv Класс Model и View для MVC используется для занесения данных 
	 * (для отображения на странице)
	 */
	private ModelAndView setHarmonicsData(ModelAndView mv) {
		Transform t = new Transform( new DiscreteFourierTransform( ) );
		
		double[] yVar = (double[])mv.getModel().get("yArr");
		double[ ] arrFreq = t.forward( Arrays.copyOfRange(yVar, 0, yVar.length-1) ); // 1-D DFT forward
		
		double[ ] arrFilter = new double[arrFreq.length];
		
		for(int i=0;i<arrFilter.length;i++)
			arrFilter[i] = arrFreq[i]<((int)mv.getModel().get("filterFourier"))?0.0:arrFreq[i];
		
		double[ ] arrReco = t.reverse(arrFilter); // 1-D DFT reverse
		
		mv.addObject("harmonicsArray", arrFreq);
		mv.addObject("harmonicsArrayFilter", arrFilter);
		
		mv.addObject("restoredData", arrReco);
		
		double errorValue = 0.0;
		
		for(int i=0;i<arrReco.length;i++)
			errorValue += Math.pow(yVar[i] - arrReco[i], 2);
		
		errorValue /= (yVar.length+1);
		
		errorValue = Math.sqrt(errorValue);
		mv.addObject("errorValue", stat.getThreeDecimal(errorValue)); 
		
		Transform fwt = new Transform( new FastWaveletTransform( new Haar1( ) )  );
	
		double[ ] arrHilb = fwt.forward( Arrays.copyOfRange(yVar, 0, 256) );
	
		double[ ] arrWaveletReco = fwt.reverse( arrHilb );
		     
		mv.addObject("waveletReco", arrWaveletReco);
		
		return mv;
	}
	
	/**
	 * Установить данные о линейной регрессии
	 * @param mv Класс Model и View для MVC используется для занесения данных 
	 * (для отображения на странице)
	 */
	private ModelAndView setLinearData(ModelAndView mv) {
		
		List<Node> nodesM = stat.getNodes();
		
		double[] X = new double[nodesM.size()];
		double[] Y = new double[nodesM.size()];
		
		for(int i=9;i<nodesM.size();i+=10)
			nodesM.remove(i);
			
		
		for(int i=0;i<=224;i++) {
			X[i] = nodesM.get(i).getX();
			Y[i] = nodesM.get(i).getFunctionValue();
		}
		
		LinearRegression linear = new LinearRegression(X, Y);
		
		double[] xM = new double[nodesM.size()];
		double[] yM = new double[nodesM.size()];
		
		for(int i=0;i<nodesM.size();i++) {
			xM[i] = i*0.2;
			yM[i] = linear.predict(xM[i]);
		}
		
		mv.addObject("nodesM", nodesM);
		mv.addObject("yM", yM);
		mv.addObject("xM", xM);
		
		
		mv.addObject("linearSlope", linear.slope());
		mv.addObject("linearIntercept", linear.intercept());
		
		return mv;
	}
	
	/**
	 * Фитнесс функция для генетического алгоритма
	 * @param gt Массив с информацией о генотипе
	 * @return Значение силы (соответствия) текущего потомка
	 */
	private int eval(Genotype<DoubleGene> gt) {
    	
    	int result = 0;

    	for(int i=0;i<gt.chromosome().length();i++)
    		result += 500-(int)(Math.abs(gaApproximationY[i]-gt.chromosome().get(i).doubleValue())*100);
    	
        return result;
    }
	
	/**
	 * Установить данные о работе генетического алгоритма
	 * @param mv
	 */
	private ModelAndView setGeneticAlgorithmData(ModelAndView mv) {
		int arrayApproximationLength = 22;
		
		gaApproximationY = new Double[arrayApproximationLength];
		
		double[] yVar = (double[])mv.getModel().get("yArr");
		
		for(int i=0;i<arrayApproximationLength;i++) {
			gaApproximationY[i] = yVar[i*3];
		}
		
		// 1.) Определить генотип
        Factory<Genotype<DoubleGene>> gtf =
            Genotype.of(DoubleChromosome.of(-10.0, 10.0, gaApproximationY.length));
 
        // 3.) Создать двигатель для обработки данных
        Engine<DoubleGene, Integer> engine = Engine
            .builder(this::eval, gtf)
            .build();
 
        // 4.) Запуск популяция
        Genotype<DoubleGene> result = engine.stream()
            .limit(4000)
            .collect(EvolutionResult.toBestGenotype());
        
        List<Double> approximationGAResult = new ArrayList<Double>();
        
        for(int repeat = 0; repeat<4;repeat++) {
	        for(int i=0;i<result.chromosome().length()-1;i++) {
	        	for(int offset=0;offset<3;offset++)
	        		approximationGAResult.add(result.chromosome().get(i).doubleValue()+((result.chromosome().get(1+i).doubleValue()-result.chromosome().get(i).doubleValue())/5*offset));
	        }
        }
        
        double approximationError = 0.0;
        for(int i=0;i<gaApproximationY.length;i++)
        	approximationError += (gaApproximationY[i]-result.chromosome().get(i).doubleValue())/gaApproximationY[i];
        approximationError/=gaApproximationY.length;
        
        mv.addObject("approximationGAResult", approximationGAResult);
        mv.addObject("approximationGAError", approximationError);
        
        return mv;
	}
	
	private void setNeuralNetworkData(ModelAndView mv) {	
		double[] yArr = (double[]) mv.getModel().get("yArr");
		double[][] inputData = new double[yArr.length][1];
		
		for(int i=0;i<yArr.length;i++)
			inputData[i][0] = yArr[i];
			
		Double[] xArr = (Double[]) mv.getModel().get("xArr");
		
		double[][] outputData = new double[xArr.length][1];
		
		for(int i=0;i<xArr.length;i++)
			outputData[i][0] = xArr[i];
		
		MLDataSet testingSet = new BasicMLDataSet(inputData, outputData);
		
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationLinear(),true,1));
		
		//set activation function for hidden layer
		for(int i=0;i<2;i++)
			network.addLayer(new BasicLayer(new ActivationLinear(),true, 5));
		
		network.addLayer(new BasicLayer(new ActivationLinear(),false,1));
		network.getStructure().finalizeStructure();
		network.reset();
		
		MLDataSet trainingSet = new BasicMLDataSet(
				Arrays.copyOfRange(inputData, 0, 60), 
				Arrays.copyOfRange(outputData, 0, 60));
		
		// train the neural network
		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

		int epoch = 1;

		do {
			train.iteration();
			epoch++;
		} while(epoch<500);
		
		train.finishTraining();
		
		double[] neuralNetworkResult = new double[yArr.length];
		
		for(int i=0;i<testingSet.size();i++) {
			final MLData output = network.compute(testingSet.get(i).getInput());
			neuralNetworkResult[i] = 10-output.getData(0)*1.6-0.4;
		}
		
		mv.addObject("neuralNetworkResult", neuralNetworkResult);
	}

	/**
	 * Инициализация треугольного числа
	 * @param triangular Массив, в который будут занесены данные 
	 * 					 о треугольном числе
	 * @param start Точка начала треугольно числа
	 * @param center Точка центра треугольного числа
	 * @param finish Точка завершения треугольного числа
	 * @param labels 
	 * @return Энтропия треугольного числа
	 */
	public Double triangularInit(
			List<Float> triangular, 
			float start, 
			float center, 
			float finish, 
			Double[] labels) {	
		
		Double entropy = 0.0;
		
		for(int i=0;i<labels.length;i++) {
			if(labels[i]<=start || labels[i]>=finish) {
				triangular.add((float) 0.0);
			}else if(labels[i]<=center) {
				triangular.add((float) ((labels[i]-start)/(center-start)));
				entropy += triangular.get(triangular.size()-1)*Math.log(triangular.get(triangular.size()-1)/Math.log(2));
			}else if(labels[i]>center) {
				triangular.add((float) ((finish-labels[i])/(finish-center)));
				entropy -= triangular.get(triangular.size()-1)*Math.log(triangular.get(triangular.size()-1)/Math.log(2));
			}
		}
		
		return -2*entropy/labels.length;
	}
}
