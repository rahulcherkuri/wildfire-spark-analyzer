package edu.ucr.cs.cs167.rcher012

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * Spark session configuration utility for wildfire analysis workloads
 * Optimized for large-scale geospatial data processing
 */
object SparkConfig {
  
  def createSparkSession(appName: String = "Wildfire Analyzer"): SparkSession = {
    val conf = new SparkConf().setAppName(appName)
    
    // Default to local mode for development/testing
    if (!conf.contains("spark.master")) {
      conf.setMaster("local[*]")
    }
    
    // Performance optimizations for geospatial workloads
    conf.set("spark.sql.adaptive.enabled", "true")
    conf.set("spark.sql.adaptive.coalescePartitions.enabled", "true")
    conf.set("spark.sql.adaptive.skewJoin.enabled", "true")
    
    // Memory optimization for large datasets
    conf.set("spark.sql.execution.arrow.pyspark.enabled", "true")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    
    SparkSession.builder().config(conf).getOrCreate()
  }
  
  def createClusterSession(appName: String, masterUrl: String): SparkSession = {
    val conf = new SparkConf()
      .setAppName(appName)
      .setMaster(masterUrl)
      .set("spark.sql.adaptive.enabled", "true")
      .set("spark.sql.adaptive.coalescePartitions.enabled", "true")
    
    SparkSession.builder().config(conf).getOrCreate()
  }
}