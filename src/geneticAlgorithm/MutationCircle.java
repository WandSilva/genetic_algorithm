package geneticAlgorithm;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XInt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MutationCircle extends Mutation {



    private static final List VALID_TYPES = Arrays.asList(IntSolutionType.class);
    private final double[] mutationIntervals;
    private double mutationProbability;

    public MutationCircle(HashMap<String, Object> parameters, double[] intervals) {
        super(parameters);
        this.mutationIntervals = intervals;

        if (parameters.get("probability") != null) {
            mutationProbability = (Double) parameters.get("probability");
        }
    }


    private void doMutation(Solution solution) throws JMException {
        XInt x = new XInt(solution);
        int pertubation, rand, tmp;
        for (int i = 0; i < solution.getDecisionVariables().length; i++) {

            if (PseudoRandom.randDouble() < mutationProbability) {

                pertubation = (int) Math.round(mutationIntervals[i] * x.getUpperBound(i));
                rand = PseudoRandom.randInt(-pertubation, pertubation);
                tmp = x.getValue(i) + rand;

                if (tmp < x.getLowerBound(i))
                    tmp = x.getLowerBound(i);
                else if (tmp > x.getUpperBound(i))
                    tmp = x.getUpperBound(i);

                x.setValue(i, tmp);
            } // if
        } // for
    } // doMutation


    @Override
    public Object execute(Object object) throws JMException {
        Solution solution = (Solution) object;

        if (!VALID_TYPES.contains(solution.getType().getClass())) {
            Configuration.logger_.severe("MutationCircle.execute: the solution " +
                    "is not of the right type. The type should be 'Int', but " +
                    solution.getType() + " is obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } // if


        this.doMutation(solution);
        return solution;
    }
}
