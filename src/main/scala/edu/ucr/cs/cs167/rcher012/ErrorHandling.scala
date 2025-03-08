package edu.ucr.cs.cs167.rcher012

import org.apache.spark.sql.DataFrame

object ErrorHandling {
  def validateInput(path: String): Boolean = {
    new java.io.File(path).exists()
  }
  
  def validateDataFrame(df: DataFrame): Boolean = {
    df.count() > 0 && df.columns.nonEmpty
  }
  
  def handleMissingData(df: DataFrame): DataFrame = {
    df.filter("frp IS NOT NULL")
      .filter("latitude BETWEEN -90 AND 90")
      .filter("longitude BETWEEN -180 AND 180")
  }
}