function getLineChartWithTrend(xValues, yValues, trendValues, minVal, maxVal) {
  return {
    type: "line",
    data: {
      labels: xValues,
      datasets: [
        {
          label: "Функция f(x)",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(0,0,255,1.0)",
          borderColor: "rgba(0,0,255,0.1)",
          data: yValues,
        },
        {
          label: "Тренд",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(255,0,0,1.0)",
          borderColor: "rgba(255,0,0,0.1)",
          data: trendValues,
        },
      ],
    },
    options: {
      legend: { display: false },
      scales: {
        yAxes: [{ ticks: { min: minVal, max: maxVal } }],
      },
    },
  };
}

function getLineChart(xValues, sourceData, yValues, minVal, maxVal) {
  const errorData = [];

  for (let i = 0; i < sourceData.length; i++)
    errorData.push(sourceData[i] - yValues[i]);

  return {
    type: "line",
    data: {
      labels: xValues,
      datasets: [
        {
          label: "Исходный графки",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(255, 0, 0,1.0)",
          borderColor: "rgba(255, 0, 0,0.1)",
          data: sourceData,
        },
        {
          label: "Восстановленный график",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(0,0,255,1.0)",
          borderColor: "rgba(0,0,255,0.1)",
          data: yValues,
        },
        {
          label: "Значение ошибки",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(0,255,0,1.0)",
          borderColor: "rgba(0,255,0,0.1)",
          data: errorData,
        },
      ],
    },
    options: {
      legend: { display: false },
      scales: {
        yAxes: [{ ticks: { min: minVal, max: maxVal } }],
      },
    },
  };
}

function getWaveletLineChart(xValues, sourceData, arrReco, minVal, maxVal) {
  return {
    type: "line",
    data: {
      labels: xValues,
      datasets: [
        {
          label: "Исходный графки",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(255, 0, 0,1.0)",
          borderColor: "rgba(255, 0, 0,0.1)",
          data: sourceData,
        },
        {
          label: "Восстановленный график",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(0,255,0,1.0)",
          borderColor: "rgba(0,255,0,0.1)",
          data: arrReco,
        },
      ],
    },
    options: {
      legend: { display: false },
      scales: {
        yAxes: [{ ticks: { min: minVal, max: maxVal } }],
      },
    },
  };
}

function getPointChart(xValues, yValues) {
  let xyValues = [];
  for (let i = 0; i < xValues.length; i++) {
    xyValues.push({ x: xValues[i], y: yValues[i] });
  }

  return {
    type: "scatter",
    data: {
      datasets: [
        {
          label: "Полиномиальная регрессия",
          pointRadius: 4,
          pointBackgroundColor: "rgb(0,0,255)",
          data: xyValues,
        },
      ],
    },
    options: {
      legend: { display: false },
      scales: {
        xAxes: [{ ticks: { min: 0, max: 256 } }],
        yAxes: [{ ticks: { min: 0, max: 256 } }],
      },
    },
  };
}

function getHarmonicsChart(dataVal) {
  const labels = [];

  for (let i = 0; i < dataVal.length; i++) {
    labels.push(i);
  }

  const data = {
    labels: labels,
    datasets: [
      {
        label: "Ряд Фурье",
        data: dataVal,
        backgroundColor: ["rgba(54, 162, 235, 0.2)"],
        borderColor: ["rgb(54, 162, 235)"],
        borderWidth: 1,
      },
    ],
  };

  const config = {
    type: "bar",
    data: data,
    options: {
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  };

  return config;
}

function getTriangularChart(labels, dataVal, dataVal2, minVal, maxVal) {
  return {
    type: "line",
    data: {
      labels: labels,
      datasets: [
        {
          label: "Треугольное значение",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(255, 0, 0,1.0)",
          borderColor: "rgba(255, 0, 0,0.1)",
          data: dataVal,
        },
        {
          label: "Треугольное значение 2",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(0, 255, 0,1.0)",
          borderColor: "rgba(0, 255, 0,0.1)",
          data: dataVal2,
        },
      ],
    },
    options: {
      legend: { display: false },
      scales: {
        yAxes: [{ ticks: { min: minVal, max: maxVal } }],
      },
    },
  };
}

function getTriangularOperationChart(labels, dataVal, minVal, maxVal) {
  return {
    type: "line",
    data: {
      labels: labels,
      datasets: [
        {
          label: "Результат операции",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(255, 0, 0,1.0)",
          borderColor: "rgba(255, 0, 0,0.1)",
          data: dataVal,
        },
      ],
    },
    options: {
      legend: { display: false },
      scales: {
        yAxes: [{ ticks: { min: minVal, max: maxVal } }],
      },
    },
  };
}

function getPredictChart(labels, dataVal, dataVal2, minVal, maxVal) {
  return {
    type: "line",
    data: {
      labels: labels,
      datasets: [
        {
          label: "Исходный график",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(255, 0, 0,1.0)",
          borderColor: "rgba(255, 0, 0,0.1)",
          data: dataVal,
        },
        {
          label: "Предсказание",
          fill: false,
          lineTension: 0,
          backgroundColor: "rgba(0, 255, 0,1.0)",
          borderColor: "rgba(0, 255, 0,0.1)",
          data: dataVal2,
        },
      ],
    },
    options: {
      legend: { display: false },
      scales: {
        yAxes: [{ ticks: { min: minVal, max: maxVal } }],
      },
    },
  };
}
