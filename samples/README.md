# Sample Data Directory

This directory contains sample wildfire datasets and geospatial boundary files for testing the analysis pipeline.

## Available Files

### Wildfire Data
- `wildfiredb_sample.parquet` - Small sample dataset (5KB) for quick testing
- `wildfiredb_1k.csv` - 1,000 fire incidents in CSV format (1.3MB)
- `wildfiredb_1k.csv.bz2` - Compressed version of 1K dataset (249KB)
- `wildfiredb_10k.csv.bz2` - 10,000 fire incidents compressed (2.4MB)

### US County Boundaries
- `tl_2018_us_county.cpg` - Shapefile codepage
- `tl_2018_us_county.dbf` - Attribute data (948KB)
- `tl_2018_us_county.prj` - Projection information
- `tl_2018_us_county.shx` - Shape index (26KB)
- `tl_2018_us_county.shp.*.xml` - Metadata files

## Large Files (Download Separately)

Due to GitHub file size limits, larger datasets are excluded from the repository:

- `tl_2018_us_county.zip` (75MB) - Complete US county boundaries
- `tl_2018_us_county.shp` (121MB) - Uncompressed shapefile
- `wildfiredb_100k.csv.bz2` (24MB) - 100,000 fire incidents

**Download from:** [US Census Tiger/Line Shapefiles](https://www.census.gov/geographies/mapping-files/time-series/geo/tiger-line-file.html)

## Usage

```bash
# Test with small sample
./run_analysis.sh samples/wildfiredb_sample.parquet

# Process 1K incidents
./run_analysis.sh samples/wildfiredb_1k.csv
```

## Data Sources

- **Wildfire Data**: MODIS/VIIRS satellite fire detection products
- **County Boundaries**: US Census Bureau TIGER/Line Shapefiles (2018)
- **Environmental Variables**: Topographic, vegetation, and weather data integrated per incident