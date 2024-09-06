//package io.deeplay.camp;
//
//import io.deeplay.camp.bot.MyBots.UtilityFunction.ClassicUtilityFunction;
//
//import java.util.Random;
//
//public class CoefficientOptimizer {
//    private static final int POPULATION_SIZE = 50;
//    private static final int GENERATIONS = 100;
//    private static final double MUTATION_RATE = 0.1; // Вероятность мутации
//
//    private Random random = new Random();
//
//    public double[] optimizeCoefficients() {
//        double[][] population = initializePopulation();
//        for (int generation = 0; generation < GENERATIONS; generation++) {
//            double[] fitnessScores = evaluatePopulation(population);
//            population = selectAndBreed(population, fitnessScores);
//        }
//        return getBestCoefficients(population);
//    }
//
//    private double[][] initializePopulation() {
//        double[][] population = new double[POPULATION_SIZE][4];
//        for (int i = 0; i < POPULATION_SIZE; i++) {
//            population[i][0] = random.nextDouble() * 200; // Коэффициент для углов
//            population[i][1] = random.nextDouble() * 20; // Коэффициент для краев
//            population[i][2] = random.nextDouble() * 20; // Коэффициент для стабильных фишек
//            population[i][3] = random.nextDouble() * 2; // Коэффициент для счета
//        }
//        return population;
//    }
//
//    private double[] evaluatePopulation(double[][] population) {
//        double[] fitnessScores = new double[POPULATION_SIZE];
//        for (int i = 0; i < POPULATION_SIZE; i++) {
//            fitnessScores[i] = playGamesWithCoefficients(population[i]); // Ваша функция для оценки коэффициентов
//        }
//        return fitnessScores;
//    }
//
//    private double[][] selectAndBreed(double[][] population, double[] fitnessScores) {
//        double[][] newPopulation = new double[POPULATION_SIZE][4];
//        for (int i = 0; i < POPULATION_SIZE; i++) {
//            double[] parent1 = selectParent(population, fitnessScores);
//            double[] parent2 = selectParent(population, fitnessScores);
//            newPopulation[i] = crossover(parent1, parent2);
//            mutate(newPopulation[i]);
//        }
//        return newPopulation;
//    }
//
//    private double[] selectParent(double[][] population, double[] fitnessScores) {
//        double totalFitness = 0;
//        for (double score : fitnessScores) {
//            totalFitness += score; // Исправлено: сложение
//        }
//        double randomValue = random.nextDouble() * totalFitness;
//        double cumulative = 0;
//        for (int i = 0; i < population.length; i++) {
//            cumulative += fitnessScores[i]; // Исправлено: сложение
//            if (cumulative >= randomValue) {
//                return population[i];
//            }
//        }
//        return population[population.length - 1]; // На случай, если что-то пойдет не так
//    }
//
//    private double[] crossover(double[] parent1, double[] parent2) {
//        double[] child = new double[4];
//        for (int i = 0; i < child.length; i++) {
//            child[i] = random.nextBoolean() ? parent1[i] : parent2[i];
//        }
//        return child;
//    }
//
//    private void mutate(double[] coefficients) {
//        for (int i = 0; i < coefficients.length; i++) {
//            if (random.nextDouble() < MUTATION_RATE) {
//                coefficients[i] += random.nextGaussian(); // Добавляем случайное значение
//            }
//        }
//    }
//
//    private double[] getBestCoefficients(double[][] population) {
//        double bestScore = Double.NEGATIVE_INFINITY;
//        double[] bestCoefficients = null;
//        for (double[] coefficients : population) {
//            double score = playGamesWithCoefficients(coefficients); // Оценка текущих коэффициентов
//            if (score > bestScore) {
//                bestScore = score;
//                bestCoefficients = coefficients;
//            }
//        }
//        return bestCoefficients;
//    }
//
//    private double playGamesWithCoefficients(double[] coefficients) {
//        ClassicUtilityFunction utilityFunction = new ClassicUtilityFunction();
//        utilityFunction.setCornerCoef(coefficients[0]);
//        utilityFunction.setEdgesCoef(coefficients[1]);
//        utilityFunction.setStableDisksCoef(coefficients[2]);
//        utilityFunction.setScoreCoef(coefficients[3]); // Установите коэффициент для счета
//
//        SelfPlay selfPlay = new SelfPlay(100); // Запускаем 100 игр
//        selfPlay.startBotGame(); // Запускаем игры
//
//        // Вернем результат, например, количество побед первого бота
//        return selfPlay.getFirstWins(); // Или любой другой метод для получения результата
//    }
//
//    public static void main(String[] args) {
//        CoefficientOptimizer optimizer = new CoefficientOptimizer();
//        double[] bestCoefficients = optimizer.optimizeCoefficients();
//        System.out.println("Best coefficients found: ");
//        System.out.printf("Corners: %.2f, Edges: %.2f, Stable Discs: %.2f, Score: %.2f%n",
//                bestCoefficients[0], bestCoefficients[1], bestCoefficients[2], bestCoefficients[3]);
//    }
//}