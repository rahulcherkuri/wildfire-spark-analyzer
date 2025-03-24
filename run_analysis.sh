#!/bin/bash
echo "🔥 Starting Wildfire Geospatial Analysis..."
mvn clean package -q
spark-submit \
  --class edu.ucr.cs.cs167.rcher012.wildfireAnalysis \
  --master local[*] \
  target/wildfire-analyzer-*.jar \
  "$@"