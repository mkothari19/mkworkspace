from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("Join Demo") \
    .getOrCreate()

emp_data = [(1, 21, "Manish", 120000.00), (2, 22, "Rahul", 80000.00), (3, 23, "Sachin", 50000.00),
            (4, 24, "Saurabh", 20000.00)]

dep_data = [(21, "HR"), (22, "Finance"), (23, "IT"),(25, "SALES")]

emp_schema = ["emp_id", "dept_id", "emp_name", "emp_salary"]
dept_schema = ["dept_id", "dept_name"]

emp_df = spark.createDataFrame(emp_data, emp_schema)
dept_df = spark.createDataFrame(dep_data, dept_schema)

inner_join_df = emp_df.join(dept_df, on="dept_id", how="inner")
right_join_df = emp_df.join(dept_df, on="dept_id", how="right")
outer_join_df = emp_df.join(dept_df, on="dept_id", how="outer")
cross_join_df = emp_df.crossJoin(dept_df)
semi_left_join_df = emp_df.join(dept_df, on="dept_id",how="left_semi")
left_anti_join_df = emp_df.join(dept_df, on="dept_id",how="left_anti")

inner_join_df.show()
right_join_df.show()
outer_join_df.show()
cross_join_df.show()
semi_left_join_df.show()
left_anti_join_df.show()