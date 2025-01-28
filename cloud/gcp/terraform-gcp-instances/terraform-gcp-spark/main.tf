resource "google_dataproc_cluster" "spark_cluster" {
  name       = var.cluster_name
  region     = var.region
  project    = var.project_id

  cluster_config {
    master_config {
      num_instances = 1
      machine_type = var.master_machine_type
    }

    worker_config {
      num_instances = var.worker_count
      machine_type = var.worker_machine_type
    }

    software_config {
      image_version = "1.5-debian10"
      properties = {
        "dataproc:dataproc.allow.zero.workers" = "true"
      }
    }
  }
}

output "cluster_name" {
  value = google_dataproc_cluster.spark_cluster.name
}

output "cluster_endpoint" {
  value = google_dataproc_cluster.spark_cluster.endpoint
}