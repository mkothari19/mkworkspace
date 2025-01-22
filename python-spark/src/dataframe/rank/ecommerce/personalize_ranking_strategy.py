from pyspark.sql import SparkSession
from pyspark.sql import functions as F
from pyspark.sql import Window

spark = SparkSession\
    .builder \
    .appName("PersonalizeRanking")\
    .getOrCreate()

# Sample interaction data
# Step-1 Data preparation
data = [
    (1, 101, "Purchase", "2025-01-20 14:05:00", 1),
    (2, 102, "View", "2025-01-20 15:30:00", 4),
    (3, 101, "Add-to-cart", "2025-01-21 11:20:00", 5),
]

columns = ["UserID", "ProductID", "Interaction", "Timestamp", "Rating"]
interactions_df = spark.createDataFrame(data, columns)
interactions_df.show()
interactions_weight = {"View": 1, "Purchase": 5, "Add-to-cart": 3}

# Step 2 : Assign wight to interactions

interactions_df = interactions_df.withColumn("InteractionWeight",
                                             F.when(F.col("Interaction") == "View", interactions_weight["View"])
                                             .when(F.col("Interaction") == "Purchase", interactions_weight["Purchase"])
                                             .when(F.col("Interaction") == "Add-to-cart",
                                                   interactions_weight["Add-to-cart"])
                                             .otherwise(0)
                                             )

# Step 3 : Aggregated score of user and product
user_product_score = interactions_df.groupBy("UserID", "ProductID").agg(F.sum("InteractionWeight").alias(
    "UserProductScore"))

# Step 4 : Rank of product per user

window_specs = Window.partitionBy("UserID").orderBy(F.col("UserProductScore").desc())
product_rank_per_user_df = user_product_score.withColumn("Rank",F.rank().over(window_specs))
product_rank_per_user_df.show()
