import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object KMeans {
  type Point = (Double,Double)

  var centroids: Array[Point] = Array[Point]()
  
  
  def distance(a: Point, b: Point): Double = {
    
    var distance: Double = 0;
    
    // for (i <- 1 to 2){
    //   var  difference = a._i - b._i
    //   distance = distance + (scala.math.pow(difference, 2))
    // }
    var square_differnce1: Double = scala.math.pow(a._1 - b._1, 2)
    var square_differnce2: Double = scala.math.pow(a._2 - b._2, 2)
    distance = scala.math.pow((square_differnce1 + square_differnce2), 0.5)
    
    return distance
  }

  def main(args: Array[ String ]) {
    /* ... */
    val conf = new SparkConf().setAppName("KMeans")
    val sc = new SparkContext(conf)
    
    /* read initial centroids from centroids.txt */
    centroids = sc.textFile(args(1)).map(line => { val a = line.split(",")
                                                (a(0).toDouble,a(1).toDouble)}).collect
    
    var points = sc.textFile(args(0)).map(line => { val b = line.split(",")
                                                (b(0).toDouble,b(1).toDouble)})
    for ( i <- 1 to 5 ){
      val cs = sc.broadcast(centroids);
        // centroids = /* find new centroids using KMeans */
      centroids =  points.map { p => (cs.value.minBy(distance(p,_)), p)}
        .groupByKey().map { case (centroid, designated_points) =>
                          var count: Double = 0;
                          var sum1: Double = 0;
                          var sum2: Double = 0;
                          // Taking sum of all the x co-ordinates and y co-ordinates respectively
                          for (p <- designated_points){
                            sum1 += p._1;
                            sum2 += p._2;
                            count += 1;
                          }
                          var average1 = sum1/count
                          var average2 = sum2/count 
                          (average1,average2)
                          }.collect
    }                      
      centroids.foreach(println)
  }
}


