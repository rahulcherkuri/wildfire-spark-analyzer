package edu.ucr.cs.cs167.rcher012

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types._

/**
 * Data loading utilities for wildfire analysis
 * Handles various data formats commonly used in geospatial research
 */
object DataLoader {
  
  def loadWildfireParquet(spark: SparkSession, path: String): DataFrame = {
    println(s"Loading wildfire data from: $path")
    try {
      val df = spark.read.parquet(path)
      println(s"Successfully loaded ${df.count()} records")
      df
    } catch {
      case e: Exception =>
        throw new RuntimeException(s"Failed to load parquet file: ${e.getMessage}", e)
    }
  }
  
  def loadCountyShapefile(spark: SparkSession, path: String): DataFrame = {
    println(s"Loading county boundaries from: $path")
    try {
      val df = spark.read.format("shapefile").load(path)
      println(s"Loaded ${df.count()} county geometries")
      df
    } catch {
      case e: Exception =>
        throw new RuntimeException(s"Failed to load shapefile: ${e.getMessage}", e)
    }
  }
  
  def loadWildfireCSV(spark: SparkSession, path: String): DataFrame = {
    println(s"Loading wildfire CSV data from: $path")
    spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(path)
  }
  
  def validateWildfireSchema(df: DataFrame): Boolean = {
    val requiredColumns = Seq("frp", "latitude", "longitude", "acq_date")
    val missingCols = requiredColumns.filterNot(df.columns.contains)
    
    if (missingCols.nonEmpty) {
      println(s"Warning: Missing required columns: ${missingCols.mkString(", ")}")
      false
    } else {
      println("✓ All required columns present")
      true
    }
  }
  
  def validateCountySchema(df: DataFrame): Boolean = {
    val requiredColumns = Seq("GEOID", "geometry")
    val missingCols = requiredColumns.filterNot(df.columns.contains)
    
    if (missingCols.nonEmpty) {
      println(s"Warning: Missing required columns: ${missingCols.mkString(", ")}")
      false
    } else {
      println("✓ County schema validated")
      true
    }
  }
}