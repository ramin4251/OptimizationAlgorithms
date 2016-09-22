# ParticleSwarmOptimization-PSO-

This is a SCALA implementation of Particle Swarm Optimization (PSO) for a sample cost function of: f = sum(x.^2)

You can easily modify this function for your own desired functions.

Usage:
--------

You can import this project to the intellij IDE. Then some parameters need to be set for your problem:

npar = This is the number of optimizaton parameters                                         
fitnessFunc = This will contain the cost function you want to minimize                                       
varLow = This is Lower Band of search space                                                         
varHigh = This is Upper Band of search space                                                  

numOfParticles = This is number of particles to be used in searching the space                                
maxNumOfIterations = This is maximum number of optimzation iterations                         

Results 
--------
After the program is run and finished, a file titled "CostValues.txt" will be saved which contains cost minimization along different iterations.

Program itself will show the "Best Parameters" and "Minimum Cost Value" after all iterations are done in Scala Console.

