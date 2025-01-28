resource "google_container_cluster" "gke_cluster" {
  name     = var.cluster_name
  location = var.region

  initial_node_count = var.initial_node_count

  node_config {
    machine_type = var.node_machine_type

    oauth_scopes = [
      "https://www.googleapis.com/auth/cloud-platform",
    ]
  }

  remove_default_node_pool = true

  dynamic "node_pool" {
    for_each = var.node_pools
    content {
      name       = node_pool.key
      node_count = node_pool.value.count

      node_config {
        machine_type = node_pool.value.machine_type

        oauth_scopes = [
          "https://www.googleapis.com/auth/cloud-platform",
        ]
      }
    }
  }
}

resource "google_project_iam_member" "gke_service_account" {
  project = var.project_id
  role    = "roles/container.admin"
  member  = "serviceAccount:${google_service_account.gke_service_account.email}"
}

resource "google_service_account" "gke_service_account" {
  account_id   = "gke-service-account"
  display_name = "GKE Service Account"
}

output "cluster_endpoint" {
  value = google_container_cluster.gke_cluster.endpoint
}

output "cluster_name" {
  value = google_container_cluster.gke_cluster.name
}