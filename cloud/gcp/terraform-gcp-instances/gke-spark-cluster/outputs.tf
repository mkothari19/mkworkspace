variable "cluster_name" {
  description = "The name of the GKE Spark cluster"
  type        = string
}

variable "cluster_endpoint" {
  description = "The endpoint of the GKE cluster"
  type        = string
}

output "gke_cluster_name" {
  value = var.cluster_name
}

output "gke_cluster_endpoint" {
  value = var.cluster_endpoint
}