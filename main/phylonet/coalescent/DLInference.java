package phylonet.coalescent;


import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import phylonet.lca.SchieberVishkinLCA;
import phylonet.tree.model.TNode;
import phylonet.tree.model.Tree;
import phylonet.tree.model.sti.STITree;
import phylonet.tree.model.sti.STITreeCluster;
import phylonet.tree.model.sti.STITreeCluster.Vertex;

public class DLInference extends AbstractInference<STBipartition> {
	private int optimizeDuploss = 1; //one means dup, 3 means duploss
	//Map<STITreeCluster, Vertex> clusterToVertex;
	
	public DLInference(boolean rooted, boolean extrarooted, List<Tree> trees,
			List<Tree> extraTrees, boolean exactSolution, boolean duploss, boolean outputCompletedGenes) {
		super(rooted, extrarooted, trees, extraTrees, exactSolution, 0, outputCompletedGenes, false, true);
		this.optimizeDuploss = duploss ? 3 : 1;
	}

	public int getOptimizeDuploss() {
		return this.optimizeDuploss; 
	}
	
	protected int [] calc(Tree gtTree, SchieberVishkinLCA lcaLookup, Tree stTree) {
        int [] res = {0,0,0};
        Stack<TNode> stack = new Stack<TNode>();            
        for (TNode gtNode : gtTree.postTraverse()) {
            if (gtNode.isLeaf()) {
                    TNode node = stTree.getNode(GlobalMaps.taxonNameMap.getTaxonName(
                        gtNode.getName()));
                    if (node == null) {
                        throw new RuntimeException("Leaf " + gtNode.getName() +
                            " was not found in species tree; mapped as: "+
                            GlobalMaps.taxonNameMap.getTaxonName(gtNode.getName())); 
                    }
                    stack.push(node);
                //System.out.println("stack: " +this.taxonNameMap.getTaxonName(gtNode.getName()));
            } else {
                TNode rightLCA = stack.pop();
                TNode leftLCA = stack.pop();
                // If gene trees are incomplete, we can have this case
                if (rightLCA == null || leftLCA == null) {
                    stack.push(null);
                    continue;
                }
                TNode lca = lcaLookup.getLCA(leftLCA, rightLCA);
                stack.push(lca);
                if (lca == leftLCA || lca == rightLCA) {
                    // LCA in stTree dominates gtNode in gene tree
                    res[0]++;
                    if (lca == leftLCA && lca == rightLCA) {
                        res[1] += 0;
                    } else {
                        res[1] += (lca == leftLCA) ?
                                    d(rightLCA,lca) + 1:
                                    d(leftLCA,lca) + 1;
                    }
                } else {
                    res[1] += (d(rightLCA,lca) + d(leftLCA,lca));
                }
            }
        }
        TNode rootLCA = stack.pop();
        res[2] = res[1];
        res[1] += d(rootLCA,stTree.getRoot()) + (rootLCA == stTree.getRoot()?0:1);
        return res;
    }
	
	private int d(TNode down, TNode upp) {
	    int ret = 0;
	    TNode t = down;
	    //System.err.println("Down: "+down+"\nUPP: "+upp);
	    while (t != upp) {ret++; t=t.getParent();}
	    return Math.max(ret-1,0);
	}
	
	public void scoreGeneTree(Tree st) {
		// first calculated duplication cost by looking at gene trees. 
		
		SchieberVishkinLCA lcaLookup = new SchieberVishkinLCA(st);
		Integer duplications = 0;
		Integer losses = 0;
		Integer lossesstd = 0;
		
		for (Tree gtTree : this.trees) {
			int[] res = calc(gtTree,lcaLookup, st);
			duplications += res[0];
			losses += res[1];
			
			Tree hmst = new STITree(st);
			//hmst.constrainByLeaves(Arrays.asList(gtTree.getLeaves()));
			SchieberVishkinLCA hmlcaLookup = new SchieberVishkinLCA(hmst);
			int[] res2 = calc(gtTree,hmlcaLookup, hmst);
			
			lossesstd += res2[2];
		}
		System.out.println("Total number of duplications is: "+duplications);
		System.out.println("Total number of losses (bd) is: "+losses);
		System.out.println("Total number of losses (std) is: "+lossesstd);
		System.out.println("Total number of duploss (bd) is: " + (losses+duplications));
		System.out.println("Total number of duploss (st) is: " + (lossesstd+duplications));
		System.out.println("Total weighted (wd = "+this.getDLbdWeigth()+") loss is: " + (lossesstd + this.getDLbdWeigth()*(losses-lossesstd)));
	}


	Long getTotalCost(Vertex all) {
		return (long) (((DLDataCollection)this.dataCollection).sigmaNs - all._max_score);
	}


	@Override
	AbstractComputeMinCostTask<STBipartition> newComputeMinCostTask(AbstractInference<STBipartition> dlInference,
			Vertex all, IClusterCollection clusters) {
		return new DLComputeMinCostTask( (DLInference) dlInference, all,  clusters);
	}

	DLClusterCollection newClusterCollection() {
		return new DLClusterCollection(GlobalMaps.taxonIdentifier.taxonCount());
	}
	
	DLDataCollection newCounter(IClusterCollection clusters) {
		return new DLDataCollection(rooted, (DLClusterCollection)clusters);
	}

	@Override
	AbstractWeightCalculator<STBipartition> newWeightCalculator() {
		return new DLWeightCalculator(this);
	}

}
