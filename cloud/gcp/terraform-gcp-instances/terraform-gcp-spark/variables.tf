variable "project_id" {
  description = "The ID of the GCP project"
  type        = string
}

variable "region" {
  description = "The region where the Dataproc cluster will be created"
  type        = string
  default     = "us-central1"
}

variable "cluster_name" {
  description = "The name of the Dataproc cluster"
  type        = string
}

variable "master_machine_type" {
  description = "The machine type for the master node"
  type        = string
  default     = "n1-standard-2"
}

variable "worker_machine_type" {
  description = "The machine type for the worker nodes"
  type        = string
  default     = "n1-standard-2"
}

variable "num_workers" {
  description = "The number of worker nodes in the Dataproc cluster"
  type        = number
  default     = 2
}