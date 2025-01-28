variable "instance_type" {
  description = "The type of the compute instance."
  type        = string
  default     = "n1-standard-1"
}

variable "region" {
  description = "The region where resources will be provisioned."
  type        = string
  default     = "us-central1"
}

variable "zone" {
  description = "The zone where the compute instance will be created."
  type        = string
  default     = "us-central1-a"
}

variable "bucket_name" {
  description = "The name of the storage bucket."
  type        = string
}