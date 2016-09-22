import java.io._

object PSO extends App {

  val npar = 5

  def fitnessFunc(x: Seq[Double]): Double = {
    val cost = x.map(n => math.pow(n, 2.0))
    cost.sum
  }

  val varLow = -5
  val varHigh = 5

  val c1 = 2
  val c2 = 4 - c1

  val numOfParticles = 100
  val maxNumOfIterations = 1000

  var costHist = Vector[Double]()

  var gbest: Seq[Double] = for {i <- 1 to npar} yield {
    (varHigh - varLow) * math.random + varLow
  }
  var gbestCost = fitnessFunc(gbest)

  val wmin = 0.4
  val wmax = 0.9
  val alpha = for {i <- 1 to npar} yield {
    math.random * (wmax - wmin) + wmin
  }

  // initialize the particles
  var particles = Vector[PSOparticles]()
  for (i <- 0 until numOfParticles) {
    particles = particles :+ new PSOparticles {
      override var velocity: Seq[Double] = for {i <- 1 to npar} yield { math.random }
      override var position: Seq[Double] = for {i <- 1 to npar} yield { (varHigh - varLow) * math.random + varLow }
      override var pbest: Seq[Double] = position
      override var pbestCost: Double = fitnessFunc(pbest)
    }

    if (particles(i).pbestCost < gbestCost) {
      gbest = particles(i).pbest
      gbestCost = particles(i).pbestCost
    }
  } // end of initialization

  // update cost history
  costHist = costHist :+ gbestCost

  val DampCoef = math.sqrt(0.99)
  var currentIteration = 1
  var cost = for {i <- 1 to numOfParticles} yield 0.0

  while (currentIteration < maxNumOfIterations) {

    for (k <- 0 until numOfParticles) {
      cost = cost.updated(k, fitnessFunc(particles(k).position))
    }

    // find pbest and gbest
    for (k <- 0 until numOfParticles) {
      if (cost(k) < particles(k).pbestCost) {
        particles(k).pbestCost = cost(k)
        particles(k).pbest = particles(k).position
      }
      if (cost(k) < gbestCost) {
        gbestCost = cost(k)
        gbest = particles(k).position
      }
    }

    // update cost history
    costHist = costHist :+ gbestCost
    // println(gbestCost)

    // update the velocity and position
    for (k <- 0 until numOfParticles) {
      // calculate velocity
      val inertia = for {i <- 0 until npar} yield {
        alpha(i) * particles(k).velocity(i)
      }
      val term1 = for {i <- 0 until npar} yield {
        c1 * math.random * (particles(k).pbest(i) - particles(k).position(i))
      }
      val term2 = for (i <- 0 until npar) yield {
        c2 * math.random * (gbest(i) - particles(k).position(i))
      }
      particles(k).velocity = for (i <- 0 until npar) yield {
        inertia(i) + term1(i) + term2(i)
      }

      // damp velocity
      particles(k).velocity = for {i <- 0 until npar} yield {
        particles(k).velocity(i) * DampCoef
      }

      // update position
      particles(k).position = for {i <- 0 until npar} yield {
        particles(k).position(i) + particles(k).velocity(i)
      }
    } // end update velocity

    // check to see if position is in range
    for (k <- 0 until numOfParticles) {
      // control upper band
      particles(k).position = for {n <- 0 until npar} yield {
        math.min(particles(k).position(n), varHigh)
      }
      // control lower band
      particles(k).position = for {n <- 0 until npar} yield {
        math.max(particles(k).position(n), varLow)
      }
    }

    currentIteration += 1
  } // end of while

  println("Best Answer = " + gbest)
  println("Minimum Cost = " + gbestCost)

  var writer = new PrintWriter(new File("CostValues.txt"))
  for (i <- costHist.indices){
    writer.write(costHist(i).toString)
    writer.write("\n")
    }
  writer.close()


}





