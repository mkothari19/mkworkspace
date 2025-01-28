variable "project_id" {
  description = "The ID of the GCP project"
  type        = string
}

variable "region" {
  description = "The region where the GKE cluster will be created"
  type        = string
  default     = "us-central1"
}

variable "cluster_name" {
  description = "The name of the GKE cluster"
  type        = string
}

variable "num_nodes" {
  description = "The number of nodes in the GKE cluster"
  type        = number
  default     = 3
}

variable "node_machine_type" {
  description = "The machine type for the nodes in the GKE cluster"
  type        = string
  default     = "e2-standard-4"
}

variable "enable_autoscaling" {
  description = "Enable autoscaling for the node pool"
  type        = bool
  default     = false
}

variable "min_nodes" {
  description = "Minimum number of nodes for autoscaling"
  type        = number
  default     = 1
}

variable "max_nodes" {
  description = "Maximum number of nodes for autoscaling"
  type        = number
  default     = 5
}