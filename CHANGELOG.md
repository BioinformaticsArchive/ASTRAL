- version 4.7.8:
  - Fixed a bug in normalization of quartet scores. For very large datasets, the normalization of the quartet scores was incorrect. This only affected the outputted normalized score. 
    The tree topology, and the non-normalized scores were not affected. 

- version 4.7.7:
  - added a new option to filter genes by taxon occupancy

- version 4.7.6:
  - Made sure multi-allele works with new features added after 4.7.0 
  - Output normalized score with -q option

- Version 4.7.5:
  - Added an option to output the search space to standard output. (-k searchspace_norun) 	

- Version 4.7.4:
  - Adjust max polytomy size using sqrt of n
  - Misc refactoring and small bug fix (speed mostly)

- Version 4.7.3:
  - Resolving gene tree polytomies using a hybrid greedy/distance method
  - Adding to X by resolving polytomies using distances in addition to greedy
  - Changing -p1 and -p2 settings. -p2 is now much slower than -p1
  - Now -p1 is the default
  - Code refactoring
  - Some log changes
  - Removing the unnecessary global vertex cache (used up memory/time)
  
- Version 4.7.2:
  - Changed the way extra bipartitions are added for unresolved gene trees
  - Made some changes to the default strategy for addition of extra bipartitions (do not add incompatible bipartitions - these can increase running time drastically with little benefit)
  - Fixed a bug regarding addition of extra bipartitions using greedy (affects running time)
  - Change some logging statements. 
  - Some code refactoring
  
- Version 4.7.1:
  - Fixed bootstrapping to consume much less memory.
  - Do not cache weights. Consumes memory and there is barely any reuse.
  - New option to output completed gene trees
  - New option to just output bootstrap inputs and exit. 

- Version 4.7.0:
  - Changed how bipartitions from trees with missing data are completed. In new version, gene trees are completed based on a four point condition approach. 
  - Added a new mechanism based on greedy consensus for adding bipartitions to X
  - Added arbitrary resolutions of polytomies in input gene trees to set X
  - Optimization for scoring gene trees with polytomies (prune 0,0,0 sides)
  - Fixed to tree-based algorithm for score calculation (a bit slower for small and low ILS datasets)
  - Changed option p to have three values: 0 (no addition), 1 (addition by greedy), and 2 (addition by greedy and distance) 
  - Added option q to score a given species tree
  - Changed log messages a bit.
  - More code refactoring
  - Added normalized score
	 
- Version 4.6.3:
  - Fixed bug in calculation of the distance matrix for addition of extra bipartitions and completion of incomplete gene trees
  - Improved scalability of missing taxon completion algorithm (n^2 logn+n|X|^2)
  - Fixed numerical bugs related to very large number of taxa (e.g. 1000); `long` is now used everywhere
  - A bit of code refactoring
  - Adding more stderr logs
  - Added `-p` option to prevent addition of extra taxa
 
- Version 4.6.2:
  - Merge in 4.4.3 and 4.4.4 to multi-allele branch
  
- Version 4.6.1:
  - Merge in 4.4.2 changes to multi-allele branch
  
- Version 4.6.0:
  - Incorporate bug fix from 4.4.1
  
- Version 4.6.0:
  - Handle unresolved gene trees
    
- Version 4.5.1:
    - This version automatically adds extra bipartitions using a calculations based on quartet distances between two taxa 	

- Version 4.5.0:
  - ASTRAL can now handle multi individual gene trees

- Version 4.4.4:
  - Added a script to fix support values on output files that were incorrect from version 4.3.1 to 4.4.1

- Version 4.4.3:
  - Print bootstrap support as internal branch labels instead of branch length values
  - ** Bug fix**: on some machines greedy used to choke (see commit 807edf)
    
- Version 4.4.2:
  - **Bug Fix (IMPORTANT):** Support values drawn on the main tree were incorrect in previous versions since 4.3.1 (related to rerooting of trees). 
  - Prompts changed slightly 
 
- Version 4.4.1:
  - Print a user-friendly error when extra trees have taxa not in main trees.

- Version 4.4.0:
  - **Bug fix:** when gene trees had extreme levels of missing taxa, and there existed two taxa that never appeared together in the same gene tree, an uncaught division by zero could lead to wrong placement of one or both taxa. 
    
- Version 4.3.1:
  - Output consensus bootstrap tree as well
  - Output support only for internal branches
  - Fix default seed number to 692
  - Root all output trees *arbitrarily* on one taxon

- Version 4.3.0 (**command-line options changed!** Notice some commands are not backward compatible!):
  - Performs bootstrapping (`-b`, `-r`, `-g`, `-s` added)
  - Uses a library for argument parsing. We had to change some of the option names (exact version: `-xt` --> `-x`, extra trees: `-ex` --> `-e`).
  - Does not output the number of quartets satisfied by each branch; that information was not interpretable. 

- Version 4.2.1:
  - Read input gene trees with internal node labels
  - Updated the prompts and messages (e.g. Score instead of Cost)
  - Software outputs the version number too

- Version 4.2.0:
  - **Bug fix:** overflow happened for large number of genes.  Use long instead of int
  - Improve the command-line help a bit

- Version 4.1.1:
  - Added a hidden option to force algorithm used for estimation (only for debugging) 

- Version 4.1:
  - Updated the command-line so that ASTRAL algorithm is the default and DynaDup specific options are removed (-wq omitted and is default now)

- Version 4.0:
  - Added support for handling missing data by completing bipartitions
