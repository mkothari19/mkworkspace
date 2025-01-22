"""Ranking Logic
Rank products using the following weighted formula:

Weight 1: Revenue (e.g., higher revenue = better rank)
Weight 2: Average Rating (e.g., 4.8 > 4.5)
Weight 3: Number of Reviews (e.g., 500 > 300)
Stock availability should ensure only "In Stock" products are ranked.
Example scoring formula:


Score=0.5×Revenue+0.3×AvgRating+0.2×NumReviews """

from pyspark.sql import SparkSession
from pyspark.sql import functions as F
from pyspark.sql import Window

spark = SparkSession \
    .builder \
    .appName("Ecommerse Rank Demo") \
    .getOrCreate()

# Sample dataset
data = [
    (101, "Product A", 5000, 4.5, 300, "Yes"),
    (102, "Product B", 3000, 4.8, 250, "No"),
    (103, "Product C", 7000, 4.2, 500, "Yes")
]

columns = ["ProductID", "ProductName", "Revenue", "AvgRating", "NumReviews", "StockAvailability"]

data_df = spark.createDataFrame(data, columns)
in_stock_df = data_df.filter(F.col("StockAvailability") == "Yes")

score_df = in_stock_df.withColumn("Score",0.5 * F.col("Revenue")+0.3*F.col("AvgRating")+0.2*F.col("NumReviews"))
score_df.show()
window_specs= Window.orderBy(F.col("Score").desc())

rank_df = score_df.withColumn("Rank",F.rank().over(window_specs))

rank_df.select("ProductID","ProductName","Score","Rank").show()


