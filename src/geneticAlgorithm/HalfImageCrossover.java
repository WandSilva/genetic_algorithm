package geneticAlgorithm;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.operators.crossover.Crossover;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import model.Image;
import model.Images;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HalfImageCrossover extends Crossover {

    private static final List VALID_TYPES = Arrays.asList(BinarySolutionType.class,
            BinaryRealSolutionType.class,
            IntSolutionType.class);

    private Double crossoverProbability_ = null;
    private int halfPoint;

    public HalfImageCrossover(HashMap<String, Object> parameters, Images images) {
        super(parameters);
        this.halfPoint = images.getWidth() / 2;
        if (parameters.get("probability") != null)
            crossoverProbability_ = (Double) parameters.get("probability");
    }


    public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {
        int chromossomeSize = parent1.getDecisionVariables().length;
        int lastCOPoint1 = 0, lastCOPoint2 = 0;
        Solution[] offSpring = new Solution[2];
        offSpring[0] = new Solution(parent1);
        offSpring[1] = new Solution(parent2);
        try {
            if (PseudoRandom.randDouble() < probability) {
                //right circles
                for (int i = 0; i < chromossomeSize - 6; i += 6) {

                    if (parent1.getDecisionVariables()[i].getValue() < halfPoint) {
                        for (int j = 0; j <= 5; j++)
                            offSpring[0].getDecisionVariables()[lastCOPoint1 + j] = parent2.getDecisionVariables()[i + j].deepCopy();
                        lastCOPoint1 += 6;
                    }
                    if (parent2.getDecisionVariables()[i].getValue() < halfPoint) {
                        for (int j = 0; j <= 5; j++)
                            offSpring[1].getDecisionVariables()[lastCOPoint2 + j] = parent1.getDecisionVariables()[i + j].deepCopy();
                        lastCOPoint2 += 6;

                    }
                }
            }

            //left circles
            for (int z = 0; z < chromossomeSize - 6; z += 6) {

                if (parent1.getDecisionVariables()[z].getValue() >= halfPoint) {
                    for (int j = 0; j <= 5; j++)
                        offSpring[0].getDecisionVariables()[lastCOPoint1 + j] = parent1.getDecisionVariables()[z + j].deepCopy();
                    lastCOPoint1 += 6;

                }
                if (parent2.getDecisionVariables()[z].getValue() >= halfPoint) {
                    for (int j = 0; j <= 5; j++)
                        offSpring[1].getDecisionVariables()[lastCOPoint2 + j] = parent2.getDecisionVariables()[j + j].deepCopy();
                    lastCOPoint2 += 6;

                }
            }
        } catch (
                ClassCastException e1)

        {
            Configuration.logger_.severe("SinglePointCrossover.doCrossover: Cannot perfom " +
                    "SinglePointCrossover");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".doCrossover()");
        }


        return offSpring;
    }

    @Override
    public Object execute(Object object) throws JMException {
        Solution[] parents = (Solution[]) object;

        if (!(VALID_TYPES.contains(parents[0].getType().getClass()) &&
                VALID_TYPES.contains(parents[1].getType().getClass()))) {

            Configuration.logger_.severe("SinglePointCrossover.execute: the solutions " +
                    "are not of the right type. The type should be 'Int', but " +
                    parents[0].getType() + " and " +
                    parents[1].getType() + " are obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } // if

        if (parents.length < 2) {
            Configuration.logger_.severe("SinglePointCrossover.execute: operator " +
                    "needs two parents");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        Solution[] offSpring;
        offSpring = doCrossover(crossoverProbability_,
                parents[0],
                parents[1]);

       /* //-> Update the offSpring solutions
        for (int i = 0; i < offSpring.length; i++) {
            offSpring[i].setCrowdingDistance(0.0);
            offSpring[i].setRank(0);
        }*/
        return offSpring;
    } // execute


}
