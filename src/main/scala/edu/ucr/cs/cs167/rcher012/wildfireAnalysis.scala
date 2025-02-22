package edu.ucr.cs.cs167.rcher012

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.SaveMode

/**
 * Wildfire Geospatial Analysis Engine
 * 
 * This application processes large-scale wildfire datasets to generate county-level
 * Fire Radiative Power (FRP) intensity maps for emergency response planning.
 * 
 * Features:
 * - Distributed processing of 140+ environmental variables per fire incident
 * - Geospatial joins with US county boundary data
 * - Shapefile output compatible with GIS systems (ArcGIS, QGIS)
 * - Optimized for emergency response time-critical scenarios
 * 
 * @author Rahul Cherkuri
 * @version 1.0
 */
object wildfireAnalysis {
  
  def main(args: Array[String]): Unit = {
    println("🔥 Wildfire Geospatial Analysis System")
    println("===========================================")
    
    // Validate command line arguments
    if (args.length != 1) {
      System.err.println("❌ Error: Invalid arguments")
      System.err.println("Usage: wildfireAnalysis <parquet-file-path>")
      System.err.println("Example: wildfireAnalysis samples/wildfiredb_sample.parquet")
      System.exit(1)
    }

    // Configure Spark for optimal geospatial processing
    val conf = new SparkConf()
      .setAppName("Wildfire Geospatial Analyzer")
      .set("spark.sql.adaptive.enabled", "true")           // Enable adaptive query execution
      .set("spark.sql.adaptive.coalescePartitions.enabled", "true")  // Optimize partition management
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")  // Faster serialization
    
    // Use local mode if no cluster is specified (development/testing)
    if (!conf.contains("spark.master")) {
      conf.setMaster("local[*]")
      println("📍 Running in local mode with all available cores")
    }

    // Initialize Spark session with error handling
    val spark: SparkSession = try {
      SparkSession.builder()
        .config(conf)
        .getOrCreate()
    } catch {
      case e: Exception =>
        System.err.println(s"❌ Failed to initialize Spark session: ${e.getMessage}")
        System.exit(1)
        null // This will never execute but satisfies compiler
    }

    try {
      // Load and validate wildfire dataset
      val inputFile: String = args(0)
      println(s"📂 Loading wildfire data from: $inputFile")
      
      val wildfireDF: DataFrame = spark.read.parquet(inputFile)
      
      // Validate dataset structure
      val requiredColumns = Seq("frp", "County")
      val missingColumns = requiredColumns.filterNot(wildfireDF.columns.contains)
      if (missingColumns.nonEmpty) {
        throw new IllegalArgumentException(s"Missing required columns: ${missingColumns.mkString(", ")}")
      }
      
      val recordCount = wildfireDF.count()
      println(s"✅ Successfully loaded $recordCount fire incidents")
      
      // Create temporary view for SQL operations
      wildfireDF.createOrReplaceTempView("wildfires")

      // Aggregate Fire Radiative Power by County
      // FRP measures wildfire intensity - critical for emergency resource allocation
      println("🔥 Calculating county-level Fire Radiative Power aggregations...")
      
      val countyAggregation = spark.sql("""
        SELECT 
          County,
          SUM(frp) AS total_frp,
          COUNT(*) AS fire_count,
          AVG(frp) AS avg_frp,
          MAX(frp) AS max_frp
        FROM wildfires 
        WHERE County IS NOT NULL 
          AND frp IS NOT NULL 
          AND frp > 0
        GROUP BY County
        ORDER BY total_frp DESC
      """)

      println("📊 Top 10 counties by Fire Radiative Power:")
      countyAggregation.show(10)

      // Load US County boundary data for geospatial integration
      val countiesShapefile = "samples/tl_2018_us_county.zip"
      println(s"🗺️  Loading county boundaries from: $countiesShapefile")
      
      val counties = spark.read
        .format("shapefile")  // Beast-Spark provides shapefile support
        .load(countiesShapefile)

      counties.createOrReplaceTempView("counties")
      println(s"✅ Loaded ${counties.count()} county boundaries")

      // Perform geospatial join between fire data and county geometries
      // This enables creation of choropleth maps for emergency visualization
      println("🔗 Performing geospatial join with county boundaries...")
      
      val geoJoinedResult = spark.sql("""
        SELECT 
          a.County, 
          a.total_frp, 
          a.fire_count,
          a.avg_frp,
          a.max_frp,
          b.geometry
        FROM (
          SELECT 
            County, 
            SUM(frp) AS total_frp,
            COUNT(*) AS fire_count,
            AVG(frp) AS avg_frp,
            MAX(frp) AS max_frp
          FROM wildfires
          WHERE County IS NOT NULL AND frp IS NOT NULL AND frp > 0
          GROUP BY County
        ) a
        LEFT JOIN counties b ON a.County = b.GEOID
        WHERE b.geometry IS NOT NULL
      """)

      val joinedCount = geoJoinedResult.count()
      println(s"✅ Successfully joined {joinedCount} counties with geometric data")

      // Export results as shapefile for GIS integration
      val outputPath = "wildfireIntensityCounty"
      println(s"💾 Exporting results to shapefile: $outputPath")
      
      geoJoinedResult
        .coalesce(1)  // Consolidate to single file for easier distribution
        .write
        .mode(SaveMode.Overwrite)  // Replace any existing output
        .format("shapefile")
        .save(outputPath)

      println("✅ Analysis complete! Generated outputs:")
      println(s"   📁 Shapefile: $outputPath/")
      println("   🎯 Ready for import into ArcGIS, QGIS, or other GIS systems")
      println("   🚨 Emergency responders can now visualize fire intensity by county")

    } catch {
      case e: Exception =>
        System.err.println(s"❌ Analysis failed: ${e.getMessage}")
        e.printStackTrace()
        System.exit(1)
    } finally {
      // Always clean up Spark resources
      println("🔧 Cleaning up Spark session...")
      spark.stop()
    }
  }
}
