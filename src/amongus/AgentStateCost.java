

package amongus;

import frsf.cidisi.faia.solver.search.IStepCostFunction;
import frsf.cidisi.faia.solver.search.NTree;


public class AgentStateCost implements IStepCostFunction {

    /**
     * This method calculates the cost of the given NTree node.
     */
    @Override
    public double calculateCost(NTree node) 
    {
        ImpostorAgentState agentState = (ImpostorAgentState) node.getAgentState();
        
        return agentState.getDecisionCost();
    }
}
