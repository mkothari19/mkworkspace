variable "project_id" {
  description = "The ID of the GCP project"
  type        = string
}

variable "region" {
  description = "The region to deploy resources"
  type        = string
  default     = "us-central1"
}

variable "zone" {
  description = "The zone to deploy resources"
  type        = string
  default     = "us-central1-a"
}

variable "instance_type" {
  description = "The type of the compute instance"
  type        = string
  default     = "n1-standard-1"
}

variable "bucket_name" {
  description = "The name of the storage bucket"
  type        = string
}