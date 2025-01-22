from pyspark.sql import SparkSession
from pyspark.sql.window import Window
from pyspark.sql.functions import *

spark = SparkSession.builder \
    .appName("Rank function") \
    .getOrCreate()

# Sample data
data = [
    ("Maneesh", "HR", 5000),
    ("Bob", "IT", 6000),
    ("Cathy", "HR", 4500),
    ("David", "IT", 5500),
    ("Eve", "HR", 5000)
]
columns = ["Name", "Department", "Salary"]

emp_DF = spark.createDataFrame(data, columns)

# To calculate the rank of employees based on their salary within each department

# 1. Define window specs

window_specs = Window.partitionBy("Department").orderBy(col("Salary").desc())

# Apply rank function over window_specs
df_with_rank = emp_DF.withColumn("Rank", rank().over(window_specs))
df_with_rank.show()

# Apply dense rank :dense ranking (where ranks are sequential, even if there are ties)

df_with_dens_rank = emp_DF.withColumn("Dense_Rank", dense_rank().over(window_specs))

df_with_dens_rank.show()

# need a unique rank for each row (even when there are ties)

df_with_row_num = emp_DF.withColumn("Row_Number", row_number().over(window_specs))
df_with_row_num.show()

# Global ranking without using partitionBy

global_window_specs = Window.orderBy(col("Salary").desc())
df_global_rank_df=emp_DF.withColumn("Global_Rank",rank().over(global_window_specs))
df_global_rank_df.show()
