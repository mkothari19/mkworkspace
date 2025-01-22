from pyspark.sql import SparkSession
from pyspark.sql import functions as F
from pyspark.ml.recommendation import ALS

"""
For more complex personalization, use Collaborative Filtering 

"""

spark = SparkSession \
    .builder \
    .appName("PersonalizeRanking") \
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

# Step 1 :Prepare dataset  for ALS

als_data = interactions_df.select(
    F.col("UserID").cast("int"), F.col("ProductID").cast("int"),
    F.col("InteractionWeight").alias("Rating").cast("float"))

# Step 2: Initialize ALS model

als = ALS(userCol="UserID",
          itemCol="ProductID",
          ratingCol="Rating",
          numUserBlocks=2,
          coldStartStrategy="drop",
          nonnegative=True,
          intermediateStorageLevel="MEMORY_AND_DISK"
          )
# Step 3: Train  Model
model = als.fit(als_data)

# Generate top 3 product recommendations for each user
user_recommendation = model.recommendForAllUsers(3)

user_recommendation.show(truncate=False)
