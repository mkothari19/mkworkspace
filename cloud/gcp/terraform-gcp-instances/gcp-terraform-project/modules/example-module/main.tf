resource "google_compute_instance" "example_instance" {
  name         = var.instance_name
  machine_type = var.machine_type
  zone         = var.zone

  boot_disk {
    initialize_params {
      image = var.image
    }
  }

  network_interface {
    network = var.network
    access_config {
      // Ephemeral IP
    }
  }

  metadata = {
    ssh-keys = var.ssh_keys
  }
}

resource "google_storage_bucket" "example_bucket" {
  name     = var.bucket_name
  location = var.bucket_location
  storage_class = var.storage_class
}