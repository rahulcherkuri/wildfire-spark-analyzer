package edu.ucr.cs.cs167.rcher012

object AnalyticsQueries {
  val countyAggregation = """
    SELECT County, 
           SUM(frp) AS total_frp,
           COUNT(*) AS fire_count,
           AVG(frp) AS avg_frp
    FROM wildfires
    GROUP BY County
    ORDER BY total_frp DESC
  """
  
  val highIntensityFires = """
    SELECT County, latitude, longitude, frp, acq_date
    FROM wildfires 
    WHERE frp > 50.0
    ORDER BY frp DESC
  """
}