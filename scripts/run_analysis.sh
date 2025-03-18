#!/bin/bash
echo "🔥 Starting Wildfire Analysis..."
mvn clean package
spark-submit \
  --class edu.ucr.cs.cs167.rcher012.wildfireAnalysis \
  --master local[*] \
  target/wildfire-analyzer-*.jar \
  "$@"