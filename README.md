# Wildfire Geospatial Analyzer

A distributed computing solution for wildfire analysis that processes large-scale environmental datasets to generate actionable intelligence for emergency response teams. Built during my CS167 coursework, this project demonstrates practical application of big data technologies to real-world emergency management challenges.

[![Apache Spark](https://img.shields.io/badge/Apache_Spark-3.5.4-E25A1C?style=flat&logo=apache-spark&logoColor=white)](https://spark.apache.org/)
[![Scala](https://img.shields.io/badge/Scala-2.12.18-DC322F?style=flat&logo=scala&logoColor=white)](https://scala-lang.org/)
[![Maven](https://img.shields.io/badge/Apache%20Maven-Build%20Tool-C71A36?style=flat&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![Beast](https://img.shields.io/badge/Beast--Spark-0.10.1-4ECDC4?style=flat)](https://beast.cs.ucr.edu/)
[![Java](https://img.shields.io/badge/Java-8+-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://openjdk.org/)

**Processing wildfire data at scale to save lives and protect communities.**

## 🎯 **Project Impact & Technical Achievements**
- **Distributed Processing**: Handles massive wildfire datasets (1000+ fire incidents with 140+ environmental features per record)
- **Geospatial Intelligence**: Integrates satellite data, topography, vegetation indices, and weather patterns
- **Real-world Application**: Generates county-level Fire Radiative Power intensity maps for emergency resource allocation
- **Scalable Architecture**: Spark-based pipeline processes multi-dimensional environmental data with automatic optimization

## 🏗️ **System Architecture**
```
Wildfire Dataset → Spark DataFrame → SQL Aggregation → Geospatial Join → Shapefile Export
        ↓                 ↓                ↓               ↓                    ↓
  Satellite Coords    • Distributed      • County-level    • GEOID Mapping    • ArcGIS-Compatible
  Fire Radiative      • Processing       • FRP             • Geometry         • Emergency Response  
  Power (FRP)         • 140+ Features    • Summation       • Integration      • Visualization
  Environmental       • Weather Data     • SQL Analytics   • Spatial Joins    • Decision Support
```

## Tech Stack & Architecture

### Core Technologies
- **Apache Spark 3.5.4** - Distributed computing engine for large-scale data processing
- **Scala 2.12.18** - Functional programming language optimized for JVM and big data
- **Apache Maven** - Dependency management and build automation
- **Beast-Spark 0.10.1** - Specialized library for geospatial operations in Spark environments

### Data Processing Pipeline
- **Apache Parquet** - Columnar storage format optimized for analytical queries
- **SparkSQL** - SQL interface for complex aggregations and joins
- **Distributed DataFrames** - In-memory data structures for parallel processing
- **Kryo Serialization** - High-performance object serialization

### Geospatial Components
- **Shapefile I/O** - Reading and writing ESRI shapefiles for GIS integration
- **Spatial Joins** - Geometric operations between point data and polygon boundaries
- **GEOID Mapping** - US Census geographic identifier linkage
- **Coordinate Systems** - Proper handling of geographic projections

## 🚀 **Key Features**
- ✅ **140+ Environmental Variables**: Topographic, vegetation, weather, and spatial analysis
- ✅ **County-Level Aggregation**: Administrative boundary integration for emergency planning
- ✅ **Fire Radiative Power Analysis**: Quantitative wildfire intensity measurement
- ✅ **Geospatial Integration**: Shapefile export for GIS systems (ArcGIS, QGIS)
- ✅ **Scalable Processing**: Linear performance scaling across Spark clusters

## 📥 **Installation Guide**

### Prerequisites
- **Java 8+** - Required for Spark and Scala
- **Apache Spark 3.4+** - Download from [spark.apache.org](https://spark.apache.org/downloads.html)
- **Apache Maven 3.6+** - For build management
- **Git** - For repository cloning

### Setup Instructions
```bash
# 1. Install Java (if not already installed)
# On MacOS with Homebrew:
brew install openjdk@8

# On Ubuntu/Debian:
sudo apt update && sudo apt install openjdk-8-jdk

# 2. Install Apache Spark
wget https://downloads.apache.org/spark/spark-3.4.0/spark-3.4.0-bin-hadoop3.tgz
tar -xvf spark-3.4.0-bin-hadoop3.tgz
export SPARK_HOME=$HOME/spark-3.4.0-bin-hadoop3
export PATH=$PATH:$SPARK_HOME/bin

# 3. Install Maven (if not already installed)
# On MacOS with Homebrew:
brew install maven

# On Ubuntu/Debian:
sudo apt install maven
```

## 🚦 **Getting Started**
```bash
# Clone repository
git clone https://github.com/rahulcherkuri/wildfire-geospatial-analyzer.git
cd wildfire-geospatial-analyzer

# Build with Maven
mvn clean package

# Run analysis (Option 1: Using provided script)
./run_analysis.sh samples/wildfiredb_sample.parquet

# Run analysis (Option 2: Direct spark-submit)
spark-submit \
  --class edu.ucr.cs.cs167.rcher012.wildfireAnalysis \
  --master local[*] \
  target/wildfire-analyzer-*.jar \
  samples/wildfiredb_sample.parquet
```

### Expected Output
```
🔥 Wildfire Geospatial Analysis System
Loading wildfire data from: samples/wildfiredb_sample.parquet
+-------+------------------+
| County|         total_frp|
+-------+------------------+
|  06037|         15234.67|
|  06059|         12456.78|
+-------+------------------+
Created shapefile: wildfireIntensityCounty
```

## 📊 **Performance Metrics**
- **Dataset Scale**: 1,000+ fire incidents with 140+ environmental features
- **Processing Speed**: County-level aggregation in under 60 seconds
- **Memory Efficiency**: Optimized Parquet columnar storage
- **Scalability**: Linear performance scaling across cluster nodes

---
