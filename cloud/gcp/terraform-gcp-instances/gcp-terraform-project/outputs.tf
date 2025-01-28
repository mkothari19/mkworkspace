output "project_id" {
  value = google_project.project.project_id
}

output "bucket_name" {
  value = google_storage_bucket.bucket.name
}

output "instance_ip" {
  value = google_compute_instance.instance.network_interface[0].access_config[0].nat_ip
}