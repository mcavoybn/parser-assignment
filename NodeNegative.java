public class NodeMulop extends Node {

    private NodeExpr fact;

    public NodeNegative(NodeFact fact) {
		this.fact=fact;
    }

    public int op(int o1) throws EvalException {
        return 0 - o1;
    }

}
